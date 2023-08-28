package ru.workwear.server.auth.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.workwear.server.auth.models.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByUsername (String username);
}
