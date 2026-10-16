package com.springboot.repository;

import com.springboot.model.BmcUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BmcUserRepository extends JpaRepository<BmcUser, Long> {
    Optional<BmcUser> findByEmail(String email);
}
