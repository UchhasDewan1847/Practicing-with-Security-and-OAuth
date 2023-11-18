package com.example.agent47.Springsecurityclient.service;

import com.example.agent47.Springsecurityclient.entity.PasswordResetToken;
import com.example.agent47.Springsecurityclient.entity.User;
import com.example.agent47.Springsecurityclient.entity.VerificationToken;
import com.example.agent47.Springsecurityclient.model.UserModel;
import com.example.agent47.Springsecurityclient.repository.PasswordResetTokenRepository;
import com.example.agent47.Springsecurityclient.repository.UserRepository;
import com.example.agent47.Springsecurityclient.repository.VerficationTokenRerpository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerficationTokenRerpository verficationTokenRerpository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

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

    @Override
    public String verifyRegistration(String token) {
        VerificationToken verificationToken
                =verficationTokenRerpository.findByToken(token);
        if(verificationToken==null){
            return "invalid";
        }
        User user = verificationToken.getUser();
        Calendar calendar= Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime()-calendar.getTime().getTime())<=0){
            verficationTokenRerpository.delete(verificationToken);
            return "expired";
        }
        user.setIsEnabled(true);
        userRepository.save(user);

        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken=verficationTokenRerpository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(new Date(System.currentTimeMillis()+1000*60*10));
        verficationTokenRerpository.save(verificationToken);
        System.out.println(verificationToken);
        return verificationToken;

    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(
                token,
                user
        );
        passwordResetTokenRepository.save(passwordResetToken);

    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken
                =passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken ==null){
            return "invalid";
        }
        User user = passwordResetToken.getUser();
        Calendar calendar= Calendar.getInstance();
        if ((passwordResetToken.getExpirationTime().getTime()-calendar.getTime().getTime())<=0){
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}
