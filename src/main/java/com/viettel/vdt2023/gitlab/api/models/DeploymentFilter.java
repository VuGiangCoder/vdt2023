package com.viettel.vdt2023.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.vdt2023.gitlab.api.Constants;
import com.viettel.vdt2023.gitlab.api.GitLabApiForm;
import com.viettel.vdt2023.gitlab.api.utils.ISO8601;


import java.util.Date;

public class DeploymentFilter {

	/**
	 * Return deployments ordered by either one of id, iid, created_at, updated_at or ref fields. Default is id.
	 */
	private Constants.DeploymentOrderBy orderBy;

	/**
	 * Return deployments sorted in asc or desc order. Default is asc.
	 */
	private Constants.SortOrder sortOrder;

	/**
     * Return deployments updated after the specified date. Expected in ISO 8601 format (2019-03-15T08:00:00Z).
     */
    private Date updatedAfter;

    /**
     * Return deployments updated before the specified date. Expected in ISO 8601 format (2019-03-15T08:00:00Z).
     */
    private Date updatedBefore;

    /**
     * The name of the environment to filter deployments by.
     */
    private String environment;

    /**
     * The status to filter deployments by.
     */
    private Constants.DeploymentStatus status;

	public Constants.DeploymentOrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Constants.DeploymentOrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public Constants.SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Constants.SortOrder sortOrder) {
		this.sortOrder = sortOrder;
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

	/**
	 * @deprecated use {@link #getUpdatedAfter()}
	 */
	@Deprecated
	public Date getFinishedAfter() {
		return updatedAfter;
	}

	/**
	 * @deprecated use {@link #setUpdatedAfter(Date)}
	 */
	@Deprecated
	public void setFinishedAfter(Date finishedAfter) {
		this.updatedAfter = finishedAfter;
	}

	/**
	 * @deprecated use {@link #getUpdatedBefore()}
	 */
	@Deprecated
	public Date getFinishedBefore() {
		return updatedBefore;
	}

	/**
	 * @deprecated use {@link #setUpdatedBefore(Date)}
	 */
	@Deprecated
	public void setFinishedBefore(Date finishedBefore) {
		this.updatedBefore = finishedBefore;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public Constants.DeploymentStatus getStatus() {
		return status;
	}

	public void setStatus(Constants.DeploymentStatus status) {
		this.status = status;
	}

    public DeploymentFilter withOrderBy(Constants.DeploymentOrderBy orderBy) {
        this.orderBy = orderBy;
        return (this);
    }

    public DeploymentFilter withSortOrder(Constants.SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        return (this);
    }

    public DeploymentFilter withUpdatedAfter(Date updatedAfter) {
        this.updatedAfter = updatedAfter;
        return (this);
    }

    public DeploymentFilter withUpdatedBefore(Date updatedBefore) {
        this.updatedBefore = updatedBefore;
        return (this);
    }

	/**
	 * @deprecated use {@link #withUpdatedAfter(Date)}
	 */
	@Deprecated
    public DeploymentFilter withFinishedAfter(Date finishedAfter) {
        this.updatedAfter = finishedAfter;
        return (this);
    }

	/**
	 * @deprecated use {@link #withUpdatedBefore(Date)}
	 */
	@Deprecated
    public DeploymentFilter withFinishedBefore(Date finishedBefore) {
        this.updatedBefore = finishedBefore;
        return (this);
    }

    public DeploymentFilter withEnvironment(String environment) {
        this.environment = environment;
        return (this);
    }

    public DeploymentFilter withStatus(Constants.DeploymentStatus status) {
        this.status = status;
        return (this);
    }

    @JsonIgnore
    public GitLabApiForm getQueryParams(int page, int perPage) {
        return (getQueryParams()
                .withParam(Constants.PAGE_PARAM, page)
                .withParam(Constants.PER_PAGE_PARAM, perPage));
    }

    @JsonIgnore
    public GitLabApiForm getQueryParams() {
        return (new GitLabApiForm()
                .withParam("order_by", orderBy)
                .withParam("sort", sortOrder)
                .withParam("updated_after", ISO8601.toString(updatedAfter, false))
                .withParam("updated_before", ISO8601.toString(updatedBefore, false))
                .withParam("environment", environment)
                .withParam("status", status));
    }

}
