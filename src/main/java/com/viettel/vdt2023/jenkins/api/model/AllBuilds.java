/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.viettel.vdt2023.jenkins.api.model;

import java.util.List;

class AllBuilds extends BaseModel {
    private List<Build> allBuilds;

    public AllBuilds() {
    }

    public List<Build> getAllBuilds() {
        return this.allBuilds;
    }
}