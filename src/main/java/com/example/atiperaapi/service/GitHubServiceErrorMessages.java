package com.example.atiperaapi.service;

public final class GitHubServiceErrorMessages {
    public final static String USER_NOT_FOUND_MESSAGE = "User with given username does not exist";
    public final static String GITHUB_LIMIT_REACHED_MESSAGE = "GitHub API rate limit exceeded. Please try again later.";
    public final static String GITHUB_FORBIDDEN_MESSAGE = "This request exceeded Github API rate limit. Please try again later.";

    private GitHubServiceErrorMessages() {}
}
