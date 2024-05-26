package at.qe.skeleton.rest.raspberry.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.rest.raspberry.dtos.AccessPointAllowedDto;
import at.qe.skeleton.rest.raspberry.dtos.ScanOrderDto;
import at.qe.skeleton.rest.raspberry.mappers.AccessPointMapper;
import at.qe.skeleton.services.AccessPointService;
import com.fasterxml.jackson.databind.JsonNode;
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
  public ResponseEntity<AccessPointAllowedDto> getAccessPointDto(
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
    JSONObject response = new JSONObject();

    boolean connectionStatus;
    try{
      connectionStatus = payload.get("connection_status").asBoolean();
    }
    catch (NullPointerException e){
      response.put("error", e.getCause());
      return ResponseEntity.badRequest().body(response.toString());
    }

    String loggingMessage =
            "Received order to set the status of station '%s' to %s by access point %s"
                    .formatted(station_id, connectionStatus, access_point_id);
    logger.info(loggingMessage);
    try{
      TemperaStation temperaStation = accessPointService.updateStationConnectionStatus(station_id, connectionStatus);
      response.put("response", "Update successful. Updated station info: " + temperaStation);
      return ResponseEntity.ok().body(response.toString());
    } catch (CouldNotFindEntityException e) {
      response.put("error message", "No tempera station found under the %s ID".formatted(station_id));
      return ResponseEntity.badRequest()
          .body(response.toString());
    }
    catch (Exception e){
      response.put("error message", e.getCause());
      return ResponseEntity.internalServerError().body(response.toString());
    }
  }
}
