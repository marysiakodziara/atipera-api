package com.example.atiperaapi.model;

import com.example.atiperaapi.serialization.CommitSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonSerialize(using = CommitSerializer.class)
public class Commit {
    public String sha;
}
