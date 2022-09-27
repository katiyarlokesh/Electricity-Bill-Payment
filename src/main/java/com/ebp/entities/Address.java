package com.ebp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @Author rohit.parihar 9/3/2022
 * @Class connectionImplementation
 * @Project Electricity Bill Payment
 * Status COMPLETED
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @NotNull(message = "Please enter house no")
    @Min(1)
    private Integer houseNo;
    private String buildingName;
    private String landmark;
    private String village;
    private String taluka;

    @NotEmpty(message = "Enter District")
    private String district;

    @NotEmpty(message = "Enter State")
    private String state;

    @NotEmpty
    @Size(min = 6, max = 7, message = "Enter Pincode of 6 digits")
    private String pincode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate = new Date();

    @Override
    public String toString() {
        return "Address \n" +
                "\nHouse Number : " + houseNo +
                "\nBuilding Name : " + buildingName +
                "\nLandmark : " + landmark +
                "\nTown/Village : " + village +
                "\nTaluka : " + taluka +
                "\nDistrict : " + district +
                "\nState : " + state +
                "\nPincode : " + pincode;
    }
}
