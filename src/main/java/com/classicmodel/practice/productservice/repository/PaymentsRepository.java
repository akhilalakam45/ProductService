package com.classicmodel.practice.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.classicmodel.practice.productservice.entity.Payments;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Integer> {

}
