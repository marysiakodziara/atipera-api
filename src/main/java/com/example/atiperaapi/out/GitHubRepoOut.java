package com.example.atiperaapi.out;

import com.example.atiperaapi.model.Branch;
import com.example.atiperaapi.model.Owner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitHubRepoOut {
    public String name;
    public String owner;
    public List<Branch> branches;
}
