package com.ebp.controller;

import com.ebp.authorizations.accessedData;
import com.ebp.dataTransfer.paymentClone;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.helper.ebpResponse;
import com.ebp.helper.paymentResponse;
import com.ebp.security.JWTTokenHelper;
import com.ebp.service.paymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author rohit.parihar 9/8/2022
 * @Class paymentController
 * @Project Electricity Bill Payment
 */

@RestController
@RequestMapping("/ebp")
public class paymentController {

    @Autowired
    private paymentService paymentService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private accessedData accessedData;

    @PostMapping("payment/bill-payment/{billId}")
    public ResponseEntity<paymentClone> payBill(@RequestBody paymentClone paymentClone, HttpServletRequest request, @PathVariable Long billId) throws MessagingException {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Payment of bill");
        paymentClone paymentCloneData = this.paymentService.payBill(paymentClone, billId, username);
        return new ResponseEntity<>(paymentCloneData, HttpStatus.CREATED);
    }

    @GetMapping("/payments/all-payments")
    public ResponseEntity<paymentResponse> allPayments(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                       @RequestParam(value = "pageSize", defaultValue = "5",  required = false) Integer pageSize,
                                                       HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of all payments");
        paymentResponse paymentResponse = this.paymentService.allPayments(pageNumber, pageSize);
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

    @GetMapping("/all-payments/consumerNo/{consumerNo}")
    public ResponseEntity<List<paymentClone>> byConsumerNo(@PathVariable String consumerNo, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Historical Payment by Consumer no");
        List<paymentClone> paymentClones = this.paymentService.historicalPayment(consumerNo);
        return new ResponseEntity<>(paymentClones, HttpStatus.OK);
    }


    @GetMapping("payment/send-email/{paymentId}")
    public ResponseEntity<ebpResponse> sendMail(@PathVariable Long paymentId, HttpServletRequest request) throws MessagingException {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Payment Email");
        this.paymentService.emailOnCompletion(paymentId);
        return new ResponseEntity<>(new ebpResponse("Payment Done", true), HttpStatus.OK);
    }

    @GetMapping("/payments/my-payments")
    public ResponseEntity<?> myPayments(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "All Payments of : " + username);
        List<paymentClone> paymentClones = this.paymentService.myPayments(username);
        return new ResponseEntity<>(paymentClones, HttpStatus.OK);
    }
}
