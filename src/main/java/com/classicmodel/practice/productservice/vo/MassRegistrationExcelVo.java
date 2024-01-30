package com.classicmodel.practice.productservice.vo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation.ErrorStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MassRegistrationExcelVo {
	
	public static String[] HEADERs = { "NRIC", "FirstName", "LastName","MobileNumber", "ResidentialNumber", "EnlistmentDate", "Intake", "HighestStandardAttend", "NOK_NRIC", "NAPFA_TestResults", "PES", "ColorPerception", "PULHEEMS_P", "PULHEEMS_U", "PULHEEMS_L", "PULHEEMS_H", "PULHEEMS_ER", "PULHEEMS_EL", "PULHEEMS_M", "PULHEEMS_S", "Nationality", "ORD" };
	static String SHEET = "PatientRegistration";
	private byte[] uploadDocument; 
	public byte[] getUploadDocument() {
		return uploadDocument; 
		} 
	
	public void setUploadDocument(byte[] uploadDocument) { 
		this.uploadDocument = uploadDocument; 
		} 
	
	public ByteArrayInputStream load() throws ParseException { 
		ByteArrayInputStream in = MassRegistrationExcelVo.excelTemplate(); 
		return in; 
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
	
	
	
	public static ByteArrayInputStream excelTemplate() throws ParseException {
		try { 
			Workbook workbook = new XSSFWorkbook(); 
			
			CreationHelper createHelper = workbook.getCreationHelper();
			short format = createHelper.createDataFormat().getFormat("d-mmm");
			CellStyle style1 = workbook.createCellStyle();
		    style1.setDataFormat(format);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream(); 
			Sheet sheet = workbook.createSheet(SHEET); 
			
			
			
			
			CellStyle style = workbook.createCellStyle(); 
			XSSFFont font = (XSSFFont) workbook.createFont(); 
			font.setBold(true); 
			font.setFontHeight(14); 
			style.setFont(font);
			style.setLocked(true);
			//sheet.setDefaultColumnWidth(4000);
			
			// Header 
			Row headerRow = sheet.createRow(0); 
			for (int col = 0; col < HEADERs.length; col++) { 
				Cell cell = headerRow.createCell(col); 
				cell.setCellValue(HEADERs[col]); 
				cell.setCellStyle(style);
			} 
			
			
			CellStyle style2 = workbook.createCellStyle(); 
			style2.setDataFormat(createHelper.createDataFormat().getFormat("DD\"/\"MM\"/\"YYYY"));	
			for (int r = 1; r <= 500; r++) {
			    Row dataRow = sheet.createRow(r);;
			    Cell dataCell = dataRow.createCell(5);;
			    dataCell.setCellStyle(style2);
			   }
					
			DataValidationHelper dvHelper = sheet.getDataValidationHelper();
	        
			XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createNumericConstraint(ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "1.00", "1000000000000.00");
	        CellRangeAddressList regions = new CellRangeAddressList(1,20,3, 3);
	        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
	        validation.setErrorStyle(ErrorStyle.STOP);
	        validation.createErrorBox("Error", "Only numeric values are allowed");
	        validation.setShowErrorBox(true);
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("dd\"/\"MM\"/\"yyyy");
	        
	        //System.out.println(DateUtil.getExcelDate(sdf.parse("25/10/2014")));
	        
	        XSSFDataValidationConstraint dvConstraint1 = (XSSFDataValidationConstraint) dvHelper.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, "Date(1990, 1, 1)", "Date(9999, 12, 31)", "DD\"/\"MM\"/\"YYYY");
	        CellRangeAddressList regions1 = new CellRangeAddressList(1,20,5,5);
	        XSSFDataValidation validation1 = (XSSFDataValidation) dvHelper.createValidation(dvConstraint1,regions1);
	        validation1.setErrorStyle(ErrorStyle.STOP);
	        validation1.createErrorBox("Invalid date", "Please Insert Valid Date with this formate dd/mm/yyyy");
	        validation1.setShowErrorBox(true);
	        
	        String originalSheet1 = "myhiddenSheet1";
	        String [] NAfaTests = new String[]{"Gold", "Silver", "Bronze","Gold", "Silver", "Bronze","Gold", "Silver", "Bronze","Gold", "Silver", "Bronze","Gold", "Silver", "Bronze"};
	        addHiddenSheet(workbook, NAfaTests, originalSheet1, 1);
	        String formulae1 = originalSheet1.concat("!$A$1:$A$");
	        Name namedCell1 = workbook.createName();
	        namedCell1.setNameName(originalSheet1);
	        namedCell1.setRefersToFormula(formulae1 + NAfaTests.length);
	        
	        XSSFDataValidationConstraint dvConstraint9 = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(originalSheet1);
	        CellRangeAddressList regions9 = new CellRangeAddressList(1,20,9, 9);
	        XSSFDataValidation validation9 = (XSSFDataValidation) dvHelper.createValidation(dvConstraint9, regions9);
	        validation9.setShowErrorBox(true);
	        validation9.setSuppressDropDownArrow(true);
	        
	       //XSSFDataValidationConstraint dvConstraint5 = (XSSFDataValidationConstraint)  dvHelper.createNumericConstraint(ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "1.00", "1000000000000.00");
	        CellRangeAddressList regions5 = new CellRangeAddressList(1,20,4, 4);
	        XSSFDataValidation validation5 = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions5);
	        validation5.setShowErrorBox(true);
	        validation5.setErrorStyle(ErrorStyle.STOP);
	        validation5.createErrorBox("Error", "Only numeric values are allowed");
	        
	        //XSSFDataValidationConstraint dvConstraint7 = (XSSFDataValidationConstraint)  dvHelper.createNumericConstraint(ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "1.00", "1000000000000.00");
	        CellRangeAddressList regions7 = new CellRangeAddressList(1,20,6, 6);
	        XSSFDataValidation validation7 = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions7);
	        validation7.setShowErrorBox(true);
	        validation7.setErrorStyle(ErrorStyle.STOP);
	        validation7.createErrorBox("Error", "Only numeric values are allowed");
	        
	        String [] strArray = new String[]{"Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America","Indian", "Rashian", "Japan", "America"} ;
	        
	        String originalSheet = "myhiddenSheet";
	        addHiddenSheet(workbook, strArray, originalSheet, 2);
	        String formulae = originalSheet.concat("!$A$1:$A$");
	        Name namedCell = workbook.createName();
	        namedCell.setNameName(originalSheet);
	        namedCell.setRefersToFormula(formulae + strArray.length);
	        
	        XSSFDataValidationConstraint dvConstraint20 = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(originalSheet);
	        CellRangeAddressList regions20 = new CellRangeAddressList(1,20,20, 20);
	        XSSFDataValidation validation20 = (XSSFDataValidation) dvHelper.createValidation(dvConstraint20, regions20);
	        validation20.setShowErrorBox(true);
	        validation20.setSuppressDropDownArrow(true);
	        
	        sheet.addValidationData(validation);
	        sheet.addValidationData(validation1);
	        sheet.addValidationData(validation5);
	        sheet.addValidationData(validation7);
	        sheet.addValidationData(validation9);
	        sheet.addValidationData(validation20);
	        //workbook.setSheetHidden(1, true);
	        
			/*DVConstraint dvConstraint = DVConstraint.createNumericConstraint(DataValidationConstraint.ValidationType.DECIMAL, DataValidationConstraint.OperatorType.BETWEEN, "1", "9");
			CellRangeAddressList regions = new CellRangeAddressList(1,20,3, 3);
			DataValidation dataValidation = new HSSFDataValidation(regions, dvConstraint);
			dataValidation.setEmptyCellAllowed(true);
			dataValidation.setShowPromptBox(true);
			dataValidation.setSuppressDropDownArrow(false);
			dataValidation.createErrorBox("invalid Input", "enter between 1 to 9");
			sheet.addValidationData(dataValidation); */
			
			// int rowIdx = 1; 
			workbook.write(out); 
			return new ByteArrayInputStream(out.toByteArray()); 
			} catch (IOException e) {
				throw new RuntimeException("fail to import data to Excel file: " + e.getMessage()); 
				} 
		}
}
