package com.github.merger.service;

import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MergerService {

    private final PullRequestService prService;

    private final CommitService commitService;

    @Autowired
    public MergerService(PullRequestService prService, CommitService commitService) {
        this.prService = prService;
        this.commitService = commitService;
    }

    public Integer numberOfCommitsBehind(String userName, String repositoryName, Integer prId) throws IOException {
        RepositoryId id = new RepositoryId(userName, repositoryName);
        List<RepositoryCommit> commits = prService.getCommits(id, prId);
        String masterSha = commits.get(0).getParents().get(0).getSha();
        List<RepositoryCommit> masterCommits = commitService.getCommits(id);
        AtomicInteger position = new AtomicInteger(-1);
        masterCommits.stream()
                .peek(x -> position.incrementAndGet())
                .filter(c -> c.getSha().equals(masterSha))
                .findFirst();
        return position.get();
    }
}
