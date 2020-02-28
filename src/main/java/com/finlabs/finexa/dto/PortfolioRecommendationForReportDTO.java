package com.finlabs.finexa.dto;

public class PortfolioRecommendationForReportDTO {
	
	private String assetClass;
	private String subAssetClass;
	private String allocationPercentage;
	private String rebalancingAmount;
	
	public String getAssetClass() {
		return assetClass;
	}
	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}
	
	public String getSubAssetClass() {
		return subAssetClass;
	}
	public void setSubAssetClass(String subAssetClass) {
		this.subAssetClass = subAssetClass;
	}
	
	public String getAllocationPercentage() {
		return allocationPercentage;
	}
	public void setAllocationPercentage(String allocationPercentage) {
		this.allocationPercentage = allocationPercentage;
	}
	
	public String getRebalancingAmount() {
		return rebalancingAmount;
	}
	public void setRebalancingAmount(String rebalancingAmount) {
		this.rebalancingAmount = rebalancingAmount;
	}
	
	@Override
	public String toString() {
		return "PortfolioRecommendationForReportDTO [assetClass=" + assetClass + ", subAssetClass=" + subAssetClass
				+ ", allocationPercentage=" + allocationPercentage + ", rebalancingAmount=" + rebalancingAmount + "]";
	}
}
