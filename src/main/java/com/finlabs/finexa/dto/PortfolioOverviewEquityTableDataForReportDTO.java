package com.finlabs.finexa.dto;

public class PortfolioOverviewEquityTableDataForReportDTO {
	
	private String product;
	private String productType;
	private String currentValue;
	private String investmentValue;
	private String gainLoss;
	private String cagrXirr;
	private String ptpReturns;
	private String lockedInUptoDate;
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	
	public String getInvestmentValue() {
		return investmentValue;
	}
	public void setInvestmentValue(String investmentValue) {
		this.investmentValue = investmentValue;
	}
	
	public String getGainLoss() {
		return gainLoss;
	}
	public void setGainLoss(String gainLoss) {
		this.gainLoss = gainLoss;
	}
	
	public String getCagrXirr() {
		return cagrXirr;
	}
	public void setCagrXirr(String cagrXirr) {
		this.cagrXirr = cagrXirr;
	}
	
	public String getPtpReturns() {
		return ptpReturns;
	}
	public void setPtpReturns(String ptpReturns) {
		this.ptpReturns = ptpReturns;
	}
	
	public String getLockedInUptoDate() {
		return lockedInUptoDate;
	}
	public void setLockedInUptoDate(String lockedInUptoDate) {
		this.lockedInUptoDate = lockedInUptoDate;
	}
	
	@Override
	public String toString() {
		return "PortfolioOverviewEquityTableDataForReportDTO [product=" + product + ", productType=" + productType
				+ ", currentValue=" + currentValue + ", investmentValue=" + investmentValue + ", gainLoss=" + gainLoss
				+ ", cagrXirr=" + cagrXirr + ", ptpReturns=" + ptpReturns + ", lockedInUptoDate=" + lockedInUptoDate
				+ "]";
	}
}
