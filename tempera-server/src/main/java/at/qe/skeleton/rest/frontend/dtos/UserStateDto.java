package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.State;

public record UserStateDto (String username, State state){}
