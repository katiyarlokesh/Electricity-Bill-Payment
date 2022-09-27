package com.ebp.helper;

import com.ebp.dataTransfer.connectionClone;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Author rohit.parihar 9/4/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

@Getter
@Setter
@NoArgsConstructor
public class connectionResponse {
    private List<connectionClone> connectionContent;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalConnections;
    private Boolean isFirstPage;
    private Boolean isLastPage;
}
