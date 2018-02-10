package com.github.merger.service;

import com.github.merger.util.GithubUrlUtils;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.eclipse.egit.github.core.RepositoryId.create;

@Service
public class MergerService {

    private final PullRequestService prService;

    private final CommitService commitService;

    private final GitHubClient gitHubClient;

    @Autowired
    public MergerService(PullRequestService prService, CommitService commitService, GitHubClient gitHubClient) {
        this.prService = prService;
        this.commitService = commitService;
        this.gitHubClient = gitHubClient;
    }

    public Integer numberOfCommitsBehind(String userName, String repositoryName, Integer prId) throws IOException {
        RepositoryId id = create(userName, repositoryName);
        List<RepositoryCommit> commits = prService.getCommits(id, prId);
        String masterSha = commits.get(0).getParents().get(0).getSha();
        List<RepositoryCommit> masterCommits = commitService.getCommits(id);
        AtomicInteger position = new AtomicInteger(-1);
        masterCommits.stream()
                .peek(x -> position.incrementAndGet())
                .filter(c -> c.getSha().equals(masterSha))
                .findAny();
        return position.get();
    }

    public Map mergeMasterToPR(RepositoryId id, Integer prId) throws IOException {
        String uri = GithubUrlUtils.getMergeUrl(id.getOwner(), id.getName());
        PullRequest pullRequest = prService.getPullRequest(id, prId);
        String branchName = pullRequest.getBase().getLabel();
        HashMap<String, String> map = new HashMap<>();
        map.put("base", "master");
        map.put("head", branchName);
        map.put("commit_message", "Merging master to " + branchName);
        return gitHubClient.post(uri, map, Map.class);
    }

}
