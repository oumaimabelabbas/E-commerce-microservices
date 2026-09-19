package com.oumaima.ecommerce.customer;

import com.oumaima.ecommerce.exception.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public String createCustomer(@Valid CustomerRequest request) {
        var customer = customerRepository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {
         var customer = customerRepository.findById(request.id()).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
         mergerCustomer(customer,request);
         customerRepository.save(customer);

    }

    private void mergerCustomer(Customer customer, @Valid CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstname())){
            customer.setFirstname(request.firstname());
        }
        if(StringUtils.isNotBlank(request.lastname())){
            customer.setLastname(request.lastname());
        }
        if(StringUtils.isNotBlank(request.email())){
            customer.setEmail(request.email());
        }
        if(request.address()!=null){
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        //convert the list to stream , then for each customer apply mapper.fromCustomer(Customer) then convert it again to list
        return customerRepository.findAll().stream().map(mapper::fromCustomer).collect(Collectors.toList());
    }

    public  Boolean existsById(String customerId) {
        //isPresent true or false if it exists
        return customerRepository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return customerRepository.findById(customerId).map(mapper::fromCustomer).orElseThrow(()->new CustomerNotFoundException("Customer not found"));

    }

    public void deleteById(String customerId) {
        customerRepository.deleteById(customerId);

    }
}
