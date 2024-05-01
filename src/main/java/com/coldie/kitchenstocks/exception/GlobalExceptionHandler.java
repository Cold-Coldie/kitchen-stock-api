package com.coldie.kitchenstocks.exception;

import com.coldie.kitchenstocks.config.exception.NotAuthenticatedException;
import com.coldie.kitchenstocks.item.exception.ItemAlreadyExistsException;
import com.coldie.kitchenstocks.item.exception.ItemNotFoundException;
import com.coldie.kitchenstocks.measuringUnit.exception.MeasuringUnitAlreadyExistsException;
import com.coldie.kitchenstocks.measuringUnit.exception.MeasuringUnitNotFoundException;
import com.coldie.kitchenstocks.user.exception.UserAlreadyExistsException;
import com.coldie.kitchenstocks.user.exception.UserNotFoundException;
import org.springframework.http.*;
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
            NotAuthenticatedException.class,
            InvalidRequest.class,
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


        ErrorResponse response = new ErrorResponse(String.valueOf(status.value()), HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
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
                ex instanceof MeasuringUnitAlreadyExistsException
        ) {
            return HttpStatus.CONFLICT;
        } else if (ex instanceof NotAuthenticatedException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof  InvalidRequest) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
