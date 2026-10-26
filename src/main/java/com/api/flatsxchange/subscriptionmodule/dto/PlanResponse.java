package com.api.flatsxchange.subscriptionmodule.dto;

import java.util.List;

public record PlanResponse(
        Product product,
        List<Plan> plans
) {
    public record Product(String code, String name) {}

    public record Plan(
            String code,
            String name,
            long pricePaise,
            double priceRupees,
            int durationDays
    ) {}
}
