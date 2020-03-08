package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class AlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 797400378464492181L;

    public AlreadyExistException() {
    }

    public AlreadyExistException(String message) {
        super(message);
    }
}
