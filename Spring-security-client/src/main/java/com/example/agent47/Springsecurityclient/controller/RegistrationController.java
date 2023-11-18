package com.example.agent47.Springsecurityclient.controller;

import com.example.agent47.Springsecurityclient.entity.User;
import com.example.agent47.Springsecurityclient.entity.VerificationToken;
import com.example.agent47.Springsecurityclient.event.RegistrationCompleteEvent;
import com.example.agent47.Springsecurityclient.model.PasswordModel;
import com.example.agent47.Springsecurityclient.model.UserModel;
import com.example.agent47.Springsecurityclient.repository.PasswordResetTokenRepository;
import com.example.agent47.Springsecurityclient.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegistrationController {
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        var user =userService.registerUser(userModel);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return ResponseEntity.ok("Successful Registration");
    }
    @GetMapping("/verifyRegistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token){
        String result =userService.verifyRegistration(token);
        if(result.equalsIgnoreCase("valid")){
            return ResponseEntity.ok("Verification Successful");
        }
        else {
            return ResponseEntity.ok(result);
        }

    }
    @GetMapping("/resendVerifyToken")
    public ResponseEntity<String> resendVerificationToken(@RequestParam("token") String token, final HttpServletRequest request){
        VerificationToken verificationToken =
                userService.generateNewVerificationToken(token);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user,applicationUrl(request),verificationToken);
        return ResponseEntity.ok("Verification Link Sent");

    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest httpServletRequest){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url ="";
        if(user!= null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user,token);
            url = passwordResetTokenMail(user,applicationUrl(httpServletRequest),token);
        }
        return ResponseEntity.ok(url);
    }
    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,@RequestBody PasswordModel passwordModel){
        String result =userService.validatePasswordResetToken(token);
        if (! result.equalsIgnoreCase("valid")){
            return "invalid password";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()){
            userService.changePassword(user.get(),passwordModel.getNewPassword());
            return "password reset successful";
        }
        else {
            return "invalid token";
        }
    }
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordModel passwordModel){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if (!userService.checkIfValidOldPassword(user,passwordModel.getOldPassword())){
            return ResponseEntity.ok("Invalid Password");
        }
        userService.changePassword(user,passwordModel.getNewPassword());
        //save password operation executed
        return ResponseEntity.ok("Password Changed Successfully");
    }

    private String passwordResetTokenMail(User user, String s, String token) {
        String url = s
                +"/savePassword?token="
                +token;
        //Send Verification Email
        log.info("Click the link to reset your password : {}",url);
        return url;
    }

    private void resendVerificationTokenMail(User user, String s, VerificationToken verificationToken) {
        String url = s
                +"/verifyRegistration?token="
                +verificationToken.getToken();
        //Send Verification Email

        log.info("Click the link to verify your account : {}",url);
    }
    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName()+
                ":"+
                request.getServerPort()+
                request.getContextPath();
    }

}
