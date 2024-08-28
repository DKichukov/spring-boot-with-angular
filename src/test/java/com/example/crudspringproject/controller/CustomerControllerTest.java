package com.example.crudspringproject.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import com.example.crudspringproject.repository.CustomerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        if (customerRepository.count() > 0) {
            customerRepository.deleteAll();
        }
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
        createCustomer("John Doe", "123456789", "john.doe@example.com");
        createCustomer("Jane Doe", "987654321", "jane.doe@example.com");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<CustomerDto> retrievedCustomers = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        assertNotNull(retrievedCustomers);
        assertEquals(2, retrievedCustomers.size());
        CustomerDto retrievedCustomer1 = retrievedCustomers.stream()
            .filter(c -> c.email().equals("john.doe@example.com"))
            .findFirst()
            .orElse(null);
        assertNotNull(retrievedCustomer1);
        assertEquals("John Doe", retrievedCustomer1.name());
        assertEquals("123456789", retrievedCustomer1.phone());

        CustomerDto retrievedCustomer2 = retrievedCustomers.stream()
            .filter(c -> c.email().equals("jane.doe@example.com"))
            .findFirst()
            .orElse(null);
        assertNotNull(retrievedCustomer2);
        assertEquals("Jane Doe", retrievedCustomer2.name());
        assertEquals("987654321", retrievedCustomer2.phone());

    }

    @Test
    void getCustomerByIdShouldReturnCorrectCustomer() throws Exception {
        Customer customer = createCustomer("John Doe", "123456789", "john.doe@example.com");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}", customer.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        CustomerDto retrievedCustomer = objectMapper.readValue(responseContent, CustomerDto.class);

        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");
        assertEquals("John Doe", retrievedCustomer.name(), "Customer name should match");
        assertEquals("123456789", retrievedCustomer.phone(), "Customer phone should match");
        assertEquals("john.doe@example.com", retrievedCustomer.email(), "Customer email should match");
    }

    @Test
    void getCustomerByIdShouldReturn404WhenCustomerNotFound() throws Exception {

        int nonExistentCustomerId = 111;
        String expectedErrorMessage = "Customer not found with id: " + nonExistentCustomerId;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/{id}", nonExistentCustomerId))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateCustomerShouldReturnUpdatedCustomer() throws Exception {
        Customer customer = createCustomer("John Doe", "123456789", "john.doe@example.com");
        Customer newCustomer = createCustomer("Jane Doe", "987654321", "jane.doe@example.com");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/" + customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomer)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        CustomerDto retrievedCustomer = objectMapper.readValue(responseContent, CustomerDto.class);

        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");
        assertEquals("Jane Doe", retrievedCustomer.name(), "Customer name should match");
        assertEquals("987654321", retrievedCustomer.phone(), "Customer phone should match");
        assertEquals("jane.doe@example.com", retrievedCustomer.email(), "Customer email should match");
    }

    @Test
    void updateCustomerShouldReturnEmptyCustomer() throws Exception {
        int id = 123;
        Customer newCustomer = createCustomer("Jane Doe", "987654321", "jane.doe@example.com");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomer)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteCustomerShouldReturnOkStatus() throws Exception {
        Customer customer = createCustomer("Jane Doe", "987654321", "jane.doe@example.com");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/{id}", customer.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(customerRepository.count()).isEqualTo(0);
    }

    @Test
    void deleteCustomerShouldReturnNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/{id}", 1321))
            .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    private Customer createCustomer(String name, String phone, String email) {
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


