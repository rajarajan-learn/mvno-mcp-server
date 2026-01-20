package dev.thinktank.mvno.mcp.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.thinktank.mvno.mcp.model.UsageHistoryResponse;
import dev.thinktank.mvno.mcp.model.UsageRecord;
import org.springaicommunity.mcp.annotation.McpTool;

@Service
public class UsageService {

    private final Map<String, List<UsageRecord>> usageBySubscriber = new HashMap<>();

    public UsageService() {
        seedUsage();
    }

    @McpTool
    public UsageHistoryResponse getUsageHistory(String subscriberId, LocalDate from, LocalDate to) {
        List<UsageRecord> records = usageBySubscriber.get(subscriberId);
        if (records == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscriber not found");
        }

        Instant fromInstant = from != null
                ? from.atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;
        Instant toInstant = to != null
                ? to.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)
                : null;

        List<UsageRecord> filtered = records.stream()
                .filter(record -> (fromInstant == null || !record.timestamp().isBefore(fromInstant))
                        && (toInstant == null || !record.timestamp().isAfter(toInstant)))
                .collect(Collectors.toList());

        return new UsageHistoryResponse(subscriberId, filtered);
    }

    private void seedUsage() {
        usageBySubscriber.put("sub-2001", List.of(
                new UsageRecord(Instant.now().minusSeconds(3600), 120.5, "5G"),
                new UsageRecord(Instant.now().minusSeconds(7200), 80.2, "5G"),
                new UsageRecord(Instant.now().minusSeconds(10800), 45.0, "4G")
        ));
        usageBySubscriber.put("sub-2002", List.of(
                new UsageRecord(Instant.now().minusSeconds(5400), 200.0, "5G"),
                new UsageRecord(Instant.now().minusSeconds(9000), 30.0, "4G")
        ));
    }
}
