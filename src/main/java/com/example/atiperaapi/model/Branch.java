package com.example.atiperaapi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Branch {
    private String name;
    private String lastCommitSha;
}
