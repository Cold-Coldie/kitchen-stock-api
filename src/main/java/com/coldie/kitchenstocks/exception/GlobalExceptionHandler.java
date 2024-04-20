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
    @ExceptionHandler({
            UserNotFoundException.class,
            ItemNotFoundException.class,
            MeasuringUnitNotFoundException.class,
            UserAlreadyExistsException.class,
            ItemAlreadyExistsException.class,
            MeasuringUnitAlreadyExistsException.class,
            UnexpectedErrorException.class
    })

    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        HttpStatus status = getStatus(ex);

        List<String> messages = new ArrayList<>();
        messages.add(ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                String.valueOf(status.value()),
                status.getReasonPhrase(),
                messages
        );
        return new ResponseEntity<>(response, status);
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

    private HttpStatus getStatus(Exception ex) {
        if (ex instanceof UserNotFoundException ||
                ex instanceof ItemNotFoundException ||
                ex instanceof MeasuringUnitNotFoundException
        ) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof UserAlreadyExistsException ||
                ex instanceof ItemAlreadyExistsException ||
                ex instanceof MeasuringUnitAlreadyExistsException) {
            return HttpStatus.CONFLICT;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
