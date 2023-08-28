package com.example.atiperaapi.service;

import com.example.atiperaapi.model.Branch;
import com.example.atiperaapi.out.GitHubRepoOut;
import reactor.core.publisher.Flux;

public interface GitHubService {
    Flux<GitHubRepoOut> getUserRepositories(String username);
    Flux<Branch> getBranches(String username, String repositoryName);
}
