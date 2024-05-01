package at.qe.skeleton.controllers;

import at.qe.skeleton.jwt.JwtUtils;
import at.qe.skeleton.payload.ColleagueStateDto;
import at.qe.skeleton.payload.response.HomeDataResponse;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
@RestController
@RequestMapping("/api/user")
public class UserController{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/homeData")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
    public ResponseEntity<HomeDataResponse> homeData() {
        var colleagueStates = List.of(
                new ColleagueStateDto("Max Mustermann", "Raum 1", State.DEEPWORK),
                new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE),
                new ColleagueStateDto("Cooler Typ", "Raum 1", State.MEETING)
        );

        return ResponseEntity.ok(
                new HomeDataResponse(25, 50, 100, 500, Visibility.PUBLIC, State.AVAILABLE, LocalDateTime.now().toString(), null, colleagueStates)
        );

    }

}

