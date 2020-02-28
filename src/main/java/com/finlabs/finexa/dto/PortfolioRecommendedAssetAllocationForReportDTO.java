package com.finlabs.finexa.dto;

public class PortfolioRecommendedAssetAllocationForReportDTO {
	
	private String subAssetClass;
	private String currentAllocation;
	private String recommendedAllocation;
	
	public String getSubAssetClass() {
		return subAssetClass;
	}
	public void setSubAssetClass(String subAssetClass) {
		this.subAssetClass = subAssetClass;
	}
	
	public String getCurrentAllocation() {
		return currentAllocation;
	}
	public void setCurrentAllocation(String currentAllocation) {
		this.currentAllocation = currentAllocation;
	}
	
	public String getRecommendedAllocation() {
		return recommendedAllocation;
	}
	public void setRecommendedAllocation(String recommendedAllocation) {
		this.recommendedAllocation = recommendedAllocation;
	}
	
	@Override
	public String toString() {
		return "PortfolioRecommendedAssetAllocationForReportDTO [subAssetClass=" + subAssetClass
				+ ", currentAllocation=" + currentAllocation + ", recommendedAllocation=" + recommendedAllocation + "]";
	}
}
