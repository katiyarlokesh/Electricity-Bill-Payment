package com.ebp.controller;

import com.ebp.authorizations.accessedData;
import com.ebp.authorizations.changePassword;
import com.ebp.dataTransfer.userClone;
import com.ebp.email.emailService;
import com.ebp.entities.AccessHistory;
import com.ebp.helper.ebpResponse;
import com.ebp.repository.accessRepository;
import com.ebp.security.JWTTokenHelper;
import com.ebp.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class userController
 * @Project Electricity Bill Payment
 */

@RestController
@RequestMapping("/ebp")
public class userController {

    @Autowired
    private userService userService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private accessedData accessedData;

    /**
     * PostMapping || Unauthorized || Open
     * URI -> http://localhost:8080/ebp/user/open/register/admin
     * New Admin Registration
     */
    @PostMapping("/user/open/register/admin")
    public ResponseEntity<userClone> newAdmin(@Valid @RequestBody userClone userClone) throws MessagingException {
        userClone savedUser = this.userService.registerAdmin(userClone);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * PostMapping || Unauthorized || Open
     * URI -> http://localhost:8080/ebp/user/open/register/customer
     * New Customer Registration
     */
    @PostMapping("/user/open/register/customer")
    public ResponseEntity<userClone> newCustomer(@Valid @RequestBody userClone userClone) throws MessagingException {
        userClone savedUser = this.userService.registerCustomer(userClone);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/user/search/username/{username}
     * User by Username
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/search/{username}")
    public ResponseEntity<List<userClone>> byUsername(@PathVariable String username, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String userName = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(userName, "Search user by Username" + username);
        List<userClone> users = this.userService.userByUsername(username);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/user/{userId}
     * User by User Id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<userClone> getUserById(@PathVariable Long userId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search User by User Id" + userId);
        userClone user = this.userService.userById(userId);
        return ResponseEntity.ok(user);
    }

    //Change Required
    @GetMapping("user/email/{userId}")
    public ResponseEntity<ebpResponse> mailDetails(@PathVariable Long userId) throws MessagingException {
        this.userService.emailDetails(userId);
        return new ResponseEntity<>(new ebpResponse("Email Sent", true), HttpStatus.OK);
    }

    /**
     * PostMapping || Authorized || All
     * URI -> http://localhost:8080/ebp/user/change-password
     * Change Password
     */
    @PostMapping("user/changePassword")
    public ResponseEntity<ebpResponse> changePassword(@RequestBody changePassword changePassword, HttpServletRequest request) throws MessagingException {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Change password Request raised");
        this.userService.changePassword(changePassword, username);
        return new ResponseEntity<>(new ebpResponse("Password changed successfully, please check your registered mail for details.", true), HttpStatus.ACCEPTED);
    }
}
