package com.example.atiperaapi.controller;

import com.example.atiperaapi.exception.NotSupportedHeaderException;
import com.example.atiperaapi.model.GitHubRepo;
import com.example.atiperaapi.service.GitHubService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/github")
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping
    public List<GitHubRepo> getUsersRepositories(
            @RequestParam String username,
            @RequestHeader(name = "Accept") String acceptHeader) throws IOException {
        if ("application/xml".equals(acceptHeader)) {
            throw new NotSupportedHeaderException("The application does not support XML response");
        }
        return gitHubService.getUsersRepositories(username);
    }
}
