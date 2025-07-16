package com.wziem.store.controllers;


import com.wziem.store.dtos.UserDto;
import com.wziem.store.mappers.UserMapper;
import com.wziem.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/users") //for every endpoint starting with /users
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto data) {
        return data;
    }
}
