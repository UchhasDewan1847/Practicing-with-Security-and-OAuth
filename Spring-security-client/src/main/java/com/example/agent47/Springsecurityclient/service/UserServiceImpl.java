package com.example.agent47.Springsecurityclient.service;

import com.example.agent47.Springsecurityclient.entity.User;
import com.example.agent47.Springsecurityclient.entity.VerificationToken;
import com.example.agent47.Springsecurityclient.model.UserModel;
import com.example.agent47.Springsecurityclient.repository.UserRepository;
import com.example.agent47.Springsecurityclient.repository.VerficationTokenRerpository;
import com.example.agent47.Springsecurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerficationTokenRerpository verficationTokenRerpository;

    @Override
    public User registerUser(UserModel userModel) {
        var user =userRepository.save(new User(
                userModel.getFirstName(),
                userModel.getLastName(),
                passwordEncoder.encode(userModel.getPassword()),
                userModel.getEmail(),
                "User"
                )
        );
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token,user);
        verficationTokenRerpository.save(verificationToken);
    }
}
