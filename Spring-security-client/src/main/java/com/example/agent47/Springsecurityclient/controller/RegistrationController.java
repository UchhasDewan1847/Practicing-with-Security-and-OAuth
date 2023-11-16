package com.example.agent47.Springsecurityclient.controller;

import com.example.agent47.Springsecurityclient.event.RegistrationCompleteEvent;
import com.example.agent47.Springsecurityclient.model.UserModel;
import com.example.agent47.Springsecurityclient.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final ApplicationEventPublisher applicationEventPublisher;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        var user =userService.registerUser(userModel);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return ResponseEntity.ok("Successful Registration");
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName()+
                ":"+
                request.getServerPort()+
                request.getContextPath();
    }
}
