package com.example.atiperaapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "github.api")
public record GitHubApiProperties(String repoUrl, String branchesUrl) {}
