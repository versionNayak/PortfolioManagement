package com.finlabs.finexa.service;

import org.hibernate.Session;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.PortfolioNetWorthDto;

public interface PortFolioNetWorthService {

	public PortfolioNetWorthDto getclientPortfolioNetWorth(String token, int clientId, Session session,CacheInfoService cacheInfoService)
			throws FinexaBussinessException;

}
