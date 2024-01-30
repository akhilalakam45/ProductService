package com.classicmodel.practice.productservice.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.classicmodel.practice.productservice.entity.Image;
import com.classicmodel.practice.productservice.repository.ImageRepository;
import com.classicmodel.practice.productservice.vo.BulkRegistrationInputVo;
import com.classicmodel.practice.productservice.vo.BulkRegistrationListVo;
import com.classicmodel.practice.productservice.vo.BulkRegistrationVo;
import com.classicmodel.practice.productservice.vo.MassRegistrationExcelVo;
import com.classicmodel.practice.productservice.vo.SheetVo;

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

	public BulkRegistrationListVo validateFileThroughString(BulkRegistrationInputVo bulkRegistrationInputVo) {
		
		// Decode base64 string to byte array and convert to input stream 
		byte[] decoded = Base64.getDecoder().decode(bulkRegistrationInputVo.getUploadFile()); 
		InputStream is = new ByteArrayInputStream(decoded); 
		
		// Convert input stream to list 
		BulkRegistrationListVo bulkRegistrationVos = excelToTrainingDto(is); 
		return bulkRegistrationVos; 
	} 
	
	/*private List<BulkRegistrationVo> excelToTrainingDto(InputStream is) { 
		try { 
			Workbook workbook = new XSSFWorkbook(is); 
			Sheet sheet = workbook.getSheetAt(0); 
			Iterator<Row> rows = sheet.iterator(); 
			List<BulkRegistrationVo> bulkRegistrationVos = new ArrayList<BulkRegistrationVo>(); 
			int rowNumber = 0; 
			while (rows.hasNext()) { 
				Row currentRow = rows.next(); 
				
				// skip header 
				if (rowNumber == 0) { 
					rowNumber++; 
					continue; 
				} 
				
				if(!isRowEmpty(currentRow)) {
				
				Iterator<Cell> cellsInRow = currentRow.iterator(); 
				BulkRegistrationVo bulkRegistrationVo = new BulkRegistrationVo(); 
				int cellIdx = 0; 
				DataFormatter formatter = new DataFormatter(); 
				while (cellsInRow.hasNext()) { 
					Cell currentCell = cellsInRow.next(); 
					switch (cellIdx) { 
					case 0: bulkRegistrationVo.setNRIC(currentCell.getStringCellValue()); 
					break;
					case 1: bulkRegistrationVo.setFullName(currentCell.getStringCellValue()); 
					break;
					case 2: 
						if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
							Long number = ((Double)currentCell.getNumericCellValue()).longValue();
								if(isValidMobileNo(number.toString())) {
									bulkRegistrationVo.setMobileNumber("a"+number.toString());
								}else {
									bulkRegistrationVo.setMobileNumber("a1"+number.toString());
									setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[2]);
								}
						}else if(currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
							if(isValidMobileNo(currentCell.getStringCellValue())) {
								bulkRegistrationVo.setMobileNumber("s1"+currentCell.getStringCellValue());
							}else {
								bulkRegistrationVo.setMobileNumber("s2"+currentCell.getStringCellValue());
								setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[2]);
							}
						}
					break;
					case 3: 
						if(currentCell.getCellType() == CellType.NUMERIC) {
							bulkRegistrationVo.setResidentialNumber(((Double)currentCell.getNumericCellValue()).longValue()); 
						}else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[3]);
						}
						
					break;
				
				  case 4: 
						 if(currentCell.getCellType() == CellType.NUMERIC) {
							 bulkRegistrationVo.setDate(((Double)currentCell.getNumericCellValue()).toString());
						  }else {
							  setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[4]);
						  }
					  break;
				 
					case 5:
						bulkRegistrationVo.setIntake(currentCell.getStringCellValue()); 
						break;
					
					case 6: 
						bulkRegistrationVo.setHighestStandardAttended(currentCell.getStringCellValue()); 
						break;
						
					case 7:
						if(currentCell.getCellType() == CellType.NUMERIC) {
							bulkRegistrationVo.setNOK_NRIC(((Double)currentCell.getNumericCellValue()).longValue());
						} else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[7]);
						}
					break;
					
					case 8: bulkRegistrationVo.setNAPFATestResult(currentCell.getStringCellValue()); 
					break;
					case 9: bulkRegistrationVo.setPES(currentCell.getStringCellValue()); 
					break;
					case 10: bulkRegistrationVo.setColourPerception(currentCell.getStringCellValue()); 
					break;
					
					case 11: 
						if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null ) {
							bulkRegistrationVo.setPULHEEMS_P(((Double)currentCell.getNumericCellValue()).longValue()); 
						}else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[11]);
						}
						
					break;
					case 12: 
						if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
							bulkRegistrationVo.setPULHEEMS_U(((Double)currentCell.getNumericCellValue()).longValue());
						}else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[12]);
						}	 
					break;
					
					case 13: 
						if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
							bulkRegistrationVo.setPULHEEMS_L(((Double)currentCell.getNumericCellValue()).longValue()); 
						}else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[13]);
						}	
					break;
					
					case 14: 
						if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
							bulkRegistrationVo.setPULHEEMS_H(((Double)currentCell.getNumericCellValue()).longValue()); 
						}else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[14]);
						}
						break;
						
					case 15: bulkRegistrationVo.setPULHEEMS_ER(currentCell.getStringCellValue()); 
					break;
					case 16: bulkRegistrationVo.setPULHEEMS_EL(currentCell.getStringCellValue()); 
					break;
					case 17: 
						if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
							bulkRegistrationVo.setPULHEEMS_M(((Double)currentCell.getNumericCellValue()).longValue()); 
						}else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[17]);
						}
					break;
					
					case 18: 
						if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
							bulkRegistrationVo.setPULHEEMS_S(((Double)currentCell.getNumericCellValue()).longValue()); 
						}else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[18]);
						}
					break;
					
					case 19: bulkRegistrationVo.setNationality(currentCell.getStringCellValue()); 
					break;
					case 20: 
						if(currentCell.getCellType() == CellType.NUMERIC) {
							bulkRegistrationVo.setORD(((Double)currentCell.getNumericCellValue()).longValue()); 
						}else {
							setStatusAndErrorDescription(bulkRegistrationVo, rowNumber, cellIdx, MassRegistrationExcelVo.HEADERs[20]);
						}	
					break;
					
					case 21: bulkRegistrationVo.setExcusesDate(currentCell.getStringCellValue()); 
					break;
					case 22: bulkRegistrationVo.setExcuses(currentCell.getStringCellValue()); 
					break;
					default: 
						break; 
						} 
					cellIdx++; 
					}
				rowNumber++;
				bulkRegistrationVos.add(bulkRegistrationVo); 
				}
				} 
			workbook.close(); 
			return bulkRegistrationVos; 
			}catch (IOException e) { 
				throw new RuntimeException("fail to parse Excel file: " + e.getMessage()); 
			} 
		} */
	
	
  private BulkRegistrationListVo excelToTrainingDto(InputStream is) {
        
        try {
                Workbook workbook = new XSSFWorkbook(is);
    
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rows = sheet.iterator();
    
                BulkRegistrationListVo bulkRegistrationListVo = new BulkRegistrationListVo();
                List<BulkRegistrationVo> validBulkRegistrationVos = new ArrayList<BulkRegistrationVo>();
                List<BulkRegistrationVo> invalidBulkRegistrationVos = new ArrayList<BulkRegistrationVo>();
    
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
    
                    // skip header
                    if (rowNumber == 0) {
                        rowNumber++;
                        continue;
                    }
                    
                    Iterator<Cell> cellsInRow = currentRow.iterator();

                    BulkRegistrationVo bulkRegistrationVo = new BulkRegistrationVo();
                    List<String> statusDescription = new ArrayList<String>();
                    bulkRegistrationVo.setStatus("Valid");

                    int cellIdx = 0;
                    DataFormatter formatter = new DataFormatter();
                    
                    sheet.setRowBreak(501);
                    
                    System.out.println(currentRow.getLastCellNum());
                    
                    while (currentRow.getLastCellNum() > cellIdx) {
                        //Cell currentCell = cellsInRow.next();
                    	Cell currentCell = currentRow.getCell(cellIdx);
                        //cellIdx = currentCell.getColumnIndex();
                        
                        switch (cellIdx) {

                        case 0:
                            
                            if (currentCell == null || currentCell.getCellType() == CellType.BLANK) {
                                bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[0]);
                            }else {
                                if(currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                	if(validationOfString(currentCell.getStringCellValue())) {
                                		bulkRegistrationVo.setNric(currentCell.getStringCellValue());
                                	}else {
                                		bulkRegistrationVo.setNric(currentCell.getStringCellValue());
                                		bulkRegistrationVo.setStatus("Invalid");
                                		statusDescription.add(MassRegistrationExcelVo.HEADERs[0]);
                                	}
                                    
                                }else if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                    Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                    bulkRegistrationVo.setNric(number.toString());
                                    bulkRegistrationVo.setStatus("Invalid");
                                    //statusDescription.add("Enter Valid For "+ MassRegistrationExcelVo.HEADERs[0] +" At RowNumber: "+(rowNumber+1)+" and CellNumber: "+ (cellIdx+1));
                                    statusDescription.add(MassRegistrationExcelVo.HEADERs[0]);
                                }
                            }
                            break;
                            
                        case 1:
                            if (currentCell == null || currentCell.getCellType() == CellType.BLANK) {
                                bulkRegistrationVo.setStatus("Invalid");
                            }else {
                                if(currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                	
                                	if(checkSizeOfString(currentCell.getStringCellValue())) {
                                		bulkRegistrationVo.setFirstName(currentCell.getStringCellValue());
                                	}else {
                                		bulkRegistrationVo.setFirstName(currentCell.getStringCellValue());
                                		bulkRegistrationVo.setStatus("Invalid");
                                        statusDescription.add(MassRegistrationExcelVo.HEADERs[1]);
                                	}
                                    
                                }else if(currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                    Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                    bulkRegistrationVo.setFirstName(number.toString());
                                    bulkRegistrationVo.setStatus("Invalid");
                                    statusDescription.add(MassRegistrationExcelVo.HEADERs[1]);
                                }
                            }
                            break;
                            
                        case 2:
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                            	if(checkSizeOfString(currentCell.getStringCellValue())) {  
                            		bulkRegistrationVo.setLastName(currentCell.getStringCellValue());
	                            }else {
	                        		bulkRegistrationVo.setLastName(currentCell.getStringCellValue());
	                        		bulkRegistrationVo.setStatus("Invalid");
	                                statusDescription.add(MassRegistrationExcelVo.HEADERs[2]);
	                        	}
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setLastName(number.toString());
                                bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[2]);
                            }
                            break;
                            
                        case 3: 
                            //Mobile Number 
                            if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                    if(isValidMobileNo(number.toString())) {
                                        bulkRegistrationVo.setMobileNumber(number.toString());
                                    }else {
                                        bulkRegistrationVo.setMobileNumber(number.toString());
                                        bulkRegistrationVo.setStatus("Invalid");
                                        statusDescription.add(MassRegistrationExcelVo.HEADERs[3]);
                                    }
                            }else if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                if(isValidMobileNo(currentCell.getStringCellValue())) {
                                    bulkRegistrationVo.setMobileNumber(currentCell.getStringCellValue());
                                }else {
                                    bulkRegistrationVo.setMobileNumber(currentCell.getStringCellValue());
                                    bulkRegistrationVo.setStatus("Invalid");
                                    statusDescription.add(MassRegistrationExcelVo.HEADERs[3]);
                                }
                            }
                            break;
                            
                        case 4: 
                            //Residential Number 
                            if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setResidentialNumber(number.toString()); 
                            }else if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setResidentialNumber(currentCell.getStringCellValue());
                                bulkRegistrationVo.setStatus("Invalid");
                                //statusDescription.add("Enter Valid For "+ MassRegistrationExcelVo.HEADERs[6] +" At RowNumber: "+(rowNumber+1)+" and CellNumber: "+ (cellIdx+1));
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[4]);
                            }   
                        break;
                        
                        case 5: 
                            //Enlistment Date 
                            if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                 //bulkRegistrationVo.setEnlistmentDate(((Double)currentCell.getNumericCellValue()).toString());
                                 
                                 DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                 Date date = currentCell.getDateCellValue();
                                 String cellValue = df.format(date);
                                 bulkRegistrationVo.setEnlistmentDate(cellValue);
                                 
                              }else if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                //bulkRegistrationVo.setEnlistmentDate(currentCell.getStringCellValue());
                                
                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = currentCell.getDateCellValue();
                                String cellValue = df.format(date);
                                bulkRegistrationVo.setEnlistmentDate(cellValue);
                                
                                //bulkRegistrationVo.setStatus("Invalid");
                                //statusDescription.add(MassRegistrationExcelVo.HEADERs[5]);
                              }
                          break;
                          
                        case 6: 
                            //Intake
                            if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setIntake(number.toString()); 
                            }else if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setIntake(currentCell.getStringCellValue());
                                
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                //statusDescription.add("Enter Valid For "+ MassRegistrationExcelVo.HEADERs[6] +" At RowNumber: "+(rowNumber+1)+" and CellNumber: "+ (cellIdx+1));
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[6]);
                            }
                        break;
                         
                        case 7:
                            //Highest Standard Attended 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setHighestStandardAttended(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setHighestStandardAttended(number.toString());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[7]);
                            }
                            break;
                            
                        case 8:
                            //NOK NRIC 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                            	if(validationOfString(currentCell.getStringCellValue())) {
                            		bulkRegistrationVo.setNok_nric(currentCell.getStringCellValue());
                            	}else {
                            		bulkRegistrationVo.setNok_nric(currentCell.getStringCellValue());
                            		bulkRegistrationVo.setStatus("Invalid");
                            		statusDescription.add(MassRegistrationExcelVo.HEADERs[8]);
                            	}
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setNok_nric(number.toString());
                                bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[8]);
                            }
                            break;
                            
                        case 9:
                            //NAPFA Test Result 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setNapfaTestResult(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setNapfaTestResult(number.toString()); 
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[9]);
                            }
                            break;
                                
                        case 10:
                            //PES 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPes(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPes(number.toString());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[10]);
                            }
                            break;
                            
                        case 11:
                            //Color Perception 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setColourPerception(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setColourPerception(number.toString());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[11]);
                            }
                            break;
                            
                        case 12:
                            //PULHEEMS (P) 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPulheems_P(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPulheems_P(number.toString());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[12]);
                            }
                            break;
                            
                        case 13:
                            //PULHEEMS (U) 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPulheems_U(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPulheems_U(number.toString());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[13]);
                            }
                            break;
                            
                        case 14:
                            //PULHEEMS (L) 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPulheems_L(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPulheems_L(number.toString());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[14]);
                            }
                            break;
                            
                        case 15:
                            //PULHEEMS (H) 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPulheems_H(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPulheems_H(number.toString());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[15]);
                            }
                            break;
                            
                        case 16:
                            //PULHEEMS (ER) 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPulheems_ER(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPulheems_ER(number.toString());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[16]);
                            }
                            break;
                            
                        case 17:
                            //PULHEEMS (EL) 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPulheems_EL(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPulheems_EL(number.toString());
                                
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[17]);
                            }
                            break;
                            
                        case 18:
                            //PULHEEMS (M) 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPulheems_M(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPulheems_M(number.toString());
                                
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[18]);
                            }
                            break;
                            
                        case 19:
                            //PULHEEMS (S) 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setPulheems_S(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setPulheems_S(number.toString());
                                
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[19]);
                            }
                            break;
                        
                        case 20:
                            //Nationality 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setNationality(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setNationality(number.toString());
                                
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[20]);
                            }
                            break;
                            
                        case 21:
                            //ORD  
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setOrd(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setOrd(number.toString());
                                
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[21]);
                            }
                            break;
                            
                        case 22:
                            //Excuses 
                            if(currentCell != null && currentCell.getCellType() == CellType.STRING && currentCell.getStringCellValue() != null) {
                                bulkRegistrationVo.setExcuses(currentCell.getStringCellValue());
                            }else if(currentCell != null && currentCell.getCellType() == CellType.NUMERIC && (Double)currentCell.getNumericCellValue() != null) {
                                Long number = ((Double)currentCell.getNumericCellValue()).longValue();
                                bulkRegistrationVo.setExcuses(number.toString());
                                
                            }else if(currentCell != null && currentCell.getCellType() == CellType.BOOLEAN) {
                            	bulkRegistrationVo.setStatus("Invalid");
                                statusDescription.add(MassRegistrationExcelVo.HEADERs[22]);
                            }
                            break;
                        
                        default:
                            break;
                        }
                            cellIdx++;
                        }
                    
                    bulkRegistrationVo.setStatusDescription(statusDescription);
                        
                    if(bulkRegistrationVo.getStatus().equalsIgnoreCase("Valid")){
                        validBulkRegistrationVos.add(bulkRegistrationVo);
                        bulkRegistrationListVo.setValidBulkRegistrationVos(validBulkRegistrationVos);
                    }else {
                        invalidBulkRegistrationVos.add(bulkRegistrationVo);
                        bulkRegistrationListVo.setInvalidBulkRegistrationVos(invalidBulkRegistrationVos);
                    }
                }
                workbook.close();
                return bulkRegistrationListVo;
            }catch (IOException e) {
                throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
            }
    }
	
	private void setStatusAndDescription(BulkRegistrationVo bulkRegistrationVo, int rowNumber, int cellId, String fieldName) {
		
		bulkRegistrationVo.setStatus("Invalid");
		//bulkRegistrationVo.setStatusDescription("Enter Valid For "+ fieldName +" At RowNumber: "+(rowNumber+1)+" and CellNumber: "+ (cellId+1));
		
	}
	
	private boolean isRowEmpty(Row row) {
	    boolean isEmpty = true;
	    DataFormatter dataFormatter = new DataFormatter();
	    if(row != null) {
	        for(Cell cell: row) {
	            if(dataFormatter.formatCellValue(cell).trim().length() > 0) {
	                isEmpty = false;
	                break;
	            }
	        }
	    }
	    return isEmpty;
	}
	
	private static boolean isValidMobileNo(String str)  
	{  
		Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");  
		Matcher match = ptrn.matcher(str);  
		return (match.find() && match.group().equals(str));  
	}  
	

	public List<BulkRegistrationVo> validateFileThroughExcel(MultipartFile file) throws IOException {
		List<BulkRegistrationVo> bulkRegistrationVos = null ; //excelToTrainingDto(file.getInputStream()); 
		return bulkRegistrationVos; 
	}
	
	
	public void checkSingleLineData(BulkRegistrationVo bulkRegistrationVo) {
		
		if(bulkRegistrationVo != null) {
			
			List<String> statusDescription = new ArrayList<String>();
			bulkRegistrationVo.setStatus("Valid");
			
			if(bulkRegistrationVo.getNric() != null && !validationOfString(bulkRegistrationVo.getNric())) {
				bulkRegistrationVo.setStatus("Invalid");
				statusDescription.add(MassRegistrationExcelVo.HEADERs[0]);
			}
			
			if(bulkRegistrationVo.getFirstName() != null && checkSizeOfString(bulkRegistrationVo.getFirstName())) {
				bulkRegistrationVo.setStatus("Invalid");
				statusDescription.add(MassRegistrationExcelVo.HEADERs[1]);
			}
			
			if(bulkRegistrationVo.getLastName() != null && checkSizeOfString(bulkRegistrationVo.getLastName())) {
				bulkRegistrationVo.setStatus("Invalid");
				statusDescription.add(MassRegistrationExcelVo.HEADERs[2]);
			}
			
			if(bulkRegistrationVo.getMobileNumber() !=null ) {
				if(!isNumeric(bulkRegistrationVo.getMobileNumber())) {
					bulkRegistrationVo.setStatus("Invalid");
					statusDescription.add(MassRegistrationExcelVo.HEADERs[3]);
				}else if(!isValidMobileNo(bulkRegistrationVo.getMobileNumber())){
					bulkRegistrationVo.setStatus("Invalid");
					statusDescription.add(MassRegistrationExcelVo.HEADERs[3]);
				}
			}
			
			if(bulkRegistrationVo.getResidentialNumber() !=null ) {
				if(!isNumeric(bulkRegistrationVo.getResidentialNumber())) {
					bulkRegistrationVo.setStatus("Invalid");
					statusDescription.add(MassRegistrationExcelVo.HEADERs[4]);
				}
			}
			
			if(bulkRegistrationVo.getIntake() !=null ) {
				if(!isNumeric(bulkRegistrationVo.getIntake())) {
					bulkRegistrationVo.setStatus("Invalid");
					statusDescription.add(MassRegistrationExcelVo.HEADERs[6]);
				}
			}
			
			if(bulkRegistrationVo.getNok_nric() != null && !validationOfString(bulkRegistrationVo.getNok_nric())) {
				bulkRegistrationVo.setStatus("Invalid");
				statusDescription.add(MassRegistrationExcelVo.HEADERs[0]);
			}
			
			
		}
		
	}
	
	
	private boolean validationOfString(String data) {
		boolean flag = false;
		if(data != null && data.length() == 9) {
			char first = data.charAt(0);
			char last = data.charAt(8);
			if(Character.isLetter(first) && Character.isLetter(first)) {
				flag = true;
			}
		}
		return flag;
	}
	
	private boolean checkSizeOfString(String data) {
		boolean flag = false;
		if(data != null && data.trim().length()<=20) {
			flag = true;
		}
		return flag;
	}
	
	
	public static boolean isNumeric(String str) {
		  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
		}

	public void getStaticSheet(SheetVo sheetVo) throws IOException {
		
		
		Map<String, String[]> typeOfVaccines = sheetVo.getTypeOfVaccines();
		Map<String, String[]> doses = sheetVo.getDoses();
		Map<String, String[]> batchNos = sheetVo.getBatchNos();
		Map<String, String[]> expiryDate = new HashMap<String, String[]>();
		
		/*typeOfVaccines.put("vaccine1", new String[]{"V1Dose1", "V1Dose2", "V1Dose3"});
		typeOfVaccines.put("Dvaccine#2", new String[]{"V2Dose1", "V2Dose2", "V2Dose3"});
		typeOfVaccines.put("DIIV4", new String[]{"V3Dose1", "V3Dose2", "V3Dose3"});
		
		doses.put("Evaccine1", new String[]{"V1Batch1", "V1Batch2", "V1Batch3"});
		doses.put("Evaccine1", new String[]{"V2Batch1", "V2Batch2", "V2Batch3"});
		doses.put("Evaccine1", new String[]{"V3Batch1", "V3Batch2", "V3Batch3"});
		
		batchNos.put("V1Batch1", new String[]{"22/01/2023", "22/01/2023", "22/01/2023"});
		batchNos.put("V1Batch2", new String[]{"25/01/2023", "22/01/2023", "22/01/2023"});
		batchNos.put("V1Batch3", new String[]{"26/01/2023", "22/01/2023", "22/01/2023"});*/
		
		
		/*for (String vaccine : typeOfVaccines.keySet()) {
			
			String[] dosesList = typeOfVaccines.get(vaccine);
		    for (String dose : dosesList) {
		    	doses
		    }
		}*/
		
		
		 Map<String, String[]> categoryItems = new HashMap<String, String[]>();
		   categoryItems.put("Countries", new String[]{"France", "Germany", "Italy"});
		   categoryItems.put("Capitals", new String[]{"Paris", "Berlin", "Rome"});
		   categoryItems.put("Fruits", new String[]{"Apple", "Peach", "Banana", "Orange"});

		   Workbook workbook = new XSSFWorkbook();
		   
		   populateData(workbook, "Vaccines", typeOfVaccines);
		   populateData(workbook, "Doses", doses);
		   populateData(workbook, "Batches", batchNos);
		   //populateData(workbook, "expiryDate", expiryDate);

		   //hidden sheet for list values
		   Sheet sheet = workbook.createSheet("ListSheet");

		  /* Row row;  
		   Name namedRange;
		   String colLetter;
		   String reference;

		   int c = 0;
		   //put the data in
		   for (String key : categoryItems.keySet()) {
		    int r = 0;
		    row = sheet.getRow(r); 
		    if (row == null) 
		    	row = sheet.createRow(r); 
		    r++;
		    
		    row.createCell(c).setCellValue(key);
		    String[] items = categoryItems.get(key);
		    for (String item : items) {
		     row = sheet.getRow(r); 
		     if (row == null) 
		    	 row = sheet.createRow(r); 
		     r++;
		     row.createCell(c).setCellValue(item);
		    }
		    //create names for the item list constraints, each named from the current key
		    colLetter = CellReference.convertNumToColString(c);
		    namedRange = workbook.createName();
		    namedRange.setNameName(key);
		    reference = "ListSheet!$" + colLetter + "$2:$" + colLetter + "$" + r;
		    namedRange.setRefersToFormula(reference);
		    c++;
		   }

		   //create name for Categories list constraint
		   colLetter = CellReference.convertNumToColString((c-1));
		   namedRange = workbook.createName();
		   namedRange.setNameName("Categories");
		   reference = "ListSheet!$A$1:$" + colLetter + "$1";
		   namedRange.setRefersToFormula(reference);

		   //unselect that sheet because we will hide it later
		   sheet.setSelected(false);*/

		   //Sheet temp = workbook.createSheet("temp");
		  // temp.createRow(0).createCell(0).setCellValue("E");
		   //visible data sheet
		   sheet = workbook.createSheet("Sheet1");
		   
		   sheet.createRow(0).createCell(0).setCellValue("Select Vaccine");
		   sheet.getRow(0).createCell(1).setCellValue("Select Doses");
		   sheet.getRow(0).createCell(2).setCellValue("Select Batches");
		   sheet.getRow(0).createCell(3).setCellValue("Expiry Date");

		   sheet.setActiveCell(new CellAddress("A2"));

		   sheet.autoSizeColumn(0);
		   sheet.autoSizeColumn(1);

		   //data validations
		   DataValidationHelper dvHelper = sheet.getDataValidationHelper();
		   
		   Iterator<String> vaccinesIterator = typeOfVaccines.keySet().iterator();
			String[] nafpas = new String[typeOfVaccines.keySet().size()];
			int count = 0;
			while(vaccinesIterator.hasNext()) {
	        	nafpas[count] = vaccinesIterator.next().replaceFirst("D", "");
	        	count++;
	        }
	        String originalSheet = "myhiddenSheet";
	        addHiddenSheet(workbook, nafpas, originalSheet, 5);
	        String formulae = originalSheet.concat("!$A$1:$A$");
	        Name namedCell = workbook.createName();
	        namedCell.setNameName(originalSheet);
	        namedCell.setRefersToFormula(formulae + nafpas.length);
		   
		   
		   //data validation for categories in A2:
		   DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(originalSheet);
		   CellRangeAddressList addressList = new CellRangeAddressList(1, 501, 0, 0);            
		   DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		   sheet.addValidationData(validation);

		   //data validation for items of the selected category in B2:
		   dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT(CONCATENATE("+"\"D\""+",SUBSTITUTE(OFFSET(INDIRECT(ADDRESS(ROW(), COLUMN())),0,-1),"+"\" \""+","+"\"\""+")))");
		   addressList = new CellRangeAddressList(1, 501, 1, 1);            
		   validation = dvHelper.createValidation(dvConstraint, addressList);
		   sheet.addValidationData(validation);
		   
		  
		   
		   dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT(CONCATENATE("+"\"E\" "+",OFFSET(INDIRECT(ADDRESS(ROW(), COLUMN())),0,-2)))");
		   addressList = new CellRangeAddressList(1, 501, 2, 2);            
		   validation = dvHelper.createValidation(dvConstraint, addressList);
		   sheet.addValidationData(validation);
		   
		   dvConstraint = dvHelper.createFormulaListConstraint("INDIRECT(OFFSET(INDIRECT(ADDRESS(ROW(), COLUMN())),0,-1))");
		   addressList = new CellRangeAddressList(1, 501, 3, 3);            
		   validation = dvHelper.createValidation(dvConstraint, addressList);
		   sheet.addValidationData(validation);

		   //hide the ListSheet
		   workbook.setSheetHidden(0, false);
		   //set Sheet1 active
		   workbook.setActiveSheet(1);

		   FileOutputStream out = new FileOutputStream("CreateExcelDependentDataValidationListsUsingNamedRanges.xlsx");
		   workbook.write(out);
		   workbook.close();
		   out.close();
		
	}
	
	private static void addHiddenSheet(Workbook workbook, String[] values, String hiddenSheetName, int sheetNumber) {
	    Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);
	    for (int i = 0, length = values.length; i < length; i++) {
	        String name = values[i];
	        Row row = hiddenSheet.createRow(i);
	        Cell cell = row.createCell(0);
	        cell.setCellValue(name);
	    }
	    workbook.setSheetHidden(sheetNumber, true);

	}
	
	private void populateData(Workbook workbook, String sheetName, Map<String, String[]> map) {
		 //hidden sheet for list values
		   Sheet sheet = workbook.createSheet(sheetName);

		   Row row;  
		   Name namedRange;
		   String colLetter;
		   String reference;

		   int c = 0;
		   //put the data in
		   for (String key : map.keySet()) {
		    int r = 0;
		    row = sheet.getRow(r); 
		    if (row == null) 
		    	row = sheet.createRow(r); 
		    r++;
		    
		    if(sheetName.equals("Batches")) {
		    	key = "K"+key;
		    }
		    row.createCell(c).setCellValue(key);
		    
		    String[] items = map.get(key);
		    for (String item : items) {
		     row = sheet.getRow(r); 
		     if (row == null) 
		    	 row = sheet.createRow(r); 
		     r++;
		     row.createCell(c).setCellValue(item);
		    }
		    //create names for the item list constraints, each named from the current key
		    colLetter = CellReference.convertNumToColString(c);
		    namedRange = workbook.createName();
		    namedRange.setNameName(key.replace(" ", "").replaceAll("[^A-Za-z0-9]",""));
		    reference = sheetName+"!$" + colLetter + "$2:$" + colLetter + "$" + r;
		    namedRange.setRefersToFormula(reference);
		    c++;
		   }
		   
		 //create name for Categories list constraint
		  /* colLetter = CellReference.convertNumToColString((c-1));
		   namedRange = workbook.createName();
		   namedRange.setNameName("Categories"+sheetName);
		   reference = sheetName+"!$A$1:$" + colLetter + "$1";
		   System.out.println(reference);
		   namedRange.setRefersToFormula(reference);*/

		   //unselect that sheet because we will hide it later
		   sheet.setSelected(false);
	}
	
}