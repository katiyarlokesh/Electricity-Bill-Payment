package com.ebp.implementation;

import com.ebp.authorizations.changePassword;
import com.ebp.authorizations.forgotPassword;
import com.ebp.dataTransfer.userClone;
import com.ebp.email.emailService;
import com.ebp.entities.Role;
import com.ebp.entities.User;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.exceptionHandler.detailsNotAvailableException;
import com.ebp.repository.roleRepository;
import com.ebp.repository.userRepository;
import com.ebp.service.userService;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class userImplementation
 * @Project Electricity Bill Payment
 */

@Service
public class userImplementation implements userService {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private roleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private emailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public userClone registerAdmin(userClone userClone) throws MessagingException {
        User user = this.modelMapper.map(userClone, User.class);
        Role role = this.roleRepository.findById(106).get();
        user.getRoles().add(role);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        String subject = "New Registration on EBP";
        String email = user.getUsername();
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<p>Welcome</p>" +
                "<p>Account created at : " + LocalDate.now() + " </p>" +
                "<h4>Please find your registered Username for Admin Login</h4>" +
                "<h5>User Id : " + userClone.getUserId() + "</h5>" +
                "<h5>Username : " + userClone.getUsername() + "</h5>" +
                "<h5>Password : " + userClone.getPassword() + "</h5>" +
                "<h5>Please complete your Profile</h5>";
        this.emailService.sendEmail(subject, content,email);
        User savedUser = this.userRepository.save(user);
        return this.modelMapper.map(savedUser, userClone.class);
    }

    @Override
    public userClone registerCustomer(userClone userClone) throws MessagingException {
        User user = this.modelMapper.map(userClone, User.class);
        Role role = this.roleRepository.findById(205).get();
        user.getRoles().add(role);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        String subject = "New Registration on EBP";
        String email = user.getUsername();
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<p>Welcome to EBP</p>" +
                "<p>Account created at : " + LocalDate.now() + " </p>" +
                "<h4>Please find your registered Username</h4>" +
                "<h5>User Id : " + user.getUserId() + "</h5>" +
                "<h5>Username : " + user.getUsername() + "</h5>" +
                "<h5>Password : " + userClone.getPassword() + "</h5>" +
                "<h5>Password is generated at the time of Registration.</h5>";
        this.emailService.sendEmail(subject, content,email);
        User savedUser = this.userRepository.save(user);
        return this.modelMapper.map(savedUser, userClone.class);
    }

    @Override
    public List<userClone> userByUsername(String username) {
        List<User> users = this.userRepository.findUsersByUsernameContainingIgnoreCase(username);
        if(users.isEmpty()){
            throw new detailsNotAvailableException("Data");
        }
        List<userClone> collect = users.stream().map(e -> this.modelMapper.map(e, userClone.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public userClone userById(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new detailsNotAvailableException("User", "User Id", userId));
        return this.modelMapper.map(user, userClone.class);
    }

    @Override
    public void emailDetails(Long userId) throws MessagingException {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new detailsNotAvailableException("User", "User Id", userId));
        String subject = "Authorities Details EBP";
        String email = user.getUsername();
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<p>Hello " + " ,</p>" +
                "<h4>Please find your registered Username</h4>" +
                "<h5>Username : " + user.getUsername() + "</h5>" +
                "<h5>Password is generated at the time of Registration.</h5>";
        this.emailService.sendEmail(subject, content, email);
    }

    @Override
    public void changePassword(changePassword changePassword, String username) throws MessagingException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new detailsNotAvailableException("User", "Username", username));
        String encodedPassword = user.getPassword();
        String oldPassword = changePassword.getOldPassword();
        boolean matches = this.passwordEncoder.matches(oldPassword, encodedPassword);
        System.out.println(matches);
        if (matches){
            user.setPassword(this.passwordEncoder.encode(changePassword.getNewPassword()));
            this.userRepository.save(user);
        }
        else {
            throw new authorizationException("Entered Password is incorrect. Please enter correct password or forgot password.");
        }
        String subject = "Password Change request";
        String email = user.getUsername();
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<p>Hello " + " ,</p>" +
                "<h4>Password change request raised by you at " + LocalDateTime.now() + " </h4>" +
                "<h4>Please find your registered Username and password here.</h4>" +
                "<h5>Username : " + user.getUsername() + "</h5>" +
                "<h5>Password : " + changePassword.getNewPassword() + "</h5>" +
                "<h5>Don't share your password with anyone.</h5>";
        this.emailService.sendEmail(subject, content, email);
    }

    @Override
    public void forgotPassword(forgotPassword forgotPassword) throws MessagingException {
        String username = forgotPassword.getUsername();
        Long userId = forgotPassword.getUserId();
        User userWithUsername = this.userRepository.findByUsername(username).orElseThrow(() -> new detailsNotAvailableException("User", "Username", username));
        User userWithUserId = this.userRepository.findById(userId).orElseThrow(() -> new detailsNotAvailableException("User", "User Id", userId));
        String newPassword;
        if (userWithUserId.getUserId().equals(userWithUsername.getUserId()) && userWithUserId.getUsername().equals(userWithUsername.getUsername())) {
            RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange(0, (int) 'z').filteredBy(CharacterPredicates.ASCII_ALPHA_NUMERALS).build();
            newPassword = generator.generate(8);
            userWithUsername.setPassword(this.passwordEncoder.encode(newPassword));
            this.userRepository.save(userWithUsername);
        } else {
            throw new authorizationException("User Id and Username not match");
        }
        String subject = "Forgot Password request by ";
        String email = userWithUsername.getUsername();
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<p>Hello " + " ,</p>" +
                "<h4>We got your forgot password request at " + LocalDate.now() + " </h4>" +
                "<h4>Please find your registered Username and password here.</h4>" +
                "<h5>Username : " + userWithUsername.getUsername() + "</h5>" +
                "<h5>Password : " + newPassword + "</h5>" +
                "<h5>Please change your Password immediately and Don't share your password with anyone.</h5>";
        this.emailService.sendEmail(subject, content, email);
    }
}
