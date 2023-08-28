package ru.workwear.server.auth.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.workwear.server.auth.models.Address;

public interface AddressRepository extends ReactiveCrudRepository<Address,Long> {
}
