package at.qe.skeleton.exceptions;

import java.io.IOException;

public class ExternalRecordOutOfBoundsException extends IOException {

  public ExternalRecordOutOfBoundsException(String message) {
    super(message);
  }
}
