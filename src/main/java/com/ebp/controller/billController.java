package com.ebp.controller;

import com.ebp.authorizations.accessedData;
import com.ebp.dataTransfer.billClone;
import com.ebp.entities.Calculate;
import com.ebp.entities.connectionType;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.helper.billCalculatorResponse;
import com.ebp.helper.billResponse;
import com.ebp.helper.ebpResponse;
import com.ebp.security.JWTTokenHelper;
import com.ebp.service.billService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author rohit.parihar 9/6/2022
 * @Class billController
 * @Project Electricity Bill Payment
 */

@RestController
@RequestMapping("/ebp")
public class billController {

    @Autowired
    private billService billService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private accessedData accessedData;

    /**
     * GetMapping || Authorized || All Roles
     * URI -> http://localhost:8080/ebp/generate-bill
     * Generate Bill
     */
    @PostMapping("/generate-bill/reading/{readingId}")
    public ResponseEntity<?> createBill(@RequestBody billClone billClone, HttpServletRequest request, @PathVariable Long readingId) throws MessagingException {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Bill Creation");
        billClone billCloneData = this.billService.selfSubmitReading(billClone, readingId, username);
        return new ResponseEntity<>(billCloneData, HttpStatus.CREATED);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/search-bill/{billId}
     * Search bill by Id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search-bill/{billId}")
    public ResponseEntity<billClone> byId(@PathVariable Long billId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search bill by Id");
        billClone byId = this.billService.getById(billId);
        return ResponseEntity.ok(byId);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/bill/extend-duedate/{billId}
     * Extend Bill Due Date
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bill/extend-duedate/{billId}")
    public ResponseEntity<billClone> extendDueDate(@PathVariable Long billId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Extend Bill due date");
        billClone billClone = this.billService.extendDueDate(billId);
        return new ResponseEntity<>(billClone, HttpStatus.OK);
    }

    /**
     * DeleteMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/delete-bill/{billId}
     * Delete Bill by Bill Id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-bill/{billId}")
    public ResponseEntity<?> deleteBill(@PathVariable Long billId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Delete Bill by Bill Id : " + billId);
        this.billService.deleteBill(billId);
        return new ResponseEntity<>(new ebpResponse("Deleted Successfully", true), HttpStatus.OK);
    }

    /**
     * GetMapping || Unauthorized || Open
     * URI -> http://localhost:8080/ebp/user/open/search-bill/consumerno/{consumerNo}
     * Search bill by Consumer no
     */
    @GetMapping("/user/open/search-bill/consumerno/{consumerNo}")
    public ResponseEntity<List<billClone>> byConsumerNo(@PathVariable String consumerNo){
        List<billClone> list = this.billService.byConsumerNo(consumerNo);
        return ResponseEntity.ok(list);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/search/bill-mobile/{mobile}
     * Search bill by Mobile no
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/bill-mobile/{mobile}")
    public ResponseEntity<List<billClone>> byMobile(@PathVariable String mobile, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search bill by Mobile no");
        List<billClone> billClones = this.billService.byMobileNo(mobile);
        return ResponseEntity.ok(billClones);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/all-bills
     * Getting all bills
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-bills")
    public ResponseEntity<?> allBills(@RequestParam(value = "pageNumber",defaultValue = "0", required = false) Integer pageNumber,
                                     @RequestParam(value = "pageSize" ,defaultValue = "5",required = false) Integer pageSize,
                                      HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Getting all bills");
        billResponse billResponse = this.billService.allBills(pageNumber, pageSize);
        return ResponseEntity.ok(billResponse);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/all-bills/by-email/{email}
     * Getting all bills by email
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-bills/by-email/{email}")
    public ResponseEntity<?> billsByMail(@PathVariable String email, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search bill by email");
        List<billClone> billClone = this.billService.byEmail(email);
        return ResponseEntity.ok(billClone);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/bill-daterange/{from}/{to}
     * Getting all bills by date range (YYYY-MM-DD)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bill-daterange/{from}/{to}")
    public ResponseEntity<?> datesInBetween(@PathVariable String from,@PathVariable String to, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search bill by date range");
        List<billClone> billClones = this.billService.byDateRange(from, to);
        return ResponseEntity.ok(billClones);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/bill/email-bill/{billId}
     * Send bill to customer email
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bill/{billId}/email")
    public ResponseEntity<ebpResponse> billToCustomer(@PathVariable Long billId, HttpServletRequest request) throws MessagingException {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Send bill to customer email");
        this.billService.billToCustomer(billId);
        return new ResponseEntity<>(new ebpResponse("Email sent successfully", true), HttpStatus.OK);
    }

    /**
     * PostMapping || Unuthorized || Open
     * URI -> http://localhost:8080/ebp/user/open/bill-calculator
     * bill calculator
     */
    @PostMapping("/user/open/bill-calculator")
    public ResponseEntity<?> billCalculator(@RequestBody Calculate calculate){
        Integer integer = this.billService.calculateData(calculate);
        return ResponseEntity.ok(new billCalculatorResponse(""+integer, true));
    }

    @GetMapping("/my-bills")
    public ResponseEntity<?> myBills(HttpServletRequest request) throws MessagingException {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "All Bills of Customer");
        List<billClone> allbills = this.billService.allBills(username);
        return new ResponseEntity<>(allbills, HttpStatus.OK);
    }
}
