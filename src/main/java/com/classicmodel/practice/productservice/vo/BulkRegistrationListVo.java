package com.classicmodel.practice.productservice.vo;

import java.util.ArrayList;
import java.util.List;

public class BulkRegistrationListVo {
	
	private List<BulkRegistrationVo> validBulkRegistrationVos;
     private List<BulkRegistrationVo> invalidBulkRegistrationVos;
     
	public List<BulkRegistrationVo> getValidBulkRegistrationVos() {
		return validBulkRegistrationVos;
	}
	public void setValidBulkRegistrationVos(List<BulkRegistrationVo> validBulkRegistrationVos) {
		this.validBulkRegistrationVos = validBulkRegistrationVos;
	}
	public List<BulkRegistrationVo> getInvalidBulkRegistrationVos() {
		return invalidBulkRegistrationVos;
	}
	public void setInvalidBulkRegistrationVos(List<BulkRegistrationVo> invalidBulkRegistrationVos) {
		this.invalidBulkRegistrationVos = invalidBulkRegistrationVos;
	}
}
