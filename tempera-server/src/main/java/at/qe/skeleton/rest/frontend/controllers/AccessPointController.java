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
    public ResponseEntity<AccessPoint> getAccesspointById(@RequestBody String accesspointId) {
        try {
            AccessPoint a = accessPointService.getAccessPointById(UUID.fromString(accesspointId));
            return ResponseEntity.ok(a);
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/create")
    public ResponseEntity<String> createAccesspoint(@RequestBody AccessPointDto accessPointDto) {
        // String id, String room, boolean enabled, boolean isHealthy
        try {
            AccessPoint a = accessPointService.createAccessPoint(
                    accessPointDto.id(),
                    accessPointDto.room(),
                    accessPointDto.enabled(),
                    accessPointDto.isHealthy()
            );
            return ResponseEntity.ok("Accesspoint for room " + a.getRoom().getId() + " has been set.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
