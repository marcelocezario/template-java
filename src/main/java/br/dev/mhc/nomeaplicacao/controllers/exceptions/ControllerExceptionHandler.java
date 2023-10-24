package br.dev.mhc.nomeaplicacao.controllers.exceptions;

import br.dev.mhc.nomeaplicacao.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Resource not found")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException e,HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError error = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Data integrity violation")
                .message(isNull(e.getMessage()) ? null : e.getMessage().split(";")[0])
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationError(MethodArgumentNotValidException e,
                                                         HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError error = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Validation error")
                .message(e.getMessage())
                .path(request.getRequestURI()).build();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(error);
    }

    /*
     * Implementar novos handlers nas linhas acima
     * Não implementar nenhum handler após o método genericHandler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericHandler(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError error = StandardError.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Error")
                .message("There was an unexpected error")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
    }
    /*
     * Não implementar nenhum handler após o método genericHandler
     */
}
