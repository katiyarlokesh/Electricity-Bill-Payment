package com.ebp.email;

import javax.mail.MessagingException;

/**
 * @Author rohit.parihar 9/15/2022
 * @Class emailService
 * @Project Electricity Bill Payment
 */

public interface emailService {
    Boolean sendEmail(String subject, String content, String to) throws MessagingException;
}
