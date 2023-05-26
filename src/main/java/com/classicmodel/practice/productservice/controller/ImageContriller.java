package com.classicmodel.practice.productservice.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.classicmodel.practice.productservice.service.ImageService;

@RestController
@RequestMapping("/imageOrFile")
public class ImageContriller {
	
	@Autowired
	private ImageService imageService;
	
	@PostMapping("/saveImage")
	public String saveImage(@RequestParam("file") MultipartFile multipartFile, @RequestParam("name") String name, @RequestParam("description") String description) throws IOException {
		return imageService.saveImage(multipartFile, name, description);
	}

}
