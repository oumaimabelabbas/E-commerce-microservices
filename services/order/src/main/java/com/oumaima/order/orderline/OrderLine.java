package com.oumaima.order.orderline;

import com.oumaima.order.order.Order;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Data
public class OrderLine {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name="order_id") //mendatory if u dont specefiy it will be order+id(named in class order)
    private Order order;
    private Integer productId; //product and orderline are not on the same server
    private double quantity;
}
