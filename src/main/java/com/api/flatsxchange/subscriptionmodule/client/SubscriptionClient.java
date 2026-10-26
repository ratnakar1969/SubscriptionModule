
package com.api.flatsxchange.subscriptionmodule.client;



import com.api.flatsxchange.subscriptionmodule.dto.CreateOrderRequest;
import com.api.flatsxchange.subscriptionmodule.dto.OrderResponse;
import com.api.flatsxchange.subscriptionmodule.dto.PlanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SubscriptionClient {

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
        return subscriptionRestClient.post()
                .uri("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OrderResponse.class);
    }


}
