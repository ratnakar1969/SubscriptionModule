package com.api.flatsxchange.subscriptionmodule.dto;

public record VerifyPaymentRequest(
        String orderId,
        String razorpayOrderId,
        String razorpayPaymentId,
        String razorpaySignature
) {}
