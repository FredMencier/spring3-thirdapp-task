package ch.heg.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse notFound(NoSuchElementException ex) {
        ErrorResponse.Builder builder = ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage());
        builder.title("Item not Found");
        builder.type(URI.create("https://taskApplication/errors"));
        return builder.build();
    }
}
