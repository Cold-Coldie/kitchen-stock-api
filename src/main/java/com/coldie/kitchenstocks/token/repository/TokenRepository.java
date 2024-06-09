package com.coldie.kitchenstocks.token.repository;

import com.coldie.kitchenstocks.token.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Query(""" 
            SELECT t from Token t INNER JOIN User u on t.user.id = u.id WHERE u.id = :userId AND (t.expired = false or t.revoked = false)
            """)
        // List<Token> findAllByUser_IdAndExpiredFalseAndRevokedFalse(Long userId);
    List<Token> findAllValidTokensByUser(Long userId);
}
