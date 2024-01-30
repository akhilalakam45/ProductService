package com.classicmodel.practice.productservice.vo;

import java.util.HashMap;
import java.util.Map;

public class SheetVo {
	
	Map<String, String[]> typeOfVaccines ;
	Map<String, String[]> doses ;
	Map<String, String[]> batchNos ;
	
	public Map<String, String[]> getTypeOfVaccines() {
		return typeOfVaccines;
	}
	public void setTypeOfVaccines(Map<String, String[]> typeOfVaccines) {
		this.typeOfVaccines = typeOfVaccines;
	}
	public Map<String, String[]> getDoses() {
		return doses;
	}
	public void setDoses(Map<String, String[]> doses) {
		this.doses = doses;
	}
	public Map<String, String[]> getBatchNos() {
		return batchNos;
	}
	public void setBatchNos(Map<String, String[]> batchNos) {
		this.batchNos = batchNos;
	}
}
