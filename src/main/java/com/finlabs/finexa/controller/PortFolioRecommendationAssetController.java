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
import com.finlabs.finexa.dto.PortfolioRecommendationDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.service.PortFolioRecommentdationService;
import com.finlabs.finexa.service.impl.PortFolioRecommendationServiceImpl;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class PortFolioRecommendationAssetController {
	// @Autowired
	// private PortFolioRecommentdationService portFolioRecommentdationService;
	Logger log = Logger.getLogger(PortFolioRecommendationAssetController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;
	@Autowired
	private CacheInfoService cacheInfoService;
	
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getClientPortfolioAssetAllocation", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioAssetReview(@RequestParam(value = "clientId") int clientId,HttpServletRequest request) {
		log.info("PortFolioRecommendationAssetController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		String header = "",token = "";
		PortFolioRecommentdationService portFolioRecommentdationService = new PortFolioRecommendationServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			PortfolioRecommendationDto portfolioRecommendationDto = null;
			portfolioRecommendationDto = portFolioRecommentdationService.getclientPortfolioRecommendation(token, clientId,
					session,cacheInfoService);
			log.info("PortFolioRecommendationAssetController <<< Exiting getClientPortfolioAssetReview() ");
			return new ResponseEntity<PortfolioRecommendationDto>(portfolioRecommendationDto, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			FinexaBussinessException businessException = new FinexaBussinessException(
					"Client  Portfolio recommendation", "111",
					"Failed to get Client Portfolio Networth , Please try again.", exp);
			FinexaBussinessException.logFinexaBusinessException(businessException);
			return new ResponseEntity<String>(businessException.getErrorDescription(), HttpStatus.OK);
		} finally {
			session.close();

		}

	}

}
