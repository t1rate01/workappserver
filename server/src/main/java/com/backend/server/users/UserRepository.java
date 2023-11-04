package com.backend.server.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // hae sähköpostilla
    Optional <User> findByEmail(String email);

} 