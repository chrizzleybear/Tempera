package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
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
    public ResponseEntity<AccessPoint> getAccesspointsByRoomId(@RequestBody String roomId) {
        try {
            AccessPoint a = accessPointService.getAccessPointByRoomId(roomId);
            return ResponseEntity.ok(a);
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/create")
    public ResponseEntity<String> createAccesspoint(@RequestBody AccessPointDto accessPointDto) {
        // String id, String room, boolean enabled, boolean isHealthy
        try {
            AccessPoint a = accessPointService.createAccessPoint(accessPointDto);
            return ResponseEntity.ok("Added accesspoint for room " + a.getRoom().getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAccesspoint(@RequestBody AccessPointDto accessPointDto) {
        try {
            System.out.println("accessPointDto: " + accessPointDto);
            AccessPoint a = accessPointService.updateAccessPoint(accessPointDto);
            return ResponseEntity.ok("Accesspoint " + accessPointDto.id() + " has been updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccesspoint(@RequestBody String accesspointId) {
        try {
            accessPointService.delete(accessPointService.getAccessPointById(UUID.fromString(accesspointId)));
            return ResponseEntity.ok("Accesspoint deleted.");
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
