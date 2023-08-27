package com.example.atiperaapi.model;

import com.example.atiperaapi.serialization.OwnerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonSerialize(using = OwnerSerializer.class)
public class Owner {
    public String login;
}
