package org.example.service;


import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefershTokenService {
    @Autowired RefreshTokenRepository refreshTokenRepository;

    @Autowired UserRepository userRepository;

    //Create Refresh Token
    public RefreshToken createRefreshToken(String username){
        UserInfo userInfoExtracted=userRepository.findByUsername(username);
        RefreshToken refreshToken=RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    //Verify Refresh Token
    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken()+" Refresh token is expired. Please make a new Login..!");
        }
        return token;
    }

    //Find token by name
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

}
