package com.finlabs.finexa.service;

//import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.MasterdailynavDTO;
import com.finlabs.finexa.dto.PortfolioEquityDailyPriceDto;
import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
import com.finlabs.finexa.dto.PortfolioSubAssetBencmarkDto;
import com.finlabs.finexa.exception.FinexaBussinessException;

public interface PortFolioPagerTemplateService {

	public PortfolioPagerproductTemplateDto getclientStockOnePagerTemplate(String ISIN, Session session)
			throws FinexaBussinessException;

	public PortfolioPagerproductTemplateDto getclientMFOnePagerTemplateE(String AMFI, Session session)
			throws FinexaBussinessException;

	public PortfolioPagerproductTemplateDto getclientMFOnePagerTemplateD(String AMFI, Session session)
			throws FinexaBussinessException;
	
	public List<PortfolioEquityDailyPriceDto> getMasterEquityDailyPriceListOnDate(String ISIN, String startDate, String endDate,
			Session session) throws FinexaBussinessException;

	
	public List<MasterdailynavDTO> getMasterindexdailynavListOnDate(String name, String startDate, String endDate,
			Session session) throws FinexaBussinessException;
	
	public List<PortfolioSubAssetBencmarkDto> getMasterEquityDailyPriceListTimePeriod(String isin, String name, Session session)
			throws FinexaBussinessException;



	

	

}