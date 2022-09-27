package com.ebp.service;

import com.ebp.dataTransfer.paymentClone;
import com.ebp.helper.paymentResponse;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @Author rohit.parihar 9/8/2022
 * @Class paymentService
 * @Project Electricity Bill Payment
 */
public interface paymentService {
    paymentClone payBill(paymentClone paymentClone, Long billId, String username) throws MessagingException;
    paymentResponse allPayments(Integer pageNumber, Integer pageSize);
    void emailOnCompletion(Long paymentId) throws MessagingException;
    List<paymentClone> historicalPayment(String consumerNo);
    List<paymentClone> myPayments(String username);
}
