package com.example.atiperaapi.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        String message = "Accept header should be: " + headers;
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.NOT_ACCEPTABLE.value(), message);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .header("Content-Type", "application/json")
                .body(errorResponseBody));
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponseBody> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.NOT_FOUND);
    }

}
