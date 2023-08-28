package ru.workwear.server.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.workwear.server.auth.models.Address;
import ru.workwear.server.auth.repositories.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Flux<Address> findAll(){
        return addressRepository.findAll();
    }

    public Mono<Address> findById(long id){
        return addressRepository.findById(id);
    }

    public Mono<Address> save(Address address){
        return addressRepository.save(address);
    }
}
