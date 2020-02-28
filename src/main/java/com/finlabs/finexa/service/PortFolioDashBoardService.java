package com.finlabs.finexa.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.PortfolioNetWorthDto;


public interface PortFolioDashBoardService {

	public List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiodAndAsset(String token, int clientId,
			String specificRequermentStat, int timePeriod, String assetValue, Session session,CacheInfoService cacheInfoService)
			throws FinexaBussinessException, ParseException;

	public List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiod(int clientId, String specificRequermentStat,
			int timePeriod, Session session,CacheInfoService cacheInfoService) throws FinexaBussinessException, ParseException;

	public List<PortfolioNetWorthDto> getClientNetworthByAdvisorUserIDviewOnDemand(int advisorUserID, Session session,
			CacheInfoService cacheInfoService, HttpServletRequest request) throws FinexaBussinessException;

	List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiod(String token, int clientId,
			String specificRequermentStat, int timePeriod, Session session, CacheInfoService cacheInfoService)
			throws FinexaBussinessException, ParseException;

	public List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiodAndAssetForAdvisor(String token, int advisorUserId,
			String specificRequermentStat, String assetValue, int timePeriod, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException, ParseException;

	List<PortfolioOverviewDto> getclientPortfolioOverviewTimeperiodForAdvisor(String token, int advisorUserId,
			String specificRequermentStat, int timePeriod, Session session, CacheInfoService cacheInfoService)
			throws ParseException, FinexaBussinessException;

	List<PortfolioNetWorthDto> getClientNetworthDownloadByAdvisorUserID(String token, int advisorUserID,
			Session session, CacheInfoService cacheInfoService) throws FinexaBussinessException;

	
}
