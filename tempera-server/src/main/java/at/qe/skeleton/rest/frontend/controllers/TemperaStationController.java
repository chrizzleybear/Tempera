package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.frontend.dtos.SensorDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleUserDto;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping(value = "/api/temperastation", produces  = MediaType.APPLICATION_JSON_VALUE)
public class TemperaStationController {

    @Autowired
    private TemperaStationService temperaStationService;

    @GetMapping("/all")
    public ResponseEntity<List<TemperaStationDto>> getAllTemperaStations() {
        List<TemperaStationDto> temperaStations = temperaStationService.getAllTemperaStations();

        return ResponseEntity.ok(temperaStations);
    }

    @GetMapping("/load/{temperaStationId}")
    public ResponseEntity<TemperaStationDto> getTemperaStationById(@PathVariable String temperaStationId) {
        try {
            TemperaStation t = temperaStationService.findById(temperaStationId);
            TemperaStationDto temperaStationDto = new TemperaStationDto(t.getId(), t.getUser().getUsername(), t.isEnabled(), t.isHealthy());
            return ResponseEntity.ok(temperaStationDto);
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @DeleteMapping("/delete/{temperaStationId}")
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
    public ResponseEntity<MessageResponse> updateTemperaStation(@RequestBody TemperaStationDto temperaStationDto) {
        try {
            TemperaStation t = temperaStationService.findById(temperaStationDto.id());
            t.setEnabled(temperaStationDto.enabled());
            t.setIsHealthy(temperaStationDto.isHealthy());
            temperaStationService.save(t);
            return ResponseEntity.ok(new MessageResponse("TemperaStation with id " + temperaStationDto.id() + " was updated."));
        } catch (CouldNotFindEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("TemperaStation with id " + temperaStationDto.id() + " was not found."));
        }
    }

    @PutMapping("/create")
    public ResponseEntity<MessageResponse> createTemperaStation(@RequestBody TemperaStationDto temperaStationDto) {
        temperaStationService.createTemperaStation(temperaStationDto.id(), temperaStationDto.enabled(), temperaStationDto.user());
        return ResponseEntity.ok(new MessageResponse("TemperaStation was created."));
    }

    @GetMapping("sensors/{temperaStationId}")
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
    public ResponseEntity<List<SimpleUserDto>> getAvailableUsers() {
        List<SimpleUserDto> users = temperaStationService.getAvailableUsers();
        return ResponseEntity.ok(users);
    }
}
