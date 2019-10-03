package io.tools.trellobacklogsaggregator.controllers;

import io.tools.trellobacklogsaggregator.bean.ErrorDetails;
import io.tools.trellobacklogsaggregator.execptions.BoardException;
import io.tools.trellobacklogsaggregator.execptions.NumericException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class ExceptionHandlerController  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BoardException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(BoardException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(ConstraintViolationException ex, WebRequest request) {
        final Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Set<String> messages = new HashSet<>(constraintViolations.size());
        messages.addAll(constraintViolations.stream()
                .map(constraintViolation -> String.format("{ \"property\": \"%s\" ,\"value\": \"%s\", \"msg\": \"%s\"}", constraintViolation.getPropertyPath(),
                        constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
                .collect(Collectors.toList()));

        ErrorDetails errorDetails = new ErrorDetails(new Date(), "ConstraintViolationException",
                request.getDescription(false), messages);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumericException.class)
    public final ResponseEntity<ErrorDetails> handleNumericFalse(NumericException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }
}
