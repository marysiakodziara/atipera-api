package com.example.atiperaapi.model;

import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class GitHubRepo {
    public String name;
    public String owner;
    public List<Branch> branches;
}
