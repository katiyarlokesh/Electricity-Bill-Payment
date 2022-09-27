package com.ebp.service;

import com.ebp.dataTransfer.customerClone;
import com.ebp.helper.customerResponse;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

public interface customerService {
    customerClone createCustomer(String username, customerClone customerClone) throws MessagingException;
    customerClone getCustomerById(Long customerId);
    customerResponse getAllCustomer(Integer pageNumber, Integer pageSize);
    customerClone editCustomer(String username, customerClone customerClone) throws MessagingException;
    void deleteCustomer(Long customerId);
    List<customerClone> getByEmail(String email);
    List<customerClone> getByName(String name);
    List<customerClone> getByAadhar(String aadhar);
    List<customerClone> getByMobile(String mobile);
}
