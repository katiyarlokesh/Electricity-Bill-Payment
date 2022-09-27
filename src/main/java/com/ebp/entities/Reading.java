package com.ebp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Author rohit.parihar 9/4/2022
 * @Class Reading
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readingId;

    private Integer unitsConsumed;
    private String readingPhoto;

    private LocalDate readingDate;
    private Integer pricePerUnit;
    private LocalDate imageUploadDate;
    private LocalDate dateEdited;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Connection connection;
}
