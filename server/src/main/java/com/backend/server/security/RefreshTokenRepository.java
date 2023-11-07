package com.backend.server.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.users.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // poista Userin perusteella
    void deleteByUser(User user);

    // hae Tokenilla
    Optional<RefreshToken> findByToken(String token);

   

    // poista Tokenilla
    void deleteByToken(String token);

    
    // poista useridllä
    void deleteByUserId(Long userId);



    
}
