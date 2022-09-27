package com.ebp.repository;

import com.ebp.entities.Address;
import com.ebp.entities.Connection;
import com.ebp.entities.Customer;
import org.hibernate.internal.log.ConnectionAccessLogger_$logger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

public interface connectionRepository extends JpaRepository<Connection, Long> {
   List<Connection> findConnectionsByAddress_VillageAndConnStatusTrue(String village);
   List<Connection> findConnectionsByAddress_VillageAndConnStatusFalse(String village);
   List<Connection> findConnectionsByAddress_TalukaAndConnStatusTrue(String taluka);
   List<Connection> findConnectionsByAddress_TalukaAndConnStatusFalse(String taluka);
   List<Connection> findConnectionsByAddress_DistrictAndConnStatusTrue(String district);
   List<Connection> findConnectionsByAddress_DistrictAndConnStatusFalse(String district);
   List<Connection> findConnectionsByAddress_StateAndConnStatusTrue(String state);
   List<Connection> findConnectionsByAddress_StateAndConnStatusFalse(String state);
   List<Connection> findConnectionsByAddress_PincodeAndConnStatusTrue(String pincode);
   List<Connection> findConnectionsByAddress_PincodeAndConnStatusFalse(String pincode);
   Connection findByCustomer(Customer customer);
   Connection findByConsumerNo(String consumerNo);
   Optional<Connection> findByCustomer_Email(String username);
}
