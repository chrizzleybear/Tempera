package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InconsistentObjectRelationException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
  private Measurement measurementInvalidTemperaId;
  private Measurement measurementIrradiance;
  private Measurement measurementTemperature;
  private Measurement measurementNmvoc;
  private Measurement measurementDifferentTimestamp;
  private MeasurementDto measurementDto;
  private MeasurementDto measurementDtoInconsistent;

  @BeforeEach
  void setUp() throws TemperaStationIsNotEnabledException {
    measurementMapper = new MeasurementMapper(measurementService, sensorService, accessPointService);

    TemperaStation invalidTemperaStation = new TemperaStation("id_not_in_db", true);

    temperaStation = new TemperaStation("temperaStationId", true);
    temperaStation.setEnabled(true);

    accessPoint = new AccessPoint();
    accessPoint.setEnabled(true);
    accessPoint.addTemperaStation(temperaStation);

    Sensor sensorHumidity = new Sensor(SensorType.HUMIDITY, Unit.PERCENT, temperaStation);
    Sensor sensorTemperature = new Sensor(SensorType.TEMPERATURE, Unit.CELSIUS, temperaStation);
    Sensor sensorIrradiance = new Sensor(SensorType.IRRADIANCE, Unit.LUX, temperaStation);
    Sensor sensorNmvoc = new Sensor(SensorType.NMVOC, Unit.OHM, temperaStation);
    Sensor sensorInvalidTemperaId =
        new Sensor(SensorType.HUMIDITY, Unit.PERCENT, invalidTemperaStation);

    LocalDateTime timestamp = LocalDateTime.now();
    LocalDateTime differentTimestamp = timestamp.minusMinutes(3);

    measurementHumidity = new Measurement(50.0, timestamp, sensorHumidity);
    measurementHumidity.setId(
        1L); // must set id bc we dont save to db, normally gets auto generated

    measurementIrradiance = new Measurement(500.0, timestamp, sensorIrradiance);
    measurementIrradiance.setId(2L);

    measurementNmvoc = new Measurement(23.0, timestamp, sensorNmvoc);
    measurementNmvoc.setId(3L);

    measurementTemperature = new Measurement(19.9, timestamp, sensorTemperature);
    measurementTemperature.setId(4L);

    // because we dont persist to db, the id should be null:
    measurementNullId = new Measurement(50.0, timestamp, sensorHumidity);

    measurementInvalidTemperaId = new Measurement(50.0, timestamp, sensorInvalidTemperaId);
    measurementInvalidTemperaId.setId(1L);

    measurementDifferentTimestamp = new Measurement(50.0, differentTimestamp, sensorHumidity);

    measurementDto = new MeasurementDto(
        accessPoint.getId(), temperaStation.getId(), timestamp, 50.0, 500.0, 23.0, 19.9);

    //since we randomly create the accessPoint id this time, it should be inconsistent with the temperaStation id
    measurementDtoInconsistent =
        new MeasurementDto(
            UUID.randomUUID(), temperaStation.getId(), timestamp, 50.0, 500.0, 23.0, 19.9);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void testMapToValidDto() throws CouldNotFindEntityException {
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
        measurementHumidity.getTimestamp(),
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
  void testMapToInvalidDto() throws CouldNotFindEntityException {
    when(accessPointService.getAccessPointByTemperaStationId(
            measurementInvalidTemperaId.getSensor().getTemperaStation().getId()))
        .thenThrow(new CouldNotFindEntityException("invalid Id"));

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> measurementMapper.mapToDto(null),
        "Mapping a null entity should throw an IllegalArgumentException");

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(
                    measurementNullId,
                    measurementTemperature,
                    measurementIrradiance,
                    measurementNmvoc)),
        "Mapping an entity without an id should throw an IllegalArgumentException");

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(
                    measurementDifferentTimestamp,
                    measurementTemperature,
                    measurementIrradiance,
                    measurementNmvoc)),
        "Mapping an entity with a different timestamp should throw an IllegalArgumentException");

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

    // teste den fall, dass eine Liste mit weniger als 4 measurements übergeben wird
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(measurementHumidity, measurementTemperature, measurementIrradiance)));
    // teste den fall, dass zweimal ein gleicher sensor übergeben wird
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            measurementMapper.mapToDto(
                List.of(
                    measurementHumidity,
                    measurementHumidity,
                    measurementIrradiance,
                    measurementTemperature)));
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
        mappedHumidity.getTimestamp(),
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



    // inconsistent dto
  }
}
