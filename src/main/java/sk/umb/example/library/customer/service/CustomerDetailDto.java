package sk.umb.example.library.customer.service;

import sk.umb.example.library.address.service.AddressDetailDto;

public class CustomerDetailDto {
    private Long id;
    private String firstName;
    private String lastName;

    private String emailContact;

    public AddressDetailDto getAddress() {
        return address;
    }

    public void setAddress(AddressDetailDto address) {
        this.address = address;
    }

    private AddressDetailDto address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }
}
