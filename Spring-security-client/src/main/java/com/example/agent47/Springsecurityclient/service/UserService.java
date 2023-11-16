package com.example.agent47.Springsecurityclient.service;

import com.example.agent47.Springsecurityclient.entity.User;
import com.example.agent47.Springsecurityclient.model.UserModel;
import org.springframework.stereotype.Service;


public interface UserService {
    public User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);
}
