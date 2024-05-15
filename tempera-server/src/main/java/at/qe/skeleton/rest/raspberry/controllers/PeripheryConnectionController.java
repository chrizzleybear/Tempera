package at.qe.skeleton.rest.raspberry.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.raspberry.dtos.AccessPointDto;
import at.qe.skeleton.rest.raspberry.dtos.ScanOrderDto;
import at.qe.skeleton.rest.raspberry.mappers.AccessPointMapper;
import at.qe.skeleton.services.AccessPointService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.core.util.Json;
import org.primefaces.shaded.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rasp/api")
public class PeripheryConnectionController {
  private static final org.slf4j.Logger log = LoggerFactory.getLogger(PeripheryConnectionController.class);
  private final AccessPointMapper accessPointMapper;
  private final AccessPointService accessPointService;
  Logger logger = Logger.getLogger("raspberryLogger");

  public PeripheryConnectionController(
      AccessPointMapper accessPointMapper, AccessPointService accessPointService) {
    this.accessPointMapper = accessPointMapper;
    this.accessPointService = accessPointService;
  }

  // Get Requests dont allow Request Bodys according to the HTTP/1.1 specification
  // wie funktioniert das mit RequestParam - ist das dasselbe wie header? und url??
  @GetMapping("/valid_devices")
  public ResponseEntity<AccessPointDto> getAccessPointDto(
      @RequestParam("access_point_id") UUID access_point_id) {
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
  public ResponseEntity<ScanOrderDto> getScanOrder(
      @RequestParam("access_point_id") UUID access_point_id) {
    try {
      logger.info("getScanOrder: getting request with id: %s".formatted(access_point_id));
      // todo: implement logic here
      return ResponseEntity.ok(new ScanOrderDto(false));
    } catch (Exception e) {
      logger.info("caught exception: %s".formatted(e));
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/tempera_status")
  public ResponseEntity<String> putTemperaStationConnectionInfo(
      @RequestParam("access_point_id") UUID access_point_id,
      @RequestParam("station_id") String station_id,
      @RequestBody JsonNode payload) {
    boolean connection_status = payload.get("connection_status").asBoolean();

    try{
      TemperaStation temperaStation = accessPointService.updateStationConnectionStatus(station_id, connection_status);
      return ResponseEntity.ok().body("Update successful. Updated station info: " + temperaStation);
    } catch (CouldNotFindEntityException e) {
      return ResponseEntity.badRequest()
          .body(
              "{message: "
                  + "No tempera station found under the %s ID".formatted(station_id)
                  + "}");
    }
  }
}
