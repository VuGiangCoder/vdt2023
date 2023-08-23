package com.viettel.vdt2023.jenkins.api.model;

import java.util.List;
class AllMavenBuilds extends BaseModel {
    private List<MavenBuild> allBuilds;

    public AllMavenBuilds() {
    }

    public List<MavenBuild> getAllBuilds() {
        return this.allBuilds;
    }
}