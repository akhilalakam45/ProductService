package com.classicmodel.practice.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classicmodel.practice.productservice.entity.Products;
import com.classicmodel.practice.productservice.repository.ProductsRepository;

@Service
public class ProductsService {

	
	@Autowired
	private ProductsRepository productsRepository;
	
	public List<Products> getProducts() {
		return productsRepository.findAll();
	}

}
