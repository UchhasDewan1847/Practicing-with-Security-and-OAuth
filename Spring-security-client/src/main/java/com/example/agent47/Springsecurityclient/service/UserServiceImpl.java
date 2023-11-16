package com.example.agent47.Springsecurityclient.service;

import com.example.agent47.Springsecurityclient.entity.User;
import com.example.agent47.Springsecurityclient.model.UserModel;
import com.example.agent47.Springsecurityclient.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserModel userModel) {
        var user =userRepository.save(new User(
                userModel.getFirstName(),
                userModel.getLastName(),
                passwordEncoder.encode(userModel.getPassword()),
                userModel.getEmail(),
                "User"
                )
        );
        if(user!=null){
            return "Successful Registraion";
        }
        else{
            return "Unsuccessful Registration";
        }
    }
}
