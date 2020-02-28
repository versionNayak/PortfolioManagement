package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.AccordSchemeIsinMaster;
import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientCash;
import com.finlabs.finexa.model.ClientEPF;
import com.finlabs.finexa.model.ClientEquity;
import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientLumpsumInflow;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.ClientNPS;
import com.finlabs.finexa.model.ClientOtherAlternateAsset;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.ClientPreciousMetal;
import com.finlabs.finexa.model.ClientRealEstate;
import com.finlabs.finexa.model.ClientSmallSaving;
import com.finlabs.finexa.model.ClientStructuredProduct;
import com.finlabs.finexa.model.ClientVehicle;
import com.finlabs.finexa.model.MasterEquityMarketCapClassification;
import com.finlabs.finexa.model.MasterLTSTClassification;
import com.finlabs.finexa.model.MasterMFMarketCap;
import com.finlabs.finexa.model.MasterMFMaturityProfile;
import com.finlabs.finexa.model.MasterMFRatio;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterProductRecommendationRank;

public interface PortFolioOverviewDAO {

	public List<ClientEquity> getClientPortFolioEquityOverview(int clientId, Session session) throws FinexaDaoException;

	public List<ClientPPF> getClientPPF(int clientId, Session session) throws FinexaDaoException;

	public List<ClientEPF> getClientEpfList(int clientId, Session session) throws FinexaDaoException;

	public List<ClientNPS> getClientNpsList(int clientId, Session session) throws FinexaDaoException;

	public List<ClientAtalPensionYojana> getAtalPensionYojanaList(int clientId, Session session)
			throws FinexaDaoException;

	public List<ClientAnnuity> getClientAnnuityList(int clientId, Session session) throws FinexaDaoException;

	public List<ClientFixedIncome> getClientFixedincomeList(int clientId, Session session) throws FinexaDaoException;

	public MasterLTSTClassification getMasterLTSTClassificationList(int maturityYear, Session session)
			throws FinexaDaoException;

	public List<ClientSmallSaving> getClientSmallSavingsList(int clientId, Session session) throws FinexaDaoException;

	public List<ClientMutualFund> getClientMutualFundList(int clientId, Session session) throws FinexaDaoException;

	public MasterMutualFundETF getMasterMutualFundETF(String ISIN, Session session) throws FinexaDaoException;
	public MasterMFMarketCap getMasterMFMarketCap(String ISIN, Session session) throws FinexaDaoException;
	
	public List<ClientCash> getClientCashList(int clientId, Session session) throws FinexaDaoException;

	public List<ClientPreciousMetal> getClientPreciousList(int clientId, Session session) throws FinexaDaoException;

	public List<ClientRealEstate> getClientRealEstateList(int clientId, Session session) throws FinexaDaoException;

	public List<ClientVehicle> getClientVehicleList(int clientId, Session session) throws FinexaDaoException;

	public List<ClientOtherAlternateAsset> getClientOtherAlternateAssetList(int clientId, Session session)
			throws FinexaDaoException;

	public List<ClientStructuredProduct> getClientStructuredProductList(int clientId, Session session)
			throws FinexaDaoException;

	public List<ClientLumpsumInflow> getClientLumpsumInflowsumList(int clientId, Session session)
			throws FinexaDaoException;

	public List<MasterProductRecommendationRank> getMasterproductrecommendationrankList(Session session)
			throws FinexaDaoException;

	public MasterEquityMarketCapClassification getMasterequitymarketcapclassificationList(String ISIN, Session session)
			throws FinexaDaoException;
	/*
	 * public List<MasterProductClassification> getMasterProductClassifications(int
	 * clientId, Session session) throws FinexaDaoException; public
	 * List<ClientOtherAlternateAsset> getClientOtherAlternateAssetList(int
	 * clientId, Session session) throws FinexaDaoException;
	 */

	public MasterEquityMarketCapClassification getMasterDirectEquityMarketCapList(String isin, Session session)
			throws FinexaDaoException;
	
	public MasterMFMaturityProfile getMasterMFMaturityProfile(String isin, Session session)
			throws FinexaDaoException;
	
	public MasterMFRatio getRiskMeasureMap(String isin, Session session)
			throws FinexaDaoException;

	//public String getAccordSchemeIsinMaster(String isin, Session session) throws FinexaDaoException;

	public List<String> getAccordSchemeIsinMasterList(String isin, Session session) throws FinexaDaoException;

}