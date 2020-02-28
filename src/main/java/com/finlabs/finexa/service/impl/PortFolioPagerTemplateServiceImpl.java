package com.finlabs.finexa.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import com.finlabs.finexa.dto.MasterdailynavDTO;

import com.finlabs.finexa.dto.PortfolioAssetAllocationReviewDto;
import com.finlabs.finexa.dto.PortfolioDetailsDto;
import com.finlabs.finexa.dto.PortfolioEquityDailyPriceDto;
import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
import com.finlabs.finexa.dto.PortfolioSubAssetBencmarkDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.Master52WeekHighLowMF;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterDirectEquityMarketCap;
import com.finlabs.finexa.model.MasterEquityDailyPrice;
import com.finlabs.finexa.model.MasterFundManager;
import com.finlabs.finexa.model.MasterIndexDailyNAV;
import com.finlabs.finexa.model.MasterIndexName;
import com.finlabs.finexa.model.MasterMFAssetAllocation;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMFHolding;
import com.finlabs.finexa.model.MasterMFHoldingMaturity;
import com.finlabs.finexa.model.MasterMFHoldingRatingWise;
import com.finlabs.finexa.model.MasterMFMarketCap;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterRiskFreeRate;
import com.finlabs.finexa.model.MasterSectorBenchmarkMapping;
import com.finlabs.finexa.pm.util.FinanceUtil;
import com.finlabs.finexa.pm.util.FinexaDateUtil;
import com.finlabs.finexa.repository.PortFolioAssetAllocationReviewDAO;
import com.finlabs.finexa.repository.PortFolioPagerDAO;
import com.finlabs.finexa.repository.impl.PortFolioAssetAllocationReviewDAOImpl;
import com.finlabs.finexa.repository.impl.PortFolioPagerTemplateDAOImpl;
import com.finlabs.finexa.service.PortFolioPagerTemplateService;

//@Transactional
//@Service
public class PortFolioPagerTemplateServiceImpl implements PortFolioPagerTemplateService {

	// @Autowired
	// private PortFolioPagerDAO portFolioPagerDAO;

	/*
	 * @Autowired private PortFolioAssetAllocationReviewDAO
	 * portFolioAssetAllocationReviewDAO;
	 */

	Calendar currentDate = Calendar.getInstance();
	Map<String, PortfolioAssetAllocationReviewDto> assetMap = new HashMap<String, PortfolioAssetAllocationReviewDto>();
	Double currentValue = 0.0;
	List<Double> totalStdCal = new ArrayList<>();

	@Override
	public PortfolioPagerproductTemplateDto getclientStockOnePagerTemplate(String ISIN, Session session)
			throws FinexaBussinessException {
		PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = new PortfolioPagerproductTemplateDto();
		List<PortfolioEquityDailyPriceDto> portpolioEquityDailyPriceDtoList = new ArrayList<>();
		List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList = new ArrayList<>();
		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		try {
			MasterDirectEquityMarketCap masterDirectEquityMarketCap = portFolioPagerDAO
					.getMasterDirectEquityMarketCap(ISIN, session);

			System.out.println("masterDirectEquityMarketCap " + masterDirectEquityMarketCap);

			MasterDirectEquity masterDirectEquity = portFolioAssetAllocationReviewDAO.getMasterDirectEquityList(ISIN,
					session);

			portfolioPagerproductTemplateDto.setSector(masterDirectEquity.getMasterMfsector().getSector());
			portfolioPagerproductTemplateDto.setSectorFor(masterDirectEquityMarketCap.getStockName());
			portfolioPagerproductTemplateDto.setBeta(0.0);
			portfolioPagerproductTemplateDto
					.setLastClosingPrice(Double.valueOf(masterDirectEquityMarketCap.getLastClosingPrice()));
			portfolioPagerproductTemplateDto.setMarketCap(Double.valueOf(masterDirectEquityMarketCap.getMarketcap()));
			portfolioPagerproductTemplateDto
					.setWeekHigh52(Double.valueOf(masterDirectEquityMarketCap.getPrice52WeekHigh()));
			portfolioPagerproductTemplateDto
					.setWeekLow52(Double.valueOf(masterDirectEquityMarketCap.getPrice52WeekLow()));
			if (masterDirectEquityMarketCap.getP_e() != null) {
				portfolioPagerproductTemplateDto.setP_e(Double.valueOf(masterDirectEquityMarketCap.getP_e()));
			}
			if (masterDirectEquityMarketCap.getP_b() != null) {
				portfolioPagerproductTemplateDto.setP_b(Double.valueOf(masterDirectEquityMarketCap.getP_b()));
			}
			portfolioPagerproductTemplateDto
					.setDailyTradeValue(Double.valueOf(masterDirectEquityMarketCap.getDailyTradedVolume()));
			if (masterDirectEquityMarketCap.getDivYield() != null) {
				portfolioPagerproductTemplateDto.setDivYield(Double.valueOf(masterDirectEquityMarketCap.getDivYield()));
			}
			List<MasterEquityDailyPrice> masterEquityDailyPriceList = portFolioPagerDAO
					.getMasterEquityDailyPriceList(ISIN, session);
			Calendar currentDate = Calendar.getInstance();
			currentDate.set(Calendar.DAY_OF_MONTH, 26);
			currentDate.set(Calendar.MONTH, 8);
			currentDate.set(Calendar.YEAR, 2013);

			Calendar backDate = Calendar.getInstance();
			backDate.setTime(currentDate.getTime());
			backDate.add(Calendar.YEAR, -2);

			// System.out.println(currentDate.getTime());
			// System.out.println(backDate.getTime());

			for (MasterEquityDailyPrice masterEquityDailyPrice : masterEquityDailyPriceList) {
				if (masterEquityDailyPrice.getId().getDate().before(currentDate.getTime())
						&& masterEquityDailyPrice.getId().getDate().after(backDate.getTime())) {
					PortfolioEquityDailyPriceDto portfolioEquityDailyPriceDto = new PortfolioEquityDailyPriceDto();
					portfolioEquityDailyPriceDto.setClosingPrice(masterEquityDailyPrice.getClosingPrice());
					portfolioEquityDailyPriceDto.setDate(masterEquityDailyPrice.getId().getDate());
					portpolioEquityDailyPriceDtoList.add(portfolioEquityDailyPriceDto);
				}
			}

			// MF performance
			// 1 month from current
			Calendar month1 = FinexaDateUtil.getBackDateFromNow("MONTHS", 1);
			// 3 months from current
			Calendar month3 = FinexaDateUtil.getBackDateFromNow("MONTHS", 3);
			// 6 months from current
			Calendar month6 = FinexaDateUtil.getBackDateFromNow("MONTHS", 6);
			// 1 year from current
			Calendar year1 = FinexaDateUtil.getBackDateFromNow("YEARS", 1);
			// 3 years from current
			Calendar year3 = FinexaDateUtil.getBackDateFromNow("YEARS", 3);
			// 5 years from current
			Calendar year5 = FinexaDateUtil.getBackDateFromNow("YEARS", 5);

			currentDate = Calendar.getInstance();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			currentDate.set(Calendar.MILLISECOND, 0);

			List<MasterSectorBenchmarkMapping> benchMarkList = portFolioAssetAllocationReviewDAO
					.getMasterSectorBenchmarkList(session);

			List<MasterIndexDailyNAV> masterindexdailynavList = new ArrayList<>();

			PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();
			for (MasterSectorBenchmarkMapping masterSecBenchMap : benchMarkList) {
				if (masterSecBenchMap.getMasterMfsector().getSector()
						.equals(portfolioPagerproductTemplateDto.getSector())) {
					portfolioSubAssetBencmarkDto.setBenchMark(portfolioPagerproductTemplateDto.getSector());
					masterindexdailynavList = portFolioAssetAllocationReviewDAO.getMasterindexdailynavList(masterSecBenchMap.getMasterIndexName().getId(),session);
					for (MasterIndexDailyNAV masterindexdailynav : masterindexdailynavList) {
						
						if (currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {

							currentValue = masterindexdailynav.getNav().doubleValue();

						}

						if (masterindexdailynav.getId().getMasterIndexName().getId() == masterSecBenchMap.getId()) {

							if (month1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth1Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (month3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth3Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (month6.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth6Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear1Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear3Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year5.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear5Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}
						}

					}
					break;
				}

			}
			portfolioSubAssetBencmarkDtoList.add(portfolioSubAssetBencmarkDto);

			for (MasterEquityDailyPrice masterEquityDailyPrice : masterEquityDailyPriceList) {
				if (currentDate.getTime().compareTo(masterEquityDailyPrice.getId().getDate()) == 0) {
					currentValue = (double) masterEquityDailyPrice.getClosingPrice();
				}

				if (month1.getTime().compareTo(masterEquityDailyPrice.getId().getDate()) == 0) {
					portfolioSubAssetBencmarkDto
							.setMonth1Value(currentValue / masterEquityDailyPrice.getClosingPrice());
				}

				if (month3.getTime().compareTo(masterEquityDailyPrice.getId().getDate()) == 0) {
					portfolioSubAssetBencmarkDto
							.setMonth3Value(currentValue / masterEquityDailyPrice.getClosingPrice());
				}

				if (month6.getTime().compareTo(masterEquityDailyPrice.getId().getDate()) == 0) {
					portfolioSubAssetBencmarkDto
							.setMonth6Value(currentValue / masterEquityDailyPrice.getClosingPrice());
				}

				if (year1.getTime().compareTo(masterEquityDailyPrice.getId().getDate()) == 0) {
					portfolioSubAssetBencmarkDto.setYear1Value(currentValue / masterEquityDailyPrice.getClosingPrice());
				}

				if (year3.getTime().compareTo(masterEquityDailyPrice.getId().getDate()) == 0) {
					portfolioSubAssetBencmarkDto.setYear3Value(currentValue / masterEquityDailyPrice.getClosingPrice());
				}

				if (year5.getTime().compareTo(masterEquityDailyPrice.getId().getDate()) == 0) {
					portfolioSubAssetBencmarkDto.setYear5Value(currentValue / masterEquityDailyPrice.getClosingPrice());
				}

			}
			portfolioSubAssetBencmarkDto.setBenchMark("Stock");
			portfolioSubAssetBencmarkDtoList.add(portfolioSubAssetBencmarkDto);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		portfolioPagerproductTemplateDto.setPortpolioEquityDailyPriceDtoList(portpolioEquityDailyPriceDtoList);
		portfolioPagerproductTemplateDto.setPortfolioSubAssetBencmarkDtoList(portfolioSubAssetBencmarkDtoList);
		return portfolioPagerproductTemplateDto;
	}

	@Override
	public PortfolioPagerproductTemplateDto getclientMFOnePagerTemplateE(String ISIN, Session session)
			throws FinexaBussinessException {
		PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = new PortfolioPagerproductTemplateDto();
		List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList = new ArrayList<>();

		// for slop calcualtion in risk measure
		List<Double> fundValueList = new ArrayList<>();
		List<Double> benchMarkValueList = new ArrayList<>();
		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();
		// list for port folio details
		List<PortfolioDetailsDto> portfolioDetailsList = new ArrayList<>();
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		try {
			MasterMutualFundETF masterMutualFundETF = portFolioPagerDAO.getMasterMutualFundETF(ISIN, session);
			portfolioPagerproductTemplateDto.setSchemeinceptionDate(masterMutualFundETF.getSchemeInceptionDate());
			portfolioPagerproductTemplateDto
					.setSubAssetClass(masterMutualFundETF.getLookupAssetSubClass().getDescription());
			// portfolioPagerproductTemplateDto.setFundManagers(masterMutualFundETF.getMasterFundManager().getAction());
			portfolioPagerproductTemplateDto.setBenchmarkIndex(masterMutualFundETF.getMasterIndexName().getName());
			portfolioPagerproductTemplateDto
					.setMinInvestmentAmount((double) masterMutualFundETF.getMinInvestmentAmount());
			portfolioPagerproductTemplateDto.setExitLoadPeriod(masterMutualFundETF.getExitLoadAndPeriod());

			List<MasterMFAssetAllocation> masterMfAssetAllocationList = portFolioPagerDAO
					.getMasterMfAssetAllocationList(ISIN, session);
			portfolioPagerproductTemplateDto.setAum(String.valueOf(masterMfAssetAllocationList.get(0).getAum()));
			portfolioPagerproductTemplateDto
					.setExpenseRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getExpenseRatio()));
			Master52WeekHighLowMF master52WeekHighLowMf = portFolioPagerDAO.getMaster52WeekHighLowMf(ISIN, session);
			portfolioPagerproductTemplateDto.setWeekLow52(master52WeekHighLowMf.getNav52WeekLow().doubleValue());
			portfolioPagerproductTemplateDto.setWeekHigh52(master52WeekHighLowMf.getNav52WeekHigh().doubleValue());

			List<MasterMFMarketCap> masterMfMarketcapList = portFolioPagerDAO.getMasterMfMarketcapList(ISIN, session);
			portfolioPagerproductTemplateDto.setPe(masterMfMarketcapList.get(0).getPe().doubleValue());
			portfolioPagerproductTemplateDto.setPb(masterMfMarketcapList.get(0).getPb().doubleValue());

			// 1 month from current
			Calendar month1 = FinexaDateUtil.getBackDateFromNow("MONTHS", 1);
			// 3 months from current
			Calendar month3 = FinexaDateUtil.getBackDateFromNow("MONTHS", 3);
			// 6 months from current
			Calendar month6 = FinexaDateUtil.getBackDateFromNow("MONTHS", 6);
			// 1 year from current
			Calendar year1 = FinexaDateUtil.getBackDateFromNow("YEARS", 1);
			// 3 years from current
			Calendar year3 = FinexaDateUtil.getBackDateFromNow("YEARS", 3);
			// 5 years from current
			Calendar year5 = FinexaDateUtil.getBackDateFromNow("YEARS", 5);

			currentDate = Calendar.getInstance();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			currentDate.set(Calendar.MILLISECOND, 0);
			List<Double> riskmeasuresDataList = new ArrayList<>();
			List<MasterSectorBenchmarkMapping> benchMarkList = portFolioAssetAllocationReviewDAO
					.getMasterSectorBenchmarkList(session);
			List<MasterIndexDailyNAV> masterindexdailynavList = new ArrayList<>();
			List<MasterMFDailyNAV> MasterMfDailyNavList = portFolioPagerDAO.getMasterMfDailyNavList(ISIN, session);
			PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();
			for (MasterSectorBenchmarkMapping masterSecBenchMap : benchMarkList) {
				if (masterSecBenchMap.getMasterMfsector().getSector()
						.equals(portfolioPagerproductTemplateDto.getSector())) {
					portfolioSubAssetBencmarkDto.setBenchMark(portfolioPagerproductTemplateDto.getSector());
					masterindexdailynavList = portFolioAssetAllocationReviewDAO.getMasterindexdailynavList(masterSecBenchMap.getMasterIndexName().getId(),session);
					for (MasterIndexDailyNAV masterindexdailynav : masterindexdailynavList) {
						if (masterindexdailynav.getId().getMasterIndexName().getId() == Integer
								.parseInt(masterSecBenchMap.getMasterIndexName().getName())
								&& currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
							currentValue = masterindexdailynav.getNav().doubleValue();
						}

						if (masterindexdailynav.getId().getMasterIndexName().getId() == masterSecBenchMap.getId()) {

							if (month1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth1Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (month3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth3Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (month6.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth6Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear1Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear3Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year5.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear5Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}
							// calculating information ratio in risk measure
							if ((year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
									|| (currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
									|| (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 1
											&& currentDate.getTime()
													.compareTo(masterindexdailynav.getId().getDate()) == -1)) {
								totalStdCal.add(masterindexdailynav.getNav().doubleValue());
								fundValueList.add(masterindexdailynav.getNav().doubleValue());
								for (MasterMFDailyNAV masterMfDailyNav : MasterMfDailyNavList) {
									if ((masterMfDailyNav.getId().getDate()
											.compareTo(masterindexdailynav.getId().getDate()) == 0)) {
										riskmeasuresDataList.add(
												masterMfDailyNav.getNav() - masterindexdailynav.getNav().doubleValue());
									}

								}

							}

						}

					}

				}
				portfolioPagerproductTemplateDto.setInformationRatio(
						FinanceUtil.AVERAGE(riskmeasuresDataList) / FinanceUtil.STDEV(riskmeasuresDataList));
				Double lastcurrentData = null;
				Double dailyReturns = 0.0;
				List<MasterRiskFreeRate> masterRiskFreeRateList = portFolioPagerDAO.getMasterRiskFreeRateList(session);
				riskmeasuresDataList = new ArrayList<>();
				for (MasterMFDailyNAV masterMfDailyNav : MasterMfDailyNavList) {
					if (currentDate.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						currentValue = Double.valueOf(masterMfDailyNav.getNav());
					}

					if (month1.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setMonth1Value(currentValue / masterMfDailyNav.getNav());
					}

					if (month3.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setMonth3Value(currentValue / masterMfDailyNav.getNav());
					}

					if (month6.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setMonth6Value(currentValue / masterMfDailyNav.getNav());
					}

					if (year1.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setYear1Value(currentValue / masterMfDailyNav.getNav());
					}

					if (year3.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setYear3Value(currentValue / masterMfDailyNav.getNav());
					}

					if (year5.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setYear5Value(currentValue / masterMfDailyNav.getNav());
					}
					// calculating shape ratio in risk measure
					if ((year3.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0)
							|| (currentDate.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0)
							|| (year3.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 1
									&& currentDate.getTime().compareTo(masterMfDailyNav.getId().getDate()) == -1)) {
						totalStdCal.add(Double.valueOf(masterMfDailyNav.getNav()));
						benchMarkValueList.add(Double.valueOf(masterMfDailyNav.getNav()));
						if (lastcurrentData != null) {
							dailyReturns = lastcurrentData / (masterMfDailyNav.getNav() - 1);
							for (MasterRiskFreeRate masterRiskFreeRate : masterRiskFreeRateList) {
								if ((masterMfDailyNav.getId().getDate().compareTo(masterRiskFreeRate.getDate()) == 0)) {
									riskmeasuresDataList.add(
											dailyReturns - (masterRiskFreeRate.getRiskfreeRate().doubleValue() / 365));
								}
							}
						}

					}

				}

				// risk measures data
				portfolioPagerproductTemplateDto.setStdDeviation(FinanceUtil.STDEV(totalStdCal));
				portfolioPagerproductTemplateDto.setSharpeRatio(
						FinanceUtil.AVERAGE(riskmeasuresDataList) / FinanceUtil.STDEV(riskmeasuresDataList));
				portfolioPagerproductTemplateDto
						.setBeta(FinanceUtil.SLOPE(fundValueList.toArray(new Double[fundValueList.size()]),
								benchMarkValueList.toArray(new Double[benchMarkValueList.size()])));

				// Portfolio details
				// Note :- ISIN needed

				List<MasterMFHolding> masterMfHoldingList = portFolioPagerDAO.getMasterMfHoldingList(ISIN, session);
				for (MasterMFHolding masterMfHolding : masterMfHoldingList) {
					PortfolioDetailsDto portfolioDetailsDto = new PortfolioDetailsDto();
					// company code to be maped to get company name;
					// portfolioDetailsDto.setHoldings(masterMfHolding.getCompanyCode());

					// ISIN is need in amf holding to get company sector
					// portfolioDetailsDto.setSector();
					portfolioDetailsDto.setPercOfPortfolio(masterMfHolding.getAssetPercentage().doubleValue());
					portfolioDetailsList.add(portfolioDetailsDto);
				}
				portfolioSubAssetBencmarkDtoList.add(portfolioSubAssetBencmarkDto);
			}
		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		portfolioPagerproductTemplateDto.setPortfolioDetailsList(portfolioDetailsList);
		portfolioPagerproductTemplateDto.setPortfolioSubAssetBencmarkDtoList(portfolioSubAssetBencmarkDtoList);
		return portfolioPagerproductTemplateDto;
	}

	@Override
	public PortfolioPagerproductTemplateDto getclientMFOnePagerTemplateD(String ISIN, Session session)
			throws FinexaBussinessException {
		PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = new PortfolioPagerproductTemplateDto();
		List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList = new ArrayList<>();

		// for slop calcualtion in risk measure
		List<Double> fundValueList = new ArrayList<>();
		List<Double> benchMarkValueList = new ArrayList<>();

		// list for port folio details
		List<PortfolioDetailsDto> portfolioDetailsList = new ArrayList<>();
		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		try {
			MasterMutualFundETF masterMutualFundETF = portFolioPagerDAO.getMasterMutualFundETF(ISIN, session);
			portfolioPagerproductTemplateDto.setSchemeinceptionDate(masterMutualFundETF.getSchemeInceptionDate());
			portfolioPagerproductTemplateDto
					.setSubAssetClass(masterMutualFundETF.getLookupAssetSubClass().getDescription());
			// portfolioPagerproductTemplateDto.setFundManagers(masterMutualFundETF.getMasterFundManager().getAction());
			portfolioPagerproductTemplateDto.setBenchmarkIndex(masterMutualFundETF.getMasterIndexName().getName());
			portfolioPagerproductTemplateDto
					.setMinInvestmentAmount((double) masterMutualFundETF.getMinInvestmentAmount());
			portfolioPagerproductTemplateDto.setExitLoadPeriod(masterMutualFundETF.getExitLoadAndPeriod());

			List<MasterMFAssetAllocation> masterMfAssetAllocationList = portFolioPagerDAO
					.getMasterMfAssetAllocationList(ISIN, session);
			portfolioPagerproductTemplateDto.setAum(String.valueOf(masterMfAssetAllocationList.get(0).getAum()));
			portfolioPagerproductTemplateDto
					.setExpenseRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getExpenseRatio()));

			// Asset Allocation Pie chart
			portfolioPagerproductTemplateDto
					.setEquityRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getEquityRatio()));
			portfolioPagerproductTemplateDto
					.setDebtRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getDebtRatio()));
			portfolioPagerproductTemplateDto
					.setOtherRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getOthersRatio()));

			// Rating wise breakup pie chart
			Map<String, Double> ratinwiseMap = new HashMap<>();
			List<MasterMFHoldingRatingWise> masterMfHoldingRatingWiselist = portFolioAssetAllocationReviewDAO
					.getMasterMfHoldingRatingWiseList(ISIN, session);
			for (MasterMFHoldingRatingWise masterMfHoldingRatingWise : masterMfHoldingRatingWiselist) {
				ratinwiseMap.put(masterMfHoldingRatingWise.getSchemeName(),
						Double.valueOf(masterMfHoldingRatingWise.getHolding()));
			}

			Master52WeekHighLowMF master52WeekHighLowMf = portFolioPagerDAO.getMaster52WeekHighLowMf(ISIN, session);
			portfolioPagerproductTemplateDto.setWeekLow52(master52WeekHighLowMf.getNav52WeekLow().doubleValue());
			portfolioPagerproductTemplateDto.setWeekHigh52(master52WeekHighLowMf.getNav52WeekHigh().doubleValue());

			List<MasterMFHoldingMaturity> masterMfHoldingMaturityList = portFolioPagerDAO
					.getMasterMfHoldingMaturityList(ISIN, session);
			portfolioPagerproductTemplateDto.setYtm(Double.valueOf(masterMfHoldingMaturityList.get(0).getYtm()));
			portfolioPagerproductTemplateDto
					.setDuration(Double.valueOf(masterMfHoldingMaturityList.get(0).getDuration()));

			// 1 month from current
			Calendar month1 = FinexaDateUtil.getBackDateFromNow("MONTHS", 1);
			// 3 months from current
			Calendar month3 = FinexaDateUtil.getBackDateFromNow("MONTHS", 3);
			// 6 months from current
			Calendar month6 = FinexaDateUtil.getBackDateFromNow("MONTHS", 6);
			// 1 year from current
			Calendar year1 = FinexaDateUtil.getBackDateFromNow("YEARS", 1);
			// 3 years from current
			Calendar year3 = FinexaDateUtil.getBackDateFromNow("YEARS", 3);
			// 5 years from current
			Calendar year5 = FinexaDateUtil.getBackDateFromNow("YEARS", 5);

			currentDate = Calendar.getInstance();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			currentDate.set(Calendar.MILLISECOND, 0);
			List<Double> riskmeasuresDataList = new ArrayList<>();
			List<MasterSectorBenchmarkMapping> benchMarkList = portFolioAssetAllocationReviewDAO
					.getMasterSectorBenchmarkList(session);
			List<MasterIndexDailyNAV> masterindexdailynavList = new ArrayList<>();
			List<MasterMFDailyNAV> MasterMfDailyNavList = portFolioPagerDAO.getMasterMfDailyNavList(ISIN, session);
			PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();
			for (MasterSectorBenchmarkMapping masterSecBenchMap : benchMarkList) {
				if (masterSecBenchMap.getMasterMfsector().getSector()
						.equals(portfolioPagerproductTemplateDto.getSector())) {
					portfolioSubAssetBencmarkDto.setBenchMark(portfolioPagerproductTemplateDto.getSector());
					masterindexdailynavList = portFolioAssetAllocationReviewDAO.getMasterindexdailynavList(masterSecBenchMap.getMasterIndexName().getId(),session);
					for (MasterIndexDailyNAV masterindexdailynav : masterindexdailynavList) {
						if (masterindexdailynav.getId().getMasterIndexName().getId() == Integer
								.parseInt(masterSecBenchMap.getMasterIndexName().getName())
								&& currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
							currentValue = masterindexdailynav.getNav().doubleValue();
						}

						if (masterindexdailynav.getId().getMasterIndexName().getId() == masterSecBenchMap.getId()) {

							if (month1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth1Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (month3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth3Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (month6.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setMonth6Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear1Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear3Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}

							if (year5.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								portfolioSubAssetBencmarkDto
										.setYear5Value(currentValue / masterindexdailynav.getNav().doubleValue());
							}
							// calculating information ratio in risk measure
							if ((year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
									|| (currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
									|| (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 1
											&& currentDate.getTime()
													.compareTo(masterindexdailynav.getId().getDate()) == -1)) {
								totalStdCal.add(masterindexdailynav.getNav().doubleValue());
								fundValueList.add(masterindexdailynav.getNav().doubleValue());
								for (MasterMFDailyNAV masterMfDailyNav : MasterMfDailyNavList) {
									if ((masterMfDailyNav.getId().getDate()
											.compareTo(masterindexdailynav.getId().getDate()) == 0)) {
										riskmeasuresDataList.add(
												masterMfDailyNav.getNav() - masterindexdailynav.getNav().doubleValue());
									}

								}

							}

						}

					}

				}
				portfolioPagerproductTemplateDto.setInformationRatio(
						FinanceUtil.AVERAGE(riskmeasuresDataList) / FinanceUtil.STDEV(riskmeasuresDataList));
				Double lastcurrentData = null;
				Double dailyReturns = 0.0;
				List<MasterRiskFreeRate> masterRiskFreeRateList = portFolioPagerDAO.getMasterRiskFreeRateList(session);
				riskmeasuresDataList = new ArrayList<>();
				for (MasterMFDailyNAV masterMfDailyNav : MasterMfDailyNavList) {
					if (currentDate.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						currentValue = Double.valueOf(masterMfDailyNav.getNav());
					}

					if (month1.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setMonth1Value(currentValue / masterMfDailyNav.getNav());
					}

					if (month3.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setMonth3Value(currentValue / masterMfDailyNav.getNav());
					}

					if (month6.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setMonth6Value(currentValue / masterMfDailyNav.getNav());
					}

					if (year1.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setYear1Value(currentValue / masterMfDailyNav.getNav());
					}

					if (year3.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setYear3Value(currentValue / masterMfDailyNav.getNav());
					}

					if (year5.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0) {
						portfolioSubAssetBencmarkDto.setYear5Value(currentValue / masterMfDailyNav.getNav());
					}
					// calculating shape ratio in risk measure
					if ((year3.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0)
							|| (currentDate.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 0)
							|| (year3.getTime().compareTo(masterMfDailyNav.getId().getDate()) == 1
									&& currentDate.getTime().compareTo(masterMfDailyNav.getId().getDate()) == -1)) {
						totalStdCal.add(Double.valueOf(masterMfDailyNav.getNav()));
						benchMarkValueList.add(Double.valueOf(masterMfDailyNav.getNav()));
						if (lastcurrentData != null) {
							dailyReturns = lastcurrentData / (masterMfDailyNav.getNav() - 1);
							for (MasterRiskFreeRate masterRiskFreeRate : masterRiskFreeRateList) {
								if ((masterMfDailyNav.getId().getDate().compareTo(masterRiskFreeRate.getDate()) == 0)) {
									riskmeasuresDataList.add(
											dailyReturns - (masterRiskFreeRate.getRiskfreeRate().doubleValue() / 365));
								}
							}
						}

					}

				}

				// risk measures data
				portfolioPagerproductTemplateDto.setStdDeviation(FinanceUtil.STDEV(totalStdCal));
				portfolioPagerproductTemplateDto.setSharpeRatio(
						FinanceUtil.AVERAGE(riskmeasuresDataList) / FinanceUtil.STDEV(riskmeasuresDataList));
				portfolioPagerproductTemplateDto
						.setBeta(FinanceUtil.SLOPE(fundValueList.toArray(new Double[fundValueList.size()]),
								benchMarkValueList.toArray(new Double[benchMarkValueList.size()])));

				// Portfolio details
				// Note :- ISIN needed

				List<MasterMFHolding> masterMfHoldingList = portFolioPagerDAO.getMasterMfHoldingList(ISIN, session);
				for (MasterMFHolding masterMfHolding : masterMfHoldingList) {
					PortfolioDetailsDto portfolioDetailsDto = new PortfolioDetailsDto();
					// company code to be maped to get company name;
					// portfolioDetailsDto.setHoldings(masterMfHolding.getCompanyCode());

					// ISIN is need in amf holding to get company sector
					// portfolioDetailsDto.setSector();
					portfolioDetailsDto.setPercOfPortfolio(masterMfHolding.getAssetPercentage().doubleValue());
					portfolioDetailsList.add(portfolioDetailsDto);
				}
				portfolioSubAssetBencmarkDtoList.add(portfolioSubAssetBencmarkDto);
			}
		} catch (FinexaDaoException e) {
			throw new FinexaBussinessException("", "", "", e);
		}

		portfolioPagerproductTemplateDto.setPortfolioDetailsList(portfolioDetailsList);
		portfolioPagerproductTemplateDto.setPortfolioSubAssetBencmarkDtoList(portfolioSubAssetBencmarkDtoList);
		return portfolioPagerproductTemplateDto;
	}

	@Override
	public List<PortfolioEquityDailyPriceDto> getMasterEquityDailyPriceListOnDate(String ISIN, String startDate,
			String endDate, Session session) throws FinexaBussinessException {
		PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = new PortfolioPagerproductTemplateDto();
		List<PortfolioEquityDailyPriceDto> portpolioEquityDailyPriceDtoList = new ArrayList<>();

		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();

		PortfolioEquityDailyPriceDto portfolioEquityDailyPriceDto;
		try {
			Date startDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			Date endDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

			// System.out.println("startDate1 "+startDate1);
			// System.out.println("endDate1 "+endDate1);

			List<MasterEquityDailyPrice> masterEquityDailyPriceList = portFolioPagerDAO
					.getMasterEquityDailyPriceListOnDate(ISIN, startDate1, endDate1, session);

			// System.out.println("masterEquityDailyPriceList
			// "+masterEquityDailyPriceList.size());

			for (MasterEquityDailyPrice masterEquityDailyPrice : masterEquityDailyPriceList) {

				portfolioEquityDailyPriceDto = new PortfolioEquityDailyPriceDto();
				portfolioEquityDailyPriceDto.setClosingPrice(masterEquityDailyPrice.getClosingPrice());
				portfolioEquityDailyPriceDto.setDate(masterEquityDailyPrice.getId().getDate());
				portpolioEquityDailyPriceDtoList.add(portfolioEquityDailyPriceDto);

			}

			// System.out.println("portpolioEquityDailyPriceDtoList
			// "+portpolioEquityDailyPriceDtoList.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return portpolioEquityDailyPriceDtoList;
	}

	@Override
	public List<MasterdailynavDTO> getMasterindexdailynavListOnDate(String name, String startDate, String endDate,
			Session session) throws FinexaBussinessException {
		PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = new PortfolioPagerproductTemplateDto();
		List<MasterdailynavDTO> masterindexdailynavDTOList = new ArrayList<>();

		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();

		MasterdailynavDTO masterindexdailynavDTO;
		try {
			Date startDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			Date endDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

			// System.out.println("startDate1 "+startDate1);
			// System.out.println("endDate1 "+endDate1);

			List<MasterIndexDailyNAV> masterIndexDailyNAVList = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					startDate1, endDate1, session);

			// System.out.println("masterindexdailynavDTOList
			// "+masterIndexDailyNAVList.size());

			for (MasterIndexDailyNAV masterIndexDailyNAV : masterIndexDailyNAVList) {

				masterindexdailynavDTO = new MasterdailynavDTO();
				masterindexdailynavDTO.setNAV(masterIndexDailyNAV.getNav().doubleValue());
				masterindexdailynavDTO.setDate(masterIndexDailyNAV.getId().getDate());
				masterindexdailynavDTOList.add(masterindexdailynavDTO);

			}

			// System.out.println("masterindexdailynavDTOList
			// "+masterindexdailynavDTOList.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return masterindexdailynavDTOList;
	}

	@Override
	public List<PortfolioSubAssetBencmarkDto> getMasterEquityDailyPriceListTimePeriod(String ISIN, String name,
			Session session) throws FinexaBussinessException {
		double month1Value = 0;
		double month3Value = 0;
		double month6Value = 0;
		double year1Value = 0;
		double year3Value = 0;
		double div = 0;
		double c = 0;
		double year5Value = 0;
		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();
		PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto;
		List<PortfolioSubAssetBencmarkDto> PortfolioSubAssetBencmarkDtoList = new ArrayList<PortfolioSubAssetBencmarkDto>();

		try {
			Calendar currentDate = Calendar.getInstance();
			currentDate.add(Calendar.DATE, -1);

			// extra
			/*
			 * currentDate.set(Calendar.DAY_OF_MONTH, 28); currentDate.set(Calendar.MONTH,
			 * 2); currentDate.set(Calendar.YEAR, 2018);
			 */
			// 1 month from current
			Calendar month1 = FinexaDateUtil.getBackDateFromNow("MONTHS", 1);
			// 3 months from current
			Calendar month3 = FinexaDateUtil.getBackDateFromNow("MONTHS", 3);
			// 6 months from current
			Calendar month6 = FinexaDateUtil.getBackDateFromNow("MONTHS", 6);
			// 1 year from current
			Calendar year1 = FinexaDateUtil.getBackDateFromNow("YEARS", 1);
			// 3 years from current
			Calendar year3 = FinexaDateUtil.getBackDateFromNow("YEARS", 3);
			// 5 years from current
			Calendar year5 = FinexaDateUtil.getBackDateFromNow("YEARS", 5);
			// ============================================================
			// month1

			portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();

			Date curDate = currentDate.getTime();
			Date toDate = month1.getTime();

			List<MasterEquityDailyPrice> masterEquityDailyPrice = portFolioPagerDAO
					.getMasterEquityDailyPriceListTimePeriod(curDate, toDate, ISIN, session);

			if (!masterEquityDailyPrice.isEmpty()) {

				month1Value = (masterEquityDailyPrice.get(0).getClosingPrice()
						- masterEquityDailyPrice.get(1).getClosingPrice())
						/ masterEquityDailyPrice.get(1).getClosingPrice();

			}
			portfolioSubAssetBencmarkDto.setMonth1Value(month1Value * 100);
			// ==============================
			// month3
			toDate = month3.getTime();

			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyPriceListTimePeriod(curDate, toDate, ISIN,
					session);

			if (masterEquityDailyPrice.size()==2) {

				month3Value = (masterEquityDailyPrice.get(0).getClosingPrice()
						- masterEquityDailyPrice.get(1).getClosingPrice())
						/ masterEquityDailyPrice.get(1).getClosingPrice();

			}
			portfolioSubAssetBencmarkDto.setMonth3Value(month3Value * 100);
			// ==================================================
			// month6
			toDate = month6.getTime();

			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyPriceListTimePeriod(curDate, toDate, ISIN,
					session);

			if (masterEquityDailyPrice.size()==2) {

				month6Value = (masterEquityDailyPrice.get(0).getClosingPrice()
						- masterEquityDailyPrice.get(1).getClosingPrice())
						/ masterEquityDailyPrice.get(1).getClosingPrice();

			}

			portfolioSubAssetBencmarkDto.setMonth6Value(month6Value * 100);
			// ==============================================================
			// year1

			System.out.println("year1.getTime() " + year1.getTime());

			toDate = year1.getTime();

			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyPriceListTimePeriod(curDate, toDate, ISIN,
					session);

			if (masterEquityDailyPrice.size()==2) {

				year1Value = (masterEquityDailyPrice.get(0).getClosingPrice()
						- masterEquityDailyPrice.get(1).getClosingPrice())
						/ masterEquityDailyPrice.get(1).getClosingPrice();

			}
			portfolioSubAssetBencmarkDto.setYear1Value(year1Value * 100);
			// ============================

			// ========================================================
			// year3
			System.out.println("year3.getTime() " + year3.getTime());

			toDate = year3.getTime();

			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyPriceListTimePeriod(curDate, toDate, ISIN,
					session);

			if (masterEquityDailyPrice.size()==2) {

				div = masterEquityDailyPrice.get(0).getClosingPrice().doubleValue()
						/ masterEquityDailyPrice.get(1).getClosingPrice().doubleValue();

				c = Math.pow(div, (1 / 3));

				year3Value = c - 1;

			}
			portfolioSubAssetBencmarkDto.setYear3Value(year3Value * 100);

			// =========================================================
			// year5
			System.out.println("year5.getTime() " + year5.getTime());

			toDate = year5.getTime();

			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyPriceListTimePeriod(curDate, toDate, ISIN,
					session);

			if (masterEquityDailyPrice.size()==2) {

				div = masterEquityDailyPrice.get(0).getClosingPrice().doubleValue()
						/ masterEquityDailyPrice.get(1).getClosingPrice().doubleValue();

				c = Math.pow(div, (1 / 5));

				year5Value = c - 1;

			}
			portfolioSubAssetBencmarkDto.setYear5Value(year5Value * 100);
			// =================================
			portfolioSubAssetBencmarkDto.setBenchMark("Stock");
			PortfolioSubAssetBencmarkDtoList.add(portfolioSubAssetBencmarkDto);

			// ============================================================
			// month1
			portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();

			curDate = currentDate.getTime();
			toDate = month1.getTime();

			List<MasterIndexDailyNAV> masterIndexDailyNAVList = portFolioPagerDAO
					.getMasterIndexDailyNAVListTimePeriod(curDate, toDate, name, session);

			if (masterIndexDailyNAVList.size()==2) {

				month1Value = (masterIndexDailyNAVList.get(0).getNav().doubleValue()
						- masterIndexDailyNAVList.get(1).getNav().doubleValue())
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

			}
			portfolioSubAssetBencmarkDto.setMonth1Value(month1Value * 100);

			// ==============================
			// month3

			toDate = month3.getTime();

			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVListTimePeriod(curDate, toDate, name,
					session);
			if (masterIndexDailyNAVList.size()==2) {

				month3Value = (masterIndexDailyNAVList.get(0).getNav().doubleValue()
						- masterIndexDailyNAVList.get(1).getNav().doubleValue())
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

			}
			portfolioSubAssetBencmarkDto.setMonth3Value(month3Value * 100);

			// ==================================================
			// month6

			toDate = month6.getTime();
			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVListTimePeriod(curDate, toDate, name,
					session);

			if (masterIndexDailyNAVList.size()==2) {

				month6Value = (masterIndexDailyNAVList.get(0).getNav().doubleValue()
						- masterIndexDailyNAVList.get(1).getNav().doubleValue())
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

			}
			portfolioSubAssetBencmarkDto.setMonth6Value(month6Value * 100);

			// year1================
			toDate = year1.getTime();

			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVListTimePeriod(curDate, toDate, name,
					session);

			if (masterIndexDailyNAVList.size()==2) {

				year1Value = (masterIndexDailyNAVList.get(0).getNav().doubleValue()
						- masterIndexDailyNAVList.get(1).getNav().doubleValue())
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

			}
			portfolioSubAssetBencmarkDto.setYear1Value(year1Value * 100);
			// ========================================================
			// year3

			toDate = year3.getTime();

			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVListTimePeriod(curDate, toDate, name,
					session);

			if (masterIndexDailyNAVList.size()==2) {

				div = masterIndexDailyNAVList.get(0).getNav().doubleValue()
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

				c = Math.pow(div, (1 / 3));

				year3Value = c - 1;
				// System.out.println("year1Value "+year1Value);
			}
			portfolioSubAssetBencmarkDto.setYear3Value(year3Value * 100);

			// ============================
			// year5
			toDate = year5.getTime();

			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVListTimePeriod(curDate, toDate, name,
					session);

			if (masterIndexDailyNAVList.size()==2) {

				div = masterIndexDailyNAVList.get(0).getNav().doubleValue()
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

				c = Math.pow(div, (1 / 5));

				year5Value = c - 1;
				// System.out.println("year5Value "+year5Value);
			}
			portfolioSubAssetBencmarkDto.setYear5Value(year5Value * 100);
			// ========================================
			portfolioSubAssetBencmarkDto.setBenchMark("Nifty");
			PortfolioSubAssetBencmarkDtoList.add(portfolioSubAssetBencmarkDto);
			// System.out.println("size "+PortfolioSubAssetBencmarkDtoList.size());
			// ======================================

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return PortfolioSubAssetBencmarkDtoList;

	}
	
	
	
}