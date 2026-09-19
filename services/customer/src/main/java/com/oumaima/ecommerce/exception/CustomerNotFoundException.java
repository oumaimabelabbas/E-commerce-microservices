package com.oumaima.ecommerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data //generates getters/setters/a required arguments constructor for final fields
public class CustomerNotFoundException extends RuntimeException{
    private final String msg;
}
