package com.finlabs.finexa.service;

import org.hibernate.Session;

import com.finlabs.finexa.dto.PortfolioPagerproductTemplateDto;
import com.finlabs.finexa.exception.FinexaBussinessException;

public interface PortFolioPagerTemplateMFService {
	

	public PortfolioPagerproductTemplateDto getclientMFOnePagerTemplate(String ISIN, String startDate, String endDate,
			Session session) throws FinexaBussinessException;
}
