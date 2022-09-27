package com.ebp.controller;

import com.ebp.authorizations.forgotPassword;
import com.ebp.entities.AccessHistory;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.helper.ebpResponse;
import com.ebp.helper.tokenRequest;
import com.ebp.helper.tokenResponse;
import com.ebp.repository.accessRepository;
import com.ebp.security.JWTTokenHelper;
import com.ebp.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/17/2022
 * @Class authorizationController
 * @Project Electricity Bill Payment
 */

@RestController
@RequestMapping("ebp/user/open")
public class authorizationController {

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private userService userService;

    @Autowired
    private accessRepository accessRepository;

    /**
     * PostMapping || Unauthorized || Open
     * URI -> http://localhost:8080/ebp/user/open/login
     * User Login
     */
    @PostMapping("/login")
    public ResponseEntity<tokenResponse> createToken(@RequestBody tokenRequest tokenRequest){
        this.authenticate(tokenRequest.getUsername(), tokenRequest.getPassword());
        UserDetails details = this.userDetailsService.loadUserByUsername(tokenRequest.getUsername());
        String token = this.jwtTokenHelper.generateToken(details);
        tokenResponse tokenResponse = new tokenResponse();
        tokenResponse.setToken(token);
        return new ResponseEntity<>(tokenResponse, HttpStatus.CREATED);
    }

    /**
     * Authenticate the entered Username and Password
     * Throws Customized Authorization Exception
     * Calls authentication manager to authenticate
     */
    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }
        catch (Exception e){
            throw new authorizationException("Check your Password");
        }
    }

    /**
     * PostMapping || Unauthorized || Open
     * URI -> http://localhost:8080/ebp/user/open/forgot-password
     * Forgot-Password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ebpResponse> forgotPassword(@RequestBody forgotPassword forgotPassword) throws MessagingException {
        this.userService.forgotPassword(forgotPassword);
        return new ResponseEntity<>(new ebpResponse("We've sent a new Password at your Email. Please change your Password immediately", true), HttpStatus.ACCEPTED);
    }
}
