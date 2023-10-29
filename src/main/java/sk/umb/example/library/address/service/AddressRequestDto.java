package sk.umb.example.library.address.service;

import jakarta.validation.constraints.NotBlank;

public class AddressRequestDto {
    @NotBlank(message = "City must not be empty.")
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
