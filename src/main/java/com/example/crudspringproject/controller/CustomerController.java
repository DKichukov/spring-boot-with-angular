package com.example.crudspringproject.controller;

import static com.example.crudspringproject.mapper.CustomerMapper.toEntity;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import com.example.crudspringproject.service.impl.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customer")
    Customer postCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.postCustomer(toEntity(customerDto));
    }

    @GetMapping("/customers")
    List<CustomerDto> getCustomers() {
        return customerService.getAllCustomers();
    }
}
