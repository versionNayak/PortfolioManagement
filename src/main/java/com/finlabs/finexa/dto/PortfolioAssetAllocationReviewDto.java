package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PortfolioAssetAllocationReviewDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String investmentAssetClass;
	private String investmentSubAssetClass;
	private int assetClassId;
	private int subAssetClassId;
	private Double currentValue;
	private Double portFoliototalPercentage;
	private Double cagr_xirr;
	private Double investmentValue;
	private Double profitLoss;
	private Double recomentTotalPercentage;
	private Double expectedReturns;
	private Double riskStdDev;
	private Double totalExpectedRecommed;
	private Double totalRiskExpectedCurrent;
	private Double totalRiskExpectedRecommed;
	private PortfolioSubAssetBencmarkDto portSubAssetBechMark;
	private Map<String, List<PortfolioAssetAllocationReviewDto>> portfolioAssetListMap;

	public String getInvestmentAssetClass() {
		return investmentAssetClass;
	}

	public void setInvestmentAssetClass(String investmentAssetClass) {
		this.investmentAssetClass = investmentAssetClass;
	}

	public Double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}

	public Double getPortFoliototalPercentage() {
		return portFoliototalPercentage;
	}

	public void setPortFoliototalPercentage(Double portFoliototalPercentage) {
		this.portFoliototalPercentage = portFoliototalPercentage;
	}

	public Double getCagr_xirr() {
		return cagr_xirr;
	}

	public void setCagr_xirr(Double cagr_xirr) {
		this.cagr_xirr = cagr_xirr;
	}

	public Double getInvestmentValue() {
		return investmentValue;
	}

	public void setInvestmentValue(Double investmentValue) {
		this.investmentValue = investmentValue;
	}

	public Double getProfitLoss() {
		return profitLoss;
	}

	public void setProfitLoss(Double profitLoss) {
		this.profitLoss = profitLoss;
	}

	public Double getRecomentTotalPercentage() {
		return recomentTotalPercentage;
	}

	public void setRecomentTotalPercentage(Double recomentTotalPercentage) {
		this.recomentTotalPercentage = recomentTotalPercentage;
	}

	public Double getExpectedReturns() {
		return expectedReturns;
	}

	public void setExpectedReturns(Double expectedReturns) {
		this.expectedReturns = expectedReturns;
	}

	public PortfolioSubAssetBencmarkDto getPortSubAssetBechMark() {
		return portSubAssetBechMark;
	}

	public void setPortSubAssetBechMark(PortfolioSubAssetBencmarkDto portSubAssetBechMark) {
		this.portSubAssetBechMark = portSubAssetBechMark;
	}

	public String getInvestmentSubAssetClass() {
		return investmentSubAssetClass;
	}

	public void setInvestmentSubAssetClass(String investmentSubAssetClass) {
		this.investmentSubAssetClass = investmentSubAssetClass;
	}

	public Map<String, List<PortfolioAssetAllocationReviewDto>> getPortfolioAssetListMap() {
		return portfolioAssetListMap;
	}

	public void setPortfolioAssetListMap(Map<String, List<PortfolioAssetAllocationReviewDto>> portfolioAssetListMap) {
		this.portfolioAssetListMap = portfolioAssetListMap;
	}

	public int getSubAssetClassId() {
		return subAssetClassId;
	}

	public void setSubAssetClassId(int subAssetClassId) {
		this.subAssetClassId = subAssetClassId;
	}

	public Double getRiskStdDev() {
		return riskStdDev;
	}

	public void setRiskStdDev(Double riskStdDev) {
		this.riskStdDev = riskStdDev;
	}

	public int getAssetClassId() {
		return assetClassId;
	}

	public void setAssetClassId(int assetClassId) {
		this.assetClassId = assetClassId;
	}

	public Double getTotalExpectedRecommed() {
		return totalExpectedRecommed;
	}

	public void setTotalExpectedRecommed(Double totalExpectedRecommed) {
		this.totalExpectedRecommed = totalExpectedRecommed;
	}

	public Double getTotalRiskExpectedCurrent() {
		return totalRiskExpectedCurrent;
	}

	public void setTotalRiskExpectedCurrent(Double totalRiskExpectedCurrent) {
		this.totalRiskExpectedCurrent = totalRiskExpectedCurrent;
	}

	public Double getTotalRiskExpectedRecommed() {
		return totalRiskExpectedRecommed;
	}

	public void setTotalRiskExpectedRecommed(Double totalRiskExpectedRecommed) {
		this.totalRiskExpectedRecommed = totalRiskExpectedRecommed;
	}

}
