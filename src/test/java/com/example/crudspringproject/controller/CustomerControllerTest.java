package com.example.crudspringproject.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.crudspringproject.dto.CustomerDto;
import com.example.crudspringproject.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


