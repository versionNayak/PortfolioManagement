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

import com.finlabs.finexa.dto.PortfolioAssetAllocationReviewDto;
import com.finlabs.finexa.dto.PortfolioHistoricalReturnOutput;
import com.finlabs.finexa.dto.ProductRecommendationOutputPM;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.service.PortFolioAssetAllocationReviewService;
import com.finlabs.finexa.service.impl.PortFolioAssetAllocationReviewServiceImpl;
import com.finlabs.finexa.util.FinexaConstant;

@RestController
public class PortFolioAssetAllocationReviewController {
	// @Autowired
	// private PortFolioAssetAllocationReviewService
	// portFolioAssetAllocationReviewService;
	Logger log = Logger.getLogger(PortFolioAssetAllocationReviewController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;
	@Autowired
	private CacheInfoService cacheInfoService;

	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientPortfolioAssetReview", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioAssetReview(@RequestParam(value = "clientId") int clientId, HttpServletRequest request) {
		log.info("PortFolioAssetAllocationReviewController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		String header = "",token = "";
		PortFolioAssetAllocationReviewService portFolioAssetAllocationReviewService = new PortFolioAssetAllocationReviewServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			List<PortfolioAssetAllocationReviewDto> portfolioAssetAllocationReviewDtoList = null;
			portfolioAssetAllocationReviewDtoList = portFolioAssetAllocationReviewService
					.getclientPortfolioAssetAllocationReview(token, clientId, session, cacheInfoService);
			log.info("PortFolioAssetAllocationReviewController <<< Exiting getClientPortfolioAssetReview() ");
			return new ResponseEntity<List<PortfolioAssetAllocationReviewDto>>(portfolioAssetAllocationReviewDtoList,
					HttpStatus.OK);
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
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getClientPortfolioSubAssetReview", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioSubAssetReview(@RequestParam(value = "clientId") int clientId, HttpServletRequest request) {
		log.info("PortFolioAssetAllocationReviewController >>> Entering getClientPortfolioSubAssetReview() ");
		Session session = null;
		String header = "",token = "";
		PortFolioAssetAllocationReviewService portFolioAssetAllocationReviewService = new PortFolioAssetAllocationReviewServiceImpl();
		try {
			session = sessionfactory.openSession();
			PortfolioAssetAllocationReviewDto portfolioAssetAllocationReviewDto = null;
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			portfolioAssetAllocationReviewDto = portFolioAssetAllocationReviewService
					.getclientPortfolioSubAssetAllocationReview(token, clientId, session, cacheInfoService);

			log.info("PortFolioAssetAllocationReviewController <<< Exiting getClientPortfolioSubAssetReview() ");
			return new ResponseEntity<PortfolioAssetAllocationReviewDto>(portfolioAssetAllocationReviewDto,
					HttpStatus.OK);
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
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView','FinancialPlanningView')")
	@RequestMapping(value = "/getPortfolioAsssetHistoricalReturn", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getPortfolioAsssetHistoricalReturn(@RequestParam(value = "clientId") int clientId) {
		log.info("ClientGoalController >>> Entering getPortfolioAsssetHistoricalReturn() ");
		Session session = null;
		PortFolioAssetAllocationReviewService portFolioAssetAllocationReviewService = new PortFolioAssetAllocationReviewServiceImpl();
		try {
			session = sessionfactory.openSession();
			List<PortfolioHistoricalReturnOutput> historicalReturns = portFolioAssetAllocationReviewService
					.getclientPortfolioHistoricalReturn(clientId, session,cacheInfoService);
			return new ResponseEntity<List<PortfolioHistoricalReturnOutput>>(historicalReturns, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			log.info("CLienGoalController <<< Exiting getPortfolioAsssetHistoricalReturn() ");
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);

		} finally {
			session.close();

		}

	}
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getProdRecoPM", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getProdRecoPM(@RequestParam(value = "clientId") int clientId, @RequestParam(value = "advisorId") int advisorId, HttpServletRequest request) {
		log.info("ClientGoalController >>> Entering getProdRecoPM() ");
		Session session = null;
		String header = "",token = "";
		PortFolioAssetAllocationReviewService portFolioAssetAllocationReviewService = new PortFolioAssetAllocationReviewServiceImpl();
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			List<ProductRecommendationOutputPM> productRecoList = portFolioAssetAllocationReviewService.getProduductRecoPM(token, clientId, advisorId, session, cacheInfoService);
			return new ResponseEntity<List<ProductRecommendationOutputPM>>(productRecoList, HttpStatus.OK);
		} catch (FinexaBussinessException busExcep) {
			FinexaBussinessException.logFinexaBusinessException(busExcep);
			return new ResponseEntity<String>(busExcep.getErrorDescription(), HttpStatus.OK);
		} catch (Exception exp) {
			log.info("CLienGoalController <<< Exiting getProdRecoPM() ");
			return new ResponseEntity<String>(exp.getMessage(), HttpStatus.OK);

		} finally {
			session.close();

		}

	}
	
}
