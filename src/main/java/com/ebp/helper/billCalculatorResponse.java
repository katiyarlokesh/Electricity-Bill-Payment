package com.ebp.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author rohit.parihar 9/16/2022
 * @Class billCalculatorResponse
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class billCalculatorResponse {
    String BillAmount;
    Boolean status;
}
