package com.finlabs.finexa.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductRecommendationOutputPM {
	
	private String subAssetClass;
	private double subAssetAlloPerc;
	private List<String> productName;
	private List<String> isinList;
	
	private double lumpsumAmt;
	private double sipAmt;
	private int sipTenure;
	private int subAssetClassId;
	
	
	public String getSubAssetClass() {
		return subAssetClass;
	}
	public void setSubAssetClass(String subAssetClass) {
		this.subAssetClass = subAssetClass;
	}
	public double getSubAssetAlloPerc() {
		return subAssetAlloPerc;
	}
	public void setSubAssetAlloPerc(double subAssetAlloPerc) {
		this.subAssetAlloPerc = subAssetAlloPerc;
	}
	public List<String> getProductName() {
		return productName;
	}
	public void setProductName(List<String> productName) {
		this.productName = productName;
	}
	public double getLumpsumAmt() {
		return lumpsumAmt;
	}
	public void setLumpsumAmt(double lumpsumAmt) {
		this.lumpsumAmt = lumpsumAmt;
	}
	public double getSipAmt() {
		return sipAmt;
	}
	public void setSipAmt(double sipAmt) {
		this.sipAmt = sipAmt;
	}
	public int getSipTenure() {
		return sipTenure;
	}
	public void setSipTenure(int sipTenure) {
		this.sipTenure = sipTenure;
	}
	public int getSubAssetClassId() {
		return subAssetClassId;
	}
	public void setSubAssetClassId(int subAssetClassId) {
		this.subAssetClassId = subAssetClassId;
	}
	public List<String> getIsinList() {
		return isinList;
	}
	public void setIsinList(List<String> isinList) {
		this.isinList = isinList;
	}
	
}
