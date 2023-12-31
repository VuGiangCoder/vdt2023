package com.viettel.vdt2023.gitlab.api.webhook;


import com.viettel.vdt2023.gitlab.api.models.Assignee;
import com.viettel.vdt2023.gitlab.api.models.Reviewer;
import com.viettel.vdt2023.gitlab.api.models.User;
import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

import java.util.List;

public class MergeRequestEvent extends AbstractEvent {

    public static final String X_GITLAB_EVENT = "Merge Request Hook";
    public static final String OBJECT_KIND = "merge_request";

    private User user;
    private EventProject project;
    private EventRepository repository;
    private ObjectAttributes objectAttributes;
    private List<EventLabel> labels;
    private MergeRequestChanges changes;
    private List<Assignee> assignees;
    private List<Reviewer> reviewers;

    public String getObjectKind() {
        return (OBJECT_KIND);
    }

    public void setObjectKind(String objectKind) {
        if (!OBJECT_KIND.equals(objectKind))
            throw new RuntimeException("Invalid object_kind (" + objectKind + "), must be '" + OBJECT_KIND + "'");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventProject getProject() {
        return project;
    }

    public void setProject(EventProject project) {
        this.project = project;
    }

    public EventRepository getRepository() {
        return repository;
    }

    public void setRepository(EventRepository repository) {
        this.repository = repository;
    }

    public ObjectAttributes getObjectAttributes() {
        return this.objectAttributes;
    }

    public void setObjectAttributes(ObjectAttributes objectAttributes) {
        this.objectAttributes = objectAttributes;
    }

    public List<EventLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<EventLabel> labels) {
        this.labels = labels;
    }

    public MergeRequestChanges getChanges() {
        return changes;
    }

    public void setChanges(MergeRequestChanges changes) {
        this.changes = changes;
    }

    public List<Assignee> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Assignee> assignees) {
        this.assignees = assignees;
    }

    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Reviewer> reviewers) {
        this.reviewers = reviewers;
    }

    public static class ObjectAttributes extends EventMergeRequest {
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
