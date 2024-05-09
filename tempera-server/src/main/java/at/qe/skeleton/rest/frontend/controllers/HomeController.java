package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.mappers.HomeDataMapper;
import at.qe.skeleton.rest.frontend.payload.response.HomeDataResponse;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.UserxService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class HomeController {

  private UserxService userService;
  private TemperaStationService temperaService;
  private HomeDataMapper homeDataMapper;

  @GetMapping("/homeData")
  @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  @Transactional
  public ResponseEntity<HomeDataResponse> homeData(Principal principal) {
    // wir wollen alle Kollegen die im selben Team oder selben Group sind.
    //    var colleagueStates =
    //        List.of(
    //            new ColleagueStateDto("Max Mustermann", "Raum 1", State.DEEPWORK),
    //            new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE),
    //            new ColleagueStateDto("Cooler Typ", "Raum 1", State.MEETING));

    String username = principal.getName();
    Userx user = userService.loadUser(username);

    HomeDataResponse homeDataResponse = homeDataMapper.mapUserToHomeDataResponse(user);
    return ResponseEntity.ok(homeDataResponse);
  }
}
