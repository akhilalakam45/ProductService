package com.classicmodel.practice.productservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

public class PaymentsEmbeded implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int customerNumber;
	private String checkNumber;
	
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
}
