package com.finlabs.finexa.service;

import org.hibernate.Session;

import com.finlabs.finexa.dto.PortfolioRecommendationDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;

public interface PortFolioRecommentdationService {

  public PortfolioRecommendationDto getclientPortfolioRecommendation(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException;

}
