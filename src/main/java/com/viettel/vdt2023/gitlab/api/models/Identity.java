package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

public class Identity {

    private String provider;
    private String externUid;
    private Integer samlProviderId;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getExternUid() {
        return externUid;
    }

    public void setExternUid(String externUid) {
        this.externUid = externUid;
    }

    public Integer getSamlProviderId() {
	return samlProviderId;
    }

    public void setSamlProviderId(Integer samlProviderId) {
	this.samlProviderId = samlProviderId;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
