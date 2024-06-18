package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.*;
import at.qe.skeleton.repositories.*;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class TemperaStationServiceTest {

    @Mock private TemperaStationRepository temperaStationRepository;
    @Autowired private UserxRepository userxRepository;
    @Mock private AccessPointRepository accessPointRepository;
    @Mock private SensorRepository sensorRepository;
    @Mock private AuditLogService auditLogService;

    private TemperaStationService temperaStationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);
        SensorService sensorService = new SensorService(sensorRepository);
        temperaStationService =
                spy(new TemperaStationService(
                        temperaStationRepository,
                        sensorService,
                        userxRepository,
                        accessPointRepository,
                        auditLogService
                ));
    }

    @Test
    @WithMockUser(username = "johndoe", authorities = "ADMIN")
    void createTemperaStationTest() {
        String username = "johndoe";
        String accessPointId = UUID.randomUUID().toString();
        boolean enabled = true;

        Userx mockUser = new Userx();
        mockUser.setUsername(username);
        mockUser.setId(username);

        AccessPoint mockAccessPoint = new AccessPoint();
        mockAccessPoint.setId(UUID.fromString(accessPointId));

        when(accessPointRepository.findById(UUID.fromString(accessPointId))).thenReturn(Optional.of(mockAccessPoint));
        when(temperaStationRepository.findById(any(String.class))).thenReturn(Optional.empty());

        TemperaStation station = temperaStationService.createTemperaStation(enabled, username, accessPointId);

        assertEquals(8, station.getId().length());
        assertTrue(station.isEnabled());
        assertEquals(mockUser, station.getUser());
        assertEquals(mockAccessPoint.getId(), station.getAccessPoint().getId());

        verify(auditLogService, atLeastOnce());
    }

    @Test
    @WithMockUser(username = "johndoe", authorities = "ADMIN")
    void createTemperaStationWithDtoTest() {
        String accessPointId = UUID.randomUUID().toString();
        TemperaStationDto dto =
                new TemperaStationDto(null, "johndoe", true, true, accessPointId);

        String username = "johndoe";
        Userx mockUser = new Userx();
        mockUser.setUsername(username);
        mockUser.setId(username);

        when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);

        temperaStationService.createTemperaStation(dto);

        verify(temperaStationService, times(1)).createTemperaStation(false, dto.user(), dto.accessPointId());
    }
}
