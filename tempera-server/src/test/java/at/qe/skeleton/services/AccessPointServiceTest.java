package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.SensorRepository;
import at.qe.skeleton.repositories.TemperaStationRepository;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.rest.frontend.dtos.AccessPointDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@WebAppConfiguration
public class AccessPointServiceTest {

    @Mock private TemperaStationRepository temperaStationRepository;
    @Autowired private UserxRepository userxRepository;
    @Mock private AccessPointRepository accessPointRepository;
    @Mock private TemperaStationService temperaStationService;
    @Mock private RoomService roomService;
    @Mock private AuditLogService auditLogService;

    private AccessPointService accessPointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);
        this.accessPointService =
                spy(new AccessPointService(
                        accessPointRepository,
                        temperaStationService,
                        temperaStationRepository,
                        roomService,
                        auditLogService
                ));
    }

    @Test
    void testCreateAccessPoint() {
        String roomId = "room123";
        Room room = new Room("Test Room");
        when(roomService.getRoomById(roomId)).thenReturn(room);
        when(accessPointRepository.save(any())).thenAnswer(invocation -> {
            AccessPoint ap = invocation.getArgument(0);
            ap.setId(UUID.randomUUID());
            return ap;
        });

        AccessPoint createdAccessPoint = accessPointService.createAccessPoint(roomId, true, true);

        assertNotNull(createdAccessPoint.getId());
        assertEquals(room, createdAccessPoint.getRoom());
        verify(auditLogService).logEvent(eq(LogEvent.CREATE), eq(LogAffectedType.ACCESS_POINT),
                contains("Created accesspoint for room " + room + " with id "));
        verify(accessPointRepository).save(any());
    }

    @Test
    void testGetAccessPointById() {
        UUID accessPointId = UUID.randomUUID();
        AccessPoint accessPoint = new AccessPoint(accessPointId, new Room("room123"), true, true);
        when(accessPointRepository.findById(accessPointId)).thenReturn(Optional.of(accessPoint));

        AccessPoint fetchedAccessPoint = null;
        try {
            fetchedAccessPoint = accessPointService.getAccessPointById(accessPointId);
        } catch (CouldNotFindEntityException e) {
            fail("Access point should have been found.");
        }

        assertNotNull(fetchedAccessPoint);
        assertEquals(accessPointId, fetchedAccessPoint.getId());
        assertEquals("room123", fetchedAccessPoint.getRoom().getId());
        verify(accessPointRepository).findById(accessPointId);
    }

    @Test
    void testUpdateAccessPoint() {
        AccessPointDto accessPointDto = new AccessPointDto(
            UUID.randomUUID().toString(),
            "room123",
            true,
                true
        );
        Room room = new Room(accessPointDto.room());
        AccessPoint accessPoint = new AccessPoint(UUID.randomUUID(), room, true, true);
        when(accessPointRepository.findById(UUID.fromString(accessPointDto.id()))).thenReturn(Optional.of(accessPoint));
        when(roomService.getRoomById(accessPointDto.room())).thenReturn(room);
        when(accessPointRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AccessPoint updatedAccessPoint = null;
        try {
            updatedAccessPoint = accessPointService.updateAccessPoint(accessPointDto);
        } catch (IllegalArgumentException e) {
            fail("Access point should have been updated.");
        }

        assertNotNull(updatedAccessPoint);
        assertEquals(accessPointDto.enabled(), updatedAccessPoint.isEnabled());
        verify(accessPointRepository).findById(UUID.fromString(accessPointDto.id()));
        verify(roomService).getRoomById(accessPointDto.room());
        verify(auditLogService).logEvent(eq(LogEvent.EDIT), eq(LogAffectedType.ACCESS_POINT),
                contains("Edited accesspoint " + accessPoint.getId() + ", Room set to " + room + ", Enabled: " + accessPointDto.enabled()));
    }

    @Test
    void testUpdateStationConnectionStatus_Success() {
        String temperaStationId = "station123";
        boolean connectionStatus = true;
        TemperaStation temperaStation = new TemperaStation();
        temperaStation.setId("station123");
        when(temperaStationRepository.findById(temperaStationId)).thenReturn(Optional.of(temperaStation));
        when(temperaStationService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TemperaStation updatedStation = null;
        try {
            updatedStation = accessPointService.updateStationConnectionStatus(temperaStationId, connectionStatus);
        } catch (CouldNotFindEntityException e) {
            fail("Tempera station should have been found.");
        }

        assertNotNull(updatedStation);
        assertEquals(connectionStatus, updatedStation.isHealthy());
        verify(auditLogService).logEvent(eq(LogEvent.EDIT), eq(LogAffectedType.ACCESS_POINT),
                contains("Connection status to station " + temperaStationId + "was updated to " + connectionStatus + "."));
        verify(temperaStationService).save(temperaStation);
    }

    @Test
    void testUpdateStationConnectionStatus_NotFound() {
        String temperaStationId = "nonexistentStation";
        boolean connectionStatus = true;

        when(temperaStationRepository.findById(temperaStationId)).thenReturn(Optional.empty());

        assertThrows(CouldNotFindEntityException.class, () -> {
            accessPointService.updateStationConnectionStatus(temperaStationId, connectionStatus);
        });
    }

}
