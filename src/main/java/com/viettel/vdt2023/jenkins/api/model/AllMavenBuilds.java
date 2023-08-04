package com.viettel.vdt2023.jenkins.api.model;

import java.util.List;

/**
 * This class is only needed to get all builds in
 * {@link MavenJobWithDetails#getAllBuilds()}.
 * 
 * @author Karl Heinz Marbaise
 *
 *         NOTE: This class is not part of any public API
 */
class AllMavenBuilds extends BaseModel {
    private List<MavenBuild> allBuilds;

    public AllMavenBuilds() {
    }

    public List<MavenBuild> getAllBuilds() {
        return this.allBuilds;
    }
}