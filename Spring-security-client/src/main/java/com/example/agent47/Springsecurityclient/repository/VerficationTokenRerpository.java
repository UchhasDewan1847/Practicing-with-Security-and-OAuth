package com.example.agent47.Springsecurityclient.repository;

import com.example.agent47.Springsecurityclient.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerficationTokenRerpository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);
}
