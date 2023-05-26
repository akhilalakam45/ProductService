package com.classicmodel.practice.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.classicmodel.practice.productservice.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{

}
