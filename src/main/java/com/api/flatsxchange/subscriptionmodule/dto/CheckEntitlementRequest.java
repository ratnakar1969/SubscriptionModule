package com.api.flatsxchange.subscriptionmodule.dto;

public record CheckEntitlementRequest(
        String externalUserId,
        String featureCode
) {}
