package com.example.atiperaapi.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record ErrorResponseBody(int status, String message) {}
