package com.viettel.vdt2023.gitlab.api.systemhooks;

import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

public class RepositoryChange {

    private String after;
    private String before;
    private String ref;

    public String getAfter() {
        return this.after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return this.before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getRef() {
        return this.ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
