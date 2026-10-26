package com.api.flatsxchange.subscriptionmodule.dto;


public record CreateOrderRequest(
        String planCode,
        String externalUserId
) {}