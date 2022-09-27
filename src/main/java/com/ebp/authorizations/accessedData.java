package com.ebp.authorizations;

import com.ebp.entities.AccessHistory;
import com.ebp.repository.accessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/20/2022
 * @Class accessedData
 * @Project Electricity Bill Payment
 */

@Component
public class accessedData {

    @Autowired
    private accessRepository accessRepository;

    public void saveAccessedData(String by, String raisedFor){
        AccessHistory accessHistory = new AccessHistory();
        accessHistory.setAccessedAt(LocalDate.now());
        accessHistory.setAccessedBy(by);
        accessHistory.setRequestRaised(raisedFor);
        this.accessRepository.save(accessHistory);
    }
}
