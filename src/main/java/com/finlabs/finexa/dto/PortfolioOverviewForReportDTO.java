package com.finlabs.finexa.dto;

public class PortfolioOverviewForReportDTO {
	
	private String productName;
	private String productType;
	private String currentValue;
	private String percentageOfTotal;
	private String investmentValue;
	private String gainLoss;
    private String cagrXirr;
    private String totalCurrentValue;
    private String totalInvestmentValue;
    private String totalGainLoss;
    private String totalCagrXirr;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	
	public String getPercentageOfTotal() {
		return percentageOfTotal;
	}
	public void setPercentageOfTotal(String percentageOfTotal) {
		this.percentageOfTotal = percentageOfTotal;
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
	
	public String getTotalCurrentValue() {
		return totalCurrentValue;
	}
	public void setTotalCurrentValue(String totalCurrentValue) {
		this.totalCurrentValue = totalCurrentValue;
	}
	
	public String getTotalInvestmentValue() {
		return totalInvestmentValue;
	}
	public void setTotalInvestmentValue(String totalInvestmentValue) {
		this.totalInvestmentValue = totalInvestmentValue;
	}
	
	public String getTotalGainLoss() {
		return totalGainLoss;
	}
	public void setTotalGainLoss(String totalGainLoss) {
		this.totalGainLoss = totalGainLoss;
	}
	
	public String getTotalCagrXirr() {
		return totalCagrXirr;
	}
	public void setTotalCagrXirr(String totalCagrXirr) {
		this.totalCagrXirr = totalCagrXirr;
	}
	
	@Override
	public String toString() {
		return "PortfolioOverviewForReportDTO [productName=" + productName + ", productType=" + productType
				+ ", currentValue=" + currentValue + ", percentageOfTotal=" + percentageOfTotal + ", investmentValue="
				+ investmentValue + ", gainLoss=" + gainLoss + ", cagrXirr=" + cagrXirr + ", totalCurrentValue="
				+ totalCurrentValue + ", totalInvestmentValue=" + totalInvestmentValue + ", totalGainLoss="
				+ totalGainLoss + ", totalCagrXirr=" + totalCagrXirr + "]";
	}

}
