package com.viettel.vdt2023.gitlab.api.models;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

import java.util.List;

public class ApprovalState {

    private Boolean approvalRulesOverwritten;
    private List<ApprovalRule> rules;

    public Boolean getApprovalRulesOverwritten() {
	return approvalRulesOverwritten;
    }

    public void setApprovalRulesOverwritten(Boolean approvalRulesOverwritten) {
	this.approvalRulesOverwritten = approvalRulesOverwritten;
    }

    public List<ApprovalRule> getRules() {
	return rules;
    }

    public void setRules(List<ApprovalRule> rules) {
	this.rules = rules;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
