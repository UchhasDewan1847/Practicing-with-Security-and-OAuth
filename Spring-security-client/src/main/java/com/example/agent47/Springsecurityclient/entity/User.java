package com.example.agent47.Springsecurityclient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String firstName;
    private String lastName;
    @Column(length = 60)
    private String password;
    private String email;
    private String role;
    private Boolean isEnabled = false;

    public User(String firstName, String lastName, String password, String email, String role) {
        this.firstName=firstName;
        this.lastName =lastName;
        this.email=email;
        this.password = password;
        this.role =role;
    }
}
