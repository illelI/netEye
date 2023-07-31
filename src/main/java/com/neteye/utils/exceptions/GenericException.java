package com.neteye.utils.exceptions;

import lombok.Getter;

public class GenericException extends RuntimeException{
    @Getter
    private int code;

    public GenericException(String message) {
        this(message, 400);
    }

    public GenericException(String message, int code) {
        super(message);
        this.code = code;
    }
}
