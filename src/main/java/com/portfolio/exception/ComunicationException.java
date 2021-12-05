package com.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ComunicationException extends RuntimeException{

        public ComunicationException(String exception){ super(exception); }
}
