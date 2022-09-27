package com.ebp.helper;

import com.ebp.dataTransfer.customerClone;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Author rohit.parihar 9/2/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

@Getter
@Setter
@NoArgsConstructor
public class customerResponse {
    private List<customerClone> customerContent;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalCustomers;
    private Boolean isFirstPage;
    private Boolean isLastPage;
}
