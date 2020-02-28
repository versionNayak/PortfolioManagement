package com.finlabs.finexa.service;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
import com.finlabs.finexa.dto.PortfolioSubAssetBencmarkDto;
import com.finlabs.finexa.exception.FinexaBussinessException;


public interface PortFolioPagerTemplateStockService {
	

	public PortfolioPagerproductTemplateDto getclientStockOnePagerTemplate(String ISIN, String name, String startDate,
			String endDate, Session session) throws FinexaBussinessException;

	public PortfolioSubAssetBencmarkDto getStockListTimePeriod(String ISIN, Calendar currentDate, Calendar month1,
			Calendar month3, Calendar month6, Calendar year1, Calendar year3, Calendar year5, Session session)
			throws FinexaBussinessException;

}
