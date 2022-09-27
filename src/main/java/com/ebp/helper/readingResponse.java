package com.ebp.helper;

import com.ebp.dataTransfer.readingClone;
import com.ebp.entities.Reading;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Author rohit.parihar 9/5/2022
 * @Class readingResponse
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class readingResponse {
    private List<readingClone> readingContent;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalReadings;
    private Boolean isFirstPage;
    private Boolean isLastPage;
}
