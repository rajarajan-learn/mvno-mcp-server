package dev.thinktank.mvno.mcp.model;

import jakarta.validation.constraints.NotBlank;

public class ChangePlanRequest {

    @NotBlank(message = "planId is required")
    private String planId;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
