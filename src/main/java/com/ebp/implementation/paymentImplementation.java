package com.ebp.implementation;

import com.ebp.dataTransfer.customerClone;
import com.ebp.dataTransfer.paymentClone;
import com.ebp.email.emailService;
import com.ebp.entities.*;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.exceptionHandler.detailsNotAvailableException;
import com.ebp.helper.customerResponse;
import com.ebp.helper.paymentResponse;
import com.ebp.repository.billRepository;
import com.ebp.repository.paymentRepository;
import com.ebp.service.paymentService;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;



@Service
public class paymentImplementation implements paymentService {

    @Autowired
    private billRepository billRepository;

    @Autowired
    private paymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private emailService emailService;

    @Override
    public paymentClone payBill(paymentClone paymentClone, Long billId, String username) throws MessagingException {
        Bill bill = this.billRepository.findById(billId).orElseThrow(() -> new detailsNotAvailableException("Bill", "Bill Id", billId));
        Payment payment = this.modelMapper.map(paymentClone, Payment.class);
        payment.setPaymentDate(LocalDate.now());
        LocalDate billDueDate = bill.getBillDueDate();
        if(LocalDate.now().isAfter(billDueDate)){
            payment.setLatePaymentCharges(100.00);
        }
        else {
            payment.setLatePaymentCharges(0.00);
        }
        Double lateCharges = payment.getLatePaymentCharges();
        Integer billAmount = bill.getBillAmount();
        Double totalAmount = billAmount + lateCharges;
        payment.setTotalPaid(totalAmount);
        payment.setStatus(true);
        payment.setBill(bill);
        Long readingId = bill.getReading().getReadingId();
        Customer customer = bill.getReading().getConnection().getCustomer();
        String aadharNumber = bill.getReading().getConnection().getCustomer().getAadharNumber();
        int year = LocalDate.now().getYear();
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        String consumerNo = bill.getReading().getConnection().getConsumerNo();
        String transactionId = year + "-" + aadharNumber.substring(5) + "-" + readingId + dayOfMonth + "-" + new Random(1000).nextInt(4);
        payment.setTransactionId(transactionId);
        Connection connection = bill.getReading().getConnection();
        Reading reading = bill.getReading();
        String subject = "Payment for " + consumerNo + " " + LocalDate.now().getMonth();
        String email = bill.getReading().getConnection().getCustomer().getEmail();
        if (!email.equals(username)){
            throw new authorizationException("Please enter correct bill Id");
        }
        String content = "<h2 style=\"text-align:center;\">Electricity Bill Payment (EBP)</h2>" +
                "<table>" +
                "<tr>" +
                "<td>Consumer Number</td>" +
                "<td>"+consumerNo + "<td>" +
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
                "<td>"+connection.getConnDate()+"<td>" +
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
                "</table>";
        this.emailService.sendEmail(subject, content, email);
        Payment data = this.paymentRepository.save(payment);
        return this.modelMapper.map(data, paymentClone.class);
    }

    @Override
    public paymentResponse allPayments(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Payment> all = this.paymentRepository.findAll(page);
        List<Payment> paymentContent = all.getContent();
        List<paymentClone> customerCloneData = paymentContent.stream().map(e -> this.modelMapper.map(e, paymentClone.class)).collect(Collectors.toList());
        paymentResponse paymentResponse = new paymentResponse();
        paymentResponse.setPaymentContent(customerCloneData);
        if(paymentContent.isEmpty()){
            throw new detailsNotAvailableException("Payment Data");
        }
        paymentResponse.setPageNumber(all.getNumber());
        paymentResponse.setPageSize(all.getSize());
        paymentResponse.setTotalPages(all.getTotalPages());
        paymentResponse.setTotalCustomers(all.getTotalElements());
        paymentResponse.setIsFirstPage(all.isFirst());
        paymentResponse.setIsLastPage(all.isLast());
        return paymentResponse;
    }

    @Override
    public void emailOnCompletion(Long paymentId) throws MessagingException {
        try {
            Payment payment = this.paymentRepository.findById(paymentId).orElseThrow(() -> new detailsNotAvailableException("Payment", "Payment Id", paymentId));
            Bill bill = payment.getBill();
            Reading reading = bill.getReading();
            Connection connection = reading.getConnection();
            Customer customer = connection.getCustomer();
            String email = customer.getEmail();
            String one;
            if (payment.getStatus()) {
                one = "Done";
            } else {
                one = "Fail";
            }
            String subject = "Payment for " + connection.getConsumerNo();
            String body = bill.getBillAmount() + "\n" + "Status : " + one;
            this.emailService.sendEmail(subject, body, email);
        }
        catch (NullPointerException e){
            throw new authorizationException("Please enter correct payment Id");
        }
    }

    @Override
    public List<paymentClone> historicalPayment(String consumerNo) {
        List<Payment> paymentsData = this.paymentRepository.findPaymentsByBill_Reading_Connection_ConsumerNo(consumerNo);
        if(paymentsData.isEmpty()){
            throw new detailsNotAvailableException("Data");
        }
        List<paymentClone> collect = paymentsData.stream().map(e -> this.modelMapper.map(e, paymentClone.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<paymentClone> myPayments(String username) {
        List<Payment> payments = this.paymentRepository.findPaymentsByBill_Reading_Connection_Customer_Email(username);
        if (payments.isEmpty()){
            throw new detailsNotAvailableException("Payments");
        }
        List<paymentClone> collect = payments.stream().map(e -> this.modelMapper.map(e, paymentClone.class)).collect(Collectors.toList());
        return collect;
    }
}
