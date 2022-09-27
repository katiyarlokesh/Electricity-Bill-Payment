package com.ebp.dataTransfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

@Getter
@Setter
@NoArgsConstructor
public class customerClone {

    @NotEmpty(message = "Please Enter your Name")
    @Size(max = 20)
    private String firstName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String middleName;

    @NotEmpty
    @Size(max = 20)
    private String lastName;

    @NotEmpty
    @Size(min = 12, message = "Please Enter valid Aadhar Number")
    private String aadharNumber;

    private String email;

    @NotEmpty
    @Size(min = 10)
    private String mobileNumber;

    @NotEmpty
    private String gender;

    private LocalDate dateCreated;

    private LocalDate dateEdited;

    private String name = firstName + " " + middleName + " " + lastName;
}
