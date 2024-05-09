package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.payload.response.HomeDataResponse;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class HomeController {

  @GetMapping("/homeData")
  @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<HomeDataResponse> homeData() {
    // wir wollen alle Kollegen die im selben Team oder selben Group sind.
    var colleagueStates =
        List.of(
            new ColleagueStateDto("Max Mustermann", "Raum 1", State.DEEPWORK),
            new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE),
            new ColleagueStateDto("Cooler Typ", "Raum 1", State.MEETING));

    return ResponseEntity.ok(
        new HomeDataResponse(
            25,
            50,
            100,
            500,
            Visibility.PUBLIC,
            State.AVAILABLE,
            LocalDateTime.now().toString(),
            null,
            colleagueStates));
  }


  @GetMapping("/homeData/realLogic")
  @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<HomeDataResponse> homeDataRealLogic() {
    // colleagueStates könnte vor Allem interessant sein für Kollegen aus derselben Gruppe oder demselben Projekt
    var colleagueStates =
            List.of(
                    new ColleagueStateDto("Max Mustermann", "Raum 1", State.DEEPWORK),
                    new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE),
                    new ColleagueStateDto("Cooler Typ", "Raum 1", State.MEETING));

    return ResponseEntity.ok(
            new HomeDataResponse(
                    25,
                    50,
                    100,
                    500,
                    Visibility.PUBLIC,
                    State.AVAILABLE,
                    LocalDateTime.now().toString(),
                    null,
                    colleagueStates));
  }
}
