package com.finlabs.finexa.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.MasterdailynavDTO;
import com.finlabs.finexa.dto.PortfolioEquityDailyPriceDto;
import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
import com.finlabs.finexa.dto.PortfolioSubAssetBencmarkDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterDirectEquityMarketCap;
import com.finlabs.finexa.model.MasterEquityDailyPrice;
import com.finlabs.finexa.model.MasterIndexDailyNAV;
import com.finlabs.finexa.model.MasterSectorBenchmarkMapping;
import com.finlabs.finexa.pm.util.FinexaDateUtil;
import com.finlabs.finexa.repository.PortFolioAssetAllocationReviewDAO;
import com.finlabs.finexa.repository.PortFolioPagerDAO;
import com.finlabs.finexa.repository.impl.PortFolioAssetAllocationReviewDAOImpl;
import com.finlabs.finexa.repository.impl.PortFolioPagerTemplateDAOImpl;

import com.finlabs.finexa.service.PortFolioPagerTemplateService;
import com.finlabs.finexa.service.PortFolioPagerTemplateStockService;

public class PortFolioPagerTemplateStockServiceImpl implements PortFolioPagerTemplateStockService {

	@Override
	public PortfolioPagerproductTemplateDto getclientStockOnePagerTemplate(String ISIN, String name, String startDate,
			String endDate, Session session) throws FinexaBussinessException {

		PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = new PortfolioPagerproductTemplateDto();

		List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList = new ArrayList<>();
		PortFolioPagerDAO portFolioPagerDAO = new PortFolioPagerTemplateDAOImpl();
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		try {
			MasterDirectEquityMarketCap masterDirectEquityMarketCap = portFolioPagerDAO
					.getMasterDirectEquityMarketCap(ISIN, session);

			//	System.out.println("masterDirectEquityMarketCap " + masterDirectEquityMarketCap);

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

			// =========================================
			// Stock

			List<PortfolioEquityDailyPriceDto> portpolioEquityDailyPriceDtoList = new ArrayList<>();
			PortfolioEquityDailyPriceDto portfolioEquityDailyPriceDto;



			Date startDateStock = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			Date endDateStock = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

			List<MasterEquityDailyPrice> masterEquityDailyPriceStartDateList = portFolioPagerDAO
					.getMasterEquityDailyPriceListOnDate(ISIN, startDateStock, endDateStock, session);


			if(masterEquityDailyPriceStartDateList != null && !masterEquityDailyPriceStartDateList.isEmpty()) {

				for (MasterEquityDailyPrice masterEquityDailyPrice : masterEquityDailyPriceStartDateList) {

					portfolioEquityDailyPriceDto = new PortfolioEquityDailyPriceDto();
					portfolioEquityDailyPriceDto.setClosingPrice(round(masterEquityDailyPrice.getClosingPrice(),2));
					portfolioEquityDailyPriceDto.setDate(masterEquityDailyPrice.getId().getDate());
					portpolioEquityDailyPriceDtoList.add(portfolioEquityDailyPriceDto);

				}
				portfolioPagerproductTemplateDto.setMasterStockPerformancevsIndexList(portpolioEquityDailyPriceDtoList);


			}

			// Nifty==================================
			List<MasterdailynavDTO> MasterdailynavDTOList = new ArrayList<>();
			// Not required as per latest discussion on 29/05/2018 
			/*MasterdailynavDTO masterindexdailynavDTO;
			name="NIFTY 50";
			List<MasterIndexDailyNAV> masterIndexDailyNAVList = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					startDateStock, endDateStock, session);

			if(masterEquityDailyPriceStartDateList != null && !masterEquityDailyPriceStartDateList.isEmpty()) {
				for (MasterIndexDailyNAV masterIndexDailyNAV : masterIndexDailyNAVList) {

					masterindexdailynavDTO = new MasterdailynavDTO();
					masterindexdailynavDTO.setNAV(masterIndexDailyNAV.getNav().doubleValue());
					masterindexdailynavDTO.setDate(masterIndexDailyNAV.getId().getDate());
					MasterdailynavDTOList.add(masterindexdailynavDTO);

				}
			}*/

			portfolioPagerproductTemplateDto.setMasterNiftyPerformancevsIndexList(MasterdailynavDTOList);

			//================================================

			Calendar currentDate = Calendar.getInstance();
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



			PortfolioSubAssetBencmarkDto StockListTimePeriodList = getStockListTimePeriod(ISIN,currentDate, month1, month3, month6, year1, year3,  year5,  session);
			portfolioPagerproductTemplateDto.setMasterStockPerformanceTimePeriod(StockListTimePeriodList);

			PortfolioSubAssetBencmarkDto NiftyTimePeriod = getNiftyTimePeriod(ISIN,currentDate, month1, month3, month6, year1, year3,  year5,  session);
			portfolioPagerproductTemplateDto.setMasterNiftyPerformanceTimePeriod(NiftyTimePeriod);




		} catch (Exception e) {
			e.printStackTrace();
		}

		// ===============Graph ======================
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

	public PortfolioSubAssetBencmarkDto getStockListTimePeriod(String ISIN,Calendar currentDate, Calendar month1,Calendar month3,Calendar month6, Calendar year1,Calendar year3, Calendar year5, 
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

		try {

			// ============================================================
			// month1

			portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();

			Date curDate = currentDate.getTime();
			Calendar curDate2 = getBackDateFromNowCustomised("DAYS", 7);
			Date curDate3 = curDate2.getTime();
			System.out.println("curDate3 "+curDate3);

			List<MasterEquityDailyPrice> masterEquityDailyPriceStart = portFolioPagerDAO
					.getMasterEquityDailyPriceListTimePeriod(curDate, curDate3, ISIN, session);

			Date curDate4=null;
			if(masterEquityDailyPriceStart!=null && !masterEquityDailyPriceStart.isEmpty()) {
				curDate4=	masterEquityDailyPriceStart.get(0).getId().getDate();
			}

         

			Date toDate = month1.getTime();
			Calendar cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date toDate3 = cal.getTime();


			List<MasterEquityDailyPrice> masterEquityDailyPastdateList = portFolioPagerDAO
					.getMasterEquityDailyPriceListTimePeriod(toDate, toDate3,ISIN,session);
			Date toDate4 = null;
			if(masterEquityDailyPastdateList!=null && !masterEquityDailyPastdateList.isEmpty()) {
				toDate4=masterEquityDailyPastdateList.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			
			//	System.out.println("stock graph 1 monthly curDate4 "+curDate4);
			//	System.out.println("stock graph 1 monthly toDate4 "+toDate4);


			List<MasterEquityDailyPrice> masterEquityDailyPrice = portFolioPagerDAO
					.getMasterEquityDailyClosingPrice(curDate4, toDate4, ISIN, session);

			if (masterEquityDailyPrice.size()==2) {

				month1Value = (masterEquityDailyPrice.get(0).getClosingPrice()
						- masterEquityDailyPrice.get(1).getClosingPrice())
						/ masterEquityDailyPrice.get(1).getClosingPrice();

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

			 
			masterEquityDailyPastdateList = portFolioPagerDAO
					.getMasterEquityDailyPriceListTimePeriod(toDate, toDate3, ISIN, session);
			toDate4 = null;
			if(masterEquityDailyPastdateList!=null && !masterEquityDailyPastdateList.isEmpty()) {
				toDate4=masterEquityDailyPastdateList.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("stock graph 3 monthly curDate4 "+curDate4);
			//	System.out.println("stock graph 3 monthly toDate4 "+toDate4);
			
			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyClosingPrice(curDate4, toDate4, ISIN,
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

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			masterEquityDailyPastdateList = portFolioPagerDAO
					.getMasterEquityDailyPriceListTimePeriod(toDate, toDate3, ISIN, session);
			toDate4 = null;
			if(masterEquityDailyPastdateList!=null && !masterEquityDailyPastdateList.isEmpty()) {
				toDate4=masterEquityDailyPastdateList.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			

			//	System.out.println("stock graph 6 monthly curDate4 "+curDate4);
			//	System.out.println("stock graph 6 monthly toDate4 "+toDate4);

	       

			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyClosingPrice(curDate4, toDate4, ISIN,
					session);
			
			
			if (masterEquityDailyPrice.size()==2) {

				month6Value = (masterEquityDailyPrice.get(0).getClosingPrice()
						- masterEquityDailyPrice.get(1).getClosingPrice())
						/ masterEquityDailyPrice.get(1).getClosingPrice();

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


			masterEquityDailyPastdateList = portFolioPagerDAO
					.getMasterEquityDailyPriceListTimePeriod(toDate, toDate3, ISIN, session);
			toDate4 = null;
			if(masterEquityDailyPastdateList!=null && !masterEquityDailyPastdateList.isEmpty() ) {
				toDate4=masterEquityDailyPastdateList.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("stock graph 1 yearly curDate4 "+curDate4);
			//	System.out.println("stock graph 1 yearly toDate4 "+toDate4);

			
	        
			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyClosingPrice(curDate4, toDate4, ISIN,
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
			//	System.out.println("year3.getTime() " + year3.getTime());

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


			masterEquityDailyPastdateList = portFolioPagerDAO
					.getMasterEquityDailyPriceListTimePeriod(toDate, toDate3, ISIN, session);
			toDate4 = null;
			if(masterEquityDailyPastdateList!=null && !masterEquityDailyPastdateList.isEmpty()) {
				toDate4=masterEquityDailyPastdateList.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("stock graph 3 yearly curDate4 "+curDate4);
			//	System.out.println("stock graph 3 yearly toDate4 "+toDate4);
			
			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyClosingPrice(curDate4, toDate4, ISIN,
					session);

			if (masterEquityDailyPrice.size()==2) {

				div = masterEquityDailyPrice.get(0).getClosingPrice().doubleValue()
						/ masterEquityDailyPrice.get(1).getClosingPrice().doubleValue();

				double pow = 1.0/3.0;
				BigDecimal power = new BigDecimal(Math.pow(div,pow));
				year3Value = power.doubleValue() - 1;
			}
			portfolioSubAssetBencmarkDto.setYear3Value(year3Value * 100);

			// =========================================================
			// year5
			//	System.out.println("year5.getTime() " + year5.getTime());

			toDate = year5.getTime();

			cal = Calendar.getInstance();
			cal.setTime(toDate);
			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			toDate3 = cal.getTime();
			//	System.out.println("toDate3 "+toDate3);


			masterEquityDailyPastdateList = portFolioPagerDAO
					.getMasterEquityDailyPriceListTimePeriod(toDate, toDate3, ISIN, session);
			toDate4 = null;
			if(masterEquityDailyPastdateList!=null && !masterEquityDailyPastdateList.isEmpty()) {
				toDate4=masterEquityDailyPastdateList.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("stock graph 5 yearly curDate4 "+curDate4);
			//	System.out.println("stock graph 5 yearly toDate4 "+toDate4);

			
			masterEquityDailyPrice = portFolioPagerDAO.getMasterEquityDailyClosingPrice(curDate4, toDate4, ISIN,
					session);
			
			if (masterEquityDailyPrice.size()==2) {

				div = masterEquityDailyPrice.get(0).getClosingPrice().doubleValue()
						/ masterEquityDailyPrice.get(1).getClosingPrice().doubleValue();

				double pow = 1.0/5.0;
				BigDecimal power = new BigDecimal(Math.pow(div,pow));
				year5Value = power.doubleValue() - 1;

			}
			portfolioSubAssetBencmarkDto.setYear5Value(year5Value * 100);
			// =================================
			portfolioSubAssetBencmarkDto.setBenchMark("Stock");




		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portfolioSubAssetBencmarkDto;

	}



	public PortfolioSubAssetBencmarkDto getNiftyTimePeriod(String name,Calendar currentDate, Calendar month1,Calendar month3,Calendar month6, Calendar year1,Calendar year3, Calendar year5, 
			Session session) throws FinexaBussinessException {
		name="NIFTY 50";
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
		List<PortfolioSubAssetBencmarkDto> PortfolioSubAssetBencmarkDtoList = new ArrayList<PortfolioSubAssetBencmarkDto>();

		try {


			// extra
			/*
			 * currentDate.set(Calendar.DAY_OF_MONTH, 28); currentDate.set(Calendar.MONTH,
			 * 2); currentDate.set(Calendar.YEAR, 2018);
			 */



			portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();

			Date curDate = currentDate.getTime();

			//	System.out.println("Nifty curDate1 "+curDate);

			Calendar curDate2 = getBackDateFromNowCustomised("DAYS", 7);
			Date curDate3 = curDate2.getTime();
			//	System.out.println("Nifty curDate3 "+curDate3);

			List<MasterIndexDailyNAV> masterIndexDailyNAVListStart = portFolioPagerDAO
					.getMasterindexdailynavListOnDate(name,curDate, curDate3, session);

			Date curDate4=null;
			if(masterIndexDailyNAVListStart!=null && !masterIndexDailyNAVListStart.isEmpty()) {
				curDate4=	masterIndexDailyNAVListStart.get(0).getId().getDate();
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

			//	System.out.println("Nifty graph 1 month curDate4 "+curDate4);
			//	System.out.println("Nifty graph 1 month yearly toDate4 "+toDate4);

			List<MasterIndexDailyNAV> masterIndexDailyNAVList = portFolioPagerDAO
					.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name, session);

			if (masterIndexDailyNAVList.size()==2) {
				
				month1Value = (masterIndexDailyNAVList.get(0).getNav().doubleValue()
						- masterIndexDailyNAVList.get(1).getNav().doubleValue())
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

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

			//	System.out.println("Nifty graph 3 month curDate4 "+curDate4);
			//	System.out.println("Nifty graph 3 month toDate4 "+toDate4);


			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
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

			//	System.out.println("Nifty graph 6 month curDate4 "+curDate4);
			//	System.out.println("Nifty graph 6 month toDate4 "+toDate4);

			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
					session);

			if (masterIndexDailyNAVList.size()==2) {

				month6Value = (masterIndexDailyNAVList.get(0).getNav().doubleValue()
						- masterIndexDailyNAVList.get(1).getNav().doubleValue())
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

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

			//	System.out.println("Nifty graph 1 year curDate4 "+curDate4);
			//	System.out.println("Nifty graph 1 year toDate4 "+toDate4);


			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
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

			//	System.out.println("Nifty graph 3 yearly curDate4 "+curDate4);
			//	System.out.println("Nifty graph 3 yearly toDate4 "+toDate4);


			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
					session);

			if (masterIndexDailyNAVList.size()==2) {

				div = masterIndexDailyNAVList.get(0).getNav().doubleValue()
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();
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
			//	System.out.println("toDate3 "+toDate3);



			masterIndexDailyNAVListPast = portFolioPagerDAO.getMasterindexdailynavListOnDate(name,
					toDate, toDate3, session);

			toDate4 = null;
			if(masterIndexDailyNAVListPast!=null && !masterIndexDailyNAVListPast.isEmpty()) {
				toDate4=masterIndexDailyNAVListPast.get(0).getId().getDate();
				//	 System.out.println("toDate4 "+toDate4);
			}

			//	System.out.println("Nifty graph 5 yearly curDate4 "+curDate4);
			//	System.out.println("Nifty graph 5 yearly toDate4 "+toDate4);


			masterIndexDailyNAVList = portFolioPagerDAO.getMasterIndexDailyNAVClosingPrice(curDate4, toDate4, name,
					session);



			if (masterIndexDailyNAVList.size()==2) {

				div = masterIndexDailyNAVList.get(0).getNav().doubleValue()
						/ masterIndexDailyNAVList.get(1).getNav().doubleValue();

				double pow = 1.0/5.0;
				BigDecimal power = new BigDecimal(Math.pow(div,pow));
				year5Value = power.doubleValue() - 1;

			}
			portfolioSubAssetBencmarkDto.setYear5Value(year5Value * 100);
			// ========================================
			portfolioSubAssetBencmarkDto.setBenchMark("Nifty 50");

			// System.out.println("size "+PortfolioSubAssetBencmarkDtoList.size());
			// ======================================

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portfolioSubAssetBencmarkDto;

	}

}
