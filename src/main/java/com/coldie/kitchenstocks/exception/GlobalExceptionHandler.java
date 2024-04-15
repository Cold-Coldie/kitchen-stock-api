package com.coldie.kitchenstocks.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        // Create a custom ErrorResponse object

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());

        ErrorResponse response = new ErrorResponse("404", "User Not Found", messages);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(ItemNotFoundException ex) {
        // Create a custom ErrorResponse object

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());

        ErrorResponse response = new ErrorResponse("404", "Item Not Found", messages);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MeasuringUnitNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(MeasuringUnitNotFoundException ex) {
        // Create a custom ErrorResponse object

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());

        ErrorResponse response = new ErrorResponse("404", "Measuring Unit Not Found", messages);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        // Create a custom ErrorResponse object

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());

        ErrorResponse response = new ErrorResponse("409", "User Already Exists", messages);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleItemAlreadyExistsException(ItemAlreadyExistsException ex) {
        // Create a custom ErrorResponse object

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());

        ErrorResponse response = new ErrorResponse("409", "Item Already Exists", messages);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MeasuringUnitAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleMeasuringUnitAlreadyExistsException(MeasuringUnitAlreadyExistsException ex) {
        // Create a custom ErrorResponse object

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());

        ErrorResponse response = new ErrorResponse("409", "Measuring Unit Already Exists", messages);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnexpectedErrorException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedErrorException(UnexpectedErrorException ex) {
        // Create a custom ErrorResponse object

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());

        ErrorResponse response = new ErrorResponse("500", "Internal Server Error", messages);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError
                        .getDefaultMessage()
                )
                .collect(Collectors.toList());


        ErrorResponse response = new ErrorResponse("" + status.value(), "Bad Request", errors);
        return new ResponseEntity<>(response, status);
    }
}
