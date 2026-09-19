package com.oumaima.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerResponse(
        String Id,
        String firstname,
        String lastname,
        String email,
        Address address
) {
}
