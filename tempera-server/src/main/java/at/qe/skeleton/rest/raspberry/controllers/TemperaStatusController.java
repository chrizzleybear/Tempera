package at.qe.skeleton.rest.raspberry.controllers;

import at.qe.skeleton.rest.raspberry.dtos.TemperaStatusDto;
import at.qe.skeleton.services.AccessPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemperaStatusController {
    private final AccessPointService accessPointService;

    public TemperaStatusController(AccessPointService accessPointService) {
        this.accessPointService = accessPointService;
    }

    @PostMapping("/rasp/api/tempera_status")
    public ResponseEntity<TemperaStatusDto> postConnectionInfo(){
        return null;
    }
}
