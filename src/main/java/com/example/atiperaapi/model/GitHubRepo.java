package com.example.atiperaapi.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GitHubRepo {
    private String name;
    private String owner;
    private List<Branch> branches;
}
