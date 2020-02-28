package com.finlabs.finexa.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.PortfolioNetWorthDto;
import com.finlabs.finexa.pm.util.ExcelPortfolioUtility;
import com.finlabs.finexa.pm.util.FinexaConstant;
import com.finlabs.finexa.service.PortFolioDashBoardService;
import com.finlabs.finexa.service.PortFolioOverviewService;
import com.finlabs.finexa.service.PortFolioTrackerDownloadService;
import com.finlabs.finexa.service.impl.PortFolioDashBoardServiceImpl;
import com.finlabs.finexa.service.impl.PortFolioOverviewServiceImpl;
import com.finlabs.finexa.service.impl.PortFolioTrackerDownloadServiceImpl;



@RestController
public class PortFolioDashBoardController {
	// @Autowired
	// private PortFolioDashBoardService portFolioOverviewDashBoardService;
	Logger log = Logger.getLogger(PortFolioDashBoardController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;
	@Autowired
	private CacheInfoService cacheInfoService;

	
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getClientPortfolioOverviewTimeperiodAndAsset", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getclientPortfolioOverviewTimeperiodAndAsset(
			@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat,
			@RequestParam(value = "timePeriod") int timePeriod, @RequestParam(value = "asset") String asset,HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getclientPortfolioOverviewTimeperiodAndAsset() ");

		Session session = null;
		PortFolioDashBoardService portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
		String header = "",token = "";
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
			portfolioOverviewDtoList = portFolioOverviewDashBoardService.getclientPortfolioOverviewTimeperiodAndAsset(token,
					clientId, specificRequermentStat, timePeriod, asset, session,cacheInfoService);
			log.info("PortFolioOverviewController <<< Exiting getclientPortfolioOverviewTimeperiodAndAsset() ");
			return new ResponseEntity<List<PortfolioOverviewDto>>(portfolioOverviewDtoList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed to get Client Portfolio Overview , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}
	
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getclientPortfolioOverviewTimeperiodAndAssetForAdvisor", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getclientPortfolioOverviewTimeperiodAndAssetForAdvisor(
			@RequestParam(value = "advisorUserId") int advisorUserId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat,
			 @RequestParam(value = "asset") String asset,@RequestParam(value = "timePeriod") int timePeriod, HttpServletRequest request)
			throws FinexaBussinessException, ParseException {
		log.info("PortFolioOverviewController >>> Entering getclientPortfolioOverviewTimeperiodAndAssetForAdvisor() ");
        //System.out.println("PortFolioOverviewController >>> Entering getclientPortfolioOverviewTimeperiodAndAssetForAdvisor() ");
		Session session = null;
		String header = "",token = "";
		PortFolioDashBoardService portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			//List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
			List<PortfolioOverviewDto> portfolioOverviewDtoList = new ArrayList<PortfolioOverviewDto>();
			portfolioOverviewDtoList = portFolioOverviewDashBoardService
					.getclientPortfolioOverviewTimeperiodAndAssetForAdvisor(token, advisorUserId, specificRequermentStat,
							asset, timePeriod, session, cacheInfoService);
			log.info("PortFolioOverviewController <<< Exiting getclientPortfolioOverviewTimeperiodAndAssetForAdvisor() ");
			return new ResponseEntity<List<PortfolioOverviewDto>>(portfolioOverviewDtoList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed to get Client Portfolio Overview , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}
	
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getclientPortfolioOverviewTimeperiod", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getclientPortfolioOverviewTimeperiod(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat,
			@RequestParam(value = "timePeriod") int timePeriod,HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getclientPortfolioOverviewTimeperiod() ");
		Session session = null;
		String header = "",token = "";
		PortFolioDashBoardService portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
			portfolioOverviewDtoList = portFolioOverviewDashBoardService.getclientPortfolioOverviewTimeperiod(clientId,
					specificRequermentStat, timePeriod, session,cacheInfoService);
			log.info("PortFolioOverviewController <<< Exiting getClientPortfolioOverview() ");
			return new ResponseEntity<List<PortfolioOverviewDto>>(portfolioOverviewDtoList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed to get Client Portfolio PortfolioOverviewTimeperiod , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getclientPortfolioOverviewTimeperiodForAdvisor", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getclientPortfolioOverviewTimeperiodForAdvisor(
			@RequestParam(value = "advisorUserId") int advisorUserId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat,
			@RequestParam(value = "timePeriod") int timePeriod, HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getclientPortfolioOverviewTimeperiodForAdvisor() ");
		Session session = null;
		String header = "",token = "";
		PortFolioDashBoardService portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
			portfolioOverviewDtoList = portFolioOverviewDashBoardService.getclientPortfolioOverviewTimeperiodForAdvisor(token, advisorUserId, specificRequermentStat, timePeriod, session,cacheInfoService);
			log.info("PortFolioOverviewController <<< Exiting getclientPortfolioOverviewTimeperiodForAdvisor() ");
			return new ResponseEntity<List<PortfolioOverviewDto>>(portfolioOverviewDtoList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed to get Client Portfolio TimeperiodForAdvisor , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}

	/*
	 * @RequestMapping(value = "/getClientNetworthByAdvisorId", method =
	 * RequestMethod.GET, headers = "Accept=application/json") public
	 * ResponseEntity<?> getClientNetworthByAdvisorId(@RequestParam(value =
	 * "advisorUserID") int advisorUserID,HttpServletRequest request) { log.
	 * info("PortFolioOverviewController >>> Entering getClientNetworthByAdvisorId() "
	 * ); Session session = null; PortFolioDashBoardService
	 * portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl(); try
	 * { session = sessionfactory.openSession(); List<PortfolioNetWorthDto>
	 * portfolioNetworthDtoList = null; portfolioNetworthDtoList =
	 * portFolioOverviewDashBoardService.getClientNetworthByAdvisorUserID(
	 * advisorUserID, session,cacheInfoService); log.
	 * info("PortFolioOverviewController <<< Exiting getClientNetworthByAdvisorId() "
	 * ); return new
	 * ResponseEntity<List<PortfolioNetWorthDto>>(portfolioNetworthDtoList,
	 * HttpStatus.OK);
	 * 
	 * } catch (FinexaBussinessException busExcep) {
	 * FinexaBussinessException.logFinexaBusinessException(busExcep); return new
	 * ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK); }
	 * catch (Exception exp) { FinexaBussinessException businessException = new
	 * FinexaBussinessException("Client Port Folio Networth", "111",
	 * "Failed to get Client Portfolio ClientNetworthByAdvisorId , Please try again."
	 * , exp);
	 * FinexaBussinessException.logFinexaBusinessException(businessException);
	 * return new ResponseEntity<String>(businessException.getErrorDescription(),
	 * HttpStatus.OK); } finally { session.close();
	 * 
	 * }
	 * 
	 * }
	 */
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getClientNetworthByAdvisorId", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientNetworthByAdvisorId(@RequestParam(value = "advisorUserID") int advisorUserID,HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getClientNetworthByAdvisorId() ");
		Session session = null;
		PortFolioDashBoardService portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
		try {
			session = sessionfactory.openSession();
			List<PortfolioNetWorthDto> portfolioNetworthDtoList = null;
			
			
			/*
			 * portfolioNetworthDtoList =
			 * portFolioOverviewDashBoardService.getClientNetworthByAdvisorUserID(
			 * advisorUserID, session, cacheInfoService);
			 */
			 
			portfolioNetworthDtoList = portFolioOverviewDashBoardService.getClientNetworthByAdvisorUserIDviewOnDemand(advisorUserID, session, cacheInfoService, request);
			log.info("PortFolioOverviewController <<< Exiting getClientNetworthByAdvisorId() ");
			return new ResponseEntity<List<PortfolioNetWorthDto>>(portfolioNetworthDtoList, HttpStatus.OK);

		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			exp.printStackTrace();
			/*
			 * FinexaBussinessException businessException = new
			 * FinexaBussinessException("Client Port Folio Networth", "111",
			 * "Failed to get Client Portfolio ClientNetworthByAdvisorId , Please try again."
			 * , exp);
			 * FinexaBussinessException.logFinexaBusinessException(businessException);
			 * return new ResponseEntity<String>(businessException.getErrorDescription(),
			 * HttpStatus.OK);
			 */
		} finally {
			session.close();

		}
		return null;

	}

	
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getClientPortfolioOverviewDownload", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioOverviewDownload(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat, HttpServletRequest request) {
		//log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverview() download ");
		Session session = null;
		//log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		String requestHeader = "",token = "";
		//PortFolioTrackerDownloadService portFolioTrackerDownloadService = new PortFolioTrackerDownloadServiceImpl();
		PortFolioOverviewService portFolioOverviewService = new PortFolioOverviewServiceImpl();
		try {
			
			session = sessionfactory.openSession();
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
			requestHeader = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(requestHeader);
			portfolioOverviewDtoList = portFolioOverviewService.getclientPortfolioOverview(token, clientId,
					specificRequermentStat, session,cacheInfoService);
			
			
			
		   // System.out.println("portfolioOverviewDtoList jjj"+portfolioOverviewDtoList.size());
			ClassLoader loader = getClass().getClassLoader();
		  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelPortfolioUtility.writeExcelOutputData(file, portfolioOverviewDtoList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			//System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			exp.printStackTrace();
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		return returner ;

		
	}
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getclientPortfolioOverviewTimeperiodDownload", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getclientPortfolioOverviewTimeperiodDownload(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat,
			@RequestParam(value = "timePeriod") int timePeriod) {	//log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverview() download ");
		Session session = null;
		//log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		
		PortFolioDashBoardService portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
		try {
			
			session = sessionfactory.openSession();
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
		
			portfolioOverviewDtoList = portFolioOverviewDashBoardService.getclientPortfolioOverviewTimeperiod(clientId,
					specificRequermentStat, timePeriod, session,cacheInfoService);
			
			
		    //System.out.println("portfolioOverviewDtoList jjj"+portfolioOverviewDtoList.size());
			ClassLoader loader = getClass().getClassLoader();
		  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelPortfolioUtility.writeExcelOutputDataAsset(file, portfolioOverviewDtoList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			//System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			exp.printStackTrace();
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		return returner ;
		}
	
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/assetMaturityRenewalDownloadForAdvisor", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> assetMaturityRenewalDownloadForAdvisor(@RequestParam(value = "userId") int userId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat,
			@RequestParam(value = "timePeriod") int timePeriod,HttpServletRequest request) {	//log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverview() download ");
		Session session = null;
		//log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		String requsetHeader = "",token = "";
		PortFolioTrackerDownloadService portFolioAssetDownloadService = null;
		PortFolioDashBoardService portFolioOverviewDashBoardService = null;
		try {
			portFolioAssetDownloadService = new PortFolioTrackerDownloadServiceImpl();
			portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
			session = sessionfactory.openSession();
			requsetHeader = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(requsetHeader);
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
		
			portfolioOverviewDtoList = portFolioOverviewDashBoardService.getclientPortfolioOverviewTimeperiodForAdvisor(token, userId, specificRequermentStat, timePeriod, session,cacheInfoService);
			
		    //System.out.println("portfolioOverviewDtoList userID"+portfolioOverviewDtoList.size());
			ClassLoader loader = getClass().getClassLoader();
		  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelPortfolioUtility.writeExcelOutputDataAssetForAdvisor(file, portfolioOverviewDtoList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			//System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			exp.printStackTrace();
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		return returner ;
		}
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getAssetClassWiseAUMDownload", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getAssetClassWiseAUMDownload(@RequestParam(value = "userID") int userID,HttpServletRequest request) {
		//log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverview() download ");
		Session session = null;
		//log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		String requestHeader = "",token = "";
		PortFolioDashBoardService portFolioOverviewDashBoardService = null;
		try {
			session = sessionfactory.openSession();
			requestHeader = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(requestHeader);
			List<PortfolioNetWorthDto> portfolioNetworthDtoList = null;
			portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
			requestHeader = request.getHeader(FinexaConstant.HEADER_STRING);
			portfolioNetworthDtoList = portFolioOverviewDashBoardService.getClientNetworthDownloadByAdvisorUserID(token, userID,session,cacheInfoService);
		    //System.out.println("portfolioOverviewDtoList "+portfolioNetworthDtoList.size());
			ClassLoader loader = getClass().getClassLoader();
		  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelPortfolioUtility.writeExcelOutputDataAssetClassWiseAUM(file, portfolioNetworthDtoList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			//System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			exp.printStackTrace();
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		return returner ;

		
	}
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getProductTypesWiseAUMDownload", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getProductTypesWiseAUMDownload(@RequestParam(value = "userID") int userID,HttpServletRequest request) {
		//log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverview() download ");
		Session session = null;
		//log.info("ClientIncomeController >>> Entering downloadExcelOutputExl() ");
		XSSFWorkbook workbook = null;
		ResponseEntity<?> returner = null;
		byte excelByte[];
		HttpHeaders header;
		String requetHeader = "",token = "";
		try {
			session = sessionfactory.openSession();
			List<PortfolioNetWorthDto> portfolioNetworthDtoList = null;
			PortFolioDashBoardService portFolioOverviewDashBoardService = new PortFolioDashBoardServiceImpl();
			requetHeader = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(requetHeader);
			portfolioNetworthDtoList = portFolioOverviewDashBoardService.getClientNetworthDownloadByAdvisorUserID(token, userID,session,cacheInfoService);
		    //System.out.println("portfolioOverviewDtoList jjj"+portfolioNetworthDtoList.size());
			ClassLoader loader = getClass().getClassLoader();
		  
			File file = null;
			if (loader.getResource("Excel_Output.xlsx").getFile() != null) {
				file = new File(loader.getResource("Excel_Output.xlsx").getFile());
			} else {
				throw new FinexaBussinessException("ClientIncome", "111", "Download Failed");
			}
       
			workbook = ExcelPortfolioUtility.writeExcelOutputDataProductTypeWiseAUM(file, portfolioNetworthDtoList);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
		    header = new HttpHeaders();
			header.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));

		    excelByte = bos.toByteArray();
			//System.out.println("excelByte.length "+excelByte.length);
			header.setContentLength(excelByte.length);
			returner = new ResponseEntity<byte[]>(excelByte, header, HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("ClientIncome", "111",
					"Failed To download  , Please Try again later.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			exp.printStackTrace();
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);
		} finally {
			session.close();

		}
		return returner ;

		
	}
	
	
}
