package com.ebp.service;

import com.ebp.dataTransfer.readingClone;
import com.ebp.entities.Reading;
import com.ebp.helper.readingResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author rohit.parihar 9/5/2022
 * @Class readingService
 * @Project Electricity Bill Payment
 */
public interface readingService {
    readingClone selfSubmitReading(readingClone readingClone, String username);
    readingClone findById(Long readingId);
    readingResponse getAllReading(Integer pageNumber, Integer pageSize);
    readingClone editReading(Long readingId, readingClone readingClone);
    void deleteReading(Long readingId);
    readingClone byConsumerNoAndDate(String consumerNo, String date);
    List<readingClone> byConsumerNo(String consumerNo);
    List<readingClone> seeReadings(String username);
}
