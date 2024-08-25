package com.example.crudspringproject.service;

import com.example.crudspringproject.entity.Customer;
import com.example.crudspringproject.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer postCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
