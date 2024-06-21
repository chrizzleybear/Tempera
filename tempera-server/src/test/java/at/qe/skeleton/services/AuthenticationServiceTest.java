package at.qe.skeleton.services;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.LogAffectedType;
import at.qe.skeleton.model.enums.LogEvent;
import at.qe.skeleton.model.enums.UserxRole;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.PasswordAuthentication;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationServiceTest {

    @Mock private UserxService userxService;
    @Mock private EmailService emailService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuditLogService auditLogService;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(auditLogService.logEvent(any(LogEvent.class), any(LogAffectedType.class), any(String.class))).thenReturn(true);
        this.authenticationService =
                new AuthenticationService(
                        userxService,
                        emailService,
                        passwordEncoder,
                        auditLogService
                );
    }

    @Test
    void testRegisterUser() {
        UserxDto userDto = new UserxDto("u", "p", "e", "f", "l", true, Set.of(UserxRole.MANAGER));
        Userx user = new Userx();
        user.setId("u");
        user.setEmail("e");
        when(userxService.convertToEntity(any(UserxDto.class))).thenReturn(user);
        when(userxService.loadUser(userDto.username())).thenReturn(null);
        when(userxService.saveUser(user)).thenReturn(user);
        when(userxService.convertToDTO(user)).thenReturn(userDto);

        UserxDto result = authenticationService.registerUser(userDto);

        assertNotNull(result);
        verify(userxService, atLeastOnce()).saveUser(user);
        verify(auditLogService, atLeastOnce()).logEvent(any(), any(), any());
        verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendValidationEmail() {
        Userx user = new Userx();
        user.setId("u");
        user.setEmail("e");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        authenticationService.sendValidationEmail(user);

        verify(emailService).sendEmail(anyString(), anyString(), anyString());
        verify(auditLogService, atLeastOnce()).logEvent(any(), any(), any());
    }

    @Test
    void testResendValidation() {
        UserxDto userDto = new UserxDto("u", "p", "e", "f", "l", true, Set.of(UserxRole.MANAGER));
        Userx user = new Userx();
        user.setId("u");
        user.setEmail("e");
        when(userxService.loadUser(userDto.username())).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        authenticationService.resendValidation(userDto);

        verify(emailService).sendEmail(anyString(), anyString(), anyString());
        verify(auditLogService, atLeastOnce()).logEvent(any(), any(), any());
    }

}
