package ru.workwear.server.auth.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.workwear.server.auth.models.RefreshToken;

public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken,Long> {
    Mono<RefreshToken> findByToken(String token);
    Mono<Void> deleteByToken(String token);
}
