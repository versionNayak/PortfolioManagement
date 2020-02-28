package com.finlabs.finexa.controller;

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

import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.PortfolioNetWorthDto;
import com.finlabs.finexa.service.PortFolioNetWorthService;
import com.finlabs.finexa.service.impl.PortFolioNetWorthServiceImpl;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class PortFolioNetWorthController {
	//@Autowired
	//private PortFolioNetWorthService portFolioNetWorthService;
	Logger log = Logger.getLogger(PortFolioNetWorthController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;
	@Autowired
	private CacheInfoService cacheInfoService;
	
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientNetworth", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientNetworth(@RequestParam(value = "clientId") int clientId, HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getClientNetworth() ");
		Session session = null;
		String header = "",token = "";
		 PortFolioNetWorthService portFolioNetWorthService = new PortFolioNetWorthServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			PortfolioNetWorthDto portfolioNetworthDtoList = null;
			portfolioNetworthDtoList = portFolioNetWorthService.getclientPortfolioNetWorth(token, clientId, session,cacheInfoService);
			log.info("PortFolioOverviewController <<< Exiting getClientNetworth() ");
			return new ResponseEntity<PortfolioNetWorthDto>(portfolioNetworthDtoList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException("Client Port Folio Networth",
					"111", "Failed to get Client Portfolio Networth , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}

}
