package com.ebp.implementation;

import com.ebp.dataTransfer.readingClone;
import com.ebp.entities.Connection;
import com.ebp.entities.Reading;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.exceptionHandler.detailsNotAvailableException;
import com.ebp.exceptionHandler.multipleDateHandler;
import com.ebp.helper.readingResponse;
import com.ebp.repository.connectionRepository;
import com.ebp.repository.readingRepository;
import com.ebp.service.readingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author rohit.parihar 9/5/2022
 * @Class readingImplementation
 * @Project Electricity Bill Payment
 */

@Service
public class readingImplementation implements readingService {

    @Autowired
    private readingRepository readingRepository;

    @Autowired
    private connectionRepository connectionRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public readingClone selfSubmitReading(readingClone readingClone, String username) {
        Connection connection = this.connectionRepository.findByCustomer_Email(username).orElseThrow(() -> new detailsNotAvailableException("Connection", "Username", username));
        List<Reading> readingsByConsumerNo = this.readingRepository.findReadingsByConnection_ConsumerNo(connection.getConsumerNo());
        List<LocalDate> collect = readingsByConsumerNo.stream().map(Reading::getReadingDate).collect(Collectors.toList());
        collect.stream().forEach(e->{
                if (LocalDate.now().isEqual(e)){
                    throw new multipleDateHandler("Reading", LocalDate.now());
                }
        });
        Reading reading = this.modelMapper.map(readingClone, Reading.class);
        reading.setReadingPhoto("default.png");
        reading.setReadingDate(LocalDate.now());
        reading.setConnection(connection);
        Reading savedReading = this.readingRepository.save(reading);
        return this.modelMapper.map(savedReading, readingClone.class);
    }

    @Override
    public readingClone findById(Long readingId) {
        Reading reading = this.readingRepository.findById(readingId).orElseThrow(() -> new detailsNotAvailableException("Reading", "Reading Id", readingId));
        return this.modelMapper.map(reading, readingClone.class);
    }

    @Override
    public readingResponse getAllReading(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Reading> readingContent = this.readingRepository.findAll(page);
        List<Reading> contents = readingContent.getContent();
        List<readingClone> readingCloneData = contents.stream().map(e -> this.modelMapper.map(e, readingClone.class)).collect(Collectors.toList());
        readingResponse readingResponse = new readingResponse();
        readingResponse.setReadingContent(readingCloneData);
        readingResponse.setPageNumber(readingContent.getNumber());
        readingResponse.setPageSize(readingContent.getSize());
        readingResponse.setTotalPages(readingContent.getTotalPages());
        readingResponse.setTotalReadings(readingContent.getTotalElements());
        readingResponse.setIsFirstPage(readingContent.isFirst());
        readingResponse.setIsLastPage(readingContent.isLast());
        return readingResponse;
    }

    @Override
    public readingClone editReading(Long readingId, readingClone readingClone) {
        Reading reading = this.readingRepository.findById(readingId).orElseThrow(() -> new detailsNotAvailableException("Reading", "Reading Id", readingId));
        reading.setUnitsConsumed(readingClone.getUnitsConsumed());
        reading.setPricePerUnit(readingClone.getPricePerUnit());
        reading.setDateEdited(LocalDate.now());
        reading.setReadingPhoto(readingClone.getReadingPhoto());
        Reading editedReading = this.readingRepository.save(reading);
        return this.modelMapper.map(editedReading, readingClone.class);
    }

    @Override
    public void deleteReading(Long readingId) {
        Reading reading = this.readingRepository.findById(readingId).orElseThrow(() -> new detailsNotAvailableException("Reading", "Reading Id", readingId));
        this.readingRepository.delete(reading);
    }

    @Override
    public readingClone byConsumerNoAndDate(String consumerNo, String date) {
        String a = date;
        LocalDate fromDate = LocalDate.parse(a);
        Reading reading = this.readingRepository.findReadingByConnection_ConsumerNoAndReadingDate(consumerNo, fromDate);
        return this.modelMapper.map(reading, readingClone.class);
    }

    @Override
    public List<readingClone> byConsumerNo(String consumerNo) {
        List<Reading> reading = this.readingRepository.findReadingsByConnection_ConsumerNo(consumerNo);
        List<readingClone> collect = reading.stream().map(e -> this.modelMapper.map(e, readingClone.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<readingClone> seeReadings(String username) {
        List<Reading> readings = this.readingRepository.findByConnection_Customer_Email(username);
        if (readings.isEmpty()){
            throw new authorizationException("Please submit reading by the given link http://localhost:8080/ebp/all-readings");
        }
        List<readingClone> collect = readings.stream().map(e -> this.modelMapper.map(e, readingClone.class)).collect(Collectors.toList());
        return collect;
    }
}
