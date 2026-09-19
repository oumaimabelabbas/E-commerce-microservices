package com.oumaima.order.order;

import com.oumaima.order.customer.CustomerClient;
import com.oumaima.order.customer.CustomerResponse;
import com.oumaima.order.exception.BusinessException;
import com.oumaima.order.kafka.OrderConfirmation;
import com.oumaima.order.kafka.OrderProducer;
import com.oumaima.order.orderline.OrderLineRequest;
import com.oumaima.order.orderline.OrderLineService;
import com.oumaima.order.payment.PaymentClient;
import com.oumaima.order.payment.PaymentRequest;
import com.oumaima.order.product.ProductClient;
import com.oumaima.order.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderProducer orderProducer;
    private final OrderLineService orderLineService;
    private final PaymentClient paymentClient;
    public Integer createOrder(@Valid OrderRequest request) {
        //check the customer -->openfeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(()->new BusinessException("Cannot create order :: No customer exists with the provided Id"));
        //purchase the product -->product-ms (RestTemplate)
        var purchaseproducts = this.productClient.purchaseProducts(request.products());
        //persist order
        var order = this.orderRepository.save(mapper.toOrder(request));
        //persist the order lines
        for(PurchaseRequest purchaseRequest : request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    ));

        }
        //start payment process
        paymentClient.requestOrderPayment(new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        ));
        //send the order confirmation --> notif-ms (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchaseproducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll().stream().map(mapper::toOrderResponse).collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId).map(mapper::toOrderResponse).orElseThrow(()-> new EntityNotFoundException("No order found with this id"));
    }
}
