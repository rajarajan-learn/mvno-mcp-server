package dev.thinktank.mvno.mcp.model;

import java.math.BigDecimal;

public record Plan(
        String id,
        String name,
        int dataAllowanceGb,
        BigDecimal monthlyCost,
        String currency,
        String description
) {
}
