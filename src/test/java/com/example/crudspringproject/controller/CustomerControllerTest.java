package com.example.crudspringproject.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import com.example.crudspringproject.repository.CustomerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerControllerTest {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.0");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void init() {
        postgres.start();
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void testPostCustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto(1, "John Doe", "john.doe@example.com", "123456789");

        MvcResult result;
            result = mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customerDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Customer createdCustomer = objectMapper.readValue(result.getResponse().getContentAsString(), Customer.class);
        assertThat(createdCustomer.getId()).isNotNull();
        assertThat(createdCustomer.getName()).isEqualTo("John Doe");
        assertThat(createdCustomer.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(createdCustomer.getPhone()).isEqualTo("123456789");
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Customer customer1 = createCustomer("John Doe",  "123456789","john.doe@example.com");
        Customer customer2 = createCustomer("Jane Doe", "987654321","jane.doe@example.com");
        List<Customer> customers = Arrays.asList(customer1, customer2);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customers)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<CustomerDto> retrievedCustomers = objectMapper.readValue(responseContent, new TypeReference<List<CustomerDto>>() {});


        Assertions.assertNotNull(retrievedCustomers);
        assertEquals(2, retrievedCustomers.size());
        CustomerDto retrievedCustomer1 = retrievedCustomers.stream()
            .filter(c -> c.email().equals("john.doe@example.com"))
            .findFirst()
            .orElse(null);
        Assertions.assertNotNull(retrievedCustomer1);
        assertEquals("John Doe", retrievedCustomer1.name());
        assertEquals("123456789", retrievedCustomer1.phone());

        CustomerDto retrievedCustomer2 = retrievedCustomers.stream()
            .filter(c -> c.email().equals("jane.doe@example.com"))
            .findFirst()
            .orElse(null);
        Assertions.assertNotNull(retrievedCustomer2);
        assertEquals("Jane Doe", retrievedCustomer2.name());
        assertEquals("987654321", retrievedCustomer2.phone());

    }

    private Customer createCustomer(String name, String phone,String email) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setEmail(email);

        return customerRepository.save(customer);
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


