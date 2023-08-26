package com.example.atiperaapi.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Branch {
    public final String name;
    public final String lastCommitSha;
}
