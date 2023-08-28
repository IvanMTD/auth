package ru.workwear.server.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.workwear.server.auth.dto.UserDTO;
import ru.workwear.server.auth.models.Address;
import ru.workwear.server.auth.models.User;
import ru.workwear.server.auth.repositories.AddressRepository;
import ru.workwear.server.auth.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> save(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAuthorities(List.of(User.Authority.AUTHORITY_Read));

        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setMiddleName(userDTO.getMiddleName());

        user.setEMail(userDTO.getEMail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setGender(userDTO.getGender());

        user.setBirthdate(userDTO.getBirthdate());
        user.setRegistrationDate(LocalDate.now());

        Address address = new Address();
        address.setIndex(userDTO.getAddressDTO().getIndex());
        address.setCountry(userDTO.getAddressDTO().getCountry());
        address.setLocality(userDTO.getAddressDTO().getLocality());
        address.setStreet(userDTO.getAddressDTO().getStreet());
        address.setHouse(userDTO.getAddressDTO().getHouse());
        address.setApartment(userDTO.getAddressDTO().getApartment());
        addressRepository.save(address).subscribe(user::setAddress);

        return userRepository.save(user);
    }

    public Mono<User> findById(long id) {
        return userRepository.findById(id);
    }

    public Mono<Boolean> existByUsername(String username){
        return findByUsername(username)
                .cast(User.class)
                .map(user -> true)
                .defaultIfEmpty(false);
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> addressRepository.findById(user.getAddressId())
                        .map(address -> {
                            user.setAddress(address);
                            return user;
                        }));
    }
}
