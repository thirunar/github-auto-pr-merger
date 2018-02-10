package com.github.merger.service;

import com.github.merger.MergerApplication;
import org.eclipse.egit.github.core.RepositoryId;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MergerApplication.class)
@Ignore
public class MergerServiceIntegrationTest {

    @Autowired
    private MergerService mergerService;

    @Test
    public void shouldTestTheCommits() throws Exception {
        Integer numberOfCommitBehind = mergerService.numberOfCommitsBehind("thirunar", "dummy-repo", 1);

        assertThat(numberOfCommitBehind).isEqualTo(2);
    }

    @Test
    public void shouldMergeTheBranch() throws Exception {
        Map map = mergerService.mergeMasterToPR(RepositoryId.create("thirunar", "dummy-repo"), 1);

        assertThat(map).isNotNull();
    }
}