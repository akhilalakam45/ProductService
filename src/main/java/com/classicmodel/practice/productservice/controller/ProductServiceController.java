package com.classicmodel.practice.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classicmodel.practice.productservice.entity.Employees;
import com.classicmodel.practice.productservice.entity.Offices;
import com.classicmodel.practice.productservice.entity.ProductLines;
import com.classicmodel.practice.productservice.entity.Products;
import com.classicmodel.practice.productservice.response.OfficesWithEmployees;
import com.classicmodel.practice.productservice.service.EmployeeService;
import com.classicmodel.practice.productservice.service.OfficesService;
import com.classicmodel.practice.productservice.service.ProductLinesService;
import com.classicmodel.practice.productservice.service.ProductsService;

@RestController
@RequestMapping("/productService")
public class ProductServiceController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private OfficesService officesService;
	
	@Autowired
	private ProductLinesService productLinesService;
	
	@Autowired
	private ProductsService productsService;
	
	@GetMapping("getEmployees")
	public List<Employees> getEmployees() {
		return employeeService.getEmployees();
	}
	
	
	@GetMapping("getOffices")
	public List<OfficesWithEmployees> getOffices() {
		return officesService.getOffices();
	}
	
	@GetMapping("getProductLines")
	public List<ProductLines> getProductLines() {
		return productLinesService.getProductLines();
	}
	
	@GetMapping("getProducts")
	public List<Products> getProducts() {
		return productsService.getProducts();
	}
	

}
