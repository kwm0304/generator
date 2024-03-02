package com.kwm0304.cli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.kwm0304.cli.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
}