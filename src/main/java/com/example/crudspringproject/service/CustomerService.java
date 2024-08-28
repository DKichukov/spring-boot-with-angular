package com.example.crudspringproject.service;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import java.util.List;

public interface CustomerService {

    CustomerDto postCustomer(Customer customer);

    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(Integer id);
}
