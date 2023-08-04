package com.viettel.vdt2023.jenkins.api.model;

import java.util.List;

public class QueueItemActions extends BaseModel {
    private List<CauseAction> causes;

    public List<CauseAction> getCauses() {
        return causes;
    }

    public QueueItemActions setCauses(List<CauseAction> causes) {
        this.causes = causes;
        return this;
    }

}
