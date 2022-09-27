package com.ebp.repository;

import com.ebp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

public interface customerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String username);
    List<Customer> findCustomerByEmailContainingIgnoreCase(String email);
    List<Customer> findCustomerByAadharNumberContainingIgnoreCase(String aadhar);
    List<Customer> findCustomerByMobileNumberContainingIgnoreCase(String mobile);
    List<Customer> findCustomerByNameContainingIgnoreCase(String name);
}
