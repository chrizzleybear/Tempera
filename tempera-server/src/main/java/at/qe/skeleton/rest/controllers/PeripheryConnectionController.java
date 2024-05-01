package at.qe.skeleton.rest.controllers;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.rest.dtos.AccessPointDto;
import at.qe.skeleton.rest.dtos.ScanOrderDto;
import at.qe.skeleton.rest.mappers.AccessPointMapper;
import at.qe.skeleton.services.AccessPointService;
import at.qe.skeleton.services.TemperaStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rasp/api")
public class PeripheryConnectionController {
  private final AccessPointMapper accessPointMapper;
  private final AccessPointService accessPointService;
  Logger logger = Logger.getLogger("raspberryLogger");

  public PeripheryConnectionController(
      AccessPointMapper accessPointMapper, AccessPointService accessPointService) {
    this.accessPointMapper = accessPointMapper;
    this.accessPointService = accessPointService;
  }

  //Get Requests dont allow Request Bodys according to the HTTP/1.1 specification
  // wie funktioniert das mit RequestParam - ist das dasselbe wie header? und url??
  @GetMapping("/valid_devices")
  public ResponseEntity<AccessPointDto> getAccessPointDto(@RequestParam("access_point_id") UUID access_point_id) {
    try {
      logger.info("getAccessPointDto getting request with id: %s".formatted(access_point_id));
      AccessPoint entity = accessPointService.getAccessPointById(access_point_id);
      return ResponseEntity.ok(accessPointMapper.mapToDto(entity));
    } catch (Exception e) {
      logger.info("caught exception: %s".formatted(e));
      return ResponseEntity.badRequest().build();
    }
  }


  @GetMapping("/scan_order")
  public ResponseEntity<ScanOrderDto> getScanOrder(@RequestParam("access_point_id") UUID access_point_id) {
    try {
      logger.info("getScanOrder: getting request with id: %s".formatted(access_point_id));
      //todo: implement logic here
      return ResponseEntity.ok(new ScanOrderDto(false));
    } catch (Exception e) {
      logger.info("caught exception: %s".formatted(e));
      return ResponseEntity.badRequest().build();
    }
  }



}
