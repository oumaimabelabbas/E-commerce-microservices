package com.oumaima.order.kafka;

import com.oumaima.order.customer.CustomerResponse;
import com.oumaima.order.order.PaymentMethod;
import com.oumaima.order.product.PurchaseRequest;
import com.oumaima.order.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
