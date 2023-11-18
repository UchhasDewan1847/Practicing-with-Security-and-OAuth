package com.example.agent47.Springsecurityclient.event;

import com.example.agent47.Springsecurityclient.entity.User;
import com.example.agent47.Springsecurityclient.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Slf4j
@Component// I am not sure for this either
//@Service// I am not sure how to deal with its absence


public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private UserServiceImpl userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the verification token for the User with Link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(user,token);

        //Send Mail to User
        String url = event.getApplicationUrl()
                +"/verifyResitration?token="
                +token;
        //Send Verification Email
        log.info("Click the link to verify your account : {}",url);
    }
}
