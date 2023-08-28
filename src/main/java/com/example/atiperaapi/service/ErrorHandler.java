package com.example.atiperaapi.service;

import com.example.atiperaapi.exception.ToManyRequestsException;
import com.example.atiperaapi.exception.UserNotFoundException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class ErrorHandler {

    static Mono<? extends Throwable> handleNotFoundStatus(ClientResponse ex) {
        return Mono.error(new UserNotFoundException(GitHubServiceErrorMessages.USER_NOT_FOUND_MESSAGE));
    }
    static Mono<? extends Throwable> handleTooManyRequestsStatus(ClientResponse ex) {
        return Mono.error(new ToManyRequestsException(GitHubServiceErrorMessages.GITHUB_LIMIT_REACHED_MESSAGE));
    }

   static Mono<? extends Throwable> handleForbiddenStatus(ClientResponse ex) {
        return Mono.error(new ToManyRequestsException(GitHubServiceErrorMessages.GITHUB_FORBIDDEN_MESSAGE));
    }
}
