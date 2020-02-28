package com.finlabs.finexa.dto;

import java.util.List;

public class PortfolioRecommendationDto {

	// Asset allocation allocation
	private List<PortfolioRecommendationAssetAllocationDto> portfolioRecommendationAssetAllocationDtoList;

	public List<PortfolioRecommendationAssetAllocationDto> getPortfolioRecommendationAssetAllocationDtoList() {
		return portfolioRecommendationAssetAllocationDtoList;
	}

	public void setPortfolioRecommendationAssetAllocationDtoList(
			List<PortfolioRecommendationAssetAllocationDto> portfolioRecommendationAssetAllocationDtoList) {
		this.portfolioRecommendationAssetAllocationDtoList = portfolioRecommendationAssetAllocationDtoList;
	}

}
