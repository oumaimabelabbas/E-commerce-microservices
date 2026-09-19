package com.oumaima.order.orderline;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderLineRequest(
        Integer id,
        Integer order_id,
        @NotNull(message = "Product is mandatory")
        Integer product_id,
        @Positive(message = "Quantity is mandatory")
        double quantity) {
}
