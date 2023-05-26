package com.classicmodel.practice.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classicmodel.practice.productservice.entity.ProductLines;
import com.classicmodel.practice.productservice.repository.ProductLinesRepository;

@Service
public class ProductLinesService {

	
	@Autowired
	private ProductLinesRepository productLinesRepository;
	
	
	public List<ProductLines> getProductLines() {
		return productLinesRepository.findAll();
	}

}
