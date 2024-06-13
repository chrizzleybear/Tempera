package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.Room;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import at.qe.skeleton.rest.frontend.dtos.AccessPointDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/accesspoint", produces  = MediaType.APPLICATION_JSON_VALUE)
public class AccessPointController {

    @Autowired private AccessPointService accessPointService;

    @Autowired private TemperaStationService temperaStationService;

    @GetMapping("/all")
    public ResponseEntity<List<AccessPointDto>> getAllAccesspoints() {
        List<AccessPoint> accessPoints = accessPointService.getAllAccesspoints();
        List<AccessPointDto> accessPointDtos = accessPoints.stream()
                .map(a -> new AccessPointDto(
                        a.getId().toString(),
                        a.getRoom().getId(),
                        a.isEnabled(),
                        a.isHealthy()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(accessPointDtos);
    }

    @GetMapping("/load/{accesspointId}")
    public ResponseEntity<AccessPointDto> getAccesspointById(@PathVariable String accesspointId) {
        try {
            AccessPoint a = accessPointService.getAccessPointById(UUID.fromString(accesspointId));
            AccessPointDto accessPointDto = new AccessPointDto(
                    a.getId().toString(),
                    a.getRoom().getId(),
                    a.isEnabled(),
                    a.isHealthy()
            );
            return ResponseEntity.ok(accessPointDto);
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/load/room/{roomId}")
    public ResponseEntity<AccessPoint> getAccesspointsByRoomId(@PathVariable String roomId) {
        try {
            AccessPoint a = accessPointService.getAccessPointByRoomId(roomId);
            return ResponseEntity.ok(a);
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/create")
    public ResponseEntity<AccessPointDto> createAccesspoint(@RequestBody AccessPointDto accessPointDto) {
        // String id, String roomId
        try {
            AccessPoint a = accessPointService.createAccessPoint(accessPointDto);
            return ResponseEntity.ok(new AccessPointDto(
                    a.getId().toString(),
                    a.getRoom().getId(),
                    a.isEnabled(),
                    a.isHealthy()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<AccessPointDto> updateAccesspoint(@RequestBody AccessPointDto accessPointDto) {
        try {
            System.out.println("accessPointDto: " + accessPointDto);
            AccessPoint a = accessPointService.updateAccessPoint(accessPointDto);
            return ResponseEntity.ok(new AccessPointDto(
                    a.getId().toString(),
                    a.getRoom().getId(),
                    a.isEnabled(),
                    a.isHealthy()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{accesspointId}")
    public ResponseEntity<MessageResponse> deleteAccesspoint(@PathVariable String accesspointId) {
        try {
            accessPointService.delete(accessPointService.getAccessPointById(UUID.fromString(accesspointId)));
            return ResponseEntity.ok(new MessageResponse("Accesspoint deleted successfully"));
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Accesspoint not found"));
        }
    }
    @GetMapping("/availableRooms")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> rooms = accessPointService.getAvailableRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/tempera/{accesspointId}")
    public ResponseEntity<List<TemperaStationDto>> getTemperaStations(@PathVariable String accesspointId) {
        try {
            AccessPoint a = accessPointService.getAccessPointById(UUID.fromString(accesspointId));
            List<TemperaStationDto> temperaStations = a.getTemperaStations().stream()
                    .map(t -> new TemperaStationDto(
                            t.getId(),
                            t.getUser().getUsername(),
                            t.isEnabled(),
                            t.isHealthy(),
                            t.getAccessPoint().getId().toString()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(temperaStations);
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
