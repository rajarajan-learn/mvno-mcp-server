package dev.thinktank.mvno.mcp.model;

import java.time.Instant;

public record OrderStatusResponse(
        String orderId,
        OrderState status,
        String message,
        Instant updatedAt
) {
}
