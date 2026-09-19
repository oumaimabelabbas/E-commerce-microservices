package com.oumaima.product.exception;

import lombok.Data;


public class ProductPurchaseException extends RuntimeException {
    public ProductPurchaseException(String message){
        super(message);
    }

}
