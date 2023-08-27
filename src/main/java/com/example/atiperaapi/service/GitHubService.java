package com.example.atiperaapi.service;

import com.example.atiperaapi.exception.GitHubResponseException;
import com.example.atiperaapi.exception.UserNotFoundException;
import com.example.atiperaapi.model.Branch;
import com.example.atiperaapi.model.GitHubRepo;
import com.example.atiperaapi.out.BranchOut;
import com.example.atiperaapi.out.GitHubRepoOut;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GitHubService {

    private final WebClient webClient;

    public GitHubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Value("${github.api.repo.url}")
    private String GITHUB_API_REPO_URL;

    @Value("${github.api.branches.url}")
    private String GITHUB_API_BRANCHES_URL;

    public Flux<GitHubRepoOut> getUserRepositories(String username) {
        String URL = UriComponentsBuilder.fromUriString(GITHUB_API_REPO_URL)
                .buildAndExpand(username)
                .toUriString();

        Flux<GitHubRepo> repoFlux = webClient
                .get()
                .uri(URL)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::equals,
                        clientResponse -> Mono
                                .error(new UserNotFoundException(GitHubServiceErrorMessages
                                        .USER_NOT_FOUND_MESSAGE)))
                .bodyToFlux(GitHubRepo.class)
                .onErrorResume(ex -> {
                    if (ex instanceof UserNotFoundException)
                        return Mono.error(ex);
                    else
                        return Mono
                                .error(new GitHubResponseException(GitHubServiceErrorMessages
                                        .REPOSITORY_RETRIEVAL_ERROR_MESSAGE));
                })
                .filter(repo -> !repo.fork());

        Flux<List<Branch>> branchesFlux =
                repoFlux.flatMap(repo -> getBranches(repo.owner().login(), repo.name()).collectList());

        return repoFlux.zipWith(branchesFlux, (repo, branches) -> {
            List<BranchOut> branchOuts = branches.stream()
                    .map(branch -> new BranchOut(branch.name(), branch.commit().sha()))
                    .toList();
            return new GitHubRepoOut(repo.name(), repo.owner().login(), branchOuts);
        });
    }

    public Flux<Branch> getBranches(String username, String repositoryName) {
        final String URL = UriComponentsBuilder.fromUriString(GITHUB_API_BRANCHES_URL)
                .buildAndExpand(username, repositoryName)
                .toUriString();

        return webClient.get().uri(URL).retrieve()
                .bodyToFlux(Branch.class)
                .onErrorResume(ex -> Mono.error(new GitHubResponseException(GitHubServiceErrorMessages.BRANCHES_RETRIEVAL_ERROR_MESSAGE)));
    }
}