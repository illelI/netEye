package com.neteye.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException{
    @Getter
    private final HttpStatus httpStatus;

    public GenericException(String message) {
        this(message, 400);
    }

    public GenericException(String message, int code) {
        super(message);
        this.httpStatus = HttpStatus.valueOf(code);
    }
}
