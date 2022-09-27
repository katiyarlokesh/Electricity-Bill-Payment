package com.ebp.implementation;

import com.ebp.dataTransfer.customerClone;
import com.ebp.email.emailService;
import com.ebp.entities.Customer;
import com.ebp.entities.User;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.exceptionHandler.detailsNotAvailableException;
import com.ebp.helper.customerResponse;
import com.ebp.repository.customerRepository;
import com.ebp.repository.userRepository;
import com.ebp.service.customerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class customerImplementation implements customerService {

    @Autowired
    private customerRepository customerRepository;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private emailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Implementation for creating new Customer
     * Calls userRepository and customerRepository
     * Throws Messaging Exception
     */
    @Override
    public customerClone createCustomer(String username, customerClone customerClone) throws MessagingException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new detailsNotAvailableException("User", "Username", username));
        Customer customer = this.modelMapper.map(customerClone, Customer.class);
        String name = customer.getFirstName() + " " + customer.getLastName();
            customer.setName(name);
            customer.setEmail(user.getUsername());
            customer.setDateCreated(LocalDate.now());
            customer.setUser(user);
            Customer newCustomer = this.customerRepository.save(customer);
        String subject = "Details successfully" + " EBP at " + LocalDate.now();
        String email = user.getUsername();
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<h4>Welcome, "+customer.getName()+"</h4>" +
                "<h4>You have been successfully registered</h4>" +
                "<h5>Please check your data carefully</h5>" +
                "<table>" +
                "<tr>" +
                "<td>Username</td>" +
                "<td>"+user.getUsername()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Customer Name</td>" +
                "<td>"+name+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Mobile</td>" +
                "<td>"+customerClone.getMobileNumber()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Email</td>" +
                "<td>"+user.getUsername()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Aadhar No.</td>" +
                "<td>"+customerClone.getAadharNumber()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Gender</td>" +
                "<td>"+customerClone.getGender()+"<td>" +
                "</tr>" +
                "</table>";
        this.emailService.sendEmail(subject, content, email);
            return this.modelMapper.map(newCustomer, customerClone.class);
    }

    /**
     * Implementation for getting customer by Id
     * Calls customerRepository
     * Throws Customised detailsNotAvailable Exception if Id is not present
     */
    @Override
    public customerClone getCustomerById(Long customerId) {
        Customer customer = this.customerRepository.findById(customerId).orElseThrow(() -> new detailsNotAvailableException("Customer", "Customer Id", customerId));
        return this.modelMapper.map(customer, customerClone.class);
    }

    /**
     * Implementation for getting all customer with appropriate Pagination
     * Calls customerRepository
     * Throws Customised detailsNotAvailable Exception if data in Empty
     */
    @Override
    public customerResponse getAllCustomer(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Customer> customerData = this.customerRepository.findAll(page);
        List<Customer> customerContent = customerData.getContent();
        List<customerClone> customerCloneData = customerContent.stream().map(e -> this.modelMapper.map(e, customerClone.class)).collect(Collectors.toList());
        customerResponse customerResponse = new customerResponse();
        customerResponse.setCustomerContent(customerCloneData);
        if(customerContent.isEmpty()){
            throw new detailsNotAvailableException("Customer Data");
        }
        customerResponse.setPageNumber(customerData.getNumber());
        customerResponse.setPageSize(customerData.getSize());
        customerResponse.setTotalPages(customerData.getTotalPages());
        customerResponse.setTotalCustomers(customerData.getTotalElements());
        customerResponse.setIsFirstPage(customerData.isFirst());
        customerResponse.setIsLastPage(customerData.isLast());
        return customerResponse;
    }

    /**
     * Implementation for update details of Customer
     * Calls customerRepository
     * Throws Customised detailsNotAvailable Exception if Customer Id is not present
     */
    @Override
    public customerClone editCustomer(String username, customerClone customerClone) throws MessagingException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new detailsNotAvailableException("User", "Username", username));
        Customer customer = this.customerRepository.findByEmail(username).orElseThrow(() -> new authorizationException("Please fill your details by the given link http://localhost:8080/ebp/user/fill-details"));
        customer.setFirstName(customerClone.getFirstName());
        customer.setLastName(customerClone.getLastName());
        customer.setMiddleName(customerClone.getMiddleName());
        String name = customerClone.getFirstName() + customerClone.getLastName();
        customer.setName(name);
        customer.setEmail(username);
        customer.setAadharNumber(customer.getAadharNumber());
        customer.setDateEdited(LocalDate.now());
        String subject = "Details successfully" + " EBP at " + LocalDate.now();
        String email = username;
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<h4>Welcome, "+name+"</h4>" +
                "<h4>You have been successfully registered</h4>" +
                "<h5>Please check your data carefully</h5>" +
                "<table>" +
                "<tr>" +
                "<td>Username</td>" +
                "<td>"+username+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Customer Name</td>" +
                "<td>"+name+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Mobile</td>" +
                "<td>"+customerClone.getMobileNumber()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Email</td>" +
                "<td>"+username+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Aadhar No.</td>" +
                "<td>"+customer.getAadharNumber()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Gender</td>" +
                "<td>"+customerClone.getGender()+"<td>" +
                "</tr>" +
                "</table>";
        this.emailService.sendEmail(subject, content, email);
        Customer editedCustomer = this.customerRepository.save(customer);
        return this.modelMapper.map(editedCustomer, customerClone.class);
    }

    /**
     * Implementation for deleting customer/user from Database
     * Calls customerRepository
     * Throws Customised detailsNotAvailable Exception
     */
    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = this.customerRepository.findById(customerId).orElseThrow(() -> new detailsNotAvailableException("Customer", "Customer Id", customerId));
        this.customerRepository.delete(customer);
    }

    /**
     * Implementation for getting customers/users by Email
     * Calls customerRepository
     * Throws Customised detailsNotAvailable Exception
     */
    @Override
    public List<customerClone> getByEmail(String email) {
        List<Customer> customer = this.customerRepository.findCustomerByEmailContainingIgnoreCase(email);
        if (customer.isEmpty()){
            throw new detailsNotAvailableException("Customer", "Email", email);
        }
        else {
            List<customerClone> listCustomerClone = customer.stream().map(e -> this.modelMapper.map(e, customerClone.class)).collect(Collectors.toList());
            return listCustomerClone;
        }
    }

    /**
     * Implementation for getting customers/users by their Name
     * Calls customerRepository
     * Throws Customised detailsNotAvailable Exception
     */
    @Override
    public List<customerClone> getByName(String name) {
        List<Customer> byName = this.customerRepository.findCustomerByNameContainingIgnoreCase(name);
        if (byName.isEmpty()){
            throw new detailsNotAvailableException("Customer", "Name", name);
        }
        else {
            List<customerClone> collect = byName.stream().map(e -> this.modelMapper.map(e, customerClone.class)).collect(Collectors.toList());
            return collect;
        }
    }

    /**
     * Implementation for getting customers/users by their Aadhar
     * Calls customerRepository
     * Throws Customised detailsNotAvailable Exception
     */
    @Override
    public List<customerClone> getByAadhar(String aadhar) {
        List<Customer> customer = this.customerRepository.findCustomerByAadharNumberContainingIgnoreCase(aadhar);
        if (customer.isEmpty()){
            throw new detailsNotAvailableException("Customer", "Aadhar", aadhar);
        }
        else {
            List<customerClone> listCustomerClone = customer.stream().map(e -> this.modelMapper.map(e, customerClone.class)).collect(Collectors.toList());
            return listCustomerClone;
        }
    }

    /**
     * Implementation for getting customers/users by their Mobile
     * Calls customerRepository
     * Throws Customised detailsNotAvailable Exception
     */
    @Override
    public List<customerClone> getByMobile(String mobile) {
        List<Customer> byMobile = this.customerRepository.findCustomerByMobileNumberContainingIgnoreCase(mobile);
        if (byMobile.isEmpty()){
            throw new detailsNotAvailableException("Customer", "Mobile", mobile);
        }
        else {
            return byMobile.stream().map(e->this.modelMapper.map(e,customerClone.class)).collect(Collectors.toList());
        }
    }


}
