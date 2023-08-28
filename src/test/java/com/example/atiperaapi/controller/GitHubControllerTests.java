package com.example.atiperaapi.controller;

import com.example.atiperaapi.exception.ToManyRequestsException;
import com.example.atiperaapi.exception.UserNotFoundException;
import com.example.atiperaapi.factory.DataFactory;
import com.example.atiperaapi.out.GitHubRepoOut;
import com.example.atiperaapi.service.BaseGitHubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GitHubControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BaseGitHubService gitHubService;

    private static final String URL_BASE = "/api/v1/github?username={username}";

    @Test
    public void testGetUserRepositories_success() {
        // given
        GitHubRepoOut repo = DataFactory.randomGitHubRepoOut();
        final String URI_TO_TEST = buildUri(DataFactory.getRandomString(10));

        given(gitHubService.getUserRepositories(anyString())).willReturn(Flux.just(repo));

        // when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBodyList(GitHubRepoOut.class)
                .hasSize(1)
                .contains(repo);
    }

    @Test
    public void testGetUserRepositories_userWithZeroRepositories() {
        // given
        final String USER_NAME = DataFactory.getRandomString(10);
        final String URI_TO_TEST = buildUri(DataFactory.getRandomString(10));

        given(gitHubService.getUserRepositories(anyString())).willReturn(Flux.empty());

        // when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBodyList(GitHubRepoOut.class)
                .hasSize(0);
    }


    @Test
    public void testGetUserRepositories_notFound() {
        // given
        final String ERROR_MESSAGE = DataFactory.getRandomString(10);
        final String URI_TO_TEST = buildUri(DataFactory.getRandomString(10));

        given(gitHubService.getUserRepositories(anyString()))
                .willReturn(Flux.error(new UserNotFoundException(ERROR_MESSAGE)));

        // when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .exchange()
                // then
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo(ERROR_MESSAGE)
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testGetUserRepositories_notAcceptable() {
        // given
        final String URI_TO_TEST = buildUri(DataFactory.getRandomString(10));

        // when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                // then
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());

        Mockito.verify(gitHubService, never()).getUserRepositories(anyString());
    }

    @Test
    public void testGetUserRepositories_errorResponseBodyPresentOnError() {
        //given
        final String URI_TO_TEST = buildUri(DataFactory.getRandomString(10));
        final String ERROR_MESSAGE = DataFactory.getRandomString(10);

        given(gitHubService.getUserRepositories(anyString()))
                .willReturn(Flux.error(new RuntimeException(ERROR_MESSAGE)));
        //when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .exchange()
                //then
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    public void testGetUserRepositories_toManyRequests() {
        // given
        final String ERROR_MESSAGE = DataFactory.getRandomString(10);
        final String URI_TO_TEST = buildUri(DataFactory.getRandomString(10));

        given(gitHubService.getUserRepositories(anyString()))
                .willReturn(Flux.error(new ToManyRequestsException(ERROR_MESSAGE)));

        // when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .exchange()
                // then
                .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value())
                .expectBody()
                .jsonPath("$.message").isEqualTo(ERROR_MESSAGE)
                .jsonPath("$.status").isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());
    }

    private String buildUri(String username){
        return UriComponentsBuilder.fromUriString(URL_BASE)
                .buildAndExpand(username)
                .toUriString();
    }
}
