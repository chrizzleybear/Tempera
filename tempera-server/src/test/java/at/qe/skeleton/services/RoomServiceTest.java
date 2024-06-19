package at.qe.skeleton.services;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.model.ThresholdTip;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.ThresholdType;
import at.qe.skeleton.repositories.AccessPointRepository;
import at.qe.skeleton.repositories.RoomRepository;
import at.qe.skeleton.repositories.ThresholdRepository;
import at.qe.skeleton.repositories.ThresholdTipRepository;
import at.qe.skeleton.rest.frontend.dtos.ThresholdDto;
import at.qe.skeleton.rest.frontend.dtos.ThresholdUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RoomServiceTest {

    @Autowired private static final Logger log = LoggerFactory.getLogger(RoomService.class);

    @Mock private RoomRepository roomRepository;
    @Mock private ThresholdRepository thresholdRepository;
    @Mock private ThresholdTipRepository thresholdTipRepository;
    @Mock private AccessPointRepository accessPointRepository;
    @Mock private AuditLogService auditLogService;

    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //doNothing().when(log.info(any(String.class)));
        when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);
        this.roomService =
                spy(new RoomService(
                        roomRepository,
                        thresholdRepository,
                        thresholdTipRepository,
                        accessPointRepository,
                        auditLogService
                ));
    }

    @Test
    void testgetAllRooms() {
        Room room1 = new Room("room1");
        Room room2 = new Room("room2");
        List<Room> rooms = Arrays.asList(room1, room2);
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> foundRooms = roomService.getAllRooms();

        assertNotNull(foundRooms);
        assertEquals(2, foundRooms.size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void testcreateRoom() {
        String roomId = "room1";
        when(roomRepository.existsById(roomId)).thenReturn(false);
        when(thresholdRepository.findDefaultThresholds()).thenReturn(Arrays.asList(new Threshold()));
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(thresholdRepository.save(any(Threshold.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Room createdRoom = roomService.createRoom(roomId);

        assertNotNull(createdRoom);
        assertEquals(roomId, createdRoom.getId());
        verify(roomRepository, times(1)).existsById(roomId);
        verify(thresholdRepository, times(1)).findDefaultThresholds();
        verify(thresholdRepository, times(1)).save(any(Threshold.class));
        verify(roomRepository, times(1)).save(any(Room.class));
   }

    @Test
    void testdeleteRoom() {
        String roomId = "room1";
        Room room = new Room(roomId);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        roomService.deleteRoom(roomId);

        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, times(1)).delete(room);
    }

    @Test
    void testaddThresholdToRoom() {
        String roomId = "room1";
        Threshold threshold = new Threshold();
        Room room = new Room(roomId);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        boolean result = roomService.addThresholdToRoom(roomId, threshold);

        assertTrue(result);
        assertTrue(room.getThresholds().contains(threshold));
        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testremoveThresholdFromRoom() {
        String roomId = "room1";
        Threshold threshold = new Threshold();
        Room room = new Room(roomId);
        room.addThreshold(threshold);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        boolean result = roomService.removeThresholdFromRoom(roomId, threshold);

        assertTrue(result);
        assertFalse(room.getThresholds().contains(threshold));
        verify(roomRepository, times(1)).findById(roomId);
        verify(roomRepository, times(1)).save(room);
        verify(auditLogService, times(1)).logEvent(any(LogEvent.class), any(LogAffectedType.class), anyString());
    }

    @Test
    void testgetAvailableRooms() {
        Room room1 = new Room("room1");
        Room room2 = new Room("room2");
        room2.setAccessPoint(new AccessPoint());
        List<Room> rooms = Arrays.asList(room1, room2);
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> availableRooms = roomService.getAvailableRooms();

        assertNotNull(availableRooms);
        assertEquals(1, availableRooms.size());
        assertEquals("room1", availableRooms.get(0).getId());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void testgetAccesspoint() {
        String roomId = "room1";
        Room room = new Room(roomId);
        AccessPoint accessPoint = new AccessPoint();
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(accessPointRepository.findByRoom(room)).thenReturn(Optional.of(accessPoint));

        AccessPoint foundAccessPoint = roomService.getAccesspoint(roomId);

        assertNotNull(foundAccessPoint);
        verify(roomRepository, times(1)).findById(roomId);
        verify(accessPointRepository, times(1)).findByRoom(room);
    }

    @Test
    void updateThresholdTest() {
        Threshold threshold = new Threshold();
        ThresholdTip tip = new ThresholdTip("test");
        ThresholdDto thresholdDto = new ThresholdDto(1L, SensorType.HUMIDITY, ThresholdType.LOWERBOUND_INFO, 7.7, tip);
        ThresholdUpdateDto dto = new ThresholdUpdateDto(thresholdDto, "test reason");
        when(thresholdRepository.findById(1L)).thenReturn(Optional.of(threshold));
        when(thresholdRepository.save(any(Threshold.class))).thenReturn(threshold);

        Threshold updatedThreshold = roomService.updateThreshold(dto);

        assertNotNull(updatedThreshold);
        assertEquals(7.7, updatedThreshold.getValue());
        verify(thresholdRepository, times(1)).findById(1L);
        verify(thresholdRepository, times(1)).save(updatedThreshold);
        verify(auditLogService, times(1)).logEvent(any(LogEvent.class), any(LogAffectedType.class), anyString());
    }


}
