package com.finlabs.finexa.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.finlabs.finexa.dto.ClientRiskDTO;
import com.finlabs.finexa.dto.PortfolioRatiosDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterWealthRatioComment;
import com.finlabs.finexa.model.PortfolioNetWorthDto;
import com.finlabs.finexa.repository.PortFolioAssetAllocationReviewDAO;
import com.finlabs.finexa.repository.PortFolioRatiosDAO;
import com.finlabs.finexa.repository.impl.PortFolioAssetAllocationReviewDAOImpl;
import com.finlabs.finexa.repository.impl.PortFolioRatiosDAOImpl;
import com.finlabs.finexa.service.PortFolioNetWorthService;
import com.finlabs.finexa.service.PortFolioRatiosService;

//@Transactional
//@Service
public class PortFolioRatiosServiceImpl implements PortFolioRatiosService {
	// @Autowired
	// private PortFolioNetWorthService portFolioNetWorthService;
	/*
	 * @Autowired private PortFolioRatiosDAO portFolioRatiosDAO;
	 */
	private int riskProfileGlobal = 0;
	@Override
	public List<PortfolioRatiosDto> getclientPortfolioRatios(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<PortfolioRatiosDto> portfolioRatiosDtoList = new ArrayList<>();
		PortFolioNetWorthService portFolioNetWorthService = new PortFolioNetWorthServiceImpl();
		PortFolioRatiosDAO portFolioRatiosDAO = new PortFolioRatiosDAOImpl();
		try {

			PortfolioRatiosDto portfolioRatiosDto1 = new PortfolioRatiosDto();
			PortfolioRatiosDto portfolioRatiosDto2 = new PortfolioRatiosDto();
			PortfolioRatiosDto portfolioRatiosDto3 = new PortfolioRatiosDto();
			PortfolioRatiosDto portfolioRatiosDto4 = new PortfolioRatiosDto();

			PortfolioNetWorthDto portfolioNetworthDto = null;
			portfolioNetworthDto = portFolioNetWorthService.getclientPortfolioNetWorth(token, clientId, session,
					cacheInfoService);

			Map<String, Double> totaltypeValueMap = portfolioNetworthDto.getTotaltypeValueMap();
			Double totalPersonalValue = totaltypeValueMap.get("Personal") == null ? 0d
					: totaltypeValueMap.get("Personal");
			Double totalInvestmentValue = totaltypeValueMap.get("Investment") == null ? 0d
					: totaltypeValueMap.get("Investment");
			Double totalLiabilities = totaltypeValueMap.get("Liabilities") == null ? 0d
					: totaltypeValueMap.get("Liabilities");
			portfolioRatiosDto1.setRatioId(2);
			portfolioRatiosDto1.setLogicRationalId(2);
			portfolioRatiosDto1
			.setValue((double) (totalPersonalValue / (double) (totalPersonalValue + totalInvestmentValue)));

			portfolioRatiosDto2.setRatioId(3);
			portfolioRatiosDto2.setLogicRationalId(3);
			portfolioRatiosDto2
			.setValue((double) (totalInvestmentValue / (double) (totalPersonalValue + totalInvestmentValue)));

			portfolioRatiosDto3.setRatioId(4);
			portfolioRatiosDto3.setLogicRationalId(4);
			portfolioRatiosDto3
			.setValue((double) (totalLiabilities / (double) (totalPersonalValue + totalInvestmentValue)));
			// done in order if debt to asset ratio is greater than 100 then 100 comment will be taken
			if (portfolioRatiosDto3.getValue() > 1) {
				portfolioRatiosDto3.setValue((double)1);
			}
			portfolioRatiosDto4.setRatioId(7);
			portfolioRatiosDto4.setLogicRationalId(7);
			portfolioRatiosDto4.setValue(((double) portfolioNetworthDto.getNetworthValue()
					/ (double) portfolioNetworthDto.getTotalTypeValue()));
			// portfolioRatiosDto4.setCommentMaster("Stressed");

			List<MasterWealthRatioComment> masterWealthCommentList = portFolioRatiosDAO
					.geMasterWealthCommentList(session);

			// **************** please uncommit this part after proper matching
			// with model******************//*
			for (MasterWealthRatioComment masterWealthRatioCommen : masterWealthCommentList) {
				if (masterWealthRatioCommen.getMasterWealthRatio().getId() == portfolioRatiosDto1.getRatioId()
						&& masterWealthRatioCommen.getMasterWealthRatioLogicRationale().getId() == portfolioRatiosDto1
						.getLogicRationalId()
						&& (masterWealthRatioCommen.getFromRange().doubleValue() <= portfolioRatiosDto1.getValue()
						&& masterWealthRatioCommen.getToRange().doubleValue() >= portfolioRatiosDto1
						.getValue())) {
					portfolioRatiosDto1.setCommentMaster(masterWealthRatioCommen.getCommentMaster());
					portfolioRatiosDto1.setRatios(masterWealthRatioCommen.getMasterWealthRatio().getRatio());
					portfolioRatiosDto1.setLogicRational(
							masterWealthRatioCommen.getMasterWealthRatioLogicRationale().getLogicRationale());
				}
				if (masterWealthRatioCommen.getMasterWealthRatio().getId() == portfolioRatiosDto2.getRatioId()
						&& masterWealthRatioCommen.getMasterWealthRatioLogicRationale().getId() == portfolioRatiosDto2
						.getLogicRationalId()
						&& (masterWealthRatioCommen.getFromRange().doubleValue() <= portfolioRatiosDto2.getValue()
						&& masterWealthRatioCommen.getToRange().doubleValue() >= portfolioRatiosDto2
						.getValue())) {
					portfolioRatiosDto2.setCommentMaster(masterWealthRatioCommen.getCommentMaster());
					portfolioRatiosDto2.setRatios(masterWealthRatioCommen.getMasterWealthRatio().getRatio());
					portfolioRatiosDto2.setLogicRational(
							masterWealthRatioCommen.getMasterWealthRatioLogicRationale().getLogicRationale());
				}
				if (masterWealthRatioCommen.getMasterWealthRatio().getId() == portfolioRatiosDto3.getRatioId()
						&& masterWealthRatioCommen.getMasterWealthRatioLogicRationale().getId() == portfolioRatiosDto3
						.getLogicRationalId()
						&& (masterWealthRatioCommen.getFromRange().doubleValue() <= portfolioRatiosDto3.getValue()
						&& masterWealthRatioCommen.getToRange().doubleValue() >= portfolioRatiosDto3
						.getValue())) {
					portfolioRatiosDto3.setCommentMaster(masterWealthRatioCommen.getCommentMaster());
					portfolioRatiosDto3.setRatios(masterWealthRatioCommen.getMasterWealthRatio().getRatio());
					portfolioRatiosDto3.setLogicRational(
							masterWealthRatioCommen.getMasterWealthRatioLogicRationale().getLogicRationale());
				}
				if (masterWealthRatioCommen.getMasterWealthRatio().getId() == portfolioRatiosDto4.getRatioId()
						&& masterWealthRatioCommen.getMasterWealthRatioLogicRationale().getId() == portfolioRatiosDto4
						.getLogicRationalId()
						&& (masterWealthRatioCommen.getFromRange().doubleValue() <= portfolioRatiosDto4.getValue()
						&& masterWealthRatioCommen.getToRange().doubleValue() >= portfolioRatiosDto4
						.getValue())) {
					portfolioRatiosDto4.setCommentMaster(masterWealthRatioCommen.getCommentMaster());
					portfolioRatiosDto4.setRatios(masterWealthRatioCommen.getMasterWealthRatio().getRatio());
					portfolioRatiosDto4.setLogicRational(
							masterWealthRatioCommen.getMasterWealthRatioLogicRationale().getLogicRationale());
				}

			}
			portfolioRatiosDto3
			.setValue((double) (totalLiabilities / (double) (totalPersonalValue + totalInvestmentValue)));
		
			portfolioRatiosDtoList.add(portfolioRatiosDto1);
			portfolioRatiosDtoList.add(portfolioRatiosDto2);
			portfolioRatiosDtoList.add(portfolioRatiosDto3);
			portfolioRatiosDtoList.add(portfolioRatiosDto4);

		} catch (FinexaDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return portfolioRatiosDtoList;
	}

	@Override
	public ClientRiskDTO getRiskOutput(String token, int clientId, Session session) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		ClientRiskDTO riskOutput = new ClientRiskDTO();
		ClientMaster clientMaster = null;
		int riskProfile = 0;
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		try {
			clientMaster = portFolioAssetAllocationReviewDAO.getClientById(clientId, session);
			if (clientMaster != null) {
				if (clientMaster.getRiskProfile() != null) {
					riskProfile = clientMaster.getRiskProfile();
				}
			}
			riskProfileGlobal = riskProfile;
			riskOutput.setClientId(clientId);
			riskOutput.setRiskProfile(riskProfile);
		} catch (FinexaDaoException e) {
			throw new FinexaBussinessException("", "", "");
		}

		return riskOutput;
	}
	
}
