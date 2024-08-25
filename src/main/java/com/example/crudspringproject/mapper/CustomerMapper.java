package com.example.crudspringproject.mapper;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerDto toDto(Customer customer) {

        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone());
    }

    public static Customer toEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.id());
        customer.setName(customerDto.name());
        customer.setEmail(customerDto.email());
        customer.setPhone(customerDto.phone());
        return customer;
    }

}
