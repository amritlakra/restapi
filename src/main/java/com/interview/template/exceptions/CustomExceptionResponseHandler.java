package com.interview.template.exceptions;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@RestController
public class CustomExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(InvalidDataAccessApiUsageException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservedUsernameException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(ReservedUsernameException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public final ResponseEntity<Object> handleDataIntegrityViolationException(DuplicateUserException ex, WebRequest request) {
        return createErrorResponse(ex.getMessage(), request.getDescription(false), HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return createErrorResponse("Validation Failed", request.getDescription(false), errors, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> createErrorResponse(String message, String details, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), message, details, new ArrayList<>());
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }

    private ResponseEntity<Object> createErrorResponse(String message, String details, List<String> errors, HttpStatus httpStatus) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), message, details, errors);
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
}
