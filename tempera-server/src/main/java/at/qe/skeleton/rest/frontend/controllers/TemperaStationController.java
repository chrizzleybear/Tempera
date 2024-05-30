package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.frontend.dtos.TemperaStationDto;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
}
