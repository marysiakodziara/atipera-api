package com.example.atiperaapi.controller;

import com.example.atiperaapi.model.GitHubRepo;
import com.example.atiperaapi.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/github")
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping(headers = "Accept=application/json")
    public Flux<GitHubRepo> getUsersRepositories(
            @RequestParam String username) {
        return gitHubService.getUserRepositories(username);
    }
}
