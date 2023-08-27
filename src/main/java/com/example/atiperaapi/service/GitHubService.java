package com.example.atiperaapi.service;

import com.example.atiperaapi.exception.UserNotFoundException;
import com.example.atiperaapi.model.Branch;
import com.example.atiperaapi.model.GitHubRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
                .map(node -> mapper.convertValue(node, GitHubRepo.class))
                .flatMap(repo -> {
                    Flux<Branch> branches = getBranches(repo.getOwner().login, repo.getName())
                            .subscribeOn(Schedulers.parallel());

                    return branches.collectList()
                            .zipWith(Mono.just(repo.getOwner().login), (branchList, repoOwner) ->
                                    {
                                        repo.setBranches(branchList);
                                        return repo;
                                    }
                            );
                });
    }

    public Flux<Branch> getBranches(String username, String repositoryName) {
        String url = String.format(GITHUB_API_BRANCHES_URL, username, repositoryName);
        return webClient.get().uri(url).retrieve()
                .bodyToFlux(JsonNode.class)
                .onErrorResume(ex -> Mono.empty())
                .map(node -> mapper.convertValue(node, Branch.class));
    }
}
