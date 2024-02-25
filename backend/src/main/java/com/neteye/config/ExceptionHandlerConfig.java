package com.neteye.config;

import com.neteye.utils.exceptions.GenericException;
import com.neteye.utils.exceptions.UserAlreadyExistsException;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistsException ex, WebRequest request) {
        logger.error("UserAlreadyExistsException occurred");
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<Object> handleGenericException(GenericException ex, WebRequest request) {
        logger.error(new ParameterizedMessage("GenericException occurred; {}", ex.getMessage()));
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        logger.error("UsernameNotFoundException occurred");
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleOtherExceptions(RuntimeException ex, WebRequest request) {
        logger.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
