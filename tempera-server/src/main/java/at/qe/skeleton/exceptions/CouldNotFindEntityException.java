package at.qe.skeleton.exceptions;

import java.io.IOException;

public class CouldNotFindEntityException extends IOException {
    public CouldNotFindEntityException(String message){
        super(message);
    }
}
