package ru.workwear.server.auth.configurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http){
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .securityContextRepository(securityContextRepository)
                .authenticationManager(authenticationManager)
                .authorizeExchange(auth -> {
                    auth
                            .pathMatchers(
                                    "/api/auth/user/save",
                                    "/api/auth/user/check/**",
                                    "/api/auth/refresh/login",
                                    "/api/auth/login"
                            ).permitAll()
                            .anyExchange()
                            .authenticated();
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
