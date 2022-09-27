package com.ebp.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	private String transactionId;
	private LocalDate PaymentDate;
	private Double LatePaymentCharges;
	private Double TotalPaid;
	private Boolean Status;
	
	@OneToOne
	private Bill bill;
	@Enumerated(EnumType.STRING)
	private paymentMode paymentMode;

}
