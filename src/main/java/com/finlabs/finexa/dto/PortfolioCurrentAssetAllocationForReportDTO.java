package com.finlabs.finexa.dto;

public class PortfolioCurrentAssetAllocationForReportDTO {
	
	private String subAssetClass;
	private String currentValue;
	private String investmentValue;
	private String gainLoss;
	private String cagrXirr;
	
	public String getSubAssetClass() {
		return subAssetClass;
	}
	public void setSubAssetClass(String subAssetClass) {
		this.subAssetClass = subAssetClass;
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
	
	@Override
	public String toString() {
		return "PortfolioCurrentAssetAllocationForReportDTO [subAssetClass=" + subAssetClass + ", currentValue="
				+ currentValue + ", investmentValue=" + investmentValue + ", gainLoss=" + gainLoss + ", cagrXirr="
				+ cagrXirr + "]";
	}
	
}
