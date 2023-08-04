package com.viettel.vdt2023.gitlab.api.webhook;


import com.viettel.vdt2023.gitlab.api.models.Reviewer;

import java.util.List;

public class MergeRequestChanges extends EventChanges {

    private ChangeContainer<String> mergeStatus;
    private ChangeContainer<List<Reviewer>> reviewers;

    public ChangeContainer<String> getMergeStatus() {
        return mergeStatus;
    }

    public void setMergeStatus(ChangeContainer<String> mergeStatus) {
        this.mergeStatus = mergeStatus;
    }

    public ChangeContainer<List<Reviewer>> getReviewers() {
        return reviewers;
    }

    public void setReviewers(ChangeContainer<List<Reviewer>> reviewers) {
        this.reviewers = reviewers;
    }

}
