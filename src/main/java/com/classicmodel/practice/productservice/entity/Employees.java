package com.classicmodel.practice.productservice.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Columns;
import org.springframework.lang.Nullable;

@Entity
public class Employees implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="employeenumber")
	private int employeeNumber;
	
	@Column(name="lastname")
	private String lastName;
	
	@Column(name="firstname")
	private String firstName;
	
	@Column(name="extension")
	private String extension;
	
	@Column(name="email")
	private String email; 
	
	@Column(name="reportsto")
	private Integer reportsTo;
	
	@Column(name="jobtitle")
	private String jobTitle;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "officecode")
	private Offices office;
	
	
	public Offices getOffice() {
		return office;
	}
	public void setOffice(Offices office) {
		this.office = office;
	}
	public int getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getReportsTo() {
		return reportsTo;
	}
	public void setReportsTo(Integer reportsTo) {
		this.reportsTo = reportsTo;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
}
