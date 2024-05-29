package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.ExtendedProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDashboardDataRequest;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
      TimeRecordService timeRecordService,
      UserxService userxService,
      ProjectService projectService) {
    this.temperaService = temperaService;
    this.measurementService = measurementService;
    this.timeRecordService = timeRecordService;
    this.userService = userxService;
    this.projectService = projectService;
  }

  private List<ColleagueStateDto> mapUserToColleagueStateDto(Userx user) {

    // using hashmap for faster compare algorithm
    Set<Groupx> userGroups = new HashSet<>(user.getGroups());

    // we dont want user to be displayed as his own colleague
    List<Userx> colleagues =
        userService.getAllUsers().stream().filter(col -> !col.equals(user)).toList();

    var colleagueStates = new ArrayList<ColleagueStateDto>();

    for (var colleague : colleagues) {
      State state = colleague.getState();
      String username = colleague.getUsername();

      String workplace;
      if (temperaService.findByUsername(username).isEmpty()) {
        throw new RuntimeException("User has no temperaStation assigned");
      }
      TemperaStation temperaStation = temperaService.findByUsername(username).get();

      if (temperaStation.isEnabled()) {
        workplace = temperaStation.getAccessPoint().getRoom().toString();

        // for each colleague, check if the user is in one of the groups of the colleague
        List<String> groupOverlap = new ArrayList<>();
        colleague
            .getGroups()
            .forEach(
                colGroup -> {
                  if (userGroups.contains(colGroup)) {
                    groupOverlap.add(colGroup.getName());
                  }
                });
        // calculating whether user gets to see colleague state or not
        Visibility visibility = colleague.getStateVisibility();
        boolean isVisible = true;
        if (visibility == Visibility.HIDDEN) isVisible = false;
        if (groupOverlap.isEmpty() && visibility == Visibility.PRIVATE) isVisible = false;

        colleagueStates.add(
            new ColleagueStateDto(username, workplace, state, isVisible, groupOverlap));
      }
    }
    return colleagueStates;
  }

  /**
   * Maps a user to a HomeDataResponse object. This object contains all the data needed to display
   * the home screen of the frontend. This includes the current measurements, the current state of
   * the user itself and the states of the colleagues (if they are visible and their temperaStation
   * is enabled).
   *
   * @param String username The username of the user for which the data should be mapped.
   * @return DashboardDataResponse If there are no existing measurements for this user, it will
   *     return null as values. If there is no existing * TimeRecord for this user, it will return
   *     null as stateTimeStamp. If there is no default project assigned to this user, * it will
   *     return null as defaultProject. If there are no projects assigned to this user, it will
   *     return an empty list as projects.
   */
  @Transactional
  public DashboardDataResponse mapUserToHomeDataResponse(String username) {
    Userx user = userService.loadUserDetailed(username);
    var colleagueStateDtos = mapUserToColleagueStateDto(user);
    // next up: current measurements
    var sensors = user.getTemperaStation().getSensors();
    Sensor temperatureSensor =
        sensors.stream()
            .filter(sensor -> sensor.getSensorType().equals(SensorType.TEMPERATURE))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No temperature sensor found"));
    Sensor humiditySensor =
        sensors.stream()
            .filter(sensor -> sensor.getSensorType().equals(SensorType.HUMIDITY))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No humidity sensor found"));
    Sensor irradianceSensor =
        sensors.stream()
            .filter(sensor -> sensor.getSensorType().equals(SensorType.IRRADIANCE))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No irradiance sensor found"));
    Sensor nmvocSensor =
        sensors.stream()
            .filter(sensor -> sensor.getSensorType().equals(SensorType.NMVOC))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No nmvoc sensor found"));

    Optional<Measurement> temperatureMeasurement =
        measurementService.findLatestMeasurementBySensor(temperatureSensor);
    Optional<Measurement> humidityMeasurement =
        measurementService.findLatestMeasurementBySensor(humiditySensor);
    Optional<Measurement> irradianceMeasurement =
        measurementService.findLatestMeasurementBySensor(irradianceSensor);
    Optional<Measurement> nmvocMeasurement =
        measurementService.findLatestMeasurementBySensor(nmvocSensor);

    Double temperature = temperatureMeasurement.map(Measurement::getValue).orElse(null);
    Double humidity = humidityMeasurement.map(Measurement::getValue).orElse(null);
    Double irradiance = irradianceMeasurement.map(Measurement::getValue).orElse(null);
    Double nmvoc = nmvocMeasurement.map(Measurement::getValue).orElse(null);

    Optional<ExternalRecord> externalRecordOptional =
        timeRecordService.findLatestExternalRecordByUser(user);
    String stateTimeStamp =
        externalRecordOptional
            .map(externalRecord -> externalRecord.getStart().toString())
            .orElse(null);

    GroupxProject defaultGroupxProject = user.getDefaultGroupxProject();
    SimpleGroupxProjectDto defaultGxpDto;
    if (defaultGroupxProject == null) {
      defaultGxpDto = null;
    } else {
      defaultGxpDto =
          new SimpleGroupxProjectDto(
                defaultGroupxProject.getGroup().getId().toString(),
                defaultGroupxProject.getGroup().getName(),
                defaultGroupxProject.getProject().getId().toString(),
                defaultGroupxProject.getProject().getName());
    }

    List<SimpleGroupxProjectDto> availableGxps =
        projectService.getSimpleGroupxProjectDtoByUser(user.getUsername()).stream().toList();

    return new DashboardDataResponse(
        temperature,
        humidity,
        irradiance,
        nmvoc,
        user.getStateVisibility(),
        user.getState(),
        stateTimeStamp,
        defaultGxpDto,
availableGxps,
            colleagueStateDtos);
  }


  @Transactional
  public MessageResponse updateUserVisibilityAndTimeStampProject(
      UpdateDashboardDataRequest request, Userx user) throws CouldNotFindEntityException {
    SimpleGroupxProjectDto gxpDto = request.groupxProject();

    InternalRecord record =
            timeRecordService
                    .findLatestInternalRecordByUser(user)
                    .orElseThrow(
                            () -> new CouldNotFindEntityException("No external record found for user"));

    if (gxpDto != null) {
    GroupxProject groupxProject = projectService.findByGroupAndProject(Long.valueOf(gxpDto.groupId()), Long.valueOf(gxpDto.projectId()));
    groupxProject.addInternalRecord(record);
    projectService.saveGroupxProject(groupxProject);
    }

    user.setStateVisibility(request.visibility());
    userService.saveUser(user);

    return new MessageResponse("Dashboard data updated successfully!");
  }
}
