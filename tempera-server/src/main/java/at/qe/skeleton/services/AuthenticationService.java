package at.qe.skeleton.services;

import at.qe.skeleton.model.DTOs.UserDTO;
import at.qe.skeleton.model.Userx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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

    @Transactional
    public void registerUser(UserDTO userDTO) {
        Userx newUser=userxService.convertToEntity(userDTO);
        userxService.saveUser(newUser);
        sendValidationEmail(newUser);

    }
    //Encode username for security
    public void sendValidationEmail(Userx user) {
        String password = generateAndSaveActivationToken(user);
        emailService.sendEmail(user.getEmail(), "Registration successful", "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
                "Your registration was successful. Your username is: " + user.getUsername() + "\n" +
                "Your password is: " + password + " \n\n" +
                "Please follow the link to set your password.\n\n" +
                "http://localhost:4200/register/" + user.getUsername() + "\n\n" +
                "Best regards,\n" +
                "The Tempera Team");
    }

    private String generateAndSaveActivationToken(Userx user) {
        String code = generateActivationCode(6);
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
}

