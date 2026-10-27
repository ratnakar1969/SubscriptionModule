
package com.api.flatsxchange.subscriptionmodule.client;



import com.api.flatsxchange.subscriptionmodule.dto.CreateOrderRequest;
import com.api.flatsxchange.subscriptionmodule.dto.OrderResponse;
import com.api.flatsxchange.subscriptionmodule.dto.PlanResponse;
import com.api.flatsxchange.subscriptionmodule.dto.VerifyPaymentRequest;
import com.api.flatsxchange.subscriptionmodule.exception.ActiveSubscriptionException;
import com.fasterxml.jackson.databind.JsonNode;
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

            return subscriptionRestClient.post()
                    .uri("/api/payments/verify")
                    .body(request)
                    .retrieve()
                    .body(JsonNode.class);
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
    }






