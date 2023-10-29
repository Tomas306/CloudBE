package sk.umb.example.library.customer.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.umb.example.library.customer.service.CustomerRequestDto;
import sk.umb.example.library.customer.service.CustomerDetailDto;
import sk.umb.example.library.customer.service.CustomerService;

import java.net.URI;
import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/api/customers")
    public List<CustomerDetailDto> searchCustomer(@RequestParam(required = false) String lastName) {
        logger.info("Search customer called.");

        if (lastName == null || lastName.trim().isEmpty()) {
            return customerService.getAllCustomers();
        } else {
            return customerService.searchCustomerByLastName(lastName.trim());
        }
    }

    @GetMapping("/api/customers/{customerId}")
    public CustomerDetailDto getCustomer(@PathVariable Long customerId) {
        logger.info("Get customer called with ID: {}", customerId);
        return customerService.getCustomerById(customerId);
    }

    @PostMapping("/api/customers")
    public ResponseEntity<Long> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDTO) {
        logger.info("Create customer called.");
        Long customerId = customerService.createCustomer(customerRequestDTO);
        return ResponseEntity.created(URI.create("/api/customers/" + customerId)).body(customerId);
    }

    @PutMapping("/api/customers/{customerId}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long customerId, @Valid  @RequestBody CustomerRequestDto customerRequestDTO) {
        logger.info("Update customer called: ID = {}", customerId);
        customerService.updateCustomer(customerId, customerRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        logger.info("Delete customer called: ID = {}", customerId);
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
