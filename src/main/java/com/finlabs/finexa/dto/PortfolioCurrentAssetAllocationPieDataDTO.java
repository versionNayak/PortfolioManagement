package com.finlabs.finexa.dto;

public class PortfolioCurrentAssetAllocationPieDataDTO {
	
	private String assetAllocationKey;
	private Double assetAllocationValue;
	
	public String getAssetAllocationKey() {
		return assetAllocationKey;
	}
	public void setAssetAllocationKey(String assetAllocationKey) {
		this.assetAllocationKey = assetAllocationKey;
	}
	
	public Double getAssetAllocationValue() {
		return assetAllocationValue;
	}
	public void setAssetAllocationValue(Double assetAllocationValue) {
		this.assetAllocationValue = assetAllocationValue;
	}
	
	@Override
	public String toString() {
		return "PortfolioCurrentAssetAllocationPieDataDTO [assetAllocationKey=" + assetAllocationKey
				+ ", assetAllocationValue=" + assetAllocationValue + "]";
	}
	
}
