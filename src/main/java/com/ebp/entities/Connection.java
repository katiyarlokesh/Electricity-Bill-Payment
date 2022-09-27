package com.ebp.entities;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "consumerNo"})
})
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long connectionId;

    private String consumerNo;

    private LocalDate appDate;

    @Enumerated(EnumType.STRING)
    private connectionType connectionType;
    private LocalDate connDate;
    private Boolean connStatus;
    private LocalDate updatedDate;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType. EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;
}
