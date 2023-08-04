package com.viettel.vdt2023.gitlab.api.systemhooks;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;
import com.viettel.vdt2023.gitlab.api.webhook.AbstractPushEvent;

public class TagPushSystemHookEvent extends AbstractPushEvent implements SystemHookEvent {

    public static final String TAG_PUSH_EVENT = "tag_push";

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
