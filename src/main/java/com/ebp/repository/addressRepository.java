package com.ebp.repository;

import com.ebp.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

public interface addressRepository extends JpaRepository<Address, Long> {
//    List<Address> findByVillageContainingIgnoreCase(String village);
}
