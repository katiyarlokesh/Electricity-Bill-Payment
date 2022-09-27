package com.ebp.dataTransfer;

import com.ebp.entities.Address;
import com.ebp.entities.Customer;
import com.ebp.entities.connectionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionClone
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

@Getter
@Setter
@NoArgsConstructor
public class connectionClone {
    private Long connectionId;

    private String consumerNo;
    private LocalDate appDate;
    private connectionType connectionType;
    private LocalDate connDate;
    private Boolean connStatus;
    private LocalDate updatedDate;
//    private Customer customer;
    private Address address;
}
