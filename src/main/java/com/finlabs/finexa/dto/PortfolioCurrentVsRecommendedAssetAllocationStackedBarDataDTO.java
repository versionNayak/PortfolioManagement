package com.finlabs.finexa.dto;

public class PortfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO {
	
	private String category;
	private String key;
	private Double value;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
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
		return "PortfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO [category=" + category + ", key=" + key
				+ ", value=" + value + "]";
	}

}
