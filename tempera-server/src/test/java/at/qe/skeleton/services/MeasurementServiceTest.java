package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.MeasurementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@SpringBootTest
@WebAppConfiguration
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS, scripts = "classpath:measurementServiceTest.sql")
class MeasurementServiceTest {
  @Autowired private MeasurementRepository measurementRepository;
  @Autowired private MeasurementService measurementService;
  @Autowired private SensorService sensorService;
  @Autowired private TemperaStationService temperaStationService;

  private Sensor getSensor() {
    SensorId sensorId = new SensorId();
    sensorId.setSensorId(-10L);
    sensorId.setTemperaId("TEMP123");
    return sensorService.findSensorById(sensorId);
  }

  @DirtiesContext
  @Test
  void loadMeasurement() throws CouldNotFindEntityException {
    // loads a measurement Entity from the database
    LocalDateTime timestamp = LocalDateTime.of(2024, 5, 10, 8, 30, 0);
    String temperaId = "TEMP123";
    Long sensorId = -10L;
    Measurement measurement = measurementService.loadMeasurementByIdComponents(temperaId, sensorId, timestamp);

    assertEquals(-10L, measurement.getSensor().getId().getSensorId(), "Sensor Id should be 1");
    assertEquals(20.0, measurement.getValue(), "Measurement value should be 20.0");
    assertEquals(
        LocalDateTime.of(2024, 5, 10, 8, 30, 0),
        measurement.getId().getTimestamp(),
        "Measurement timestamp should be 2024-05-10 08:30:00");
    assertEquals(
        "TEMP123",
        measurement.getSensor().getId().getTemperaId(),
        "Tempera Station Id should be TEMP123");
  }

  @DirtiesContext
  @Test
  // found this SQL annotation with the help of Copilot.
  // it is described in the documentation:
  // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/context/jdbc/Sql.html
  @Sql(
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      scripts = "classpath:beforeMeasurementTest.sql")
  void saveMeasurement() throws CouldNotFindEntityException {

    // is redundant but good display of different ways to manipulate the database for testing.
    measurementRepository.findAll().forEach(measurementRepository::delete);
    LocalDateTime timestamp = LocalDateTime.now();

    // this sensor should fit the sensor in the data.sql
    Sensor sensor = getSensor();

    Measurement measurement = new Measurement(50.0, timestamp, sensor);

    Measurement savedMeasurement = measurementService.saveMeasurement(measurement);
    assertEquals(measurement.getSensor(), savedMeasurement.getSensor(), "sensor should be equal");
    assertEquals(
        measurement.getId().getTimestamp(), savedMeasurement.getId().getTimestamp(), "timestamp should be equal");
    assertEquals(measurement.getValue(), savedMeasurement.getValue(), "value should be equal");

    assertEquals(
        1,
        measurementRepository.findAll().size(),
        "after saving 1 Measurement, the db should " + "hold exactly 1 measurement");
  }

  @Test
  @DirtiesContext
  @Sql(
          executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
          scripts = "classpath:measurementServiceTest.sql")
  void deleteMeasurement() {
    assertEquals(
        4,
        measurementRepository.findAll().size(),
        "before deleting, the db should hold exactly 4 measurements");
    Measurement measurement = measurementRepository.findAll().get(0);
    measurementService.deleteMeasurement(measurement);
    assertEquals(
        3,
        measurementRepository.findAll().size(),
        "after deleting, the db should hold exactly 3 measurements");
  }

  @Test
  @DirtiesContext
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:measurementServiceTest.sql")
  void saveLoadDeleteMeasurement() throws CouldNotFindEntityException {
    measurementRepository.findAll().forEach(measurementRepository::delete);

    // We have to truncate the LocalDateTime to Milliseconds because some versions of postgres are messing with
    // DateTime Objects that are stored more fine grained than Milliseconds (e.g. nano seconds)
    // But since Leo will make sure that neither Measurement Timestamps nor TimeRecord Timestamps will be that finegrained,
    // it shouldnt be a problem
    LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    Sensor sensor = getSensor();

    Measurement measurement = new Measurement(50.0, timestamp, sensor);
    measurement = measurementService.saveMeasurement(measurement);
    assertEquals(
        1,
        measurementRepository.findAll().size(),
        "after saving 1 Measurement, the db should " + "hold exactly 1 measurement");

    Measurement loadedMeasurement = measurementService.loadMeasurement(measurement.getId());

    measurementService.deleteMeasurement(loadedMeasurement);
    assertEquals(
        0,
        measurementRepository.findAll().size(),
        "after deleting, the db should hold exactly 0 measurements");
  }

    @Test
    @DirtiesContext
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:measurementServiceAlertTest.sql")
  public void reviewForAlerts() throws CouldNotFindEntityException {
       TemperaStation temperaStation = temperaStationService.findById("TEMP123");
       List<Measurement> measurements =
    }
}
