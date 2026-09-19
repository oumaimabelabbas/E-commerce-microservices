package com.oumaima.product.product;

import com.oumaima.product.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    public Integer createProduct(@Valid ProductRequest request) {
        var product = mapper.toProduct(request);
        return productRepository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        //list of product ids;
        var productIds = request.stream().map(ProductPurchaseRequest::productId).toList();
        //find all products with those ids
        var storedProducts = productRepository.findAllByIdInOrderById(productIds);
        if(productIds.size()!=storedProducts.size()){
            throw new ProductPurchaseException("One or more Products does not exists");
        }
        //list of request products with ids ordered
        var storedRequest = request.stream().sorted(Comparator.comparing(ProductPurchaseRequest::productId)).toList();
        var purshaedProducts = new ArrayList<ProductPurchaseResponse>();
        for(int i =0;i<storedProducts.size();i++){
            var product = storedProducts.get(i);
            var productRequest = storedRequest.get(i);
            //if requested quantity is bigger than available quanity of product throw error
            if(product.getAvailableQuantity() < productRequest.quantity()){
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID ::"+productRequest.productId());
            }
            //else we will update the new quantity (available - requested)
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);
            purshaedProducts.add(mapper.toProductPurshaseResponse(product,productRequest.quantity()));
        }
        return purshaedProducts;
    }
    public ProductResponse findById(Integer productId) {
        return productRepository.findById(productId).map(mapper::toProductResponse).orElseThrow(()->new EntityNotFoundException("Product not found"));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(mapper::toProductResponse).collect(Collectors.toList());
    }


}

