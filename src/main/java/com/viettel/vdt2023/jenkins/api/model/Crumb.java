package com.viettel.vdt2023.jenkins.api.model;

public class Crumb extends BaseModel {

    private String crumbRequestField;
    private String crumb;

    public Crumb() {
    }

    public Crumb(String crumbRequestField, String crumb) {
        this.crumbRequestField = crumbRequestField;
        this.crumb = crumb;
    }

    public String getCrumbRequestField() {
        return crumbRequestField;
    }

    public String getCrumb() {
        return crumb;
    }
}
