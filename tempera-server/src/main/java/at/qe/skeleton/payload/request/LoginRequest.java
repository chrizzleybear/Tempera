package at.qe.skeleton.payload.request;

import jakarta.validation.constraints.NotBlank;

// This code was taken from: https://www.bezkoder.com/angular-17-spring-boot-jwt-auth/

public class LoginRequest {
    @NotBlank private String username;

    @NotBlank private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
