package com.example.atiperaapi.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GitHubRepo {
    public String name;
    public String owner;
    public List<Branch> branches;
}
