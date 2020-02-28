package com.finlabs.finexa.repository;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.MasterMFHoldingRatingWiseDTO;
import com.finlabs.finexa.dto.MasterMFSectorHoldingWiseDTO;
import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.Master52WeekHighLowMF;
import com.finlabs.finexa.model.MasterCompany;
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
import com.finlabs.finexa.model.MasterMFHoldingSectorWise;
import com.finlabs.finexa.model.MasterMFMarketCap;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterRiskFreeRate;

public interface PortFolioPagerDAO {

	public MasterDirectEquityMarketCap getMasterDirectEquityMarketCap(String ISIN, Session session)
			throws FinexaDaoException;
	
	public MasterDirectEquity getMasterDirectEquity(String ISIN, Session session)
			throws FinexaDaoException;

	public List<MasterEquityDailyPrice> getMasterEquityDailyPriceList(String ISIN, Session session)
			throws FinexaDaoException;

	public MasterMutualFundETF getMasterMutualFundETF(String AMFI, Session session) throws FinexaDaoException;

	public List<MasterMFAssetAllocation> getMasterMfAssetAllocationList(String AMFI, Session session)
			throws FinexaDaoException;

	public Master52WeekHighLowMF getMaster52WeekHighLowMf(String AMFI, Session session) throws FinexaDaoException;

	public List<MasterMFMarketCap> getMasterMfMarketcapList(String AMFI, Session session) throws FinexaDaoException;

	public List<MasterMFDailyNAV> getMasterMfDailyNavList(String AMFI, Session session) throws FinexaDaoException;

	public List<MasterRiskFreeRate> getMasterRiskFreeRateList(Session session) throws FinexaDaoException;

	public List<MasterMFHolding> getMasterMfHoldingList(String ISIN, Session session) throws FinexaDaoException;

	public List<MasterMFHoldingMaturity> getMasterMfHoldingMaturityList(String AMFI, Session session)
			throws FinexaDaoException;
	
	public List<MasterMFHoldingMaturity> getMasterMfHoldingMaturityListByIsin(String isin, Session session)
			throws FinexaDaoException;
	public List<MasterEquityDailyPrice> getMasterEquityDailyPriceListOnDate(String iSIN, Date startDate1, Date endDate1,
			Session session) throws FinexaDaoException;

	public List<MasterIndexDailyNAV> getMasterindexdailynavListOnDate(String Name, Date startDate, Date endDate, Session session)
			throws FinexaDaoException;
	
	public List<MasterEquityDailyPrice> getMasterEquityDailyPriceListTimePeriod(Date currentDate, Date toDate, String ISIN,
			Session session) throws FinexaDaoException;
	
	public List<MasterEquityDailyPrice> getMasterEquityDailyClosingPrice(Date currentDate, Date toDate, String ISIN,
			Session session) throws FinexaDaoException;

	public List<MasterIndexDailyNAV> getMasterIndexDailyNAVListTimePeriod(Date currentDate, Date toDate, String name,
			Session session) throws FinexaDaoException;
	
	public List<MasterIndexDailyNAV> getMasterIndexDailyNAVClosingPrice(Date currentDate, Date toDate, String name,
			Session session) throws FinexaDaoException;


	public MasterMFMarketCap getMasterMfMarketcap(String ISIN, Session session) throws FinexaDaoException;

	public Master52WeekHighLowMF getMaster52WeekHighLowMF(String ISIN, Session session) throws FinexaDaoException;

	//public MasterMFAssetAllocation getMasterMFAssetAllocation(String ISIN, Session session) throws FinexaDaoException;

	public MasterIndexName getMasterIndexName(String ISIN, Session session) throws FinexaDaoException;

	public MasterFundManager getMasterFundManager(String ISIN, Session session) throws FinexaDaoException;

	public List<MasterMFDailyNAV> getMasterMFDailyNAVListOnDate(String ISIN, Date startDate, Date endDate, Session session)
			throws FinexaDaoException;

	public List<MasterMFDailyNAV> getMasterMFListTimePeriod(Date currentDate, Date toDate, String ISIN, Session session)
			throws FinexaDaoException;

	public List<MasterMFDailyNAV> getMasterMFListClosingPrice(Date currentDate, Date toDate, String ISIN, Session session)
			throws FinexaDaoException;
	
	public List<MasterCompany> getMasterCompany(int companyCode, Session session) throws FinexaDaoException;

	public List<Object[]> getMFSectorHolding(String ISIN, Date curDate, Date toDate, Session session)
			throws FinexaDaoException;

	public List<Object[]> getMFSectorHolding(String ISIN, Session session) throws FinexaDaoException;

	public ClientMutualFund getClientMutualFund(int id, Session session) throws FinexaDaoException;

	public Date getRatingWiseMaxDate(String isin, Session session) throws FinexaDaoException;

	public List<MasterMFHoldingRatingWiseDTO> getMasterMFHoldingRatingWiseList(String isin, Date maxDate, Session session) throws FinexaDaoException;;
	/*	public List<MasterEquityDailyPrice> getMasterEquityDailyPriceOnDate(String ISIN, Date startDate, Date pastDate, Session session)
			throws FinexaDaoException;
*/
	

	
}