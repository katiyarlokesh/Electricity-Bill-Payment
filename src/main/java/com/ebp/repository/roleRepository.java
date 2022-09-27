package com.ebp.repository;

import com.ebp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class rolerepository
 * @Project Electricity Bill Payment
 */
public interface roleRepository extends JpaRepository<Role, Integer> {
}
