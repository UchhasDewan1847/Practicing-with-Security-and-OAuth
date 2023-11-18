package com.example.agent47.Springsecurityclient.service;

import com.example.agent47.Springsecurityclient.entity.User;
import com.example.agent47.Springsecurityclient.entity.VerificationToken;
import com.example.agent47.Springsecurityclient.model.UserModel;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {
    public User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);

    String verifyRegistration(String token);

    VerificationToken generateNewVerificationToken(String token);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}
