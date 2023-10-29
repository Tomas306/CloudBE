package sk.umb.example.library.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import sk.umb.example.library.customer.service.CustomerDetailDto;
import sk.umb.example.library.customer.service.CustomerService;
import sk.umb.example.library.exception.LibraryApplicationException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // NOTE: @MockBean is a spring class. Not a Mockito class
    @MockBean
    private CustomerService customerService;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void searchCustomerAll() throws Exception {
        CustomerDetailDto customerDetailDto = new CustomerDetailDto();
        customerDetailDto.setFirstName("John");

        when(customerService.getAllCustomers())
                .thenReturn(List.of(customerDetailDto));

        mockMvc.perform(
                get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName",
                                    Matchers.equalTo("John")));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    public void searchCustomerEmpty() throws Exception {
        when(customerService.getAllCustomers())
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/api/customers"))
                .andExpect(status().isOk());

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    public void getCustomerByIdFound() throws Exception {
        CustomerDetailDto customerDetailDto = new CustomerDetailDto();
        customerDetailDto.setFirstName("John");
        customerDetailDto.setLastName("Woody");

        when(customerService.getCustomerById(any()))
                .thenReturn(customerDetailDto);

        mockMvc.perform(
                        get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        Matchers.equalTo("John")))
                .andExpect(jsonPath("$.lastName",
                        Matchers.equalTo("Woody")));

        verify(customerService, times(1)).getCustomerById(any());
    }

    @Test
    public void getCustomerByIdNotFound() throws Exception {
        when(customerService.getCustomerById(any()))
                .thenThrow(new LibraryApplicationException("Customer not found. ID: 1"));

        mockMvc.perform(
                        get("/api/customers/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error",
                        Matchers.equalTo("Customer not found. ID: 1")));

        verify(customerService, times(1)).getCustomerById(any());
    }
}