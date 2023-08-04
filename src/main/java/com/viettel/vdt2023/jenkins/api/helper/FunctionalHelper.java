package com.viettel.vdt2023.jenkins.api.helper;
import com.viettel.vdt2023.jenkins.api.client.JenkinsHttpConnection;
import com.viettel.vdt2023.jenkins.api.model.BaseModel;

import java.util.function.Function;

public final class FunctionalHelper {

    private FunctionalHelper() {
        // intentionally empty.
    }

    public static final <T extends BaseModel> Function<T, T> SET_CLIENT(JenkinsHttpConnection client) {
        return s -> {
            s.setClient(client);
            return s;
        };
    }


}
