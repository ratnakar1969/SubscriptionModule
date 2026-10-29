
package com.api.flatsxchange.subscriptionmodule.controller;


import com.api.flatsxchange.subscriptionmodule.dto.*;
import com.api.flatsxchange.subscriptionmodule.exception.ActiveSubscriptionException;
import com.api.flatsxchange.subscriptionmodule.service.SubscriptionService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SubscriptionController {

    Logger subscriptionLog;

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/plans")
    public PlanResponse getPlans() {
        return subscriptionService.getPlans();
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public  ResponseEntity<?> createOrder(
            @RequestBody CreateOrderBody body) {

       // Obtain the stable ID from your existing authenticated principal.
        try {
            OrderResponse response = subscriptionService.createOrder(
                    body.planCode(), body.externalUserId());
            return ResponseEntity.ok(response);


        }
        catch (ActiveSubscriptionException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "ACTIVE_SUBSCRIPTION_EXISTS",
                            "message",
                            "You already have an active subscription. Please check your current plan."
                    ));
        }

    }

    public record CreateOrderBody(String planCode,String externalUserId) {}

    // ProductUserPrincipal is illustrative.
    // Replace it with your application's existing principal class.

    @PostMapping("/payments/verify")
    public JsonNode verifyPayment(
            @RequestBody VerifyPaymentRequest request) {

        return subscriptionService.verifyPayment(request);
    }

    @GetMapping("/status")
    public JsonNode getSubscriptionStatus(
            @RequestParam String externalUserId) {

        return subscriptionService.getSubscriptionStatus(externalUserId);
    }


    @PostMapping("/usage/consume")
    public JsonNode consumeUsage(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody ConsumeUsageRequest body) {

        return subscriptionService.consumeUsage(
                body.userId(),
                body.metricKey(),
                body.quantity(),
                idempotencyKey
        );
    }
    @PostMapping("/entitlements/check")
    public JsonNode checkEntitlements(
            @RequestBody CheckEntitlementRequest body) {

        return subscriptionService.checkEntitlements(
                body.externalUserId(),
                body.featureCode()
        );
    }



}
