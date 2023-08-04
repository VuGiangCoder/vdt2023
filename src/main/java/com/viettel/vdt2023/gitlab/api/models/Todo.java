package com.viettel.vdt2023.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.viettel.vdt2023.gitlab.api.Constants;
import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

import java.io.IOException;
import java.util.Date;

public class Todo {

    private Long id;
    private Project project;
    private Author author;
    private Constants.TodoAction actionName;
    private Constants.TodoType targetType;

    @JsonDeserialize(using = TargetDeserializer.class)
    private Object target;

    private String targetUrl;
    private String body;
    private Constants.TodoState state;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Constants.TodoAction getActionName() {
        return actionName;
    }

    public void setActionName(Constants.TodoAction actionName) {
        this.actionName = actionName;
    }

    public Constants.TodoType getTargetType() {
        return targetType;
    }

    public void setTargetType(Constants.TodoType targetType) {
        this.targetType = targetType;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Constants.TodoState getState() {
        return state;
    }

    public void setState(Constants.TodoState state) {
        this.state = state;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonIgnore
    public Issue getIssueTarget() {
        return (targetType == Constants.TodoType.ISSUE ? (Issue) target : null);
    }

    @JsonIgnore
    public MergeRequest getMergeRequestTarget() {
        return (targetType == Constants.TodoType.MERGE_REQUEST ? (MergeRequest) target : null);
    }

    @JsonIgnore
    public boolean isIssueTodo() {
        return (targetType == Constants.TodoType.ISSUE);
    }

    @JsonIgnore
    public boolean isMergeRequestTodo() {
        return (targetType == Constants.TodoType.MERGE_REQUEST);
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }

    // This deserializer will determine the target type and deserialize to the correct class (either MergeRequest or Issue).
    private static class TargetDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser jp,  DeserializationContext context)
                throws IOException, JsonProcessingException {

            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            ObjectNode root = (ObjectNode) mapper.readTree(jp);
            boolean isMergeRequestTarget = root.has("source_branch");
            if (isMergeRequestTarget) {
                return mapper.treeToValue(root, MergeRequest.class);
            } else {
                return mapper.treeToValue(root, Issue.class);
            }
        }
    }
}
