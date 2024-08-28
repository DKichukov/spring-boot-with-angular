package com.example.crudspringproject.service.impl;

import static com.example.crudspringproject.mapper.CustomerMapper.toDto;
import static com.example.crudspringproject.mapper.CustomerMapper.toDtoList;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import com.example.crudspringproject.mapper.CustomerMapper;
import com.example.crudspringproject.repository.CustomerRepository;
import com.example.crudspringproject.service.CustomerService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto postCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return toDto(savedCustomer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return toDtoList(customers);
    }

    public CustomerDto getCustomerById(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(CustomerMapper::toDto)
            .orElse(null);
    }
}
