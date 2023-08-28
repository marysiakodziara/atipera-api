package com.example.atiperaapi.controller;

import com.example.atiperaapi.exception.ErrorResponseBody;
import com.example.atiperaapi.exception.GitHubResponseException;
import com.example.atiperaapi.exception.ToManyRequestsException;
import com.example.atiperaapi.exception.UserNotFoundException;
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
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GitHubResponseException.class)
    protected ResponseEntity<ErrorResponseBody> handleGitHubResponse(GitHubResponseException ex) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ToManyRequestsException.class)
    protected ResponseEntity<ErrorResponseBody> handleToManyRequests(ToManyRequestsException ex) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(HttpStatus.TOO_MANY_REQUESTS.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.TOO_MANY_REQUESTS);
    }
}
