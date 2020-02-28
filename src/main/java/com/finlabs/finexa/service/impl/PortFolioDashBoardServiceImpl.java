package com.finlabs.finexa.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.AdvisorUser;
import com.finlabs.finexa.model.AdvisorUserSupervisorMapping;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.PortfolioNetWorthDto;
import com.finlabs.finexa.model.UserClientRedis;
import com.finlabs.finexa.pm.util.FinanceUtil;
import com.finlabs.finexa.pm.util.FinexaConstant;
import com.finlabs.finexa.repository.AdvisorUserDAO;
import com.finlabs.finexa.repository.impl.AdvisorUserDaoImpl;
import com.finlabs.finexa.service.PortFolioDashBoardService;
import com.finlabs.finexa.service.PortFolioNetWorthService;
import com.finlabs.finexa.service.PortFolioOverviewService;

//@Transactional
//@Service
public class PortFolioDashBoardServiceImpl implements PortFolioDashBoardService {
	// private static Logger log =
	 //LoggerFactory.getLogger(PortFolioDashBoardServiceImpl.class);

	//@Autowired
	private AdvisorUserDAO advisorUserDAO;

	// @Autowired
	private PortFolioOverviewService portFolioOverviewService;

	// @Autowired
	private PortFolioNetWorthService portFolioNetWorthService;
	
	/*
	 * @Override public List<PortfolioOverviewDto>
	 * getclientPortfolioOverviewTimeperiodAndAsset(int clientId, String
	 * specificRequermentStat, int timePeriod, String assetValue) throws
	 * FinexaBussinessException, ParseException { List<PortfolioOverviewDto>
	 * PortfolioOverviewDtoList = getclientPortfolioOverview(clientId,
	 * specificRequermentStat);
	 * 
	 * List<PortfolioOverviewDto> PortfolioOverviewDtoAssetList = new
	 * ArrayList<PortfolioOverviewDto>();
	 * 
	 * List<PortfolioOverviewDto> PortfolioOverviewDtoAssetListReturn = new
	 * ArrayList<PortfolioOverviewDto>();
	 * 
	 * for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoList) {
	 * if ((portfolioOverviewDto.getProductName()).equals(assetValue)) {
	 * 
	 * PortfolioOverviewDtoAssetList.add(portfolioOverviewDto); } }
	 * 
	 * Calendar c = null;
	 * 
	 * if (timePeriod == 1) { c =
	 * com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("DAYS", 7);
	 * 
	 * } if (timePeriod == 2) { c =
	 * com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 1);
	 * 
	 * } if (timePeriod == 3) { c =
	 * com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 3);
	 * 
	 * } SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); Date maxDate1 =
	 * sdf.parse(sdf.format(c.getTime())); String maxDate2 = sdf.format(maxDate1);
	 * 
	 * Date currentDate1 = new Date(); String currentDate2 =
	 * sdf.format(currentDate1);
	 * 
	 * DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
	 * 
	 * LocalDate maxDate = formatter.parseLocalDate(maxDate2); LocalDate currentDate
	 * = formatter.parseLocalDate(currentDate2);
	 * 
	 * for (PortfolioOverviewDto portfolioOverviewDto :
	 * PortfolioOverviewDtoAssetList) {
	 * 
	 * System.out.println("inside " + portfolioOverviewDto.getProductName()); } for
	 * (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoAssetList) {
	 * if (!portfolioOverviewDto.getMaturityDate().equals("N/A")) { Date
	 * maturityDate1 = sdf.parse(portfolioOverviewDto.getMaturityDate()); String
	 * maturityDate2 = sdf.format(maturityDate1); LocalDate maturityDate =
	 * formatter.parseLocalDate(maturityDate2);
	 * 
	 * 
	 * System.out.println("inside it "+portfolioOverviewDto. getProductName());
	 * System.out.println("currentDate "+currentDate);
	 * System.out.println("maxDate "+maxDate);
	 * System.out.println("maturityDate"+maturityDate);
	 * 
	 * if (maturityDate.compareTo(currentDate) >= 0 &&
	 * maturityDate.compareTo(maxDate) <= 0) { System.out.println("between ");
	 * PortfolioOverviewDtoAssetListReturn.add(portfolioOverviewDto); } } }
	 * 
	 * System.out.println("size "+PortfolioOverviewDtoAssetListReturn.size() );
	 * 
	 * return PortfolioOverviewDtoAssetListReturn; }
	 */

	/*
	 * @Override public List<PortfolioOverviewDto>
	 * getclientPortfolioOverviewTimeperiod(int clientId, String
	 * specificRequermentStat, int timePeriod) throws FinexaBussinessException,
	 * ParseException { List<PortfolioOverviewDto> PortfolioOverviewDtoList =
	 * getclientPortfolioOverview(clientId, specificRequermentStat);
	 * 
	 * List<PortfolioOverviewDto> PortfolioOverviewDtoAssetListReturn = new
	 * ArrayList<PortfolioOverviewDto>();
	 * 
	 * Calendar c = null;
	 * 
	 * if (timePeriod == 1) { c =
	 * com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("DAYS", 7);
	 * 
	 * } if (timePeriod == 2) { c =
	 * com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 1);
	 * 
	 * } if (timePeriod == 3) { c =
	 * com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 3);
	 * 
	 * } SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); Date maxDate1 =
	 * sdf.parse(sdf.format(c.getTime())); String maxDate2 = sdf.format(maxDate1);
	 * 
	 * Date currentDate1 = new Date(); String currentDate2 =
	 * sdf.format(currentDate1);
	 * 
	 * DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
	 * 
	 * LocalDate maxDate = formatter.parseLocalDate(maxDate2); LocalDate currentDate
	 * = formatter.parseLocalDate(currentDate2);
	 * 
	 * for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoList) {
	 * 
	 * 
	 * System.out.println("inside "+portfolioOverviewDto.getProductName( ));
	 * 
	 * } for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoList)
	 * { if (!portfolioOverviewDto.getMaturityDate().equals("N/A")) {
	 * 
	 * Date maturityDate1 = sdf.parse(portfolioOverviewDto.getMaturityDate());
	 * String maturityDate2 = sdf.format(maturityDate1); LocalDate maturityDate =
	 * formatter.parseLocalDate(maturityDate2);
	 * 
	 * 
	 * System.out.println("inside it "+portfolioOverviewDto. getProductName());
	 * System.out.println("currentDate "+currentDate);
	 * System.out.println("maxDate "+maxDate);
	 * System.out.println("maturityDate"+maturityDate);
	 * 
	 * if (maturityDate.compareTo(currentDate) >= 0 &&
	 * maturityDate.compareTo(maxDate) <= 0) { System.out.println("between ");
	 * PortfolioOverviewDtoAssetListReturn.add(portfolioOverviewDto); } } }
	 * 
	 * System.out.println("size "+PortfolioOverviewDtoAssetListReturn.size() );
	 * 
	 * return PortfolioOverviewDtoAssetListReturn; }
	 */

	@Override
	public List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiodAndAsset(String token, int clientId,
			String specificRequermentStat, int timePeriod, String assetValue, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException, ParseException {
		portFolioOverviewService = new PortFolioOverviewServiceImpl();
	
		List<PortfolioOverviewDto> PortfolioOverviewDtoList = portFolioOverviewService.getclientPortfolioOverview(token, clientId, specificRequermentStat, session, cacheInfoService);

		List<PortfolioOverviewDto> PortfolioOverviewDtoAssetList = new ArrayList<PortfolioOverviewDto>();

		List<PortfolioOverviewDto> PortfolioOverviewDtoAssetListReturn = new ArrayList<PortfolioOverviewDto>();

	   try {
		   System.out.println("assetValue: " + assetValue);
		   for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoList) {
	   
			   if ((portfolioOverviewDto.getProductName()).equals(assetValue)) {

					PortfolioOverviewDtoAssetList.add(portfolioOverviewDto);
				}
			
		}

		Calendar c = null;

		if (timePeriod == 1) {
			c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("DAYS", 7);

		}
		if (timePeriod == 2) {
			c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 1);

		}
		if (timePeriod == 3) {
			c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 3);

		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Date maxDate1 = sdf.parse(sdf.format(c.getTime()));
		String maxDate2 = sdf.format(maxDate1);

		Date currentDate1 = new Date();
		String currentDate2 = sdf.format(currentDate1);

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yyyy");

		LocalDate maxDate = formatter.parseLocalDate(maxDate2);
		LocalDate currentDate = formatter.parseLocalDate(currentDate2);

		/*
		 * for (PortfolioOverviewDto portfolioOverviewDto :
		 * PortfolioOverviewDtoAssetList) {
		 * 
		 * //System.out.println("inside " + portfolioOverviewDto.getProductName()); }
		 */
		for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoAssetList) {
			if (portfolioOverviewDto.getMaturityDate() != null) {
				if (!portfolioOverviewDto.getMaturityDate().equals("N/A")) {
					Date maturityDate1 = sdf.parse(portfolioOverviewDto.getMaturityDate());
					String maturityDate2 = sdf.format(maturityDate1);
					LocalDate maturityDate = formatter.parseLocalDate(maturityDate2);

					/**
					 * System.out.println("inside it "+portfolioOverviewDto. getProductName());
					 * System.out.println("currentDate "+currentDate); System.out.println("maxDate
					 * "+maxDate); System.out.println("maturityDate"+maturityDate);
					 */

					if (maturityDate.compareTo(currentDate) >= 0 && maturityDate.compareTo(maxDate) <= 0) {
						//System.out.println("between ");
						PortfolioOverviewDtoAssetListReturn.add(portfolioOverviewDto);
					}
				}
			}
		}

		/* System.out.println("size "+PortfolioOverviewDtoAssetListReturn.size() ); */
	}catch (Exception exp) {

		/*FinexaBussinessException finexaBuss = new FinexaBussinessException(FinexaConstant.PORTFOLIO_NETWORTH_MODULE,
				FinexaConstant.PORTFOLIO_NETWORTH_MODULE_CODE, FinexaConstant.PORTFOLIO_NETWORTH_MODULE_DESC, exp);
		FinexaBussinessException.logFinexaBusinessException(finexaBuss);*/
		exp.printStackTrace();
	}
		return PortfolioOverviewDtoAssetListReturn;
	}

	@Override
	public List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiod(String token, int clientId, String specificRequermentStat,
			int timePeriod, Session session, CacheInfoService cacheInfoService)
			throws FinexaBussinessException, ParseException {
		portFolioOverviewService = new PortFolioOverviewServiceImpl();
		List<PortfolioOverviewDto> PortfolioOverviewDtoAssetListReturn = new ArrayList<PortfolioOverviewDto>();
		try{
		List<PortfolioOverviewDto> PortfolioOverviewDtoList = portFolioOverviewService
				.getclientPortfolioOverview(token, clientId, specificRequermentStat, session, cacheInfoService);

		

		Calendar c = null;

		if (timePeriod == 1) {
			c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("DAYS", 7);

		}
		if (timePeriod == 2) {
			c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 1);

		}
		if (timePeriod == 3) {
			c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 3);

		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Date maxDate1 = sdf.parse(sdf.format(c.getTime()));
		String maxDate2 = sdf.format(maxDate1);

		Date currentDate1 = new Date();
		String currentDate2 = sdf.format(currentDate1);

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yyyy");

		LocalDate maxDate = formatter.parseLocalDate(maxDate2);
		LocalDate currentDate = formatter.parseLocalDate(currentDate2);

		/*
		 * for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoList) {
		 * 
		 * 
		 * System.out.println("inside "+portfolioOverviewDto.getProductName( ));
		 * 
		 * }
		 */
		for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoList) {
			// System.out.println("portfolioOverviewDto.getMaturityDate()
			// "+portfolioOverviewDto.getProductName()+"
			// "+portfolioOverviewDto.getProductTypeId()+"
			// "+portfolioOverviewDto.getMaturityDate());
			if (portfolioOverviewDto.getMaturityDate() != null) {
				if (!portfolioOverviewDto.getMaturityDate().equals("N/A")) {

					Date maturityDate1 = sdf.parse(portfolioOverviewDto.getMaturityDate());
					String maturityDate2 = sdf.format(maturityDate1);
					LocalDate maturityDate = formatter.parseLocalDate(maturityDate2);

					/*
					 * System.out.println("inside it "+portfolioOverviewDto. getProductName());
					 * System.out.println("currentDate "+currentDate);
					 * System.out.println("maxDate "+maxDate);
					 * System.out.println("maturityDate"+maturityDate);
					 */
					if (maturityDate.compareTo(currentDate) >= 0 && maturityDate.compareTo(maxDate) <= 0) {
						/* System.out.println("between "); */
						PortfolioOverviewDtoAssetListReturn.add(portfolioOverviewDto);
					}
				}
			}

		}
	}
	catch (Exception exp) {

			FinexaBussinessException finexaBuss = new FinexaBussinessException(FinexaConstant.PORTFOLIO_NETWORTH_MODULE,
					FinexaConstant.PORTFOLIO_NETWORTH_MODULE_CODE, FinexaConstant.PORTFOLIO_NETWORTH_MODULE_DESC, exp);
			FinexaBussinessException.logFinexaBusinessException(finexaBuss);
	}
		// System.out.println("size "+PortfolioOverviewDtoAssetListReturn.size());

		return PortfolioOverviewDtoAssetListReturn;
	}

	@Override
	public List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiodForAdvisor(String token, int advisorUserId,
			String specificRequermentStat, int timePeriod, Session session, CacheInfoService cacheInfoService)
			throws ParseException, FinexaBussinessException {
		List<PortfolioOverviewDto> PortfolioOverviewDtoAssetListReturn = new ArrayList<PortfolioOverviewDto>();
		portFolioOverviewService = new PortFolioOverviewServiceImpl();
		advisorUserDAO = new AdvisorUserDaoImpl();
		try {
			AdvisorUser advisorUser = advisorUserDAO.getAdvisorUserByID(advisorUserId, session);
			//====new get userList=======
			FinanceUtil financeUtil = new FinanceUtil();
			List<ClientMaster> clientMasterList = financeUtil.getClientMasterList(advisorUser, advisorUserDAO, session);
			System.out.println("clientMasterList "+clientMasterList.size());
			//===========end=====
		    //System.out.println("size client List"+advisorUser.getClientMasters().size());

			Calendar c = null;

			if (timePeriod == 1) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("DAYS", 7);

			}
			if (timePeriod == 2) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("DAYS", 14);

			}

			if (timePeriod == 3) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 1);

			}
			if (timePeriod == 4) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 3);

			}
			if (timePeriod == 5) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 6);

			}
			if (timePeriod == 6) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("YEARS", 1);

			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			Date maxDate1 = sdf.parse(sdf.format(c.getTime()));
			String maxDate2 = sdf.format(maxDate1);

			Date currentDate1 = new Date();
			String currentDate2 = sdf.format(currentDate1);

			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yyyy");

			LocalDate maxDate = formatter.parseLocalDate(maxDate2);
			LocalDate currentDate = formatter.parseLocalDate(currentDate2);

			// System.out.println("currentDate "+currentDate);
			// System.out.println("maxDate "+maxDate);

			String middlename = "";
			for (ClientMaster client : advisorUser.getClientMasters()) {
				if(client.getActiveFlag().equals("Y")) {
				if (client.getMiddleName() != null) {
					middlename = client.getMiddleName();
				}
				
				List<PortfolioOverviewDto> PortfolioOverviewDtoList = portFolioOverviewService
						.getclientPortfolioOverview(token, client.getId(), specificRequermentStat, session, cacheInfoService);

				for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoList) {
					if (portfolioOverviewDto.getMaturityDate() != null) {
						if (!portfolioOverviewDto.getMaturityDate().equals("N/A")) {

							Date maturityDate1 = sdf.parse(portfolioOverviewDto.getMaturityDate());
							String maturityDate2 = sdf.format(maturityDate1);
							LocalDate maturityDate = formatter.parseLocalDate(maturityDate2);

							/*
							 * System.out.println("inside it "+portfolioOverviewDto. getProductName());
							 * System.out.println("currentDate "+currentDate);
							 * System.out.println("maxDate "+maxDate);
							 * System.out.println("maturityDate"+maturityDate);
							 */
							// System.out.println("maturityDate "+maturityDate);
							if (maturityDate.compareTo(currentDate) >= 0 && maturityDate.compareTo(maxDate) <= 0) {
								/* System.out.println("between "); */

								portfolioOverviewDto.setClientName(
										client.getFirstName() + " " + middlename + " " + client.getLastName());
								PortfolioOverviewDtoAssetListReturn.add(portfolioOverviewDto);
							}
						}
					}
				}
			}
		
			
			Collections.sort(PortfolioOverviewDtoAssetListReturn, new Comparator<PortfolioOverviewDto>() {
		     
				@Override
				public int compare(PortfolioOverviewDto p1, PortfolioOverviewDto p2) {
					 try {
			                return sdf.parse(p1.getMaturityDate()).compareTo(sdf.parse(p2.getMaturityDate()));
			            } catch (ParseException e) {
			                throw new IllegalArgumentException(e);
			            }
				}
		    });
			}
		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return PortfolioOverviewDtoAssetListReturn;

	}

	@Override
	public List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiodAndAssetForAdvisor(String token, int advisorUserId,
	     String specificRequermentStat, String assetValue, int timePeriod, Session session,
	     CacheInfoService cacheInfoService) throws FinexaBussinessException, ParseException {
		advisorUserDAO = new AdvisorUserDaoImpl();
		portFolioOverviewService = new PortFolioOverviewServiceImpl();
		List<PortfolioOverviewDto> PortfolioOverviewDtoAssetListReturn = null;
		try {
			AdvisorUser advisorUser = advisorUserDAO.getAdvisorUserByID(advisorUserId, session);
			
			//====new get userList=======
			FinanceUtil financeUtil = new FinanceUtil();
			List<ClientMaster> clientMasterList = financeUtil.getClientMasterList(advisorUser, advisorUserDAO, session);
			System.out.println("clientMasterList "+clientMasterList.size());
			//===========end=====
			
					//System.out.println("size client List"+advisorUser.getClientMasters().size());
			List<PortfolioOverviewDto> PortfolioOverviewDtoAssetList = new ArrayList<PortfolioOverviewDto>();

			PortfolioOverviewDtoAssetListReturn = new ArrayList<PortfolioOverviewDto>();

			Calendar c = null;

			if (timePeriod == 1) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("DAYS", 7);

			}
			if (timePeriod == 2) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("DAYS", 14);

			}

			if (timePeriod == 3) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 1);

			}
			if (timePeriod == 4) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 3);

			}
			if (timePeriod == 5) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("MONTHS", 6);

			}
			if (timePeriod == 6) {
				c = com.finlabs.finexa.pm.util.FinexaDateUtil.getNextDateFromNow("YEARS", 1);

			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			Date maxDate1 = sdf.parse(sdf.format(c.getTime()));
			String maxDate2 = sdf.format(maxDate1);

			Date currentDate1 = new Date();
			String currentDate2 = sdf.format(currentDate1);

			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-yyyy");

			LocalDate maxDate = formatter.parseLocalDate(maxDate2);
			LocalDate currentDate = formatter.parseLocalDate(currentDate2);

			// System.out.println("currentDate "+currentDate);
			// System.out.println("maxDate "+maxDate);

			String middleName = "";
		
			//for (ClientMaster client : advisorUser.getClientMasters()) {
			for (ClientMaster client : clientMasterList) {
				if(client.getActiveFlag().equals("Y")) {
				try {
					if (client.getMiddleName() != null) {
						middleName = client.getMiddleName();
					}
					
					List<PortfolioOverviewDto> PortfolioOverviewDtoList = portFolioOverviewService
							.getclientPortfolioOverview(token, client.getId(), specificRequermentStat, session, cacheInfoService);
					
					for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoList) {

						
						if ((portfolioOverviewDto.getProductName()).equals(assetValue)) {
							
							portfolioOverviewDto
									.setClientName(client.getFirstName() + " " + middleName + " " + client.getLastName());
							PortfolioOverviewDtoAssetList.add(portfolioOverviewDto);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		  }
			// System.out.println("PortfolioOverviewDtoAssetList"+PortfolioOverviewDtoAssetList.size());
			// System.out.println("================ ");
			/*
			 * if(!PortfolioOverviewDtoAssetList.isEmpty()){ for (PortfolioOverviewDto
			 * portfolioOverviewDto : PortfolioOverviewDtoAssetList) {
			 * 
			 * System.out.println("inside " +
			 * portfolioOverviewDto.getProductName()+" "+client.getFirstName());
			 * System.out.println("portfolioOverviewDto.getMaturityDate() "+portfolioOverviewDto.
			 * getMaturityDate()); System.out.println("===================== "); } }
			 */
			if (!PortfolioOverviewDtoAssetList.isEmpty()) {
				for (PortfolioOverviewDto portfolioOverviewDto : PortfolioOverviewDtoAssetList) {

					if (portfolioOverviewDto.getMaturityDate() != null) {
						if (!portfolioOverviewDto.getMaturityDate().equals("N/A")) {
							Date maturityDate1 = sdf.parse(portfolioOverviewDto.getMaturityDate());
							String maturityDate2 = sdf.format(maturityDate1);
							LocalDate maturityDate = formatter.parseLocalDate(maturityDate2);

							// System.out.println("maturityDate "+maturityDate);

							if (maturityDate.compareTo(currentDate) >= 0 && maturityDate.compareTo(maxDate) <= 0) {
								// System.out.println("between ");

								PortfolioOverviewDtoAssetListReturn.add(portfolioOverviewDto);
							}
						}
					}
				}
			}
		  
			System.out.println("PortfolioOverviewDtoAssetListReturn "+PortfolioOverviewDtoAssetListReturn.size());
		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collections.sort(PortfolioOverviewDtoAssetListReturn, new Comparator<PortfolioOverviewDto>() {
			public int compare(PortfolioOverviewDto o1, PortfolioOverviewDto o2) {
				return (o1.getClientName()).compareTo(o2.getClientName());
			}
		});

		/*
		 * if(!PortfolioOverviewDtoAssetListReturn.isEmpty()){ for (PortfolioOverviewDto
		 * portfolioOverviewDto : PortfolioOverviewDtoAssetListReturn) {
		 * 
		 * //System.out.println("inside " +
		 * portfolioOverviewDto.getProductName()+" "+portfolioOverviewDto.getClientName(
		 * ));
		 * //System.out.println("portfolioOverviewDto.getMaturityDate() "+portfolioOverviewDto.
		 * getMaturityDate()); //System.out.println("===================== "); } }
		 */
		// System.out.println("size "+PortfolioOverviewDtoAssetListReturn.size());
		return PortfolioOverviewDtoAssetListReturn;

	}

	@SuppressWarnings("null")
	@Override
	public List<PortfolioNetWorthDto> getClientNetworthDownloadByAdvisorUserID(String token, int advisorUserID, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<Integer> idList = null;
		PortfolioNetWorthDto portfolioNetWorthDto;
		portFolioNetWorthService = new PortFolioNetWorthServiceImpl();
		List<PortfolioNetWorthDto> PortfolioNetWorthDtoList = new ArrayList<PortfolioNetWorthDto>();
		try {
			advisorUserDAO = new AdvisorUserDaoImpl();
			AdvisorUser advisorUser = advisorUserDAO.getAdvisorUserByID(advisorUserID, session);
			/// =============start===============
			/*
			 * idList = new ArrayList<Integer>(); // Parent idList.add(advisorUser.getId());
			 * // Child List<AdvisorUserSupervisorMapping> advisorUserSupervisorParentList =
			 * advisorUserDAO.geSubUser(advisorUser.getId(), session);
			 * List<AdvisorUserSupervisorMapping> advisorUserSupervisorChildList = null; if
			 * (!advisorUserSupervisorParentList.isEmpty()) { for
			 * (AdvisorUserSupervisorMapping obj : advisorUserSupervisorParentList) {
			 * idList.add(obj.getId());
			 * 
			 * // Grandchild advisorUserSupervisorParentList =
			 * advisorUserDAO.geSubUser(obj.getAdvisorUser1().getId(), session); if
			 * (!advisorUserSupervisorChildList.isEmpty()) { for
			 * (AdvisorUserSupervisorMapping advisorUserSupervisor :
			 * advisorUserSupervisorChildList) { idList.add(advisorUserSupervisor.getId());
			 * } } } } //client List System.out.println("idList "+idList);
			 * List<ClientMaster> clientMasterList = advisorUserDAO.getClientsByUser(idList,
			 * session); System.out.println("clientMasterList "+clientMasterList.size());
			 */
			 FinanceUtil financeUtil = new FinanceUtil();
			 List<ClientMaster> clientMasterList = financeUtil.getClientMasterList(advisorUser,advisorUserDAO,session);
			 System.out.println("clientMasterList "+clientMasterList.size());
			 /// =============end===============
			for (ClientMaster client : clientMasterList){
			//for (ClientMaster client : advisorUser.getClientMasters()) {
				if (client.getActiveFlag().equals("Y")) {
					portfolioNetWorthDto = portFolioNetWorthService.getclientPortfolioNetWorth(token, client.getId(), session,
							cacheInfoService);
					portfolioNetWorthDto.setClientID(client.getId());
					String name = client.getFirstName() + " "
							+ (client.getMiddleName() != null ? client.getMiddleName() : "") + " "
							+ client.getLastName();
					portfolioNetWorthDto.setName(name);

					PortfolioNetWorthDtoList.add(portfolioNetWorthDto);
				}

			}

			Collections.sort(PortfolioNetWorthDtoList, new Comparator<PortfolioNetWorthDto>() {
				public int compare(PortfolioNetWorthDto o1, PortfolioNetWorthDto o2) {
					return (o1.getName()).compareTo(o2.getName());
				}
			});
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} //
		System.out.println("PortfolioNetWorthDtoList " + PortfolioNetWorthDtoList.size());
		return PortfolioNetWorthDtoList;

	}
	 
	
	@Override
	public List<PortfolioNetWorthDto> getClientNetworthByAdvisorUserIDviewOnDemand(int advisorUserID, Session session,
			CacheInfoService cacheInfoService, HttpServletRequest request) throws FinexaBussinessException {
		
		PortfolioNetWorthDto portfolioNetWorthDto;
		portFolioNetWorthService = new PortFolioNetWorthServiceImpl();
		List<PortfolioNetWorthDto> PortfolioNetWorthDtoList = new ArrayList<PortfolioNetWorthDto>();
		UserClientRedis userDetailsDTO = null;
		List<PortfolioNetWorthDto> portfolioNetWorthDtolistFromRedis = null;
		int endFlag = 0;
		try {
			String header = request.getHeader(FinexaConstant.HEADER_STRING);
			String token = cacheInfoService.getToken(header);
			
			advisorUserDAO = new AdvisorUserDaoImpl();
			//AdvisorUser advisorUser = advisorUserDAO.getAdvisorUserByID(advisorUserID, session);
			userDetailsDTO = cacheInfoService.getClientCacheMap(token, advisorUserID);
			if(userDetailsDTO != null) {
			portfolioNetWorthDtolistFromRedis = userDetailsDTO.getPortfolioNetWorthDtoList();
			if(portfolioNetWorthDtolistFromRedis == null) {
				portfolioNetWorthDtolistFromRedis = new ArrayList<PortfolioNetWorthDto>();
			}
			if(portfolioNetWorthDtolistFromRedis.size() == userDetailsDTO.getTotalClient()) {
				endFlag = 1;
			}
			if(endFlag != 1) 
			{
			 for (ClientMaster client : userDetailsDTO.getClientmasters()) {
				portfolioNetWorthDto = portFolioNetWorthService.getclientPortfolioNetWorth(token, client.getId(), session,
						cacheInfoService);
				portfolioNetWorthDto.setClientID(client.getId());
				String name = client.getFirstName() + " " + (client.getMiddleName() != null ? client.getMiddleName() : "")
						+ " " + client.getLastName();
				portfolioNetWorthDto.setName(name);
				
				PortfolioNetWorthDtoList.add(portfolioNetWorthDto);
			 }
			}
			//portfolioNetWorthDtolistFromRedis = userDetailsDTO.getPortfolioNetWorthDtoList();
			if(endFlag != 1) 
			{
			portfolioNetWorthDtolistFromRedis.addAll(PortfolioNetWorthDtoList);
			userDetailsDTO.setPortfolioNetWorthDtoList(portfolioNetWorthDtolistFromRedis);
			userDetailsDTO.setId(advisorUserID);
			
				/*
				 * if(userDetailsDTO.getPortfolioNetWorthDtoList().size() ==
				 * userDetailsDTO.getTotalClient()) { System.out.
				 * println("after userDetailsDTO.getPortfolioNetWorthDtoList().size() "
				 * +userDetailsDTO.getPortfolioNetWorthDtoList().size());
				 * System.out.println("after userDetailsDTO.getTotalClient() "+userDetailsDTO.
				 * getTotalClient()); int c = 1; userDetailsDTO.setPMCount(c);
				 * System.out.println("userDetailsDTO.getTotalClient() "+userDetailsDTO.
				 * getPMCount()); }
				 */
			cacheInfoService.addClientCacheMap(token, userDetailsDTO);
			//userDetailsDTO = cacheInfoService.getClientCacheMap(token, advisorUserID);
			}
		
			Collections.sort(portfolioNetWorthDtolistFromRedis, new Comparator<PortfolioNetWorthDto>() {
				public int compare(PortfolioNetWorthDto o1, PortfolioNetWorthDto o2) {
					return (o1.getName()).compareTo(o2.getName());
				}
			});
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   // System.out.println("PortfolioNetWorthDtoList "+PortfolioNetWorthDtoList.size());
		return portfolioNetWorthDtolistFromRedis;

	}

	@Override
	public List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiod(int clientId, String specificRequermentStat,
			int timePeriod, Session session, CacheInfoService cacheInfoService)
			throws FinexaBussinessException, ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
