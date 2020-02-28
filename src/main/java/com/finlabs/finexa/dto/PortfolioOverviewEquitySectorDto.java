package com.finlabs.finexa.dto;

public class PortfolioOverviewEquitySectorDto {

	private String sectorName;
	private double exposureInPortfolio;
	private PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto;

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public double getExposureInPortfolio() {
		return exposureInPortfolio;
	}

	public void setExposureInPortfolio(double exposureInPortfolio) {
		this.exposureInPortfolio = exposureInPortfolio;
	}

	public PortfolioSubAssetBencmarkDto getPortfolioSubAssetBencmarkDto() {
		return portfolioSubAssetBencmarkDto;
	}

	public void setPortfolioSubAssetBencmarkDto(PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto) {
		this.portfolioSubAssetBencmarkDto = portfolioSubAssetBencmarkDto;
	}

}
