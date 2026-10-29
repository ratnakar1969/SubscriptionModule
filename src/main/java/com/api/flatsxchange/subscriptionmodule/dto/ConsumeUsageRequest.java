package com.api.flatsxchange.subscriptionmodule.dto;

public record ConsumeUsageRequest(
        String userId,
        String metricKey,
        Integer quantity
) {}