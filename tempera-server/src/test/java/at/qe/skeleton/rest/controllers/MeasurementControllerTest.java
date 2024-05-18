package at.qe.skeleton.rest.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InconsistentObjectRelationException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.Unit;
import at.qe.skeleton.rest.raspberry.controllers.MeasurementController;
import at.qe.skeleton.rest.raspberry.dtos.MeasurementDto;
import at.qe.skeleton.rest.raspberry.mappers.MeasurementMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.TemperaStationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MeasurementControllerTest {

  MeasurementController measurementController;
  @Mock MeasurementService measurementService;
  @Mock MeasurementMapper measurementMapper;
  @Mock AccessPointService accessPointService;
  @Mock TemperaStationService temperaStationService;

  @BeforeEach
  void setUp() {
    measurementController =
        new MeasurementController(
            measurementService, measurementMapper, accessPointService, temperaStationService);
  }

  @AfterEach
  void tearDown() {
    Mockito.reset(measurementService, measurementMapper, accessPointService, temperaStationService);
  }

  @Test
  void createMeasurement() throws CouldNotFindEntityException, InconsistentObjectRelationException {
      UUID accessPointId = UUID.randomUUID();
    MeasurementDto measurementDto =
        new MeasurementDto(
            accessPointId, "tempera_station_id", LocalDateTime.now(), 30.0, 10.0, 19.8, 29.1);

    Measurement measurement =
        new Measurement(
            20.0,
            LocalDateTime.now(),
            new Sensor(
                SensorType.HUMIDITY,
                Unit.PERCENT,
                new TemperaStation("tempera_station_id", true, new Userx(), true)));
    List<Measurement> measurementList = List.of(measurement);
    when(accessPointService.isEnabled(measurementDto.access_point_id())).thenReturn(true);
    when(temperaStationService.isEnabled(measurementDto.tempera_station_id())).thenReturn(true);
    when(measurementMapper.mapFromDto(measurementDto)).thenReturn(measurementList);
    when(measurementService.saveMeasurement(measurement)).thenReturn(measurement);
    when(measurementMapper.mapToDto(measurementList)).thenReturn(measurementDto);

    ResponseEntity<MeasurementDto> response =
        measurementController.createMeasurement(measurementDto);

    assertEquals(200, response.getStatusCode().value());
    assertEquals(measurementDto, response.getBody());
    verify(measurementService, times(1)).saveMeasurement(measurement);
    verify(temperaStationService, times(1)).isEnabled("tempera_station_id");
    verify(accessPointService, times(1)).isEnabled(accessPointId);
    verify(measurementMapper, times(1)).mapFromDto(measurementDto);
    verify(measurementMapper, times(1)).mapToDto(measurementList);
  }

  // accessPoint is not enabled
  @Test
  public void createMeasurementTemperaStationNotEnabled() throws Exception{
      MeasurementDto measurementDto =
              new MeasurementDto(
                      UUID.randomUUID(), "tempera_station_id", LocalDateTime.now(), 30.0, 10.0, 19.8, 29.1);

      Measurement measurement =
              new Measurement(
                      20.0,
                      LocalDateTime.now(),
                      new Sensor(
                              SensorType.HUMIDITY,
                              Unit.PERCENT,
                              new TemperaStation("tempera_station_id", false, new Userx(), true)));
      List<Measurement> measurementList = List.of(measurement);
      when(accessPointService.isEnabled(measurementDto.access_point_id())).thenReturn(true);
      when(temperaStationService.isEnabled(measurementDto.tempera_station_id())).thenReturn(false);
      when(measurementMapper.mapFromDto(measurementDto)).thenReturn(measurementList);
      when(measurementService.saveMeasurement(measurement)).thenReturn(measurement);
      when(measurementMapper.mapToDto(measurementList)).thenReturn(measurementDto);

      ResponseEntity<MeasurementDto> response =
              measurementController.createMeasurement(measurementDto);

      assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
      assertEquals(null, response.getBody());

      verify(temperaStationService, times(1)).isEnabled("tempera_station_id");
      verify(measurementService, times(0)).saveMeasurement(measurement);
      verify(measurementMapper, times(0)).mapFromDto(measurementDto);
      verify(measurementMapper, times(0)).mapToDto(measurementList);
  }

  @Test
  public void createMeasurementAccessPointNotEnabled() throws Exception{
      UUID accessPointId = UUID.randomUUID();
      MeasurementDto measurementDto =
              new MeasurementDto(
                      accessPointId, "tempera_station_id", LocalDateTime.now(), 30.0, 10.0, 19.8, 29.1);

      Measurement measurement =
              new Measurement(
                      20.0,
                      LocalDateTime.now(),
                      new Sensor(
                              SensorType.HUMIDITY,
                              Unit.PERCENT,
                              new TemperaStation("tempera_station_id", true, new Userx(), true)));

      List<Measurement> measurementList = List.of(measurement);
      when(accessPointService.isEnabled(measurementDto.access_point_id())).thenReturn(false);
      when(temperaStationService.isEnabled(measurementDto.tempera_station_id())).thenReturn(true);
      when(measurementMapper.mapFromDto(measurementDto)).thenReturn(measurementList);
      when(measurementService.saveMeasurement(measurement)).thenReturn(measurement);
      when(measurementMapper.mapToDto(measurementList)).thenReturn(measurementDto);

      ResponseEntity<MeasurementDto> response =
              measurementController.createMeasurement(measurementDto);

      assertEquals(403, response.getStatusCode().value());
      assertNull(response.getBody());

      verify(measurementService, times(0)).saveMeasurement(measurement);
      verify(temperaStationService, times(0)).isEnabled("tempera_station_id");
      verify(accessPointService, times(1)).isEnabled(accessPointId);
      verify(measurementMapper, times(0)).mapFromDto(measurementDto);
      verify(measurementMapper, times(0)).mapToDto(measurementList);
  }

  // temperaStation is not enabled
}
