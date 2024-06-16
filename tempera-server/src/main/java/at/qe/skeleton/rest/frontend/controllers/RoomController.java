package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.*;
import at.qe.skeleton.rest.frontend.dtos.AccessPointDto;
import at.qe.skeleton.rest.frontend.dtos.FloorComponent;
import at.qe.skeleton.rest.frontend.dtos.ThresholdUpdateDto;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.FloorMapper;
import at.qe.skeleton.services.RoomService;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {

  @Autowired private RoomService roomService;

  @Autowired private TemperaStationService temp;

  @Autowired private FloorMapper flo;

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody String roomId) {
        try {
            roomService.createRoom(roomId);
            return ResponseEntity.ok("Room created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

  @DeleteMapping("/delete/{roomId}")
  public ResponseEntity<String> deleteRoom(@PathVariable String roomId) {
    try {
      roomService.deleteRoom(roomId);
      return ResponseEntity.ok("Room deleted successfully.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/addThreshold/{roomId}")
  public ResponseEntity<String> addThresholdToRoom(
      @PathVariable String roomId, @RequestBody Threshold threshold) {
    try {
      boolean result = roomService.addThresholdToRoom(roomId, threshold);
      return result
          ? ResponseEntity.ok("Threshold added successfully.")
          : ResponseEntity.badRequest().body("Failed to add threshold.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/removeThreshold/{roomId}")
  public ResponseEntity<String> removeThresholdFromRoom(
      @PathVariable String roomId, @RequestBody Threshold threshold) {
    try {
      boolean result = roomService.removeThresholdFromRoom(roomId, threshold);
      return result
          ? ResponseEntity.ok("Threshold removed successfully.")
          : ResponseEntity.badRequest().body("Failed to remove threshold.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

    @GetMapping("/load/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable String roomId) {
        try {
            Room room = roomService.getRoomById(roomId);
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/threshold/update")
    public ResponseEntity<Threshold> updateThreshold(@RequestBody ThresholdUpdateDto dto) {
        Threshold updatedThreshold = roomService.updateThreshold(dto);
        return ResponseEntity.ok(updatedThreshold);
    }

    @GetMapping("/floor")
    public ResponseEntity<List<FloorComponent>> getFloorComponents() {
        List<FloorComponent> floorComponents = flo.getAllFloorComponents();
        return ResponseEntity.ok(floorComponents);
    }

  //dummy methods
    @GetMapping("/accesspoint/{roomId}")
    public ResponseEntity<AccessPointDto> getAccessPoints(@PathVariable String roomId) {
        AccessPoint ap = this.roomService.getAccesspoint(roomId);
        AccessPointDto dto = new AccessPointDto(ap.getId().toString(), ap.getRoom().getId() ,ap.isEnabled(), ap.isHealthy());
        return ResponseEntity.ok(dto);
    }

}
