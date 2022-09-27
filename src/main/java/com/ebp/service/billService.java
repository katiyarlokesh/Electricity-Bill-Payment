package com.ebp.service;

import com.ebp.dataTransfer.billClone;
import com.ebp.entities.Calculate;
import com.ebp.entities.connectionType;
import com.ebp.helper.billCalculator;
import com.ebp.helper.billResponse;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author rohit.parihar 9/6/2022
 * @Class billService
 * @Project Electricity Bill Payment
 */
public interface billService {
    billClone selfSubmitReading(billClone billClone, Long readingId, String username) throws MessagingException;
    billClone getById(Long billId);
    billResponse allBills(Integer pageNumber, Integer pageSize);
    billClone extendDueDate(Long billId);
    void deleteBill(Long billId);
    List<billClone> byConsumerNo(String consumerNo);
    List<billClone> byMobileNo(String mobile);
    List<billClone> byEmail(String email);
    List<billClone> byDateRange(String from, String to);
    Integer calculateData(Calculate calculate);
    void billToCustomer(Long billId) throws MessagingException;
    List<billClone> allBills(String username);
}
