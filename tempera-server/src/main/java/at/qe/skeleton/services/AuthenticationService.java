package at.qe.skeleton.services;

import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.model.Userx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Component
@Scope("application")
public class AuthenticationService {

  @Autowired private UserxService userxService;

  @Autowired private EmailService emailService;

  @Autowired private PasswordEncoder encode;
  static int tokenLength = 6;

  @Transactional
  @PreAuthorize("hasAuthority('ADMIN')")
  public UserxDto registerUser(UserxDto userxDTO) {
    Userx newUser = userxService.convertToEntity(userxDTO);
    if(userxService.loadUser(newUser.getUsername()) != null) {
      throw new IllegalArgumentException("Username already exists");
    }
    userxService.saveUser(newUser);
    sendValidationEmail(newUser);
    return userxService.convertToDTO(newUser);
  }

  // Encode username for security
  public void sendValidationEmail(Userx user) {
    String password = generateAndSaveActivationToken(user);
    try {
      emailService.sendEmail(
              user.getEmail(),
              "Registration successful",
              "Hello "
                      + user.getFirstName()
                      + " "
                      + user.getLastName()
                      + ",\n\n"
                      + "Your registration was successful.\nYour username is: "
                      + user.getUsername()
                      + "\n"
                      + "Your password is: "
                      + password
                      + " \n\n"
                      + "Please follow the link to set your password.\n\n"
                      + "http://localhost:4200/validate"
                      + "\n\n"
                      + "Best regards,\n"
                      + "The Tempera Team");
    }
    catch (Exception e) {
      throw new RuntimeException("Registration email could not be sent");
    }
  }

  private String generateAndSaveActivationToken(Userx user) {
    String code = generateActivationCode(tokenLength);
    user.setPassword(encode.encode(code));
    userxService.saveUser(user);
    return code;
  }

  private String generateActivationCode(int length) {
    String chars = "0123456789";
    StringBuilder code = new StringBuilder();
    SecureRandom random = new SecureRandom();
    for (int i = 0; i < length; i++) {
      int index = random.nextInt(chars.length());
      code.append(chars.charAt(index));
    }
    return code.toString();
  }

  @Transactional
  @PreAuthorize("hasAuthority('ADMIN')")
  public void resendValidation(UserxDto userxDTO) {
    Userx user = userxService.convertToEntity(userxDTO);
    String password = generateAndSaveActivationToken(user);
    emailService.sendEmail(
        user.getEmail(),
        "Registration successful",
        "Hello "
            + user.getFirstName()
            + " "
            + user.getLastName()
            + ",\n\n"
            + "Your registration was successful. Your username is: "
            + user.getUsername()
            + "\n"
            + "Your password is: "
            + password
            + " \n\n"
            + "Please follow the link to set your password.\n\n"
            + "http://localhost:4200/validate/"
            + user.getUsername()
            + "\n\n"
            + "Best regards,\n"
            + "The Tempera Team");
  }

  public void validateUser(String username, String password) {
    Userx user = userxService.loadUser(username);
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    if (!encode.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("Password incorrect");
    }
  }

  public void setPassword(String username, String password, String passwordRepeat) {
    if (!password.equals(passwordRepeat)) {
      throw new IllegalArgumentException("Passwords do not match");
    }
    Userx user = userxService.loadUser(username);
    user.setPassword(encode.encode(password));
    userxService.saveUser(user);
  }
}
