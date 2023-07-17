package com.neteye.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GenericResponse {
    @Getter @Setter
    private String message;
    @Getter @Setter
    private String error;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse(List<ObjectError> errors, String error) {
        this.error = error;
        String tmp = errors.stream().map( e -> {
                    if (e instanceof FieldError) {
                        return "{\"field\":\"" + ((FieldError) e).getField() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
                    }
                    else {
                        return "{\"object\":\"" + e.getObjectName() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
                    }
                }
        ).collect(Collectors.joining(","));
        this.message = "[" + tmp + "]";
    }
}
