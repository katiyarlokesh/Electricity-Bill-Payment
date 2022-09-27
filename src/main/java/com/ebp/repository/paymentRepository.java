package com.ebp.repository;

import com.ebp.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author rohit.parihar 9/8/2022
 * @Class paymentRepository
 * @Project Electricity Bill Payment
 */
public interface paymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findPaymentsByBill_Reading_Connection_ConsumerNo(String consumerNo);
    List<Payment> findPaymentsByBill_Reading_Connection_Customer_Email(String username);
}
