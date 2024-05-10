package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;

import at.qe.skeleton.rest.frontend.payload.response.HomeDataResponse;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.UserxService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import at.qe.skeleton.rest.frontend.mappers.HomeDataMapper;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class HomeController {

  private HomeDataMapper homeDataMapper;

  public HomeController(HomeDataMapper homeDataMapper) {
    this.homeDataMapper = homeDataMapper;
  }

  @GetMapping("/homeData")
  @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  @Transactional
  public ResponseEntity<HomeDataResponse> homeData(Userx user) {
    // wir wollen alle Kollegen die in der selben Group sind.

    HomeDataResponse homeDataResponse = homeDataMapper.mapUserToHomeDataResponse(user);
    return ResponseEntity.ok(homeDataResponse);
  }
}
