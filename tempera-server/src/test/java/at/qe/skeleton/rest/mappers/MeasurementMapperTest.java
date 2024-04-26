package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.TemperaStationIsNotEnabledException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.Unit;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.SensorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MeasurementMapperTest {
  private MeasurementMapper measurementMapper;
  @Mock private MeasurementService measurementService;
  @Mock private AccessPointService accessPointService;

  // todo: wozu braucht es den Sensorservice?
  @Mock private SensorService sensorService;
  private AccessPoint accessPoint;
  private TemperaStation temperaStation;
  private Measurement measurement;
  private Measurement measurementNullId;
  private Measurement measurementNullTimestamp;
  private Measurement measurementNullSensor;
  private Measurement measurementNullTemperaStation;
  private Measurement measurementInvalidTemperaId;

  @BeforeEach
  void setUp() throws TemperaStationIsNotEnabledException {
    measurementMapper = new MeasurementMapper(measurementService, null, accessPointService);

    TemperaStation invalidTemperaStation = new TemperaStation("id_not_in_db");

    temperaStation = new TemperaStation("temperaStationId");
    temperaStation.setEnabled(true);

    accessPoint = new AccessPoint();
    accessPoint.setEnabled(true);
    accessPoint.addTemperaStation(temperaStation);

    SensorTemperaCompositeId sensorTemperaCompositeId = new SensorTemperaCompositeId();
    sensorTemperaCompositeId.setSensorId(1L);
    sensorTemperaCompositeId.setTemperaStationId(temperaStation.getId());

    SensorTemperaCompositeId sensorTemperaCompositeIdNull = new SensorTemperaCompositeId();
    sensorTemperaCompositeIdNull.setSensorId(null);
    sensorTemperaCompositeIdNull.setTemperaStationId(null);

    SensorTemperaCompositeId sensorTemperaCompositeIdInvalid = new SensorTemperaCompositeId();
    sensorTemperaCompositeIdInvalid.setSensorId(1L);
    sensorTemperaCompositeIdInvalid.setTemperaStationId(invalidTemperaStation.getId());

    Sensor sensor =
        new Sensor(SensorType.HUMIDITY, Unit.PERCENT, temperaStation, sensorTemperaCompositeId);

    Sensor sensorNullTemperaStation =
        new Sensor(SensorType.HUMIDITY, Unit.PERCENT, null, sensorTemperaCompositeIdNull);

    Sensor sensorInvalidTemperaId =
        new Sensor(
            SensorType.HUMIDITY,
            Unit.PERCENT,
            invalidTemperaStation,
            sensorTemperaCompositeIdInvalid);

    measurement = new Measurement(50.0, sensor);
    measurement.setId(1L); // need to set Id, since @GeneratedValue only works when persisting

    measurementNullId = new Measurement(50.0, sensor);
    measurementNullId.setId(null);

    measurementNullTimestamp = new Measurement(50.0, sensor);
    measurementNullTimestamp.setTimestamp(null);

    measurementNullSensor = new Measurement(50.0, null);
    measurementNullSensor.setId(2L);

    measurementNullTemperaStation = new Measurement(50.0, sensorNullTemperaStation);

    measurementInvalidTemperaId = new Measurement(50.0, sensor);
    measurementInvalidTemperaId.setId(1L);
    measurementInvalidTemperaId.setSensor(sensorInvalidTemperaId);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void testMapToValidDto() throws CouldNotFindEntityException {
    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    MeasurementDto mappedMeasurementDto = measurementMapper.mapToDto(measurement);

    Assertions.assertNotNull(mappedMeasurementDto, "mapped measurement dto is null");
    Assertions.assertEquals(
        measurement.getId(),
        mappedMeasurementDto.id(),
        "measurement does not have id %s as entity".formatted(measurement.getId()));
    Assertions.assertEquals(
        measurement.getSensor().getId().getSensorId(),
        mappedMeasurementDto.sensorId(),
        "sensor id does not match");
    Assertions.assertEquals(
        measurement.getTimestamp(), mappedMeasurementDto.timestamp(), "timestamp does not match");
    Assertions.assertEquals(
        measurement.getValue(), mappedMeasurementDto.value(), "value does not match");
    Assertions.assertEquals(
        measurement.getSensor().getUnit(), mappedMeasurementDto.unit(), "unit does not match");
    Assertions.assertEquals(
        measurement.getSensor().getTemperaStation().getId(),
        mappedMeasurementDto.stationId(),
        "Tempera Station Id does not match");
  }

  @Test
  void testMapToInvalidDto() throws CouldNotFindEntityException {
    when(accessPointService.getAccessPointByTemperaStationId(
            measurementInvalidTemperaId.getSensor().getTemperaStation().getId()))
        .thenThrow(new CouldNotFindEntityException("invalid Id"));

    // todo: an den code reviewer: ist es praktikabel und sinnvoll im Mapper all diese Sanity Checks
    // abzufragen? Wohlgemerkt nicht im Test sondern im mapper
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapToDto(null),
        "Mapping a null entity should throw an IllegalArgumentException");
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapToDto(measurementNullId),
        "Mapping an entity without an id should throw an IllegalArgumentException");
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapToDto(measurementNullTimestamp),
        "Mapping an entity without a timestamp should throw an IllegalArgumentException");
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapToDto(measurementNullSensor),
        "Mapping an entity without a sensor should throw an IllegalArgumentException");
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapToDto(measurementNullTemperaStation),
        "Mapping an entity without a TemperaStation should throw an IllegalArgumentException");
    Assertions.assertThrows(
        CouldNotFindEntityException.class,
        () -> measurementMapper.mapToDto(measurementInvalidTemperaId),
        "Mapping an entity with an invalid TemperaStation should throw a CouldNotFindEntityException");
  }

  @Test
  void mapFromDto() {}
}
