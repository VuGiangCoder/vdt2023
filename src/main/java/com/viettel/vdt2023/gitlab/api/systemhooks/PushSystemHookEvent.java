package com.viettel.vdt2023.gitlab.api.systemhooks;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;
import com.viettel.vdt2023.gitlab.api.webhook.AbstractPushEvent;

public class PushSystemHookEvent extends AbstractPushEvent implements SystemHookEvent {

    public static final String PUSH_EVENT = "push";

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
