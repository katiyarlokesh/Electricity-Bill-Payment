package com.ebp.service;

import com.ebp.dataTransfer.connectionClone;
import com.ebp.dataTransfer.paymentClone;
import com.ebp.helper.connectionResponse;

import java.util.List;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

public interface connectionService {
    connectionClone getConnectionById(Long connectionId);
    connectionClone createConnection(connectionClone connectionClone, String username);
    connectionResponse allConnections(Integer pageNumber, Integer pageSize);
    connectionClone editConnection(connectionClone connectionClone, String username);
    void deleteConnection(Long connectionId);
    void activateConnection(Long connectionId);
    List<connectionClone> activeConnectionByVillage(String village);
    List<connectionClone> inActiveConnectionByVillage(String village);
    List<connectionClone> activeConnectionByTaluka(String taluka);
    List<connectionClone> inactiveConnectionByTaluka(String taluka);
    List<connectionClone> activeConnectionByDistrict(String district);
    List<connectionClone> inactiveConnectionByDistrict(String district);
    List<connectionClone> activeConnectionByState(String state);
    List<connectionClone> inactiveConnectionByState(String state);
    List<connectionClone> activeConnectionByPincode(String pincode);
    List<connectionClone> inactiveConnectionByPincode(String pincode);
    connectionClone byConsumerNo(String consumerNo);
    connectionClone changeAddress(connectionClone connectionClone, String username);
    Boolean forNewConnection(paymentClone paymentClone, String username);
    connectionClone seeConnection(String username);
}
