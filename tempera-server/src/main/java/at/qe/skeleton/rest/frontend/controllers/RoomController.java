package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Threshold;
import at.qe.skeleton.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {

  @Autowired private RoomService roomService;

  @GetMapping("/all")
  public ResponseEntity<List<Room>> getAllRooms() {
    List<Room> rooms = roomService.getAllRooms();
    return ResponseEntity.ok(rooms);
  }

  @PostMapping("/create")
  public ResponseEntity<String> createRoom(@RequestBody String roomId) {
    try {
      Room room = roomService.createRoom(roomId);
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
    Optional<Room> room = roomService.getRoomById(roomId);
    if (room.isPresent()) {
      return ResponseEntity.ok(room.get());
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
  }
}
