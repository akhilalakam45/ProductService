package com.classicmodel.practice.productservice.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.classicmodel.practice.productservice.entity.Image;
import com.classicmodel.practice.productservice.repository.ImageRepository;

@Service
public class ImageService {
	
	@Autowired
	private ImageRepository imageRepository;
	
	public String saveImage(MultipartFile multipartFile, String name, String description) throws IOException {
		Image image = new Image();
		image.setName(name);
		image.setDescription(description);
		image.setImage(multipartFile.getBytes());
		imageRepository.save(image);
		return "Image saved successfully";
	}

}
