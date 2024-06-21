package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.frontend.dtos.SensorDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleUserDto;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/temperastation", produces  = MediaType.APPLICATION_JSON_VALUE)
public class TemperaStationController {

    @Autowired
    private TemperaStationService temperaStationService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TemperaStationDto>> getAllTemperaStations() {
        List<TemperaStationDto> temperaStations = temperaStationService.getAllTemperaStations();

        return ResponseEntity.ok(temperaStations);
    }

    @GetMapping("/load/{temperaStationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TemperaStationDto> getTemperaStationById(@PathVariable String temperaStationId) {
        try {
            TemperaStation t = temperaStationService.findById(temperaStationId);
            TemperaStationDto temperaStationDto = new TemperaStationDto(t.getId(), t.getUser().getUsername(), t.isEnabled(), t.isHealthy(), t.getAccessPoint().getId().toString());
            return ResponseEntity.ok(temperaStationDto);
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/delete/{temperaStationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponse> deleteTemperaStationById(@PathVariable String temperaStationId) {
        try {
            TemperaStation t = temperaStationService.findById(temperaStationId);
            temperaStationService.delete(t);
            return ResponseEntity.ok(new MessageResponse("TemperaStation with id " + temperaStationId + " was deleted."));
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("TemperaStation with id " + temperaStationId + " was not found."));
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponse> updateTemperaStation(@RequestBody TemperaStationDto temperaStationDto) {
        try {
            temperaStationService.updateTemperaStation(temperaStationDto.id(), temperaStationDto.enabled(), temperaStationDto.user());
            return ResponseEntity.ok(new MessageResponse("TemperaStation with id " + temperaStationDto.id() + " was updated."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("TemperaStation with id " + temperaStationDto.id() + " was not found."));
        }
    }

    @PutMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageResponse> createTemperaStation(@RequestBody TemperaStationDto temperaStationDto) {
        try {
            TemperaStation t = temperaStationService.createTemperaStation(temperaStationDto);
            return ResponseEntity.ok(new MessageResponse("TemperaStation was created with id " + t.getId() + "."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("sensors/{temperaStationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<SensorDto>> getTemperaStationSensors(@PathVariable String temperaStationId) {
        try {
            TemperaStation t = temperaStationService.findById(temperaStationId);
            List<SensorDto> sensors = t.getSensors().stream()
                    .map(s -> new SensorDto(s.getSensorId().toString(), s.getSensorType().toString(), s.getUnit().toString(), s.getTemperaStation().getId()))
                    .toList();
            return ResponseEntity.ok(sensors);
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("availableUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<SimpleUserDto>> getAvailableUsers() {
        List<SimpleUserDto> users = temperaStationService.getAvailableUsers();
        return ResponseEntity.ok(users);
    }
}
