package com.github.merger.service;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MergerServiceTest {

    @InjectMocks
    private MergerService mergerService;

    @Mock
    private PullRequestService prService;

    @Mock
    private CommitService commitService;

    @Mock
    private GitHubClient gitHubClient;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        Commit masterHeadInPr = new Commit().setSha("master-pr-branch-out");
        RepositoryCommit prCommit = new RepositoryCommit().setParents(Arrays.asList(masterHeadInPr));
        when(prService.getCommits(any(), anyInt())).thenReturn(Arrays.asList(prCommit));
        RepositoryCommit masterLatest = new RepositoryCommit().setSha("master-latest").setParents(Arrays.asList());
        RepositoryCommit repositoryMasterHeadInPr = new RepositoryCommit().setSha("master-pr-branch-out").setCommit(masterHeadInPr).setParents(Arrays.asList());
        when(commitService.getCommits(any())).thenReturn(Arrays.asList(masterLatest, repositoryMasterHeadInPr));
    }

    @Test
    public void shouldInvokePRServiceWithRepositoryIdAndPrId() throws Exception {

        mergerService.numberOfCommitsBehind("thirunar", "dummy-repo", 1);

        ArgumentCaptor<RepositoryId> captor = ArgumentCaptor.forClass(RepositoryId.class);
        verify(prService).getCommits(captor.capture(), eq(1));
        assertRepository(captor);
    }

    @Test
    public void shouldInvokeCommitServiceToFetchTheCommits() throws Exception {
        mergerService.numberOfCommitsBehind("thirunar", "dummy-repo", 1);

        ArgumentCaptor<RepositoryId> captor = ArgumentCaptor.forClass(RepositoryId.class);
        verify(commitService).getCommits(captor.capture());
        assertRepository(captor);
    }

    @Test
    public void shouldReturnTheNumberOfCommitsBehind() throws Exception {

        Integer commitsBehind = mergerService.numberOfCommitsBehind("thirunar", "dummy-repo", 1);

        assertThat(commitsBehind).isEqualTo(1);
    }

    @Test
    public void shouldInvokePRServiceToFetchTheBranchName() throws Exception {
        RepositoryId id = RepositoryId.create("thirunar", "dummy-repo");
        when(prService.getPullRequest(id, 1)).thenReturn(new PullRequest().setBase(new PullRequestMarker().setLabel("pr-1")));

        mergerService.mergeMasterToPR(RepositoryId.create("thirunar", "dummy-repo"), 1);

        ArgumentCaptor<RepositoryId> captor = ArgumentCaptor.forClass(RepositoryId.class);
        verify(prService).getPullRequest(captor.capture(), eq(1));
        assertRepository(captor);
    }

    @Test
    public void shouldInvokeGitHubClientWithMapWithBranchNameDetails() throws Exception {
        RepositoryId id = RepositoryId.create("thirunar", "dummy-repo");
        when(prService.getPullRequest(id, 1)).thenReturn(new PullRequest().setBase(new PullRequestMarker().setLabel("pr-1")));

        mergerService.mergeMasterToPR(id, 1);

        ArgumentCaptor<Map> mapCapture = ArgumentCaptor.forClass(Map.class);
        verify(gitHubClient).post(eq("/repos/thirunar/dummy-repo/merges"), mapCapture.capture(), eq(Map.class));
        Map actualMap = mapCapture.getValue();
        HashMap<String, String> expectedMap = new HashMap<String, String>() {{
            put("base", "master");
            put("head", "pr-1");
            put("commit_message", "Merging master to pr-1");
        }};
        assertThat(actualMap).isEqualTo(expectedMap);
    }

    private void assertRepository(ArgumentCaptor<RepositoryId> captor) {
        RepositoryId actualId = captor.getValue();
        RepositoryId expectedId = RepositoryId.create("thirunar", "dummy-repo");
        assertThat(actualId).isEqualTo(expectedId);
    }
}