package com.finlabs.finexa.dto;

public class PortfolioOverviewDebtTableDataForReportDTO {
	
	private String product;
	private String productType;
	private String currentValue;
	private String investmentValue;
	private String gainLoss;
	private String returnType;
	private String cagrXirr;
	private String lockedInUptoDate;
	private String maturityDate;
	private String maturityAmount;
	
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
	
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	public String getCagrXirr() {
		return cagrXirr;
	}
	public void setCagrXirr(String cagrXirr) {
		this.cagrXirr = cagrXirr;
	}
	
	public String getLockedInUptoDate() {
		return lockedInUptoDate;
	}
	public void setLockedInUptoDate(String lockedInUptoDate) {
		this.lockedInUptoDate = lockedInUptoDate;
	}
	
	public String getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}
	
	public String getMaturityAmount() {
		return maturityAmount;
	}
	public void setMaturityAmount(String maturityAmount) {
		this.maturityAmount = maturityAmount;
	}
	
	@Override
	public String toString() {
		return "PortfolioOverviewDebtTableDataForReportDTO [product=" + product + ", productType=" + productType
				+ ", currentValue=" + currentValue + ", investmentValue=" + investmentValue + ", gainLoss=" + gainLoss
				+ ", returnType=" + returnType + ", cagrXirr=" + cagrXirr + ", lockedInUptoDate=" + lockedInUptoDate
				+ ", maturityDate=" + maturityDate + ", maturityAmount=" + maturityAmount + "]";
	}
}
