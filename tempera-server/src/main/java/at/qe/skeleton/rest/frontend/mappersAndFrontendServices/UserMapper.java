package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.dtos.SimpleUserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public SimpleUserDto getSimpleUser(Userx userx) {
        return new SimpleUserDto(
                userx.getUsername(),
                userx.getFirstName(),
                userx.getLastName(),
                userx.getEmail()
        );
    }


}
