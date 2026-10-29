package com.api.flatsxchange.subscriptionmodule.dto;

import java.util.List;




import java.util.List;

public record PlanResponse(
        Product product,
        Pricing pricing,
        List<Plan> plans
) {
    public record Product(
            String code,
            String name
    ) {}

    public record Pricing(
            String country,
            String currency
    ) {}

    public record Plan(
            String code,
            String name,
            long priceMinor,
            String currency,
            long pricePaise,
            double priceRupees,
            int durationDays,
            String pricingSource,
            List<Feature> features
    ) {}

    public record Feature(
            String code,
            String name,
            String description,
            boolean enabled
    ) {}
}
