package com.ebp.implementation;

import com.ebp.dataTransfer.connectionClone;
import com.ebp.dataTransfer.paymentClone;
import com.ebp.entities.Address;
import com.ebp.entities.Connection;
import com.ebp.entities.Customer;
import com.ebp.entities.Payment;
import com.ebp.exceptionHandler.*;
import com.ebp.helper.connectionResponse;
import com.ebp.repository.addressRepository;
import com.ebp.repository.connectionRepository;
import com.ebp.repository.customerRepository;
import com.ebp.repository.paymentRepository;
import com.ebp.service.connectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

@Service
public class connectionImplementation implements connectionService {

    @Autowired
    private connectionRepository connectionRepository;

    @Autowired
    private addressRepository addressRepository;

    @Autowired
    private customerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private paymentRepository paymentRepository;


    /**
     *
     * Complete
     *
     */
    @Override
    public connectionClone getConnectionById(Long connectionId) {
        Connection connection = this.connectionRepository.findById(connectionId).orElseThrow(() -> new detailsNotAvailableException("Connection", "Connection Id", connectionId));
        return this.modelMapper.map(connection, connectionClone.class);
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public connectionClone createConnection(connectionClone connectionClone, String username) {
        Customer customer = this.customerRepository.findByEmail(username).orElseThrow(() -> new authorizationException("Please fill your details by this Link http://localhost:8080/ebp/user/fill-details"));
        Connection connection = this.modelMapper.map(connectionClone, Connection.class);
        String mobileNumber = customer.getMobileNumber();
        String aadharNumber = customer.getAadharNumber();
        Long customerId = customer.getCustomerId();
        String consumerNo = mobileNumber.substring(4,8) + customerId + aadharNumber.substring(0,4);
        connection.setCustomer(customer);
        connection.setConsumerNo(consumerNo);
        connection.setAppDate(LocalDate.now());
        connection.setConnectionType(connectionClone.getConnectionType());
        connection.setAddress(connectionClone.getAddress());
        connection.setConnStatus(false);
        Connection savedConnection = this.connectionRepository.save(connection);
        return this.modelMapper.map(savedConnection, connectionClone.class);
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public connectionResponse allConnections(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Connection> connectionData = this.connectionRepository.findAll(pageable);
        List<Connection> connectionContent = connectionData.getContent();
        List<connectionClone> connectionCloneData = connectionContent.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        connectionResponse connectionResponse = new connectionResponse();
        connectionResponse.setConnectionContent(connectionCloneData);
        if(connectionContent.isEmpty()){
            throw new detailsNotAvailableException("Connection data");
        }
        connectionResponse.setPageNumber(connectionData.getNumber());
        connectionResponse.setPageSize(connectionData.getSize());
        connectionResponse.setTotalPages(connectionData.getTotalPages());
        connectionResponse.setTotalConnections(connectionData.getTotalElements());
        connectionResponse.setIsFirstPage(connectionData.isFirst());
        connectionResponse.setIsLastPage(connectionData.isLast());
        return connectionResponse;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public connectionClone editConnection(connectionClone connectionClone, String username) {
        Customer customer = this.customerRepository.findByEmail(username).orElseThrow(() -> new authorizationException("Please fill your details by this Link http://localhost:8080/ebp/user/fill-details. Then apply for new Connection by this Link http://localhost:8080/ebp/customer/apply-connection"));
        Connection connection = this.connectionRepository.findByCustomer(customer);
        if (connectionClone.getConnectionType()==null) {
            connection.setConnectionType(connectionClone.getConnectionType());
        }
        if (connectionClone.getAddress()==null) {
            connection.setAddress(connectionClone.getAddress());
        }
        connection.setUpdatedDate(LocalDate.now());
        return this.modelMapper.map(connection, connectionClone.class);
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public void deleteConnection(Long connectionId) {
        Connection connection = this.connectionRepository.findById(connectionId).orElseThrow(() -> new detailsNotAvailableException("Connection", "Connection Id", connectionId));
        this.connectionRepository.delete(connection);
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public void activateConnection(Long connectionId) {
        Connection connection = this.connectionRepository.findById(connectionId).orElseThrow(() -> new detailsNotAvailableException("Connection", "Connection Id", connectionId));
        if (connection.getConnStatus()){
            throw new alreadyActivated("Connection", connectionId);
        }
        connection.setConnStatus(true);
        connection.setConnDate(LocalDate.now());
        this.connectionRepository.save(connection);
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> activeConnectionByVillage(String village) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_VillageAndConnStatusTrue(village);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> inActiveConnectionByVillage(String village) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_VillageAndConnStatusFalse(village);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> activeConnectionByTaluka(String taluka) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_TalukaAndConnStatusTrue(taluka);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> inactiveConnectionByTaluka(String taluka) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_TalukaAndConnStatusFalse(taluka);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> activeConnectionByDistrict(String district) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_DistrictAndConnStatusTrue(district);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> inactiveConnectionByDistrict(String district) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_DistrictAndConnStatusFalse(district);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> activeConnectionByState(String state) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_StateAndConnStatusTrue(state);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> inactiveConnectionByState(String state) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_StateAndConnStatusFalse(state);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> activeConnectionByPincode(String pincode) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_PincodeAndConnStatusTrue(pincode);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public List<connectionClone> inactiveConnectionByPincode(String pincode) {
        List<Connection> byAddress = this.connectionRepository.findConnectionsByAddress_PincodeAndConnStatusFalse(pincode);
        List<connectionClone> collect = byAddress.stream().map(e -> this.modelMapper.map(e, connectionClone.class)).collect(Collectors.toList());
        return collect;
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public connectionClone byConsumerNo(String consumerNo) {
        try {
            Connection byConsumerNo = this.connectionRepository.findByConsumerNo(consumerNo);
            return this.modelMapper.map(byConsumerNo, connectionClone.class);
        }
        catch (IncorrectResultSizeDataAccessException e){
            throw new multipleConnectionByConsumerNo("Connection", "Consumer No", consumerNo);
        }
        catch(Exception e){
            throw new detailsNotAvailableException("Connection", "Consumer No", consumerNo);
        }
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public connectionClone changeAddress(connectionClone connectionClone, String username) {
        Customer customer = this.customerRepository.findByEmail(username).orElseThrow(() -> new authorizationException("Please fill your details by this Link http://localhost:8080/ebp/user/fill-details. Then apply for new Connection by this Link http://localhost:8080/ebp/customer/apply-connection"));
        Connection connection = this.connectionRepository.findByCustomer(customer);
        connection.setAddress(connectionClone.getAddress());
        Connection updatedAddress = this.connectionRepository.save(connection);
        return this.modelMapper.map(updatedAddress, connectionClone.class);
    }

    /**
     *
     * Complete
     *
     */
    @Override
    public Boolean forNewConnection(paymentClone paymentClone, String username) {
        Customer customer = this.customerRepository.findByEmail(username).orElseThrow(() -> new detailsNotAvailableException("Customer", "Email", username));
        Connection connection = this.connectionRepository.findByCustomer(customer);
        LocalDate appDate = connection.getAppDate();
        Payment payment = this.modelMapper.map(paymentClone, Payment.class);
        int year = LocalDate.now().getYear();
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        String mobileNumber = customer.getMobileNumber();
        String aadharNumber = customer.getAadharNumber();
        String consumerNo = connection.getConsumerNo();
        String transactionId = year + "-" + aadharNumber.substring(5) + "-" + connection.getConnectionId() + dayOfMonth + consumerNo.substring(4) + "-" + mobileNumber.substring(5);
        payment.setTransactionId(transactionId);
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus(true);
        payment.setPaymentMode(paymentClone.getPaymentMode());
        Double lateCharges = null;
        if (LocalDate.now().isAfter(appDate)){
            lateCharges=100.00;
             payment.setLatePaymentCharges(100.00);
        }
        else {
            lateCharges=0.00;
            payment.setLatePaymentCharges(0.00);
        }
        payment.setTotalPaid(500.00 + lateCharges);
        this.paymentRepository.save(payment);
        connection.setConnStatus(true);
        connection.setConnDate(LocalDate.now());
        this.connectionRepository.save(connection);
        return true;
    }

    @Override
    public connectionClone seeConnection(String username) {
        Connection connection = this.connectionRepository.findByCustomer_Email(username).orElseThrow(() -> new authorizationException("Kindly apply for new Connection as you don't have any connection. Apply for new Connection by the given Link http://localhost:8080/ebp/customer/apply-connection"));
        connectionClone connectionCloneData = this.modelMapper.map(connection, connectionClone.class);
        return connectionCloneData;
    }
}
