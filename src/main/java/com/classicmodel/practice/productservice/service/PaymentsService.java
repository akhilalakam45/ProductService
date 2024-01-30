package com.classicmodel.practice.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classicmodel.practice.productservice.entity.Payments;
import com.classicmodel.practice.productservice.repository.PaymentsRepository;

@Service
public class PaymentsService {
	
	@Autowired
	private PaymentsRepository paymentsRepository;

	public List<Payments> getPayments() {
		return paymentsRepository.findAll();
	}
	

}
