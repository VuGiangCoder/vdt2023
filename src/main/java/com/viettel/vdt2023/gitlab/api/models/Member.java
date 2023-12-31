
package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

import java.util.Date;

public class Member extends AbstractUser<Member> {

    private AccessLevel accessLevel;
    private Date expiresAt;
    private Identity groupSamlIdentity;

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Date getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Identity getGroupSamlIdentity() {
        return groupSamlIdentity;
    }

    public void setGroupSamlIdentity(Identity groupSamlIdentity) {
        this.groupSamlIdentity = groupSamlIdentity;
    }

    public Member withAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return this;
    }

    public Member withExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public Member withGroupSamlIdentity(Identity groupSamlIdentity) {
        this.groupSamlIdentity = groupSamlIdentity;
        return this;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
