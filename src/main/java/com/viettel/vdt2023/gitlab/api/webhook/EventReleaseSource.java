package com.viettel.vdt2023.gitlab.api.webhook;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

public class EventReleaseSource {
    private String format;
    private String url;

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
