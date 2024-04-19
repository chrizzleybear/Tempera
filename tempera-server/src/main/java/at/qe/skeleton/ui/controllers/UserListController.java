package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.model.Userx;
import java.io.Serializable;
import java.util.Collection;

import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user list view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
@Component
@Scope("view")
public class UserListController implements Serializable {

    @Autowired
    private UserxService userxService;

    /**
     * Returns a list of all users.
     *
     * @return
     */
    public Collection<Userx> getUsers() {
        return userxService.getAllUsers();
    }

}
