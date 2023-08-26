package com.example.atiperaapi.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

@Builder
@JsonSerialize
public class ErrorResponse {
    public int status;
    public String message;
}
