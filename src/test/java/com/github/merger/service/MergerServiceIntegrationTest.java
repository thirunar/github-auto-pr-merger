package com.github.merger.service;

import com.github.merger.MergerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MergerApplication.class)
public class MergerServiceIntegrationTest {

    @Autowired
    private MergerService mergerService;

    @Test
    public void shouldTestTheCommits() throws Exception {
        Integer numberOfCommitBehind = mergerService.numberOfCommitsBehind("thirunar", "dummy-repo", 1);

        assertThat(numberOfCommitBehind).isEqualTo(2);
    }
}