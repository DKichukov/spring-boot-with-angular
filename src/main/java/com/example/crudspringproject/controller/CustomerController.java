package com.example.crudspringproject.controller;

import static com.example.crudspringproject.mapper.CustomerMapper.toEntity;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customer")
    ResponseEntity<CustomerDto> postCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomerDto = customerService.postCustomer(toEntity(customerDto));
        return ResponseEntity.ok(savedCustomerDto);
    }

    @GetMapping("/customers")
    ResponseEntity<List<CustomerDto>> getCustomers() {
        List<CustomerDto> allCustomers = customerService.getAllCustomers();
        return ResponseEntity.ok(allCustomers);
    }

    @GetMapping("/customers/{id}")
    ResponseEntity<CustomerDto> getCustomerById(@PathVariable Integer id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }

    @PutMapping("/customers/{id}")
    ResponseEntity<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDto customerDto) {
        CustomerDto updatedCustomerDto = customerService.updateCustomer(id, toEntity(customerDto));
        return ResponseEntity.ok(updatedCustomerDto);
    }

    @DeleteMapping("/customers/{id}")
    ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}
