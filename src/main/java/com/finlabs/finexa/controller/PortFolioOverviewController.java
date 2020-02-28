package com.finlabs.finexa.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.finlabs.finexa.dto.PortfolioOvervieEquityDto;
import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.service.PortFolioOverviewService;
import com.finlabs.finexa.service.impl.PortFolioOverviewServiceImpl;
import com.finlabs.finexa.util.FinexaConstant;


@RestController
public class PortFolioOverviewController {
	// @Autowired
	// private PortFolioOverviewService portFolioOverviewService;
	Logger log = Logger.getLogger(PortFolioOverviewController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;
	@Autowired
	private CacheInfoService cacheInfoService;

	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getClientPortfolioOverview", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioOverview(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat, HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverview() ");
		Session session = null;
		String header = "",token = "";
		PortFolioOverviewService portFolioOverviewService = new PortFolioOverviewServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
			portfolioOverviewDtoList = portFolioOverviewService.getclientPortfolioOverview(token, clientId,
					specificRequermentStat, session, cacheInfoService);
			
			log.info("PortFolioOverviewController <<< Exiting getClientPortfolioOverview() ");
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
	@RequestMapping(value = "/getClientPortfolioOverviewEquity", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioOverviewEquity(@RequestParam(value = "clientId") int clientId, HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverviewEquity() ");
		Session session = null;
		String header = "",token = "";
		PortFolioOverviewService portFolioOverviewService = new PortFolioOverviewServiceImpl();
		try {
			session = sessionfactory.openSession();
			PortfolioOvervieEquityDto portfolioOvervieEquityDto = null;
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			portfolioOvervieEquityDto = portFolioOverviewService.getclientPortfolioOverviewEquity(token, clientId, session,
					cacheInfoService);
			
			log.info("PortFolioOverviewController <<< Exiting getClientPortfolioOverviewEquity() ");
			return new ResponseEntity<PortfolioOvervieEquityDto>(portfolioOvervieEquityDto, HttpStatus.OK);
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
	@RequestMapping(value = "/getClientPortfolioOverviewFixedIncome", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioOverviewFixedIncome(@RequestParam(value = "clientId") int clientId, HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverviewFixedIncome() ");
		Session session = null;
		String header = "",token = "";
		PortFolioOverviewService portFolioOverviewService = new PortFolioOverviewServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			PortfolioOvervieEquityDto portfolioOvervieEquityDto = null;
			portfolioOvervieEquityDto = portFolioOverviewService.getclientPortfolioOverviewFixedIncome(token, clientId,
					session, cacheInfoService);
			log.info("PortFolioOverviewController <<< Exiting getClientPortfolioOverviewFixedIncome() ");
			return new ResponseEntity<PortfolioOvervieEquityDto>(portfolioOvervieEquityDto, HttpStatus.OK);
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
	@RequestMapping(value = "/getClientPortfolioOverviewKV", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioOverviewKV(@RequestParam(value = "clientId") int clientId,
			@RequestParam(value = "specificRequermentStat") String specificRequermentStat) {
		log.info("PortFolioOverviewController >>> Entering getClientPortfolioOverview() ");
		Session session = null;
		PortFolioOverviewService portFolioOverviewService = new PortFolioOverviewServiceImpl();
		try {
			session = sessionfactory.openSession();
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
			portfolioOverviewDtoList = portFolioOverviewService.getclientPortfolioOverviewKV(clientId,
					specificRequermentStat, session, cacheInfoService);
			
			log.info("PortFolioOverviewController <<< Exiting getClientPortfolioOverview() ");
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
	
	


		

}
