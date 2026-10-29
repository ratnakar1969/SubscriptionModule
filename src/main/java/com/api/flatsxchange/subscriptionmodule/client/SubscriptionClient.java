
package com.api.flatsxchange.subscriptionmodule.client;



import com.api.flatsxchange.subscriptionmodule.dto.*;
import com.api.flatsxchange.subscriptionmodule.exception.ActiveSubscriptionException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

@Component


public class SubscriptionClient {

    String idempotencyKey = UUID.randomUUID().toString();

    private static final Logger log =
            LoggerFactory.getLogger(SubscriptionClient.class);


    @Autowired
    private RestClient subscriptionRestClient;
    public SubscriptionClient(
            RestClient.Builder builder,
            @Value("${subscription.api.base-url}") String baseUrl) {

        this.subscriptionRestClient = builder
                .baseUrl(baseUrl)
                .build();
    }


    public PlanResponse getPlans() {
        return subscriptionRestClient.get()
                .uri("/api/plans")
                .retrieve()
                .body(PlanResponse.class);
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        try {
            return subscriptionRestClient.post()
                    .uri("/api/orders")
                    .header("Idempotency-Key", idempotencyKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(OrderResponse.class);
        }
        catch (RestClientResponseException ex) {

            if (ex.getStatusCode().value() == 409) {
                throw new ActiveSubscriptionException(
                        ex.getResponseBodyAsString()
                );
            }

            throw ex;
        }

    }





        public JsonNode verifyPayment(VerifyPaymentRequest request) {
            log.info(
                    "Verifying payment: orderId={}, razorpayOrderId={}, razorpayPaymentId={}",
                    request.orderId(),
                    request.razorpayOrderId(),
                    request.razorpayPaymentId()
            );
            try {
                return subscriptionRestClient.post()
                        .uri("/api/payments/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .body(JsonNode.class);

            } catch (RestClientResponseException ex) {
                log.error(
                        "Payment verification failed. HTTP status: {}, response: {}",
                        ex.getStatusCode(),
                        ex.getResponseBodyAsString()
                );
                throw ex;
            }

            }

        public JsonNode getSubscriptionStatus(String externalUserId) {

            return subscriptionRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/subscriptions/status")
                            .queryParam("externalUserId", externalUserId)
                            .build())
                    .retrieve()
                    .body(JsonNode.class);
        }


    public JsonNode consumeUsage(
            ConsumeUsageRequest request,
            String idempotencyKey) {

        log.info(
                "Consuming subscription usage: userId={}, metricKey={}, quantity={}",
                request.userId(),
                request.quantity()
        );

        try {
            return subscriptionRestClient.post()
                    .uri("/api/usage/consume")
                    .header("Idempotency-Key", idempotencyKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(JsonNode.class);

        } catch (RestClientResponseException ex) {
            log.error(
                    "Usage consumption failed. HTTP status: {}, response: {}",
                    ex.getStatusCode(),
                    ex.getResponseBodyAsString()
            );
            throw ex;
        }
    }


    public JsonNode checkEntitlements(CheckEntitlementRequest request) {

        log.info(
                "Checking entitlement: externalUserId={}, featureCode={}",
                request.externalUserId(),
                request.featureCode()
        );

        try {
            return subscriptionRestClient.post()
                    .uri("/api/entitlements/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(JsonNode.class);

        } catch (RestClientResponseException ex) {
            log.error(
                    "Entitlement check failed. HTTP status: {}, response: {}",
                    ex.getStatusCode(),
                    ex.getResponseBodyAsString()
            );
            throw ex;
        }
    }


}









