package at.qe.skeleton.exceptions;

import java.io.IOException;

/**
 * Exception thrown when an object relation is inconsistent within the DB or between DB and API
 * requests, eg. when a MeasurementDto Object specifies a TemperaStation and an AccessPoint, but the
 * TemperaStation is not part of the AccessPoint.
 */
public class InconsistentObjectRelationException extends IOException {
  public InconsistentObjectRelationException(String message) {
    super(message);
  }
}
