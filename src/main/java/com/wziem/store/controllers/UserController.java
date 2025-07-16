package com.wziem.store.controllers;


import com.wziem.store.dtos.UserDto;
import com.wziem.store.entities.User;
import com.wziem.store.mappers.UserMapper;
import com.wziem.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/users") //for every endpoint starting with /users
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping()
    //method: GET
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
