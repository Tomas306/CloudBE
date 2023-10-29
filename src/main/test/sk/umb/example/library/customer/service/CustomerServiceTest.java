package sk.umb.example.library.customer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.umb.example.library.address.persistence.entity.AddressEntity;
import sk.umb.example.library.address.persistence.repository.AddressRepository;
import sk.umb.example.library.customer.persistence.entity.CustomerEntity;
import sk.umb.example.library.customer.persistence.repository.CustomerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Test
    public void getAllCustomersOk() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1L);
        customerEntity.setEmailContact("emai@test.com");
        customerEntity.setFirstName("John");
        customerEntity.setLastName("tester");

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCity("Banska Bystrica");
        addressEntity.setId(2l);

        customerEntity.setAddress(addressEntity);

        when(customerRepository.findAll())
                .thenReturn(List.of(customerEntity));

        List<CustomerDetailDto> customers = customerService.getAllCustomers();

        verify(customerRepository, times(1)).findAll();

        assertEquals(customers.size(), 1);
        assertEquals(customers.get(0).getEmailContact(), "emai@test.com");
    }
}