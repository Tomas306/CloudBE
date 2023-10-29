package sk.umb.example.library.customer.service;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import sk.umb.example.library.address.persistence.entity.AddressEntity;
import sk.umb.example.library.address.persistence.repository.AddressRepository;
import sk.umb.example.library.address.service.AddressDetailDto;
import sk.umb.example.library.customer.persistence.entity.CustomerEntity;
import sk.umb.example.library.customer.persistence.repository.CustomerRepository;
import sk.umb.example.library.exception.LibraryApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@CacheConfig(cacheNames = "customers")
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerService(CustomerRepository customerRepository,
                           AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @Cacheable(key = "'all'", unless="#result.size() == 0")
    public List<CustomerDetailDto> getAllCustomers() {
        return mapToDtoList(customerRepository.findAll());
    }

    public List<CustomerDetailDto> searchCustomerByLastName(String lastName) {
        return mapToDtoList(customerRepository.findByLastName(lastName));
    }


    public CustomerDetailDto getCustomerById(Long customerId) {
        return mapToDto(getCustomerEntityById(customerId));
    }

    @Transactional
    public Long createCustomer(CustomerRequestDto customerRequestDto) {
        CustomerEntity entity = mapToEntity(customerRequestDto);
        return customerRepository.save(entity).getId();
    }

    @Transactional
    public void updateCustomer(Long customerId, CustomerRequestDto customerRequestDTO) {
        CustomerEntity customer = getCustomerEntityById(customerId);

        if (! Strings.isEmpty(customerRequestDTO.getFirstName())) {
            customer.setFirstName(customerRequestDTO.getFirstName());
        }

        if (! Strings.isEmpty(customerRequestDTO.getLastName())) {
            customer.setLastName(customerRequestDTO.getLastName());
        }


        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    private CustomerEntity getCustomerEntityById(Long customerId) {
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            throw new LibraryApplicationException("Customer not found. ID: " + customerId);
        }

        return customer.get();
    }

    private CustomerEntity mapToEntity(CustomerRequestDto dto) {
        CustomerEntity customer = new CustomerEntity();

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());

        return customer;
    }

    private List<CustomerDetailDto> mapToDtoList(Iterable<CustomerEntity> customerEntities) {
        List<CustomerDetailDto> customers = new ArrayList<>();

        customerEntities.forEach(customerEntity -> {
                    CustomerDetailDto dto = mapToDto(customerEntity);
                    customers.add(dto);
                });

        return customers;
    }

    private CustomerDetailDto mapToDto(CustomerEntity customerEntity) {
        CustomerDetailDto dto = new CustomerDetailDto();
        dto.setId(customerEntity.getId());
        dto.setFirstName(customerEntity.getFirstName());
        dto.setLastName(customerEntity.getLastName());


        return dto;
    }

    private AddressDetailDto mapToDto(AddressEntity addressEntity) {
        AddressDetailDto dto = new AddressDetailDto();
        dto.setId(addressEntity.getId());
        dto.setCity(addressEntity.getCity());

        return dto;
    }
}
