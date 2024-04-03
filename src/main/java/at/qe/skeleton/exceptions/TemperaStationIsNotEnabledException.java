package at.qe.skeleton.exceptions;

import java.io.IOException;

public class TemperaStationIsNotEnabledException extends IOException {
    public TemperaStationIsNotEnabledException (String message) {
        super(message);
    }
    public TemperaStationIsNotEnabledException () {
        super();
    }


}
