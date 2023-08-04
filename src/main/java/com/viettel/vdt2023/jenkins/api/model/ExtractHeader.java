package com.viettel.vdt2023.jenkins.api.model;

public class ExtractHeader extends BaseModel {

    private String location;

    public ExtractHeader setLocation(String value) {
        location = value;
        return this;
    }

    public String getLocation() {
        return location;
    }

}
