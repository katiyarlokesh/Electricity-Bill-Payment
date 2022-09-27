package com.ebp.controller;

import com.ebp.authorizations.accessedData;
import com.ebp.dataTransfer.customerClone;
import com.ebp.entities.AccessHistory;
import com.ebp.helper.customerResponse;
import com.ebp.helper.ebpResponse;
import com.ebp.repository.accessRepository;
import com.ebp.security.JWTTokenHelper;
import com.ebp.service.customerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;



@RestController
@RequestMapping("/ebp")
public class customerController {

    @Autowired
    private customerService customerService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private accessedData accessedData;

    /**
     * PostMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/user/fill-details
     * Fill fresh details in User
     */
    @PostMapping("/user/fill-details")
    public ResponseEntity<customerClone> fillDetails(@Valid @RequestBody customerClone customerClone, HttpServletRequest request) throws MessagingException {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Fill Details in User Data.");
        customerClone createdCustomer = this.customerService.createCustomer(username, customerClone);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * GetMapping || Authorized || ADMIN Role
     * URI -> http://localhost:8080/ebp/user/all
     * Get all the customers
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/all")
    public ResponseEntity<customerResponse> getAllCustomer(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                           @RequestParam(value = "pageSize", defaultValue = "5",  required = false) Integer pageSize,
                                                           HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Getting details of all the users.");
        customerResponse allCustomer = this.customerService.getAllCustomer(pageNumber, pageSize);
        return ResponseEntity.ok(allCustomer);
    }

    /**
     * GetMapping || Authorized || ADMIN Role
     * URI -> http://localhost:8080/ebp/customer/1
     * Get user by Id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<customerClone> getCustomerById(@PathVariable Long customerId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search single user by Id");
            customerClone customerById = this.customerService.getCustomerById(customerId);
            return new ResponseEntity<>(customerById, HttpStatus.OK);
    }

    /**
     * PutMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/user/1/fill-details
     * Edit details of user
     */
    @PutMapping("user/edit-details/")
    public ResponseEntity<customerClone> editCustomer(@Valid @RequestBody customerClone customerClone, HttpServletRequest request) throws MessagingException {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Edit detail of customer with username " + customerClone.getEmail());
        customerClone editedCustomer = this.customerService.editCustomer(username, customerClone);
        return new ResponseEntity<>(editedCustomer, HttpStatus.OK);
    }

    /**
     * DeleteMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/user/authorized/delete-user/1
     * Delete particular user
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/authorized/delete-user/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Delete Particular User");
        this.customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(new ebpResponse("Deleted Successfully", true), HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/user/search/email/{email}
     * Search user by email
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/search/email/{email}")
    public ResponseEntity<List<customerClone>> getByEmail(@PathVariable String email, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Getting user by Email with input : " + email);
        List<customerClone> byEmail = this.customerService.getByEmail(email);
        return new ResponseEntity<>(byEmail, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/user/search/aadhar/{aadhar}
     * Search user by aadhar
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/search/aadhar/{aadhar}")
    public ResponseEntity<List<customerClone>> getByAadhar(@PathVariable String aadhar, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Getting user by aadhar with input : " + aadhar);
        List<customerClone> byAadhar = this.customerService.getByAadhar(aadhar);
        return new ResponseEntity<>(byAadhar, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/user/search/name/{name}
     * Search user by name
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/search/name/{name}")
    public ResponseEntity<List<customerClone>> getByName(@PathVariable String name, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Getting user by name with input : " + name);
        List<customerClone> byName = this.customerService.getByName(name);
        return new ResponseEntity<>(byName, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/user/search/mobile/{mobile}
     * Search user by mobile
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/search/mobile/{mobile}")
    public ResponseEntity<List<customerClone>> getByMobile(@PathVariable String mobile, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Getting user by Mobile with input : " + mobile);
        List<customerClone> byMobile = this.customerService.getByMobile(mobile);
        return new ResponseEntity<>(byMobile, HttpStatus.OK);
    }
}
