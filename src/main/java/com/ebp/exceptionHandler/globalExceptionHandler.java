package com.ebp.exceptionHandler;

import com.ebp.helper.ebpResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validatorHandler(MethodArgumentNotValidException exception){
        Map<String, String> details = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(e->{
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            details.put(field, message);
        });
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(detailsNotAvailableException.class)
    public ResponseEntity<ebpResponse> detailsNotAvailableExceptionHandler(detailsNotAvailableException exception){
        String message = exception.getMessage();
        return new ResponseEntity<>(new ebpResponse(message, false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(alreadyActivated.class)
    public ResponseEntity<ebpResponse> alreadyActivatedHandler(alreadyActivated exception){
        String message = exception.getMessage();
        return new ResponseEntity<>(new ebpResponse(message, false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(multipleConnectionByConsumerNo.class)
    public ResponseEntity<ebpResponse> multipleConnectionByConsumerNoHandler(multipleConnectionByConsumerNo exception){
        String message = exception.getMessage();
        return new ResponseEntity<>(new ebpResponse(message, false), HttpStatus.MULTI_STATUS);
    }

    @ExceptionHandler(uniqueConstraintsException.class)
    public ResponseEntity<ebpResponse> uniqueConstraintHandler(uniqueConstraintsException exception){
        String message = exception.getMessage();
        return new ResponseEntity<>(new ebpResponse(message, false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> uniqueConstraintHandler(MethodArgumentTypeMismatchException exception){
        Map<String, String> message = new HashMap<>();
        String name = exception.getName();
        String reason = "Please enter correct URI";
        message.put(name, reason);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(multipleDateHandler.class)
    public ResponseEntity<ebpResponse> multipleDateHandler(multipleDateHandler exception){
        String message = exception.getMessage();
        return new ResponseEntity<>(new ebpResponse(message, false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ebpResponse> messageNotReadableHandler(HttpMessageNotReadableException exception){
        String message = "Please check your values. Connection Type must be one of the given values : NON_INDUSTRIAL, INDUSTRIAL, AGRICULTURAL";
        return new ResponseEntity<>(new ebpResponse(message, false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(authorizationException.class)
    public ResponseEntity<ebpResponse> authorizationExceptionHandler(authorizationException exception){
        String message = exception.getMessage();
        return new ResponseEntity<>(new ebpResponse(message, false), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> constraintViolationExceptionHandler(ConstraintViolationException exception){
        Map<String, String> details = new HashMap<>();
        exception.getConstraintViolations().forEach(e->{
            String field = ((FieldError)e).getField();
            String message = ((FieldError) e).getDefaultMessage();
            details.put(field, message);
        });
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public  ResponseEntity<ebpResponse> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception){
        String message = exception.getMessage();
        return new ResponseEntity<>(new ebpResponse("Check for Duplicate input", true), HttpStatus.BAD_REQUEST);
    }
}
