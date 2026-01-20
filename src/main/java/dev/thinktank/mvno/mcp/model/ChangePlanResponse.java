package dev.thinktank.mvno.mcp.model;

import java.time.Instant;

public record ChangePlanResponse(
        String subscriberId,
        Plan newPlan,
        Instant effectiveAt,
        String status
) {
}
