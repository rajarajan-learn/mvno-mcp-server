package dev.thinktank.mvno.mcp.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.thinktank.mvno.mcp.model.ChangePlanResponse;
import dev.thinktank.mvno.mcp.model.Plan;
import org.springaicommunity.mcp.annotation.McpTool;

@Service
public class SubscriptionService {

    private final PlanService planService;
    private final Map<String, String> subscriberPlans = new ConcurrentHashMap<>();

    public SubscriptionService(PlanService planService) {
        this.planService = planService;
        seedSubscribers();
    }

    @McpTool
    public ChangePlanResponse changePlan(String subscriberId, String planId) {
        Plan plan = planService.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));

        subscriberPlans.put(subscriberId, planId);
        return new ChangePlanResponse(subscriberId, plan, Instant.now(), "PENDING_ACTIVATION");
    }


    private Optional<Plan> getCurrentPlan(String subscriberId) {
        String planId = subscriberPlans.get(subscriberId);
        if (planId == null) {
            return Optional.empty();
        }
        return planService.findById(planId);
    }

    @McpTool
    public Plan getCurrentPlanOrThrow(String subscriberId) {
        return getCurrentPlan(subscriberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber not found"));
    }

    private void seedSubscribers() {
        subscriberPlans.put("sub-2001", "starter-5g");
        subscriberPlans.put("sub-2002", "family-40g");
    }
}
