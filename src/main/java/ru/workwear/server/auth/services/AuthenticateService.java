package ru.workwear.server.auth.services;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.workwear.server.auth.configurations.security.JwtProvider;
import ru.workwear.server.auth.dto.AuthRequest;
import ru.workwear.server.auth.dto.UserDTO;
import ru.workwear.server.auth.models.RefreshToken;
import ru.workwear.server.auth.models.User;
import ru.workwear.server.auth.repositories.RefreshTokenRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public Mono<RefreshToken> generateRefreshToken(UserDTO userDTO){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(jwtProvider.generateRefreshToken(userDTO));
        refreshToken.setNewToken("NULL");
        return refreshTokenRepository.save(refreshToken);
    }

    public Mono<User> login(AuthRequest authRequest){
        if(jwtProvider.validateRefreshToken(authRequest.getRefreshToken())){
            log.info("Validation is ok");

            return refreshTokenRepository.findByToken(authRequest.getRefreshToken())
                    .flatMap(refreshToken -> {
                        log.info("Found refresh token in DB " + refreshToken.getToken());
                        log.info(refreshToken.getNewToken());
                        if(refreshToken.getNewToken().equals("NULL")){
                            log.info("New Token in current refresh token not found it's ok");
                            Claims claims = jwtProvider.getRefreshClaims(refreshToken.getToken());
                            String username = claims.getSubject();
                            String digitalSignature = (String)claims.get("digitalSignature");
                            if(digitalSignature.equals(authRequest.getDigitalSignature())){
                                log.info("Digital Signature is OK");
                                return userService.findByUsername(username)
                                        .map(user -> {
                                            user.setAccessToken(jwtProvider.generateAccessToken(user));
                                            return user;
                                        });
                            }else{
                                log.error("Digital Signature is NOT OK");
                                log.error("Digital Signature is base: " + digitalSignature);
                                log.error("Digital Signature in request " + authRequest.getDigitalSignature());
                                return Mono.empty();
                            }
                        }else{
                            log.info("Attention! Some one try login with old refresh token");
                            refreshTokenRepository.deleteByToken(refreshToken.getToken()).subscribe();
                            refreshTokenRepository.deleteByToken(refreshToken.getNewToken()).subscribe();
                            return Mono.empty();
                        }
                    })
                    .switchIfEmpty(Mono.empty());
        }else{
            log.info("Validation is false");
            return Mono.empty();
        }
    }

    public Mono<RefreshToken> login(UserDTO userDTO){
        return userService.findByUsername(userDTO.getUsername())
                .flatMap(user -> {
                    String rawPassword = userDTO.getPassword();
                    String password = user.getPassword();
                    if(passwordEncoder.matches(rawPassword,password)){
                        RefreshToken refreshToken = new RefreshToken();
                        refreshToken.setToken(jwtProvider.generateRefreshToken(userDTO));
                        refreshToken.setNewToken("NULL");
                        return refreshTokenRepository.save(refreshToken);
                    }else{
                        return Mono.empty();
                    }
                }).switchIfEmpty(Mono.empty());
    }

    public Mono<Void> delete(String refreshToken) {
        log.info("Try delete refreshToken");
        return refreshTokenRepository
                .deleteByToken(refreshToken)
                .onErrorComplete();
    }
}
