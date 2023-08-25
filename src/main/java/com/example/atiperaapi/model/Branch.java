package com.example.atiperaapi.model;

import com.example.atiperaapi.deserializer.CommitDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Branch {
    @SerializedName("name")
    private String name;
    @SerializedName("commit")
    @JsonAdapter(CommitDeserializer.class)
    private String lastCommitSha;
}
