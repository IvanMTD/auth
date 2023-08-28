package ru.workwear.server.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.workwear.server.auth.dto.AuthRequest;
import ru.workwear.server.auth.dto.UserDTO;
import ru.workwear.server.auth.models.User;
import ru.workwear.server.auth.services.AuthenticateService;
import ru.workwear.server.auth.services.UserService;

/***********
 * CRUD    *
 * Create  *
 * Read    *
 * Update  *
 * Delete  *
 ***********/

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticateService authenticateService;

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> baseLogin(@RequestBody UserDTO userDTO){
        return authenticateService.login(userDTO)
                .map(refreshToken -> new ResponseEntity<>(refreshToken.getToken(),HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>("null",HttpStatus.NOT_FOUND));
    }

    @PostMapping("/refresh/login")
    public Mono<ResponseEntity<User>> refreshLogin(@RequestBody AuthRequest authRequest){
        return authenticateService.login(authRequest)
                .map(user -> ResponseEntity.accepted().body(user))
                .defaultIfEmpty(new ResponseEntity<>(null,HttpStatus.FORBIDDEN));
    }

    @DeleteMapping("/refresh/delete/{refreshToken}")
    public Mono<ResponseEntity<?>> deleteRefreshToken(@PathVariable(name = "refreshToken") String refreshToken){
        return authenticateService.delete(refreshToken)
                .map(voidClass -> new ResponseEntity<>(true,HttpStatus.OK));
    }

    @PostMapping("/user/save")
    public Mono<ResponseEntity<?>> saveUser(@RequestBody UserDTO userDTO){
        return userService.save(userDTO)
                .flatMap(user -> authenticateService.generateRefreshToken(userDTO)
                        .map(refreshToken -> new ResponseEntity<>(refreshToken.getToken(), HttpStatus.CREATED)));
    }

    @GetMapping("/user/check/{username}")
    public Mono<Boolean> checkUser(@PathVariable(name = "username") String username){
        return userService.existByUsername(username);
    }
}
