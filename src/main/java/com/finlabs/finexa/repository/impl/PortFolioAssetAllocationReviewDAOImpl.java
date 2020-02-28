 package com.finlabs.finexa.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.GenericDao;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.LookupAssetAllocation;
import com.finlabs.finexa.model.MasterAssetClassReturn;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterExpectedHistoricalReturn;
import com.finlabs.finexa.model.MasterIndexDailyNAV;
import com.finlabs.finexa.model.MasterMFHolding;
import com.finlabs.finexa.model.MasterMFHoldingRatingWise;
import com.finlabs.finexa.model.MasterMFHoldingSectorWise;
import com.finlabs.finexa.model.MasterMFProductRecommendation;
import com.finlabs.finexa.model.MasterMFRatio;
import com.finlabs.finexa.model.MasterSectorBenchmarkMapping;
import com.finlabs.finexa.model.MasterSubAssetClassReturn;
import com.finlabs.finexa.repository.PortFolioAssetAllocationReviewDAO;

//@Repository
public class PortFolioAssetAllocationReviewDAOImpl implements PortFolioAssetAllocationReviewDAO {
	/*@Autowired
	GenericDao genericDao;*/
	
	@Override
	public ClientMaster getClientById(int clientid, Session session) throws FinexaDaoException {
		try {
			ClientMaster clientMaster = (ClientMaster) session.get(ClientMaster.class, new Integer(clientid));
			// session.clear();
			return clientMaster;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LookupAssetAllocation> getLookupAssetAllocationList(int riskScore, Session session)
			throws FinexaDaoException {
		try {
			List<LookupAssetAllocation> lookupAssetAllocationList = null;
			lookupAssetAllocationList = session.createQuery(
					"SELECT lua FROM LookupAssetAllocation lua  LEFT JOIN lua.lookupAssetAllocationCategory  laac LEFT JOIN lua.lookupAssetSubClass lasc  WHERE laac.id=:riskScore")
					.setInteger("riskScore", riskScore).list();
			// session.clear();
			return lookupAssetAllocationList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LookupAssetAllocation> getLookupSubAssetAllocationList(int riskScore, String subAssetName,
			Session session) throws FinexaDaoException {
		try {
			List<LookupAssetAllocation> lookupAssetAllocationList = null;
			lookupAssetAllocationList = session.createQuery(
					"SELECT lua FROM LookupAssetAllocation lua  LEFT JOIN lua.lookupAssetAllocationCategory  laac LEFT JOIN lua.lookupAssetSubClass lasc  WHERE   lasc.description= :subAssetName AND laac.id=:riskScore")
					.setString("subAssetName", subAssetName).setInteger("riskScore", riskScore).list();
			// session.clear();
			return lookupAssetAllocationList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@Override
	public List<MasterSubAssetClassReturn> getMasterSubAssetClassReturnList(Session session) throws FinexaDaoException {
		List<MasterSubAssetClassReturn> masterSubAssetClassReturnList = null;
		try {
			GenericDao genericDao = new GenericDao();
			masterSubAssetClassReturnList = genericDao.loadAll(MasterSubAssetClassReturn.class, session);
			// session.clear();
			return masterSubAssetClassReturnList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterIndexDailyNAV> getMasterindexdailynavList(int indexCode,Session session) throws FinexaDaoException {
		List<MasterIndexDailyNAV> masterindexdailynavList = null;
		try {
			masterindexdailynavList = session.createQuery("SELECT midn FROM MasterIndexDailyNAV midn where midn.id.masterIndexName.id=:indexCode")
					.setInteger("indexCode", indexCode).list();

			// genericDao.loadAll(Masterindexdailynav.class);
			// session.clear();
			return masterindexdailynavList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@Override
	public MasterDirectEquity getMasterDirectEquityList(String ISIN, Session session) throws FinexaDaoException {
		try {
			MasterDirectEquity lookupAssetAllocation = null;
			lookupAssetAllocation = (MasterDirectEquity) session
					.createQuery("SELECT mde FROM MasterDirectEquity mde WHERE mde.isin=:ISIN").setString("ISIN", ISIN)
					.uniqueResult();
			// session.clear();
			return lookupAssetAllocation;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFHoldingSectorWise> getMasterMfHoldingSectorwiseList(String isin, Session session)
			throws FinexaDaoException {
		List<MasterMFHoldingSectorWise> masterMfHoldingSectorwiseList = null;
		Date maxDate = null;
		try {
			 maxDate = (Date) session.createQuery(
					"SELECT max(sectorWise.id.date) FROM MasterMFHoldingSectorWise sectorWise WHERE sectorWise.id.masterMutualFundEtf.isin=:isin")
					.setString("isin", isin).uniqueResult();
			if(maxDate != null) {
				masterMfHoldingSectorwiseList = (List<MasterMFHoldingSectorWise>) session.createQuery(
						"SELECT sectorWise FROM MasterMFHoldingSectorWise sectorWise WHERE sectorWise.id.masterMutualFundEtf.isin=:isin and  sectorWise.id.date=:date")
						.setString("isin", isin).setDate("date", maxDate).list();
				
			}
			if(masterMfHoldingSectorwiseList == null) {
			Integer requiredSchemeCode = null;
			/*requiredSchemeCode = (Integer)session
					.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
					.setString("isin", isin).uniqueResult();
		*/
          List<Integer> requiredSchemeCodeList = null;
          requiredSchemeCodeList = (List<Integer>)session
    			.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
    			 .setString("isin", isin).list();
			Integer primaryFdCode= null;
			if (requiredSchemeCodeList.size() == 1 ) {
				requiredSchemeCode=requiredSchemeCodeList.get(0);
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
							"SELECT max(sectorWise.id.date) FROM MasterMFHoldingSectorWise sectorWise WHERE sectorWise.id.masterMutualFundEtf.isin=:isin")
							.setString("isin", isinObj).uniqueResult();
					if(maxDate != null) {
						masterMfHoldingSectorwiseList = (List<MasterMFHoldingSectorWise>) session.createQuery(
								"SELECT sectorWise FROM MasterMFHoldingSectorWise sectorWise WHERE sectorWise.id.masterMutualFundEtf.isin=:isin and  sectorWise.id.date=:date")
								.setString("isin", isinObj).setDate("date", maxDate).list();
						if(masterMfHoldingSectorwiseList != null && !masterMfHoldingSectorwiseList.isEmpty()) {
							break;
						}
					}
				 }
					
			  }
		  }
       }
           
			// session.clear();
			return masterMfHoldingSectorwiseList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public List<MasterMFHoldingRatingWise> getMasterMfHoldingRatingWiseList(String isin, Session session)
			throws FinexaDaoException {
		List<MasterMFHoldingRatingWise> masterMfHoldingRatingWiseList = null;
		Date maxDate = null;
		
		try {
			maxDate = (Date) session.createQuery(
					"SELECT max(ratingWise.id.date) FROM MasterMFHoldingRatingWise ratingWise WHERE ratingWise.id.masterMutualFundEtf.isin=:isin")
					.setString("isin", isin).uniqueResult();
			System.out.println("maxDate "+maxDate);
			if(maxDate != null) {
				masterMfHoldingRatingWiseList = (List<MasterMFHoldingRatingWise>) session.createQuery(
						"SELECT ratingWise FROM MasterMFHoldingRatingWise ratingWise WHERE ratingWise.id.masterMutualFundEtf.isin=:isin and  ratingWise.id.date=:maxDate")
						.setString("isin", isin).setDate("maxDate", maxDate).list();
				
			}
			
			if(masterMfHoldingRatingWiseList == null) {
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
						 maxDate = (Date) session.createQuery(
							"SELECT max(ratingWise.id.date) FROM MasterMFHoldingRatingWise ratingWise WHERE ratingWise.id.masterMutualFundEtf.isin=:isin")
							.setString("isin", isinObj).uniqueResult();
					if(maxDate != null) {
						masterMfHoldingRatingWiseList = (List<MasterMFHoldingRatingWise>) session.createQuery(
								"SELECT ratingWise FROM MasterMFHoldingRatingWise ratingWise WHERE ratingWise.id.masterMutualFundEtf.isin=:isin and  ratingWise.id.date=:maxDate")
								.setString("isin", isinObj).setDate("maxDate", maxDate).list();
						if(masterMfHoldingRatingWiseList != null && !masterMfHoldingRatingWiseList.isEmpty()) {
							break;
						}
						}
					}
					
				}
			}
		}
			// session.clear();
			return masterMfHoldingRatingWiseList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterSectorBenchmarkMapping> getMasterSectorBenchmarkList(Session session) throws FinexaDaoException {
		List<MasterSectorBenchmarkMapping> masterSectorBenchmarkMapping = null;
		try {
			masterSectorBenchmarkMapping = session.createQuery("SELECT mmfs FROM MasterSectorBenchmarkMapping mmfs ")
					.list();
			// session.clear();
			return masterSectorBenchmarkMapping;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterExpectedHistoricalReturn> getRateOfReturn(int year, Session session) throws FinexaDaoException {
		List<MasterExpectedHistoricalReturn> rateOfReturnList = null;
		try {
			rateOfReturnList = session
					.createQuery("SELECT mhr FROM MasterExpectedHistoricalReturn mhr where mhr.year=:year")
					.setInteger("year", year).list();
			// session.clear();
			return rateOfReturnList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@Override
	public MasterAssetClassReturn getMLRFromMaster(int assetClassid, Session session) throws FinexaDaoException {
		try {
			MasterAssetClassReturn mlr = null;
			mlr = (MasterAssetClassReturn) session.createQuery(
					"SELECT masetReturn FROM MasterAssetClassReturn masetReturn WHERE masetReturn.lookupAssetClass.id=:assetClassid")
					.setInteger("assetClassid", assetClassid).uniqueResult();
			// session.clear();
			return mlr;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getProductListPM(int assetSubClass, int advisorId, Session session) throws FinexaDaoException {
		List<String> productList = null;
		try {
			productList = session.createQuery(
					"SELECT mpf.schemeName FROM MasterMFProductRecommendation mpf where mpf.lookupAssetSubClass.id=:assetSubClass and mpf.id.advisorMaster.id=:advisorId")
					.setByte("assetSubClass", (byte) assetSubClass).setInteger("advisorId",advisorId).list();
			session.clear();
			return productList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFProductRecommendation> getProductIsinListPM(int assetSubClass, int advisorId, Session session)
			throws FinexaDaoException {
		// TODO Auto-generated method stub

		List<MasterMFProductRecommendation> productIsinDTOList = null;
		try {
			productIsinDTOList = session.createQuery(
					"SELECT mpf FROM MasterMFProductRecommendation mpf where mpf.lookupAssetSubClass.id=:assetSubClass and mpf.id.advisorMaster.id=:advisorId")
					.setByte("assetSubClass", (byte) assetSubClass).setInteger("advisorId", advisorId).list();
//			session.clear();
			return productIsinDTOList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

}