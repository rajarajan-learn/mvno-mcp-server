package dev.thinktank.mvno.mcp.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.thinktank.mvno.mcp.model.ChangePlanResponse;
import dev.thinktank.mvno.mcp.model.Plan;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springaicommunity.mcp.annotation.McpTool;

@Service
public class SubscriptionService {

    private final PlanService planService;
    private final Map<String, String> subscriberPlans = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

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

    @McpPrompt(
            name = "change-plan",
            title = "Change Plan Assistant",
            description = """
                    Guides the agent through a safe plan-change flow: collect subscriberId,
                    check the current plan, avoid redundant changes, and confirm before calling tools.
                    """)
    public McpSchema.GetPromptResult changePlanPrompt() {
        List<McpSchema.PromptMessage> messages = List.of(
                new McpSchema.PromptMessage(
                        McpSchema.Role.ASSISTANT,
                        new McpSchema.TextContent("""
                                You are an MVNO care copilot that helps subscribers change plans safely.

                                Workflow:
                                1) Always collect a subscriberId before taking any action. If missing, ask for it.
                                2) Normalize all user-provided identifiers (subscriberId, planId, plan name) to lowercase before calling tools.
                                3) Fetch the current plan using getCurrentPlanOrThrow(subscriberId).
                                4) If the requested plan matches the current plan, state that it is already active and stop.
                                5) If the user has not picked a plan, call listPlans to show concise options (name, data, price).
                                6) If the user picked plan is not explicitly available, list plans, pick the closest match, and confirm with the user.
                                7) Summarize the differences between current and requested plans and ask for confirmation.
                                8) After confirmation, call changePlan(subscriberId, planId) and report the returned status and effective time.
                                """)));
        logger.info("This is called");
        return new McpSchema.GetPromptResult("change-plan", messages);
    }

    private void seedSubscribers() {
        subscriberPlans.put("sub-2001", "starter-5g");
        subscriberPlans.put("sub-2002", "family-40g");
    }
}
