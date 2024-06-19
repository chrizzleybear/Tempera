package at.qe.skeleton.services;

import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.SensorId;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.services.AuditLogService;
import at.qe.skeleton.repositories.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    private SensorService sensorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // uncomment if audit logs are added to sensor service
        // when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);
        this.sensorService =
                spy(new SensorService(
                        sensorRepository
                ));
    }

    @Test
    void testfindSensorById() {
        SensorId sensorId = new SensorId("stationId", 100L);
        Sensor sensor = new Sensor();
        when(sensorRepository.findById(sensorId)).thenReturn(Optional.of(sensor));

        Sensor foundSensor = sensorService.findSensorById(sensorId);

        assertNotNull(foundSensor);
        verify(sensorRepository, times(1)).findById(sensorId);
    }

    @Test
    void testfindAllSensorsByTemperaStationId() {
        String temperaStationId = "temperaStationId";
        Sensor sensor1 = new Sensor();
        Sensor sensor2 = new Sensor();
        List<Sensor> sensors = Arrays.asList(sensor1, sensor2);
        when(sensorRepository.findAllByTemperaStationId(temperaStationId)).thenReturn(sensors);

        List<Sensor> foundSensors = sensorService.findAllSensorsByTemperaStationId(temperaStationId);

        assertNotNull(foundSensors);
        assertEquals(2, foundSensors.size());
        verify(sensorRepository, times(1)).findAllByTemperaStationId(temperaStationId);
    }

    @Test
    void testsaveSensor() {
        Sensor sensor = new Sensor();
        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);

        Sensor savedSensor = sensorService.saveSensor(sensor);

        assertNotNull(savedSensor);
        verify(sensorRepository, times(1)).save(sensor);
    }

    @Test
    void testdeleteSensor() {
        Sensor sensor = new Sensor();
        doNothing().when(sensorRepository).delete(sensor);

        sensorService.deleteSensor(sensor);

        verify(sensorRepository, times(1)).delete(sensor);
    }

}
