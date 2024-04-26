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
  private Measurement measurementHumidity;
  private Measurement measurementNullId;
  private Measurement measurementNullTimestamp;
  private Measurement measurementNullSensor;
  private Measurement measurementNullTemperaStation;
  private Measurement measurementInvalidTemperaId;
  private Measurement measurementNoValue;
  private Measurement measurementIrradiance;
  private Measurement measurementTemperature;
  private Measurement measurementNmvoc;

  @BeforeEach
  void setUp() throws TemperaStationIsNotEnabledException {
    measurementMapper = new MeasurementMapper(measurementService, null, accessPointService);

    TemperaStation invalidTemperaStation = new TemperaStation("id_not_in_db", true);

    temperaStation = new TemperaStation("temperaStationId", true);
    temperaStation.setEnabled(true);

    accessPoint = new AccessPoint();
    accessPoint.setEnabled(true);
    accessPoint.addTemperaStation(temperaStation);

    Sensor sensorHumidity = new Sensor(SensorType.HUMIDITY, Unit.PERCENT, temperaStation);
    Sensor sensorNullTemperaStation = new Sensor(SensorType.HUMIDITY, Unit.PERCENT, null);
    Sensor sensorTemperature = new Sensor(SensorType.TEMPERATURE, Unit.CELSIUS, temperaStation);
    Sensor sensorIrradiance = new Sensor(SensorType.IRRADIANCE, Unit.LUX, temperaStation);
    Sensor sensorNmvoc = new Sensor(SensorType.NMVOC, Unit.OHM, temperaStation);
    Sensor sensorInvalidTemperaId =
        new Sensor(SensorType.HUMIDITY, Unit.PERCENT, invalidTemperaStation);

    measurementHumidity = new Measurement();
    measurementHumidity.setId(1L);
    measurementHumidity.setValue(50.0);
    measurementHumidity.setSensor(sensorHumidity);
    measurementHumidity.setTimestamp(java.time.LocalDateTime.now());

    measurementIrradiance = new Measurement();
    measurementIrradiance.setId(2L);
    measurementIrradiance.setValue(500.0);
    measurementIrradiance.setSensor(sensorIrradiance);
    measurementIrradiance.setTimestamp(java.time.LocalDateTime.now());

    measurementNmvoc = new Measurement();
    measurementNmvoc.setId(3L);
    measurementNmvoc.setValue(23.0);
    measurementNmvoc.setSensor(sensorNmvoc);
    measurementNmvoc.setTimestamp(java.time.LocalDateTime.now());

    measurementTemperature = new Measurement();
    measurementTemperature.setId(3L);
    measurementTemperature.setValue(23.0);
    measurementTemperature.setSensor(sensorTemperature);
    measurementTemperature.setTimestamp(java.time.LocalDateTime.now());

    measurementNullId = new Measurement();
    measurementNullId.setId(null);
    measurementNullId.setValue(50.0);
    measurementNullId.setSensor(sensorHumidity);
    measurementNullId.setTimestamp(java.time.LocalDateTime.now());

    measurementNullTimestamp = new Measurement();
    measurementNullTimestamp.setId(1L);
    measurementNullTimestamp.setValue(50.0);
    measurementNullTimestamp.setSensor(sensorHumidity);
    measurementNullTimestamp.setTimestamp(null);

    measurementNullSensor = new Measurement();
    measurementNullSensor.setValue(50.0);
    measurementNullSensor.setSensor(null);
    measurementNullSensor.setTimestamp(java.time.LocalDateTime.now());
    measurementNullSensor.setId(2L);

    measurementNullTemperaStation = new Measurement();
    measurementNullTemperaStation.setId(1L);
    measurementNullTemperaStation.setValue(50.0);
    measurementNullTemperaStation.setSensor(sensorNullTemperaStation);
    measurementNullTemperaStation.setTimestamp(java.time.LocalDateTime.now());

    measurementInvalidTemperaId = new Measurement();
    measurementInvalidTemperaId.setId(1L);
    measurementInvalidTemperaId.setSensor(sensorInvalidTemperaId);
    measurementInvalidTemperaId.setTimestamp(java.time.LocalDateTime.now());
    measurementInvalidTemperaId.setValue(50.0);

    measurementNoValue = new Measurement();
    measurementNoValue.setId(1L);
    measurementNoValue.setSensor(sensorHumidity);
    measurementNoValue.setTimestamp(java.time.LocalDateTime.now());
  }

  @AfterEach
  void tearDown() {}

  @Test
  void testMapToValidDto() throws CouldNotFindEntityException {
    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    MeasurementDto mappedMeasurementDto = measurementMapper.mapToDto(measurementHumidity);

    Assertions.assertNotNull(mappedMeasurementDto, "mapped measurementHumidity dto is null");
    Assertions.assertEquals(
        measurementHumidity.getId(),
        mappedMeasurementDto.id(),
        "measurementHumidity does not have id %s as entity".formatted(measurementHumidity.getId()));
    Assertions.assertEquals(
        measurementHumidity.getSensor().getId().getSensorId(),
        mappedMeasurementDto.sensorId(),
        "sensor id does not match");
    Assertions.assertEquals(
        measurementHumidity.getTimestamp(),
        mappedMeasurementDto.timestamp(),
        "timestamp does not match");
    Assertions.assertEquals(
        measurementHumidity.getValue(), mappedMeasurementDto.value(), "value does not match");
    Assertions.assertEquals(
        measurementHumidity.getSensor().getUnit(),
        mappedMeasurementDto.unit(),
        "unit does not match");
    Assertions.assertEquals(
        measurementHumidity.getSensor().getTemperaStation().getId(),
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

    // wir brauchen noch 3 valide measurements für den test
    // teste den fall, dass ein measurementHumidity keine value hat
    // teste den fall, dass eine Liste mit weniger als 4 measurements übergeben wird
    // teste den fall, dass zweimal ein gleicher sensor übergeben wird

  }

  @Test
  void mapFromDto() {
    // todo: siehe leos doku zu dtos und was er schickt.
  }
}
