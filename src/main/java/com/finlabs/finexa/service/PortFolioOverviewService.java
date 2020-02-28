package com.finlabs.finexa.service;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.PortfolioOvervieEquityDto;
import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;

public interface PortFolioOverviewService {

	public List<PortfolioOverviewDto> getclientPortfolioOverview(String token, int clientId, String specificRequermentStat,
			Session session,CacheInfoService cacheInfoService) throws FinexaBussinessException;

	public PortfolioOvervieEquityDto getclientPortfolioOverviewEquity(String token, int clientId, Session session,CacheInfoService cacheInfoService)
			throws FinexaBussinessException;

	public PortfolioOvervieEquityDto getclientPortfolioOverviewFixedIncome(String token, int clientId, Session session,CacheInfoService cacheInfoService)
			throws FinexaBussinessException;

	
	public List<PortfolioOverviewDto> getclientPortfolioOverviewKV(int clientId, String specificRequermentStat,
			Session session, CacheInfoService cacheInfoService) throws FinexaBussinessException;

}
