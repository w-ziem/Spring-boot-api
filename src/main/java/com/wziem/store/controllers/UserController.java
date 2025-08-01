package com.wziem.store.controllers;


import com.wziem.store.dtos.ChangePasswordRequest;
import com.wziem.store.dtos.RegisterUserRequest;
import com.wziem.store.dtos.UpdateUserRequest;
import com.wziem.store.dtos.UserDto;
import com.wziem.store.entities.Role;
import com.wziem.store.mappers.UserMapper;
import com.wziem.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/users") //for every endpoint starting with /users
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping()
    //method: GET
    public Iterable<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) {
//        @RequestHeader(name = "x-auth-token") String authToken
//        System.out.println(authToken);
        //instead of throwing 500 status error, when given wrong argument, we check it and set sroting by name as default
        if (!Set.of("name", "email").contains(sortBy)) sortBy = "name";

        return userRepository.findAll(Sort.by(sortBy)).stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    // MethodArgumentNotValidException
    public ResponseEntity<?> registerUser(UriComponentsBuilder uriBuilder, @Valid @RequestBody RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(Map.of("email", "Email is already registered"));
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id, @RequestBody UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        userMapper.update(request, user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
