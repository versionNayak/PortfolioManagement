package com.finlabs.finexa.dto;

public class PortfolioOverviewDebtByMarketTypePieDataDTO {
	
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
		return "PortfolioOverviewDebtByMarketTypePieDataDTO [key=" + key + ", value=" + value + "]";
	}
}
