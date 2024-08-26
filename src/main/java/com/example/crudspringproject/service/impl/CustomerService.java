package com.example.crudspringproject.service.impl;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import java.util.List;

public interface CustomerService {

    Customer postCustomer(Customer customer);

    List<CustomerDto> getAllCustomers();
}
