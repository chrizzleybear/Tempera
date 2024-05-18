package at.qe.skeleton.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimestampToolkit {

    /**
     * This service-method truncates the given timestamp to milliseconds
     * because the db may potentially alter smaller scale timestamps.
     * @param timestamp
     * @return
     */
    public static LocalDateTime timeStampClipper(LocalDateTime timestamp){
        return timestamp.truncatedTo(ChronoUnit.MILLIS);
    }
}
