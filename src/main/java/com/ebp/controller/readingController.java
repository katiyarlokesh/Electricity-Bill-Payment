package com.ebp.controller;

import com.ebp.authorizations.accessedData;
import com.ebp.dataTransfer.readingClone;
import com.ebp.entities.Connection;
import com.ebp.entities.Reading;
import com.ebp.exceptionHandler.detailsNotAvailableException;
import com.ebp.helper.ebpResponse;
import com.ebp.helper.readingResponse;
import com.ebp.repository.connectionRepository;
import com.ebp.repository.readingRepository;
import com.ebp.security.JWTTokenHelper;
import com.ebp.service.imageService;
import com.ebp.service.readingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author rohit.parihar 9/5/2022
 * @Class readingController
 * @Project Electricity Bill Payment
 */

@RestController
@RequestMapping("/ebp")
public class readingController {

    @Autowired
    private readingService readingService;

    @Autowired
    private imageService imageService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private accessedData accessedData;

    @Autowired
    private connectionRepository connectionRepository;

    @Autowired
    private readingRepository readingRepository;

    /**
     * PostMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/connection/submit-reading
     * Submit Reading
     */
    @PostMapping("/connection/submit-reading")
    public ResponseEntity<ebpResponse> selfSubmitReading(@Valid @RequestBody readingClone readingClone, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Submit Reading for Email Id : " + username);
        readingClone reading = this.readingService.selfSubmitReading(readingClone, username);
        return new ResponseEntity<>(new ebpResponse("Reading saved Successfully at " + LocalDate.now() + "Please Upload meter Image at http://localhost:8080/reading/submit-reading/upload-image", true), HttpStatus.ACCEPTED);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/reading/{readingId}
     * Find Reading by Reading Id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reading/{readingId}")
    public ResponseEntity<readingClone> findbyId(@PathVariable Long readingId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search Reading by Id : " + readingId);
        readingClone reading = this.readingService.findById(readingId);
        return ResponseEntity.ok(reading);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/reading/all-reading
     * Get all Readings
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reading/all-reading")
    public  ResponseEntity<readingResponse> getAllReading(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                          HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Get all Readings");
        readingResponse allReading = this.readingService.getAllReading(pageNumber, pageSize);
        return new ResponseEntity<>(allReading, HttpStatus.OK);
    }

    /**
     * PutMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/reading/edit-reading/{readingId}
     * Edit Reading
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reading/edit-reading/{readingId}")
    public ResponseEntity<readingClone> editReading(@PathVariable Long readingId, @RequestBody readingClone readingClone, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Edit Reading for Reading Id : " + readingId);
        readingClone reading = this.readingService.editReading(readingId, readingClone);
        return ResponseEntity.ok(reading);
    }

    /**
     * DeleteMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/reading/delete-reading/{readingId}
     * Delete Reading
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/reading/delete-reading/{readingId}")
    public ResponseEntity<?> deleteReading(@PathVariable Long readingId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Delete Reading for Reading Id : " + readingId);
        this.readingService.deleteReading(readingId);
        return new ResponseEntity<>(new ebpResponse("Deleted Successfully", true), HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/search/connection/{consumerNo}/reading/{date}
     * Search Reading by Consumer No and Date (YYYY-MM-DD)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/connection/{consumerNo}/reading/{date}")
    public ResponseEntity<readingClone> byConsumerNoAndDate(@PathVariable String consumerNo, @PathVariable String date, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search Reading by Consumer No : " + consumerNo + " and Date : " + date);
        readingClone readingClone = this.readingService.byConsumerNoAndDate(consumerNo, date);
        return new ResponseEntity<>(readingClone, HttpStatus.OK);
    }

    /**
     * GetMapping || Authorized || ADMIN
     * URI -> http://localhost:8080/ebp/search/connection/consumerNo/{consumerNo}/reading
     * Search Reading by Consumer No.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/connection/consumerNo/{consumerNo}/reading")
    public ResponseEntity<List<readingClone>> byConsumerNo(@PathVariable String consumerNo, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "Search Reading by Consumer No. : " + consumerNo);
        List<readingClone> readingClones = this.readingService.byConsumerNo(consumerNo);
        return new ResponseEntity<>(readingClones, HttpStatus.OK);
    }

    /**
     * PutMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/reading/image/insert/{readingId}
     * Upload Image for Reading
     */
    @PutMapping("/reading/image/insert/{readingId}")
    public ResponseEntity<readingClone> readingImage(@RequestParam MultipartFile image, @PathVariable Long readingId) throws IOException {
        readingClone reading = this.readingService.findById(readingId);
        String imageName = this.imageService.readingImage(path, image);
        reading.setReadingPhoto(imageName);
        readingClone readingClone = this.readingService.editReading(readingId, reading);
        return new ResponseEntity<>(readingClone, HttpStatus.CREATED);
    }

    /**
     * GetMapping || Authorized || All Role
     * URI -> http://localhost:8080/ebp/reading/submit-reading/download-image
     * Upload Image for Reading
     */
    @GetMapping("/reading/submit-reading/download-image/{readingId}")
    public ResponseEntity<?> getImage(@PathVariable Long readingId, HttpServletResponse imageResponse) throws IOException {
        readingClone readingClone = this.readingService.findById(readingId);
        String imageName = readingClone.getReadingPhoto();
        InputStream readingImage = this.imageService.getReadingImage(path, imageName);
        imageResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(readingImage, imageResponse.getOutputStream());
        return ResponseEntity.ok(readingImage);
    }

    @GetMapping("/all-readings")
    public ResponseEntity<List<readingClone>> allReadings(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String realToken = token.substring(7);
        String username = this.jwtTokenHelper.getUsernameFromToken(realToken);
        this.accessedData.saveAccessedData(username, "All Readings of : " + username);
        List<readingClone> readingClones = this.readingService.seeReadings(username);
        return new ResponseEntity<>(readingClones, HttpStatus.OK);
    }
}
