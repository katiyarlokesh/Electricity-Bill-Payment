package com.ebp.dataTransfer;

import com.ebp.entities.Reading;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/6/2022
 * @Class billClone
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class billClone {
    private Long billId;
    private LocalDate billDate;
    private LocalDate billDueDate;
    private Integer unitsConsumed;
    private Integer billAmount;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Reading reading;
}
