package com.ebp.helper;

import com.ebp.dataTransfer.billClone;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * @Author rohit.parihar 9/8/2022
 * @Class billResponse
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class billResponse {

    private List<billClone> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalBills;
    private Integer totalPages;
    private Boolean isFirstPage;
    private Boolean isLastPage;

}
