package com.example.atiperaapi.controller;

import com.example.atiperaapi.out.ErrorResponseBody;
import com.example.atiperaapi.exception.ToManyRequestsException;
import com.example.atiperaapi.exception.UserNotFoundException;
import java.util.UUID;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected Mono<ResponseEntity<Object>> handleNotAcceptableStatusException(
            NotAcceptableStatusException ex, HttpHeaders headers, HttpStatusCode status,
            ServerWebExchange exchange) {
        String message = String.format("Accept header should be: %s", headers);
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.NOT_ACCEPTABLE.value(), message);

        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(errorResponseBody)
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponseBody> handleUserNotFound(UserNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ToManyRequestsException.class)
    protected ResponseEntity<ErrorResponseBody> handleToManyRequests(ToManyRequestsException ex) {
        return createErrorResponse(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponseBody> handleRuntimeException(RuntimeException ex) {
        final String referenceId = generateReferenceId();
        final String errorMessage = String.format("An internal error occurred. Please contact support with reference ID: %s", referenceId);

        final String logMessage = String.format("Reference ID: %s", referenceId);
        logger.error(logMessage, ex);

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    private ResponseEntity<ErrorResponseBody> createErrorResponse(HttpStatus status, String message) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(status.value(), message);
        return new ResponseEntity<>(errorResponseBody, status);
    }

    private String generateReferenceId() {
        return UUID.randomUUID().toString();
    }
}
