package com.finlabs.finexa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.finlabs.finexa.dto.ClientFamilyLoanOutput;
import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.PortfolioNetWorthDto;
import com.finlabs.finexa.pm.util.FinexaConstant;
import com.finlabs.finexa.service.ClientLoanService;
import com.finlabs.finexa.service.PortFolioNetWorthService;
import com.finlabs.finexa.service.PortFolioOverviewService;

//@Transactional
//@Service
public class PortFolioNetWorthServiceImpl implements PortFolioNetWorthService {
	// private static Logger log =
	// LoggerFactory.getLogger(PortFolioNetWorthServiceImpl.class);
	// @Autowired
	// private PortFolioOverviewService portFolioOverviewService;

	// @Autowired
	// private ClientLoanService clientLoanService;

	@SuppressWarnings("unchecked")
	@Override
	public PortfolioNetWorthDto getclientPortfolioNetWorth(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException {

		PortfolioNetWorthDto portfolioNetWorthDto = new PortfolioNetWorthDto();
		List<PortfolioNetWorthDto> networthList = new ArrayList<>();
		PortFolioOverviewService portFolioOverviewService = new PortFolioOverviewServiceImpl();
		ClientLoanService clientLoanService = new ClientLoanServiceImpl();
		double totalPersonalAssets = 0.0, totalInvestmentsAssets = 0.0, totalAssets = 0.0;

		try {
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;

			portfolioOverviewDtoList = (List<PortfolioOverviewDto>) cacheInfoService.getCacheList(
					FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
					FinexaConstant.CALCULATION_SUB_TYPE_PORTFOLIO_CONSTANT);

			if (portfolioOverviewDtoList == null || portfolioOverviewDtoList.isEmpty()) {
				portfolioOverviewDtoList = portFolioOverviewService.getclientPortfolioOverview(token, clientId, "", session,
						cacheInfoService);
			//	System.out.println("portfolioOverviewDtoList "+portfolioOverviewDtoList.size());
			}

			Map<String, Map<String, Map<String, List<PortfolioNetWorthDto>>>> rootMap = new HashMap<>();
			Map<String, Map<String, List<PortfolioNetWorthDto>>> subrootMap = new HashMap<>();
			Map<String, List<PortfolioNetWorthDto>> subtypeInvestmentRootMap = new HashMap<>();
			Map<String, List<PortfolioNetWorthDto>> subtypePersonalRootMap = new HashMap<>();
			Map<String, List<PortfolioNetWorthDto>> subtypeLiabilitiesRootMap = new HashMap<>();
			Map<String, Map<String, List<PortfolioNetWorthDto>>> subrootLiabilitiesRootMap = new HashMap<>();
			List<ClientFamilyLoanOutput> clientLoanList = clientLoanService.getCLientFamilyAllLoans(clientId, 0,
					session);
			Map<String, Double> typeValueMap = new HashMap<>();
			Map<String, Double> personaltypeValueMap = new HashMap<>();
			Map<String, Double> totaltypeValueMap = new HashMap<>();

			for (PortfolioOverviewDto portfolioOverviewDto : portfolioOverviewDtoList) {
				if (portfolioOverviewDto.getProductId() == 38) {
					continue;
				}
				boolean subTypeAddFlag = true;
				PortfolioNetWorthDto portfoliochild1NetWorth = new PortfolioNetWorthDto();
				// portfoliochild1NetWorth.setRootTypeName("Assets");
         
				if (portfolioOverviewDto.getInvestmentOrPersonFlag() != null && portfolioOverviewDto.getInvestmentOrPersonFlag().equals("Y")) {
					portfoliochild1NetWorth.setProductType(portfolioOverviewDto.getProductType());

					portfolioOverviewDto.setInvestmentOrPerson("Investment");
					portfoliochild1NetWorth.setCurrentValue(portfolioOverviewDto.getCurrentValue());
					// portfolioInvestmentAssetDtoList.add(portfoliochild1NetWorth);
					totalInvestmentsAssets = totalInvestmentsAssets + portfolioOverviewDto.getCurrentValue();
					List<PortfolioNetWorthDto> networthDtoList = new ArrayList<>();
					List<PortfolioNetWorthDto> networthProductNameLevelDtoList = new ArrayList<>();
					if (subtypeInvestmentRootMap.get(portfolioOverviewDto.getAssetClassification()) != null) {
						Double currentValue = 0.0;
						networthDtoList = subtypeInvestmentRootMap.get(portfolioOverviewDto.getAssetClassification());
						for (PortfolioNetWorthDto portfolioNetWorth : new ArrayList<>(networthDtoList)) {
							PortfolioNetWorthDto portfolioproductNameNetWorth = new PortfolioNetWorthDto();
							if (portfolioNetWorth.getProductType().equals(portfolioOverviewDto.getProductType())) {
								if (typeValueMap.get(portfolioOverviewDto.getProductType()) != null
										&& typeValueMap.get(portfolioOverviewDto.getProductType()) != 0.0) {
									currentValue = typeValueMap.get(portfolioOverviewDto.getProductType());
									currentValue = currentValue + portfolioOverviewDto.getCurrentValue();
									typeValueMap.put(portfolioOverviewDto.getProductType(), currentValue);

								} else {
									if (portfolioOverviewDto.getProductType() != null) {
										typeValueMap.put(portfolioNetWorth.getProductType(),
												portfolioOverviewDto.getCurrentValue()
														+ portfolioNetWorth.getCurrentValue());

									}
								}
								portfolioNetWorth.setCurrentValue(currentValue);
								subTypeAddFlag = false;
								portfolioproductNameNetWorth.setProductName(portfolioOverviewDto.getProductDesc());
								portfolioproductNameNetWorth.setCurrentValue(portfolioOverviewDto.getCurrentValue());
								if (portfolioNetWorth.getNetworthDetails() != null
										&& !portfolioNetWorth.getNetworthDetails().isEmpty()) {

									networthProductNameLevelDtoList = portfolioNetWorth.getNetworthDetails();
								} else {
									networthProductNameLevelDtoList = new ArrayList<>();
								}
								networthProductNameLevelDtoList.add(portfolioproductNameNetWorth);
								portfolioNetWorth.setNetworthDetails(networthProductNameLevelDtoList);
							}

						}

						if (subTypeAddFlag) {
							if(typeValueMap.get(portfolioOverviewDto.getProductType()) != null) {
								double presentVal = typeValueMap.get(portfolioOverviewDto.getProductType());
								presentVal = presentVal + portfolioOverviewDto.getCurrentValue();
								typeValueMap.put(portfolioOverviewDto.getProductType(), presentVal);
							} else {
								typeValueMap.put(portfolioOverviewDto.getProductType(),
										portfolioOverviewDto.getCurrentValue());
							}
							PortfolioNetWorthDto portfolioproductNameNetWorth = new PortfolioNetWorthDto();
							portfolioproductNameNetWorth.setProductName(portfolioOverviewDto.getProductDesc());
							portfolioproductNameNetWorth.setCurrentValue(portfolioOverviewDto.getCurrentValue());
							networthProductNameLevelDtoList.clear();
							networthProductNameLevelDtoList.add(portfolioproductNameNetWorth);
							portfoliochild1NetWorth.setNetworthDetails(networthProductNameLevelDtoList);
							networthDtoList.add(portfoliochild1NetWorth);
						}
					} else {
						if(typeValueMap.get(portfolioOverviewDto.getProductType()) != null) {
							double presentVal = typeValueMap.get(portfolioOverviewDto.getProductType());
							presentVal = presentVal + portfolioOverviewDto.getCurrentValue();
							typeValueMap.put(portfolioOverviewDto.getProductType(), presentVal);
						} else {
							typeValueMap.put(portfolioOverviewDto.getProductType(),
									portfolioOverviewDto.getCurrentValue());
						}
						PortfolioNetWorthDto portfolioproductNameNetWorth = new PortfolioNetWorthDto();
						portfolioproductNameNetWorth.setProductName(portfolioOverviewDto.getProductDesc());
						portfolioproductNameNetWorth.setCurrentValue(portfolioOverviewDto.getCurrentValue());
						networthProductNameLevelDtoList.clear();
						networthProductNameLevelDtoList.add(portfolioproductNameNetWorth);
						portfoliochild1NetWorth.setNetworthDetails(networthProductNameLevelDtoList);
						networthDtoList.add(portfoliochild1NetWorth);
					}

					subtypeInvestmentRootMap.put(portfolioOverviewDto.getAssetClassification() == null ? ""
							: portfolioOverviewDto.getAssetClassification(), networthDtoList);
				} else {
					List<PortfolioNetWorthDto> networthProductNameLevelDtoList = new ArrayList<>();
					portfolioOverviewDto.setInvestmentOrPerson("Personal");
					List<PortfolioNetWorthDto> networthDtoList = new ArrayList<>();
					portfoliochild1NetWorth.setProductType(portfolioOverviewDto.getProductName());

					if (subtypePersonalRootMap.get(portfolioOverviewDto.getAssetClassification()) != null) {
						Double currentValue = 0.0;
						networthDtoList = subtypePersonalRootMap.get(portfolioOverviewDto.getAssetClassification());
						for (PortfolioNetWorthDto portfolioNetWorth : new ArrayList<>(networthDtoList)) {
							PortfolioNetWorthDto portfolioproductNameNetWorth = new PortfolioNetWorthDto();
							if (portfolioNetWorth.getProductType().equals(portfoliochild1NetWorth.getProductType())) {
								if (personaltypeValueMap.get(portfoliochild1NetWorth.getProductType()) != null
										&& personaltypeValueMap.get(portfoliochild1NetWorth.getProductType()) != 0.0) {
									currentValue = personaltypeValueMap.get(portfoliochild1NetWorth.getProductType());
									currentValue = currentValue + portfolioOverviewDto.getCurrentValue();
									personaltypeValueMap.put(portfoliochild1NetWorth.getProductType(), currentValue);
								} else {
									if (portfolioOverviewDto.getProductType() != null) {
										personaltypeValueMap.put(portfolioNetWorth.getProductType(),
												portfolioOverviewDto.getCurrentValue()
														+ portfolioNetWorth.getCurrentValue());
									}
								}
								portfolioNetWorth.setCurrentValue(currentValue);
								subTypeAddFlag = false;

								if (portfolioNetWorth.getNetworthDetails() != null
										&& !portfolioNetWorth.getNetworthDetails().isEmpty()) {

									networthProductNameLevelDtoList = portfolioNetWorth.getNetworthDetails();
								} else {
									networthProductNameLevelDtoList = new ArrayList<>();
								}
								portfolioproductNameNetWorth.setProductName(portfolioOverviewDto.getProductDesc());
								portfolioproductNameNetWorth.setCurrentValue(portfolioOverviewDto.getCurrentValue());
								networthProductNameLevelDtoList.add(portfolioproductNameNetWorth);
								portfolioNetWorth.setNetworthDetails(networthProductNameLevelDtoList);
							}

						}
						if (subTypeAddFlag) {
							personaltypeValueMap.put(portfoliochild1NetWorth.getProductType(),
									portfolioOverviewDto.getCurrentValue());
							PortfolioNetWorthDto portfolioproductNameNetWorth = new PortfolioNetWorthDto();
							portfolioproductNameNetWorth.setProductName(portfolioOverviewDto.getProductDesc());
							portfolioproductNameNetWorth.setCurrentValue(portfolioOverviewDto.getCurrentValue());
							networthProductNameLevelDtoList.clear();
							networthProductNameLevelDtoList.add(portfolioproductNameNetWorth);
							portfoliochild1NetWorth.setNetworthDetails(networthProductNameLevelDtoList);
							networthDtoList.add(portfoliochild1NetWorth);
						}
					} else {
						personaltypeValueMap.put(portfoliochild1NetWorth.getProductType(),
								portfolioOverviewDto.getCurrentValue());
						PortfolioNetWorthDto portfolioproductNameNetWorth = new PortfolioNetWorthDto();
						portfolioproductNameNetWorth.setProductName(portfolioOverviewDto.getProductDesc());
						portfolioproductNameNetWorth.setCurrentValue(portfolioOverviewDto.getCurrentValue());
						networthProductNameLevelDtoList.clear();
						networthProductNameLevelDtoList.add(portfolioproductNameNetWorth);
						portfoliochild1NetWorth.setNetworthDetails(networthProductNameLevelDtoList);
						networthDtoList.add(portfoliochild1NetWorth);
					}
					subtypePersonalRootMap.put(portfolioOverviewDto.getAssetClassification(), networthDtoList);
					totalPersonalAssets = totalPersonalAssets + portfolioOverviewDto.getCurrentValue();
				}

				networthList.add(portfoliochild1NetWorth);

				if (totaltypeValueMap.get(portfolioOverviewDto.getInvestmentOrPerson()) != null
						&& totaltypeValueMap.get(portfolioOverviewDto.getInvestmentOrPerson()) != 0.0) {
					Double currentValue = totaltypeValueMap.get(portfolioOverviewDto.getInvestmentOrPerson());
					totalAssets = totalAssets + portfolioOverviewDto.getCurrentValue();
					currentValue = currentValue + portfolioOverviewDto.getCurrentValue();
					totaltypeValueMap.put(portfolioOverviewDto.getInvestmentOrPerson(), currentValue);
				} else {
					if (portfolioOverviewDto.getInvestmentOrPerson() != null) {
						totaltypeValueMap.put(portfolioOverviewDto.getInvestmentOrPerson(),
								portfolioOverviewDto.getCurrentValue());
						totalAssets = totalAssets + portfolioOverviewDto.getCurrentValue();
					}
				}

			}

			List<PortfolioNetWorthDto> networthLiaibiltyDtoList = new ArrayList<>();

			for (ClientFamilyLoanOutput clientFamilyLoanOutput : clientLoanList) {
				PortfolioNetWorthDto portrtfolioLiabilityNetWorthDto = new PortfolioNetWorthDto();
				String loanType = "Loan " + clientFamilyLoanOutput.getLoanType();
				Double outstandPrincCurrentLoan = 0d;

				// if loan type available and original loan flag checking
				if (clientFamilyLoanOutput.getLoanType() != null) {
					if (clientFamilyLoanOutput.getLoanOriginalFlag() != null)
						if (clientFamilyLoanOutput.getLoanOriginalFlag().equals("Y")) {
							outstandPrincCurrentLoan = clientFamilyLoanOutput.getOriginalPrincipal();
						} else {
							outstandPrincCurrentLoan = clientFamilyLoanOutput.getOutstandingPrincipal();
						}
				}

				if (totaltypeValueMap.get(FinexaConstant.NETWORTH_HEADING_LIABILITY) != null
						&& totaltypeValueMap.get("Liabilities") != 0.0) {
					Double totaloutstandOrigPrinc = totaltypeValueMap.get(FinexaConstant.NETWORTH_HEADING_LIABILITY);
					totaloutstandOrigPrinc = totaloutstandOrigPrinc + outstandPrincCurrentLoan;
					totaltypeValueMap.put(FinexaConstant.NETWORTH_HEADING_LIABILITY, totaloutstandOrigPrinc);
				} else {
					totaltypeValueMap.put(FinexaConstant.NETWORTH_HEADING_LIABILITY, outstandPrincCurrentLoan);
				}

				portrtfolioLiabilityNetWorthDto.setProductName(clientFamilyLoanOutput.getLoanType());
				portrtfolioLiabilityNetWorthDto.setCurrentValue(outstandPrincCurrentLoan);
				networthLiaibiltyDtoList.add(portrtfolioLiabilityNetWorthDto);
				if (typeValueMap.get(loanType) != null && typeValueMap.get(loanType) != 0.0) {
					Double lastLoanValue = typeValueMap.get(loanType);
					outstandPrincCurrentLoan = lastLoanValue + outstandPrincCurrentLoan;
					typeValueMap.put("Loan " + clientFamilyLoanOutput.getLoanType(), outstandPrincCurrentLoan);
				} else {

					typeValueMap.put(loanType, outstandPrincCurrentLoan);

				}

				subtypeLiabilitiesRootMap.put("LOANS", networthLiaibiltyDtoList);
			}
			// Addition of Code By Debolina for differentiate between MF Equity and MF Fixed Income
			for (Map.Entry<String, List<PortfolioNetWorthDto>> obj : subtypeInvestmentRootMap.entrySet()) {
					List<PortfolioNetWorthDto> equityList = obj.getValue();
					for(PortfolioNetWorthDto dto : equityList) {
						double currentValue = dto.getCurrentValue();
						double reCheckCurrentValue = 0.0;
						List<PortfolioNetWorthDto> networthDetails = dto.getNetworthDetails();
						for (PortfolioNetWorthDto networthDetailsDTO : networthDetails) {
							reCheckCurrentValue = reCheckCurrentValue + networthDetailsDTO.getCurrentValue();
						}
						if(currentValue != reCheckCurrentValue) {
							dto.setCurrentValue(reCheckCurrentValue);
						}
					}
					
			}
			//End of Code
			rootMap = new HashMap<>();
			subrootMap.put(FinexaConstant.ASSET_TYPE_PERSONAL, subtypePersonalRootMap);
			subrootMap.put(FinexaConstant.ASSET_TYPE_INVESTMENT, subtypeInvestmentRootMap);
			subrootLiabilitiesRootMap.put(FinexaConstant.LIABILITIES, subtypeLiabilitiesRootMap);
			rootMap.put(FinexaConstant.NETWORTH_HEADING_ASSETS, subrootMap);
			rootMap.put(FinexaConstant.LIABILITIES, subrootLiabilitiesRootMap);
			portfolioNetWorthDto.setRootMap(rootMap);
			portfolioNetWorthDto.setTypeValueMap(typeValueMap);
			portfolioNetWorthDto.setTotaltypeValueMap(totaltypeValueMap);
			portfolioNetWorthDto.setTotalTypeValue(totalAssets);
			portfolioNetWorthDto.setNetworthValue(
					totalAssets - (totaltypeValueMap.get(FinexaConstant.NETWORTH_HEADING_LIABILITY) != null
							? totaltypeValueMap.get(FinexaConstant.NETWORTH_HEADING_LIABILITY)
							: 0));
			portfolioNetWorthDto.setClientID(clientId);

		} catch (Exception exp) {
            exp.printStackTrace();
			FinexaBussinessException finexaBuss = new FinexaBussinessException(FinexaConstant.PORTFOLIO_NETWORTH_MODULE,
					FinexaConstant.PORTFOLIO_NETWORTH_MODULE_CODE, FinexaConstant.PORTFOLIO_NETWORTH_MODULE_DESC, exp);
			FinexaBussinessException.logFinexaBusinessException(finexaBuss);
		}
		return portfolioNetWorthDto;
	}

}
