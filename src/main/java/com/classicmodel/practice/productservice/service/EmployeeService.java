package com.classicmodel.practice.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classicmodel.practice.productservice.entity.Employees;
import com.classicmodel.practice.productservice.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	public List<Employees> getEmployees() {
		return employeeRepository.findAll();
	}

}
