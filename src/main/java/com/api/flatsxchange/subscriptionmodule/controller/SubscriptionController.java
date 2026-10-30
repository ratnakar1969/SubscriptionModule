
package com.api.flatsxchange.subscriptionmodule.controller;


import com.api.flatsxchange.subscriptionmodule.dto.*;
import com.api.flatsxchange.subscriptionmodule.exception.ActiveSubscriptionException;
import com.api.flatsxchange.subscriptionmodule.service.SubscriptionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SubscriptionController {


    private final SubscriptionService subscriptionService;

    ObjectMapper objectMapper = new ObjectMapper();


    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/plans")
    public ResponseEntity<JsonNode> getPlans() {

       JsonNode planResponse= subscriptionService.getPlans();

        return ResponseEntity.ok(planResponse);

    }

    @PostMapping("/orders")
    public  ResponseEntity<JsonNode> createOrder(
            @RequestBody CreateOrderRequest body) {

       // Obtain the stable ID from your existing authenticated principal.
        try {
            JsonNode orderResponse = subscriptionService.createOrder(
                    body.planCode(), body.externalUserId());
            return ResponseEntity.ok(orderResponse);


        }
        catch (ActiveSubscriptionException ex) {
            ObjectNode errorResponse = objectMapper.createObjectNode()
                    .put("error", "ACTIVE_SUBSCRIPTION_EXISTS")
                    .put("message",
                            "You already have an active subscription. Please check your current plan.");

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(errorResponse);


        }

    }


    // ProductUserPrincipal is illustrative.
    // Replace it with your application's existing principal class.

    @PostMapping("/payments/verify")
    public ResponseEntity<JsonNode> verifyPayment(
            @RequestBody VerifyPaymentRequest request) {

      JsonNode paymentVerifyResponse=subscriptionService.verifyPayment(request);
        return ResponseEntity.ok(paymentVerifyResponse);


    }


    @PostMapping("/usage/consume")
    public ResponseEntity<JsonNode> consumeUsage(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody ConsumeUsageRequest body) {

       JsonNode consumeUsageResponse=subscriptionService.consumeUsage(
                body.userId(),
                body.metricKey(),
                body.quantity(),
                idempotencyKey
        );
        return ResponseEntity.ok(consumeUsageResponse);

    }

    @PostMapping("/entitlements/check")
    public  ResponseEntity<JsonNode> checkEntitlements(
            @RequestBody CheckEntitlementRequest body) {

        JsonNode chkEntitlementResponse= subscriptionService.checkEntitlements(
                body.externalUserId(),
                body.featureCode()
        );
        return ResponseEntity.ok(chkEntitlementResponse);

    }

    @PostMapping("/usage/record-flats-listed")
    public ResponseEntity<JsonNode> recordFlatListed(
            @RequestBody RecordFlatListedRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {

        JsonNode response = subscriptionService.recordFlatListed(
                request.userId(),
                request.propertyId(),
                idempotencyKey
        );

        return ResponseEntity.ok(response);
    }



}
