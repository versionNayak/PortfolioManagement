package com.finlabs.finexa.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PortfolioPagerproductTemplateDto {

	// for sector pager
	private String sectorFor;
	private String sector;
	private Double beta;
	private Double LastClosingPrice;
	private Double marketCap;
	private Double weekHigh52;
	private Double weekLow52;
	private Double p_e;
	private Double p_b;
	private Double dailyTradeValue;
	private Double divYield;
	
	private List<PortfolioEquityDailyPriceDto> portpolioEquityDailyPriceDtoList;
	private List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList;
	
	private List<PortfolioEquityDailyPriceDto> masterStockPerformancevsIndexList;
	private List<MasterdailynavDTO> masterNiftyPerformancevsIndexList;
	private PortfolioSubAssetBencmarkDto masterStockPerformanceTimePeriod;
	private PortfolioSubAssetBencmarkDto masterNiftyPerformanceTimePeriod;
	


	// for MF pager

	private Date schemeinceptionDate;
	private String subAssetClass;
	private String fundManagers;
	private String benchmarkIndex;
	private Double minInvestmentAmount;
	private String exitLoadPeriod;
	private String aum;
	private Double expenseRatio;
	private Double pe;
	private Double pb;
	private String capRank;
	private String investmentStyle;
	public String getInvestmentStyle() {
		return investmentStyle;
	}

	public void setInvestmentStyle(String investmentStyle) {
		this.investmentStyle = investmentStyle;
	}

	private List<MasterdailynavDTO> masterFundPerformancevsIndexList;
	private List<MasterdailynavDTO> masterBenchMarkPerformancevsIndexList;
	private PortfolioSubAssetBencmarkDto masterFundPerformanceTimePeriod;
	private PortfolioSubAssetBencmarkDto masterBenchMarkPerformanceTimePeriod;
	
	

	// asset allocation pie graph
	private Double equityRatio;
	private Double debtRatio;
	private Double otherRatio;

	// market capitalization breakup
	private Double largeCap;
	private Double midCap;
	private Double smallCap;
	private Double otherCap;
	
	// risk measures
	private Double stdDeviation;
	private Double sharpeRatio;
	private Double informationRatio;
	private Double treynorRatio;

	// port folio details in mf pager
	private List<PortfolioDetailsDto> portfolioDetailsList;

	// mf pager fixed income
	private Double ytm;
	private Double duration;
	
	//for sector Holding
	List<MasterMFSectorHoldingWiseDTO> masterMFSectorHoldingWiseDTOList;
	
	// assetQualityMap for FixedIncomeType pager
	private Map<String, Double> assetQualityMap;

	public Map<String, Double> getAssetQualityMap() {
		return assetQualityMap;
	}

	public void setAssetQualityMap(Map<String, Double> assetQualityMap) {
		this.assetQualityMap = assetQualityMap;
	}

	public String getSector() {
		return sector;
	}

	public List<PortfolioDetailsDto> getPortfolioDetailsList() {
		return portfolioDetailsList;
	}

	public void setPortfolioDetailsList(List<PortfolioDetailsDto> portfolioDetailsList) {
		this.portfolioDetailsList = portfolioDetailsList;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public Double getBeta() {
		return beta;
	}

	public void setBeta(Double beta) {
		this.beta = beta;
	}

	public Double getLastClosingPrice() {
		return LastClosingPrice;
	}

	public void setLastClosingPrice(Double lastClosingPrice) {
		LastClosingPrice = lastClosingPrice;
	}

	public Double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(Double marketCap) {
		this.marketCap = marketCap;
	}

	public Double getWeekHigh52() {
		return weekHigh52;
	}

	public void setWeekHigh52(Double weekHigh52) {
		this.weekHigh52 = weekHigh52;
	}

	public Double getWeekLow52() {
		return weekLow52;
	}

	public void setWeekLow52(Double weekLow52) {
		this.weekLow52 = weekLow52;
	}

	public Double getP_e() {
		return p_e;
	}

	public void setP_e(Double p_e) {
		this.p_e = p_e;
	}

	public Double getP_b() {
		return p_b;
	}

	public void setP_b(Double p_b) {
		this.p_b = p_b;
	}

	public Double getDailyTradeValue() {
		return dailyTradeValue;
	}

	public void setDailyTradeValue(Double dailyTradeValue) {
		this.dailyTradeValue = dailyTradeValue;
	}

	public Double getDivYield() {
		return divYield;
	}

	public void setDivYield(Double divYield) {
		this.divYield = divYield;
	}

	public List<PortfolioEquityDailyPriceDto> getPortpolioEquityDailyPriceDtoList() {
		return portpolioEquityDailyPriceDtoList;
	}

	public void setPortpolioEquityDailyPriceDtoList(
			List<PortfolioEquityDailyPriceDto> portpolioEquityDailyPriceDtoList) {
		this.portpolioEquityDailyPriceDtoList = portpolioEquityDailyPriceDtoList;
	}

	public String getSectorFor() {
		return sectorFor;
	}

	public void setSectorFor(String sectorFor) {
		this.sectorFor = sectorFor;
	}

	public List<PortfolioSubAssetBencmarkDto> getPortfolioSubAssetBencmarkDtoList() {
		return portfolioSubAssetBencmarkDtoList;
	}

	public void setPortfolioSubAssetBencmarkDtoList(
			List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList) {
		this.portfolioSubAssetBencmarkDtoList = portfolioSubAssetBencmarkDtoList;
	}

	public Date getSchemeinceptionDate() {
		return schemeinceptionDate;
	}

	public void setSchemeinceptionDate(Date schemeinceptionDate) {
		this.schemeinceptionDate = schemeinceptionDate;
	}

	public String getSubAssetClass() {
		return subAssetClass;
	}

	public void setSubAssetClass(String subAssetClass) {
		this.subAssetClass = subAssetClass;
	}

	public String getFundManagers() {
		return fundManagers;
	}

	public void setFundManagers(String fundManagers) {
		this.fundManagers = fundManagers;
	}

	public String getBenchmarkIndex() {
		return benchmarkIndex;
	}

	public void setBenchmarkIndex(String benchmarkIndex) {
		this.benchmarkIndex = benchmarkIndex;
	}

	public Double getMinInvestmentAmount() {
		return minInvestmentAmount;
	}

	public void setMinInvestmentAmount(Double minInvestmentAmount) {
		this.minInvestmentAmount = minInvestmentAmount;
	}

	public Double getStdDeviation() {
		return stdDeviation;
	}

	public void setStdDeviation(Double stdDeviation) {
		this.stdDeviation = stdDeviation;
	}

	public Double getSharpeRatio() {
		return sharpeRatio;
	}

	public void setSharpeRatio(Double sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}

	public Double getInformationRatio() {
		return informationRatio;
	}

	public void setInformationRatio(Double informationRatio) {
		this.informationRatio = informationRatio;
	}

	public Double getTreynorRatio() {
		return treynorRatio;
	}

	public void setTreynorRatio(Double treynorRatio) {
		this.treynorRatio = treynorRatio;
	}

	public String getExitLoadPeriod() {
		return exitLoadPeriod;
	}

	public void setExitLoadPeriod(String exitLoadPeriod) {
		this.exitLoadPeriod = exitLoadPeriod;
	}

	public String getAum() {
		return aum;
	}

	public void setAum(String aum) {
		this.aum = aum;
	}

	public Double getExpenseRatio() {
		return expenseRatio;
	}

	public void setExpenseRatio(Double expenseRatio) {
		this.expenseRatio = expenseRatio;
	}

	public Double getPe() {
		return pe;
	}

	public void setPe(Double pe) {
		this.pe = pe;
	}

	public Double getPb() {
		return pb;
	}

	public void setPb(Double pb) {
		this.pb = pb;
	}

	public Double getEquityRatio() {
		return equityRatio;
	}

	public void setEquityRatio(Double equityRatio) {
		this.equityRatio = equityRatio;
	}

	public Double getDebtRatio() {
		return debtRatio;
	}

	public void setDebtRatio(Double debtRatio) {
		this.debtRatio = debtRatio;
	}

	public Double getOtherRatio() {
		return otherRatio;
	}

	public void setOtherRatio(Double otherRatio) {
		this.otherRatio = otherRatio;
	}

	public Double getLargeCap() {
		return largeCap;
	}

	public void setLargeCap(Double largeCap) {
		this.largeCap = largeCap;
	}

	public Double getMidCap() {
		return midCap;
	}

	public void setMidCap(Double midCap) {
		this.midCap = midCap;
	}

	public Double getSmallCap() {
		return smallCap;
	}

	public void setSmallCap(Double smallCap) {
		this.smallCap = smallCap;
	}

	public Double getYtm() {
		return ytm;
	}

	public void setYtm(Double ytm) {
		this.ytm = ytm;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getCapRank() {
		return capRank;
	}

	public void setCapRank(String capRank) {
		this.capRank = capRank;
	}

	public List<MasterdailynavDTO> getMasterFundPerformancevsIndexList() {
		return masterFundPerformancevsIndexList;
	}

	public void setMasterFundPerformancevsIndexList(List<MasterdailynavDTO> masterFundPerformancevsIndexList) {
		this.masterFundPerformancevsIndexList = masterFundPerformancevsIndexList;
	}

	public List<MasterdailynavDTO> getMasterBenchMarkPerformancevsIndexList() {
		return masterBenchMarkPerformancevsIndexList;
	}

	public void setMasterBenchMarkPerformancevsIndexList(List<MasterdailynavDTO> masterBenchMarkPerformancevsIndexList) {
		this.masterBenchMarkPerformancevsIndexList = masterBenchMarkPerformancevsIndexList;
	}

	public PortfolioSubAssetBencmarkDto getMasterFundPerformanceTimePeriod() {
		return masterFundPerformanceTimePeriod;
	}

	public void setMasterFundPerformanceTimePeriod(PortfolioSubAssetBencmarkDto masterFundPerformanceTimePeriod) {
		this.masterFundPerformanceTimePeriod = masterFundPerformanceTimePeriod;
	}

	public PortfolioSubAssetBencmarkDto getMasterBenchMarkPerformanceTimePeriod() {
		return masterBenchMarkPerformanceTimePeriod;
	}

	public void setMasterBenchMarkPerformanceTimePeriod(PortfolioSubAssetBencmarkDto masterBenchMarkPerformanceTimePeriod) {
		this.masterBenchMarkPerformanceTimePeriod = masterBenchMarkPerformanceTimePeriod;
	}

	public List<MasterMFSectorHoldingWiseDTO> getMasterMFSectorHoldingWiseDTOList() {
		return masterMFSectorHoldingWiseDTOList;
	}

	public void setMasterMFSectorHoldingWiseDTOList(List<MasterMFSectorHoldingWiseDTO> masterMFSectorHoldingWiseDTOList) {
		this.masterMFSectorHoldingWiseDTOList = masterMFSectorHoldingWiseDTOList;
	}

	public List<PortfolioEquityDailyPriceDto> getMasterStockPerformancevsIndexList() {
		return masterStockPerformancevsIndexList;
	}

	public void setMasterStockPerformancevsIndexList(List<PortfolioEquityDailyPriceDto> masterStockPerformancevsIndexList) {
		this.masterStockPerformancevsIndexList = masterStockPerformancevsIndexList;
	}

	public List<MasterdailynavDTO> getMasterNiftyPerformancevsIndexList() {
		return masterNiftyPerformancevsIndexList;
	}

	public void setMasterNiftyPerformancevsIndexList(List<MasterdailynavDTO> masterNiftyPerformancevsIndexList) {
		this.masterNiftyPerformancevsIndexList = masterNiftyPerformancevsIndexList;
	}

	public PortfolioSubAssetBencmarkDto getMasterStockPerformanceTimePeriod() {
		return masterStockPerformanceTimePeriod;
	}

	public void setMasterStockPerformanceTimePeriod(PortfolioSubAssetBencmarkDto masterStockPerformanceTimePeriod) {
		this.masterStockPerformanceTimePeriod = masterStockPerformanceTimePeriod;
	}

	public PortfolioSubAssetBencmarkDto getMasterNiftyPerformanceTimePeriod() {
		return masterNiftyPerformanceTimePeriod;
	}

	public void setMasterNiftyPerformanceTimePeriod(PortfolioSubAssetBencmarkDto masterNiftyPerformanceTimePeriod) {
		this.masterNiftyPerformanceTimePeriod = masterNiftyPerformanceTimePeriod;
	}

	public Double getOtherCap() {
		return otherCap;
	}

	public void setOtherCap(Double otherCap) {
		this.otherCap = otherCap;
	}

	

	


	

}