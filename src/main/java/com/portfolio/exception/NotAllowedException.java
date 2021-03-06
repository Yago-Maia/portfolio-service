package com.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAllowedException extends RuntimeException {

    public NotAllowedException(String exception) {
        super(exception);
    }
}
