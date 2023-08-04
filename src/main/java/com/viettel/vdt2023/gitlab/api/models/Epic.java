package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

import java.util.Date;

public class Epic extends AbstractEpic<Epic> {

    private Boolean startDateIsFixed;
    private Boolean dueDateIsFixed;
    private Date dueDateFromInheritedSource;
    private Boolean subscribed;

    public Boolean getStartDateIsFixed() {
        return startDateIsFixed;
    }

    public void setStartDateIsFixed(Boolean startDateIsFixed) {
        this.startDateIsFixed = startDateIsFixed;
    }

    public Boolean getDueDateIsFixed() {
        return dueDateIsFixed;
    }

    public void setDueDateIsFixed(Boolean dueDateIsFixed) {
        this.dueDateIsFixed = dueDateIsFixed;
    }

    public Date getDueDateFromInheritedSource() {
        return dueDateFromInheritedSource;
    }

    public void setDueDateFromInheritedSource(Date dueDateFromInheritedSource) {
        this.dueDateFromInheritedSource = dueDateFromInheritedSource;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
