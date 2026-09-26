package com.oumaima.order.payment;

import com.oumaima.order.customer.CustomerResponse;
import com.oumaima.order.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
