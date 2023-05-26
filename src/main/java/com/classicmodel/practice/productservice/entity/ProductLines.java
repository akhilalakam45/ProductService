package com.classicmodel.practice.productservice.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name ="productlines")
public class ProductLines {
	
	@Id
	@Column(name = "productline", nullable = false)
	private String productLine;
	
	@Column(name="textdescription")
	private String textDescription;
	
	@OneToMany(mappedBy = "productLines", fetch = FetchType.LAZY)
	@JsonManagedReference(value = "products")
	private List<Products> products;

	public String getProductLine() {
		return productLine;
	}

	public List<Products> getProducts() {
		return products;
	}

	public void setProducts(List<Products> products) {
		this.products = products;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public String getTextDescription() {
		return textDescription;
	}

	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
	}
}
