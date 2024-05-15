package at.qe.skeleton.rest.frontend.mappers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.services.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardDataMapper {

  private final UserxService userService;
  private final TemperaStationService temperaService;
  private final MeasurementService measurementService;
  private final TimeRecordService timeRecordService;
  private final ProjectService projectService;

  public DashboardDataMapper(
      UserxService userService,
      TemperaStationService temperaService,
      MeasurementService measurementService,
      TimeRecordService timeRecordService, UserxService userxService,
      ProjectService projectService) {
    this.temperaService = temperaService;
    this.measurementService = measurementService;
    this.timeRecordService = timeRecordService;
    this.userService = userxService;
    this.projectService = projectService;
  }

  private List<ColleagueStateDto> mapUserToColleagueStateDto(Userx user) {

    // using hashmap for faster compare algorithm
    Collection<Group> groups = user.getGroups();
    Set<Group> userGroups = new HashSet<>(groups);

    // we dont want user to be displayed as his own colleague
    List<Userx> colleagues = userService.getAllUsers().stream().filter(col -> !col.equals(user)).toList();

    var colleagueStates = new ArrayList<ColleagueStateDto>();


    for (var colleague : colleagues) {
      State state = colleague.getState();
      String username = colleague.getUsername();

      String workplace;
      if (temperaService.findByUsername(username).isEmpty()) {
        throw new RuntimeException("User has no temperaStation assigned");
      }
      TemperaStation temperaStation =
          temperaService.findByUsername(username).get();

      if (temperaStation.isEnabled()){
        workplace = temperaStation.getAccessPoint().getRoom().toString();

        // for each colleague, check if the user is in one of the groups of the colleague
        List<String> groupOverlap = new ArrayList<>();
        colleague.getGroups().forEach(
                colGroup -> {
                  if(userGroups.contains(colGroup)){
                    groupOverlap.add(colGroup.getName());
                  }
                }
        );
        // calculating whether user gets to see colleague state or not
        Visibility visibility = colleague.getStateVisibility();
        boolean isVisible = true;
        if(visibility == Visibility.HIDDEN) isVisible = false;
        if(groupOverlap.isEmpty() && visibility == Visibility.PRIVATE) isVisible = false;

        colleagueStates.add(new ColleagueStateDto(username, workplace, state, isVisible, groupOverlap));
      }
    }
    return colleagueStates;
  }

  /**
   * Maps a user to a HomeDataResponse object. This object contains all the data needed to display the
   * home screen of the frontend. This includes the current measurements, the current state of the user
   * itself and the states of the colleagues (if they are visible and their temperaStation is enabled).
   * @param user
   * @return
   */
  @Transactional
  public DashboardDataResponse mapUserToHomeDataResponse(Userx user) {
    var colleagueStateDtos = mapUserToColleagueStateDto(user);
    // next up: current measurements
    var sensors = user.getTemperaStation().getSensors();
    Sensor temperatureSensor =
        sensors.stream()
            .filter(sensor -> sensor.getSensorType().equals(SensorType.TEMPERATURE))
            .findFirst()
            .get();
    Sensor humiditySensor =
        sensors.stream()
            .filter(sensor -> sensor.getSensorType().equals(SensorType.HUMIDITY))
            .findFirst()
            .get();
    Sensor irradianceSensor =
        sensors.stream()
            .filter(sensor -> sensor.getSensorType().equals(SensorType.IRRADIANCE))
            .findFirst()
            .get();
    Sensor nmvocSensor =
        sensors.stream()
            .filter(sensor -> sensor.getSensorType().equals(SensorType.NMVOC))
            .findFirst()
            .get();

    double temperature =
        measurementService.findLatestMeasurementBySensor(temperatureSensor).get().getValue();
    double humidity =
        measurementService.findLatestMeasurementBySensor(humiditySensor).get().getValue();
    double irradiance =
        measurementService.findLatestMeasurementBySensor(irradianceSensor).get().getValue();
    double nmvoc = measurementService.findLatestMeasurementBySensor(nmvocSensor).get().getValue();

    ExternalRecord externalRecord = timeRecordService.findLatestExternalRecordByUser(user).get();
    LocalDateTime timeRecordStart = externalRecord.getStart();
    String stateTimestamp = timeRecordStart.toString();

    Project defaultProject = user.getDefaultProject();
    ProjectDto defaultProjectDto = new ProjectDto(defaultProject.getId(), defaultProject.getDescription());

    List<ProjectDto> projects =
        projectService.getProjectsByContributor(user.getUsername()).stream()
            .map(p -> new ProjectDto(p.getId(), p.getName()))
            .toList();

    return
        new DashboardDataResponse(
            temperature,
            humidity,
            irradiance,
            nmvoc,
            user.getStateVisibility(),
            user.getState(),
            stateTimestamp,
            defaultProjectDto,
            projects,
            colleagueStateDtos);
  }
}
