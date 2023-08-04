package com.viettel.vdt2023.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;


public class References {

    @JsonProperty("short")
    private String _short;
    private String relative;
    private String full;

    public String getShort() {
        return _short;
    }

    public void setShort(String _short) {
        this._short = _short;
    }

    public String getRelative() {
        return relative;
    }

    public void setRelative(String relative) {
        this.relative = relative;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
