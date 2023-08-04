package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

public class EpicInLink extends AbstractEpic<EpicInLink> {

    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
