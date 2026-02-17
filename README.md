# MVNO MCP Server

Spring Boot MCP-style API for a mobile virtual network operator. The service exposes annotated REST endpoints for:
- Getting order status
- Getting subscriber data-usage history
- Changing a subscriber's plan
- Listing available plans

Supported tools:
- `get_order_status` — params: `orderId`
- `getUsageHistory` — params: `subscriberId`, optional `from`, `to` (yyyy-MM-dd)
- `changePlan` — params: `planId`
- `listPlans` — params: none
- `getCurrentPlanOrThrow` — params: none

Supported prompts:
- `change-plan`

## Authentication and Authorization

The server uses:
- Spring Security resource server (`spring-boot-starter-oauth2-resource-server`)
- `org.springaicommunity:mcp-server-security`
- `McpServerOAuth2Configurer` in `src/main/java/dev/thinktank/mvno/mcp/config/SecurityConfig.java`

All requests are authenticated (`anyRequest().authenticated()`).

For subscription operations (`changePlan`, `getCurrentPlanOrThrow`), the current user is derived from the JWT claim `preferred_username` via:
- `src/main/java/dev/thinktank/mvno/mcp/service/UtilService.java`

`getUsageHistory` still accepts explicit `subscriberId` as input.

## CORS

CORS is enabled globally in `SecurityConfig`:
- `allowedOriginPatterns: *`
- `allowedMethods: *`
- `allowedHeaders: *`
- `exposedHeaders: *`
- `allowCredentials: true`

## Keycloak Setup Checklist

1. Configure Keycloak realm issuer:
   - `spring.security.oauth2.resourceserver.jwt.issuer-uri`
2. Ensure issued access tokens include `preferred_username` claim (used by plan tools).
3. Configure MCP security properties under `mcp.security` as needed for your environment.

## Configuration

See `src/main/resources/application.yml`:
- `spring.security.oauth2.resourceserver.jwt.issuer-uri`
- `mcp.security.required-scope`
- `mcp.security.subject-claim`
- `mcp.security.audience`
- `mcp.security.allowed-clients`
- `spring.ai.mcp.server.streamable-http.mcp-endpoint` (default `/mcp`)
- `server.port` (current value `8081`)

OAuth discovery metadata:
- Exposed by MCP security integration for inspector/client discovery.
- Includes protected resource metadata used during OAuth discovery and token flow.
