package com.github.merger.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubUrlUtilsTest {

    @Test
    public void shouldMergeUrlForTheGivenUsernameAndRepositoryId() throws Exception {

        String url = GithubUrlUtils.getMergeUrl("thirunar", "dummy-repo");

        assertThat(url).isEqualTo("/repos/thirunar/dummy-repo/merges");
    }
}