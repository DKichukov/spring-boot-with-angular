package com.example.crudspringproject.mapper;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import java.util.List;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerDto toDto(Customer customer) {

        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone());
    }

    public static List<CustomerDto> toDtoList(List<Customer> customers) {
        return customers.stream().map(CustomerMapper::toDto).toList();
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
