package dev.thinktank.mvno.mcp.service;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.thinktank.mvno.mcp.model.OrderState;
import dev.thinktank.mvno.mcp.model.OrderStatusResponse;
import org.springaicommunity.mcp.annotation.McpTool;

@Service
public class OrderService {

    private final Map<String, OrderStatusResponse> orderStatuses = Map.of(
            "ord-1001", new OrderStatusResponse("ord-1001", OrderState.PROCESSING, "Provisioning SIM", Instant.now().minusSeconds(3600)),
            "ord-1002", new OrderStatusResponse("ord-1002", OrderState.COMPLETED, "Activated", Instant.now().minusSeconds(7200)),
            "ord-1003", new OrderStatusResponse("ord-1003", OrderState.FAILED, "Payment declined", Instant.now().minusSeconds(1800))
    );

    @McpTool(name = "get_order_status")
    public OrderStatusResponse getOrderStatus(String orderId) {
        OrderStatusResponse status = orderStatuses.get(orderId);
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return status;
    }
}
