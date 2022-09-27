package com.ebp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class Calculate
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class Calculate {
    private connectionType connectionType;
    private Integer unitsConsumed;
    private Integer pricePerUnit;
}
