package com.classicmodel.practice.productservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classicmodel.practice.productservice.entity.Employees;
import com.classicmodel.practice.productservice.entity.Offices;
import com.classicmodel.practice.productservice.repository.OfficesRepository;
import com.classicmodel.practice.productservice.vo.response.EmployeesResponseVo;
import com.classicmodel.practice.productservice.vo.response.OfficesResponseVo;

@Service
public class OfficesService {
	
	@Autowired
	private OfficesRepository officesRepository;

	public List<OfficesResponseVo> getOffices() {
		
		List<Offices> offices = officesRepository.findAll();
		List<OfficesResponseVo> officesResponseVos = new ArrayList<OfficesResponseVo>();
		
		for (Offices office  : offices) {
			
			OfficesResponseVo officesResponseVo = new OfficesResponseVo();
			officesResponseVo.setOfficeCode(office.getOfficeCode());
			officesResponseVo.setAddressLine1(office.getAddressLine1());
			officesResponseVo.setAddressLine2(office.getAddressLine2());
			officesResponseVo.setCity(office.getCity());
			officesResponseVo.setCountry(office.getCountry());
			officesResponseVo.setPhone(office.getPhone());
			officesResponseVo.setPostalCode(office.getPostalCode());
			officesResponseVo.setState(office.getState());
			officesResponseVo.setTerritory(office.getTerritory());
			
			List<EmployeesResponseVo> employeesResponseVos = new ArrayList<EmployeesResponseVo>();
			
			for (Employees employee : office.getEmployees()) {
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
			
			officesResponseVo.setEmployees(employeesResponseVos);
			officesResponseVos.add(officesResponseVo);
		}
		return officesResponseVos;
	}

}
