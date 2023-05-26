package com.classicmodel.practice.productservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classicmodel.practice.productservice.entity.Employees;
import com.classicmodel.practice.productservice.entity.Offices;
import com.classicmodel.practice.productservice.repository.OfficesRepository;
import com.classicmodel.practice.productservice.response.EmployeesOfOffice;
import com.classicmodel.practice.productservice.response.OfficesWithEmployees;

@Service
public class OfficesService {
	
	@Autowired
	private OfficesRepository officesRepository;

	public List<OfficesWithEmployees> getOffices() {
		
		List<Offices> offices = officesRepository.findAll();
		
		List<OfficesWithEmployees> officesWithEmployees = new ArrayList<OfficesWithEmployees>();
		
		for (Offices offices2  : offices) {
			
			OfficesWithEmployees withEmployees = new OfficesWithEmployees();
			
			withEmployees.setOfficeCode(offices2.getOfficeCode());
			
			List<EmployeesOfOffice> employeesOfOffices = new ArrayList<EmployeesOfOffice>();
			
			for (Employees employees : offices2.getEmployees()) {
				
				EmployeesOfOffice employeesOfOffice = new EmployeesOfOffice();
				employeesOfOffice.setEmployeeNumber(employees.getEmployeeNumber());
				employeesOfOffices.add(employeesOfOffice);
			}
			
			withEmployees.setEmployees(employeesOfOffices);
			officesWithEmployees.add(withEmployees);
		}
		
		
		return officesWithEmployees;
	}

}
