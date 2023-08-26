package com.example.atiperaapi.service;

import com.example.atiperaapi.exception.UserNotFoundException;
import com.example.atiperaapi.model.Branch;
import com.example.atiperaapi.model.GitHubRepo;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

    public Flux<GitHubRepo> getUserRepositories(String username) {
        String url = String.format(GITHUB_API_REPO_URL, username);
        return webClient
                .get()
                .uri(url).retrieve().onStatus(
                HttpStatus.NOT_FOUND::equals,
                clientResponse -> Mono.error(new UserNotFoundException("User with given username does not exist")))
                .bodyToFlux(JsonNode.class)
                .filter(repo -> !repo.get("fork").asBoolean())
                .flatMap(repo -> {
                    String repoName = repo.get("name").asText();
                    String owner = repo.get("owner").get("login").asText();
                    Flux<Branch> branches = getBranches(owner, repoName)
                            .subscribeOn(Schedulers.parallel());

                    return branches.collectList()
                            .zipWith(Mono.just(owner), (branchList, repoOwner) ->
                                    GitHubRepo.builder()
                                            .name(repoName)
                                            .owner(repoOwner)
                                            .branches(branchList)
                                            .build()
                            );
                });
    }

    public Flux<Branch> getBranches(String username, String repositoryName) {
        String url = String.format(GITHUB_API_BRANCHES_URL, username, repositoryName);
        return webClient.get().uri(url).retrieve()
                .bodyToFlux(JsonNode.class)
                .onErrorResume(ex -> Mono.empty())
                .map(branch -> Branch.builder()
                    .name(branch.get("name").asText())
                    .lastCommitSha(branch.get("commit").get("sha").asText())
                    .build());
    }
}
