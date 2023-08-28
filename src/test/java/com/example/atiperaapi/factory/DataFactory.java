package com.example.atiperaapi.factory;

import com.example.atiperaapi.out.BranchOut;
import com.example.atiperaapi.out.GitHubRepoOut;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataFactory {

    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static BranchOut randomBranchOut() {
        String name = getRandomString(10);
        String sha = getRandomString(10);
        return new BranchOut(name, sha);
    }

    public static GitHubRepoOut randomGitHubRepoOut() {
        String name = getRandomString(10);
        String owner = getRandomString(10);
        List<BranchOut> branches = IntStream.range(0, RANDOM.nextInt(5) + 1)
                .mapToObj(i -> randomBranchOut())
                .collect(Collectors.toList());
        return new GitHubRepoOut(name, owner, branches);
    }

    public static String getRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return builder.toString();
    }
}
