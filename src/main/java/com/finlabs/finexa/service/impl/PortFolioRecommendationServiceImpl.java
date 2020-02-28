package com.finlabs.finexa.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.finlabs.finexa.dto.PortfolioAssetAllocationReviewDto;
import com.finlabs.finexa.dto.PortfolioRecommendationAssetAllocationDto;
import com.finlabs.finexa.dto.PortfolioRecommendationDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.service.PortFolioAssetAllocationReviewService;
import com.finlabs.finexa.service.PortFolioRecommentdationService;

//@Transactional
//@Service
public class PortFolioRecommendationServiceImpl implements PortFolioRecommentdationService {

	/*
	 * @Autowired private PortFolioOverviewService portFolioOverviewService;
	 */

	/*
	 * @Autowired private PortFolioAssetAllocationReviewService
	 * portFolioAssetAllocationReviewService;
	 */
	/*
	 * @Autowired private PortFolioRecommendationAssetDAO
	 * portFolioRecommendationAssetDAO;
	 */

	@Override
	public PortfolioRecommendationDto getclientPortfolioRecommendation(String token, int clientId, Session session,CacheInfoService cacheInfoService)
			throws FinexaBussinessException {

		PortfolioRecommendationDto portfolioRecommendationDto = new PortfolioRecommendationDto();
		PortFolioAssetAllocationReviewService portFolioAssetAllocationReviewService = new PortFolioAssetAllocationReviewServiceImpl();
		List<PortfolioRecommendationAssetAllocationDto> portfolioRecommendationAssetAllocationDtoList = new ArrayList<>();
		try {
			PortfolioAssetAllocationReviewDto portfolioAssetAllocationReviewDto = portFolioAssetAllocationReviewService
					.getclientPortfolioSubAssetAllocationReview(token, clientId, session,cacheInfoService);
			Map<String, List<PortfolioAssetAllocationReviewDto>> portfolioAssetListMap = portfolioAssetAllocationReviewDto
					.getPortfolioAssetListMap();
			for (Map.Entry<String, List<PortfolioAssetAllocationReviewDto>> mapEntry : portfolioAssetListMap
					.entrySet()) {
				for (PortfolioAssetAllocationReviewDto portfolioAssetAlloReviewDto : mapEntry.getValue()) {
					PortfolioRecommendationAssetAllocationDto portfolioRecommendationAssetAllocationDto = new PortfolioRecommendationAssetAllocationDto();
					portfolioRecommendationAssetAllocationDto
							.setAssetClass(portfolioAssetAlloReviewDto.getInvestmentAssetClass());
					portfolioRecommendationAssetAllocationDto
							.setSubAssetClass(portfolioAssetAlloReviewDto.getInvestmentSubAssetClass());
					portfolioRecommendationAssetAllocationDto
							.setAllocation(portfolioAssetAlloReviewDto.getPortFoliototalPercentage());
					if ((portfolioAssetAlloReviewDto.getRecomentTotalPercentage()
									- portfolioAssetAlloReviewDto.getPortFoliototalPercentage())
									* portfolioAssetAlloReviewDto.getCurrentValue() < 0) {
						portfolioRecommendationAssetAllocationDto.setRebalancingAmount(0.00);
					} else {
						portfolioRecommendationAssetAllocationDto
						.setRebalancingAmount((portfolioAssetAlloReviewDto.getRecomentTotalPercentage()
								- portfolioAssetAlloReviewDto.getPortFoliototalPercentage())
								* portfolioAssetAlloReviewDto.getCurrentValue());
					}
					// list all products under sub asset class
					List<String> recommendeMasterList = new ArrayList<>();
					/*
					 * List<MasterProductRecommendation> recomedMasterList =
					 * portFolioRecommendationAssetDAO .geMasterProductRecommendationList(
					 * portfolioAssetAlloReviewDto.getSubAssetClassId());
					 * 
					 * for (MasterProductRecommendation masterProductRecommendation :
					 * recomedMasterList) { recommendeMasterList.add(masterProductRecommendation.
					 * getProduct()); }
					 */
					portfolioRecommendationAssetAllocationDto.setRecommendedMasterList(recommendeMasterList);
					portfolioRecommendationAssetAllocationDtoList.add(portfolioRecommendationAssetAllocationDto);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*
			 * finally { genericDao.closeCurrentSession(); }
			 */
		portfolioRecommendationDto
				.setPortfolioRecommendationAssetAllocationDtoList(portfolioRecommendationAssetAllocationDtoList);

		return portfolioRecommendationDto;
	}

}
