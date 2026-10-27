
package com.api.flatsxchange.subscriptionmodule.service;




import com.api.flatsxchange.subscriptionmodule.client.SubscriptionClient;
import com.api.flatsxchange.subscriptionmodule.dto.CreateOrderRequest;
import com.api.flatsxchange.subscriptionmodule.dto.OrderResponse;
import com.api.flatsxchange.subscriptionmodule.dto.PlanResponse;
import com.api.flatsxchange.subscriptionmodule.dto.VerifyPaymentRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final SubscriptionClient client;

    public SubscriptionService(SubscriptionClient client) {
        this.client = client;
    }

    public PlanResponse getPlans() {
        return client.getPlans();
    }

    public OrderResponse createOrder(
            String planCode,
            String authenticatedUserId) {

        return client.createOrder(
                new CreateOrderRequest(planCode, authenticatedUserId));
    }

    public JsonNode verifyPayment(VerifyPaymentRequest request) {

        return client.verifyPayment(request);
    }

    public JsonNode getSubscriptionStatus(String externalUserId) {

        return client.getSubscriptionStatus(externalUserId);
    }


}
