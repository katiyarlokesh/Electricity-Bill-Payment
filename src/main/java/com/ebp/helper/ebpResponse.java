package com.ebp.helper;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ebpResponse {
    private String Message;
    private Boolean Done;
}
