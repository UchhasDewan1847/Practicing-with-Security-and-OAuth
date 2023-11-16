package com.example.agent47.Springsecurityclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HelloController {
    @GetMapping("/hello")
    public String helloWorld(){
        return "<h1>Hello World</h2>";
    }
}
