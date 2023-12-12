package com.backend.server.security;

import java.util.Optional;


import org.springframework.stereotype.Service;

import com.backend.server.users.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

   /*  public RefreshToken getRefreshTokenByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }*/

    public Optional<RefreshToken> getRefreshTokenByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public void deleteRefreshTokenByUser(User user) {
        refreshTokenRepository.deleteRefreshTokenByUser(user);
    }






    
}
