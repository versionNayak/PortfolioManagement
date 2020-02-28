package com.finlabs.finexa.service;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.PortfolioAssetAllocationReviewDto;
import com.finlabs.finexa.dto.PortfolioHistoricalReturnOutput;
import com.finlabs.finexa.dto.ProductRecommendationOutputPM;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;

public interface PortFolioAssetAllocationReviewService {

	public List<PortfolioAssetAllocationReviewDto> getclientPortfolioAssetAllocationReview(String token, int clientId,
			Session session, CacheInfoService cacheInfoService) throws FinexaBussinessException;

	public PortfolioAssetAllocationReviewDto getclientPortfolioSubAssetAllocationReview(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException;

	public List<PortfolioHistoricalReturnOutput> getclientPortfolioHistoricalReturn(int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException;

	public List<ProductRecommendationOutputPM> getProduductRecoPM(String token, int clientId, int advisorId, Session session, CacheInfoService cacheInfoService) throws FinexaBussinessException;

}
