package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupAssetAllocation;
import com.finlabs.finexa.model.MasterAssetClassReturn;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterExpectedHistoricalReturn;
import com.finlabs.finexa.model.MasterIndexDailyNAV;
import com.finlabs.finexa.model.MasterMFHoldingRatingWise;
import com.finlabs.finexa.model.MasterMFHoldingSectorWise;
import com.finlabs.finexa.model.MasterMFProductRecommendation;
import com.finlabs.finexa.model.MasterSectorBenchmarkMapping;
import com.finlabs.finexa.model.MasterSubAssetClassReturn;

public interface PortFolioAssetAllocationReviewDAO {
	public List<LookupAssetAllocation> getLookupAssetAllocationList(int riskScore, Session session)
			throws FinexaDaoException;

	public List<LookupAssetAllocation> getLookupSubAssetAllocationList(int riskScore, String subAssetName,
			Session session) throws FinexaDaoException;

	public List<MasterSubAssetClassReturn> getMasterSubAssetClassReturnList(Session session) throws FinexaDaoException;

	public List<MasterIndexDailyNAV> getMasterindexdailynavList(int indexCode, Session session) throws FinexaDaoException;

	public MasterDirectEquity getMasterDirectEquityList(String ISIN, Session session) throws FinexaDaoException;

	public List<MasterMFHoldingSectorWise> getMasterMfHoldingSectorwiseList(String isin, Session session)
			throws FinexaDaoException;

	public List<MasterMFHoldingRatingWise> getMasterMfHoldingRatingWiseList(String AMFI, Session session)
			throws FinexaDaoException;

	public List<MasterSectorBenchmarkMapping> getMasterSectorBenchmarkList(Session session) throws FinexaDaoException;

	// getting historical rate of return
	public List<MasterExpectedHistoricalReturn> getRateOfReturn(int year, Session session) throws FinexaDaoException;

	public MasterAssetClassReturn getMLRFromMaster(int assetClassid, Session session) throws FinexaDaoException;
	
	// getting recommended product
	public List<String> getProductListPM(int assetSubClass, int advisorId, Session session) throws FinexaDaoException;
	
	// getting recommended isin
	public List<MasterMFProductRecommendation> getProductIsinListPM(int assetSubClass, int advisorId, Session session) throws FinexaDaoException;

	ClientMaster getClientById(int clientid, Session session) throws FinexaDaoException;



}