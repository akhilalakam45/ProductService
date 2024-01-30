package com.classicmodel.practice.productservice.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@IdClass(PaymentsEmbeded.class)
public class Payments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="customernumber")
	private int customerNumber;
	
	@Id
	@Column(name="checknumber")
	private String checkNumber;
	
	@Column(name="paymentdate")
	private Date paymentDate;
	
	private Double amount;
	
	public int getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getCheckNumber() {
		return checkNumber;
	}
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
