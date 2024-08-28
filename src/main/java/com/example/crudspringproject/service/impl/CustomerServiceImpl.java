package com.example.crudspringproject.service.impl;

import static com.example.crudspringproject.mapper.CustomerMapper.toDto;
import static com.example.crudspringproject.mapper.CustomerMapper.toDtoList;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import com.example.crudspringproject.exception.CustomerNotFoundException;
import com.example.crudspringproject.mapper.CustomerMapper;
import com.example.crudspringproject.repository.CustomerRepository;
import com.example.crudspringproject.service.CustomerService;
import java.util.List;
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
        return customerRepository.findById(id)
            .map(CustomerMapper::toDto)
            .orElseThrow(() -> {
                return new CustomerNotFoundException("Customer not found with id: " + id);
            });
    }

    @Override
    public CustomerDto updateCustomer(Integer id, Customer customer) {
        Customer foundCustomer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        foundCustomer.setName(customer.getName());
        foundCustomer.setEmail(customer.getEmail());
        foundCustomer.setPhone(customer.getPhone());

        return toDto(customerRepository.save(foundCustomer));
    }

    @Override
    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }
}
