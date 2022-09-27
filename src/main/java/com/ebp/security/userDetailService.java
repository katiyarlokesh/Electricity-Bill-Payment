package com.ebp.security;

import com.ebp.entities.User;
import com.ebp.exceptionHandler.detailsNotAvailableException;
import com.ebp.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @Author rohit.parihar 9/17/2022
 * @Class userDetailService
 * @Project Electricity Bill Payment
 */

@Component
public class userDetailService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new detailsNotAvailableException("User", "Username", username));
        return user;
    }
}
