package com.finlabs.finexa.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import com.finlabs.finexa.dto.PortfolioAssetAllocationReviewDto;
import com.finlabs.finexa.dto.PortfolioHistoricalReturnOutput;
import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.dto.PortfolioSubAssetBencmarkDto;
import com.finlabs.finexa.dto.ProductRecommendationOutputPM;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.genericDao.GenericDao;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupAssetAllocation;
import com.finlabs.finexa.model.MasterAssetClassReturn;
import com.finlabs.finexa.model.MasterExpectedHistoricalReturn;
import com.finlabs.finexa.model.MasterIndexDailyNAV;
import com.finlabs.finexa.model.MasterMFProductRecommendation;
import com.finlabs.finexa.model.MasterSubAssetClassReturn;
import com.finlabs.finexa.pm.util.FinexaConstant;
import com.finlabs.finexa.repository.ClientMasterDAO;
import com.finlabs.finexa.repository.PortFolioAssetAllocationReviewDAO;
import com.finlabs.finexa.repository.impl.ClientMasterDAOImpl;
import com.finlabs.finexa.repository.impl.PortFolioAssetAllocationReviewDAOImpl;
import com.finlabs.finexa.service.PortFolioAssetAllocationReviewService;
import com.finlabs.finexa.service.PortFolioOverviewService;
import com.finlabs.finexa.util.FinanceUtil;

//@Transactional
//@Service
public class PortFolioAssetAllocationReviewServiceImpl implements PortFolioAssetAllocationReviewService {

	// @Autowired
	// private PortFolioOverviewService portFolioOverviewService;

	// @Autowired
	// private PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO;

	/*
	 * @Autowired private ClientMasterDAO clientMasterDao;
	 */

	private Calendar currentDate = Calendar.getInstance();
	@Autowired
	private CacheInfoService cacheInfoService;
	private Double calculateNav = null;
	private boolean navFound = false;

	@SuppressWarnings("unchecked")
	@Override
	public List<PortfolioAssetAllocationReviewDto> getclientPortfolioAssetAllocationReview(String token, int clientId,
			Session session, CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<PortfolioAssetAllocationReviewDto> portfolioAssetAllocationReviewDtoDtoList = null;
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		Map<String, PortfolioAssetAllocationReviewDto> assetMap = new HashMap<String, PortfolioAssetAllocationReviewDto>();
		List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
		Map<String, Double> multipleProductsubassetMap = new HashMap<String, Double>();
		PortFolioOverviewService portFolioOverviewService = new PortFolioOverviewServiceImpl();
		ClientMasterDAO clientMasterDao = new ClientMasterDAOImpl();
		try {
			portfolioOverviewDtoList = (List<PortfolioOverviewDto>) cacheInfoService.getCacheList(
					FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
					FinexaConstant.CALCULATION_SUB_TYPE_PORTFOLIO_CONSTANT);

			if (portfolioOverviewDtoList == null || portfolioOverviewDtoList.isEmpty()) {
				portfolioOverviewDtoList = portFolioOverviewService.getclientPortfolioOverview(token, clientId, "", session,
						cacheInfoService);
			}
			List<ClientMaster> clientMasterList = clientMasterDao.getAllClientsById(clientId, session);
			double totalCurrentBal = 0;
			double totalInvestmentBal = 0;
			double totalGain = 0;
			double totalportfolioPerc = 0;
			double totalprofLoss = 0;
			double totalrecommendPerc = 0;
			double totalMultiplerecommendPerc = 0;
			double totalexpectedReturn = 0;
			double totalXirr = 0.0;
			double totalRecommendated = 0d;
			double totalExpectedCurrentAllocation = 0d;
			double totalExpectedRecommenedAllocation = 0d;

			for (PortfolioOverviewDto portfolioOverviewDto : portfolioOverviewDtoList) {
				if (portfolioOverviewDto.getInvestmentOrPersonFlag() != null
						&& portfolioOverviewDto.getInvestmentOrPersonFlag().equals("N")) {
					continue;
				}
				PortfolioAssetAllocationReviewDto portfolioAssetAllocationReviewDto = new PortfolioAssetAllocationReviewDto();

				if (assetMap.get(portfolioOverviewDto.getAssetClassification()) != null) {
					PortfolioAssetAllocationReviewDto porfolioAssetAllObj = assetMap
							.get(portfolioOverviewDto.getAssetClassification());
					totalCurrentBal = totalCurrentBal + portfolioOverviewDto.getCurrentValue();
					totalInvestmentBal = totalInvestmentBal + portfolioOverviewDto.getInvestmentValue();
					totalGain = totalGain + portfolioOverviewDto.getGains();
					porfolioAssetAllObj.setInvestmentAssetClass(portfolioOverviewDto.getAssetClassification());
					porfolioAssetAllObj.setCurrentValue(
							porfolioAssetAllObj.getCurrentValue() + portfolioOverviewDto.getCurrentValue());
					porfolioAssetAllObj.setInvestmentValue(
							porfolioAssetAllObj.getInvestmentValue() + portfolioOverviewDto.getInvestmentValue());
					// porfolioAssetAllObj.setProfitLoss(
					// porfolioAssetAllObj.getCurrentValue() -
					// porfolioAssetAllObj.getInvestmentValue());
					double totalgainforAsset = portfolioOverviewDto.getGains() + porfolioAssetAllObj.getProfitLoss();
					porfolioAssetAllObj.setProfitLoss(totalgainforAsset);
					porfolioAssetAllObj
							.setPortFoliototalPercentage((porfolioAssetAllObj.getCurrentValue() / totalCurrentBal));
					porfolioAssetAllObj.setSubAssetClassId(portfolioOverviewDto.getSubAssetClassificationId());

					portfolioAssetAllocationReviewDto
							.setSubAssetClassId(portfolioOverviewDto.getSubAssetClassificationId());
					portfolioAssetAllocationReviewDto.setAssetClassId(portfolioOverviewDto.getAssetClassificationId());
					assetMap.put(portfolioOverviewDto.getAssetClassification(), porfolioAssetAllObj);
				} else {
					portfolioAssetAllocationReviewDto
							.setInvestmentAssetClass(portfolioOverviewDto.getAssetClassification());
					portfolioAssetAllocationReviewDto.setInvestmentValue(portfolioOverviewDto.getInvestmentValue());
					portfolioAssetAllocationReviewDto.setCurrentValue(portfolioOverviewDto.getCurrentValue());
					totalGain = totalGain + portfolioOverviewDto.getGains();
					portfolioAssetAllocationReviewDto.setProfitLoss(portfolioOverviewDto.getGains());
					portfolioAssetAllocationReviewDto
							.setSubAssetClassId(portfolioOverviewDto.getSubAssetClassificationId());
					totalCurrentBal = totalCurrentBal + portfolioAssetAllocationReviewDto.getCurrentValue();
					portfolioAssetAllocationReviewDto
							.setPortFoliototalPercentage((portfolioOverviewDto.getCurrentValue() / totalCurrentBal));
					totalInvestmentBal = totalInvestmentBal + portfolioAssetAllocationReviewDto.getInvestmentValue();
					portfolioAssetAllocationReviewDto
							.setSubAssetClassId(portfolioOverviewDto.getSubAssetClassificationId());
					portfolioAssetAllocationReviewDto.setAssetClassId(portfolioOverviewDto.getAssetClassificationId());
					assetMap.put(portfolioOverviewDto.getAssetClassification(), portfolioAssetAllocationReviewDto);
				}

			}
			// Calculate Cagr
			for (PortfolioOverviewDto portfolioOverviewDto : portfolioOverviewDtoList) {
				if (portfolioOverviewDto.getInvestmentOrPersonFlag() != null
						&& portfolioOverviewDto.getInvestmentOrPersonFlag().equals("N")) {
					continue;
				}
				if (portfolioOverviewDto.getCagr() != null
						&& portfolioOverviewDto.getCagr().matches("(\\+|-)?([0-9]+(\\.[0-9]+))")) {
					PortfolioAssetAllocationReviewDto porfolioAssetAllObj = assetMap
							.get(portfolioOverviewDto.getAssetClassification());
					double currentWeightageAssetClass = portfolioOverviewDto.getCurrentValue()
							/ porfolioAssetAllObj.getCurrentValue();
					double assetClasswisePerformance = currentWeightageAssetClass
							* Double.valueOf(portfolioOverviewDto.getCagr());
					if (porfolioAssetAllObj.getCagr_xirr() == null) {
						porfolioAssetAllObj.setCagr_xirr(0.0);
					}
					porfolioAssetAllObj.setCagr_xirr(porfolioAssetAllObj.getCagr_xirr() + assetClasswisePerformance);
					porfolioAssetAllObj.setSubAssetClassId(portfolioOverviewDto.getSubAssetClassificationId());
					porfolioAssetAllObj.setAssetClassId(portfolioOverviewDto.getAssetClassificationId());
					assetMap.put(portfolioOverviewDto.getAssetClassification(), porfolioAssetAllObj);
				}
			}
			portfolioAssetAllocationReviewDtoDtoList = new ArrayList<PortfolioAssetAllocationReviewDto>(
					assetMap.values());

			List<LookupAssetAllocation> lookupAssetAllocationList = portFolioAssetAllocationReviewDAO
					.getLookupAssetAllocationList((int) clientMasterList.get(0).getRiskProfile(), session);
			Map<Byte, LookupAssetAllocation> mapForSubAssetWeightage = new HashMap<>();
			boolean checkForAssetAllocation = true;
			for (LookupAssetAllocation lookupAssetAllocation : lookupAssetAllocationList) {
				checkForAssetAllocation = true;
				for (PortfolioAssetAllocationReviewDto portfoliewDto : new ArrayList<>(
						portfolioAssetAllocationReviewDtoDtoList)) {
					if (portfoliewDto.getRecomentTotalPercentage() == null) {
						portfoliewDto.setRecomentTotalPercentage(0.0);
					}
					if (portfoliewDto.getAssetClassId() == lookupAssetAllocation.getLookupAssetSubClass()
							.getLookupAssetClass().getId()) {

						portfoliewDto
								.setRecomentTotalPercentage(portfoliewDto.getRecomentTotalPercentage().doubleValue()
										+ lookupAssetAllocation.getWeightage().doubleValue());
						checkForAssetAllocation = false;
					}

					/*
					 * if (multipleProductsubassetMap.get(portfoliewDto.getInvestmentAssetClass())
					 * != null &&
					 * multipleProductsubassetMap.get(portfoliewDto.getInvestmentAssetClass()) !=
					 * 0.0) { Double currentMLR =
					 * multipleProductsubassetMap.get(portfoliewDto.getInvestmentAssetClass());
					 * currentMLR = currentMLR * lookupAssetAllocation.getLookupAssetSubClass()
					 * .getMasterSubAssetClassReturns().getMlr().doubleValue();
					 * multipleProductsubassetMap.put(portfoliewDto.getInvestmentAssetClass(),
					 * currentMLR); } else { if (portfoliewDto.getInvestmentAssetClass() != null) {
					 * multipleProductsubassetMap.put(portfoliewDto.getInvestmentAssetClass(),
					 * lookupAssetAllocation.getLookupAssetSubClass().getMasterSubAssetClassReturns(
					 * ) .getMlr().doubleValue()); } } if (totalMultiplerecommendPerc > 0) {
					 * totalMultiplerecommendPerc = totalMultiplerecommendPerc
					 * portfoliewDto.getRecomentTotalPercentage(); }
					 */

				}
				if (checkForAssetAllocation) {
					if (mapForSubAssetWeightage.get(
							lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId()) != null) {

						LookupAssetAllocation lookAsset = mapForSubAssetWeightage
								.get(lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId());
						lookAsset.setWeightage(lookAsset.getWeightage().add(lookupAssetAllocation.getWeightage()));
						mapForSubAssetWeightage
								.remove(lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId());
						mapForSubAssetWeightage.put(
								lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId(),
								lookAsset);
					} else {
						mapForSubAssetWeightage.put(
								lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId(),
								lookupAssetAllocation);
					}

				}
			}

			for (PortfolioAssetAllocationReviewDto portfoliewDto : new ArrayList<>(
					portfolioAssetAllocationReviewDtoDtoList)) {
				portfoliewDto.setPortFoliototalPercentage(portfoliewDto.getCurrentValue() / totalCurrentBal * 100);
				totalportfolioPerc = totalportfolioPerc + portfoliewDto.getPortFoliototalPercentage();
				if (portfoliewDto.getRecomentTotalPercentage() == null) {
					portfoliewDto.setRecomentTotalPercentage(0.0);
				}
				portfoliewDto.setRecomentTotalPercentage(portfoliewDto.getRecomentTotalPercentage() * 100);
				totalrecommendPerc = totalrecommendPerc + portfoliewDto.getRecomentTotalPercentage();
				if (portfoliewDto.getProfitLoss() == null) {
					portfoliewDto.setProfitLoss(0.0);
				}
				totalprofLoss = totalprofLoss + portfoliewDto.getProfitLoss();

			}

			for (Map.Entry<Byte, LookupAssetAllocation> mapEntryObj : mapForSubAssetWeightage.entrySet()) {
				LookupAssetAllocation lookAsset = mapEntryObj.getValue();
				PortfolioAssetAllocationReviewDto portfolioAssetAllDto = new PortfolioAssetAllocationReviewDto();
				portfolioAssetAllDto.setInvestmentAssetClass(
						lookAsset.getLookupAssetSubClass().getLookupAssetClass().getDescription());
				portfolioAssetAllDto.setRecomentTotalPercentage(lookAsset.getWeightage().doubleValue() * 100);
				if (lookAsset.getWeightage().doubleValue() <= 0) {
					continue;
				}
				portfolioAssetAllDto.setCurrentValue(0d);
				portfolioAssetAllDto.setPortFoliototalPercentage(0d);
				portfolioAssetAllDto.setInvestmentValue(0d);
				portfolioAssetAllDto.setCagr_xirr(0d);
				portfolioAssetAllDto.setProfitLoss(0d);
				portfolioAssetAllocationReviewDtoDtoList.add(portfolioAssetAllDto);
				totalrecommendPerc = totalrecommendPerc + portfolioAssetAllDto.getRecomentTotalPercentage();
			}

			for (PortfolioAssetAllocationReviewDto portfoliewDto : new ArrayList<>(
					portfolioAssetAllocationReviewDtoDtoList)) {
				Double currentMLR = multipleProductsubassetMap.get(portfoliewDto.getInvestmentAssetClass());
				if (currentMLR == null) {
					currentMLR = 0.0;
				}
				portfoliewDto.setExpectedReturns(portfoliewDto.getRecomentTotalPercentage() + currentMLR);
				if (totalexpectedReturn > 0) {
					totalexpectedReturn = totalexpectedReturn * portfoliewDto.getRecomentTotalPercentage();
				}
				if (portfoliewDto.getCagr_xirr() == null) {
					portfoliewDto.setCagr_xirr(0.0);
				}
				if (portfoliewDto.getPortFoliototalPercentage() == null) {
					portfoliewDto.setPortFoliototalPercentage(0.0);
				}
				totalXirr = totalXirr
						+ (portfoliewDto.getCagr_xirr() * (portfoliewDto.getPortFoliototalPercentage() / (double) 100));

				MasterAssetClassReturn mlr = portFolioAssetAllocationReviewDAO
						.getMLRFromMaster(portfoliewDto.getAssetClassId(), session);
				if (mlr != null) {
					totalRecommendated = totalRecommendated + (mlr.getMlr().doubleValue()
							* (portfoliewDto.getRecomentTotalPercentage() / (double) 100));

					totalExpectedCurrentAllocation = totalExpectedCurrentAllocation + (mlr.getRiskStdDev().doubleValue()
							* (portfoliewDto.getPortFoliototalPercentage() / (double) 100));
					totalExpectedRecommenedAllocation = totalExpectedRecommenedAllocation
							+ (mlr.getRiskStdDev().doubleValue()
									* (portfoliewDto.getRecomentTotalPercentage() / (double) 100));
				}
			}

			PortfolioAssetAllocationReviewDto totalAssetAllocationReviewDto = new PortfolioAssetAllocationReviewDto();
			totalAssetAllocationReviewDto.setInvestmentAssetClass("Total");
			totalAssetAllocationReviewDto.setCurrentValue(totalCurrentBal);
			totalAssetAllocationReviewDto.setPortFoliototalPercentage(totalportfolioPerc);
			totalAssetAllocationReviewDto.setInvestmentValue(totalInvestmentBal);
			totalAssetAllocationReviewDto.setCagr_xirr(totalXirr);
			totalAssetAllocationReviewDto.setProfitLoss(totalprofLoss);
			totalAssetAllocationReviewDto.setRecomentTotalPercentage(totalrecommendPerc);
			totalAssetAllocationReviewDto.setTotalExpectedRecommed(totalRecommendated * 100);
			totalAssetAllocationReviewDto.setTotalRiskExpectedCurrent(totalExpectedCurrentAllocation * 100);
			totalAssetAllocationReviewDto.setTotalRiskExpectedRecommed(totalExpectedRecommenedAllocation * 100);
			totalAssetAllocationReviewDto.setExpectedReturns(totalMultiplerecommendPerc + totalexpectedReturn);
			portfolioAssetAllocationReviewDtoDtoList.add(totalAssetAllocationReviewDto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return portfolioAssetAllocationReviewDtoDtoList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PortfolioAssetAllocationReviewDto getclientPortfolioSubAssetAllocationReview(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
		PortfolioAssetAllocationReviewDto portfoliosubAssetDto = new PortfolioAssetAllocationReviewDto();
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		PortFolioOverviewService portFolioOverviewService = new PortFolioOverviewServiceImpl();
		List<PortfolioAssetAllocationReviewDto> portfoliosubAssetDtoList = new ArrayList<>();
		Map<String, PortfolioAssetAllocationReviewDto> portfolioAssetMap = new ConcurrentHashMap<>();
		Map<String, List<PortfolioAssetAllocationReviewDto>> portfolioAssetListMap = new HashMap<>();
		ClientMasterDAO clientMasterDao = new ClientMasterDAOImpl();
		double totalCurrentBal = 0;
		double totalInvestmentValue = 0;
		double totalPortfolioPerc = 0;
		double totalrecommendPerc = 0;
		double totalTotalProfiLoss = 0;

		double totalproductrecommendPerc = 0.0;
		double totalproductexpReturn = 0.0;
		double totalXirr = 0.0;

		currentDate = Calendar.getInstance();

		try {

			portfolioOverviewDtoList = (List<PortfolioOverviewDto>) cacheInfoService.getCacheList(
					FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
					FinexaConstant.CALCULATION_SUB_TYPE_PORTFOLIO_CONSTANT);

			if (portfolioOverviewDtoList == null || portfolioOverviewDtoList.isEmpty()) {
				portfolioOverviewDtoList = portFolioOverviewService.getclientPortfolioOverview(token, clientId, "", session,
						cacheInfoService);
			}

			List<ClientMaster> clientMasterList = clientMasterDao.getAllClientsById(clientId, session);
			List<MasterSubAssetClassReturn> masterSubAssetClassReturnList = portFolioAssetAllocationReviewDAO
					.getMasterSubAssetClassReturnList(session);

			List<MasterIndexDailyNAV> masterindexdailynavList = null;
			List<LookupAssetAllocation> lookupAssetAllocationList = portFolioAssetAllocationReviewDAO
					.getLookupAssetAllocationList((int) clientMasterList.get(0).getRiskProfile(), session);

			for (PortfolioOverviewDto portfolioOverviewDto : portfolioOverviewDtoList) {
				if (portfolioOverviewDto.getInvestmentOrPersonFlag() != null
						&& portfolioOverviewDto.getInvestmentOrPersonFlag().equals("N")) {
					continue;
				}
				if(portfolioOverviewDto.getCurrentValue() !=0 && portfolioOverviewDto.getInvestmentValue() != 0) {
				currentDate.add(Calendar.MONTH, -1);
				Calendar month1 = currentDate;
				month1.set(Calendar.HOUR_OF_DAY, 0);
				month1.set(Calendar.MINUTE, 0);
				month1.set(Calendar.SECOND, 0);
				month1.set(Calendar.MILLISECOND, 0);

				currentDate = Calendar.getInstance();
				currentDate.add(Calendar.MONTH, -3);
				Calendar month3 = currentDate;
				month3.set(Calendar.HOUR_OF_DAY, 0);
				month3.set(Calendar.MINUTE, 0);
				month3.set(Calendar.SECOND, 0);
				month3.set(Calendar.MILLISECOND, 0);

				currentDate = Calendar.getInstance();
				currentDate.add(Calendar.MONTH, -6);
				Calendar month6 = currentDate;
				month6.set(Calendar.HOUR_OF_DAY, 0);
				month6.set(Calendar.MINUTE, 0);
				month6.set(Calendar.SECOND, 0);
				month6.set(Calendar.MILLISECOND, 0);

				currentDate = Calendar.getInstance();
				currentDate.add(Calendar.YEAR, -1);
				Calendar year1 = currentDate;
				year1.set(Calendar.HOUR_OF_DAY, 0);
				year1.set(Calendar.MINUTE, 0);
				year1.set(Calendar.SECOND, 0);
				year1.set(Calendar.MILLISECOND, 0);

				currentDate = Calendar.getInstance();
				currentDate.add(Calendar.YEAR, -3);
				Calendar year3 = currentDate;
				year3.set(Calendar.HOUR_OF_DAY, 0);
				year3.set(Calendar.MINUTE, 0);
				year3.set(Calendar.SECOND, 0);
				year3.set(Calendar.MILLISECOND, 0);

				currentDate = Calendar.getInstance();
				currentDate.add(Calendar.YEAR, -5);
				Calendar year5 = currentDate;
				year5.set(Calendar.HOUR_OF_DAY, 0);
				year5.set(Calendar.MINUTE, 0);
				year5.set(Calendar.SECOND, 0);
				year5.set(Calendar.MILLISECOND, 0);

				currentDate = Calendar.getInstance();
				currentDate.set(Calendar.HOUR_OF_DAY, 0);
				currentDate.set(Calendar.MINUTE, 0);
				currentDate.set(Calendar.SECOND, 0);
				currentDate.set(Calendar.MILLISECOND, 0);
				PortfolioAssetAllocationReviewDto portfolioSubAssetAlloDto = new PortfolioAssetAllocationReviewDto();
				portfolioSubAssetAlloDto
						.setInvestmentAssetClass(portfolioOverviewDto.getSubAssetClassification() == null ? ""
								: portfolioOverviewDto.getSubAssetClassification());
				portfolioSubAssetAlloDto.setAssetClassId(portfolioOverviewDto.getProductId());
				portfolioSubAssetAlloDto.setCurrentValue(portfolioOverviewDto.getCurrentValue());
				portfolioSubAssetAlloDto.setInvestmentValue(portfolioOverviewDto.getInvestmentValue());
				portfolioSubAssetAlloDto.setInvestmentAssetClass(portfolioOverviewDto.getAssetClassification());
				portfolioSubAssetAlloDto.setInvestmentSubAssetClass(portfolioOverviewDto.getSubAssetClassification());
				portfolioSubAssetAlloDto.setSubAssetClassId(portfolioOverviewDto.getSubAssetClassificationId());
				portfolioSubAssetAlloDto.setProfitLoss(portfolioOverviewDto.getGains());

				if (null != lookupAssetAllocationList && !lookupAssetAllocationList.isEmpty()) {
					portfolioSubAssetAlloDto
							.setRecomentTotalPercentage(lookupAssetAllocationList.get(0).getWeightage().doubleValue());
					if(portfolioSubAssetAlloDto.getSubAssetClassId() <= 0) {
						portfolioSubAssetAlloDto.setSubAssetClassId(lookupAssetAllocationList.get(0).getLookupAssetSubClass().getId());
					}
				}

				if (portfolioSubAssetAlloDto.getRecomentTotalPercentage() != null
						&& portfolioSubAssetAlloDto.getRecomentTotalPercentage() > 0) {
					// what is mlr
					portfolioSubAssetAlloDto.setExpectedReturns(lookupAssetAllocationList.get(0)
							.getLookupAssetSubClass().getMasterSubAssetClassReturns().getMlr().doubleValue());

				} else {
					portfolioSubAssetAlloDto.setExpectedReturns(0.0);
				}
				if (totalproductexpReturn > 0) {
					totalproductexpReturn = totalproductexpReturn * portfolioSubAssetAlloDto.getExpectedReturns();
				}
				totalCurrentBal = totalCurrentBal + portfolioOverviewDto.getCurrentValue();
				totalInvestmentValue = totalInvestmentValue + portfolioOverviewDto.getInvestmentValue();
				totalTotalProfiLoss = totalTotalProfiLoss + portfolioOverviewDto.getGains();
				if (portfolioSubAssetAlloDto.getRecomentTotalPercentage() != null) {
					totalrecommendPerc = totalrecommendPerc + portfolioSubAssetAlloDto.getRecomentTotalPercentage();
					if (totalproductrecommendPerc > 0) {
						totalproductrecommendPerc = totalproductrecommendPerc
								* portfolioSubAssetAlloDto.getRecomentTotalPercentage();
					}
				}
				List<Double> totalStdCal = new ArrayList<>();
				Double currentValue = 0.0;
				PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();
				for (MasterSubAssetClassReturn masterSubAssetClassReturn : masterSubAssetClassReturnList) {
					if (masterSubAssetClassReturn.getLookupAssetSubClass().getId() == portfolioSubAssetAlloDto
							.getSubAssetClassId()) {
						if (masterSubAssetClassReturn.getMasterIndexName() != null) {

							portfolioSubAssetBencmarkDto
									.setBenchMark(masterSubAssetClassReturn.getMasterIndexName().getName());
							masterindexdailynavList = portFolioAssetAllocationReviewDAO
							.getMasterindexdailynavList(masterSubAssetClassReturn.getMasterIndexName().getId(),session);
							for (MasterIndexDailyNAV masterindexdailynav : masterindexdailynavList) {
								if (currentDate.getTime()
												.compareTo(masterindexdailynav.getId().getDate()) == 0) {
									currentValue = masterindexdailynav.getNav().doubleValue();
									break;
								}
							}

							if (currentValue == null || currentValue == 0) {
								calculateNav = null;
								Double nav = this.searchForLastAvailableNav(currentDate,
										masterSubAssetClassReturn.getMasterIndexName().getId(),
										masterindexdailynavList);
								currentValue = nav;
							}
							for (MasterIndexDailyNAV masterindexdailynav : masterindexdailynavList) {
								if (masterindexdailynav.getId().getMasterIndexName()
										.getId() == masterSubAssetClassReturn.getMasterIndexName().getId()) {

									/* System.out.println(month1.getTime()); */
									if (month1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setMonth1Value(
												((currentValue / (double) masterindexdailynav.getNav().doubleValue())
														- 1) * 100);
										continue;
									}
									/* System.out.println(month3.getTime()); */
									if (month3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setMonth3Value(
												((currentValue / (double) masterindexdailynav.getNav().doubleValue())
														- 1) * 100);
										continue;
									}
									/* System.out.println(month6.getTime()); */
									if (month6.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setMonth6Value(
												((currentValue / (double) masterindexdailynav.getNav().doubleValue())
														- 1) * 100);
										continue;
									}
									/* System.out.println(year1.getTime()); */
									if (year1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setYear1Value(
												((currentValue / (double) masterindexdailynav.getNav().doubleValue())
														- 1) * 100);
									}
									/* System.out.println(year3.getTime()); */
									if (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setYear3Value((Math.pow(
												(currentValue / (double) masterindexdailynav.getNav().doubleValue()),
												1 / (double) 3) - 1) * 100);
										continue;
									}
									/* System.out.println(year5.getTime()); */
									if (year5.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setYear5Value((Math.pow(
												(currentValue / (double) masterindexdailynav.getNav().doubleValue()),
												1 / (double) 5) - 1) * 100);
										continue;
									}
									/* System.out.println(year3.getTime()); */
									if ((year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
											|| (currentDate.getTime()
													.compareTo(masterindexdailynav.getId().getDate()) == 0)
											|| (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == -1
													&& currentDate.getTime()
															.compareTo(masterindexdailynav.getId().getDate()) == 1)) {
										//totalStdCal.add(masterindexdailynav.getNav().doubleValue());
										double returns = ((currentValue - masterindexdailynav.getNav().doubleValue())/masterindexdailynav.getNav().doubleValue())*100;
										totalStdCal.add(returns);
										continue;

									}
								}

							}

							if (portfolioSubAssetBencmarkDto.getMonth1Value() == null) {
								calculateNav = null;
								Double nav = this.searchForLastAvailableNav(month1,
										masterSubAssetClassReturn.getMasterIndexName().getId(),
										masterindexdailynavList);
								if (nav > 0) {
									portfolioSubAssetBencmarkDto
											.setMonth1Value(((currentValue / (double) nav) - 1) * 100);
								} else {
//									portfolioSubAssetBencmarkDto.setMonth1Value(((0d) - 1) * 100);
									portfolioSubAssetBencmarkDto.setMonth1Value(null);
								}
							}
							if (portfolioSubAssetBencmarkDto.getMonth3Value() == null) {
								calculateNav = null;
								Double nav = this.searchForLastAvailableNav(month3,
										masterSubAssetClassReturn.getMasterIndexName().getId(),
										masterindexdailynavList);
								if (nav > 0) {
									portfolioSubAssetBencmarkDto
											.setMonth3Value(((currentValue / (double) nav) - 1) * 100);
								} else {
//									portfolioSubAssetBencmarkDto.setMonth3Value(((0d) - 1) * 100);
									portfolioSubAssetBencmarkDto.setMonth3Value(null);
								}
							}
							if (portfolioSubAssetBencmarkDto.getMonth6Value() == null) {
								calculateNav = null;
								Double nav = this.searchForLastAvailableNav(month6,
										masterSubAssetClassReturn.getMasterIndexName().getId(),
										masterindexdailynavList);
								if (nav > 0) {
									portfolioSubAssetBencmarkDto
											.setMonth6Value(((currentValue / (double) nav) - 1) * 100);
								} else {
//									portfolioSubAssetBencmarkDto.setMonth6Value(((0d) - 1) * 100);
									portfolioSubAssetBencmarkDto.setMonth6Value(null);
								}
							}
							if (portfolioSubAssetBencmarkDto.getYear1Value() == null) {
								calculateNav = null;
								Double nav = this.searchForLastAvailableNav(year1,
										masterSubAssetClassReturn.getMasterIndexName().getId(),
										masterindexdailynavList);
								if (nav > 0) {
									portfolioSubAssetBencmarkDto
											.setYear1Value(((currentValue / (double) nav) - 1) * 100);
								} else {
//									portfolioSubAssetBencmarkDto.setYear1Value(((0d) - 1) * 100);
									portfolioSubAssetBencmarkDto.setYear1Value(null);
								}
							}
							if (portfolioSubAssetBencmarkDto.getYear3Value() == null) {
								calculateNav = null;
								Double nav = this.searchForLastAvailableNav(year3,
										masterSubAssetClassReturn.getMasterIndexName().getId(),
										masterindexdailynavList);
								if (nav > 0) {
									portfolioSubAssetBencmarkDto.setYear3Value(
											(Math.pow((currentValue / (double) nav), 1 / (double) 3) - 1) * 100);
								} else {
									/*portfolioSubAssetBencmarkDto
											.setYear3Value((Math.pow((0d), 1 / (double) 3) - 1) * 100);*/
									portfolioSubAssetBencmarkDto
									.setYear3Value(null);
								}
							}
							if (portfolioSubAssetBencmarkDto.getYear5Value() == null) {
								calculateNav = null;
								Double nav = this.searchForLastAvailableNav(year5,
										masterSubAssetClassReturn.getMasterIndexName().getId(),
										masterindexdailynavList);
								if (nav > 0) {
									portfolioSubAssetBencmarkDto.setYear5Value(
											(Math.pow((currentValue / (double) nav), 1 / (double) 5) - 1) * 100);
								} else {
									/*portfolioSubAssetBencmarkDto
											.setYear5Value((Math.pow((0d), 1 / (double) 5) - 1) * 100);*/
									portfolioSubAssetBencmarkDto
									.setYear5Value(null);
								}
							}
							//fumction to calculate standard deviation in portfolio management
							portfolioSubAssetBencmarkDto.setRiskStdDev(FinanceUtil.STDEV(totalStdCal));
							portfolioSubAssetAlloDto.setPortSubAssetBechMark(portfolioSubAssetBencmarkDto);
						} else {
							portfolioSubAssetBencmarkDto.setBenchMark("N/A");
						}
					}
				}
				portfolioSubAssetAlloDto.setPortSubAssetBechMark(portfolioSubAssetBencmarkDto);
				PortfolioAssetAllocationReviewDto reviewMapdto = null;
				if (portfolioOverviewDto.getSubAssetClassification() != null) {
					reviewMapdto = portfolioAssetMap.get(portfolioOverviewDto.getSubAssetClassification());
				}
				if (reviewMapdto == null) {
					reviewMapdto = new PortfolioAssetAllocationReviewDto();
				}
				if (reviewMapdto.getCurrentValue() == null) {
					reviewMapdto.setCurrentValue(0.0);
				}
				reviewMapdto
						.setCurrentValue(reviewMapdto.getCurrentValue() + portfolioSubAssetAlloDto.getCurrentValue());
				reviewMapdto.setInvestmentValue(
						(reviewMapdto.getInvestmentValue() != null ? reviewMapdto.getInvestmentValue() : 0d)
								+ (portfolioSubAssetAlloDto.getInvestmentValue() != null
										? portfolioSubAssetAlloDto.getInvestmentValue()
										: 0d));
				reviewMapdto.setCagr_xirr(portfolioSubAssetAlloDto.getCagr_xirr());
				reviewMapdto.setInvestmentAssetClass(portfolioSubAssetAlloDto.getInvestmentAssetClass());
				reviewMapdto.setInvestmentSubAssetClass(portfolioSubAssetAlloDto.getInvestmentSubAssetClass());
				reviewMapdto.setPortSubAssetBechMark(portfolioSubAssetAlloDto.getPortSubAssetBechMark());
				reviewMapdto.setProfitLoss((reviewMapdto.getProfitLoss() == null ? 0d : reviewMapdto.getProfitLoss())
						+ (portfolioSubAssetAlloDto.getProfitLoss() == null ? 0d
								: portfolioSubAssetAlloDto.getProfitLoss()));
				reviewMapdto.setAssetClassId(portfolioSubAssetAlloDto.getAssetClassId());
				reviewMapdto.setSubAssetClassId(portfolioSubAssetAlloDto.getSubAssetClassId());
				if (reviewMapdto != null && portfolioOverviewDto.getSubAssetClassification() != null) {
					portfolioAssetMap.put(portfolioOverviewDto.getSubAssetClassification(), reviewMapdto);
				}
				portfoliosubAssetDtoList.add(portfolioSubAssetAlloDto);
			   } 
			}

			// Calculate Cagr
			for (PortfolioOverviewDto portfolioOverviewDto : portfolioOverviewDtoList) {
				if (portfolioOverviewDto.getCagr() != null && portfolioOverviewDto.getCagr().matches("-?[0-9.]*")) {
					if (portfolioOverviewDto.getSubAssetClassification() != null) {
						PortfolioAssetAllocationReviewDto porfolioAssetAllObj = portfolioAssetMap
								.get(portfolioOverviewDto.getSubAssetClassification());

						if (porfolioAssetAllObj != null) {
							double currentWeightageAssetClass = portfolioOverviewDto.getCurrentValue()
									/ porfolioAssetAllObj.getCurrentValue();
							double assetClasswisePerformance = currentWeightageAssetClass
									* Double.valueOf(portfolioOverviewDto.getCagr());
							if (porfolioAssetAllObj.getCagr_xirr() == null) {
								porfolioAssetAllObj.setCagr_xirr(0.0);
							}
							porfolioAssetAllObj
									.setCagr_xirr(porfolioAssetAllObj.getCagr_xirr() + assetClasswisePerformance);
							portfolioAssetMap.put(portfolioOverviewDto.getSubAssetClassification(),
									porfolioAssetAllObj);
							totalXirr = totalXirr + porfolioAssetAllObj.getCagr_xirr();
						}
					}
				}
			}

			for (Map.Entry<String, PortfolioAssetAllocationReviewDto> mapObj : portfolioAssetMap.entrySet()) {
				PortfolioAssetAllocationReviewDto reviewMapdto = mapObj.getValue();

				if (reviewMapdto.getCurrentValue() == null) {
					reviewMapdto.setCurrentValue(0.0);
				}
				if (reviewMapdto.getInvestmentValue() == null) {
					reviewMapdto.setInvestmentValue(0.0);
				}

				if (reviewMapdto.getPortFoliototalPercentage() == null) {
					reviewMapdto.setPortFoliototalPercentage(0.0);
				}
				reviewMapdto.setPortFoliototalPercentage((reviewMapdto.getCurrentValue() / totalCurrentBal) * 100);
				totalPortfolioPerc = totalPortfolioPerc + reviewMapdto.getPortFoliototalPercentage();
				if (reviewMapdto.getExpectedReturns() == null) {
					reviewMapdto.setExpectedReturns(0.0);
				}

				if (reviewMapdto.getRecomentTotalPercentage() == null) {
					reviewMapdto.setRecomentTotalPercentage(0.0);
				}

				portfolioAssetMap.put(reviewMapdto.getInvestmentSubAssetClass(), reviewMapdto);
			}

			/***Inclusion of those subAsset Classes which are not considered as no products are falling under those classes
			 * However, for recommended Total Percentage we have to find out the same ***/
				for (LookupAssetAllocation lookupAssetAllocation : lookupAssetAllocationList) {
					if (!portfolioAssetMap.containsKey(lookupAssetAllocation.getLookupAssetSubClass().getDescription()) && 
							lookupAssetAllocation.getWeightage().compareTo(new BigDecimal(0.0)) == 1) {
						PortfolioAssetAllocationReviewDto newObj = new PortfolioAssetAllocationReviewDto();
						newObj.setAssetClassId(lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId());
						newObj.setSubAssetClassId(lookupAssetAllocation.getLookupAssetSubClass().getId());
						newObj.setRecomentTotalPercentage(lookupAssetAllocation.getWeightage().doubleValue());
						newObj.setPortFoliototalPercentage(0.0);
						newObj.setInvestmentAssetClass(lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getDescription());
						newObj.setInvestmentSubAssetClass(lookupAssetAllocation.getLookupAssetSubClass().getDescription());
						portfolioAssetMap.put(newObj.getInvestmentSubAssetClass(), newObj);
						
					}
				}
			
			portfoliosubAssetDtoList = new ArrayList<PortfolioAssetAllocationReviewDto>(portfolioAssetMap.values());

			/*
			 * List<LookupAssetAllocation> lookupAssetAllocationList =
			 * portFolioAssetAllocationReviewDAO .getLookupAssetAllocationList((int)
			 * clientMasterList.get(0).getRiskProfile(), session);
			 */
			Map<Byte, LookupAssetAllocation> mapForSubAssetWeightage = new HashMap<>();
			boolean checkForAssetAllocation = true;
			for (LookupAssetAllocation lookupAssetAllocation : lookupAssetAllocationList) {
				//System.out.println("lookup " + lookupAssetAllocation.getLookupAssetSubClass().getId());
				checkForAssetAllocation = true;
				for (PortfolioAssetAllocationReviewDto portfoliewDto : new ArrayList<>(portfoliosubAssetDtoList)) {
					//System.out.println("portfoliewDto.getSubAssetClassId()" + portfoliewDto.getSubAssetClassId());
					if (portfoliewDto.getRecomentTotalPercentage() == null) {
						portfoliewDto.setRecomentTotalPercentage(0.0);
					}
					if (portfoliewDto.getSubAssetClassId() == lookupAssetAllocation.getLookupAssetSubClass().getId()) {

						portfoliewDto
								.setRecomentTotalPercentage(lookupAssetAllocation.getWeightage().doubleValue());
						checkForAssetAllocation = false;
					}

					/*
					 * if (multipleProductsubassetMap.get(portfoliewDto.getInvestmentAssetClass())
					 * != null &&
					 * multipleProductsubassetMap.get(portfoliewDto.getInvestmentAssetClass()) !=
					 * 0.0) { Double currentMLR =
					 * multipleProductsubassetMap.get(portfoliewDto.getInvestmentAssetClass());
					 * currentMLR = currentMLR * lookupAssetAllocation.getLookupAssetSubClass()
					 * .getMasterSubAssetClassReturns().getMlr().doubleValue();
					 * multipleProductsubassetMap.put(portfoliewDto.getInvestmentAssetClass(),
					 * currentMLR); } else { if (portfoliewDto.getInvestmentAssetClass() != null) {
					 * multipleProductsubassetMap.put(portfoliewDto.getInvestmentAssetClass(),
					 * lookupAssetAllocation.getLookupAssetSubClass().getMasterSubAssetClassReturns(
					 * ) .getMlr().doubleValue()); } } if (totalMultiplerecommendPerc > 0) {
					 * totalMultiplerecommendPerc = totalMultiplerecommendPerc
					 * portfoliewDto.getRecomentTotalPercentage(); }
					 */

				}
				if (checkForAssetAllocation) {
					if (mapForSubAssetWeightage.get(
							lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId()) != null) {

						LookupAssetAllocation lookAsset = mapForSubAssetWeightage
								.get(lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId());
						lookAsset.setWeightage(lookAsset.getWeightage().add(lookupAssetAllocation.getWeightage()));
						mapForSubAssetWeightage
								.remove(lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId());
						mapForSubAssetWeightage.put(
								lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId(),
								lookAsset);
					} else {
						mapForSubAssetWeightage.put(
								lookupAssetAllocation.getLookupAssetSubClass().getLookupAssetClass().getId(),
								lookupAssetAllocation);
					}

				}
			}

			for (Map.Entry<Byte, LookupAssetAllocation> mapEntryObj : mapForSubAssetWeightage.entrySet()) {
				LookupAssetAllocation lookAsset = mapEntryObj.getValue();
				PortfolioAssetAllocationReviewDto portfolioAssetAllDto = new PortfolioAssetAllocationReviewDto();
				portfolioAssetAllDto.setInvestmentAssetClass(
						lookAsset.getLookupAssetSubClass().getLookupAssetClass().getDescription());
				portfolioAssetAllDto.setInvestmentSubAssetClass(lookAsset.getLookupAssetSubClass().getDescription());
				portfolioAssetAllDto.setSubAssetClassId(lookAsset.getLookupAssetSubClass().getId());
				if (lookAsset.getWeightage().doubleValue() <= 0) {
					continue;
				}
				portfolioAssetAllDto.setRecomentTotalPercentage(lookAsset.getWeightage().doubleValue());
				portfolioAssetAllDto.setCurrentValue(0d);
				portfolioAssetAllDto.setPortFoliototalPercentage(0d);
				portfolioAssetAllDto.setInvestmentValue(0d);
				portfolioAssetAllDto.setCagr_xirr(0d);
				portfolioAssetAllDto.setProfitLoss(0d);
				portfoliosubAssetDtoList.add(portfolioAssetAllDto);
			}

			for (PortfolioAssetAllocationReviewDto portfolioAssetAllocReviewDto : new ArrayList<>(
					portfoliosubAssetDtoList)) {
				List<PortfolioAssetAllocationReviewDto> subAssetList = new ArrayList<>();
				if (portfolioAssetAllocReviewDto.getRecomentTotalPercentage() == null) {
					portfolioAssetAllocReviewDto.setRecomentTotalPercentage(0.0);
				}
				portfolioAssetAllocReviewDto
						.setRecomentTotalPercentage(portfolioAssetAllocReviewDto.getRecomentTotalPercentage() * 100);
				if (portfolioAssetListMap.get(portfolioAssetAllocReviewDto.getInvestmentAssetClass()) != null) {
					subAssetList = portfolioAssetListMap.get(portfolioAssetAllocReviewDto.getInvestmentAssetClass());
				}
				if (portfolioAssetAllocReviewDto.getInvestmentAssetClass() != null) {
					subAssetList.add(portfolioAssetAllocReviewDto);
					portfolioAssetListMap.put(portfolioAssetAllocReviewDto.getInvestmentAssetClass(), subAssetList);
				}
			}
			portfoliosubAssetDto.setInvestmentAssetClass("Total");
			portfoliosubAssetDto.setCurrentValue(totalCurrentBal);
			portfoliosubAssetDto.setInvestmentValue(totalInvestmentValue);
			portfoliosubAssetDto.setProfitLoss(totalTotalProfiLoss);
			portfoliosubAssetDto.setPortFoliototalPercentage(Double.valueOf(Math.round(totalPortfolioPerc)));
			portfoliosubAssetDto.setRecomentTotalPercentage(totalrecommendPerc);
			portfoliosubAssetDto.setExpectedReturns(totalproductrecommendPerc + totalproductexpReturn);
			portfoliosubAssetDto.setCagr_xirr(totalXirr);
			portfoliosubAssetDto.setPortfolioAssetListMap(portfolioAssetListMap);
			// portfoliosubAssetDto.setPortfolioAssetAllocationReviewDtoList(portfoliosubAssetDtoList);
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portfoliosubAssetDto;
	}

	public Double searchForLastAvailableNav(Calendar date, int id, List<MasterIndexDailyNAV> masterindexdailynavList) {

		Calendar minDate = Calendar.getInstance();

		Calendar givenDate = Calendar.getInstance();
		givenDate.setTime(date.getTime());
		navFound = false;
		boolean checkData = masterindexdailynavList.stream()
				.anyMatch(dto -> dto.getId().getMasterIndexName().getId() == id);
		if (!checkData) {
			return 0d;
		}

		List<MasterIndexDailyNAV> masterFilterindexdailynavList = masterindexdailynavList.stream()
				.filter(dto -> dto.getId().getMasterIndexName().getId() == id).
				// put those filtered elements into a new List.
				collect(Collectors.toCollection(() -> new ArrayList<MasterIndexDailyNAV>()));

		do {
			for (MasterIndexDailyNAV masterindexdailynav : masterFilterindexdailynavList) {
				if (masterindexdailynav.getId().getMasterIndexName().getId() == id) {
					if (givenDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
						calculateNav = masterindexdailynav.getNav().doubleValue();
						navFound = true;
						break;
					}

				}

				if (masterindexdailynav.getId().getDate().compareTo(minDate.getTime()) < 0) {
					minDate.setTime(masterindexdailynav.getId().getDate());
				}

			}
			givenDate.add(Calendar.DAY_OF_MONTH, -1);
			// System.out.println("min date=" + minDate.getTime());
			if (givenDate.getTime().compareTo(minDate.getTime()) < 0) {
				// System.out.println("current date=" + givenDate.getTime());
				calculateNav = 0d;
				navFound = true;
				break;
			}
			// System.out.println(date.getTime());

		} while (!navFound);

		return calculateNav;

	}

	@Override
	public List<PortfolioHistoricalReturnOutput> getclientPortfolioHistoricalReturn(int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<PortfolioHistoricalReturnOutput> historicalReturnOutput = new ArrayList<PortfolioHistoricalReturnOutput>();
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		ClientMasterDAO clientMasterDao = new ClientMasterDAOImpl();
		try {
			List<ClientMaster> clientMasterList = clientMasterDao.getAllClientsById(clientId, session);
			List<LookupAssetAllocation> lookupAssetAllocationList = portFolioAssetAllocationReviewDAO
					.getLookupAssetAllocationList((int) clientMasterList.get(0).getRiskProfile(), session);
			Calendar cal = Calendar.getInstance();
			Date planDate = new Date();
			cal.setTime(planDate);
			int endYear = cal.get(Calendar.YEAR) - 1;
			// calculation will start from
			int startYear = endYear - 10; // previous year
			for (int currentYear = startYear; currentYear <= endYear; currentYear++) {
				// Getting historical return of current year
				PortfolioHistoricalReturnOutput hitoryThisYear = new PortfolioHistoricalReturnOutput();
				hitoryThisYear.setYear(currentYear);

				List<MasterExpectedHistoricalReturn> historicalReturnList = portFolioAssetAllocationReviewDAO
						.getRateOfReturn(currentYear, session);
				if (historicalReturnList == null || historicalReturnList.isEmpty()) {
					hitoryThisYear.setAssetReturn(0.0);
					// calculate rate of return from masterindices daily NAV
				} else {
					double rateOfReturn = sumProduct(historicalReturnList, lookupAssetAllocationList);
					hitoryThisYear.setAssetReturn(round(rateOfReturn, 2));
				}
				historicalReturnOutput.add(hitoryThisYear);
			}
		} catch (FinexaDaoException e) {
			e.printStackTrace();
		}
		return historicalReturnOutput;
	}

	private double sumProduct(List<MasterExpectedHistoricalReturn> historicalReturnList,
			List<LookupAssetAllocation> assetList) {

		double rateOfReturn = 0.0;
		for (LookupAssetAllocation asset : assetList) {
			for (MasterExpectedHistoricalReturn masterReturn : historicalReturnList) {
				try {
					if (asset.getLookupAssetSubClass().getId() == masterReturn.getLookupAssetSubClass().getId()) {
						double assetAllo = asset.getWeightage().doubleValue();
						rateOfReturn = rateOfReturn + (masterReturn.getReturnRate().doubleValue() * assetAllo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		rateOfReturn = rateOfReturn * 100;
		return rateOfReturn;
	}

	private double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.00");
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		String valueString = df.format(value);
		value = Double.parseDouble(valueString);
		return value;
	}
	
	@Override
	public List<ProductRecommendationOutputPM> getProduductRecoPM(String token,int clientId, int advisorId, Session session, CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<ProductRecommendationOutputPM> output = new ArrayList<>();
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		PortfolioAssetAllocationReviewDto portfolioAssetAllocationReviewDto = null;
		portfolioAssetAllocationReviewDto = this.getclientPortfolioSubAssetAllocationReview(token, clientId, session, cacheInfoService);
		
		if (portfolioAssetAllocationReviewDto != null) {
			double totalValue = portfolioAssetAllocationReviewDto.getCurrentValue();
			try {
				Map<String, List<PortfolioAssetAllocationReviewDto>> portfolioAssetMap = portfolioAssetAllocationReviewDto.getPortfolioAssetListMap();
				for (Map.Entry<String, List<PortfolioAssetAllocationReviewDto>> obj : portfolioAssetMap.entrySet()) { 
					List<PortfolioAssetAllocationReviewDto> portfolioAssetAllocationReviewDtoList = obj.getValue();
					if (portfolioAssetAllocationReviewDtoList != null && !portfolioAssetAllocationReviewDtoList.isEmpty()) {
						for (PortfolioAssetAllocationReviewDto listObj : portfolioAssetAllocationReviewDtoList) {
							double lumpsumAmt = ((listObj.getRecomentTotalPercentage() - listObj.getPortFoliototalPercentage())/100) * totalValue;
							ProductRecommendationOutputPM prodOutput = new ProductRecommendationOutputPM();
							List<MasterMFProductRecommendation> isinDTOList = new ArrayList<>();
							isinDTOList = portFolioAssetAllocationReviewDAO.getProductIsinListPM(listObj.getSubAssetClassId(), advisorId, session);
									
							List<String> isinListForScheme = new ArrayList<>();
							for (MasterMFProductRecommendation productReco : isinDTOList)   {
								isinListForScheme.add(productReco.getId().getMasterMutualFundEtf().getIsin());
							}
							int subAssetClassId = listObj.getSubAssetClassId();
							prodOutput.setSubAssetClassId(subAssetClassId);
							List<String> productListForThisAssetType = portFolioAssetAllocationReviewDAO.getProductListPM(subAssetClassId, advisorId, session);
							prodOutput.setSubAssetClass(listObj.getInvestmentSubAssetClass());
							double alloPerc = listObj.getRecomentTotalPercentage();
							if (alloPerc > 0) {
								prodOutput.setSubAssetAlloPerc(alloPerc/100);
								prodOutput.setProductName(productListForThisAssetType);
								prodOutput.setIsinList(isinListForScheme);
								if (lumpsumAmt > 0) {
									prodOutput.setLumpsumAmt(lumpsumAmt);
								} else {
									prodOutput.setLumpsumAmt(0);
								}
								output.add(prodOutput);
							}
						}
					}
				}
				Collections.sort(output, new Comparator<ProductRecommendationOutputPM>() {
					public int compare(ProductRecommendationOutputPM o1, ProductRecommendationOutputPM o2) {
						return (new Integer(o1.getSubAssetClassId())).compareTo(new Integer(o2.getSubAssetClassId()));
					}
				});
				
			} catch (FinexaDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}
}
