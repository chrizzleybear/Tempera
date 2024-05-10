package at.qe.skeleton.rest.frontend.mappers;

import at.qe.skeleton.services.MeasurementService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.TimeRecordService;
import at.qe.skeleton.services.UserxService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@WebAppConfiguration
class HomeDataMapperTest {
    @Autowired private UserxService userService;
    @Autowired private TemperaStationService temperaService;
    @Autowired private MeasurementService measurementService;
    @Autowired private TimeRecordService timeRecordService;
    @Autowired private HomeDataMapper homeDataMapper;

    @BeforeEach
    void setUp() {
      }

    @AfterEach
    void tearDown() {
      }

    @Test
    void mapUserToHomeDataResponse() {
      }
}