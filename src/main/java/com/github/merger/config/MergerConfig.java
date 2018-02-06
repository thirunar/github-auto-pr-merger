package com.github.merger.config;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MergerConfig {

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClient("api.github.com");
    }

    @Bean
    public PullRequestService pullRequestService() {
        return new PullRequestService(gitHubClient());
    }

    @Bean
    public RepositoryService repositoryService() {
        return new RepositoryService(gitHubClient());
    }

    @Bean
    public CommitService commitService() {
        return new CommitService(gitHubClient());
    }

}
