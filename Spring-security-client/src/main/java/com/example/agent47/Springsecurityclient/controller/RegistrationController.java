package com.example.agent47.Springsecurityclient.controller;

import com.example.agent47.Springsecurityclient.model.UserModel;
import com.example.agent47.Springsecurityclient.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    @Autowired
    private final UserServiceImpl userService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserModel userModel){
        return ResponseEntity.ok(userService.registerUser(userModel));
    }
}
