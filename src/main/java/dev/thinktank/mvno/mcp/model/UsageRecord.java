package dev.thinktank.mvno.mcp.model;

import java.time.Instant;

public record UsageRecord(
        Instant timestamp,
        double dataUsedMb,
        String networkType
) {
}
