package com.example.atiperaapi.service;

public final class GitHubServiceErrorMessages {
    public final static String USER_NOT_FOUND_MESSAGE = "User with given username does not exist";
    public final static String REPOSITORY_RETRIEVAL_ERROR_MESSAGE = "Error while retrieving repositories for given user";
    public final static String GIT_HUB_LIMIT_REACHED_MESSAGE = "GitHub API rate limit exceeded. Please try again later.";

    private GitHubServiceErrorMessages() {}
}
