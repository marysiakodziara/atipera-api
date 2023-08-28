package com.example.atiperaapi.service;

import com.example.atiperaapi.config.GitHubApiProperties;
import com.example.atiperaapi.model.Branch;
import com.example.atiperaapi.model.GitHubRepo;
import com.example.atiperaapi.out.BranchOut;
import com.example.atiperaapi.out.GitHubRepoOut;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Service
public class BaseGitHubService implements GitHubService {

    private final WebClient webClient;
    private final GitHubApiProperties gitHubApiProperties;

    public BaseGitHubService(WebClient.Builder webClientBuilder, GitHubApiProperties gitHubApiProperties) {
        this.webClient = webClientBuilder.build();
        this.gitHubApiProperties = gitHubApiProperties;
    }

    @Override
    public Flux<GitHubRepoOut> getUserRepositories(String username) {
        String URL = UriComponentsBuilder.fromUriString(gitHubApiProperties.repoUrl())
                .buildAndExpand(username)
                .toUriString();

        Flux<GitHubRepo> repoFlux = webClient
                .get()
                .uri(URL)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, ErrorHandler::handleNotFoundStatus)
                .onStatus(HttpStatus.FORBIDDEN::equals, ErrorHandler::handleForbiddenStatus)
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, ErrorHandler::handleTooManyRequestsStatus)
                .bodyToFlux(GitHubRepo.class)
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

    @Override
    public Flux<Branch> getBranches(String username, String repositoryName) {
        String URL = UriComponentsBuilder.fromUriString(gitHubApiProperties.branchesUrl())
                .buildAndExpand(username, repositoryName)
                .toUriString();

        return webClient
                .get()
                .uri(URL)
                .retrieve()
                .onStatus(HttpStatus.FORBIDDEN::equals, ErrorHandler::handleForbiddenStatus)
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, ErrorHandler::handleTooManyRequestsStatus)
                .bodyToFlux(Branch.class);
    }
}