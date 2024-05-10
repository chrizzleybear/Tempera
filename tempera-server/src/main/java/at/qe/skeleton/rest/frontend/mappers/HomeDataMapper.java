package at.qe.skeleton.rest.frontend.mappers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.repositories.ExternalRecordRepository;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.ProjectDto;
import at.qe.skeleton.rest.frontend.dtos.UserStateDto;
import at.qe.skeleton.rest.frontend.payload.response.HomeDataResponse;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import at.qe.skeleton.services.UserxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomeDataMapper {
  private UserxService userService;
  private TemperaStationService temperaService;
  private MeasurementService measurementService;
  private TimeRecordService timeRecordService;

  public HomeDataMapper(
      UserxService userService,
      TemperaStationService temperaService,
      MeasurementService measurementService,
      TimeRecordService timeRecordService) {
    this.userService = userService;
    this.temperaService = temperaService;
    this.measurementService = measurementService;
    this.timeRecordService = timeRecordService;
  }

  private List<ColleagueStateDto> mapUserToColleagueStateDto(Userx user) {
    List<Group> groups = user.getGroups();
    // todo: decide wether to show the whole crew here or just colleagues from same groups as user
    List<Userx> colleagues = groups.stream().map(Group::getMembers).flatMap(List::stream).toList();
    // List<Userx> colleagues = userService.getAllUsers().stream().toList();
    List<UserStateDto> userStateDtos = userService.getUserWithStates(colleagues);
    var colleagueStates = new ArrayList<ColleagueStateDto>();

    for (var colleague : userStateDtos) {
      State state = colleague.state();
      String username = colleague.username();
      String workplace;
      if (temperaService.findByUsername(username).isEmpty()) {
        throw new RuntimeException("User has no temperaStation assigned");
      }
      TemperaStation temperaStation =
          temperaService.findByUsername(username).get();
      if (temperaStation.isEnabled()){
        workplace = temperaStation.getAccessPoint().getRoom().toString();
        colleagueStates.add(new ColleagueStateDto(username, workplace, state));
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
  public HomeDataResponse mapUserToHomeDataResponse(Userx user) {
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
    // we can just grap the first one since the external and internal record should not be finished
    // yet, therefor
    // there should only exist one corresponding internalRecord.
    Project project = externalRecord.getInternalRecords().get(0).getAssignedProject();
    ProjectDto projectDto = new ProjectDto(project.getId(), project.getDescription());

    HomeDataResponse homeDataResponse =
        new HomeDataResponse(
            temperature,
            humidity,
            irradiance,
            nmvoc,
            user.getStateVisibility(),
            user.getState(),
            stateTimestamp,
            projectDto,
            colleagueStateDtos);
    return homeDataResponse;
  }
}
