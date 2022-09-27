package com.ebp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/19/2022
 * @Class AccessHistory
 * @Project Electricity Bill Payment
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccessHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accessedBy;
    private String requestRaised;
    private LocalDate accessedAt;
}
