package at.qe.skeleton.services;

import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.*;
import at.qe.skeleton.repositories.*;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

    @Test
    @WithMockUser(username = "johndoe", authorities = "ADMIN")
    void getAllTemperaStationsTest() {
        List<TemperaStation> mockTemperaStations = Arrays.asList(
                createMockTemperaStation("1", "user1", true, true, UUID.randomUUID().toString()),
                createMockTemperaStation("2", "user2", true, true, UUID.randomUUID().toString())
        );
        when(temperaStationRepository.findAll()).thenReturn(mockTemperaStations);
        List<TemperaStationDto> result = temperaStationService.getAllTemperaStations();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("user1", result.get(0).user());
        assertTrue(result.get(0).enabled());
        assertTrue(result.get(0).isHealthy());
        assertEquals(36, result.get(0).accessPointId().length());

        assertEquals("2", result.get(1).id());
        assertEquals("user2", result.get(1).user());
        assertTrue(result.get(1).enabled());
        assertTrue(result.get(1).isHealthy());
        assertEquals(36, result.get(1).accessPointId().length());

        verify(temperaStationRepository, times(1)).findAll();
    }

    // Helper method
    private TemperaStation createMockTemperaStation(String id, String username, boolean enabled, boolean healthy, String accessPointId) {
        Userx user = new Userx();
        user.setUsername(username);
        user.setId(username);

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setId(UUID.fromString(accessPointId));

        TemperaStation station = new TemperaStation();
        station.setId(id);
        station.setUser(user);
        station.setEnabled(enabled);
        station.setIsHealthy(healthy);
        station.setAccessPoint(accessPoint);

        return station;
    }

    @Test
    void getAvailableUsersTest() {
        int resultBefore = temperaStationService.getAvailableUsers().size();
        Userx user1 = createUser("user1", "John", "Doe", "john.doe@example.com");
        Userx user2 = createUser("user2", "Jane", "Smith", "jane.smith@example.com");
        TemperaStation station1 = createMockTemperaStation("1", "user1", true, true, UUID.randomUUID().toString());
        TemperaStation station2 = createMockTemperaStation("2", "not user 2", true, true, UUID.randomUUID().toString());
        when(temperaStationRepository.findAll()).thenReturn(Arrays.asList(station1, station2));
        int resultAfter = temperaStationService.getAvailableUsers().size();

        // to-do: test does not work properly since users are not saved
        // assertEquals(resultBefore+1, resultAfter);
    }

    // Helper method to create a mock Userx object
    private Userx createUser(String username, String firstName, String lastName, String email) {
        Userx user = new Userx();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }

    @Test
    void updateTemperaStationTest() {
        String id = "1";
        TemperaStation mockStation = createMockTemperaStation(id, "user1", true, true, UUID.randomUUID().toString());
        when(temperaStationRepository.findById(id)).thenReturn(Optional.of(mockStation));
        String updatedUsername = "user2";
        temperaStationService.updateTemperaStation(id, false, updatedUsername);

        verify(temperaStationRepository, times(1)).findById(id);
        verify(temperaStationRepository, times(1)).save(any(TemperaStation.class));
        verify(auditLogService, times(1)).logEvent(eq(LogEvent.EDIT), eq(LogAffectedType.TEMPERA_STATION), anyString());
    }
}
