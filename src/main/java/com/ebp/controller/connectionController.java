package com.ebp.controller;

import com.ebp.authorizations.accessedData;
import com.ebp.dataTransfer.connectionClone;
import com.ebp.dataTransfer.paymentClone;
import com.ebp.exceptionHandler.authorizationException;
import com.ebp.exceptionHandler.detailsNotAvailableException;
import com.ebp.helper.connectionResponse;
import com.ebp.helper.ebpResponse;
import com.ebp.security.JWTTokenHelper;
import com.ebp.service.connectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionController
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

@RestController
@RequestMapping("/ebp")
public class connectionController {

    @Autowired
    private connectionService connectionService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private accessedData accessedData;

    /* Get mapping for showing connection by connection ID*/
    @GetMapping("/connection/{connectionId}")
    public ResponseEntity<connectionClone> getConnectionById(@PathVariable Long connectionId){
        connectionClone connectionById = this.connectionService.getConnectionById(connectionId);
        return ResponseEntity.ok(connectionById);
    }

    /**
     * PostMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/customer/apply-connection
     * Apply for new Connection
     */
    @PostMapping("/customer/apply-connection")
    public ResponseEntity<connectionClone> createConnection(@Valid @RequestBody connectionClone connectionClone, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Apply for new Connection");
        connectionClone connection = this.connectionService.createConnection(connectionClone, username);
        return new ResponseEntity<>(connection, HttpStatus.PAYMENT_REQUIRED);
    }

    /**
     * GetMapping || Authorized || Admin
     * URI -> http://localhost:8080/ebp/connection/all-connection
     * Fill fresh details in User
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/all-connection")
    public ResponseEntity<connectionResponse> getAllConnection(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                               @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                               HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of all Collections");
        connectionResponse allConnections = this.connectionService.allConnections(pageNumber, pageSize);
        return ResponseEntity.ok(allConnections);
    }

    /**
     * PutMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/connection/edit-connection
     * Fill fresh details in User
     */
    @PutMapping("/connection/edit-connection")
    public ResponseEntity<connectionClone> editConnection(@Valid @RequestBody connectionClone connectionClone, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Edit Connection");
        connectionClone editedConnection = this.connectionService.editConnection(connectionClone, username);
        return new ResponseEntity<>(editedConnection, HttpStatus.CREATED);
    }

    /**
     * DeleteMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/{connectionId}/delete-connection
     * Delete Connection by Connection Id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/connection/{connectionId}/delete-connection")
    public ResponseEntity<?> deleteConnection(@PathVariable Long connectionId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Deleting Connection of Id : " + connectionId);
        this.connectionService.deleteConnection(connectionId);
        return new ResponseEntity<>(new ebpResponse("Connection Deleted Successfully", true), HttpStatus.OK);
    }

    /**
     * PutMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/{connectionId}/activate-connection
     * Activate Connection by ID
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/connection/{connectionId}/activate-connection")
    public ResponseEntity<?> activateConnection(@PathVariable Long connectionId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Activate Connection of Connection Id : " + connectionId);
        this.connectionService.activateConnection(connectionId);
        return new ResponseEntity<>(new ebpResponse("Connection is activated Successfully", true), HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/active-village/{village}
     * List of Active Connections by Village
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/active-village/{village}")
    public ResponseEntity<List<connectionClone>> activeByVillage(@PathVariable String village, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Active Connections by Village");
        List<connectionClone> connectionClones = this.connectionService.activeConnectionByVillage(village);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/inactive-village/{village}
     * List of Inactive Connections by Village
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/inactive-village/{village}")
    public ResponseEntity<List<connectionClone>> inActiveByVillage(@PathVariable String village, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Inactive Connections by Village");
        List<connectionClone> connectionClones = this.connectionService.inActiveConnectionByVillage(village);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/active-taluka/{taluka}
     * List of Active Connections by Taluka
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/active-taluka/{taluka}")
    public ResponseEntity<List<connectionClone>> activeByTaluka(@PathVariable String taluka, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Active Connections by Taluka");
        List<connectionClone> connectionClones = this.connectionService.activeConnectionByTaluka(taluka);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/inactive-taluka/{taluka}
     * List of Inactive Connections by Taluka
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/inactive-taluka/{taluka}")
    public ResponseEntity<List<connectionClone>> inActiveByTaluka(@PathVariable String taluka, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Inactive Connections by Taluka");
        List<connectionClone> connectionClones = this.connectionService.inactiveConnectionByTaluka(taluka);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/active-district/{district}
     * List of Active Connections by District
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/active-district/{district}")
    public ResponseEntity<List<connectionClone>> activeByDistrict(@PathVariable String district, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Active Connections by District");
        List<connectionClone> connectionClones = this.connectionService.activeConnectionByDistrict(district);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/inactive-district/{district}
     * List of Inactive Connections by District
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/inactive-district/{district}")
    public ResponseEntity<List<connectionClone>> inActiveByDistrict(@PathVariable String district, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Inactive Connections by District");
        List<connectionClone> connectionClones = this.connectionService.inactiveConnectionByDistrict(district);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/active-state/{state}
     * List of Active Connections by State
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/active-state/{state}")
    public ResponseEntity<List<connectionClone>> activeByState(@PathVariable String state, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Active Connections by State");
        List<connectionClone> connectionClones = this.connectionService.activeConnectionByState(state);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/inactive-state/{state}
     * List of Inactive Connections by State
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/inactive-state/{state}")
    public ResponseEntity<List<connectionClone>> inActiveByState(@PathVariable String state, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Inactive Connections by State");
        List<connectionClone> connectionClones = this.connectionService.inactiveConnectionByState(state);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/active-pincode/{pincode}
     * List of Active Connections by Pincode
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/active-pincode/{pincode}")
    public ResponseEntity<List<connectionClone>> activeByPincode(@PathVariable String pincode, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Active Connections by Pincode");
        List<connectionClone> connectionClones = this.connectionService.activeConnectionByPincode(pincode);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/inactive-pincode/{pincode}
     * List of Inactive Connections by Pincode
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/inactive-pincode/{pincode}")
    public ResponseEntity<List<connectionClone>> inActiveByPincode(@PathVariable String pincode, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "List of Inactive Connections by Pincode");
        List<connectionClone> connectionClones = this.connectionService.inactiveConnectionByPincode(pincode);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/connection/consumerNo/{consumerNo}
     * Search connection by Consumer No
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/consumerNo/{consumerNo}")
    public ResponseEntity<connectionClone> byConsumerNo(@PathVariable String consumerNo, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search connection by Consumer No");
        connectionClone connectionClones = this.connectionService.byConsumerNo(consumerNo);
        return new ResponseEntity<>(connectionClones, HttpStatus.OK);
    }

    /**
     * PutMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/connection/consumerNo/{consumerNo}
     * Search connection by Consumer No
     */
    @PutMapping("/connection/change-address")
    public ResponseEntity<connectionClone> changeAddress(@RequestBody connectionClone connectionClone, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Change Address for Connection");
        connectionClone updatedAddress = this.connectionService.changeAddress(connectionClone, username);
        return new ResponseEntity<>(updatedAddress, HttpStatus.ACCEPTED);
    }

    /**
     * PostMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/connection/payment-connection
     * Payment for new Connection
     */
    @PostMapping("/connection/payment-connection")
    public ResponseEntity<ebpResponse> paymentForConnection(String consumerNo, paymentClone paymentClone, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Payment for new connection");
        Boolean status = this.connectionService.forNewConnection(paymentClone, username);
        if (status) {
            return new ResponseEntity<>(new ebpResponse("Payment Done", true), HttpStatus.ACCEPTED);
        }
        else {
            throw new authorizationException("Something went wrong please try again later. Make sure to login with your Username and Password to apply for new Connection");
        }
    }

    @GetMapping("/see-connection")
    public ResponseEntity<connectionClone> seeConnection(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Payment of bill");
        connectionClone connectionClone = this.connectionService.seeConnection(username);
        return new ResponseEntity<>(connectionClone, HttpStatus.OK);
    }
}
