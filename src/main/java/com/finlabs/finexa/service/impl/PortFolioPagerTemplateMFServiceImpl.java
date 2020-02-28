package com.finlabs.finexa.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Session;
import org.springframework.boot.autoconfigure.template.TemplateLocation;

import com.finlabs.finexa.dto.MasterMFHoldingRatingWiseDTO;
import com.finlabs.finexa.dto.MasterMFSectorHoldingWiseDTO;
import com.finlabs.finexa.dto.MasterdailynavDTO;
import com.finlabs.finexa.dto.PortfolioDetailsDto;
import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
import com.finlabs.finexa.dto.PortfolioSubAssetBencmarkDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.Master52WeekHighLowMF;
import com.finlabs.finexa.model.MasterCompany;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterFundManager;
import com.finlabs.finexa.model.MasterIndexDailyNAV;
import com.finlabs.finexa.model.MasterIndexName;
import com.finlabs.finexa.model.MasterMFAssetAllocation;
import com.finlabs.finexa.model.MasterMFDailyNAV;
import com.finlabs.finexa.model.MasterMFHolding;
import com.finlabs.finexa.model.MasterMFHoldingMaturity;
import com.finlabs.finexa.model.MasterMFMarketCap;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.pm.util.FinexaDateUtil;
import com.finlabs.finexa.repository.PortFolioPagerDAO;
import com.finlabs.finexa.repository.impl.PortFolioPagerTemplateDAOImpl;
import com.finlabs.finexa.service.PortFolioPagerTemplateMFService;


public class PortFolioPagerTemplateMFServiceImpl implements PortFolioPagerTemplateMFService{

	@Override
	public PortfolioPagerproductTemplateDto getclientMFOnePagerTemplate(String ISIN, String startDate,
			String endDate, Session session)
					throws FinexaBussinessException {
		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();
		PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = new PortfolioPagerproductTemplateDto();
		try {
			int assetClassType = 0;
			Calendar currentDate;
			Date curDate = null;
			MasterMFMarketCap masterMfMarketcap = null;
			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.DATE, -1);
			curDate = currentDate.getTime();


			masterMfMarketcap = portFolioPagerDAO.getMasterMfMarketcap(ISIN,session);
			if(masterMfMarketcap!=null) {
				if(masterMfMarketcap.getPb() !=null) {
					portfolioPagerproductTemplateDto.setP_b(masterMfMarketcap.getPb().doubleValue());
				}
				if(masterMfMarketcap.getPe() !=null) {
					portfolioPagerproductTemplateDto.setP_e(masterMfMarketcap.getPe().doubleValue());
				}
				portfolioPagerproductTemplateDto.setCapRank(masterMfMarketcap.getCapRank());
				portfolioPagerproductTemplateDto.setInvestmentStyle(masterMfMarketcap.getInvestmentStyle());
			}

			Master52WeekHighLowMF master52WeekHighLowMF=portFolioPagerDAO.getMaster52WeekHighLowMf(ISIN, session);
			if(master52WeekHighLowMF != null) {

				if(master52WeekHighLowMF.getNav52WeekHigh() !=null) {
					portfolioPagerproductTemplateDto.setWeekHigh52(master52WeekHighLowMF.getNav52WeekHigh().doubleValue());
				}
				if(master52WeekHighLowMF.getNav52WeekLow() !=null) {
					portfolioPagerproductTemplateDto.setWeekLow52(master52WeekHighLowMF.getNav52WeekLow().doubleValue());
				}
			}
			MasterIndexName masterIndexName = portFolioPagerDAO.getMasterIndexName( ISIN,  session);
			if(masterIndexName != null) {
				if(masterIndexName.getName() != null)
					portfolioPagerproductTemplateDto.setBenchmarkIndex(masterIndexName.getName());
			}
			MasterFundManager masterFundManager = portFolioPagerDAO.getMasterFundManager(ISIN,session);
			if(masterFundManager!=null) {
				if(masterFundManager.getManagerName()!=null)
					portfolioPagerproductTemplateDto.setFundManagers(masterFundManager.getManagerName());
			}
			MasterMutualFundETF masterMutualFundETF = portFolioPagerDAO.getMasterMutualFundETF(ISIN, session);
			if(masterMutualFundETF!=null) {
				portfolioPagerproductTemplateDto.setMinInvestmentAmount((double) masterMutualFundETF.getMinInvestmentAmount());
				//		System.out.println("masterMutualFundETF.getExitLoadAndPeriod() "+masterMutualFundETF.getExitLoadAndPeriod());
				if(masterMutualFundETF.getExitLoadAndPeriod()!=null)
					portfolioPagerproductTemplateDto.setExitLoadPeriod(masterMutualFundETF.getExitLoadAndPeriod());
				if(masterMutualFundETF.getSchemeInceptionDate()!=null)
					portfolioPagerproductTemplateDto.setSchemeinceptionDate(masterMutualFundETF.getSchemeInceptionDate());
				portfolioPagerproductTemplateDto.setSubAssetClass(masterMutualFundETF.getLookupAssetSubClass().getDescription());
				assetClassType = masterMutualFundETF.getLookupAssetClass().getId();
			}
			List<MasterMFAssetAllocation> masterMfAssetAllocationList = portFolioPagerDAO
					.getMasterMfAssetAllocationList(ISIN, session);
			if(masterMfAssetAllocationList != null && !masterMfAssetAllocationList.isEmpty()) {
				if(masterMfAssetAllocationList.get(0).getAum()!=null)
					portfolioPagerproductTemplateDto.setAum(String.valueOf(masterMfAssetAllocationList.get(0).getAum()));
				if(masterMfAssetAllocationList.get(0).getExpenseRatio()!=null)
					portfolioPagerproductTemplateDto
					.setExpenseRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getExpenseRatio()));
			}
			
			List<MasterMFHoldingMaturity> masterMFHoldingMaturityList = portFolioPagerDAO.getMasterMfHoldingMaturityListByIsin(ISIN, session);
			if(masterMFHoldingMaturityList != null && !masterMFHoldingMaturityList.isEmpty()) {
				if(masterMFHoldingMaturityList.get(0).getYtm() == null) {
					portfolioPagerproductTemplateDto.setYtm(0.0);
				} else {
					portfolioPagerproductTemplateDto.setYtm((double)masterMFHoldingMaturityList.get(0).getYtm());
				}
				if(masterMFHoldingMaturityList.get(0).getDuration() == null) {
					portfolioPagerproductTemplateDto.setDuration(0.0);
				} else {
					portfolioPagerproductTemplateDto.setDuration((double)masterMFHoldingMaturityList.get(0).getDuration());
				}
			}
			
			
			//===================================================================================
			MasterdailynavDTO masterdailynavDTO = new MasterdailynavDTO();
			List<MasterdailynavDTO> masterdailynavDTOList = new ArrayList<>();


			Date startDateMF = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			Date endDate1MF = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			List<MasterMFDailyNAV> getMasterMFDailyNAVListOnDateStart = null;
			if (startDateMF != null && endDate1MF != null) {
				getMasterMFDailyNAVListOnDateStart = portFolioPagerDAO
						.getMasterMFDailyNAVListOnDate(ISIN, startDateMF, endDate1MF, session);
			}

			if(getMasterMFDailyNAVListOnDateStart != null && !getMasterMFDailyNAVListOnDateStart.isEmpty()) {

				for (MasterMFDailyNAV masterMFDailyNAV : getMasterMFDailyNAVListOnDateStart) {

					masterdailynavDTO = new MasterdailynavDTO();
					masterdailynavDTO.setNAV(round(masterMFDailyNAV.getNav().doubleValue(),2));
					masterdailynavDTO.setDate(masterMFDailyNAV.getId().getDate());
					masterdailynavDTOList.add(masterdailynavDTO);

				}
				portfolioPagerproductTemplateDto.setMasterFundPerformancevsIndexList(masterdailynavDTOList);



			}
			//==============================================================

			List<MasterdailynavDTO> masterBenchMarkDTOList = new ArrayList<>();

              // not required as per latest discussion with Nimish Sir on 29/05/2018
			/*MasterdailynavDTO masterindexdailynavDTO;

			List<MasterIndexDailyNAV> masterBenchmarkList = portFolioPagerDAO.getMasterindexdailynavListOnDate(masterIndexName.getName(),
					startDateMF, endDate1MF, session);

			for (MasterIndexDailyNAV masterBenchmark : masterBenchmarkList) {

				masterindexdailynavDTO = new MasterdailynavDTO();
				masterindexdailynavDTO.setNAV(masterBenchmark.getNav().doubleValue());
				masterindexdailynavDTO.setDate(masterBenchmark.getId().getDate());
				masterBenchMarkDTOList.add(masterindexdailynavDTO);

			}*/

			portfolioPagerproductTemplateDto.setMasterBenchMarkPerformancevsIndexList(masterBenchMarkDTOList);
			//==========================================================

             //return
			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.DATE, -1);

			// extra
			/*
			 * currentDate.set(Calendar.DAY_OF_MONTH, 28); currentDate.set(Calendar.MONTH,
			 * 2); currentDate.set(Calendar.YEAR, 2018);
			 */
			// 1 month from current
			Calendar month1 = getBackDateFromNowCustomised("MONTHS", 1);
			// 3 months from current
			Calendar month3 = getBackDateFromNowCustomised("MONTHS", 3);
			// 6 months from current
			Calendar month6 = getBackDateFromNowCustomised("MONTHS", 6);
			// 1 year from current
			Calendar year1 = getBackDateFromNowCustomised("YEARS", 1);
			// 3 years from current
			Calendar year3 = getBackDateFromNowCustomised("YEARS", 3);
			// 5 years from current
			Calendar year5 = getBackDateFromNowCustomised("YEARS", 5);

			PortfolioSubAssetBencmarkDto masterFundPerformanceTimePeriod=getFundListTimePeriod( ISIN,  currentDate,  month1, month3, month6,  year1, year3,  year5, session);

			portfolioPagerproductTemplateDto.setMasterFundPerformanceTimePeriod(masterFundPerformanceTimePeriod);
			//==========================
			if (masterIndexName != null){
				PortfolioSubAssetBencmarkDto masterbenchmarkPerformanceTimePeriod= getBenchmarkListTimePeriod( masterIndexName.getName(), currentDate,  month1, month3, month6,  year1, year3,  year5, session);
				portfolioPagerproductTemplateDto.setMasterBenchMarkPerformanceTimePeriod(masterbenchmarkPerformanceTimePeriod);
			}

			//=====================================

			// Asset Allocation  chart
			if(masterMfAssetAllocationList != null && !masterMfAssetAllocationList.isEmpty()) {
				if(masterMfAssetAllocationList.get(0).getEquityRatio()!=null)
					portfolioPagerproductTemplateDto
					.setEquityRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getEquityRatio()));
				if(masterMfAssetAllocationList.get(0).getDebtRatio()!=null)
					portfolioPagerproductTemplateDto
					.setDebtRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getDebtRatio()));
				if(masterMfAssetAllocationList.get(0).getOthersRatio()!=null)
					portfolioPagerproductTemplateDto
					.setOtherRatio(Double.valueOf(masterMfAssetAllocationList.get(0).getOthersRatio()));
			}
			//======================================		
			//Market  Capitalization
			masterMfMarketcap = portFolioPagerDAO.getMasterMfMarketcap(ISIN, session);
			double totalCapValues = 0.0;
			if(masterMfMarketcap!=null) {
				if(masterMfMarketcap.getLargecap() !=null) {
					portfolioPagerproductTemplateDto.setLargeCap(masterMfMarketcap.getLargecap().doubleValue());
					totalCapValues = totalCapValues + portfolioPagerproductTemplateDto.getLargeCap();
				}
				if(masterMfMarketcap.getMidcap() !=null) {
					portfolioPagerproductTemplateDto.setMidCap(masterMfMarketcap.getMidcap().doubleValue());
					totalCapValues = totalCapValues + portfolioPagerproductTemplateDto.getMidCap();
				}
				if(masterMfMarketcap.getSmallcap() !=null) {
					portfolioPagerproductTemplateDto.setSmallCap(masterMfMarketcap.getSmallcap().doubleValue());
					totalCapValues = totalCapValues + portfolioPagerproductTemplateDto.getSmallCap();
				}
				if (totalCapValues < 100) {
					portfolioPagerproductTemplateDto.setOtherCap(100.0 - totalCapValues);
				}
			}

			//	Top 10 Sector Holdings===========================

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.DATE, -1);
			curDate = currentDate.getTime();

			Calendar date = getBackDateFromNowCustomised("DAYS", 7);
			Date toDate = date.getTime();

			//	startDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			//	endDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

			//	System.out.println("holding curDate "+curDate);
			//	System.out.println("holding toDate "+toDate);

			List<Object[]> obList = portFolioPagerDAO.getMFSectorHolding(ISIN,session);
			List<MasterMFSectorHoldingWiseDTO> masterMFSectorHoldingWiseDTOList=new ArrayList<MasterMFSectorHoldingWiseDTO>();

			//	System.out.println("masterMFSectorHoldingWiseDTOList "+obList.size());



			double holdingSum = 0.0;
			if(obList != null) {
				for(Object[] obj:obList) {
					//now you have one array of Object for each row
					float holdingValue = Float.parseFloat(String.valueOf(obj[0])); // don't know the type of column CLIENT assuming String 
					String sector = String.valueOf(obj[1]); //SERVICE assumed as int

					MasterMFSectorHoldingWiseDTO masterMFSectorHoldingWiseDTO = new MasterMFSectorHoldingWiseDTO();
					holdingSum = holdingSum + holdingValue;
					masterMFSectorHoldingWiseDTO.setHolding(holdingValue);
					masterMFSectorHoldingWiseDTO.setSectorName(sector);
					masterMFSectorHoldingWiseDTO.setIsin(String.valueOf(obj[2]));
					masterMFSectorHoldingWiseDTO.setDate((Date)obj[3]);

					masterMFSectorHoldingWiseDTOList.add(masterMFSectorHoldingWiseDTO);
				}
			}

			for (MasterMFSectorHoldingWiseDTO obj : masterMFSectorHoldingWiseDTOList) {
				if (holdingSum > 0) {
					double holdingPerc = (obj.getHolding()/holdingSum) * 100;
					obj.setHolding((float)holdingPerc);
				}
			}
			List<MasterMFSectorHoldingWiseDTO> tempList = new ArrayList<>();
			for (int i = 0; i < masterMFSectorHoldingWiseDTOList.size(); i++) {
				if(i == 10) {
					break;
				}else {
					tempList.add(masterMFSectorHoldingWiseDTOList.get(i));
				}
			}
			if(tempList != null) {
				masterMFSectorHoldingWiseDTOList.clear();
				masterMFSectorHoldingWiseDTOList = tempList;
			}
			portfolioPagerproductTemplateDto.setMasterMFSectorHoldingWiseDTOList(masterMFSectorHoldingWiseDTOList);
			//Portfolio Details


			List<MasterMFHolding> masterMfHoldingList = portFolioPagerDAO.getMasterMfHoldingList(ISIN, session);
			MasterDirectEquity masterDirectEquity = null;
			
			//	System.out.println("masterMfHoldingList "+masterMfHoldingList.size());
			PortfolioDetailsDto portfolioDetailsDto;
			List<PortfolioDetailsDto> portfolioDetailsDtoList = null;
			portfolioDetailsDtoList = new ArrayList<PortfolioDetailsDto>();
			if(masterMfHoldingList != null) {
				for (MasterMFHolding masterMfHolding : masterMfHoldingList) {
					//	System.out.println("masterMfHolding =================="+masterMfHolding);
					if(masterMfHolding!=null) {



						int companyCode=masterMfHolding.getId().getCompanyCode();


						List<MasterCompany> masterCompanyList = portFolioPagerDAO.getMasterCompany(companyCode, session);
						for(MasterCompany masterCompany:masterCompanyList) {
							//	System.out.println("obList ___________"+obList.size());
							//for equity
							if (assetClassType == 3) {
								if (masterCompany.getIsin() != null) {
									masterDirectEquity = portFolioPagerDAO.getMasterDirectEquity(masterCompany.getIsin(), session);
								}
							}
							portfolioDetailsDto = new PortfolioDetailsDto();

							//  System.out.println("masterCompany.getCompName() "+masterCompany.getFinCode());
							portfolioDetailsDto.setHoldings(masterCompany.getCompName());
							//	System.out.println("getAssetPercentage() "+masterMfHolding.getAssetPercentage().doubleValue());   
							portfolioDetailsDto.setPercOfPortfolio(masterMfHolding.getAssetPercentage().doubleValue());

							if (masterDirectEquity != null) {
								String sector = masterDirectEquity.getMasterMfsector().getSector(); 
								portfolioDetailsDto.setSector(sector);
							}
							
							//	System.out.println("sector "+sector);   
							
							portfolioDetailsDto.setAssetDate(masterMfHolding.getId().getAssetDate());

							portfolioDetailsDtoList.add(portfolioDetailsDto);

						}


					}
				}
			}

			//	Collections.sort(portfolioDetailsDtoList, new DateComparator());
			portfolioPagerproductTemplateDto.setPortfolioDetailsList(portfolioDetailsDtoList);

			Map<String,Double> assetValueMap = new HashMap();
			Date ratingWiseMaxDate = portFolioPagerDAO.getRatingWiseMaxDate(ISIN, session);
			double totalRatingVal = 0.0;
			if (ratingWiseMaxDate != null) {
				List<MasterMFHoldingRatingWiseDTO> ratingDtoList = portFolioPagerDAO.getMasterMFHoldingRatingWiseList(ISIN, ratingWiseMaxDate, session);
				if (ratingDtoList != null && !ratingDtoList.isEmpty()) {
					for (MasterMFHoldingRatingWiseDTO ratingObj : ratingDtoList) {
						totalRatingVal = totalRatingVal + ratingObj.getHolding();
					}
					for (MasterMFHoldingRatingWiseDTO ratingObj : ratingDtoList) {
						double val = 0.0;
						if (assetValueMap.get(ratingObj.getRatingName()) != null) {
							val = assetValueMap.get(ratingObj.getRatingName());
							val = val + ratingObj.getHolding();
							assetValueMap.put(ratingObj.getRatingName(),val);
						} else {
							assetValueMap.put(ratingObj.getRatingName(),ratingObj.getHolding());
						}
					}
					if (assetValueMap != null && !assetValueMap.isEmpty()) {
						for (Map.Entry<String, Double> obj : assetValueMap.entrySet()) {
							double perc = (obj.getValue()/totalRatingVal) * 100;
							obj.setValue(perc);
						}
					}
				}

			}
			portfolioPagerproductTemplateDto.setAssetQualityMap(assetValueMap); 
			//	System.out.println("po details "+portfolioPagerproductTemplateDto.getPortfolioDetailsList().size());

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//	portfolioPagerproductTemplateDto.setPortfolioDetailsList(portfolioDetailsList);
		//	portfolioPagerproductTemplateDto.setPortfolioSubAssetBencmarkDtoList(portfolioSubAssetBencmarkDtoList);
		return portfolioPagerproductTemplateDto;


	}
	public Calendar getBackDateFromNowCustomised(String YEARS_DAYS, int interval) {

		if (YEARS_DAYS != null) {
			Calendar backDate = Calendar.getInstance();
			backDate.add(Calendar.DATE, -1);
			//extra
			/*backDate.set(Calendar.DAY_OF_MONTH, 23);
			backDate.set(Calendar.MONTH, 1);
			backDate.set(Calendar.YEAR, 2018);
			*/

			if (YEARS_DAYS.equals("YEARS")) {
				backDate.add(Calendar.YEAR, -interval);
				backDate.set(Calendar.HOUR_OF_DAY, 0);
				backDate.set(Calendar.MINUTE, 0);
				backDate.set(Calendar.SECOND, 0);
				backDate.set(Calendar.MILLISECOND, 0);
			} 
			if (YEARS_DAYS.equals("MONTHS")) {
					backDate.add(Calendar.MONTH, -interval);
					backDate.set(Calendar.HOUR_OF_DAY, 0);
					backDate.set(Calendar.MINUTE, 0);
					backDate.set(Calendar.SECOND, 0);
					backDate.set(Calendar.MILLISECOND, 0);
				}
			if (YEARS_DAYS.equals("DAYS")) {
					backDate.add(Calendar.DATE, -interval);
					backDate.set(Calendar.HOUR_OF_DAY, 0);
					backDate.set(Calendar.MINUTE, 0);
					backDate.set(Calendar.SECOND, 0);
					backDate.set(Calendar.MILLISECOND, 0);
				}
			
			return backDate;
		} else {
			return Calendar.getInstance();
		}
	}

	public PortfolioSubAssetBencmarkDto getFundListTimePeriod(String ISIN,Calendar currentDate, Calendar month1,Calendar month3,Calendar month6, Calendar year1,Calendar year3, Calendar year5, 
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
		PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = null;
		//	List<PortfolioSubAssetBencmarkDto> PortfolioSubAssetBencmarkDtoList = new ArrayList<PortfolioSubAssetBencmarkDto>();

		try {


			// extra

			//  currentDate.set(Calendar.DAY_OF_MONTH, 23); currentDate.set(Calendar.MONTH,
			//	  1); currentDate.set(Calendar.YEAR, 2018);


			// ============================================================


			portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();

			Date curDate = currentDate.getTime();



			//   System.out.println("FUND curDate1 "+curDate);

			Calendar curDate2 = getBackDateFromNowCustomised("DAYS", 7);
			Date curDate3 = curDate2.getTime();
			//	System.out.println("Fund curDate3 "+curDate3);

			List<MasterMFDailyNAV> masterMFDailyNAVListOnDateStart = portFolioPagerDAO
					.getMasterMFDailyNAVListOnDate(ISIN,curDate, curDate3, session);

			Date curDate4=null;
			if(masterMFDailyNAVListOnDateStart!=null && !masterMFDailyNAVListOnDateStart.isEmpty()) {
				curDate4=	masterMFDailyNAVListOnDateStart.get(0).getId().getDate();
			}

			// month1
			Date toDate = month1.getTime();


			Calendar cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			List<MasterMFDailyNAV> masterMFDailyNAVListOnDatePast = portFolioPagerDAO.getMasterMFDailyNAVListOnDate(ISIN,
					toDate, toDate3, session);

			Date toDate4 = null;
			if(masterMFDailyNAVListOnDatePast!=null && !masterMFDailyNAVListOnDatePast.isEmpty()) {
				toDate4=masterMFDailyNAVListOnDatePast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("Fund graph 1 month curDate4 "+curDate4);
			//	System.out.println("Fund graph 1 month yearly toDate4 "+toDate4);





			List<MasterMFDailyNAV> masterMFDailyNAVList = portFolioPagerDAO.getMasterMFListClosingPrice(curDate4, toDate4, ISIN, session);
			//	System.out.println("masterMFDailyNAVList "+masterMFDailyNAVList.get(0).getNav());

			if (masterMFDailyNAVList.size()==2) {

				month1Value = (masterMFDailyNAVList.get(0).getNav()
						- masterMFDailyNAVList.get(1).getNav())
						/ masterMFDailyNAVList.get(1).getNav();

			}
			portfolioSubAssetBencmarkDto.setMonth1Value(month1Value * 100);
			// ==============================
			// month3
			toDate = month3.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			masterMFDailyNAVListOnDatePast = portFolioPagerDAO.getMasterMFDailyNAVListOnDate(ISIN,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterMFDailyNAVListOnDatePast!=null && !masterMFDailyNAVListOnDatePast.isEmpty()) {
				toDate4=masterMFDailyNAVListOnDatePast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("Fund graph 3 month curDate4 "+curDate4);
			//	System.out.println("Fund graph 3 month yearly toDate4 "+toDate4);


			masterMFDailyNAVList = portFolioPagerDAO.getMasterMFListClosingPrice(curDate4, toDate4, ISIN, session);

			if (masterMFDailyNAVList.size()==2) {

				month3Value = (masterMFDailyNAVList.get(0).getNav()
						- masterMFDailyNAVList.get(1).getNav())
						/ masterMFDailyNAVList.get(1).getNav();

			}
			portfolioSubAssetBencmarkDto.setMonth3Value(month3Value * 100);
			// ==================================================
			// month6
			toDate = month6.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//		System.out.println("toDate3 "+toDate3);


			masterMFDailyNAVListOnDatePast = portFolioPagerDAO.getMasterMFDailyNAVListOnDate(ISIN,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterMFDailyNAVListOnDatePast!=null && !masterMFDailyNAVListOnDatePast.isEmpty()) {
				toDate4=masterMFDailyNAVListOnDatePast.get(0).getId().getDate();
				//		 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("Fund graph 6 month curDate4 "+curDate4);
			//	System.out.println("Fund graph 6 month yearly toDate4 "+toDate4);



			masterMFDailyNAVList = portFolioPagerDAO.getMasterMFListClosingPrice(curDate4, toDate4, ISIN, session);


			if (masterMFDailyNAVList.size()==2) {

				month6Value = (masterMFDailyNAVList.get(0).getNav()
						- masterMFDailyNAVList.get(1).getNav())
						/ masterMFDailyNAVList.get(1).getNav();

			}

			portfolioSubAssetBencmarkDto.setMonth6Value(month6Value * 100);
			// ==============================================================
			// year1

			//	System.out.println("year1.getTime() " + year1.getTime());

			toDate = year1.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			masterMFDailyNAVListOnDatePast = portFolioPagerDAO.getMasterMFDailyNAVListOnDate(ISIN,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterMFDailyNAVListOnDatePast!=null && !masterMFDailyNAVListOnDatePast.isEmpty()) {
				toDate4=masterMFDailyNAVListOnDatePast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//		System.out.println("Fund graph 1 year curDate4 "+curDate4);
			//		System.out.println("Fund graph 1 year yearly toDate4 "+toDate4);



			masterMFDailyNAVList = portFolioPagerDAO.getMasterMFListClosingPrice(curDate4, toDate4, ISIN, session);



			if (masterMFDailyNAVList.size()==2) {

				year1Value = (masterMFDailyNAVList.get(0).getNav()
						- masterMFDailyNAVList.get(1).getNav())
						/ masterMFDailyNAVList.get(1).getNav();

			}
			portfolioSubAssetBencmarkDto.setYear1Value(year1Value * 100);
			// ============================

			// ========================================================
			// year3
			//		System.out.println("year3.getTime() " + year3.getTime());

			toDate = year3.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//		System.out.println("toDate3 "+toDate3);


			masterMFDailyNAVListOnDatePast = portFolioPagerDAO.getMasterMFDailyNAVListOnDate(ISIN,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterMFDailyNAVListOnDatePast!=null && !masterMFDailyNAVListOnDatePast.isEmpty()) {
				toDate4=masterMFDailyNAVListOnDatePast.get(0).getId().getDate();
				//		 System.out.println("toDate4 "+toDate4);
			}

			//		System.out.println("Fund graph 3 year curDate4 "+curDate4);
			//		System.out.println("Fund graph 3 year yearly toDate4 "+toDate4);



			masterMFDailyNAVList = portFolioPagerDAO.getMasterMFListClosingPrice(curDate4, toDate4, ISIN, session);


			if (masterMFDailyNAVList.size()==2) {

				div = masterMFDailyNAVList.get(0).getNav().doubleValue()
						/ masterMFDailyNAVList.get(1).getNav().doubleValue();

				double pow = 1.0/3.0;
				BigDecimal power = new BigDecimal(Math.pow(div,pow));
				year3Value = power.doubleValue() - 1;

			}
			portfolioSubAssetBencmarkDto.setYear3Value(year3Value * 100);

			// =========================================================
			// year5
			//		System.out.println("year5.getTime() " + year5.getTime());

			toDate = year5.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//		System.out.println("toDate3 "+toDate3);


			masterMFDailyNAVListOnDatePast = portFolioPagerDAO.getMasterMFDailyNAVListOnDate(ISIN,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterMFDailyNAVListOnDatePast!=null && !masterMFDailyNAVListOnDatePast.isEmpty()) {
				toDate4=masterMFDailyNAVListOnDatePast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("Fund graph 5 year curDate4 "+curDate4);
			//	System.out.println("Fund graph 5 year yearly toDate4 "+toDate4);



			masterMFDailyNAVList = portFolioPagerDAO.getMasterMFListClosingPrice(curDate4, toDate4, ISIN, session);



			if (masterMFDailyNAVList.size()==2) {

				div = masterMFDailyNAVList.get(0).getNav().doubleValue()
						/ masterMFDailyNAVList.get(1).getNav().doubleValue();

				double pow = 1.0/5.0;
				BigDecimal power = new BigDecimal(Math.pow(div,pow));
				year5Value = power.doubleValue() - 1;

			}
			portfolioSubAssetBencmarkDto.setYear5Value(year5Value * 100);
			// =================================
			portfolioSubAssetBencmarkDto.setBenchMark("Fund");



			// ======================================





		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portfolioSubAssetBencmarkDto;

	}

	public PortfolioSubAssetBencmarkDto getBenchmarkListTimePeriod(String name,Calendar currentDate, Calendar month1,Calendar month3,Calendar month6, Calendar year1,Calendar year3, Calendar year5, 
			Session session) throws FinexaBussinessException {
		double month1Value = 0;
		double month3Value = 0;
		double month6Value = 0;
		double year1Value = 0;
		double year3Value = 0;
		double div = 0;
		double c = 0;
		double year5Value = 0;
		double newValue=0;
		double oldValue=0;
		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();
		PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = null;

		try {


			portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();

			Date curDate = currentDate.getTime();

			//	  System.out.println("Bencmark curDate1 "+curDate);

			Calendar curDate2 = getBackDateFromNowCustomised("DAYS", 7);
			Date curDate3 = curDate2.getTime();
			//		System.out.println("NifBencmark curDate3 "+curDate3);

			List<MasterIndexDailyNAV> masterIndexDailyNAVListStart = portFolioPagerDAO
					.getMasterindexdailynavListOnDate(name,curDate, curDate3, session);

			Date curDate4=null;
			if(masterIndexDailyNAVListStart!=null && !masterIndexDailyNAVListStart.isEmpty()) {
				curDate4 = masterIndexDailyNAVListStart.get(0).getId().getDate();
			}
			// month1		
			Date toDate = month1.getTime();

			Calendar cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			List<MasterIndexDailyNAV> masterIndexDailyNAVListPast = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					toDate, toDate3, session);

			Date toDate4 = null;
			if(masterIndexDailyNAVListPast!=null && !masterIndexDailyNAVListPast.isEmpty()) {
				toDate4=masterIndexDailyNAVListPast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//		System.out.println("BencgMark graph 1 month curDate4 "+curDate4);
			//		System.out.println("BencgMark graph 1 month yearly toDate4 "+toDate4);



			List<MasterIndexDailyNAV> masterBenchMarkList = portFolioPagerDAO
					.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name, session);

			if (masterBenchMarkList.size()==2) {

				month1Value = (masterBenchMarkList.get(0).getNav().doubleValue()
						- masterBenchMarkList.get(1).getNav().doubleValue())
						/ masterBenchMarkList.get(1).getNav().doubleValue();

			}
			portfolioSubAssetBencmarkDto.setMonth1Value(month1Value * 100);

			// ==============================
			// month3

			toDate = month3.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			masterIndexDailyNAVListPast = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterIndexDailyNAVListPast!=null && !masterIndexDailyNAVListPast.isEmpty()) {
				toDate4=masterIndexDailyNAVListPast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("BenchMark graph 3 month curDate4 "+curDate4);
			//	System.out.println("BenchMark graph 3 month yearly toDate4 "+toDate4);


			masterBenchMarkList = portFolioPagerDAO
					.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name, session);
			if (masterBenchMarkList.size()==2) {

				month3Value = (masterBenchMarkList.get(0).getNav().doubleValue()
						- masterBenchMarkList.get(1).getNav().doubleValue())
						/ masterBenchMarkList.get(1).getNav().doubleValue();

			}
			portfolioSubAssetBencmarkDto.setMonth3Value(month3Value * 100);

			// ==================================================
			// month6

			toDate = month6.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//		System.out.println("toDate3 "+toDate3);


			masterIndexDailyNAVListPast = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterIndexDailyNAVListPast!=null && !masterIndexDailyNAVListPast.isEmpty()) {
				toDate4=masterIndexDailyNAVListPast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("BencgMark graph 6 month curDate4 "+curDate4);
			//	System.out.println("BencgMark graph 6 month  toDate4 "+toDate4);


			masterBenchMarkList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
					session);

			if (masterBenchMarkList.size()==2) {

				month6Value = (masterBenchMarkList.get(0).getNav().doubleValue()
						- masterBenchMarkList.get(1).getNav().doubleValue())
						/ masterBenchMarkList.get(1).getNav().doubleValue();

			}
			portfolioSubAssetBencmarkDto.setMonth6Value(month6Value * 100);

			// year1================
			toDate = year1.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			masterIndexDailyNAVListPast = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterIndexDailyNAVListPast!=null && !masterIndexDailyNAVListPast.isEmpty()) {
				toDate4=masterIndexDailyNAVListPast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("BenchMark graph 1 year curDate4 "+curDate4);
			//	System.out.println("BenchMark graph 1 year toDate4 "+toDate4);


			masterBenchMarkList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
					session);

			if (masterBenchMarkList.size()==2) {

				year1Value = (masterBenchMarkList.get(0).getNav().doubleValue()
						- masterBenchMarkList.get(1).getNav().doubleValue())
						/ masterBenchMarkList.get(1).getNav().doubleValue();

			}
			portfolioSubAssetBencmarkDto.setYear1Value(year1Value * 100);
			// ========================================================
			// year3

			toDate = year3.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			masterIndexDailyNAVListPast = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterIndexDailyNAVListPast!=null && !masterIndexDailyNAVListPast.isEmpty()) {
				toDate4=masterIndexDailyNAVListPast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("BenchMark graph 1 month curDate4 "+curDate4);
			//	System.out.println("BenchMark graph 1 month yearly toDate4 "+toDate4);


			masterBenchMarkList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
					session);

			if (masterBenchMarkList.size()==2) {

				div = masterBenchMarkList.get(0).getNav().doubleValue()
						/ masterBenchMarkList.get(1).getNav().doubleValue();

				double pow = 1.0/3.0;
				BigDecimal power = new BigDecimal(Math.pow(div,pow));
				year3Value = power.doubleValue() - 1;
				// System.out.println("year1Value "+year1Value);
			}
			portfolioSubAssetBencmarkDto.setYear3Value(year3Value * 100);

			// ============================
			// year5
			toDate = year5.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//		System.out.println("toDate3 "+toDate3);


			masterIndexDailyNAVListPast = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterIndexDailyNAVListPast!=null && !masterIndexDailyNAVListPast.isEmpty()) {
				toDate4=masterIndexDailyNAVListPast.get(0).getId().getDate();
				//		 System.out.println("toDate4 "+toDate4);
			}

			//		System.out.println("BenchMark graph 5 month curDate4 "+curDate4);
			//		System.out.println("BenchMark graph 5 month yearly toDate4 "+toDate4);


			masterBenchMarkList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
					session);

			if (masterBenchMarkList.size()==2) {

				div = masterBenchMarkList.get(0).getNav().doubleValue()
						/ masterBenchMarkList.get(1).getNav().doubleValue();

				double pow = 1.0/5.0;
				BigDecimal power = new BigDecimal(Math.pow(div,pow));
				year5Value = power.doubleValue() - 1;
				// System.out.println("year5Value "+year5Value);
			}
			portfolioSubAssetBencmarkDto.setYear5Value(year5Value * 100);
			// ========================================
			portfolioSubAssetBencmarkDto.setBenchMark(name);

			// System.out.println("size "+PortfolioSubAssetBencmarkDtoList.size());
			// ======================================

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portfolioSubAssetBencmarkDto;

	}
	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.00");
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		String valueString = df.format(value);
		value = Double.parseDouble(valueString);
		return value;
	}

}

class DateComparator implements Comparator<PortfolioDetailsDto> {  
	@Override  
	public int compare(PortfolioDetailsDto obj1, PortfolioDetailsDto obj2) {  
		if(obj1.getAssetDate().compareTo(obj2.getAssetDate()) == -1)
			return 1;
		if(obj1.getAssetDate().compareTo(obj2.getAssetDate()) == 1)
			return -1;
		else
			return 0;
	}  
}  

