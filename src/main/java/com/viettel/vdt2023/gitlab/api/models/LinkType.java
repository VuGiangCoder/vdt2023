package com.viettel.vdt2023.gitlab.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.viettel.vdt2023.gitlab.api.utils.JacksonJsonEnumHelper;

/**
 * Enum to model the type of link between issues or epics
 */
public enum LinkType {
    RELATES_TO,
    BLOCKS,
    IS_BLOCKED_BY;

    private static JacksonJsonEnumHelper<LinkType> enumHelper = new JacksonJsonEnumHelper<>(LinkType.class);

    @JsonCreator
    public static LinkType forValue(String value) {
        return enumHelper.forValue(value);
    }

    @JsonValue
    public String toValue() {
        return (enumHelper.toString(this));
    }

    @Override
    public String toString() {
        return (enumHelper.toString(this));
    }
}
