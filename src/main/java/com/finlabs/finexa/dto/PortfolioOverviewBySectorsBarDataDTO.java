package com.finlabs.finexa.dto;

public class PortfolioOverviewBySectorsBarDataDTO {
	
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
		return "PortfolioOverviewBySecordsBarDataDTO [category=" + category + ", value=" + value + "]";
	}

}
