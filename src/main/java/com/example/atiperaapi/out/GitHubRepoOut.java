package com.example.atiperaapi.out;

import java.util.List;
public record GitHubRepoOut(String name, String owner, List<BranchOut> branches) {}
