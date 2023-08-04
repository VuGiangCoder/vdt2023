package com.viettel.vdt2023.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.vdt2023.gitlab.api.Constants;
import com.viettel.vdt2023.gitlab.api.GitLabApiForm;
import com.viettel.vdt2023.gitlab.api.utils.ISO8601;

import java.util.Date;
import java.util.List;

/**
 *  This class is used to filter issues when getting lists of them.
 */
public class IssueFilter {

    /**
     * Return only the milestone having the given iid.
     */
    private List<String> iids;


    private Constants.IssueState state;

    /**
     * Comma-separated list of label names, issues must have all labels to be returned. No+Label lists all issues with no labels.
     */
    private List<String> labels;

    /**
     * The milestone title. No+Milestone lists all issues with no milestone.
     */
    private String milestone;


    private Constants.IssueScope scope;

    /**
     * Return issues created by the given user id.
     */
    private Long authorId;

    /**
     * Return issues assigned to the given user id.
     */
    private Long assigneeId;

    /**
     * Return issues reacted by the authenticated user by the given emoji.
     */
    private String myReactionEmoji;


    private Constants.IssueOrderBy orderBy;


    private Constants.SortOrder sort;

    /**
     * Search project issues against their title and description.
     */
    private String search;

    /**
     * Return issues created on or after the given time.
     */
    private Date createdAfter;

    /**
     * Return issues created on or before the given time.
     */
    private Date createdBefore;

    /**
     * Return issues updated on or after the given time.
     */
    private Date updatedAfter;

    /**
     * Return issues updated on or before the given time.
     */
    private Date updatedBefore;

    /**
     * Return issues in current iteration.
     */
    private String iterationTitle;


    /*- properties -*/
    public List<String> getIids() {
        return iids;
    }

    public void setIids(List<String> iids) {
        this.iids = iids;
    }

    public Constants.IssueState getState() {
        return state;
    }

    public void setState(Constants.IssueState state) {
        this.state = state;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public Constants.IssueScope getScope() {
        return scope;
    }

    public void setScope(Constants.IssueScope scope) {
        this.scope = scope;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getMyReactionEmoji() {
        return myReactionEmoji;
    }

    public void setMyReactionEmoji(String myReactionEmoji) {
        this.myReactionEmoji = myReactionEmoji;
    }

    public Constants.IssueOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Constants.IssueOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Constants.SortOrder getSort() {
        return sort;
    }

    public void setSort(Constants.SortOrder sort) {
        this.sort = sort;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
    }

    public Date getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
    }

    public Date getUpdatedAfter() {
        return updatedAfter;
    }

    public void setUpdatedAfter(Date updatedAfter) {
        this.updatedAfter = updatedAfter;
    }

    public Date getUpdatedBefore() {
        return updatedBefore;
    }

    public void setUpdatedBefore(Date updatedBefore) {
        this.updatedBefore = updatedBefore;
    }

    public String getIterationTitle() {
        return iterationTitle;
    }

    public void setIterationTitle(String iterationTitle) {
        this.iterationTitle = iterationTitle;
    }

    /*- builder -*/
    public IssueFilter withIids(List<String> iids) {
        this.iids = iids;
        return (this);
    }

    public IssueFilter withState(Constants.IssueState state) {
        this.state = state;
        return (this);
    }

    public IssueFilter withLabels(List<String> labels) {
        this.labels = labels;
        return (this);
    }

    public IssueFilter withMilestone(String milestone) {
        this.milestone = milestone;
        return (this);
    }

    public IssueFilter withScope(Constants.IssueScope scope) {
        this.scope = scope;
        return (this);
    }

    public IssueFilter withAuthorId(Long authorId) {
        this.authorId = authorId;
        return (this);
    }

    public IssueFilter withAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
        return (this);
    }

    public IssueFilter withMyReactionEmoji(String myReactionEmoji) {
        this.myReactionEmoji = myReactionEmoji;
        return (this);
    }

    public IssueFilter withOrderBy(Constants.IssueOrderBy orderBy) {
        this.orderBy = orderBy;
        return (this);
    }

    public IssueFilter withSort(Constants.SortOrder sort) {
        this.sort = sort;
        return (this);
    }

    public IssueFilter withSearch(String search) {
        this.search = search;
        return (this);
    }

    public IssueFilter withCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
        return (this);
    }

    public IssueFilter withCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
        return (this);
    }

    public IssueFilter withUpdatedAfter(Date updatedAfter) {
        this.updatedAfter = updatedAfter;
        return (this);
    }

    public IssueFilter withUpdatedBefore(Date updatedBefore) {
        this.updatedBefore = updatedBefore;
        return (this);
    }

    public IssueFilter withIterationTitle(String iterationTitle) {
        this.iterationTitle = iterationTitle;
        return (this);
    }

    /*- params generator -*/
    @JsonIgnore
    public GitLabApiForm getQueryParams(int page, int perPage) {
        return (getQueryParams()
                .withParam(Constants.PAGE_PARAM, page)
                .withParam(Constants.PER_PAGE_PARAM, perPage));
    }

    @JsonIgnore
    public GitLabApiForm getQueryParams() {
        return (new GitLabApiForm()
                .withParam("iids", iids)
                .withParam("state", state)
                .withParam("labels", (labels != null ? String.join(",", labels) : null))
                .withParam("milestone", milestone)
                .withParam("scope", scope)
                .withParam("author_id", authorId)
                .withParam("assignee_id", assigneeId)
                .withParam("my_reaction_emoji", myReactionEmoji)
                .withParam("order_by", orderBy)
                .withParam("sort", sort)
                .withParam("search", search)
                .withParam("created_after", ISO8601.toString(createdAfter, false))
                .withParam("created_before", ISO8601.toString(createdBefore, false))
                .withParam("updated_after", ISO8601.toString(updatedAfter, false))
                .withParam("updated_before", ISO8601.toString(updatedBefore, false)))
                .withParam("iteration_title", iterationTitle);
    }
}
