
package com.api.flatsxchange.subscriptionmodule.controller;


import com.api.flatsxchange.subscriptionmodule.dto.OrderResponse;
import com.api.flatsxchange.subscriptionmodule.dto.PlanResponse;
import com.api.flatsxchange.subscriptionmodule.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class SubscriptionController {

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
    public OrderResponse createOrder(
            @RequestBody CreateOrderBody body) {


        // Obtain the stable ID from your existing authenticated principal.
        return subscriptionService.createOrder(
                body.planCode(), body.userId()
        );
    }

    public record CreateOrderBody(String planCode,String userId) {}

    // ProductUserPrincipal is illustrative.
    // Replace it with your application's existing principal class.
}
