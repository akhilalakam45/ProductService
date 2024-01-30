package com.classicmodel.practice.productservice.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.classicmodel.practice.productservice.service.ImageService;
import com.classicmodel.practice.productservice.vo.BulkRegistrationInputVo;
import com.classicmodel.practice.productservice.vo.BulkRegistrationListVo;
import com.classicmodel.practice.productservice.vo.BulkRegistrationVo;
import com.classicmodel.practice.productservice.vo.MassRegistrationExcelVo;
import com.classicmodel.practice.productservice.vo.SheetVo;

@RestController
@RequestMapping("/imageOrFile")
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@PostMapping("/saveImage")
	public String saveImage(@RequestParam("file") MultipartFile multipartFile, @RequestParam("name") String name, @RequestParam("description") String description) throws IOException {
		return imageService.saveImage(multipartFile, name, description);
	}

	@GetMapping(value="downloadSheet") 
	public ResponseEntity<Resource> getFile() throws ParseException { 
		String filename = "massData.xlsx"; 
		MassRegistrationExcelVo massRegistrationExcelVo=new MassRegistrationExcelVo(); 
		InputStreamResource file = new InputStreamResource(massRegistrationExcelVo.load()); 
		ResponseEntity<Resource> body = ResponseEntity.ok() .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(file); 
		return body;
		}
	
	@PostMapping(value="validateFile") 
	public ResponseEntity<List<BulkRegistrationVo>> validateFile(@RequestParam("file") MultipartFile file) throws IOException { 
		 
		List<BulkRegistrationVo> bulkRegistrationVos = imageService.validateFileThroughExcel(file); 
		return (ResponseEntity<List<BulkRegistrationVo>>) bulkRegistrationVos; 
		}
	
	@PostMapping(value="validateString") 
	public ResponseEntity<BulkRegistrationListVo> validateFileThroughString(@RequestBody BulkRegistrationInputVo bulkRegistrationInputVo) throws IOException { 
		 
		BulkRegistrationListVo bulkRegistrationVos = imageService.validateFileThroughString(bulkRegistrationInputVo); 
		return new ResponseEntity<>(bulkRegistrationVos, org.springframework.http.HttpStatus.OK); 
		}
	
	@PostMapping(value="staticdSheet") 
	public void getStaticSheet(@RequestBody SheetVo SheetVo ) throws ParseException, IOException { 
		String filename = "massData.xlsx"; 
		imageService.getStaticSheet(SheetVo);
		}
}
