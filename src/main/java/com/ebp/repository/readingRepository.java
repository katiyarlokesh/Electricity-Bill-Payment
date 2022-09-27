package com.ebp.repository;

import com.ebp.entities.Connection;
import com.ebp.entities.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @Author rohit.parihar 9/5/2022
 * @Class readingRepository
 * @Project Electricity Bill Payment
 */
public interface readingRepository extends JpaRepository<Reading, Long> {
    Reading findReadingByConnection_ConsumerNoAndReadingDate(String consumerNo, LocalDate date);
    List<Reading> findReadingsByConnection_ConsumerNo(String consumerNo);
    List<Reading> findByConnection_Customer_Email(String username);
}
