package com.classicmodel.practice.productservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classicmodel.practice.productservice.entity.Employees;
import com.classicmodel.practice.productservice.repository.EmployeeRepository;
import com.classicmodel.practice.productservice.vo.response.EmployeesResponseVo;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	public List<EmployeesResponseVo> getEmployees() {
		
		List<Employees> employees = employeeRepository.findAll();
		
		List<EmployeesResponseVo> employeesResponseVos = new ArrayList<EmployeesResponseVo>();
		
		for (Employees employee : employees) {
			EmployeesResponseVo employeesResponseVo = new EmployeesResponseVo();
			employeesResponseVo.setEmployeeNumber(employee.getEmployeeNumber());
			employeesResponseVo.setEmail(employee.getEmail());
			employeesResponseVo.setExtension(employee.getExtension());
			employeesResponseVo.setFirstName(employee.getFirstName());
			employeesResponseVo.setLastName(employee.getLastName());
			employeesResponseVo.setJobTitle(employee.getJobTitle());
			employeesResponseVo.setReportsTo(employee.getReportsTo());
			employeesResponseVos.add(employeesResponseVo);
		}
		
		return employeesResponseVos;
	}

}
