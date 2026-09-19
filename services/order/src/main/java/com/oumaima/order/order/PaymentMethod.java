package com.oumaima.order.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


public enum PaymentMethod {
    PAYPAL,
    CREDIT_CARD,
    VISA,
    MASTER_CARD,
    BITCOIN
}