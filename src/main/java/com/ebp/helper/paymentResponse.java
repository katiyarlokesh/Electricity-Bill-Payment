package com.ebp.helper;

import com.ebp.dataTransfer.customerClone;
import com.ebp.dataTransfer.paymentClone;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Author rohit.parihar 9/8/2022
 * @Class paymentResponse
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class paymentResponse {
    private List<paymentClone> paymentContent;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalCustomers;
    private Boolean isFirstPage;
    private Boolean isLastPage;
}
