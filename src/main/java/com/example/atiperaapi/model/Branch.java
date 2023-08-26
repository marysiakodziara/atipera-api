package com.example.atiperaapi.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Branch {
    public final String name;
    public final String lastCommitSha;
}
