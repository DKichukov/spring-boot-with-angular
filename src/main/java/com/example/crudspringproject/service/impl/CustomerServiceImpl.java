package com.example.crudspringproject.service.impl;

import static com.example.crudspringproject.mapper.CustomerMapper.toDtoList;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import com.example.crudspringproject.repository.CustomerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer postCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return toDtoList(customers);
    }
}
