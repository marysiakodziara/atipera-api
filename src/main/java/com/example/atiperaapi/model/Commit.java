package com.example.atiperaapi.model;

import com.example.atiperaapi.serialization.CommitSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = CommitSerializer.class)
public record Commit(String sha) {}
