package com.example.atiperaapi.service;

import com.example.atiperaapi.exception.GitHubResponseException;
import com.example.atiperaapi.exception.ToManyRequestsException;
import com.example.atiperaapi.exception.UserNotFoundException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class ErrorHandler {

    public static Throwable handleProcessingErrors(Throwable ex) {
        if (ex instanceof UserNotFoundException) {
            return new UserNotFoundException(GitHubServiceErrorMessages.USER_NOT_FOUND_MESSAGE);
        } else if (ex instanceof ToManyRequestsException) {
            return new ToManyRequestsException(GitHubServiceErrorMessages.GIT_HUB_LIMIT_REACHED_MESSAGE);
        } else {
            return new GitHubResponseException(GitHubServiceErrorMessages.REPOSITORY_RETRIEVAL_ERROR_MESSAGE);
        }
    };

    static Mono<? extends Throwable> handleNotFoundStatus(ClientResponse ex) {
        return Mono.error(new UserNotFoundException(GitHubServiceErrorMessages.USER_NOT_FOUND_MESSAGE));
    }
    static Mono<? extends Throwable> handleTooManyRequestsStatus(ClientResponse ex) {
        return Mono.error(new ToManyRequestsException(GitHubServiceErrorMessages.GIT_HUB_LIMIT_REACHED_MESSAGE));
    }

   static Mono<? extends Throwable> handleForbiddenStatus(ClientResponse ex) {
        return Mono.error(new UserNotFoundException(GitHubServiceErrorMessages.GIT_HUB_LIMIT_REACHED_MESSAGE));
    }
}
