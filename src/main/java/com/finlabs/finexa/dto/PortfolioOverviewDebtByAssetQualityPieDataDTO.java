package com.finlabs.finexa.dto;

public class PortfolioOverviewDebtByAssetQualityPieDataDTO {
	
	private String key;
	private Double value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "PortfolioOverviewDebtByAssetTypePieDataDTO [key=" + key + ", value=" + value + "]";
	}
}
