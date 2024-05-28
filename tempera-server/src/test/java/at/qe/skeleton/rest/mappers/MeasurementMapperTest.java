package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InconsistentObjectRelationException;
import at.qe.skeleton.exceptions.TemperaStationIsNotEnabledException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.Unit;
import at.qe.skeleton.rest.raspberry.dtos.MeasurementDto;
import at.qe.skeleton.rest.raspberry.mappers.MeasurementMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.SensorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MeasurementMapperTest {
  private MeasurementMapper measurementMapper;
  @Mock private MeasurementService measurementService;
  @Mock private AccessPointService accessPointService;
  @Mock private SensorService sensorService;
  private AccessPoint accessPoint;
  private TemperaStation temperaStation;
  private Measurement measurementHumidity;
  private Measurement measurementNullId;
  private Measurement measurementInvalidTemperaId;
  private Measurement measurementIrradiance;
  private Measurement measurementTemperature;
  private Measurement measurementNmvoc;
  private Measurement measurementDifferentTimestamp;
  private MeasurementDto measurementDto;
  private MeasurementDto measurementDtoInconsistent;
  private Measurement measurementDifferentTemperaStation;
  private TemperaStation differentTemperaStation;
  private Sensor sensorDifferentTemperaStation;
  private TemperaStation invalidTemperaStation;

  @BeforeEach
  void setUp() throws TemperaStationIsNotEnabledException {
    measurementMapper =
        new MeasurementMapper(measurementService, sensorService, accessPointService);

    temperaStation = new TemperaStation("temperaStationId", true, null, true);
    temperaStation.setEnabled(true);

    accessPoint = new AccessPoint();
    accessPoint.setEnabled(true);
    accessPoint.addTemperaStation(temperaStation);

    Sensor sensorHumidity = new Sensor(SensorType.HUMIDITY, Unit.PERCENT, temperaStation);
    Sensor sensorTemperature = new Sensor(SensorType.TEMPERATURE, Unit.CELSIUS, temperaStation);
    Sensor sensorIrradiance = new Sensor(SensorType.IRRADIANCE, Unit.LUX, temperaStation);
    Sensor sensorNmvoc = new Sensor(SensorType.NMVOC, Unit.OHM, temperaStation);


    LocalDateTime timestamp = LocalDateTime.now();
    LocalDateTime differentTimestamp = timestamp.minusMinutes(3);

    measurementHumidity = new Measurement(50.0, timestamp, sensorHumidity);

    measurementIrradiance = new Measurement(500.0, timestamp, sensorIrradiance);

    measurementNmvoc = new Measurement(23.0, timestamp, sensorNmvoc);

    measurementTemperature = new Measurement(19.9, timestamp, sensorTemperature);

    // because we dont persist to db, the id should be null:
    measurementNullId = new Measurement(50.0, timestamp, sensorHumidity);

    invalidTemperaStation = new TemperaStation("id_not_in_db", true, null, true);
    Sensor sensorInvalidTemperaId =
            new Sensor(SensorType.HUMIDITY, Unit.PERCENT, invalidTemperaStation);
    measurementInvalidTemperaId = new Measurement(50.0, timestamp, sensorInvalidTemperaId);

    differentTemperaStation = new TemperaStation("differentTemperaStation", true, null, true);
    sensorDifferentTemperaStation =
        new Sensor(SensorType.HUMIDITY, Unit.PERCENT, differentTemperaStation);

    measurementDifferentTemperaStation =
        new Measurement(50.0, timestamp, sensorDifferentTemperaStation);

    measurementDifferentTimestamp = new Measurement(50.0, differentTimestamp, sensorHumidity);

    measurementDto =
        new MeasurementDto(
            accessPoint.getId(), temperaStation.getId(), timestamp, 50.0, 500.0, 23.0, 19.9);

    // since we randomly create the accessPoint id this time, it should be inconsistent with the
    // temperaStation id
    measurementDtoInconsistent =
        new MeasurementDto(
            UUID.randomUUID(), temperaStation.getId(), timestamp, 50.0, 500.0, 23.0, 19.9);
  }

  @AfterEach
  void tearDown() {
    Mockito.reset(measurementService, accessPointService, sensorService);
  }

  @Test
  void testMapToValidDto() throws CouldNotFindEntityException, InconsistentObjectRelationException {
    when(sensorService.findAllSensorsByTemperaStationId(temperaStation.getId()))
        .thenReturn(
            List.of(
                measurementHumidity.getSensor(),
                measurementIrradiance.getSensor(),
                measurementNmvoc.getSensor(),
                measurementTemperature.getSensor()));
    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    List<Measurement> measurements =
        List.of(
            measurementHumidity, measurementIrradiance, measurementNmvoc, measurementTemperature);

    MeasurementDto mappedMeasurementDto = measurementMapper.mapToDto(measurements);

    Assertions.assertNotNull(mappedMeasurementDto, "mapped measurementHumidity dto is null");
    Assertions.assertEquals(
        accessPoint.getId(),
        mappedMeasurementDto.access_point_id(),
        "access point id does not match");
    Assertions.assertEquals(
        measurementHumidity.getSensor().getTemperaStation().getId(),
        mappedMeasurementDto.tempera_station_id(),
        "sensor id does not match");
    Assertions.assertEquals(
        measurementHumidity.getId().getTimestamp(),
        mappedMeasurementDto.timestamp(),
        "timestamp does not match");
    Assertions.assertEquals(
        measurementHumidity.getValue(),
        mappedMeasurementDto.humidity(),
        "humidity value does not match");
    Assertions.assertEquals(
        measurementIrradiance.getValue(),
        mappedMeasurementDto.irradiance(),
        "irradiance value does not match");
    Assertions.assertEquals(
        measurementNmvoc.getValue(), mappedMeasurementDto.nmvoc(), "nmvoc value does not match");
    Assertions.assertEquals(
        measurementTemperature.getValue(),
        mappedMeasurementDto.temperature(),
        "temperature value does not match");
  }

  @Test
  void testMapToDtoSanityChecks() throws CouldNotFindEntityException {

    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapToDto(null),
        "Mapping a null entity should throw an IllegalArgumentException");

    List<Measurement> measurementsNullList = new ArrayList<>();
    measurementsNullList.add(null);
    measurementsNullList.add(null);
    measurementsNullList.add(null);
    measurementsNullList.add(null);
    Assertions.assertEquals(4, measurementsNullList.size());

    Assertions.assertThrows(
        NullPointerException.class,
        () -> measurementMapper.mapToDto(measurementsNullList),
        "Mapping an empty list should throw an IllegalArgumentException");

    // teste den fall, dass eine Liste mit weniger als 4 measurements übergeben wird
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(measurementHumidity, measurementTemperature, measurementIrradiance)),
        "Mapping an entity with less than 4 measurements should throw an IllegalArgumentException");

    // teste den fall, dass zweimal ein gleicher sensor übergeben wird
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(
                    measurementHumidity,
                    measurementHumidity,
                    measurementIrradiance,
                    measurementTemperature)),
        "Mapping an entity with duplicate sensors should throw an IllegalArgumentException");
  }

  @Test
  void testMapToDtoInvalidTemperaId() throws CouldNotFindEntityException {

    when(accessPointService.getAccessPointByTemperaStationId(
            measurementInvalidTemperaId.getSensor().getTemperaStation().getId()))
        .thenThrow(new CouldNotFindEntityException("invalid Id"));

    Assertions.assertThrows(
        CouldNotFindEntityException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(
                    measurementInvalidTemperaId,
                    measurementTemperature,
                    measurementIrradiance,
                    measurementNmvoc)),
        "Mapping an entity with an invalid TemperaStation should throw a CouldNotFindEntityException");
  }

  @Test
  void testMapToDtoInconsistentTemperaStationId() throws CouldNotFindEntityException {
    when(accessPointService.getAccessPointByTemperaStationId(differentTemperaStation.getId()))
        .thenReturn(accessPoint);

    Assertions.assertThrows(
        InconsistentObjectRelationException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(
                    measurementDifferentTemperaStation,
                    measurementTemperature,
                    measurementIrradiance,
                    measurementNmvoc)),
        "Mapping an entity with a different TemperaStation should throw an IllegalArgumentException");
  }

  @Test
  void testMapToDtoInconsistentTimestamp() throws CouldNotFindEntityException {
    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    Assertions.assertThrows(
        InconsistentObjectRelationException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(
                    measurementDifferentTimestamp,
                    measurementTemperature,
                    measurementIrradiance,
                    measurementNmvoc)),
        "Mapping an entity with a different timestamp should throw an IllegalArgumentException");
  }

  @Test
  void mapFromValidDto() throws CouldNotFindEntityException, InconsistentObjectRelationException {
    // todo: siehe leos doku zu dtos und was er schickt.
    when(sensorService.findAllSensorsByTemperaStationId(temperaStation.getId()))
        .thenReturn(
            List.of(
                measurementHumidity.getSensor(),
                measurementIrradiance.getSensor(),
                measurementNmvoc.getSensor(),
                measurementTemperature.getSensor()));

    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    List<Measurement> mappedMeasurements = measurementMapper.mapFromDto(measurementDto);

    // Make sure the list is not null and has the correct size
    Assertions.assertNotNull(mappedMeasurements, "mapped measurements are null");
    Assertions.assertEquals(4, mappedMeasurements.size(), "mapped measurements have wrong size");

    // Make sure all measurements are present
    Measurement mappedHumidity;
    Measurement mappedIrradiance;
    Measurement mappedNmvoc;
    Measurement mappedTemperature;

    Assertions.assertNotNull(
        mappedHumidity =
            mappedMeasurements.stream()
                .filter(m -> m.getSensor().getSensorType() == SensorType.HUMIDITY)
                .findFirst()
                .orElse(null),
        "humidity measurement not found");

    Assertions.assertNotNull(
        mappedIrradiance =
            mappedMeasurements.stream()
                .filter(m -> m.getSensor().getSensorType() == SensorType.IRRADIANCE)
                .findFirst()
                .orElse(null),
        "irradiance measurement not found");

    Assertions.assertNotNull(
        mappedNmvoc =
            mappedMeasurements.stream()
                .filter(m -> m.getSensor().getSensorType() == SensorType.NMVOC)
                .findFirst()
                .orElse(null),
        "nmvoc measurement not found");

    Assertions.assertNotNull(
        mappedTemperature =
            mappedMeasurements.stream()
                .filter(m -> m.getSensor().getSensorType() == SensorType.TEMPERATURE)
                .findFirst()
                .orElse(null),
        "temperature measurement not found");

    // Make sure the measurements have the correct values
    Assertions.assertEquals(
        measurementDto.timestamp(),
        mappedHumidity.getId().getTimestamp(),
        "Timestamp of Humidity does not match the dto");
    Assertions.assertEquals(
        measurementDto.tempera_station_id(),
        mappedHumidity.getSensor().getTemperaStation().getId(),
        "TemperaStation of Humidity does not match the dto");
    Assertions.assertEquals(measurementDto.access_point_id(), accessPoint.getId());

    Assertions.assertEquals(
        measurementDto.humidity(),
        mappedHumidity.getValue(),
        "Value of Humidity does not match the dto");
    Assertions.assertEquals(
        measurementDto.irradiance(),
        mappedIrradiance.getValue(),
        "Value of Irradiance does not match the dto");
    Assertions.assertEquals(
        measurementDto.nmvoc(), mappedNmvoc.getValue(), "Value of Nmvoc does not match the dto");
    Assertions.assertEquals(
        measurementDto.temperature(),
        mappedTemperature.getValue(),
        "Value of Temperature does not match the dto");
  }

  @Test
  void mapFromInvalidDto() {
    // null dto
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapFromDto(null),
        "Mapping a null dto should throw an IllegalArgumentException");

    // dto missing accessPoint ID
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapFromDto(
                new MeasurementDto(
                    null, temperaStation.getId(), LocalDateTime.now(), 1.0, 1.0, 1.0, 1.0)),
        "Mapping a dto with null accessPoint ID should throw an IllegalArgumentException");

    // dto missing temperaStation ID
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapFromDto(
                new MeasurementDto(
                    UUID.randomUUID(), null, LocalDateTime.now(), 1.0, 1.0, 1.0, 1.0)),
        "Mapping a dto with null temperaStation ID should throw an IllegalArgumentException");

    // dto missing timestamp
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapFromDto(
                new MeasurementDto(
                    UUID.randomUUID(), temperaStation.getId(), null, 1.0, 1.0, 1.0, 1.0)),
        "Mapping a dto with null timestamp should throw an IllegalArgumentException");

    // dto missing humidity
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapFromDto(
                new MeasurementDto(
                    UUID.randomUUID(),
                    temperaStation.getId(),
                    LocalDateTime.now(),
                    null,
                    1.0,
                    1.0,
                    1.0)),
        "Mapping a dto with null humidity should throw an IllegalArgumentException");

    // dto missing irradiance
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapFromDto(
                new MeasurementDto(
                    UUID.randomUUID(),
                    temperaStation.getId(),
                    LocalDateTime.now(),
                    1.0,
                    null,
                    1.0,
                    1.0)),
        "Mapping a dto with null irradiance should throw an IllegalArgumentException");

    // dto missing nmvoc
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapFromDto(
                new MeasurementDto(
                    UUID.randomUUID(),
                    temperaStation.getId(),
                    LocalDateTime.now(),
                    1.0,
                    1.0,
                    null,
                    1.0)),
        "Mapping a dto with null nmvoc should throw an IllegalArgumentException");

    // dto missing temperature
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapFromDto(
                new MeasurementDto(
                    UUID.randomUUID(),
                    temperaStation.getId(),
                    LocalDateTime.now(),
                    1.0,
                    1.0,
                    1.0,
                    null)),
        "Mapping a dto with null temperature should throw an IllegalArgumentException");
  }

  @Test
  void mapFromDtoInsufficientAmountOfSensors() throws CouldNotFindEntityException {
    // sensorService delivers only 3 sensors
    when(sensorService.findAllSensorsByTemperaStationId(temperaStation.getId()))
        .thenReturn(
            List.of(
                measurementHumidity.getSensor(),
                measurementIrradiance.getSensor(),
                measurementNmvoc.getSensor()));
    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapFromDto(measurementDto),
        "IllegalArgumentException should be thrown since the temperaStation has only 3 sensors");
  }

  @Test
  void mapFromDtoDuplicateSensors() throws CouldNotFindEntityException {
    // sensorService delivers 4 sensors, but two of them are the same
    when(sensorService.findAllSensorsByTemperaStationId(temperaStation.getId()))
        .thenReturn(
            List.of(
                measurementHumidity.getSensor(),
                measurementHumidity.getSensor(),
                measurementIrradiance.getSensor(),
                measurementTemperature.getSensor()));

    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapFromDto(measurementDto),
        "IllegalArgumentException should be thrown since two sensors are the same");
  }

  @Test
  void mapFromDtoInconsistentTemperaStationId() throws CouldNotFindEntityException {
    when(sensorService.findAllSensorsByTemperaStationId(temperaStation.getId()))
        .thenReturn(
            List.of(
                measurementHumidity.getSensor(),
                measurementIrradiance.getSensor(),
                measurementNmvoc.getSensor(),
                measurementTemperature.getSensor()));

    when(accessPointService.getAccessPointByTemperaStationId(temperaStation.getId()))
        .thenReturn(accessPoint);

    // measurementDtoInconsistent has a random id for accessPoint, so it should be inconsistent with
    // the accessPointId
    // that is returned by the mocked accessPointService
    Assertions.assertThrows(
        InconsistentObjectRelationException.class,
        () -> measurementMapper.mapFromDto(measurementDtoInconsistent),
        "InconsistentObjectRelationException should be thrown since"
            + "the accessPointId in the dto is inconsistent with the accessPointId of the temperaStation in db");
  }
}
