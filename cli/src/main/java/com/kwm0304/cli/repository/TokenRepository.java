package com.kwm0304.cli.repository;

import com.kwm0304.cli.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("""
        select t from Token t inner join Customer u on t.customer.id = u.id
        where t.customer.id = :customerId and t.loggedOut = false
        """)
    List<Token> findAllTokensByUser(Long customerId);

    Optional<Token> findByToken(String token);
}