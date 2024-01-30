package com.classicmodel.practice.productservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classicmodel.practice.productservice.entity.Payments;
import com.classicmodel.practice.productservice.entity.ProductLines;
import com.classicmodel.practice.productservice.entity.Products;
import com.classicmodel.practice.productservice.service.EmployeeService;
import com.classicmodel.practice.productservice.service.OfficesService;
import com.classicmodel.practice.productservice.service.PaymentsService;
import com.classicmodel.practice.productservice.service.ProductLinesService;
import com.classicmodel.practice.productservice.service.ProductsService;
import com.classicmodel.practice.productservice.vo.response.EmployeesResponseVo;
import com.classicmodel.practice.productservice.vo.response.OfficesResponseVo;

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
	
	@Autowired
	private PaymentsService paymentsService;
	
	@GetMapping("getEmployees")
	public List<EmployeesResponseVo> getEmployees() {
		return employeeService.getEmployees();
	}
	
	
	@GetMapping("getOffices")
	public List<OfficesResponseVo> getOffices() {
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
	
	@GetMapping("getPayments")
	public List<Payments> getPayments() {
		return paymentsService.getPayments();
	}
	

}
