package com.classicmodel.practice.productservice.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="products")
public class Products implements Serializable {
	
	@Id
	@Column(name = "productcode", nullable = false)
	private String productCode;
	
	@Column(name = "productname")
	private String productName;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "productline")
	@JsonBackReference(value = "productline")
	private ProductLines productLines;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ProductLines getProductLines() {
		return productLines;
	}

	public void setProductLines(ProductLines productLines) {
		this.productLines = productLines;
	}

}
