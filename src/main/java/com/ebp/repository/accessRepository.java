package com.ebp.repository;

import com.ebp.entities.AccessHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author rohit.parihar 9/19/2022
 * @Class accessRepository
 * @Project Electricity Bill Payment
 */

public interface accessRepository extends JpaRepository<AccessHistory, Long> {
}
