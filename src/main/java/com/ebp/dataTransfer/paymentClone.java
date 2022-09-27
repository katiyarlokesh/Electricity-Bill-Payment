package com.ebp.dataTransfer;

import com.ebp.entities.Bill;
import com.ebp.entities.paymentMode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/8/2022
 * @Class paymentClone
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class paymentClone {
    private String transactionId;
    private LocalDate PaymentDate;
    private Double LatePaymentCharges;
    private Double TotalPaid;
    private Boolean Status;
    private paymentMode paymentMode;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Bill bill;
}
