# MVNO MCP Server

Spring Boot MCP-style API for a mobile virtual network operator. The service exposes annotated REST endpoints for:
- Getting order status
- Getting subscriber data-usage history
- Changing a subscriber's plan
- Listing available plans

Supported tools:
- `getOrderStatus` — params: `orderId`
- `getUsageHistory` — params: `subscriberId`, optional `from`, `to` (yyyy-MM-dd)
- `changePlan` — params: `subscriberId`, `planId`
- `listPlans` — params: none
- `getCurrentPlan` — params: `subscriberId`
