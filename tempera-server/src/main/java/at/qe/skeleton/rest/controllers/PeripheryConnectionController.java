package at.qe.skeleton.rest.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.rest.dtos.AccessPointDto;
import at.qe.skeleton.rest.mappers.AccessPointMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/rasp/api/access_point")
public class PeripheryConnectionController {
  private final AccessPointMapper accessPointMapper;
  private final AccessPointService accessPointService;

  public PeripheryConnectionController(
      AccessPointMapper accessPointMapper, AccessPointService accessPointService) {
    this.accessPointMapper = accessPointMapper;
    this.accessPointService = accessPointService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<AccessPointDto> getAccessPointDto(@PathVariable UUID id) {
    try {
      AccessPoint entity = accessPointService.getAccessPointById(id);
      return ResponseEntity.ok(accessPointMapper.mapToDto(entity));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
