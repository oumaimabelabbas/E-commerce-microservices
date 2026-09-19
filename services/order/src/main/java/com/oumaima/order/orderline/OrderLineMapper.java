package com.oumaima.order.orderline;

import com.oumaima.order.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        return new OrderLine(orderLineRequest.id(),Order.builder().id(orderLineRequest.order_id()).build(),orderLineRequest.product_id(),orderLineRequest.quantity());
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
