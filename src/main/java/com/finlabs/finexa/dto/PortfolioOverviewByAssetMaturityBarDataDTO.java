package com.finlabs.finexa.dto;

public class PortfolioOverviewByAssetMaturityBarDataDTO {

	private String category;
	private Double value;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "PortfolioOverviewByAssetMaturityBarDataDTO [category=" + category + ", value=" + value + "]";
	}
	
}
