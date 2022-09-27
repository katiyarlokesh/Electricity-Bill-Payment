package com.ebp.repository;

import com.ebp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class userRepository
 * @Project Electricity Bill Payment
 */
public interface userRepository extends JpaRepository<User, Long> {
    List<User> findUsersByUsernameContainingIgnoreCase(String username);
    Optional<User> findByUsername(String username);
}
