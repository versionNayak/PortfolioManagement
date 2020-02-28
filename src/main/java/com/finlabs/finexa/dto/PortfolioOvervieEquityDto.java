package com.finlabs.finexa.dto;

import java.util.List;
import java.util.Map;

public class PortfolioOvervieEquityDto {

	private List<PortfolioOverviewDto> portfolioOverviewList;

	private List<PortfolioSubAssetBencmarkDto> overviewEquityBenchMarkList;

	private List<PortfolioOverviewEquitySectorDto> portfolioOverviewEquitySectorDtoList;

	private Map<String, Double> assetQualityMap;

	public List<PortfolioOverviewEquitySectorDto> getPortfolioOverviewEquitySectorDtoList() {
		return portfolioOverviewEquitySectorDtoList;
	}

	public void setPortfolioOverviewEquitySectorDtoList(
			List<PortfolioOverviewEquitySectorDto> portfolioOverviewEquitySectorDtoList) {
		this.portfolioOverviewEquitySectorDtoList = portfolioOverviewEquitySectorDtoList;
	}

	public List<PortfolioOverviewDto> getPortfolioOverviewList() {
		return portfolioOverviewList;
	}

	public void setPortfolioOverviewList(List<PortfolioOverviewDto> portfolioOverviewList) {
		this.portfolioOverviewList = portfolioOverviewList;
	}

	public List<PortfolioSubAssetBencmarkDto> getOverviewEquityBenchMarkList() {
		return overviewEquityBenchMarkList;
	}

	public void setOverviewEquityBenchMarkList(List<PortfolioSubAssetBencmarkDto> overviewEquityBenchMarkList) {
		this.overviewEquityBenchMarkList = overviewEquityBenchMarkList;
	}

	public Map<String, Double> getAssetQualityMap() {
		return assetQualityMap;
	}

	public void setAssetQualityMap(Map<String, Double> assetQualityMap) {
		this.assetQualityMap = assetQualityMap;
	}

}
