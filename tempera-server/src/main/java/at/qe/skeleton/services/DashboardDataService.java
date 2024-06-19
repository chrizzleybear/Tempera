package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.MissingTemperaStationException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.*;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.GroupxProjectMapper;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDashboardDataRequest;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DashboardDataService {

  private final UserxService userService;
  private final TemperaStationService temperaService;
  private final MeasurementService measurementService;
  private final TimeRecordService timeRecordService;
  private final ProjectService projectService;
  private final GroupxProjectMapper groupxProjectMapper;

  private final ThresholdService thresholdService;

  public DashboardDataService(
          TemperaStationService temperaService,
          MeasurementService measurementService,
          TimeRecordService timeRecordService,
          UserxService userxService,
          ProjectService projectService,
          GroupxProjectMapper groupxProjectMapper,
          ThresholdService thresholdService) {
    this.thresholdService = thresholdService;
    this.temperaService = temperaService;
    this.measurementService = measurementService;
    this.timeRecordService = timeRecordService;
    this.userService = userxService;
    this.projectService = projectService;
    this.groupxProjectMapper = groupxProjectMapper;
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
      TemperaStation temperaStation = temperaService.findByUsername(username).orElseThrow(() ->new MissingTemperaStationException("User has no temperaStation assigned"));

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
   * @param  username The username of the user for which the data should be mapped.
   * @return DashboardDataResponse If there are no existing measurements for this user, it will
   *     return null as FrontendMeasurementDto. If there is no existing * TimeRecord for this user, it will return
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

    Set<Threshold> thresholds = thresholdService.getThresholdsByUsername(username);

    FrontendMeasurementDto temperatureDto = measurementService.createFrontendMeasurementDto(temperature, thresholds, SensorType.TEMPERATURE);
    FrontendMeasurementDto humidityDto = measurementService.createFrontendMeasurementDto(humidity, thresholds, SensorType.HUMIDITY);
    FrontendMeasurementDto irradianceDto = measurementService.createFrontendMeasurementDto(irradiance, thresholds, SensorType.IRRADIANCE);
    FrontendMeasurementDto nmvocDto = measurementService.createFrontendMeasurementDto(nmvoc, thresholds, SensorType.NMVOC);

    Optional<ExternalRecord> externalRecordOptional =
        timeRecordService.findLatestExternalRecordByUser(user);
    String stateTimeStamp =
        externalRecordOptional
            .map(externalRecord -> externalRecord.getStart().toString())
            .orElse(null);

    Optional<InternalRecord> internalRecordOptional =
            timeRecordService
                    .findLatestInternalRecordByUser(user);
    GroupxProject project = internalRecordOptional.map(InternalRecord::getGroupxProject).orElse(null);
    SimpleGroupxProjectDto projectDto = groupxProjectMapper.mapToDto(project);

    GroupxProject defaultGroupxProject = user.getDefaultGroupxProject();
    SimpleGroupxProjectDto defaultGxpDto = groupxProjectMapper.mapToDto(defaultGroupxProject);

    List<SimpleGroupxProjectDto> availableGxps =
        projectService.getSimpleGroupxProjectDtoByUser(user.getUsername()).stream().toList();

    return new DashboardDataResponse(
        temperatureDto,
        humidityDto,
        irradianceDto,
        nmvocDto,
        user.getStateVisibility(),
        user.getState(),
        stateTimeStamp,
        projectDto,
        defaultGxpDto,
        availableGxps,
        colleagueStateDtos);
  }


  @Transactional
  public MessageResponse updateUserVisibilityAndTimeStampProject(
      UpdateDashboardDataRequest request, Userx user) throws CouldNotFindEntityException {
    SimpleGroupxProjectDto gxpDto = request.groupxProject();

    InternalRecord internalRecord =
            timeRecordService
                    .findLatestInternalRecordByUser(user)
                    .orElseThrow(
                            () -> new CouldNotFindEntityException("No internal record found for user"));

    if (gxpDto != null) {
    GroupxProject groupxProject = projectService.findByGroupAndProject(Long.valueOf(gxpDto.groupId()), Long.valueOf(gxpDto.projectId()));
    groupxProject.addInternalRecord(internalRecord);
    projectService.saveGroupxProject(groupxProject);
    }

    user.setStateVisibility(request.visibility());
    userService.saveUser(user);

    return new MessageResponse("Dashboard data updated successfully!");
  }
}
