package com.finlabs.finexa.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.dto.MasterMFHoldingRatingWiseDTO;
import com.finlabs.finexa.dto.MasterMFSectorHoldingWiseDTO;
import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
//import com.finlabs.finexa.controller.PortFolioPagerTemplateController;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.AccordSchemeDetails;
import com.finlabs.finexa.model.AccordSchemeIsinMaster;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.Master52WeekHighLowMF;
import com.finlabs.finexa.model.MasterCompany;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterDirectEquityMarketCap;
import com.finlabs.finexa.model.MasterEquityDailyPrice;
import com.finlabs.finexa.model.MasterFundManager;
//import com.finlabs.finexa.model.MasterEquityMarketCapClassification;
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
import com.finlabs.finexa.repository.PortFolioPagerDAO;

//@Repository
public class PortFolioPagerTemplateDAOImpl implements PortFolioPagerDAO {
	Logger log = Logger.getLogger(PortFolioPagerTemplateDAOImpl.class.getName());
	//Saheli
	@Override
	public MasterDirectEquityMarketCap getMasterDirectEquityMarketCap(String ISIN, Session session)
			throws FinexaDaoException {
		try {
			MasterDirectEquityMarketCap masterDirectEquityMarketCap = null;
			masterDirectEquityMarketCap = (MasterDirectEquityMarketCap) session.createQuery(
					"SELECT mdemc FROM MasterDirectEquityMarketCap mdemc WHERE mdemc.masterDirectEquity.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();
			return masterDirectEquityMarketCap;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterEquityDailyPrice> getMasterEquityDailyPriceList(String ISIN, Session session)
			throws FinexaDaoException {
		try {
			List<MasterEquityDailyPrice> masterEquityDailyPriceList = null;
			masterEquityDailyPriceList = session.createQuery(
					"SELECT medp FROM MasterEquityDailyPrice medp WHERE medp.id.masterDirectEquity.isin=:ISIN ORDER BY medp.id.date DESC")
					.setString("ISIN", ISIN).list();
			return masterEquityDailyPriceList;
		}catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@Override
	public MasterMutualFundETF getMasterMutualFundETF(String isin, Session session) throws FinexaDaoException {
		try {
			MasterMutualFundETF masterMutualFundETF = null;
			masterMutualFundETF = (MasterMutualFundETF) session
					.createQuery("SELECT mmetf FROM MasterMutualFundETF mmetf WHERE mmetf.isin=:isin")
					.setString("isin", isin).uniqueResult();
			return masterMutualFundETF;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFAssetAllocation> getMasterMfAssetAllocationList(String isin, Session session)
			throws FinexaDaoException {
		try {
			List<MasterMFAssetAllocation> masterMfAssetAllocationList = null;
			

			Date maxDate = (Date) session.createQuery(
					"SELECT max(mmh.id.date) FROM MasterMFAssetAllocation mmh WHERE mmh.masterMutualFundEtf=:ISIN")
					.setString("ISIN", isin).uniqueResult();
			if(maxDate != null) {
				masterMfAssetAllocationList = session
						.createQuery("SELECT mma FROM MasterMFAssetAllocation mma WHERE mma.masterMutualFundEtf.isin=:isin and date=:maxDate")
						.setString("isin", isin).setDate("maxDate", maxDate).list();
			
			}
		
			if(masterMfAssetAllocationList == null) {
			// get primary_fd_code from accordSchemeDetails
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
						 maxDate = (Date) session.createQuery(
								"SELECT max(mmh.id.date) FROM MasterMFAssetAllocation mmh WHERE mmh.masterMutualFundEtf=:ISIN")
								.setString("ISIN", isinObj).uniqueResult();
						if(maxDate != null) {
							masterMfAssetAllocationList = session
									.createQuery("SELECT mma FROM MasterMFAssetAllocation mma WHERE mma.masterMutualFundEtf.isin=:isin and date=:maxDate")
									.setString("isin", isinObj).setDate("maxDate", maxDate).list();
							if (masterMfAssetAllocationList != null && masterMfAssetAllocationList.size() > 0) {
								break;
							}
						}
					}
				}
			}
		}	
			return masterMfAssetAllocationList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}
	// Saheli
	@SuppressWarnings("unchecked")
	@Override
	public Master52WeekHighLowMF getMaster52WeekHighLowMf(String isin, Session session) throws FinexaDaoException {
		Master52WeekHighLowMF master52WeekHighLowMf = null;
		try {
			
			master52WeekHighLowMf = (Master52WeekHighLowMF) session
					.createQuery(
							"SELECT m52w FROM Master52WeekHighLowMF m52w WHERE m52w.masterMutualFundEtf.isin=:isin")
					.setString("isin", isin).uniqueResult();
			
			if(master52WeekHighLowMf == null) {
				//Integer requiredSchemeCode = null;
				//requiredSchemeCode = (Integer)session
					//	.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
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
							master52WeekHighLowMf = (Master52WeekHighLowMF) session
								.createQuery(
										"SELECT m52w FROM Master52WeekHighLowMF m52w WHERE m52w.masterMutualFundEtf.isin=:isin")
								.setString("isin", isinObj).uniqueResult();
							if(master52WeekHighLowMf != null) {
								break;
							}
						}
					}
				}
			}


		} catch (Exception e) {
			// throw new FinexaDaoException(e.getMessage());
			e.printStackTrace();

		}
		return master52WeekHighLowMf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFMarketCap> getMasterMfMarketcapList(String AMFI, Session session) throws FinexaDaoException {
		try {
			List<MasterMFMarketCap> masterMfMarketcapList = null;
			masterMfMarketcapList = session.createQuery("SELECT mmc FROM MasterMfMarketCap mmc WHERE mmc.AMFI=:AMFI")
					.setString("AMFI", AMFI).list();
			return masterMfMarketcapList;
			
			
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFDailyNAV> getMasterMfDailyNavList(String AMFI, Session session) throws FinexaDaoException {
		try {
			List<MasterMFDailyNAV> masterMfDailyNavList = null;
			masterMfDailyNavList = session
					.createQuery("SELECT mmdn FROM MasterMfDailyNav mmdn WHERE medp.amfiCode=:AMFI")
					.setString("AMFI", AMFI).list();
			return masterMfDailyNavList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterRiskFreeRate> getMasterRiskFreeRateList(Session session) throws FinexaDaoException {
		try {
			List<MasterRiskFreeRate> masterRiskFreeRateList = null;
			masterRiskFreeRateList = session.createQuery("SELECT mrfr FROM MasterRiskFreeRate mrfr ").list();
			return masterRiskFreeRateList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFHolding> getMasterMfHoldingList(String ISIN, Session session) throws FinexaDaoException {
		
			List<MasterMFHolding> mastermfholdingList=null;
			try {
				/*List<MasterMFHolding> mastermfholdingList=null;*/
		
				Date maxDate = (Date) session.createQuery(
						"SELECT max(mmh.id.assetDate) FROM MasterMFHolding mmh WHERE mmh.id.masterMutualFundEtf=:ISIN")
						.setString("ISIN", ISIN).uniqueResult();
				if(maxDate != null) {
					mastermfholdingList = session
							.createQuery("SELECT mmh FROM MasterMFHolding mmh WHERE mmh.id.masterMutualFundEtf=:ISIN and  mmh.id.assetDate=:date ORDER BY mmh.assetPercentage desc")
							.setString("ISIN", ISIN).setDate("date", maxDate).list();
					
				}
				//// get primary_fd_code from accordSchemeDetails
			     if(mastermfholdingList == null) {
				//Integer requiredSchemeCode = null;
				//requiredSchemeCode = (Integer)session
				//		.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
				//		.setString("isin", ISIN).uniqueResult();
			    List<Integer> requiredSchemeCodeList = null;
					requiredSchemeCodeList = (List<Integer>)session
					.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
					.setString("isin", ISIN).list();

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
									"SELECT max(mmh.id.assetDate) FROM MasterMFHolding mmh WHERE mmh.id.masterMutualFundEtf=:ISIN")
									.setString("ISIN", isinObj).uniqueResult();
							if(maxDate != null) {
								mastermfholdingList = session
										.createQuery("SELECT mmh FROM MasterMFHolding mmh WHERE mmh.id.masterMutualFundEtf=:ISIN and  mmh.id.assetDate=:date ORDER BY mmh.assetPercentage desc")
										.setString("ISIN", isinObj).setDate("date", maxDate).list();
								if (mastermfholdingList != null && mastermfholdingList.size() > 0) {
									break;
								}
							}
						}
					}
				}
			  }
				/*masterMfHoldingList = session.createQuery(
						"SELECT mmh FROM MasterMFHolding mmh WHERE mmh.id.masterMutualFundEtf=:ISIN ORDER BY mmh.id.assetDate desc")
						.setString("ISIN", ISIN).list();*/
				
				
				//max Date
				
				/*Date maxDate = (Date) session.createQuery(
						"SELECT max(mmh.id.assetDate) FROM MasterMFHolding mmh WHERE mmh.id.masterMutualFundEtf=:ISIN")
						.setString("ISIN", ISIN).uniqueResult();
				
				 mastermfholdingList = (List<MasterMFHolding>) session.createQuery(
						"SELECT mmh FROM MasterMFHolding mmh WHERE mmh.id.masterMutualFundEtf=:ISIN and  mmh.id.assetDate=:date ORDER BY mmh.assetPercentage desc")
						.setString("ISIN", ISIN).setDate("date", maxDate).list();
	*/
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaDaoException(e.getMessage());

		}
		return mastermfholdingList;
	
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterCompany> getMasterCompany(int companyCode, Session session) throws FinexaDaoException {
		List<MasterCompany> masterCompanyList = null;
		try {

			masterCompanyList = session.createQuery("SELECT mc FROM MasterCompany mc WHERE mc.finCode=:companyCode")
					.setInteger("companyCode", companyCode).list();

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
		return masterCompanyList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFHoldingMaturity> getMasterMfHoldingMaturityList(String AMFI, Session session)
			throws FinexaDaoException {
		try {
			List<MasterMFHoldingMaturity> masterMfHoldingMaturityList = null;
			masterMfHoldingMaturityList = session
					.createQuery("SELECT mmhm FROM MasterMfHoldingMaturity mmhm WHERE mmhm.amfiCode=:AMFI")
					.setString("AMFI", AMFI).list();
			return masterMfHoldingMaturityList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFHoldingMaturity> getMasterMfHoldingMaturityListByIsin(String isin, Session session)
			throws FinexaDaoException {
		try {
			/*List<MasterMFHoldingMaturity> masterMfHoldingMaturityList = null;
			
			Date maxDate = (Date) session.createQuery(
					"SELECT max(holdingMaturity.id.asOfDate) FROM MasterMFHoldingMaturity holdingMaturity WHERE holdingMaturity.id.masterMutualFundEtf.isin=:ISIN")
					.setString("ISIN", isin).uniqueResult();

			
			masterMfHoldingMaturityList = session
					.createQuery("SELECT mmhm FROM MasterMFHoldingMaturity mmhm WHERE mmhm.id.masterMutualFundEtf.isin=:isin and mmhm.id.asOfDate = :maxDate")
					.setString("isin", isin).setDate("maxDate", maxDate).list();
			return masterMfHoldingMaturityList;
			*/
			
			List<MasterMFHoldingMaturity> masterMfHoldingMaturityList = null;
			// get primary_fd_code from accordSchemeDetails
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
						Date maxDate = (Date) session.createQuery(
								"SELECT max(holdingMaturity.id.asOfDate) FROM MasterMFHoldingMaturity holdingMaturity WHERE holdingMaturity.id.masterMutualFundEtf.isin=:ISIN")
								.setString("ISIN", isinObj).uniqueResult();
						if(maxDate != null) {
							masterMfHoldingMaturityList = session
									.createQuery("SELECT mmhm FROM MasterMFHoldingMaturity mmhm WHERE mmhm.id.masterMutualFundEtf.isin=:isin and mmhm.id.asOfDate = :maxDate")
									.setString("isin", isinObj).setDate("maxDate", maxDate).list();
							if (masterMfHoldingMaturityList != null && masterMfHoldingMaturityList.size() > 0) {
								break;
							}
						}
					}
				}
			}
			return masterMfHoldingMaturityList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterIndexDailyNAV> getMasterindexdailynavListOnDate(String Name, Date startDate, Date endDate,
			Session session) throws FinexaDaoException {
		List<MasterIndexDailyNAV> masterindexdailynavList = null;
		try {
			// System.out.println("Name "+Name);
			if (startDate.compareTo(endDate) > 0) {
				// System.out.println("startDate.compareTo(endDate)>0");
				masterindexdailynavList = session.createQuery(
						"SELECT midn FROM MasterIndexDailyNAV midn WHERE midn.id.date<=:startDate and midn.id.date>=:endDate and midn.id.masterIndexName.name =:NAME ORDER BY midn.id.date DESC")
						.setDate("startDate", startDate).setDate("endDate", endDate).setString("NAME", Name).list();
			} else {
				// System.out.println("startDate.compareTo(endDate)>0");
				masterindexdailynavList = session.createQuery(
						"SELECT midn FROM MasterIndexDailyNAV midn WHERE midn.id.date<=:endDate and midn.id.date>=:startDate and midn.id.masterIndexName.name =:NAME ORDER BY midn.id.date DESC")
						.setDate("startDate", startDate).setDate("endDate", endDate).setString("NAME", Name).list();
			}

			// System.out.println("masterindexdailynavList
			// "+masterindexdailynavList.size());
			// genericDao.loadAll(Masterindexdailynav.class);
			// session.clear();
			return masterindexdailynavList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterEquityDailyPrice> getMasterEquityDailyPriceListOnDate(String ISIN, Date startDate, Date endDate,
			Session session) throws FinexaDaoException {
		try {
			List<MasterEquityDailyPrice> masterEquityDailyPriceList = null;

			if (startDate.compareTo(endDate) > 0) {
				masterEquityDailyPriceList = session.createQuery(
						"SELECT medp FROM MasterEquityDailyPrice medp WHERE medp.id.date<=:startDate and medp.id.date>=:endDate and medp.id.masterDirectEquity.isin=:ISIN ORDER BY medp.id.date asc")
						.setDate("startDate", startDate).setDate("endDate", endDate).setString("ISIN", ISIN).list();
			} else {
				masterEquityDailyPriceList = session.createQuery(
						"SELECT medp FROM MasterEquityDailyPrice medp WHERE medp.id.date<=:endDate and medp.id.date>=:startDate and medp.id.masterDirectEquity.isin=:ISIN ORDER BY medp.id.date asc")
						.setDate("startDate", startDate).setDate("endDate", endDate).setString("ISIN", ISIN).list();
			}
			// System.out.println("masterEquityDailyPriceList
			// "+masterEquityDailyPriceList.size());

			return masterEquityDailyPriceList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFDailyNAV> getMasterMFDailyNAVListOnDate(String ISIN, Date startDate, Date endDate,
			Session session) throws FinexaDaoException {
		try {
			List<MasterMFDailyNAV> masterMFDailyNAVList = null;
			if (startDate.compareTo(endDate) > 0) {
				// System.out.println("startDate.compareTo(endDate)>0");
				masterMFDailyNAVList = session.createQuery(
						"SELECT mmdn FROM MasterMFDailyNAV mmdn WHERE mmdn.id.date<=:startDate and mmdn.id.date>=:endDate and mmdn.id.masterMutualFundEtf.isin=:ISIN ORDER BY mmdn.id.date asc")
						.setDate("startDate", startDate).setDate("endDate", endDate).setString("ISIN", ISIN).list();
			} else {
				// System.out.println("startDate.compareTo(endDate)<0");
				masterMFDailyNAVList = session.createQuery(
						"SELECT mmdn FROM MasterMFDailyNAV mmdn WHERE mmdn.id.date<=:endDate and mmdn.id.date>=:startDate and mmdn.id.masterMutualFundEtf.isin=:ISIN ORDER BY mmdn.id.date asc")
						.setDate("startDate", startDate).setDate("endDate", endDate).setString("ISIN", ISIN).list();
			}
			// System.out.println("masterMFDailyNAVList "+masterMFDailyNAVList.size());

			return masterMFDailyNAVList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterIndexDailyNAV> getMasterIndexDailyNAVListTimePeriod(Date currentDate, Date toDate, String name,
			Session session) throws FinexaDaoException {
		List<MasterIndexDailyNAV> masterIndexDailyNAVList = null;
		try {
			// System.out.println("currentDate in time nifty time period "+currentDate);
			// System.out.println("todate in time nifty time period "+toDate);

			// System.out.println("name "+name);
			masterIndexDailyNAVList = session.createQuery(
					"SELECT midn FROM MasterIndexDailyNAV midn WHERE midn.id.date between :toDate and :currentDate and midn.id.masterIndexName.name =:NAME ORDER BY midn.id.date DESC")
					.setDate("currentDate", currentDate).setDate("toDate", toDate).setString("NAME", name).list();

			// System.out.println("masterIndexDailyNAVList
			// "+masterIndexDailyNAVList.size());

		} catch (Exception e) {
			// throw new FinexaDaoException(e.getMessage());
			e.printStackTrace();

		}
		return masterIndexDailyNAVList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MasterIndexDailyNAV> getMasterIndexDailyNAVClosingPrice(Date currentDate, Date toDate, String name,
			Session session) throws FinexaDaoException {
		List<MasterIndexDailyNAV> masterIndexDailyNAVList = null;
		try {
			masterIndexDailyNAVList = session.createQuery(
					"SELECT midn FROM MasterIndexDailyNAV midn WHERE midn.id.date in (:currentDate , :toDate) and midn.id.masterIndexName.name =:NAME ORDER BY midn.id.date DESC")
					.setDate("currentDate", currentDate).setDate("toDate", toDate).setString("NAME", name).list();

		} catch (Exception e) {
			// throw new FinexaDaoException(e.getMessage());
			e.printStackTrace();

		}
		return masterIndexDailyNAVList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterEquityDailyPrice> getMasterEquityDailyPriceListTimePeriod(Date currentDate, Date toDate,
			String ISIN, Session session) throws FinexaDaoException {
		try {
			List<MasterEquityDailyPrice> masterEquityDailyPriceList = null;
			masterEquityDailyPriceList = session.createQuery(
					"SELECT medp FROM MasterEquityDailyPrice medp WHERE medp.id.date between :toDate and :currentDate and medp.id.masterDirectEquity.isin=:ISIN ORDER BY medp.id.date DESC")
					.setParameter("currentDate", currentDate).setParameter("toDate", toDate).setString("ISIN", ISIN).list();

			// System.out.println("masterEquityDailyPriceList
			// "+masterEquityDailyPriceList.size());

			return masterEquityDailyPriceList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MasterEquityDailyPrice> getMasterEquityDailyClosingPrice(Date currentDate, Date toDate,
			String ISIN, Session session) throws FinexaDaoException {
		try {
			List<MasterEquityDailyPrice> masterEquityDailyPriceList = null;
			masterEquityDailyPriceList = session.createQuery(
					"SELECT medp FROM MasterEquityDailyPrice medp WHERE medp.id.date in (:currentDate , :toDate) and medp.id.masterDirectEquity.isin=:ISIN ORDER BY medp.id.date DESC")
					.setParameter("currentDate", currentDate).setParameter("toDate", toDate).setString("ISIN", ISIN).list();

			// System.out.println("masterEquityDailyPriceList
			// "+masterEquityDailyPriceList.size());

			return masterEquityDailyPriceList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFDailyNAV> getMasterMFListTimePeriod(Date currentDate, Date toDate, String ISIN, Session session)
			throws FinexaDaoException {
		try {
			List<MasterMFDailyNAV> masterMFDailyNAVList = null;
			masterMFDailyNAVList = session.createQuery(
					"SELECT mmdn FROM MasterMFDailyNAV mmdn WHERE mmdn.id.date between :toDate and :currentDate and mmdn.id.masterMutualFundEtf.isin=:ISIN ORDER BY mmdn.id.date DESC")
					.setDate("currentDate", currentDate).setDate("toDate", toDate).setString("ISIN", ISIN).list();

			// System.out.println("masterMFDailyNAVList "+masterMFDailyNAVList.size());

			return masterMFDailyNAVList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public MasterMFMarketCap getMasterMfMarketcap(String ISIN, Session session) throws FinexaDaoException {
		MasterMFMarketCap portFolioOverviewMasterMFMarketCap = null;
		try {
		
			Date maxDate = (Date) session.createQuery(
					"SELECT max(marketCap.id.date) FROM MasterMFMarketCap marketCap WHERE marketCap.id.masterMutualFundEtf.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();
			if(maxDate != null) {
				portFolioOverviewMasterMFMarketCap = (MasterMFMarketCap) session
						.createQuery("SELECT marketCap FROM MasterMFMarketCap marketCap WHERE marketCap.id.masterMutualFundEtf.isin=:ISIN and marketCap.id.date=:maxDate")
						.setString("ISIN", ISIN).setDate("maxDate",maxDate).uniqueResult();
			}
				
			if(portFolioOverviewMasterMFMarketCap == null)	{
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
								"SELECT max(marketCap.id.date) FROM MasterMFMarketCap marketCap WHERE marketCap.id.masterMutualFundEtf.isin=:ISIN")
								.setString("ISIN", isinObj).uniqueResult();
						if(maxDate != null) {
							portFolioOverviewMasterMFMarketCap = (MasterMFMarketCap) session
									.createQuery("SELECT marketCap FROM MasterMFMarketCap marketCap WHERE marketCap.id.masterMutualFundEtf.isin=:ISIN and marketCap.id.date=:maxDate")
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
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Master52WeekHighLowMF getMaster52WeekHighLowMF(String ISIN, Session session) throws FinexaDaoException {
		Master52WeekHighLowMF master52WeekHighLowMf = null;
		try {
			//Integer requiredSchemeCode = null;
			//requiredSchemeCode = (Integer)session
			//		.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
			//		.setString("isin", ISIN).uniqueResult();
			List<Integer> requiredSchemeCodeList = null;
			requiredSchemeCodeList = (List<Integer>)session
					.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
					.setString("isin", ISIN).list();
		
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
						master52WeekHighLowMf = (Master52WeekHighLowMF) session
							.createQuery(
									"SELECT m52w FROM Master52WeekHighLowMF m52w WHERE m52w.masterMutualFundEtf.isin=:isin")
							.setString("isin", isinObj).uniqueResult();
						if(master52WeekHighLowMf != null) {
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			// throw new FinexaDaoException(e.getMessage());
			e.printStackTrace();

		}
		return master52WeekHighLowMf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MasterIndexName getMasterIndexName(String ISIN, Session session) throws FinexaDaoException {
		MasterIndexName masterIndexName = null;
		try {

			masterIndexName = (MasterIndexName) session.createQuery(
					"SELECT mind FROM MasterMutualFundETF mmfe left join mmfe.masterIndexName mind WHERE mmfe.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();

		} catch (Exception e) {
			// throw new FinexaDaoException(e.getMessage());
			e.printStackTrace();

		}
		return masterIndexName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MasterFundManager getMasterFundManager(String ISIN, Session session) throws FinexaDaoException {
		MasterFundManager masterFundManager = null;
		try {

			masterFundManager = (MasterFundManager) session.createQuery(
					"SELECT mfm FROM MasterMutualFundETF mmfe left join mmfe.masterFundManager mfm WHERE mmfe.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();

		} catch (Exception e) {
			// throw new FinexaDaoException(e.getMessage());
			e.printStackTrace();

		}
		return masterFundManager;
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public MasterMFAssetAllocation getMasterMFAssetAllocation(String
	 * ISIN, Session session) throws FinexaDaoException { MasterMFAssetAllocation
	 * masterMFAssetAllocation = null; try {
	 * 
	 * masterMFAssetAllocation = (MasterMFAssetAllocation) session.
	 * createQuery("SELECT mma FROM MasterMFAssetAllocation mma WHERE mma.isin=:ISIN"
	 * ) .setString("ISIN", ISIN).uniqueResult();
	 * 
	 * } catch (Exception e) { // throw new FinexaDaoException(e.getMessage());
	 * e.printStackTrace();
	 * 
	 * } return masterMFAssetAllocation; }
	 */

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public List<Object[]> getMFSectorHolding(String ISIN, Session session) throws FinexaDaoException {//List<Object[]> obList = new ArrayList<>();
		List<Object[]> obList = null;
		Date maxDate;
		try {

			 
			 maxDate = (Date) session.createQuery(
					"SELECT max(mmhs.id.date) FROM MasterMFHoldingSectorWise mmhs WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();
			if(maxDate != null) {
				obList =(List<Object[]>) session
						.createQuery("SELECT mmhs.holding, mms.sector,mmhs.id.masterMutualFundEtf.isin,mmhs.id.date FROM MasterMFHoldingSectorWise mmhs left join mmhs.id.masterMfsector mms WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN  AND mmhs.id.date=:date ORDER BY mmhs.holding desc")
						.setString("ISIN", ISIN).setDate("date", maxDate).list();
				
				
			}
			
			//System.out.println("obListuyuyuy "+obList);
		    if(obList  == null) {
			
			// get primary_fd_code from accordSchemeDetails
			//Integer requiredSchemeCode = null;
			//requiredSchemeCode = (Integer)session
			//		.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:ISIN")
			//		.setString("ISIN", ISIN).uniqueResult();
		
		    List<Integer> requiredSchemeCodeList = null;
			requiredSchemeCodeList = (List<Integer>)session
						.createQuery("SELECT schemeIsinMaster.id.schemeCode FROM AccordSchemeIsinMaster schemeIsinMaster WHERE schemeIsinMaster.id.isin=:isin")
						.setString("isin", ISIN).list();
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
								"SELECT max(mmhs.id.date) FROM MasterMFHoldingSectorWise mmhs WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN")
								.setString("ISIN", isinObj).uniqueResult();
						if(maxDate != null) {
							obList =(List<Object[]>) session
									.createQuery("SELECT mmhs.holding, mms.sector,mmhs.id.masterMutualFundEtf.isin,mmhs.id.date FROM MasterMFHoldingSectorWise mmhs left join mmhs.id.masterMfsector mms WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN  AND mmhs.id.date=:date ORDER BY mmhs.holding desc")
									.setString("ISIN", isinObj).setDate("date", maxDate).list();
							
							if (obList != null && obList.size() > 0) {
								break;
							}
						}
					}
				}
			}
		    }
			return obList;
			/*
			 * obList = (List<Object[]>) session.
			 * createQuery("SELECT mmhs.holding, mms.sector,mmhs.id.masterMutualFundEtf.isin,mmhs.id.date FROM MasterMFHoldingSectorWise mmhs left join mmhs.id.masterMfsector mms WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN and mmhs.id.date<=:curDate and mmhs.id.date>=:toDate ORDER BY mmhs.holding asc,mmhs.id.date desc"
			 * ) .setString("ISIN", ISIN).setDate("curDate", curDate).setDate("toDate",
			 * toDate).setMaxResults(10).list();
			 */
			/*
			 * List<MasterMFHoldingSectorWise> masterMFHoldingSectorWise =
			 * (List<MasterMFHoldingSectorWise>) session.
			 * createQuery("SELECT mmhs FROM MasterMFHoldingSectorWise mmhs WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN ORDER BY mmhs.id.date desc"
			 * ) .setString("ISIN", ISIN).list();
			 */

			// System.out.println("max date
			// "+masterMFHoldingSectorWise.get(0).getId().getDate());
			/*
			 * obList = (List<Object[]>) session.
			 * createQuery("SELECT mmhs.holding, mms.sector,mmhs.id.masterMutualFundEtf.isin,mmhs.id.date FROM MasterMFHoldingSectorWise mmhs left join mmhs.id.masterMfsector mms WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN  AND mmhs.id.date=:date ORDER BY mmhs.holding asc"
			 * ) .setString("ISIN",
			 * ISIN).setDate("date",masterMFHoldingSectorWise.get(0).getId().getDate()).list
			 * ();
			 */
			
			//max Date
			/*Date maxDate = (Date) session.createQuery(
					"SELECT max(mmhs.id.date) FROM MasterMFHoldingSectorWise mmhs WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();

			
			obList = (List<Object[]>) session.createQuery(
					"SELECT mmhs.holding, mms.sector,mmhs.id.masterMutualFundEtf.isin,mmhs.id.date FROM MasterMFHoldingSectorWise mmhs left join mmhs.id.masterMfsector mms WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN  AND mmhs.id.date=:date ORDER BY mmhs.holding desc").setMaxResults(10)
					.setString("ISIN", ISIN).setDate("date", maxDate).list();
*/
		

		} catch (Exception e) {
			// throw new FinexaDaoException(e.getMessage());
			e.printStackTrace();

		}

		return obList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getMFSectorHolding(String ISIN, Date curDate, Date toDate, Session session)
			throws FinexaDaoException {
		List<Object[]> obList = new ArrayList<>();
		try {

			obList = (List<Object[]>) session.createQuery(
					"SELECT mmhs.holding, mms.sector,mmhs.id.masterMutualFundEtf.isin,mmhs.id.date FROM MasterMFHoldingSectorWise mmhs left join mmhs.id.masterMfsector mms WHERE mmhs.id.masterMutualFundEtf.isin=:ISIN and mmhs.id.date<=:curDate and mmhs.id.date>=:toDate ORDER BY mmhs.holding asc,mmhs.id.date desc")
					.setString("ISIN", ISIN).setDate("curDate", curDate).setDate("toDate", toDate).setMaxResults(10)
					.list();

		} catch (Exception e) {
			// throw new FinexaDaoException(e.getMessage());
			e.printStackTrace();

		}

		return obList;
	}

	@Override
	public ClientMutualFund getClientMutualFund(int id, Session session) throws FinexaDaoException {
		ClientMutualFund clientMutualFund = null;
		try {
			clientMutualFund = (ClientMutualFund) session
					.createCriteria(ClientMutualFund.class).add(Restrictions.eq("id", (int) id))
					.uniqueResult();
			// session.clear();
			return clientMutualFund;
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Date getRatingWiseMaxDate(String isin, Session session) throws FinexaDaoException {
		// TODO Auto-generated method stub
		Date maxDate = null;
		try {
			
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
						maxDate = (Date)session.createQuery("SELECT max(date) from MasterMFHoldingRatingWise "
								+ "mmfhr where mmfhr.id.masterMutualFundEtf.isin=:isin").setString("isin", isinObj).uniqueResult();
						if(maxDate != null) {
							break;
						}
					}		
				}
			}
			
		} catch (Exception e) {
			throw new FinexaDaoException(e);
		}
		return maxDate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFHoldingRatingWiseDTO> getMasterMFHoldingRatingWiseList(String isin, Date maxDate, Session session)
			throws FinexaDaoException {
		List<MasterMFHoldingRatingWiseDTO> masterMFHoldingRatingWiseDTOList = new ArrayList();
		List<MasterMFHoldingRatingWise> masterMfHoldingRatingWiseList = null;
		try {
			
			// get primary_fd_code from accordSchemeDetails
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
						
						masterMfHoldingRatingWiseList = session
								.createQuery("SELECT mmfs FROM MasterMFHoldingRatingWise mmfs where mmfs.id.masterMutualFundEtf.isin=:isin and mmfs.id.date=:maxDate")
								.setString("isin", isinObj).setDate("maxDate", maxDate).list();
						if (masterMfHoldingRatingWiseList != null) {
							for (MasterMFHoldingRatingWise obj : masterMfHoldingRatingWiseList) {
								MasterMFHoldingRatingWiseDTO objDTO = new MasterMFHoldingRatingWiseDTO();
								objDTO.setRatingName(obj.getId().getMasterMfrating().getRating());
								objDTO.setHolding(obj.getHolding());
								masterMFHoldingRatingWiseDTOList.add(objDTO);
							}
						}
						   
						if (masterMfHoldingRatingWiseList != null && masterMfHoldingRatingWiseList.size() > 0) {
							break;
						}
					}
				}
			}
			return masterMFHoldingRatingWiseDTOList;
			
			/*masterMfHoldingRatingWiseList = (List<MasterMFHoldingRatingWise>)session.createQuery("SELECT mmfs FROM MasterMFHoldingRatingWise mmfs where mmfs.id.masterMutualFundEtf.isin=:isin and mmfs.id.date=:maxDate").setString("isin", isin).setDate("maxDate", maxDate)
					.list();
			if (masterMfHoldingRatingWiseList != null) {
				for (MasterMFHoldingRatingWise obj : masterMfHoldingRatingWiseList) {
					MasterMFHoldingRatingWiseDTO objDTO = new MasterMFHoldingRatingWiseDTO();
					objDTO.setRatingName(obj.getId().getMasterMfrating().getRating());
					objDTO.setHolding(obj.getHolding());
					masterMFHoldingRatingWiseDTOList.add(objDTO);
				}
			}
			return masterMFHoldingRatingWiseDTOList;*/
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

	@Override
	public MasterDirectEquity getMasterDirectEquity(String ISIN, Session session) throws FinexaDaoException {
		// TODO Auto-generated method stub
		try {
			MasterDirectEquity masterDirectEquity = null;
			masterDirectEquity = (MasterDirectEquity) session.createQuery(
					"SELECT mdemc FROM MasterDirectEquity mdemc WHERE mdemc.isin=:ISIN")
					.setString("ISIN", ISIN).uniqueResult();
			return masterDirectEquity;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasterMFDailyNAV> getMasterMFListClosingPrice(Date currentDate, Date toDate, String ISIN,
			Session session) throws FinexaDaoException {
		try {
			List<MasterMFDailyNAV> masterMFDailyNAVList = null;
			masterMFDailyNAVList = (List<MasterMFDailyNAV>)session.createQuery(
					"SELECT mmdn FROM MasterMFDailyNAV mmdn WHERE mmdn.id.date in(:startDate, :endDate) and mmdn.id.masterMutualFundEtf.isin=:ISIN ORDER BY mmdn.id.date DESC")
					.setDate("startDate", toDate).setDate("endDate", currentDate).setString("ISIN", ISIN).list();
	
			// System.out.println("masterMFDailyNAVList "+masterMFDailyNAVList.size());

			return masterMFDailyNAVList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasterEquityDailyPrice>
	 * getMasterEquityDailyPriceOnDate(String ISIN, Date startDate, Date pastDate,
	 * Session session) throws FinexaDaoException { try {
	 * List<MasterEquityDailyPrice> masterEquityDailyPriceList = null;
	 * 
	 * if( startDate.compareTo(pastDate)>0) { masterEquityDailyPriceList =
	 * session.createQuery(
	 * "SELECT medp FROM MasterEquityDailyPrice medp WHERE medp.id.date<=:startDate and medp.id.date>=:pastDate and medp.id.masterDirectEquity.isin=:ISIN ORDER BY medp.id.date DESC"
	 * ) .setDate("startDate", startDate).setDate("pastDate",
	 * pastDate).setString("ISIN", ISIN).list(); }
	 * System.out.println("masterEquityDailyPriceList "+masterEquityDailyPriceList);
	 * 
	 * return masterEquityDailyPriceList;
	 * 
	 * } catch (Exception e) { throw new FinexaDaoException(e.getMessage());
	 * 
	 * }
	 * 
	 * }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<MasterMFDailyNAV> getMasterMFDailyNAVOnDate(String
	 * ISIN, Date startDate, Date pastDate, Session session) throws
	 * FinexaDaoException { try { List<MasterMFDailyNAV> masterMFDailyNAVList =
	 * null; if( startDate.compareTo(pastDate)>0) {
	 * System.out.println("startDate.compareTo(endDate)>0"); masterMFDailyNAVList =
	 * session.createQuery(
	 * "SELECT mmdn FROM MasterMFDailyNAV mmdn WHERE mmdn.id.date<=:startDate and mmdn.id.date>=:pastDate and mmdn.id.masterDirectEquity.isin=:ISIN ORDER BY mmdn.id.date DESC"
	 * ) .setDate("startDate", startDate).setDate("endDate",
	 * pastDate).setString("ISIN", ISIN).list(); }
	 * System.out.println("masterMFDailyNAVList "+masterMFDailyNAVList.size());
	 * 
	 * return masterMFDailyNAVList;
	 * 
	 * } catch (Exception e) { throw new FinexaDaoException(e.getMessage());
	 * 
	 * }
	 * 
	 * }
	 */

}