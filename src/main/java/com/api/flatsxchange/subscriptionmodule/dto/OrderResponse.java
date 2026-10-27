
package com.api.flatsxchange.subscriptionmodule.dto;

public record OrderResponse(
        String orderId,
        String razorpayOrderId,
        long amountPaise,
        String currency,
        Plan plan,
        String status,
        String keyId,
        String razorPaySignature
) {
    public record Plan(
            String code,
            String name,
            int durationDays
    ) {}
}
