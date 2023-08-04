package com.viettel.vdt2023.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.vdt2023.gitlab.api.Constants;
import com.viettel.vdt2023.gitlab.api.GitLabApiForm;


import java.util.Date;

/**
 *  This class is used to filter Pipelines when getting lists of them.
 */
public class PipelineFilter {

    private Constants.PipelineScope scope;

    private PipelineStatus status;

    /** The ref of pipelines. */
    private String ref;

    /** The SHA of pipelines. */
    private String sha;

    /** If true, returns pipelines with invalid configurations. */
    private Boolean yamlErrors;

    /** The name of the user who triggered pipelines. */
    private String name;

    /** The username of the user who triggered pipelines */
    private String username;

    /** Return pipelines updated after the specified date. */
    private Date updatedAfter;

    /** Return pipelines updated before the specified date. */
    private Date updatedBefore;


    private Constants.PipelineOrderBy orderBy;


    private Constants.SortOrder sort;


    public void setScope(Constants.PipelineScope scope) {
        this.scope = scope;
    }

    public void setStatus(PipelineStatus status) {
        this.status = status;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public void setYamlErrors(Boolean yamlErrors) {
        this.yamlErrors = yamlErrors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUpdatedAfter(Date updatedAfter) {
        this.updatedAfter = updatedAfter;
    }

    public void setUpdatedBefore(Date updatedBefore) {
        this.updatedBefore = updatedBefore;
    }

    public void setOrderBy(Constants.PipelineOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public void setSort(Constants.SortOrder sort) {
        this.sort = sort;
    }

    public PipelineFilter withScope(Constants.PipelineScope scope) {
        this.scope = scope;
        return this;
    }

    public PipelineFilter withStatus(PipelineStatus status) {
        this.status = status;
        return this;
    }

    public PipelineFilter withRef(String ref) {
        this.ref = ref;
        return this;
    }

    public PipelineFilter withSha(String sha) {
        this.sha = sha;
        return this;
    }

    public PipelineFilter withYamlErrors(Boolean yamlErrors) {
        this.yamlErrors = yamlErrors;
        return this;
    }

    public PipelineFilter withName(String name) {
        this.name = name;
        return this;
    }

    public PipelineFilter withUsername(String username) {
        this.username = username;
        return this;
    }

    public PipelineFilter withUpdatedAfter(Date updatedAfter) {
        this.updatedAfter = updatedAfter;
        return this;
    }

    public PipelineFilter withUpdatedBefore(Date updatedBefore) {
        this.updatedBefore = updatedBefore;
        return this;
    }

    public PipelineFilter withOrderBy(Constants.PipelineOrderBy orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public PipelineFilter withSort(Constants.SortOrder sort) {
        this.sort = sort;
        return this;
    }

    @JsonIgnore
    public GitLabApiForm getQueryParams() {
        return (new GitLabApiForm()
                .withParam("scope", scope)
                .withParam("status", status)
                .withParam("ref", ref)
                .withParam("sha", sha)
                .withParam("yaml_errors", yamlErrors)
                .withParam("name", name)
                .withParam("username", username)
                .withParam("updated_after", updatedAfter)
                .withParam("updated_before", updatedBefore)
                .withParam("order_by", orderBy)
                .withParam("sort", sort));
    }
}
