package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.Constants;
import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

import java.util.Date;

public class Deployment {

    private Long id;
    private Long iid;
    private String ref;
    private String sha;
    private Date createdAt;
    private Date updatedAt;
    private Constants.DeploymentStatus status;
    private User user;
    private Environment environment;
    private Deployable deployable;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getIid() {
	return iid;
    }

    public void setIid(Long iid) {
	this.iid = iid;
    }

    public String getRef() {
	return ref;
    }

    public void setRef(String ref) {
	this.ref = ref;
    }

    public String getSha() {
	return sha;
    }

    public void setSha(String sha) {
	this.sha = sha;
    }

    public Date getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
	this.updatedAt = updatedAt;
    }

    public Constants.DeploymentStatus getStatus() {
	return status;
    }

    public void setStatus(Constants.DeploymentStatus status) {
	this.status = status;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Environment getEnvironment() {
	return environment;
    }

    public void setEnvironment(Environment environment) {
	this.environment = environment;
    }

    public Deployable getDeployable() {
	return deployable;
    }

    public void setDeployable(Deployable deployable) {
	this.deployable = deployable;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
