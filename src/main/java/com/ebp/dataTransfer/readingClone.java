package com.ebp.dataTransfer;

import com.ebp.entities.Connection;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

/**
 * @Author rohit.parihar 9/4/2022
 * @Class readingClone
 * @Project Electricity Bill Payment
 */

@Getter
@Setter
@NoArgsConstructor
public class readingClone {
    private Long readingId;

    private Integer unitsConsumed;
    private String readingPhoto;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate readingDate;
    private Integer pricePerUnit;
    private LocalDate imageUploadDate;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate dateEdited;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Connection connection;
}
