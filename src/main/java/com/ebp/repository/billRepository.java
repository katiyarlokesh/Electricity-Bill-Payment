package com.ebp.repository;

import com.ebp.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @Author rohit.parihar 9/6/2022
 * @Class billRepository
 * @Project Electricity Bill Payment
 */
public interface billRepository extends JpaRepository<Bill, Long> {
    List<Bill> findBillByReading_Connection_ConsumerNo(String consumerNo);
    List<Bill> findBillByReading_Connection_Customer_MobileNumber(String mobileNo);
    List<Bill> findBillByReading_Connection_Customer_Email(String mail);
    List<Bill> findBillsByBillDateBetween(LocalDate from,LocalDate to);

}