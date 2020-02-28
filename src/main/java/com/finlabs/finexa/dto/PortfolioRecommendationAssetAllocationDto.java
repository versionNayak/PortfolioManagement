package com.finlabs.finexa.dto;

import java.util.List;

public class PortfolioRecommendationAssetAllocationDto {

	private String assetClass;
	private String subAssetClass;
	private Double allocation;
	private Double rebalancingAmount;
	private List<String> recommendedMasterList;

	public List<String> getRecommendedMasterList() {
		return recommendedMasterList;
	}

	public void setRecommendedMasterList(List<String> recommendedMasterList) {
		this.recommendedMasterList = recommendedMasterList;
	}

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

	public Double getAllocation() {
		return allocation;
	}

	public void setAllocation(Double allocation) {
		this.allocation = allocation;
	}

	public Double getRebalancingAmount() {
		return rebalancingAmount;
	}

	public void setRebalancingAmount(Double rebalancingAmount) {
		this.rebalancingAmount = rebalancingAmount;
	}

}
