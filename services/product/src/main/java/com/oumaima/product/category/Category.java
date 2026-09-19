package com.oumaima.product.category;

import com.oumaima.product.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    //when i remove a category remove all products related to this category
    @OneToMany(mappedBy = "category",cascade= CascadeType.REMOVE)
    private List<Product> products;
}
