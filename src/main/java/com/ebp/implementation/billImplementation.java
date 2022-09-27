package com.ebp.implementation;

import com.ebp.dataTransfer.billClone;
import com.ebp.email.emailService;
import com.ebp.entities.*;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.exceptionHandler.detailsNotAvailableException;
import com.ebp.helper.billCalculator;
import com.ebp.helper.billResponse;
import com.ebp.repository.billRepository;
import com.ebp.repository.connectionRepository;
import com.ebp.repository.readingRepository;
import com.ebp.service.billService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author rohit.parihar 9/6/2022
 * @Class billImplementation
 * @Project Electricity Bill Payment
 */

@Service
public class billImplementation implements billService {

    @Autowired
    private billRepository billRepository;

    @Autowired
    private readingRepository readingRepository;

    @Autowired
    private billCalculator billCalculator;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private emailService emailService;

    @Autowired
    private connectionRepository connectionRepository;

    @Override
    public billClone selfSubmitReading(billClone billClone, Long readingId, String username) throws MessagingException {
        Bill bill = new Bill();
        Reading reading = this.readingRepository.findById(readingId).orElseThrow(()-> new detailsNotAvailableException("Reading", "Reading Id", readingId));
        Integer pricePerUnit = reading.getPricePerUnit();
        Integer unitsConsumed = reading.getUnitsConsumed();
        Connection connection = reading.getConnection();
        Customer customer = connection.getCustomer();
        if (!customer.getEmail().equals(username)){
            throw new authorizationException("Please enter your correct reading Id.");
        }
        connectionType connectionType = connection.getConnectionType();
        Integer billAmount=null;
        if (connectionType == com.ebp.entities.connectionType.INDUSTRIAL){
            billAmount=billCalculator.forINDUSTRIAL(unitsConsumed, pricePerUnit);
        }
        else if (connectionType == com.ebp.entities.connectionType.AGRICULTURAL){
            billAmount=billCalculator.forAGRICULTURAL(unitsConsumed, pricePerUnit);
        }
        else if (connectionType == com.ebp.entities.connectionType.NON_INDUSTRIAL){
            billAmount=billCalculator.forNON_INDUSTRIAL(unitsConsumed, pricePerUnit);
        }
        bill.setBillDate(LocalDate.now());
        bill.setBillDueDate(LocalDate.now().plusDays(10));
        bill.setUnitsConsumed(unitsConsumed);
        bill.setBillAmount(billAmount);
        bill.setReading(reading);
        Bill save = this.billRepository.save(bill);
        String subject = "Bill for " + connection.getConsumerNo() + " " + LocalDate.now().getMonth();
        String email = customer.getEmail();
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<table>" +
                "<tr>" +
                "<td>Consumer Number</td>" +
                "<td>"+connection.getConsumerNo()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Customer Name</td>" +
                "<td>"+customer.getName()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Mobile Number</td>" +
                "<td>"+customer.getMobileNumber()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Connection Date</td>" +
                "<td>"+connection.getConnectionType()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Connection Date</td>" +
                "<td>"+connection.getConnDate()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Connection Status</td>" +
                "<td>"+connection.getConnStatus()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Reading Date</td>" +
                "<td>"+reading.getReadingDate()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Units Consumed</td>" +
                "<td>"+reading.getUnitsConsumed()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Price Per Unit</td>" +
                "<td>"+reading.getPricePerUnit()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Total Amount</td>" +
                "<td>"+bill.getBillAmount()+"<td>" +
                "</tr>" +
                "<tr>" +
                "<td>Bill Due Date</td>" +
                "<td>"+bill.getBillDueDate()+"<td>" +
                "</tr>" +
                "</table>";
        this.emailService.sendEmail(subject, content, email);
        return this.modelMapper.map(save, billClone.class);
    }

    @Override
    public billClone getById(Long billId) {
        Bill bill = this.billRepository.findById(billId).orElseThrow(() -> new detailsNotAvailableException("Bill", "Bill Id", billId));
        System.out.println(bill.getBillDate());
        return this.modelMapper.map(bill, billClone.class);
    }

    @Override
    public billResponse allBills(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Bill> all = this.billRepository.findAll(pageable);
        List<Bill> content = all.getContent();
        List<billClone> collect = content.stream().map(e -> this.modelMapper.map(e, billClone.class)).collect(Collectors.toList());
        billResponse billResponse=new billResponse();
        billResponse.setContent(collect);
        if(collect.isEmpty()){
            throw new detailsNotAvailableException("Bills");
        }

        billResponse.setPageNumber(all.getNumber());
        billResponse.setPageSize(all.getSize());
        billResponse.setTotalBills(all.getTotalElements());
        billResponse.setTotalPages(all.getTotalPages());
        billResponse.setIsFirstPage(all.isFirst());
        billResponse.setIsLastPage(all.isLast());


        return billResponse;
    }

    @Override
    public billClone extendDueDate(Long billId) {
        Bill bill = this.billRepository.findById(billId).orElseThrow(() -> new detailsNotAvailableException("Bill", "Bill Id", billId));
        LocalDate billDueDate = bill.getBillDueDate();
        LocalDate newBillDueDate = billDueDate.plusDays(10);
        bill.setBillDueDate(newBillDueDate);
        Bill save = this.billRepository.save(bill);
        billClone billCloneData = this.modelMapper.map(save, billClone.class);
        return billCloneData;
    }

    @Override
    public void deleteBill(Long billId) {
        Bill bill = this.billRepository.findById(billId).orElseThrow(() -> new detailsNotAvailableException("Bill", "Bill Id", billId));
        this.billRepository.delete(bill);
    }

    @Override
    public List<billClone> byConsumerNo(String consumerNo) {
        List<Bill> bills = this.billRepository.findBillByReading_Connection_ConsumerNo(consumerNo);
        List<billClone> collect = bills.stream().map(e -> this.modelMapper.map(e, billClone.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<billClone> byMobileNo(String mobile) {
        List<Bill> bills = this.billRepository.findBillByReading_Connection_Customer_MobileNumber(mobile);
        List<billClone> collect = bills.stream().map(e -> this.modelMapper.map(e, billClone.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<billClone> byEmail(String email) {
        List<Bill> bills = billRepository.findBillByReading_Connection_Customer_Email(email);
        List<billClone> collect = bills.stream().map(e -> this.modelMapper.map(e, billClone.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<billClone> byDateRange(String from, String to) {
        String a = from;
        String b = to;
        LocalDate fromDate = LocalDate.parse(a);
        LocalDate toDate = LocalDate.parse(b);
        List<Bill> billsByBillDateBetween = this.billRepository.findBillsByBillDateBetween(fromDate, toDate);
        List<billClone> collect = billsByBillDateBetween.stream().map(e -> this.modelMapper.map(e, billClone.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Integer calculateData(Calculate calculate) {
        connectionType connectionType = calculate.getConnectionType();
        Integer unitsConsumed = calculate.getUnitsConsumed();
        Integer pricePerUnit = calculate.getPricePerUnit();
        Integer billAmount=null;
        if (connectionType == com.ebp.entities.connectionType.INDUSTRIAL){
            billAmount=billCalculator.forINDUSTRIAL(unitsConsumed, pricePerUnit);
        }
        else if (connectionType == com.ebp.entities.connectionType.AGRICULTURAL){
            billAmount=billCalculator.forAGRICULTURAL(unitsConsumed, pricePerUnit);
        }
        else if (connectionType == com.ebp.entities.connectionType.NON_INDUSTRIAL){
            billAmount=billCalculator.forNON_INDUSTRIAL(unitsConsumed, pricePerUnit);
        }
        return billAmount;
    }

    @Override
    public void billToCustomer(Long billId) throws MessagingException {
        Bill bill = this.billRepository.findById(billId).orElseThrow(() -> new detailsNotAvailableException("Bill", "Bill Id", billId));
        Reading reading = bill.getReading();
        Connection connection = reading.getConnection();
        Customer customer = connection.getCustomer();
        String email = customer.getEmail();
        String billDate = bill.getBillDate().toString();
        String subject = "Bill " + billDate;
        String content = "Electricity Bill \n" + customer.getName() + "\n" + customer.getMobileNumber() + "\n"
                + customer.getEmail() + "\n"
                + connection.getConsumerNo() + "\n"
                + connection.getConnectionType() + "\n\n"
                + connection.getAddress() + "\n"
                + reading.getReadingDate() + "\n"
                + reading.getUnitsConsumed() + "\n"
                + reading.getPricePerUnit() + "\n"
                + bill.getBillAmount() + "\n"
                + bill.getBillDueDate() + "\n";
        this.emailService.sendEmail(subject, content, email);
    }

    @Override
    public List<billClone> allBills(String username) {
        List<Bill> bills = this.billRepository.findBillByReading_Connection_Customer_Email(username);
        if (bills.isEmpty()){
            throw new detailsNotAvailableException("You have not any bills");
        }
        List<billClone> collect = bills.stream().map(e -> this.modelMapper.map(e, billClone.class)).collect(Collectors.toList());
        return collect;
    }
}
