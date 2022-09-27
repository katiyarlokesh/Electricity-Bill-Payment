package com.ebp.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Author rohit.parihar 9/15/2022
 * @Class emailImplementation
 * @Project Electricity Bill Payment
 */

@Service
public class emailImplementation implements emailService{

    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public Boolean sendEmail(String subject, String content, String to) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(mimeMessage);
        return true;
    }
}
