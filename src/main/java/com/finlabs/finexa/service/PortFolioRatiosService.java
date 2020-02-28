package com.finlabs.finexa.service;

import java.util.List;
import org.hibernate.Session;
import com.finlabs.finexa.dto.ClientRiskDTO;
import com.finlabs.finexa.dto.PortfolioRatiosDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;

public interface PortFolioRatiosService {

	
	public ClientRiskDTO getRiskOutput(String token, int clientId, Session session) throws FinexaBussinessException;

	public List<PortfolioRatiosDto> getclientPortfolioRatios(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException;

}
