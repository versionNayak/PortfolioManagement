package com.finlabs.finexa.dto;

import java.util.List;

public class PortfolioOverviewFixedIncomeDto {

	private List<PortfolioOverviewDto> portfolioOverviewList;

	private List<PortfolioSubAssetBencmarkDto> overviewEquityBenchMarkList;

	private List<PortfolioOverviewEquitySectorDto> portfolioOverviewEquitySectorDtoList;

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

}
