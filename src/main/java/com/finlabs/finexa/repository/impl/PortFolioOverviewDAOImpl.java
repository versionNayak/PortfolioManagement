package com.finlabs.finexa.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.dto.MasterMFHoldingRatingWiseDTO;
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
import com.finlabs.finexa.model.MasterMFHoldingMaturity;
import com.finlabs.finexa.model.MasterMFHoldingRatingWise;
import com.finlabs.finexa.model.MasterMFMarketCap;
import com.finlabs.finexa.model.MasterMFMaturityProfile;
import com.finlabs.finexa.model.MasterMFRatio;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterProductRecommendationRank;
import com.finlabs.finexa.repository.PortFolioOverviewDAO;

//@Repository
public class PortFolioOverviewDAOImpl implements PortFolioOverviewDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientEquity> getClientPortFolioEquityOverview(int clientId, Session session)
			throws FinexaDaoException {
		List<ClientEquity> portFolioOverviewEquityList = null;
		try {
			portFolioOverviewEquityList = (List<ClientEquity>) session.createCriteria(ClientEquity.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewEquityList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@Override
	public MasterEquityMarketCapClassification getMasterDirectEquityMarketCapList(String ISIN, Session session)
			throws FinexaDaoException {
		try {
			MasterEquityMarketCapClassification masterEquityMarketCapClassification = null;
			masterEquityMarketCapClassification = (MasterEquityMarketCapClassification) session.createQuery(
					"SELECT memc FROM MasterDirectEquityMarketCap mdmc left join mdmc.masterEquityMarketCapClassification memc WHERE mdmc.masterDirectEquity.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();
			// session.clear();
			return masterEquityMarketCapClassification;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientPPF> getClientPPF(int clientId, Session session) throws FinexaDaoException {
		List<ClientPPF> portFolioOverviewPpfList = null;
		try {
			portFolioOverviewPpfList = (List<ClientPPF>) session.createCriteria(ClientPPF.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewPpfList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientEPF> getClientEpfList(int clientId, Session session) throws FinexaDaoException {
		List<ClientEPF> portFolioOverviewEpfList = null;
		try {
			portFolioOverviewEpfList = (List<ClientEPF>) session.createCriteria(ClientEPF.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewEpfList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientNPS> getClientNpsList(int clientId, Session session) throws FinexaDaoException {
		List<ClientNPS> portFolioOverviewNpsList = null;
		try {
			portFolioOverviewNpsList = (List<ClientNPS>) session.createCriteria(ClientNPS.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewNpsList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientAtalPensionYojana> getAtalPensionYojanaList(int clientId, Session session)
			throws FinexaDaoException {
		List<ClientAtalPensionYojana> portFolioOverviewAPYList = null;
		try {
			portFolioOverviewAPYList = (List<ClientAtalPensionYojana>) session
					.createCriteria(ClientAtalPensionYojana.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewAPYList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientAnnuity> getClientAnnuityList(int clientId, Session session) throws FinexaDaoException {
		List<ClientAnnuity> portFolioOverviewAnnuityList = null;
		try {
			portFolioOverviewAnnuityList = (List<ClientAnnuity>) session.createCriteria(ClientAnnuity.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewAnnuityList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientFixedIncome> getClientFixedincomeList(int clientId, Session session) throws FinexaDaoException {
		List<ClientFixedIncome> portFolioOverviewClientFixedIncomeList = null;
		try {
			portFolioOverviewClientFixedIncomeList = (List<ClientFixedIncome>) session
					.createCriteria(ClientFixedIncome.class).add(Restrictions.eq("clientMaster.id", (int) clientId))
					.list();
			// session.clear();
			return portFolioOverviewClientFixedIncomeList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@Override
	public MasterLTSTClassification getMasterLTSTClassificationList(int maturityterm, Session session)
			throws FinexaDaoException {
		MasterLTSTClassification masterLTSTClassification = null;
		try {
			masterLTSTClassification = (MasterLTSTClassification) session.createQuery(
					"SELECT mltst FROM MasterLTSTClassification mltst  WHERE  (  (:maturityterm >= mltst.avgMaturityStart  AND    2<=mltst.avgMaturityEnd  ) OR (:maturityterm <=mltst.avgMaturityEnd AND mltst.avgMaturityStart IS NULL ) ) ")
					.setInteger("maturityterm", maturityterm).uniqueResult();
			// session.clear();
			return masterLTSTClassification;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientSmallSaving> getClientSmallSavingsList(int clientId, Session session) throws FinexaDaoException {
		List<ClientSmallSaving> portFolioOverviewClientSmallSavingList = null;
		try {
			portFolioOverviewClientSmallSavingList = (List<ClientSmallSaving>) session
					.createCriteria(ClientSmallSaving.class).add(Restrictions.eq("clientMaster.id", (int) clientId))
					.list();
			// session.clear();
			return portFolioOverviewClientSmallSavingList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientMutualFund> getClientMutualFundList(int clientId, Session session) throws FinexaDaoException {
		List<ClientMutualFund> portFolioOverviewClientMutualFundList = null;
		try {
			portFolioOverviewClientMutualFundList = (List<ClientMutualFund>) session
					.createCriteria(ClientMutualFund.class).add(Restrictions.eq("clientMaster.id", (int) clientId))
					.list();
			// session.clear();
			return portFolioOverviewClientMutualFundList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaDaoException(e);

		}
	}

	@Override
	public MasterMutualFundETF getMasterMutualFundETF(String ISIN, Session session) throws FinexaDaoException {
		MasterMutualFundETF portFolioOverviewMasterMutualFundETF = null;
		try {
			portFolioOverviewMasterMutualFundETF = (MasterMutualFundETF) session
					.createQuery("SELECT mmde FROM MasterMutualFundETF mmde WHERE mmde.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();
			// session.clear();
			return portFolioOverviewMasterMutualFundETF;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public MasterMFMarketCap getMasterMFMarketCap(String ISIN, Session session) throws FinexaDaoException {
		MasterMFMarketCap portFolioOverviewMasterMFMarketCap = null;
		Date maxDate = null;
		try {
			 maxDate = (Date) session.createQuery(
					"SELECT max(marketCap.id.date) FROM MasterMFMarketCap marketCap WHERE marketCap.id.masterMutualFundEtf.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();
			if(maxDate != null) {
				portFolioOverviewMasterMFMarketCap = (MasterMFMarketCap) session
						.createQuery("SELECT marketCap FROM MasterMFMarketCap marketCap WHERE marketCap.id.masterMutualFundEtf.isin=:ISIN and date=:maxDate")
						.setString("ISIN", ISIN).setDate("maxDate",maxDate).uniqueResult();
				
			}
			if(portFolioOverviewMasterMFMarketCap == null) {
			// get primary_fd_code from accordSchemeDetails
			//Integer requiredSchemeCode = null;
			//requiredSchemeCode = (Integer)session
				//	.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
				//	.setString("isin", ISIN).uniqueResult();
				
				List<Integer> requiredSchemeCodeList = null;
				requiredSchemeCodeList = (List<Integer>)session
						.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
						.setString("isin", ISIN).list();
			
		
			Integer primaryFdCode= null;
			if (requiredSchemeCodeList != null && !requiredSchemeCodeList.isEmpty()) {
				Integer requiredSchemeCode = requiredSchemeCodeList.get(0);
				primaryFdCode = (Integer)session
						.createQuery("SELECT schemeDetails.primaryFdCode FROM AccordSchemeDetails schemeDetails WHERE schemeDetails.schemeCode=:requiredSchemeCode")
						.setInteger("requiredSchemeCode", requiredSchemeCode).uniqueResult();
				
				List<String> isinList = null;
				if (primaryFdCode != null) {
					isinList = (List<String>)session
							.createQuery("SELECT asm.id.isin FROM AccordSchemeDetails asd,AccordSchemeIsinMaster asm  WHERE asm.id.schemeCode = asd.schemeCode and asd.primaryFdCode=:primaryFdCode")
							.setInteger("primaryFdCode", primaryFdCode).list();
				}
				if (isinList != null) {
					for (String isinObj : isinList) {
						 maxDate = (Date) session.createQuery(
								"SELECT max(marketCap.id.date) FROM MasterMFMarketCap marketCap WHERE marketCap.id.masterMutualFundEtf.isin=:ISIN")
								.setString("ISIN", isinObj).uniqueResult();
						if(maxDate != null) {
							portFolioOverviewMasterMFMarketCap = (MasterMFMarketCap) session
									.createQuery("SELECT marketCap FROM MasterMFMarketCap marketCap WHERE marketCap.id.masterMutualFundEtf.isin=:ISIN and date=:maxDate")
									.setString("ISIN", isinObj).setDate("maxDate",maxDate).uniqueResult();
							if (portFolioOverviewMasterMFMarketCap != null) {
								break;
							}
						}
					}
				}
			}
		}
			return portFolioOverviewMasterMFMarketCap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientCash> getClientCashList(int clientId, Session session) throws FinexaDaoException {
		List<ClientCash> portFolioOverviewClientCashList = null;
		try {
			portFolioOverviewClientCashList = (List<ClientCash>) session.createCriteria(ClientCash.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewClientCashList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientPreciousMetal> getClientPreciousList(int clientId, Session session) throws FinexaDaoException {
		List<ClientPreciousMetal> portFolioOverviewClientPreciousMetalList = null;
		try {
			portFolioOverviewClientPreciousMetalList = (List<ClientPreciousMetal>) session
					.createCriteria(ClientPreciousMetal.class).add(Restrictions.eq("clientMaster.id", (int) clientId))
					.list();
			// session.clear();
			return portFolioOverviewClientPreciousMetalList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientRealEstate> getClientRealEstateList(int clientId, Session session) throws FinexaDaoException {
		List<ClientRealEstate> portFolioOverviewClientRealEstateList = null;
		try {
			portFolioOverviewClientRealEstateList = (List<ClientRealEstate>) session
					.createCriteria(ClientRealEstate.class).add(Restrictions.eq("clientMaster.id", (int) clientId))
					.list();
			// session.clear();
			return portFolioOverviewClientRealEstateList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientVehicle> getClientVehicleList(int clientId, Session session) throws FinexaDaoException {
		List<ClientVehicle> portFolioOverviewClientVehicleList = null;
		try {
			portFolioOverviewClientVehicleList = (List<ClientVehicle>) session.createCriteria(ClientVehicle.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewClientVehicleList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientOtherAlternateAsset> getClientOtherAlternateAssetList(int clientId, Session session)
			throws FinexaDaoException {
		List<ClientOtherAlternateAsset> portFolioOverviewClientOtherAlternateAssetList = null;
		try {
			portFolioOverviewClientOtherAlternateAssetList = (List<ClientOtherAlternateAsset>) session
					.createCriteria(ClientOtherAlternateAsset.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewClientOtherAlternateAssetList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientStructuredProduct> getClientStructuredProductList(int clientId, Session session)
			throws FinexaDaoException {
		List<ClientStructuredProduct> portFolioOverviewClientStructuredProductList = null;
		try {
			portFolioOverviewClientStructuredProductList = (List<ClientStructuredProduct>) session
					.createCriteria(ClientStructuredProduct.class)
					.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return portFolioOverviewClientStructuredProductList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientLumpsumInflow> getClientLumpsumInflowsumList(int clientId, Session session)
			throws FinexaDaoException {
		List<ClientLumpsumInflow> portFolioOverviewClientLumpsumInflowsumList = null;
		try {
			portFolioOverviewClientLumpsumInflowsumList = (List<ClientLumpsumInflow>) session
					.createCriteria(ClientLumpsumInflow.class).add(Restrictions.eq("clientMaster.id", (int) clientId))
					.list();
			// session.clear();
			return portFolioOverviewClientLumpsumInflowsumList;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterProductRecommendationRank> getMasterproductrecommendationrankList(Session session)
			throws FinexaDaoException {
		List<MasterProductRecommendationRank> masterProductRecommendationRank = null;
		try {
			masterProductRecommendationRank = (List<MasterProductRecommendationRank>) session
					.createCriteria(MasterProductRecommendationRank.class).list();
			// session.clear();
			return masterProductRecommendationRank;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@Override
	public MasterEquityMarketCapClassification getMasterequitymarketcapclassificationList(String ISIN, Session session)
			throws FinexaDaoException {
		MasterEquityMarketCapClassification masterEquityMarketCapClassification = null;
		try {
			masterEquityMarketCapClassification = (MasterEquityMarketCapClassification) session.createQuery(
					"SELECT mmde FROM MasterEquityMarketCapClassification mmde left join mmde.masterDirectEquityMarketCaps mdem WHERE mdem.masterDirectEquity.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();
			// session.clear();
			return masterEquityMarketCapClassification;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public MasterMFMaturityProfile getMasterMFMaturityProfile(String isin, Session session) throws FinexaDaoException {
		// TODO Auto-generated method stub
		MasterMFMaturityProfile masterMFMaturityProfile = null;
		try {
			Date maxDate = (Date)session.createQuery(
					"SELECT max(maturityProf.id.invdate) FROM MasterMFMaturityProfile maturityProf WHERE maturityProf.id.isin=:isin").setString("isin", isin).uniqueResult();
			if (maxDate != null) {
				masterMFMaturityProfile = (MasterMFMaturityProfile) session.createQuery(
						"SELECT maturityProf FROM MasterMFMaturityProfile maturityProf WHERE maturityProf.id.isin=:isin and maturityProf.id.invdate=:maxDate")
						.setString("isin", isin).setDate("maxDate", maxDate).uniqueResult();
				
			}
			if(masterMFMaturityProfile == null) {
			//Integer requiredSchemeCode = null;
			//requiredSchemeCode = (Integer)session
			//		.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
			//		.setString("isin", isin).uniqueResult();
				
			List<Integer> requiredSchemeCodeList = null;
			requiredSchemeCodeList = (List<Integer>)session
					.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
					.setString("isin", isin).list();
		
			Integer primaryFdCode= null;
			//if (requiredSchemeCode != null) {
			if (requiredSchemeCodeList != null && !requiredSchemeCodeList.isEmpty()) {
				Integer requiredSchemeCode = requiredSchemeCodeList.get(0);
				primaryFdCode = (Integer)session
						.createQuery("SELECT schemeDetails.primaryFdCode FROM AccordSchemeDetails schemeDetails WHERE schemeDetails.schemeCode=:requiredSchemeCode")
						.setInteger("requiredSchemeCode", requiredSchemeCode).uniqueResult();
				
				List<String> isinList = null;
				if (primaryFdCode != null) {
					isinList = (List<String>)session
							.createQuery("SELECT asm.id.isin FROM AccordSchemeDetails asd,AccordSchemeIsinMaster asm  WHERE asm.id.schemeCode = asd.schemeCode and asd.primaryFdCode=:primaryFdCode")
							.setInteger("primaryFdCode", primaryFdCode).list();
				}
				if (isinList != null) {
					for (String isinObj : isinList) {
						 maxDate = (Date)session.createQuery(
								"SELECT max(maturityProf.id.invdate) FROM MasterMFMaturityProfile maturityProf WHERE maturityProf.id.isin=:isin").setString("isin", isinObj).uniqueResult();
						if (maxDate != null) {
							masterMFMaturityProfile = (MasterMFMaturityProfile) session.createQuery(
									"SELECT maturityProf FROM MasterMFMaturityProfile maturityProf WHERE maturityProf.id.isin=:isin and maturityProf.id.invdate=:maxDate")
									.setString("isin", isinObj).setDate("maxDate", maxDate).uniqueResult();
							if(masterMFMaturityProfile != null) {
								break;
							}
						}
					}
				}
			}
		}
			// session.clear();
			return masterMFMaturityProfile;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public MasterMFRatio getRiskMeasureMap(String isin, Session session) throws FinexaDaoException {
		// TODO Auto-generated method stub
		try {
			MasterMFRatio masterMFRatio = null;
			
			masterMFRatio = (MasterMFRatio) session.createQuery(
						"SELECT ratio FROM MasterMFRatio ratio WHERE ratio.id.isin=:isin ORDER BY ratio.id.upddate desc")
						.setString("isin", isin).setMaxResults(1).uniqueResult();
			

			if(masterMFRatio == null) {
			//Integer requiredSchemeCode = null;
			
			//requiredSchemeCode = (Integer)session
				//	.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
				//	.setString("isin", isin).uniqueResult();
				
			List<Integer> requiredSchemeCodeList = null;
				requiredSchemeCodeList = (List<Integer>)session
						.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
						.setString("isin", isin).list();
		
			Integer primaryFdCode= null;
			//if (requiredSchemeCode != null) {
			if (requiredSchemeCodeList != null && !requiredSchemeCodeList.isEmpty()) {
				Integer requiredSchemeCode = requiredSchemeCodeList.get(0);
				primaryFdCode = (Integer)session
						.createQuery("SELECT schemeDetails.primaryFdCode FROM AccordSchemeDetails schemeDetails WHERE schemeDetails.schemeCode=:requiredSchemeCode")
						.setInteger("requiredSchemeCode", requiredSchemeCode).uniqueResult();
				
				List<String> isinList = null;
				if (primaryFdCode != null) {
					isinList = (List<String>)session
							.createQuery("SELECT asm.id.isin FROM AccordSchemeDetails asd,AccordSchemeIsinMaster asm  WHERE asm.id.schemeCode = asd.schemeCode and asd.primaryFdCode=:primaryFdCode")
							.setInteger("primaryFdCode", primaryFdCode).list();
				}
				if (isinList != null) {
					for (String isinObj : isinList) {
						Date maxDate = (Date)session.createQuery(
							"SELECT max(ratio.id.upddate) FROM MasterMFRatio ratio WHERE ratio.id.isin=:isin").setString("isin", isinObj).uniqueResult();
					if (maxDate != null) {
						masterMFRatio = (MasterMFRatio) session.createQuery(
								"SELECT ratio FROM MasterMFRatio ratio WHERE ratio.id.isin=:isin and ratio.id.upddate=:maxDate")
								.setString("isin", isinObj).setDate("maxDate", maxDate).uniqueResult();
					}
				}
			  }
		  }
		}
			// session.clear();
			return masterMFRatio;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaDaoException(e.getMessage());

		}
	}
	
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public String getAccordSchemeIsinMaster(String isin, Session
	 * session) throws FinexaDaoException { String series = ""; try {
	 * 
	 * series = (String)session
	 * .createQuery("SELECT schemeIsinMaster.series FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin"
	 * ) .setString("isin", isin).uniqueResult(); // session.clear(); return series;
	 * } catch (Exception e) { throw new FinexaDaoException(e);
	 * 
	 * } }
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAccordSchemeIsinMasterList(String isin, Session session)
			throws FinexaDaoException {
		List<String> series = null;
		try {
			
			 series = (List<String>)session
					.createQuery("SELECT schemeIsinMaster.series FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
					.setString("isin", isin).list();
			// session.clear();
			return series;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}


}
