
package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

public class EpicIssueLink {

    private Long id;
    private Integer relativePosition;
    private Epic epic;
    private Issue issue;

    public Long getId() {
        return id;
    }

    public void setId(Long epicIssueId) {
        this.id = epicIssueId;
    }

    public Integer getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Integer relativePosition) {
        this.relativePosition = relativePosition;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
