package com.blog.exception;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandeling extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandeling.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {  // What is the use of web request

        List<String> details = new ArrayList<>(); // Why we have taken list to store our message
        details.add(ex.getLocalizedMessage());
        ErrorDetails error = new ErrorDetails( // OBJECT in which we store our error message
                new Date(),
                "Server Error",
                details,
                request.getDescription(false), //what is the purpose of this
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        //String cause = ExceptionUtils.getStackTrace(ex);
        //logger.info("handleAllExceptions -> cause = "+cause);

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webReq) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDetails error = new ErrorDetails(
                new Date(),
                "Record Not Found",
                details,
                webReq.getDescription(false),
                HttpStatus.NOT_FOUND.value()
        );

        //String cause = ExceptionUtils.getStackTrace(ex);
        //log.info("handleResourceNotFoundException -> cause = "+cause);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<Object> handleRollbackException(RollbackException ex, WebRequest webReq) {

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDetails error = new ErrorDetails(
                new Date(),
                "Record Not Found",
                details,
                webReq.getDescription(false),
                HttpStatus.NOT_FOUND.value()
        );

        //String cause = ExceptionUtils.getStackTrace(ex);
        //log.info("handleResourceNotFoundException -> cause = "+cause);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDetails error = new ErrorDetails(new Date(),"Record Not Found", details, request.getDescription(false),HttpStatus.NOT_FOUND.value());
        //String cause = ExceptionUtils.getStackTrace(ex);
        //log.info("handleResourceNotFoundException -> cause = "+cause);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest webReq) {

        List<String> details = new ArrayList<>();

        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }

        ErrorDetails error = new ErrorDetails (
                new Date(),
                "Record Not Found",
                details,
                webReq.getDescription(false),
                HttpStatus.NOT_FOUND.value()
        );

        //String cause = ExceptionUtils.getStackTrace(ex);
        //log.info("handleMethodArgumentNotValid -> cause = "+cause);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}

/*
    private ApiErrorResponse getErrorResponse(HttpStatus status, String errorMessage) {
        if (StringUtils.isEmpty(errorMessage)) {
            errorMessage = "An unexpected error occurred";
        }
        return new ApiErrorResponse(status.value(), status.name(), errorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String ERROR_MESSAGE = ex.getMessage();

        try {
            ERROR_MESSAGE = ex.getBindingResult().getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        } catch (Exception e) {
            logger.error("Error constructing error message", e);
        }

        logger.error(ERROR_MESSAGE, ex);

        ApiErrorResponse errorResponse = getErrorResponse(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);

        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest webReq) {
        final String ERROR_MESSAGE = ex.getMessage();
        logger.error(ERROR_MESSAGE, ex);
        ApiErrorResponse errorResponse = getErrorResponse(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, webReq);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<Object> handleRollbackException(RollbackException ex, WebRequest webReq) {
        final String ERROR_MESSAGE = ex.getMessage();
        logger.error(ERROR_MESSAGE, ex);
        ApiErrorResponse errorResponse = getErrorResponse(HttpStatus.BAD_REQUEST, ERROR_MESSAGE);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, webReq);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest webReq) {
        final String ERROR_MESSAGE = "An unexpected error occurred";
        logger.error(ERROR_MESSAGE, ex);
        ApiErrorResponse errorResponse = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                webReq);
    }*/


