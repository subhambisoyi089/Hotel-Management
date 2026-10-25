package com.hotel.exception;

/** A problem the user can fix (e.g. room already booked). The message is shown on screen. */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
