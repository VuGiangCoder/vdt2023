
package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

import java.util.Date;

public class Issue extends AbstractIssue {

    private Boolean subscribed;

    private Long issueLinkId;
    private LinkType linkType;
    private Date linkCreatedAt;
    private Date linkUpdatedAt;

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Long getIssueLinkId() {
        return issueLinkId;
    }

    public void setIssueLinkId(Long issueLinkId) {
        this.issueLinkId = issueLinkId;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public Date getLinkCreatedAt() {
        return linkCreatedAt;
    }

    public void setLinkCreatedAt(Date linkCreatedAt) {
        this.linkCreatedAt = linkCreatedAt;
    }

    public Date getLinkUpdatedAt() {
        return linkUpdatedAt;
    }

    public void setLinkUpdatedAt(Date linkUpdatedAt) {
        this.linkUpdatedAt = linkUpdatedAt;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
