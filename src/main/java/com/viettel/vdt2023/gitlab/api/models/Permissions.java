package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

public class Permissions {

    private ProjectAccess projectAccess;
    private ProjectAccess groupAccess;

    public ProjectAccess getProjectAccess() {
        return projectAccess;
    }

    public void setProjectAccess(ProjectAccess projectAccess) {
        this.projectAccess = projectAccess;
    }

    public ProjectAccess getGroupAccess() {
        return groupAccess;
    }

    public void setGroupAccess(ProjectAccess groupAccess) {
        this.groupAccess = groupAccess;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
