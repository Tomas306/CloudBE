package sk.umb.example.library.address.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.umb.example.library.address.persistence.entity.AddressEntity;
import sk.umb.example.library.address.persistence.repository.AddressRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<AddressDetailDto> getAllAddresses() {
        return mapToDto(addressRepository.findAll());
    }

    @Transactional
    public Long createAddress(AddressRequestDto requestDto) {
        return addressRepository.save(mapToEntity(requestDto)).getId();
    }

    private List<AddressDetailDto> mapToDto(List<AddressEntity> addressEntities) {
        List<AddressDetailDto> dtos = new ArrayList<>();

        for (AddressEntity ae : addressEntities) {
            AddressDetailDto dto = new AddressDetailDto();

            dto.setId(ae.getId());
            dto.setCity(ae.getCity());

            dtos.add(dto);
        }

        return dtos;
    }

    private AddressEntity mapToEntity(AddressRequestDto dto) {
        AddressEntity ae = new AddressEntity();

        ae.setCity(dto.getCity());

        return ae;
    }
}
