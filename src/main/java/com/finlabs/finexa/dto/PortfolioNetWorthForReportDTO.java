package com.finlabs.finexa.dto;

public class PortfolioNetWorthForReportDTO {
	
	private String assets;
	private String currentValue;
	private String percentOfTotal;
	
	public String getAssets() {
		return assets;
	}
	public void setAssets(String assets) {
		this.assets = assets;
	}
	public String getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}
	public String getPercentOfTotal() {
		return percentOfTotal;
	}
	public void setPercentOfTotal(String percentOfTotal) {
		this.percentOfTotal = percentOfTotal;
	}
	
	@Override
	public String toString() {
		return "PortfolioNetWorthForReportDTO [assets=" + assets + ", currentValue=" + currentValue
				+ ", percentOfTotal=" + percentOfTotal + "]";
	}

}
