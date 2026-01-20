package dev.thinktank.mvno.mcp.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.thinktank.mvno.mcp.model.Plan;
import org.springaicommunity.mcp.annotation.McpTool;

@Service
public class PlanService {

    private final Map<String, Plan> plans = new LinkedHashMap<>();

    public PlanService() {
        seedPlans();
    }

    @McpTool
    public List<Plan> listPlans() {
        return List.copyOf(plans.values());
    }

    public Optional<Plan> findById(String planId) {
        return Optional.ofNullable(plans.get(planId));
    }

    private void seedPlans() {
        plans.put("starter-5g", new Plan(
                "starter-5g",
                "Starter 5G",
                10,
                new BigDecimal("25.00"),
                "USD",
                "10GB high-speed data with unlimited talk and text."
        ));
        plans.put("family-40g", new Plan(
                "family-40g",
                "Family 40G",
                40,
                new BigDecimal("60.00"),
                "USD",
                "Shared 40GB with hotspot support for up to 4 lines."
        ));
        plans.put("unlimited-plus", new Plan(
                "unlimited-plus",
                "Unlimited Plus",
                Integer.MAX_VALUE,
                new BigDecimal("85.00"),
                "USD",
                "Unlimited data with 50GB premium speeds and international roaming add-on."
        ));
    }
}
