package com.ebp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;

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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String firstName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String middleName;
    private String lastName;

    @Column(unique = true)
    private String aadharNumber;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobileNumber;
    private String gender;

    @Column(updatable = false)
    private LocalDate dateCreated;
    private LocalDate dateEdited;
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private User user;
}
