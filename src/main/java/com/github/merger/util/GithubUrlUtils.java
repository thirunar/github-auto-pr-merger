package com.github.merger.util;

import static java.lang.String.format;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_REPOS;

public class GithubUrlUtils {

    public static String getMergeUrl(String userName, String repositoryName) {
        return format("%s/%s/%s/merges", SEGMENT_REPOS, userName, repositoryName);
    }
}
