package com.example.atiperaapi.model;

import com.example.atiperaapi.deserializer.UserDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubRepo {
    @SerializedName("name")
    private String name;
    @SerializedName("owner")
    @JsonAdapter(UserDeserializer.class)
    private String owner;
    @JsonIgnore
    @SerializedName("fork")
    private boolean fork;
    private List<Branch> branches;
}
