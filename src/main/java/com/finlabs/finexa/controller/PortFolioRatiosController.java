package com.finlabs.finexa.controller;

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
import com.finlabs.finexa.dto.ClientRiskDTO;
import com.finlabs.finexa.dto.PortfolioRatiosDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.service.PortFolioRatiosService;
import com.finlabs.finexa.service.impl.PortFolioRatiosServiceImpl;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class PortFolioRatiosController {
	// @Autowired
	// private PortFolioRatiosService portFolioRatiosService;
	Logger log = Logger.getLogger(PortFolioRatiosController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;
	@Autowired
	private CacheInfoService cacheInfoService;

	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientPortfolioRatios", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientNetworth(@RequestParam(value = "clientId") int clientId, HttpServletRequest request) {
		log.info("PortFolioOverviewController >>> Entering getClientPortfolioRatios() ");
		Session session = null;
		String header = "",token = "";
		PortFolioRatiosService portFolioRatiosService = new PortFolioRatiosServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			List<PortfolioRatiosDto> portfolioNetworthDtoList = null;
			portfolioNetworthDtoList = portFolioRatiosService.getclientPortfolioRatios(token, clientId, session,
					cacheInfoService);
			log.info("PortFolioOverviewController <<< Exiting getClientPortfolioRatios() ");
			return new ResponseEntity<List<PortfolioRatiosDto>>(portfolioNetworthDtoList, HttpStatus.OK);
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
	
	    // calling client risk
	    @PreAuthorize("hasAnyRole('Admin','PortFolioManagementView')")
		@RequestMapping(value = "/getClientRiskInfo", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<?> getClientRiskInfo(@RequestParam(value = "clientId") int clientId, HttpServletRequest request) {
			log.info("ClientGoalController >>> Entering getClientRiskInfo() ");
			Session session = null;
			String header = "",token = "";
			PortFolioRatiosService portFolioRatiosService = new PortFolioRatiosServiceImpl();
			try {
				session = sessionfactory.openSession();
				header = request.getHeader(FinexaConstant.HEADER_STRING);
				token = cacheInfoService.getToken(header);
				ClientRiskDTO riskOutput = portFolioRatiosService.getRiskOutput(token, clientId, session);
				return new ResponseEntity<ClientRiskDTO>(riskOutput, HttpStatus.OK);
			} catch (FinexaBussinessException busExcep) {
				FinexaBussinessException.logFinexaBusinessException(busExcep);
				return null;
			} catch (Exception exp) {
				log.info("CLienGoalController <<< Exiting getClientRiskInfo() ");
				return null;
			} finally {
				session.close();

			}

		}

}
