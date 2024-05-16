package at.qe.skeleton.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MeasurementTest {


      @Test void measurementConstructor() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Measurement(1.0, LocalDateTime.now(), null);
        }, "timestamp should not be allowed to be null");

        Assertions.assertThrows(NullPointerException.class, () -> {
            new Measurement(1.0, null, new Sensor());
        }, "timestamp should not be allowed to be null");
      }

    @Test
    void setTimestamp() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Measurement measurement = new Measurement();
            measurement.getId().setTimestamp(null);
        }, "timestamp should not be allowed to be null");
      }


    @Test
    void testEquals() {
        LocalDateTime localDateTime = LocalDateTime.now();

        SensorId sensorId = new SensorId();
        sensorId.setSensorId(1L);
        sensorId.setTemperaId("tempera_station_1");
        Sensor sensor = new Sensor();
        sensor.setId(sensorId);
        Measurement measurement = new Measurement(11, localDateTime,sensor );
        Measurement measurement1 = new Measurement(11, localDateTime,sensor );

        Assertions.assertEquals(measurement, measurement1, "measurement should be equal to measurement1");
      }

}