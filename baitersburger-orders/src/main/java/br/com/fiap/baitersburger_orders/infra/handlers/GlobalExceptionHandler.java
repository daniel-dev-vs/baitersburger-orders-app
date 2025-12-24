package br.com.fiap.baitersburger_orders.infra.handlers;

import br.com.fiap.baitersburger_orders.application.exception.NotFoundException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            NotFoundException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse body = ErrorResponse.create(ex, status, ex.getMessage());

        return ResponseEntity.status(status).body(body);
    }


    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            FeignException ex,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.valueOf(ex.status());

        ErrorResponse body = ErrorResponse.create(ex, status, ex.getMessage());

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse body = ErrorResponse.create(ex, status, ex.getMessage());

        return ResponseEntity.status(status).body(body);
    }
}