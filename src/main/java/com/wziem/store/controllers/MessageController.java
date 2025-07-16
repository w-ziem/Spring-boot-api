package com.wziem.store.controllers;


import com.wziem.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/message")
    public Message sayHello() {
        return new Message("Hello from Spring Boot");
    }
}
