package ru.workwear.server.auth.configurations.application;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.workwear.server.auth.models.Gender;
import ru.workwear.server.auth.models.User;
import ru.workwear.server.auth.repositories.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner preLoad(ConnectionFactory connectionFactory){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                log.info("Pre-started configuring");
                //insertUser();
            }
        };
    }

    private void insertUser(){
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("Admin_123!"));
        List<User.Authority> authorities = new ArrayList<>();
        authorities.add(User.Authority.AUTHORITY_Create);
        authorities.add(User.Authority.AUTHORITY_Read);
        authorities.add(User.Authority.AUTHORITY_Update);
        authorities.add(User.Authority.AUTHORITY_Delete);
        user.setAuthorities(authorities);
        user.setLastName("Карачков");
        user.setFirstName("Иван");
        user.setMiddleName("Сергеевич");
        user.setEMail("ivan.se.karachkov@gmail.com");
        user.setPhoneNumber("+79168792043");
        user.setGender(Gender.MALE);
        user.setBirthdate(LocalDate.of(1985,10,30));
        user.setRegistrationDate(LocalDate.now());
        user.setAddressId(1L);
        userRepository.save(user).subscribe();
    }
}
