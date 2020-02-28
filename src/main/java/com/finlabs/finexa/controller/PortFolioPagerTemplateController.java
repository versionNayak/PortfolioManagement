package com.finlabs.finexa.controller;

import java.util.List;
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

import com.finlabs.finexa.dto.MasterdailynavDTO;
import com.finlabs.finexa.dto.PortfolioEquityDailyPriceDto;
import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
import com.finlabs.finexa.dto.PortfolioSubAssetBencmarkDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.service.PortFolioPagerTemplateMFService;
import com.finlabs.finexa.service.PortFolioPagerTemplateService;
import com.finlabs.finexa.service.PortFolioPagerTemplateStockService;
import com.finlabs.finexa.service.impl.PortFolioPagerTemplateMFServiceImpl;
import com.finlabs.finexa.service.impl.PortFolioPagerTemplateServiceImpl;
import com.finlabs.finexa.service.impl.PortFolioPagerTemplateStockServiceImpl;

@RestController
public class PortFolioPagerTemplateController {
	// @Autowired
	// private PortFolioPagerTemplateService portFolioPagerTemplateService;
	Logger log = Logger.getLogger(PortFolioPagerTemplateController.class.getName());
	@Autowired
	private SessionFactory sessionfactory;

	/*@RequestMapping(value = "/getClientStockOnePager", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioAssetReview(@RequestParam(value = "ISIN") String ISIN) {
		log.info("PortFolioPagerTemplateController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		PortFolioPagerTemplateService portFolioPagerTemplateService = new PortFolioPagerTemplateServiceImpl();
		try {
			session = sessionfactory.openSession();
			PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = null;
			portfolioPagerproductTemplateDto = portFolioPagerTemplateService.getclientStockOnePagerTemplate(ISIN,
					session);
			log.info("PortFolioPagerTemplateController <<< Exiting getClientPortfolioAssetReview() ");
			return new ResponseEntity<PortfolioPagerproductTemplateDto>(portfolioPagerproductTemplateDto,
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
*/
/*	@RequestMapping(value = "/getClientMFOnePagerE", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientMFOnePagerE(@RequestParam(value = "ISIN") String ISIN) {
		log.info("PortFolioPagerTemplateController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		PortFolioPagerTemplateService portFolioPagerTemplateService = new PortFolioPagerTemplateServiceImpl();
		try {
			session = sessionfactory.openSession();
			PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = null;
			portfolioPagerproductTemplateDto = portFolioPagerTemplateService.getclientMFOnePagerTemplateE(ISIN,
					session);
			log.info("PortFolioPagerTemplateController <<< Exiting getClientPortfolioAssetReview() ");
			return new ResponseEntity<PortfolioPagerproductTemplateDto>(portfolioPagerproductTemplateDto,
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

	@RequestMapping(value = "/getClientMFOnePagerD", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientMFOnePagerD(@RequestParam(value = "ISIN") String ISIN) {
		log.info("PortFolioPagerTemplateController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		PortFolioPagerTemplateService portFolioPagerTemplateService = new PortFolioPagerTemplateServiceImpl();
		try {
			session = sessionfactory.openSession();
			PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = null;
			portfolioPagerproductTemplateDto = portFolioPagerTemplateService.getclientMFOnePagerTemplateE(ISIN,
					session);
			log.info("PortFolioPagerTemplateController <<< Exiting getClientPortfolioAssetReview() ");
			return new ResponseEntity<PortfolioPagerproductTemplateDto>(portfolioPagerproductTemplateDto,
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

	}*/
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getClientStockOnePager", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientPortfolioAssetReview(@RequestParam(value = "ISIN") String ISIN,@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate) {
		log.info("PortFolioPagerTemplateController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		PortFolioPagerTemplateStockService portFolioPagerTemplateStockService = new PortFolioPagerTemplateStockServiceImpl();
		try {
			session = sessionfactory.openSession();
			PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = null;
			String name = "NIFTY 50";
			portfolioPagerproductTemplateDto = portFolioPagerTemplateStockService.getclientStockOnePagerTemplate(ISIN, name, startDate, endDate, session);
			log.info("PortFolioPagerTemplateController <<< Exiting getClientPortfolioAssetReview() ");
			return new ResponseEntity<PortfolioPagerproductTemplateDto>(portfolioPagerproductTemplateDto,
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
	@PreAuthorize("hasAnyRole('Admin', 'PortFolioManagementView')")
	@RequestMapping(value = "/getClientMFOnePager", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getClientMFOnePager(@RequestParam(value = "ISIN") String ISIN,@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate) {
		log.info("PortFolioPagerTemplateController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		PortFolioPagerTemplateMFService portFolioPagerTemplateMFService = new PortFolioPagerTemplateMFServiceImpl();
		try {
			session = sessionfactory.openSession();
			PortfolioPagerproductTemplateDto portfolioPagerproductTemplateDto = null;
			
			portfolioPagerproductTemplateDto = portFolioPagerTemplateMFService.getclientMFOnePagerTemplate(ISIN, startDate, endDate, session);
			log.info("PortFolioPagerTemplateController <<< Exiting getClientPortfolioAssetReview() ");
			return new ResponseEntity<PortfolioPagerproductTemplateDto>(portfolioPagerproductTemplateDto,
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
	
	
/*	@RequestMapping(value = "/getMasterEquityDailyPriceListOnDate", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getMasterEquityDailyPriceListOnDate(@RequestParam(value = "ISIN") String ISIN, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate) {
		log.info("PortFolioPagerTemplateController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		PortFolioPagerTemplateService portFolioPagerTemplateService = new PortFolioPagerTemplateServiceImpl();
		try {
			session = sessionfactory.openSession();
			
			List<PortfolioEquityDailyPriceDto> portfolioEquityDailyPriceDtoList = portFolioPagerTemplateService.getMasterEquityDailyPriceListOnDate(ISIN, startDate, endDate, session);
			log.info("PortFolioPagerTemplateController <<< Exiting getClientPortfolioAssetReview() ");
			System.out.println("masterEquityDailyPriceList "+portfolioEquityDailyPriceDtoList.size());
			return new ResponseEntity<List<PortfolioEquityDailyPriceDto>>(portfolioEquityDailyPriceDtoList,
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
	
	@RequestMapping(value = "/getMasterindexdailynavListOnDate", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getMasterindexdailynavListOnDate(@RequestParam(value = "name") String name, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate) {
		log.info("PortFolioPagerTemplateController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		PortFolioPagerTemplateService portFolioPagerTemplateService = new PortFolioPagerTemplateServiceImpl();
		try {
			session = sessionfactory.openSession();
			
			List<MasterdailynavDTO> masterindexdailynavDTOList = portFolioPagerTemplateService.getMasterindexdailynavListOnDate(name, startDate, endDate, session);
			log.info("PortFolioPagerTemplateController <<< Exiting getClientPortfolioAssetReview() ");
			System.out.println("masterindexdailynavDTOList "+masterindexdailynavDTOList.size());
			return new ResponseEntity<List<MasterdailynavDTO>>(masterindexdailynavDTOList,
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

	@RequestMapping(value = "/getMasterEquityDailyPriceListTimePeriod", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getMasterEquityDailyPriceListTimePeriod(@RequestParam(value = "ISIN") String isin, @RequestParam(value = "name") String name) {
		log.info("PortFolioPagerTemplateController >>> Entering getClientPortfolioAssetReview() ");
		Session session = null;
		PortFolioPagerTemplateService portFolioPagerTemplateService = new PortFolioPagerTemplateServiceImpl();
		PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto;
		try {
			session = sessionfactory.openSession();
			
			List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList = portFolioPagerTemplateService.getMasterEquityDailyPriceListTimePeriod(isin, name, session);
			log.info("PortFolioPagerTemplateController <<< Exiting getClientPortfolioAssetReview() ");
			System.out.println("masterindexdailynavDTOList "+portfolioSubAssetBencmarkDtoList.size());
			return new ResponseEntity<List<PortfolioSubAssetBencmarkDto>>(portfolioSubAssetBencmarkDtoList,
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

	}*/

}