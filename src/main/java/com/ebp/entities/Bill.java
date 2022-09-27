package com.ebp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/4/2022
 * @Class Bill
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;
    private LocalDate billDate;
    private LocalDate billDueDate;
    private Integer unitsConsumed;
    private Integer billAmount;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Reading reading;
}
