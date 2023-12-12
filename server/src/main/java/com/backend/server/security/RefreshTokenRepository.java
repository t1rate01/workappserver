package com.backend.server.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.users.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // poista Userin perusteella
    void deleteRefreshTokenByUser(User user);

    // hae Tokenilla
    Optional<RefreshToken> findByToken(String token);

    // etsi userin perusteella
    Optional<RefreshToken> findByUser(User user);

    // etsii kaikki userin perusteella
    Optional<List<RefreshToken>> findAllByUser(User user);

   

    // poista Tokenilla
    void deleteByToken(String token);

    
    // poista useridllä
    void deleteByUserId(Long userId);



    
}
