package at.qe.skeleton.rest.frontend.controllers;


import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.AccumulatedTimeMapper;
import at.qe.skeleton.rest.frontend.payload.response.AccumulatedTimeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/accumulated-time", produces = "application/json")
public class AccumulatedTimeController {
  private static final Logger logger = LoggerFactory.getLogger(AccumulatedTimeController.class);
  private final AccumulatedTimeMapper accumulatedTimeMapper;

  public AccumulatedTimeController(AccumulatedTimeMapper accumulatedTimeMapper) {
    this.accumulatedTimeMapper = accumulatedTimeMapper;
  }

  @GetMapping("/data")
  @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('GROUPLEAD')")
  public ResponseEntity<AccumulatedTimeResponse> getAccumulatedTimeData() {
    logger.info("getAccumulatedTimeData called");
    //check for permissions
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    if (auth.getAuthorities().contains(new SimpleGrantedAuthority(UserxRole.MANAGER.toString()))){
      logger.info("detected Authorization: Manager");
      return ResponseEntity.ok(accumulatedTimeMapper.getManagerTimeData(username));
    }
    if (auth.getAuthorities().contains(new SimpleGrantedAuthority(UserxRole.GROUPLEAD.toString()))) {
        logger.info("detected Authorization: GroupLead");
      return ResponseEntity.ok(accumulatedTimeMapper.getGroupLeadTimeData(username));
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }
}
