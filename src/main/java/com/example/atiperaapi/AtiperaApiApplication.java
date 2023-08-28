package com.example.atiperaapi;

import com.example.atiperaapi.config.GitHubApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GitHubApiProperties.class)
public class AtiperaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtiperaApiApplication.class, args);
    }

}
