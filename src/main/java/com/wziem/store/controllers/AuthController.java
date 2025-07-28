package com.wziem.store.controllers;


import com.wziem.store.dtos.LoginRequest;
import com.wziem.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @RequestMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);

        var hashedPassword = passwordEncoder.encode(request.getPassword());

        if (user == null || !( passwordEncoder.matches(request.getPassword(), user.getPassword()))) {
            System.out.println("new hash:" + hashedPassword);
            System.out.println("db hash:" + user.getPassword());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }


}
