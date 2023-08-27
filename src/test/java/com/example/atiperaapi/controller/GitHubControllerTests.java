package com.example.atiperaapi.controller;

import com.example.atiperaapi.exception.UserNotFoundException;
import com.example.atiperaapi.out.BranchOut;
import com.example.atiperaapi.out.GitHubRepoOut;
import com.example.atiperaapi.service.GitHubService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
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
    private GitHubService gitHubService;

    @Test
    public void testGetUserRepositories_success() {
        // given
        BranchOut branch = new BranchOut("master", "test-sha");
        GitHubRepoOut repo = new GitHubRepoOut("test-name", "test-owner",  List.of(branch));

        final String URI_TO_TEST = String.format("/api/v1/github?username=%s", repo.owner());

        given(gitHubService.getUserRepositories(anyString())).willReturn(Flux.just(repo));

        // when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody(GitHubRepoOut.class)
                .isEqualTo(repo);
    }


    @Test
    public void testGetUserRepositories_notFound() {
        // given
        final String ERROR_MESSAGE = "User with given username does not exist";
        final String USER_NAME = "test";
        final String URI_TO_TEST = String.format("/api/v1/github?username=%s", USER_NAME);

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
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    public void testGetUserRepositories_successOnErrorInGetBranches() {
        // given
        GitHubRepoOut repo = new GitHubRepoOut("test-name", "test-owner", Collections.emptyList());

        final String URI_TO_TEST = String.format("/api/v1/github?username=%s", repo.owner());

        given(gitHubService.getUserRepositories(anyString())).willReturn(Flux.just(repo));
        given(gitHubService.getBranches(anyString(), anyString())).willReturn(Flux.error(new RuntimeException()));

        // when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody(GitHubRepoOut.class)
                .isEqualTo(repo);
    }

    @Test
    public void testGetUserRepositories_notAcceptable() {
        // given
        final String USER_NAME = "test";
        final String URI_TO_TEST = String.format("/api/v1/github?username=%s", USER_NAME);

        // when
        webTestClient
                .get()
                .uri(URI_TO_TEST)
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                // then
                .expectStatus().isEqualTo(406);

        Mockito.verify(gitHubService, never()).getUserRepositories(any());
    }
}
