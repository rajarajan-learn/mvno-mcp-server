package dev.thinktank.mvno.mcp.model;

import java.util.List;

public record UsageHistoryResponse(
        String subscriberId,
        List<UsageRecord> records
) {
}
