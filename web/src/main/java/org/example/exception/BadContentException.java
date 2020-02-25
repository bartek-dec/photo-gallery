package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadContentException extends RuntimeException {

    private static final long serialVersionUID = 7160619472218733553L;

    public BadContentException() {
        super();
    }

    public BadContentException(String message) {
        super(message);
    }
}
