package com.ebp.service;

import com.ebp.authorizations.changePassword;
import com.ebp.authorizations.forgotPassword;
import com.ebp.dataTransfer.userClone;
import com.ebp.entities.User;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class userService
 * @Project Electricity Bill Payment
 */
public interface userService {
    userClone registerAdmin(userClone userClone) throws MessagingException;
    userClone registerCustomer(userClone userClone) throws MessagingException;
    List<userClone> userByUsername(String username);
    userClone userById(Long userId);
    void emailDetails(Long userId) throws MessagingException;
    void changePassword(changePassword changePassword, String username) throws MessagingException;
    void forgotPassword(forgotPassword forgotPassword) throws MessagingException;
}
