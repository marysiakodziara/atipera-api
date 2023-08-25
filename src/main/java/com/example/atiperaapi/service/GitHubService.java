package com.example.atiperaapi.service;

import com.example.atiperaapi.exception.NotSupportedHeaderException;
import com.example.atiperaapi.exception.UserNotFoundException;
import com.example.atiperaapi.model.Branch;
import com.example.atiperaapi.model.GitHubRepo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GitHubService {

    public List<GitHubRepo> getUsersRepositories(String username) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/users/" + username + "/repos")
                .get()
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() == 200) {
            Gson gson = new Gson();
            String responseBody = response.body().string();
            Type listType = new TypeToken<List<GitHubRepo>>(){}.getType();
            List<GitHubRepo> gitHubRepos = gson.fromJson(responseBody, listType);
            gitHubRepos.forEach(repo -> {
                try {
                    repo.setBranches(getBranches(username, repo.getName()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return gitHubRepos;
        } else {
            throw new UserNotFoundException("User with given username does not exist");
        }
    }

    public List<Branch> getBranches(String username, String repositoryName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/" + username + "/" + repositoryName + "/branches")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        String responseBody = response.body().string();
        Type listType = new TypeToken<List<Branch>>(){}.getType();
        return gson.fromJson(responseBody, listType);
    }
}
