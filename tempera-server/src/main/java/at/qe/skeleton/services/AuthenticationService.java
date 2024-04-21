package at.qe.skeleton.services;

import at.qe.skeleton.model.DTOs.UserDTO;
import at.qe.skeleton.model.Userx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("application")
public class AuthenticationService {

    @Autowired private UserxService userxService;

    @Transactional
    public void registerUser(UserDTO userDTO) {
        Userx newUser=userxService.convertToEntity(userDTO);
        String password = newUser.getPassword();
        userxService.saveUser(newUser);




    }

}
