package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

public class IssueLink {

    private Issue sourceIssue;
    private Issue targetIssue;
    private LinkType linkType;

    public Issue getSourceIssue() {
        return sourceIssue;
    }

    public void setSourceIssue(Issue sourceIssue) {
        this.sourceIssue = sourceIssue;
    }

    public Issue getTargetIssue() {
        return targetIssue;
    }

    public void setTargetIssue(Issue targetIssue) {
        this.targetIssue = targetIssue;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
