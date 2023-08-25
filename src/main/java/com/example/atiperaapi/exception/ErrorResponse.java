package com.example.atiperaapi.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    @JsonProperty
    private int status;
    @JsonProperty
    private String message;
}
