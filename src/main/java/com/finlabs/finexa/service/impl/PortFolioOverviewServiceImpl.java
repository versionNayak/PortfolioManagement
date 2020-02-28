package com.finlabs.finexa.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Session;

import com.finlabs.finexa.dto.PortfolioOvervieEquityDto;
import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.dto.PortfolioOverviewEquitySectorDto;
import com.finlabs.finexa.dto.PortfolioSubAssetBencmarkDto;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.exception.FinexaServiceException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientCash;
import com.finlabs.finexa.model.ClientEPF;
import com.finlabs.finexa.model.ClientEquity;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientLumpsumInflow;
import com.finlabs.finexa.model.ClientMutualFund;
import com.finlabs.finexa.model.ClientNPS;
import com.finlabs.finexa.model.ClientOtherAlternateAsset;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.ClientPreciousMetal;
import com.finlabs.finexa.model.ClientRealEstate;
import com.finlabs.finexa.model.ClientSmallSaving;
import com.finlabs.finexa.model.ClientStructuredProduct;
import com.finlabs.finexa.model.ClientVehicle;
import com.finlabs.finexa.model.MasterDirectEquity;
import com.finlabs.finexa.model.MasterEquityMarketCapClassification;
import com.finlabs.finexa.model.MasterIndexDailyNAV;
import com.finlabs.finexa.model.MasterMFHoldingRatingWise;
import com.finlabs.finexa.model.MasterMFHoldingSectorWise;
import com.finlabs.finexa.model.MasterMFMarketCap;
import com.finlabs.finexa.model.MasterMFMaturityProfile;
import com.finlabs.finexa.model.MasterMFRatio;
import com.finlabs.finexa.model.MasterMutualFundETF;
import com.finlabs.finexa.model.MasterProductClassification;
import com.finlabs.finexa.model.MasterProductRecommendationRank;
import com.finlabs.finexa.model.MasterSectorBenchmarkMapping;
import com.finlabs.finexa.pm.util.FinanceUtil;
import com.finlabs.finexa.pm.util.FinexaConstant;
import com.finlabs.finexa.repository.PortFolioAssetAllocationReviewDAO;
import com.finlabs.finexa.repository.PortFolioOverviewDAO;
import com.finlabs.finexa.repository.impl.PortFolioAssetAllocationReviewDAOImpl;
import com.finlabs.finexa.repository.impl.PortFolioOverviewDAOImpl;
import com.finlabs.finexa.resources.model.Annuity2ProductCalculator;
import com.finlabs.finexa.resources.model.AtalPensionYojana;
import com.finlabs.finexa.resources.model.AtalPensionYojanaLookup;
import com.finlabs.finexa.resources.model.BankBondDebenturesLookup;
import com.finlabs.finexa.resources.model.BankFDSTDRCDCP;
import com.finlabs.finexa.resources.model.BankFDSTDRCDCPLookup;
import com.finlabs.finexa.resources.model.BankFDTDRLookup;
import com.finlabs.finexa.resources.model.BankFDTDRPC;
import com.finlabs.finexa.resources.model.BankRecurringDeposit;
import com.finlabs.finexa.resources.model.BankRecurringDepositLookup;
import com.finlabs.finexa.resources.model.BondDebentures;
import com.finlabs.finexa.resources.model.EPF2Calculator;
import com.finlabs.finexa.resources.model.EPF2Lookup;
import com.finlabs.finexa.resources.model.EquityCalculator;
import com.finlabs.finexa.resources.model.EquityCalculatorLookup;
import com.finlabs.finexa.resources.model.KisanVikasPatra;
import com.finlabs.finexa.resources.model.KisanVikasPatraLookup;
import com.finlabs.finexa.resources.model.MutualFundLumpsumSip;
import com.finlabs.finexa.resources.model.MutualFundLumpsumSipLookup;
import com.finlabs.finexa.resources.model.NPSCAL;
import com.finlabs.finexa.resources.model.NPSLookup;
import com.finlabs.finexa.resources.model.PONSC;
import com.finlabs.finexa.resources.model.PONSCLookup;
import com.finlabs.finexa.resources.model.PORecurringDeposit;
import com.finlabs.finexa.resources.model.PORecurringDepositLookup;
import com.finlabs.finexa.resources.model.POTimeDeposit;
import com.finlabs.finexa.resources.model.POTimeDepositLookup;
import com.finlabs.finexa.resources.model.PPFFixedAmountDeposit;
import com.finlabs.finexa.resources.model.PPFFixedAmountLookup;
import com.finlabs.finexa.resources.model.PerpetualBond;
import com.finlabs.finexa.resources.model.PerpetualBondLookup;
import com.finlabs.finexa.resources.model.PostOfficeMonthlyIncomeScheme;
import com.finlabs.finexa.resources.model.PostOfficeMonthlyIncomeSchemeLookup;
import com.finlabs.finexa.resources.model.SeniorCitizenSavingScheme;
import com.finlabs.finexa.resources.model.SeniorCitizenSavingSchemeLookup;
import com.finlabs.finexa.resources.model.SukanyaSamriddhiScheme;
import com.finlabs.finexa.resources.model.SukanyaSamriddhiSchemeLookup;
import com.finlabs.finexa.resources.model.ZeroCouponBond;
import com.finlabs.finexa.resources.model.ZeroCouponBondLookup;
import com.finlabs.finexa.resources.service.Annuity2ProductService;
import com.finlabs.finexa.resources.service.AtalPensionYojanaService;
import com.finlabs.finexa.resources.service.BankFDSTDRCDCPService;
import com.finlabs.finexa.resources.service.BankFDTDRService;
import com.finlabs.finexa.resources.service.BankRecurringDespositService;
import com.finlabs.finexa.resources.service.BondDebenturesService;
import com.finlabs.finexa.resources.service.EPF2Service;
import com.finlabs.finexa.resources.service.EquityCalculatorService;
import com.finlabs.finexa.resources.service.KisanVikasPatraService;
import com.finlabs.finexa.resources.service.MutualFundLumpsumSipService;
import com.finlabs.finexa.resources.service.NPSCalService;
import com.finlabs.finexa.resources.service.PONSCService;
import com.finlabs.finexa.resources.service.PORecurringDespositService;
import com.finlabs.finexa.resources.service.POTimeDespositService;
import com.finlabs.finexa.resources.service.PPFFixedAmountService;
import com.finlabs.finexa.resources.service.PerpetualBondService;
import com.finlabs.finexa.resources.service.PostOfficeMonthlyIncomeSchemeService;
import com.finlabs.finexa.resources.service.SeniorCitizenSavingSchemeService;
import com.finlabs.finexa.resources.service.SukanyaSamriddhiSchemeService;
import com.finlabs.finexa.resources.service.ZeroCouponService;
import com.finlabs.finexa.resources.util.FinexaDateUtil;
import com.finlabs.finexa.service.PortFolioOverviewService;


//@Transactional
//@Service
public class PortFolioOverviewServiceImpl implements PortFolioOverviewService {
	// private static Logger log =
	// LoggerFactory.getLogger(PortFolioOverviewService.class);
	// @Autowired
	// private PortFolioOverviewDAO portFolioOverviewDAO;

	private Calendar currentDate = Calendar.getInstance();
	private Calendar variableCal = Calendar.getInstance();
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	private double lastValue = 0.0;
	private double epsBalAtage = 0.0;

	/*
	 * @Autowired private MasterProductClassification masterProductClassification;
	 */

	/*
	 * @Autowired private PortFolioAssetAllocationReviewDAO
	 * portFolioAssetAllocationReviewDAO;
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<PortfolioOverviewDto> getclientPortfolioOverview(String token, int clientId, String specificRequermentStat,
			Session session, CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<PortfolioOverviewDto> outputList = null;
		PortFolioOverviewDAO portFolioOverviewDAO = new PortFolioOverviewDAOImpl();
		MasterProductClassification masterProductClassification = new MasterProductClassification();
		try {
			outputList = (List<PortfolioOverviewDto>) cacheInfoService.getCacheList(
					FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
					FinexaConstant.CALCULATION_SUB_TYPE_PORTFOLIO_CONSTANT);

			if (outputList == null || outputList.isEmpty()) {
				outputList = new ArrayList<>();
				double totalCurrentValue = 0.0;
				
				// Direct Equity // International Equity
				try {
					List<ClientEquity> portFolioEquityList = portFolioOverviewDAO
							.getClientPortFolioEquityOverview(clientId, session);
					for (ClientEquity clientEquity : portFolioEquityList) {
						try {
							masterProductClassification = clientEquity.getMasterProductClassification();
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							EquityCalculatorService ecs = new EquityCalculatorService();
							EquityCalculatorLookup ec = new EquityCalculatorLookup();
							output.setProductId(clientEquity.getId());
							output.setProductName(masterProductClassification.getProductName());
							//gourab
							output.setBankIssuerName(clientEquity.getMasterDirectEquity() == null ?clientEquity.getUnlistedStockName() : clientEquity.getMasterDirectEquity().getStockName());
							output.setPortFolioId(clientEquity.getId());
							populateFamilyRelation(
									clientEquity.getClientFamilyMember().getLookupRelation().getDescription(), output);
							if (clientEquity.getMasterDirectEquity() != null) {
								output.setProductDesc(clientEquity.getMasterDirectEquity().getStockName());
								/*ec = ecs.getEquityCalculation(clientEquity.getPurchaseDate(),
										clientEquity.getInvestmentAmount() != null
										? clientEquity.getInvestmentAmount().doubleValue()
												: clientEquity.getCurrentMarketValue().doubleValue(),
												clientEquity.getQuantity(), clientEquity.getMasterDirectEquity().getIsin());
								 output.setCurrentValue(ec.getPortfolioValue());			
								*/
								//new Code 
								ec = ecs.getPortfolioValue(clientEquity.getQuantity(), clientEquity.getMasterDirectEquity().getIsin(),clientEquity.getPurchaseDate(),
										clientEquity.getInvestmentAmount() != null
										? clientEquity.getInvestmentAmount().doubleValue()
												: clientEquity.getCurrentMarketValue().doubleValue());
								output.setCurrentValue(ec.getCurrentvalue());
								
						         //Discuss with Neha  cagr and ptp  value be shown
								// for ESOP
								//if (masterProductClassification.getId() == 17) {
								//	output.setCagr("N/A");
								//	output.setPtpReturns("N/A");
								//} else {
									output.setCagr(String.valueOf(String.valueOf(ec.getReturnCagr() * 100)));
									//output.setPtpReturns(String.valueOf(ec.getReturnAbsolute() * 100));
									// discussion with Nissy correction done
									output.setPtpReturns(String.valueOf(ec.getPtpReturns() * 100));
									
								//}

								output.setIsin(clientEquity.getMasterDirectEquity().getIsin());

							} else {
								output.setProductDesc(clientEquity.getUnlistedStockName());
								output.setCurrentValue(clientEquity.getCurrentMarketValue().doubleValue());
								 //Discuss with Neha  cagr and ptp  value be shown
								//if (masterProductClassification.getId() == 17) {
								//	output.setCagr("N/A");
								//	output.setPtpReturns("N/A");
								//} else {

									double diff = FinanceUtil.YEARFRAC(clientEquity.getPurchaseDate(),
											Calendar.getInstance().getTime(), 1);
									double currentPortFolioValue = clientEquity.getCurrentMarketValue().doubleValue();
									double absoluteReturn = (currentPortFolioValue
											/ (double) clientEquity.getInvestmentAmount().doubleValue()) - 1;

									if (diff <= 1) {
										output.setCagr(String.valueOf(absoluteReturn * 100));
									} else {
										/*
										 * String cagr = cagrCal(currentPortFolioValue,
										 * clientEquity.getInvestmentAmount().doubleValue(),
										 * clientEquity.getPurchaseDate());
										 */

										double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;

										output.setCagr(String.valueOf(cagrCal * 100));
										// output.setCagr(String.valueOf((Math.pow((1 + absoluteReturn), 1 / diff) -
										// 1)));
									}
									output.setPtpReturns(String.valueOf(absoluteReturn*100));
								//}

							}
							// populating class and type description
							classLevelDescription(output, masterProductClassification);

							MasterEquityMarketCapClassification masterEquityMarketCapClassification = portFolioOverviewDAO
									.getMasterDirectEquityMarketCapList(
											clientEquity.getMasterDirectEquity() == null ? ""
													: clientEquity.getMasterDirectEquity().getIsin(),
													session);
							// for ISIN not available for Equities needs to be change the database,
							// currently not adding.
							if (masterEquityMarketCapClassification == null) {
								if (masterProductClassification.getId() == 17
										|| masterProductClassification.getId() == 15) {
									//as per discussions with mihir sir and debolina both decided to make this value hardcoded
									  output.setSubAssetClassification("Equity Others");
									  output.setSubAssetClassificationId(10);
									//output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
									//output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
								} else {
									if (masterProductClassification.getId() == 16) {
										output.setSubAssetClassification("Equity International");
										output.setSubAssetClassificationId(7);
									//	output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
									//	output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
	
									}
								}
							} else {
								System.out.println("masterEquityMarketCapClassification "+masterEquityMarketCapClassification.getId());
								if (masterEquityMarketCapClassification.getMarketcapCategory().equals("Largecap")) {
									output.setSubAssetClassification("Equity Large Cap");
									output.setSubAssetClassificationId(5);
									output.setMarketCapId(1);
								    output.setMarketCapName("LargeCap");
									
									//output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
									//output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
									//output.setMarketCapId(masterEquityMarketCapClassification.getId());
									//output.setMarketCapName(masterEquityMarketCapClassification.getMarketcapCategory());
								}
								if (masterEquityMarketCapClassification.getMarketcapCategory().equals("Smallcap")) {
									output.setSubAssetClassification("Equity Small Cap");
									output.setSubAssetClassificationId(13);
									output.setMarketCapId(2);
									output.setMarketCapName("SmallCap");
									
									//output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
									//output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
									//output.setMarketCapId(masterEquityMarketCapClassification.getId());
									//output.setMarketCapName(masterEquityMarketCapClassification.getMarketcapCategory());
								}
								if (masterEquityMarketCapClassification.getMarketcapCategory().equals("Midcap")) {
									output.setSubAssetClassification("Equity Mid Cap");
									output.setSubAssetClassificationId(6);
									output.setMarketCapId(3);
									output.setMarketCapName("MidCap");
									
									//output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
									//output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
									//output.setMarketCapId(masterEquityMarketCapClassification.getId());
									//output.setMarketCapName(masterEquityMarketCapClassification.getMarketcapCategory());
								}
							}
							output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
							if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
								output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_INVESTMENT);

							} else {
								output.setInvestmentOrPerson("/NA");
							}
							if (masterProductClassification.getId() == 17) {
								output.setInvestmentDate("N/A");
								output.setInvestmentValue(clientEquity.getInvestmentAmount() != null
										? clientEquity.getInvestmentAmount().doubleValue():0);
							} else {
								output.setInvestmentDate(simpleDateFormat.format(clientEquity.getPurchaseDate()));
								output.setInvestmentValue(clientEquity.getInvestmentAmount() != null
										? clientEquity.getInvestmentAmount().doubleValue()
												: clientEquity.getCurrentMarketValue().doubleValue());
							}
							if (masterProductClassification.getLockedInFlag().equals("Y")) {
								output.setLockedInDate(FinexaConstant.LOCKED_IN_DATE_AVAILABLE);
								output.setSt_ltClassification("N/A");
							}

							// check and populate market linked
							output = getMarketLinkedCheck(output, masterProductClassification.getMarketLinkedFlag());
							if (masterProductClassification.getId() == 17) {
								output.setLockedInDate(simpleDateFormat.format(clientEquity.getEsopVestingDate()));
								//output.setGains(0);
								output.setGains(output.getCurrentValue() - output.getInvestmentValue());
							} else {
								output.setGains(output.getCurrentValue() - output.getInvestmentValue());
							}

							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							outputList.add(output);
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}

					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}
				// ************************************* Equity End ***********

				// Retirement Schemes
				try {
					// Client PPF
					List<ClientPPF> clientPPFList = portFolioOverviewDAO.getClientPPF(clientId, session);
					for (ClientPPF clientPPF : clientPPFList) {
						try {
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							masterProductClassification = clientPPF.getMasterProductClassification();
							output.setProductName(masterProductClassification.getProductName());
							output.setProductDesc("PPF");
							// populating class and type description
							classLevelDescription(output, masterProductClassification);
							populateFamilyRelation(
									clientPPF.getClientFamilyMember().getLookupRelation().getDescription(), output);
							output.setPortFolioId(clientPPF.getId());
							PPFFixedAmountService ppffd = new PPFFixedAmountService();
							PPFFixedAmountDeposit pffixedAmount = ppffd.getPPFFixedAmountCalculationDetails(clientPPF.getCurrentBalance().doubleValue(),
									clientPPF.getPlannedDepositAmount().doubleValue(), "", clientPPF.getPpfTenure(),
									clientPPF.getLookupFrequency1().getId(), clientPPF.getLookupFrequency2().getId(),
									clientPPF.getStartDate());
							output.setCurrentValue(pffixedAmount.getCurrentBalance());
							output.setCagr(String.valueOf(pffixedAmount.getAnnualInterest() * 100));
							if (clientPPF.getExtensionFlag().equals("Y")) {
								pffixedAmount = new PPFFixedAmountService().getPPFExtensionCalcuation(
										clientPPF.getDepositAmountExt().doubleValue(), "Y",
										clientPPF.getExtensionTenure(), clientPPF.getAmountDepositFrequencyExt(),
										clientPPF.getExtensionCompoundingFrequency(), clientPPF.getExtensionStartDate(),
										pffixedAmount.getMaturityAmount(), pffixedAmount.getTotalAmountDeposited());
								
							}
							output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
							if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
								output.setInvestmentOrPerson("Investment Assets");
							} else {
								output.setInvestmentOrPerson("NA");
							}

							if (masterProductClassification.getLockedInFlag().equals("Y")) {
								output.setLockedInDate(simpleDateFormat.format(pffixedAmount.getMaturityDate()));
								output.setMaturityDate(simpleDateFormat.format(pffixedAmount.getMaturityDate()));
								double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
										pffixedAmount.getMaturityDate(), 1);
								output.setTimeToMaturity(String.valueOf(diff));

								// check st lt classification
								output = getSTLTClassificationCheck(output, diff);

							}

							// check and populate market linked
							output = getMarketLinkedCheck(output, masterProductClassification.getMarketLinkedFlag());
							double sumAmountDeposit = 0.0;
							/*for (PPFFixedAmountLookup ppfFixedLookup : pffixedAmount.getPpfFixedAmountLookupList()) {
								variableCal.setTime(simpleDateFormat.parse(ppfFixedLookup.getReferenceDate()));

								if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
										&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
									if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
											.get(Calendar.DAY_OF_MONTH)) {
										output.setCurrentValue(ppfFixedLookup.getClosingBalance());
									} else {
										output.setCurrentValue(lastValue);
									}
									if (output.getCurrentValue() == 0 && (simpleDateFormat.parse(
											pffixedAmount.getPpfFixedAmountLookupList().get(0).getReferenceDate())
											.after(currentDate.getTime()))) {
										output.setCurrentValue(clientPPF.getPlannedDepositAmount().doubleValue());
									}
									break;
								}
								sumAmountDeposit = sumAmountDeposit + ppfFixedLookup.getAmountDeposited();
								lastValue = ppfFixedLookup.getClosingBalance();
							}*/
							//System.out.println("clientPPF.getAmountDepositFrequencyExt() "+clientPPF.getAmountDepositFrequencyExt());
							sumAmountDeposit = ppffd.getPPFFixedAmountDepositDetails(
									clientPPF.getPlannedDepositAmount().doubleValue(),clientPPF.getPpfTenure(),clientPPF.getStartDate(),clientPPF.getAmountDepositFrequencyExt());
							//System.out.println("sumAmountDepositfgfdgfd "+sumAmountDeposit);
							
							output.setInvestmentDate(simpleDateFormat.format(clientPPF.getStartDate()));
							//output.setInvestmentValue(sumAmountDeposit);
							output.setInvestmentValue(0);
							
							output.setPtpReturns("N/A");
							//output.setGains(output.getCurrentValue() - output.getInvestmentValue());
							output.setGains(0);
							output.setMaturityDate(simpleDateFormat.format(pffixedAmount.getMaturityDate()));
							output.setMaturityAmount(pffixedAmount.getMaturityAmount());
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							outputList.add(output);
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}

					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}
				/************************ End of PPF *************************/
				// Client EPF
				try {
					List<ClientEPF> clientEpfList = portFolioOverviewDAO.getClientEpfList(clientId, session);

					for (ClientEPF clientEpf : clientEpfList) {
						try {
							if (clientEpf.getMasterProductClassification().getId() == 13) {
								PortfolioOverviewDto output = new PortfolioOverviewDto();
								masterProductClassification = clientEpf.getMasterProductClassification();
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc("EPF");
								output.setPortFolioId(clientEpf.getId());
								populateFamilyRelation(
										clientEpf.getClientFamilyMember().getLookupRelation().getDescription(), output);
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");
								} else {
									output.setInvestmentOrPerson("NA");
								}

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									Date epfDate = FinexaDateUtil.getDateFromBirthDateToGivenAge(
											clientEpf.getClientMaster().getBirthDate(),
											clientEpf.getEpfWithdrawalAge());
									// EPF Maturity Date will be Retirement age Month -1 for making it 
									//available for Goal Earmarking
									Calendar epfCalendar = Calendar.getInstance();
									epfCalendar.setTime(epfDate);
									epfCalendar.add(Calendar.MONTH, -1);
									output.setLockedInDate(simpleDateFormat.format(epfCalendar.getTime()));
									output.setMaturityDate(simpleDateFormat.format(epfCalendar.getTime()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(), epfDate, 1);
									output.setTimeToMaturity(String.valueOf(diff));
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);
								}

								// check and populate market linked output =
								getMarketLinkedCheck(output, masterProductClassification.getMarketLinkedFlag());

								EPF2Service efpsService = new EPF2Service();
								EPF2Calculator epfCal = efpsService.getEPF2CaculatedValues(
										clientEpf.getClientMaster().getBirthDate(),
										clientEpf.getEpfCurrentBalance().doubleValue(),
										clientEpf.getEpsCurrentBalance().doubleValue(),
										clientEpf.getMonthlyBasicDA().doubleValue(),
										clientEpf.getAnnualContributionIncrease().doubleValue(),
										clientEpf.getContributionUptoAge(), clientEpf.getEpfWithdrawalAge(),
										clientEpf.getSalaryIncreaseRefMonth());
								List<EPF2Lookup> epfList = epfCal.getLookupList();
								output.setCurrentValue(clientEpf.getEpfCurrentBalance().doubleValue());
								output.setMaturityAmount(epfList.get(epfList.size() - 1).getClosingBalEPF());
								// EPF Maturity Date will be Retirement age Month -1 for making it 
								//available for Goal Earmarking
								/*output.setMaturityDate(
										simpleDateFormat.format(epfList.get(epfList.size() - 1).getRefDate()));*/
								currentDate = Calendar.getInstance();
								output.setInvestmentDate("N/A");
								output.setInvestmentValue(0);
								//BigDecimal cagrCal = clientEpf.getEpfoInterestRate();
								//cagrCal = cagrCal.multiply(new BigDecimal(100));
								//output.setCagr(String.valueOf(cagrCal));
								//new Code
							
								output.setCagr(String.valueOf(epfCal.getCurrentInterestRate()*100));
								//System.out.println("output.setCagr "+output.getCagr());
								output.setGains(0);
								output.setPtpReturns("N/A");
								if (!Double.isNaN(output.getCurrentValue())) {
									totalCurrentValue = totalCurrentValue + output.getCurrentValue();
								}
								output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName()
								+ "-" + output.getProductDesc());
								outputList.add(output);
							}
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}
					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}
				/************************* End of EPF ************************/

				if (specificRequermentStat.equals("overview")) {
					// Client NPS
					try {
						List<ClientNPS> clientNpsList = portFolioOverviewDAO.getClientNpsList(clientId, session);

						for (ClientNPS clientNps : clientNpsList) {
							try {
								PortfolioOverviewDto output = new PortfolioOverviewDto();
								masterProductClassification = clientNps.getMasterProductClassification();
								output.setProductName(masterProductClassification.getProductName());
								populateFamilyRelation(
										clientNps.getClientFamilyMember().getLookupRelation().getDescription(), output);
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setProductDesc("NPS");
								output.setPortFolioId(clientNps.getId());
								NPSCalService npsCalService = new NPSCalService();			
								NPSCAL npsCAL = npsCalService.calculateNPSValues(
										clientNps.getClientMaster().getBirthDate(),
										clientNps.getNpsCurrentBalance().doubleValue(),
										clientNps.getEmployeeContribution().doubleValue(),
										clientNps.getEmployeeContributionFrequency(),
										clientNps.getEmployerContribution().doubleValue(),
										clientNps.getEmployerContributionFrequency(),
										clientNps.getAssetClassEAllocation() == null ? 0
												: clientNps.getAssetClassEAllocation().doubleValue(),
												clientNps.getAssetClassCAllocation() == null ? 0
														: clientNps.getAssetClassCAllocation().doubleValue(),
														clientNps.getAssetClassGAllocation() == null ? 0
																: clientNps.getAssetClassGAllocation().doubleValue(),
																clientNps.getEmployeeContributionUptoAge(),
																clientNps.getEmployerContributionUptoAge(),
																clientNps.getClientMaster().getRetirementAge(), clientNps.getPlanType());
								
								System.out.println("Done");
								System.out.flush();
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_INVESTMENT);
								} else {
									output.setInvestmentOrPerson("NA");
								}

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									Calendar epfDate = Calendar.getInstance();
									epfDate.setTime(clientNps.getClientMaster().getBirthDate());
									epfDate.add(Calendar.YEAR, (clientNps.getEmployeeContributionUptoAge()));
									output.setLockedInDate(simpleDateFormat.format(epfDate.getTime()));
									output.setMaturityDate(simpleDateFormat.format(epfDate.getTime()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(), epfDate.getTime(), 1);
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);
								}

								// check and populate market linked
								output = getMarketLinkedCheck(output,
										masterProductClassification.getMarketLinkedFlag());
								System.out.println("Done");
								for (NPSLookup npsLookup : npsCAL.getNpsLookupList()) {
									variableCal.setTime(simpleDateFormat.parse(npsLookup.getReferenceDate()));

									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setCurrentValue(npsLookup.getNpsClosingBalance());
										} else {
											output.setCurrentValue(lastValue);
										}

									}
									lastValue = npsLookup.getNpsClosingBalance();
								}
								currentDate = Calendar.getInstance();
								output.setInvestmentDate("N/A");
								output.setInvestmentValue(-1);
								if (clientNps.getPlanType() == 1)

								{
									output.setCagr(String
											.valueOf(clientNps.getAutoPlanReturns().multiply(new BigDecimal(100))));

								}
								if (clientNps.getPlanType() == 2) {
									output.setCagr(String.valueOf((clientNps.getAssetClassCAllocation().doubleValue()
											* clientNps.getAssetClassGAllocation().doubleValue()
											* clientNps.getAssetClassEAllocation().doubleValue()
											+ npsCAL.getAssetClassCExp() + npsCAL.getAssetClassGExp()
											+ npsCAL.getAssetClassEExp()) * 100));
								}
								output.setGains(-1);
								output.setPtpReturns("N/A");
								if (!Double.isNaN(output.getCurrentValue())) {
									totalCurrentValue = totalCurrentValue + output.getCurrentValue();
								}
								output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName()
								+ "-" + output.getProductDesc());
								outputList.add(output);
							} catch (Exception exp) {
								exp.printStackTrace();
								FinexaBussinessException finexaBuss = new FinexaBussinessException(
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
								FinexaBussinessException.logFinexaBusinessException(finexaBuss);
							}
						}
					} catch (Exception exp) {
						exp.printStackTrace();
						FinexaBussinessException finexaBuss = new FinexaBussinessException(
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
						FinexaBussinessException.logFinexaBusinessException(finexaBuss);
					}
					/************************** End of NPS ********************/

					// Client Atal Pension Yojana
					try {
						List<ClientAtalPensionYojana> clientAtalPensionYojanaList = portFolioOverviewDAO
								.getAtalPensionYojanaList(clientId, session);

						for (ClientAtalPensionYojana atalPensionYojana : clientAtalPensionYojanaList) {
							try {
								lastValue = 0.0;
								PortfolioOverviewDto output = new PortfolioOverviewDto();
								masterProductClassification = atalPensionYojana.getMasterProductClassification();
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc("APY");
								populateFamilyRelation(
										atalPensionYojana.getClientFamilyMember().getLookupRelation().getDescription(),
										output);
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setPortFolioId(atalPensionYojana.getId());
								AtalPensionYojanaService atalPensionService = new AtalPensionYojanaService();
								AtalPensionYojana atalPension = atalPensionService.getAtalPensionYojanaCal(
										atalPensionYojana.getClientMaster().getBirthDate(),
										atalPensionYojana.getLookupFrequency().getId(),
										atalPensionYojana.getMonthlyPensionRequired().doubleValue(),
										atalPensionYojana.getClientMaster().getRetirementAge(),
										atalPensionYojana.getApyStartDate(),
										atalPensionYojana.getClientFamilyMember().getLifeExpectancy());
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_INVESTMENT);
								} else {
									output.setInvestmentOrPerson("NA");
								}

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									Calendar apyDate = Calendar.getInstance();
									apyDate.setTime(atalPension.getContrEndDate());
									apyDate.add(Calendar.YEAR,
											((atalPensionYojana.getClientFamilyMember().getLifeExpectancy()
													- atalPensionYojana.getClientMaster().getRetirementAge()) * 12)
											+ 1);
									output.setLockedInDate(simpleDateFormat.format(apyDate.getTime()));
									output.setMaturityDate(simpleDateFormat.format(apyDate.getTime()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(), apyDate.getTime(), 1);
									output.setTimeToMaturity(String.valueOf(diff));

									// check st lt classification output =
									getSTLTClassificationCheck(output, diff);
								}

								// check and populate market linked
								output = getMarketLinkedCheck(output,
										masterProductClassification.getMarketLinkedFlag());

								for (AtalPensionYojanaLookup atalPensionYojanaLookup : atalPension
										.getAtalPensionYojanaLookupList()) {
									variableCal.setTime(
											simpleDateFormat.parse(atalPensionYojanaLookup.getReferenceDate()));

									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										lastValue = lastValue + atalPensionYojanaLookup.getAmountInvested();
										break;
									} else {
										lastValue = lastValue + atalPensionYojanaLookup.getAmountInvested();
									}

								}
								output.setCurrentValue(lastValue);
								currentDate = Calendar.getInstance();
								output.setInvestmentDate("N/A");
								output.setCagr("N/A");
								output.setMaturityAmount(atalPension.getAmountToNominee());
								output.setPtpReturns("N/A");
								if (!Double.isNaN(output.getCurrentValue())) {
									totalCurrentValue = totalCurrentValue + output.getCurrentValue();
								}
								output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName()
								+ "-" + output.getProductDesc());
								outputList.add(output);
							} catch (Exception exp) {
								exp.printStackTrace();
								FinexaBussinessException finexaBuss = new FinexaBussinessException(
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
								FinexaBussinessException.logFinexaBusinessException(finexaBuss);
							}
						}
					} catch (Exception exp) {
						exp.printStackTrace();
						FinexaBussinessException finexaBuss = new FinexaBussinessException(
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
						FinexaBussinessException.logFinexaBusinessException(finexaBuss);
					}
					/****************** END of APY ***********************/

					// ClientAnnuity

					try {
						List<ClientAnnuity> clientAnnuityList = new ArrayList<>();
						clientAnnuityList= portFolioOverviewDAO.getClientAnnuityList(clientId, session);

						for (ClientAnnuity clientAnnuity : clientAnnuityList) {
							try {
								lastValue = 0.0;
								PortfolioOverviewDto output1 = new PortfolioOverviewDto();
								PortfolioOverviewDto output2 = new PortfolioOverviewDto();
								PortfolioOverviewDto output3 = new PortfolioOverviewDto();
								PortfolioOverviewDto output4 = new PortfolioOverviewDto();
								PortfolioOverviewDto output5 = new PortfolioOverviewDto();
								PortfolioOverviewDto outputEpsAnnuity = new PortfolioOverviewDto();
								masterProductClassification = clientAnnuity.getMasterProductClassification();
								output1.setProductName(masterProductClassification.getProductName());
								output1.setProductDesc("ANNUITY");
								populateFamilyRelation(
										clientAnnuity.getClientFamilyMember().getLookupRelation().getDescription(),
										output1);
								// populating class and type description
								classLevelDescription(output1, masterProductClassification);
								output1.setPortFolioId(clientAnnuity.getId());
								Annuity2ProductService annuity2ProductService = new Annuity2ProductService();
								int spouseLifeExpecAge = 0;
								for (ClientFamilyMember famMem : clientAnnuity.getClientMaster()
										.getClientFamilyMembers()) {
									System.out.println("famMem "+famMem);
									if (famMem.getLookupRelation().getId() == 1) {
								
										spouseLifeExpecAge = famMem.getLifeExpectancy();
									}
								}
								
								Annuity2ProductCalculator annuityCal = annuity2ProductService.getAnnuityProductValues(
										clientAnnuity.getClientMaster().getBirthDate(),
										clientAnnuity.getPensionableCorpus().doubleValue(),
										clientAnnuity.getAnnuityRate().doubleValue(),
										clientAnnuity.getClientFamilyMember().getLifeExpectancy(), spouseLifeExpecAge,
										clientAnnuity.getLookupFrequency().getId(), clientAnnuity.getAnnuityStartDate(),
										clientAnnuity.getLookupAnnuityType().getId(),
										clientAnnuity.getGrowthRate().doubleValue(), 1);
								output1.setInvestmentOrPersonFlag(
										masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output1.setInvestmentOrPerson("Investment Assets");
									totalCurrentValue = totalCurrentValue + output1.getCurrentValue();
								} else {
									output1.setInvestmentOrPerson("NA");
								}
								output1.setCurrentValue(clientAnnuity.getPensionableCorpus().doubleValue());
								output1.setInvestmentDate(simpleDateFormat.format(clientAnnuity.getAnnuityStartDate()));
								output1.setInvestmentValue(clientAnnuity.getPensionableCorpus().doubleValue());
								output1.setPtpReturns("N/A");
								output1.setCagr(String.valueOf(clientAnnuity.getAnnuityRate().multiply(new BigDecimal(100))));

								// check and populate market linked
								output1 = getMarketLinkedCheck(output1,
										masterProductClassification.getMarketLinkedFlag());

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									currentDate = Calendar.getInstance();
									output1.setLockedInDate(simpleDateFormat.format(annuityCal.getAnnuityT1EndDate()));
									output1.setMaturityDate(simpleDateFormat.format(annuityCal.getAnnuityT1EndDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											annuityCal.getAnnuityT1EndDate(), 1);
									output1.setTimeToMaturity(String.valueOf(diff));

									// check st lt classification
									output1 = getSTLTClassificationCheck(output1, diff);
								}
								output1.setPlanType(clientAnnuity.getLookupAnnuityType().getDescription());
								if (clientAnnuity.getLookupAnnuityType().getId() == 1) {
									output1.setFinancialAssetName(output1.getPortFolioId() + "-"
											+ output1.getProductName() + "-" + output1.getProductDesc());
									outputList.add(output1);
								}

								output2 = output1;
								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									currentDate = Calendar.getInstance();
									output2.setLockedInDate(simpleDateFormat.format(annuityCal.getAnnuityT2EndDate()));
									output2.setMaturityDate(simpleDateFormat.format(annuityCal.getAnnuityT2EndDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											annuityCal.getAnnuityT2EndDate(), 1);
									output2.setTimeToMaturity(String.valueOf(diff));

									// check st lt classification
									output2 = getSTLTClassificationCheck(output2, diff);
								}
								if (clientAnnuity.getLookupAnnuityType().getId() == 2) {
									output2.setFinancialAssetName(output2.getPortFolioId() + "-"
											+ output2.getProductName() + "-" + output2.getProductDesc());
									outputList.add(output2);
								}
								output3 = output2;
								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									currentDate = Calendar.getInstance();
									output3.setLockedInDate(simpleDateFormat.format(annuityCal.getAnnuityT3EndDate()));
									output3.setMaturityDate(simpleDateFormat.format(annuityCal.getAnnuityT3EndDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											annuityCal.getAnnuityT3EndDate(), 1);
									output3.setTimeToMaturity(String.valueOf(diff));

									// check st lt classification
									output3 = getSTLTClassificationCheck(output3, diff);
								}
								if (clientAnnuity.getLookupAnnuityType().getId() == 3) {
									output3.setFinancialAssetName(output2.getPortFolioId() + "-"
											+ output3.getProductName() + "-" + output3.getProductDesc());
									outputList.add(output3);
								}
								output4 = output3;

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									currentDate = Calendar.getInstance();
									output4.setLockedInDate(simpleDateFormat.format(annuityCal.getAnnuityT4EndDate()));
									output4.setMaturityDate(simpleDateFormat.format(annuityCal.getAnnuityT4EndDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											annuityCal.getAnnuityT4EndDate(), 1);
									output4.setTimeToMaturity(String.valueOf(diff));

									// check st lt classification
									output4 = getSTLTClassificationCheck(output4, diff);
								}
								if (clientAnnuity.getLookupAnnuityType().getId() == 4) {
									output4.setFinancialAssetName(output4.getPortFolioId() + "-"
											+ output4.getProductName() + "-" + output4.getProductDesc());
									outputList.add(output4);
								}
								output5 = output4;
								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									currentDate = Calendar.getInstance();
									output5.setLockedInDate(simpleDateFormat.format(annuityCal.getAnnuityT5EndDate()));
									output5.setMaturityDate(simpleDateFormat.format(annuityCal.getAnnuityT5EndDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											annuityCal.getAnnuityT5EndDate(), 1);
									output5.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output5 = getSTLTClassificationCheck(output5, diff);
								}

								if (clientAnnuity.getLookupAnnuityType().getId() == 5) {
									output5.setFinancialAssetName(output5.getPortFolioId() + "-"
											+ output5.getProductName() + "-" + output5.getProductDesc());
									outputList.add(output5);
								}
								outputEpsAnnuity = output5;

								outputEpsAnnuity.setCurrentValue(epsBalAtage);
								outputEpsAnnuity.setInvestmentValue(epsBalAtage);

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									currentDate = Calendar.getInstance();
									outputEpsAnnuity
									.setLockedInDate(simpleDateFormat.format(annuityCal.getEpsEndDate()));
									outputEpsAnnuity
									.setMaturityDate(simpleDateFormat.format(annuityCal.getEpsEndDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											annuityCal.getEpsEndDate(), 1);
									outputEpsAnnuity.setTimeToMaturity(String.valueOf(diff));

									// check st lt classification
									outputEpsAnnuity = getSTLTClassificationCheck(outputEpsAnnuity, diff);
								}

								if (clientAnnuity.getLookupAnnuityType().getId() == 6) {
									outputEpsAnnuity.setFinancialAssetName(
											outputEpsAnnuity.getPortFolioId() + "-" + outputEpsAnnuity.getProductName()
											+ "-" + outputEpsAnnuity.getProductDesc());
									outputList.add(outputEpsAnnuity);
								}

							} catch (Exception exp) {
								exp.printStackTrace();
								FinexaBussinessException finexaBuss = new FinexaBussinessException(
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
										FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
								FinexaBussinessException.logFinexaBusinessException(finexaBuss);
							}

						}
					} catch (Exception exp) {
						exp.printStackTrace();
						FinexaBussinessException finexaBuss = new FinexaBussinessException(
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
						FinexaBussinessException.logFinexaBusinessException(finexaBuss);
					}
					/********************* END of Annuity ************************/
				}

				// <<<<<<<<<<<< Retirement Products end >>>>>>>>>>>>>>>>

				// ********************* Fixed Income Starts ****************
				try {
					List<ClientFixedIncome> clientFixedIncomeList = portFolioOverviewDAO
							.getClientFixedincomeList(clientId, session);

					for (ClientFixedIncome income : clientFixedIncomeList) {
						try {
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							/*output.setBankIssuerName(
									income.getMasterCash() == null ? "" : income.getMasterCash().getName());*/
							output.setBankIssuerName(income.getBankIssuerName());
							masterProductClassification = income.getMasterProductClassification();
							populateFamilyRelation(income.getClientFamilyMember().getLookupRelation().getDescription(),
									output);
							// check and populate market linked
							if (masterProductClassification != null) {
								output = getMarketLinkedCheck(output,
										masterProductClassification.getMarketLinkedFlag());
							} else {
								output.setMarketLinkOrFixedReturn("N/A");
							}
							output.setPortFolioId(income.getId());
							// FD TDR
							if (masterProductClassification.getId() == 22) {

								if (income.getLookupFixedDepositType().getId() == 2) {
									output.setProductDesc(
											output.getBankIssuerName().isEmpty() ? "Bank FD- Interest Payout"
													: output.getBankIssuerName() + "-Bank FD- Interest Payout");
									// Interest Payout
									BankFDTDRService fdtdrService = new BankFDTDRService();
									BankFDTDRPC bankftdr = fdtdrService.getFDTDROutput(income.getAmount().doubleValue(),
											income.getInterestCouponRate().doubleValue(), income.getTenureYearsDays(),
											income.getTenure(), income.getLookupFrequency2().getId(),
											income.getInvestmentDepositDate());

									for (BankFDTDRLookup bankFDTDRLookup : bankftdr.getBankFdTdrLookupList()) {
										variableCal.setTime(simpleDateFormat.parse(bankFDTDRLookup.getReferenceDate()));
										if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
												&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
											if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
													.get(Calendar.DAY_OF_MONTH)) {
												output.setGains(bankFDTDRLookup.getTotalInterestAccrued());
											} else {
												output.setGains(lastValue);
											}
											if (output.getCurrentValue() == 0 && (simpleDateFormat
													.parse(bankftdr.getBankFdTdrLookupList().get(0).getReferenceDate())
													.after(currentDate.getTime()))) {
												output.setCurrentValue(income.getAmount().doubleValue());
											}
											break;
										}

										lastValue = bankFDTDRLookup.getTotalInterestAccrued();
									}

									output.setInvestmentValue(income.getAmount().doubleValue());
									output.setCagr(String
											.valueOf(income.getInterestCouponRate().multiply(new BigDecimal(100))));
									//new requirement
									output.setPtpReturns(String
											.valueOf(income.getInterestCouponRate().multiply(new BigDecimal(100))));
									output.setCurrentValue(income.getAmount().doubleValue());
									if (masterProductClassification.getLockedInFlag().equals("Y")) {
										output.setLockedInDate(simpleDateFormat.format(bankftdr.getMaturityDate()));
										output.setMaturityDate(simpleDateFormat.format(bankftdr.getMaturityDate()));
										double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
												bankftdr.getMaturityDate(), 1);
										output.setTimeToMaturity(String.valueOf(diff));
										// check st lt classification
										output = getSTLTClassificationCheck(output, diff);
									}
									output.setMaturityAmount(income.getAmount().doubleValue());
								} else {

									// Cumulative
									output.setProductDesc(output.getBankIssuerName().isEmpty() ? "Bank FD- Cummulative"
											: output.getBankIssuerName() + "-Bank FD- Cummulative");
									BankFDSTDRCDCPService bankFDSTDRCDCPService = new BankFDSTDRCDCPService();
									BankFDSTDRCDCP bankFDSTDRCDCP = bankFDSTDRCDCPService.getFDSTDRCDCPOutputList(
											income.getAmount().doubleValue(),
											income.getInterestCouponRate().doubleValue(), income.getTenureYearsDays(),
											income.getTenure(), income.getLookupFrequency1().getId(),
											income.getInvestmentDepositDate());
									for (BankFDSTDRCDCPLookup bankFDSTDRCDCPLookup : bankFDSTDRCDCP
											.getBankFdTdrLookupList()) {
										variableCal.setTime(
												simpleDateFormat.parse(bankFDSTDRCDCPLookup.getReferenceDate()));

										if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
												&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
											if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
													.get(Calendar.DAY_OF_MONTH)) {
												output.setCurrentValue(bankFDSTDRCDCPLookup.getClosingBalance());
											} else {
												output.setCurrentValue(lastValue);
											}
											if (output.getCurrentValue() == 0
													&& (simpleDateFormat
															.parse(bankFDSTDRCDCP.getBankFdTdrLookupList().get(0)
																	.getReferenceDate())
															.after(currentDate.getTime()))) {
												output.setCurrentValue(income.getAmount().doubleValue());
											}
											break;
										}
										lastValue = bankFDSTDRCDCPLookup.getClosingBalance();
									}

									output.setInvestmentValue(income.getAmount().doubleValue());
									output.setCagr(String
											.valueOf(income.getInterestCouponRate().multiply(new BigDecimal(100))));
									//new requirement
									output.setPtpReturns(String
											.valueOf(income.getInterestCouponRate().multiply(new BigDecimal(100))));
									output.setGains(output.getCurrentValue() - output.getInvestmentValue());
									if (masterProductClassification.getLockedInFlag().equals("Y")) {
										output.setLockedInDate(
												simpleDateFormat.format(bankFDSTDRCDCP.getMaturityDate()));
										output.setMaturityDate(
												simpleDateFormat.format(bankFDSTDRCDCP.getMaturityDate()));
										double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
												bankFDSTDRCDCP.getMaturityDate(), 1);
										output.setTimeToMaturity(String.valueOf(diff));
										// check st lt classification
										output = getSTLTClassificationCheck(output, diff);
									}
									output.setMaturityAmount(bankFDSTDRCDCP.getMaturityAmount());

								}
								output.setProductName(masterProductClassification.getProductName());

								// populating class and type description
								classLevelDescription(output, masterProductClassification); //
								/*
								 * MasterLTSTClassification masterLTSTClassification = portFolioOverviewDAO
								 * .getMasterLTSTClassificationList(income.getTenure(), session);
								 */
								output.setSubAssetClassification(
										masterProductClassification.getLookupAssetClass().getDescription());
								output.setSubAssetClassification(
										setSubAssetClassificationForFixedIncome(income.getTenure(),
												income.getInvestmentDepositDate(), income.getTenureYearsDays()));

								output.setSubAssetClassificationId(
										setSubAssetClassificationIdForFixedIncome(income.getTenure(),
												income.getInvestmentDepositDate(), income.getTenureYearsDays()));


								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");
								} else {
									output.setInvestmentOrPerson("NA");
								}

								output.setInvestmentDate(simpleDateFormat.format(income.getInvestmentDepositDate()));

								// output.setPtpReturns(String.valueOf(output.getInvestmentValue()
								// / output.getInvestmentValue() - 1));
								//	output.setPtpReturns("" + (((output.getCurrentValue() / output.getInvestmentValue()) - 1) * 100));

							}

							// STDR CD CP
							if (masterProductClassification.getId() == 27) {

								lastValue = 0.0;
								output.setProductDesc(masterProductClassification.getProductName());
								BankFDSTDRCDCPService bankFDSTDRCDCPService = new BankFDSTDRCDCPService();
								BankFDSTDRCDCP bankFDSTDRCDCP = bankFDSTDRCDCPService.getFDSTDRCDCPOutputList(
										income.getAmount().doubleValue(), income.getInterestCouponRate().doubleValue(),
										income.getTenureYearsDays(), income.getTenure(),
										income.getLookupFrequency1().getId(), income.getInvestmentDepositDate());
								for (BankFDSTDRCDCPLookup bankFDSTDRCDCPLookup : bankFDSTDRCDCP
										.getBankFdTdrLookupList()) {
									variableCal
									.setTime(simpleDateFormat.parse(bankFDSTDRCDCPLookup.getReferenceDate()));

									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setCurrentValue(bankFDSTDRCDCPLookup.getClosingBalance());
										} else {
											output.setCurrentValue(lastValue);
										}
										if (output.getCurrentValue() == 0 && (simpleDateFormat.parse(
												bankFDSTDRCDCP.getBankFdTdrLookupList().get(0).getReferenceDate())
												.after(currentDate.getTime()))) {
											output.setCurrentValue(income.getAmount().doubleValue());
										}
										break;
									}
									lastValue = bankFDSTDRCDCPLookup.getClosingBalance();
								}

								output.setProductName(masterProductClassification.getProductName());
								// populating class and type description
								classLevelDescription(output, masterProductClassification);

								/*
								 * MasterLTSTClassification masterLTSTClassification = portFolioOverviewDAO
								 * .getMasterLTSTClassificationList(income.getTenure(), session);
								 */
								output.setSubAssetClassification(
										masterProductClassification.getLookupAssetClass().getDescription());

								output.setSubAssetClassification(
										setSubAssetClassificationForFixedIncome(income.getTenure(),
												income.getInvestmentDepositDate(), income.getTenureYearsDays()));
								output.setSubAssetClassificationId(
										setSubAssetClassificationIdForFixedIncome(income.getTenure(),
												income.getInvestmentDepositDate(), income.getTenureYearsDays()));

								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");
								} else {
									output.setInvestmentOrPerson("NA");
								}

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									output.setLockedInDate(simpleDateFormat.format(bankFDSTDRCDCP.getMaturityDate()));
									output.setMaturityDate(simpleDateFormat.format(bankFDSTDRCDCP.getMaturityDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											bankFDSTDRCDCP.getMaturityDate(), 1);
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);
								}
								output.setMaturityAmount(bankFDSTDRCDCP.getMaturityAmount());

								output.setInvestmentDate(simpleDateFormat.format(income.getInvestmentDepositDate()));
								output.setInvestmentValue(income.getAmount().doubleValue());
								BigDecimal interestRate = income.getInterestCouponRate();
								interestRate = interestRate.multiply(new BigDecimal(100));
								output.setCagr(String.valueOf(interestRate));
								//new requirement
								output.setPtpReturns(String.valueOf(interestRate));
								// output.setPtpReturns(String.valueOf(output.getInvestmentValue()
								// * output.getInvestmentValue() - 1)); according to
								// excel
								// below.

								output.setGains(output.getCurrentValue() - output.getInvestmentValue());
								// output.setMaturityAmount(maturityAmount);
								//output.setPtpReturns("" + (((output.getCurrentValue() / output.getInvestmentValue()) - 1) * 100));
							}

							// Bond Debentures
							if (masterProductClassification.getId() == 24) {

								lastValue = 0.0;
								output.setProductDesc(income.getLookupBondType().getDescription());
								/// For Bonds with coupons
								if (income.getLookupBondType().getId() == 3) {
									BondDebentures dbentureReturn = new BondDebenturesService()
											.calculateDebenturesValue(income.getTenureYearsDays(),
													income.getInvestmentDepositDate(),
													String.valueOf(income.getTenure()),
													income.getLookupFrequency2().getId(),
													income.getInterestCouponRate().doubleValue(),
													income.getBondFaceValue().doubleValue(), income.getBondPurchased(),
													income.getBondCurrentYield().doubleValue());

									List<BankBondDebenturesLookup> bondDebenturesList = new BondDebenturesService()
											.getBondDebentures(income.getBondPurchased(),
													income.getBondFaceValue().doubleValue(),
													income.getLookupFrequency2().getId(),
													income.getInvestmentDepositDate(),
													dbentureReturn.getDaysToMaturity(),
													dbentureReturn.getCouponReceived(),
													dbentureReturn.getTotalMonths());

									for (BankBondDebenturesLookup bankBondDebenturesLookup : bondDebenturesList) {
										variableCal.setTime(
												simpleDateFormat.parse(bankBondDebenturesLookup.getReferenceDate()));
										if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
											if (variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
												break;
											}
										}
										lastValue = lastValue + bankBondDebenturesLookup.getCouponReceived();

									}
									output.setGains(lastValue);

									output.setCurrentValue(income.getAmount().doubleValue());
									output.setProductName(masterProductClassification.getProductName());
									// populating class and type description
									classLevelDescription(output, masterProductClassification);
									/*
									 * MasterLTSTClassification masterLTSTClassification = portFolioOverviewDAO
									 * .getMasterLTSTClassificationList(income.getTenure(), session);
									 */
									output.setSubAssetClassification(
											masterProductClassification.getLookupAssetClass().getDescription());

									output.setSubAssetClassification(
											setSubAssetClassificationForFixedIncome(income.getTenure(),
													income.getInvestmentDepositDate(), income.getTenureYearsDays()));
									output.setSubAssetClassificationId(
											setSubAssetClassificationIdForFixedIncome(income.getTenure(),
													income.getInvestmentDepositDate(), income.getTenureYearsDays()));

									output.setInvestmentOrPersonFlag(
											masterProductClassification.getInvestmentAssetsFlag());
									if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
										output.setInvestmentOrPerson("Investment Assets");
									} else {
										output.setInvestmentOrPerson("NA");
									}

									if (masterProductClassification.getLockedInFlag().equals("Y")) {
										output.setLockedInDate(
												simpleDateFormat.format(dbentureReturn.getDaysToMaturity()));
										output.setMaturityDate(
												simpleDateFormat.format(dbentureReturn.getDaysToMaturity()));
										double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
												dbentureReturn.getDaysToMaturity(), 1);
										output.setTimeToMaturity(String.valueOf(diff));
										// check st lt classification
										output = getSTLTClassificationCheck(output, diff);

									}

									output.setInvestmentDate(
											simpleDateFormat.format(income.getInvestmentDepositDate()));
									output.setInvestmentValue(income.getAmount().doubleValue());
									output.setCagr(String.valueOf(income.getInterestCouponRate().doubleValue() * 100));
									//new requirement
									output.setPtpReturns(String.valueOf(income.getInterestCouponRate().doubleValue() * 100));
									//output.setPtpReturns(String.valueOf((output.getCurrentValue()/output.getInvestmentValue() - 1) * 100));
									output.setMaturityAmount(
											income.getBondPurchased() * income.getBondFaceValue().doubleValue());
								}

								/// For Perpetual bond
								if (income.getLookupBondType().getId() == 2) {

									lastValue = 0.0;
									PerpetualBond perpetualBond = new PerpetualBondService().calculateDebenturesValue(
											income.getTenureYearsDays(), income.getInvestmentDepositDate(),
											String.valueOf(income.getTenure()), income.getLookupFrequency2().getId(),
											income.getInterestCouponRate().doubleValue(),
											income.getBondFaceValue().doubleValue(), income.getBondPurchased(),
											income.getBondCurrentYield().doubleValue());

									List<PerpetualBondLookup> perpetuallist = new PerpetualBondService()
											.getBondDebentures(income.getAmount().doubleValue(),
													income.getLookupFrequency2().getId(),
													income.getInvestmentDepositDate(),
													perpetualBond.getDaysToMaturity(),
													perpetualBond.getCouponReceived(), perpetualBond.getTotalMonths());
									for (PerpetualBondLookup perpetualBondLookup : perpetuallist) {
										variableCal.setTime(
												simpleDateFormat.parse(perpetualBondLookup.getReferenceDate()));
										if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
											if (variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
												break;
											}
										}
										lastValue = lastValue + perpetualBondLookup.getCouponReceived();

									}

									output.setProductName(masterProductClassification.getProductName());
									output.setProductDesc(income.getLookupBondType().getDescription());
									// populating class and type description
									classLevelDescription(output, masterProductClassification);
									output.setCurrentValue(perpetualBond.getCurrentValue());
									output.setInvestmentOrPersonFlag(
											masterProductClassification.getInvestmentAssetsFlag());
									if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
										output.setInvestmentOrPerson("Investment Assets");
										totalCurrentValue = totalCurrentValue + output.getCurrentValue();
									} else {
										output.setInvestmentOrPerson("NA");
									}

									if (masterProductClassification.getLockedInFlag().equals("Y")) {
										output.setLockedInDate(
												simpleDateFormat.format(perpetualBond.getDaysToMaturity()));
										output.setMaturityDate(
												simpleDateFormat.format(perpetualBond.getDaysToMaturity()));
										double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
												perpetualBond.getDaysToMaturity(), 1);
										output.setTimeToMaturity(String.valueOf(diff));
										// check st lt classification
										output = getSTLTClassificationCheck(output, diff);

									}

									output.setInvestmentDate(
											simpleDateFormat.format(income.getInvestmentDepositDate()));
									output.setInvestmentValue(income.getAmount().doubleValue());
									output.setCagr(String.valueOf(income.getInterestCouponRate().doubleValue() * 100));
									//new requirement
									output.setPtpReturns(String.valueOf(income.getInterestCouponRate().doubleValue() * 100));
									// output.setPtpReturns("N/A");
									//output.setPtpReturns(String.valueOf((output.getCurrentValue()/output.getInvestmentValue() - 1) * 100));
									output.setGains(lastValue);
									output.setMaturityAmount(-0);
								}

								/// Zero Coupon Bond
								if (income.getLookupBondType().getId() == 1) {
									output.setProductName(masterProductClassification.getProductName());
									output.setProductDesc(income.getLookupBondType().getDescription());
									ZeroCouponBond zeroCouponBond = new ZeroCouponService().calculateZeroCouponBond(
											income.getAmount().doubleValue(),
											income.getBondCurrentYield().doubleValue(), income.getTenureYearsDays(),
											income.getTenure(), income.getLookupFrequency1().getId(),
											income.getInvestmentDepositDate());
									// populating class and type description
									classLevelDescription(output, masterProductClassification);

									output.setInvestmentOrPersonFlag(
											masterProductClassification.getInvestmentAssetsFlag());
									if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
										output.setInvestmentOrPerson("Investment Assets");
									} else {
										output.setInvestmentOrPerson("NA");
									}

									if (masterProductClassification.getLockedInFlag().equals("Y")) {
										output.setLockedInDate(
												simpleDateFormat.format(zeroCouponBond.getMaturityDate()));
										output.setMaturityDate(
												simpleDateFormat.format(zeroCouponBond.getMaturityDate()));
										double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
												zeroCouponBond.getMaturityDate(), 1);
										output.setTimeToMaturity(String.valueOf(diff));
										// check st lt classification
										output = getSTLTClassificationCheck(output, diff);

									}

									output.setInvestmentDate(
											simpleDateFormat.format(income.getInvestmentDepositDate()));
									output.setInvestmentValue(income.getAmount().doubleValue());

									// output.setPtpReturns("N/A");
									// output.setGains(output.getCurrentValue() - output.getInvestmentValue());
									zeroCouponBond = new ZeroCouponService().getZerocouponBondServiceLookupList(
											income.getAmount().doubleValue(),
											income.getBondCurrentYield().doubleValue(), income.getTenureYearsDays(),
											income.getTenure(), income.getLookupFrequency1().getId(),
											income.getInvestmentDepositDate(), income.getBondPurchased(),
											income.getBondFaceValue().doubleValue());
									double totalGain = 0d;
									for (ZeroCouponBondLookup zeroCouponBondLookup : zeroCouponBond
											.getBankFdTdrLookupList()) {
										variableCal.setTime(
												simpleDateFormat.parse(zeroCouponBondLookup.getReferenceDate()));
										if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
												&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
											if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
													.get(Calendar.DAY_OF_MONTH)) {
												break;
											} else {
												output.setGains(totalGain);
											}

											break;
										}

										totalGain = totalGain + zeroCouponBondLookup.getInterestCredited();
									}

									output.setCurrentValue(zeroCouponBond.getCurrentValue());
									/*output.setMaturityAmount(zeroCouponBond.getBankFdTdrLookupList()
											.get(zeroCouponBond.getBankFdTdrLookupList().size() - 1)
											.getClosingBalance());*/
									//new Code
									output.setMaturityAmount(income.getBondFaceValue().doubleValue());
									output.setPtpReturns(String.valueOf((output.getCurrentValue()/output.getInvestmentValue() - 1)*100));
									double diff = FinanceUtil.YEARFRAC(income.getInvestmentDepositDate(),
											Calendar.getInstance().getTime(), 1);
									double absoluteReturn = (output.getCurrentValue()
											/ output.getInvestmentValue()) - 1;
									if (diff <= 1) {
										output.setCagr(String.valueOf(absoluteReturn * 100));
									} else {
										double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;
										output.setCagr(String.valueOf(cagrCal * 100));
									}
								}
								/*
								 * MasterLTSTClassification masterLTSTClassification = portFolioOverviewDAO
								 * .getMasterLTSTClassificationList(income.getTenure(), session);
								 */
								output.setSubAssetClassification(
										masterProductClassification.getLookupAssetClass().getDescription());

								output.setSubAssetClassification(
										setSubAssetClassificationForFixedIncome(income.getTenure(),
												income.getInvestmentDepositDate(), income.getTenureYearsDays()));
								output.setSubAssetClassificationId(
										setSubAssetClassificationIdForFixedIncome(income.getTenure(),
												income.getInvestmentDepositDate(), income.getTenureYearsDays()));

							}

							// Bank Recurring Deposit
							if (masterProductClassification.getId() == 23) {

								lastValue = 0.0;

								BankRecurringDeposit bankRecurringDeposit = new BankRecurringDespositService()
										.getRecurringDepositCalculatedList(income.getAmount().doubleValue(),
												(income.getInterestCouponRate().doubleValue()), income.getTenure(), 0,
												income.getLookupFrequency3().getId(),
												income.getLookupFrequency1().getId(),
												income.getInvestmentDepositDate());
								double sumTotalSum = 0;
								for (BankRecurringDepositLookup bankRecurringDepositLookup : bankRecurringDeposit
										.getBankRecurringLookupList()) {
									//System.out.println("bankRecurringDepositLookup.getReferenceDate() "+bankRecurringDepositLookup.getReferenceDate());
									variableCal.setTime(
											simpleDateFormat.parse(bankRecurringDepositLookup.getReferenceDate()));
									
									
									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setCurrentValue(bankRecurringDepositLookup.getClosingBalance());
											//System.out.println("output.setCurrentValue( "+output.getCurrentValue());
											sumTotalSum = sumTotalSum + bankRecurringDepositLookup.getAmountDeposited();
										} else {
											output.setCurrentValue(lastValue);
										//	System.out.println("output.setCurrentValue(l "+output.getCurrentValue());
											
										}

										if (output.getCurrentValue() == 0
												&& (simpleDateFormat.parse(bankRecurringDeposit
														.getBankRecurringLookupList().get(0).getReferenceDate())
														.after(currentDate.getTime()))) {
											output.setCurrentValue(income.getAmount().doubleValue());
										}
										break;
									}
									sumTotalSum = sumTotalSum + bankRecurringDepositLookup.getAmountDeposited();
									lastValue = bankRecurringDepositLookup.getClosingBalance();
								}
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc(masterProductClassification.getProductName());
								// populating class and type description
								classLevelDescription(output, masterProductClassification);

								/*
								 * MasterLTSTClassification masterLTSTClassification = portFolioOverviewDAO
								 * .getMasterLTSTClassificationList(income.getTenure(), session);
								 */
								output.setSubAssetClassification(
										masterProductClassification.getLookupAssetClass().getDescription());

								output.setSubAssetClassification(
										setSubAssetClassificationForFixedIncome(income.getTenure(),
												income.getInvestmentDepositDate(), income.getTenureYearsDays()));
								output.setSubAssetClassificationId(
										setSubAssetClassificationIdForFixedIncome(income.getTenure(),
												income.getInvestmentDepositDate(), income.getTenureYearsDays()));

								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");

								} else {
									output.setInvestmentOrPerson("NA");
								}

								if (masterProductClassification.getLockedInFlag().equals("Y")) {

									output.setLockedInDate(
											simpleDateFormat.format(bankRecurringDeposit.getMaturityDate()));
									output.setMaturityDate(
											simpleDateFormat.format(bankRecurringDeposit.getMaturityDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											bankRecurringDeposit.getMaturityDate(), 1);
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);

								}
								output.setMaturityAmount(bankRecurringDeposit.getMaturityAmount());
								output.setInvestmentDate(simpleDateFormat.format(income.getInvestmentDepositDate()));
								output.setInvestmentValue(sumTotalSum);
								output.setCagr(
										String.valueOf(income.getInterestCouponRate().multiply(new BigDecimal(100))));
								// output.setPtpReturns("N/A");
								// current date total interest accrued == gain
								//new requirement
								output.setPtpReturns(
										String.valueOf(income.getInterestCouponRate().multiply(new BigDecimal(100))));
								//	output.setPtpReturns(String.valueOf(income.getInterestCouponRate().multiply(new BigDecimal(100))));
								output.setGains(output.getCurrentValue() - output.getInvestmentValue());
								//output.setPtpReturns("" + (((output.getCurrentValue() / output.getInvestmentValue()) - 1) * 100));

							}
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							//output.setPtpReturns("" + ((output.getCurrentValue() / output.getInvestmentValue()) - 1));
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							if(output.getCurrentValue() != 0) {
								outputList.add(output);
							}
							
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}
					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}

				// <<<<<<<<<<<<<<<<<< Fixed Income Ends >>>>>>>>>>>>>>>>>>>>>>>

				// Small Savings Starts
				try {
					List<ClientSmallSaving> clientSmallSavingList = portFolioOverviewDAO
							.getClientSmallSavingsList(clientId, session);

					for (ClientSmallSaving clientSmallSaving : clientSmallSavingList) {

						try {
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							masterProductClassification = clientSmallSaving.getMasterProductClassification();
							// check and populate market linked
							if (masterProductClassification != null) {
								output = getMarketLinkedCheck(output,
										masterProductClassification.getMarketLinkedFlag());
							} else {
								output.setMarketLinkOrFixedReturn("N/A");
							}
							populateFamilyRelation(
									clientSmallSaving.getClientFamilyMember().getLookupRelation().getDescription(),
									output);
							output.setPortFolioId(clientSmallSaving.getId());
							// NSC
							if (masterProductClassification.getId() == 25) {
								lastValue = 0.0;
								PONSCService ponscService = new PONSCService();
								PONSC ponsc = ponscService.getPONSCCalculationList(
										clientSmallSaving.getInvestmentAmount().doubleValue(),
										clientSmallSaving.getDepositTenure(),
										clientSmallSaving.getLookupFrequency1().getId(),
										clientSmallSaving.getStartDate());
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc(masterProductClassification.getProductName());
								for (PONSCLookup poNSCLookup : ponsc.getPoNScLookupList()) {
									variableCal.setTime(simpleDateFormat.parse(poNSCLookup.getReferenceDate()));

									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setCurrentValue(poNSCLookup.getClosingBalance());
										} else {
											output.setCurrentValue(lastValue);
										}
										if (output.getCurrentValue() == 0 && (simpleDateFormat
												.parse(ponsc.getPoNScLookupList().get(0).getReferenceDate())
												.after(currentDate.getTime()))) {
											output.setCurrentValue(
													clientSmallSaving.getInvestmentAmount().doubleValue());
										}
										break;
									}
									lastValue = poNSCLookup.getClosingBalance();
								}
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");

								} else {
									output.setInvestmentOrPerson("NA");
								}

								output.setInvestmentDate(simpleDateFormat.format(clientSmallSaving.getStartDate()));
								output.setInvestmentValue(clientSmallSaving.getInvestmentAmount().doubleValue());

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									// maturity date will itself be the locked in date
									output.setLockedInDate(
											simpleDateFormat.format(clientSmallSaving.getMaturityDate()));
									output.setSt_ltClassification("N/A");
								}
								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									output.setMaturityDate(
											simpleDateFormat.format(clientSmallSaving.getMaturityDate()));
								}
								double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),clientSmallSaving.getMaturityDate(), 1);
								output.setTimeToMaturity(String.valueOf(diff));
								output.setCagr(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								//new requirements
								output.setPtpReturns(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								/*output.setPtpReturns(
										String.valueOf((output.getCurrentValue() / output.getInvestmentValue() - 1) * 100));*/
								output.setGains(output.getCurrentValue() - output.getInvestmentValue());
								output.setMaturityAmount(ponsc.getMaturityAmount());
								output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
								output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							}

							// KVP
							if (masterProductClassification.getId() == 26) {

								lastValue = 0.0;
								KisanVikasPatraService kvpService = new KisanVikasPatraService();
								KisanVikasPatra kvp = kvpService.getKisanVikasPatraCalc(
										clientSmallSaving.getInvestmentAmount().doubleValue(),
										clientSmallSaving.getStartDate());
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc(masterProductClassification.getProductName());
								for (KisanVikasPatraLookup kisanVikasPatraLookup : kvp.getKisanVikasPatraLookupList()) {
									variableCal
									.setTime(simpleDateFormat.parse(kisanVikasPatraLookup.getReferenceDate()));

									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setCurrentValue(kisanVikasPatraLookup.getClosingBalance());
										} else {
											output.setCurrentValue(lastValue);
										}
										if (output.getCurrentValue() == 0 && (simpleDateFormat
												.parse(kvp.getKisanVikasPatraLookupList().get(0).getReferenceDate())
												.after(currentDate.getTime()))) {
											output.setCurrentValue(
													clientSmallSaving.getInvestmentAmount().doubleValue());
										}
										break;
									}
									lastValue = kisanVikasPatraLookup.getClosingBalance();
								}
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");

								} else {
									output.setInvestmentOrPerson("NA");
								}

								output.setInvestmentDate(simpleDateFormat.format(clientSmallSaving.getStartDate()));
								output.setInvestmentValue(clientSmallSaving.getInvestmentAmount().doubleValue());

								if (masterProductClassification.getLockedInFlag().equals("Y")) {

									output.setLockedInDate(kvp.getMaturityDate() == null ? ""
											: simpleDateFormat.format(kvp.getMaturityDate()));
									output.setMaturityDate(kvp.getMaturityDate() == null ? ""
											: simpleDateFormat.format(kvp.getMaturityDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(), kvp.getMaturityDate(), 1);
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);

								}

								output.setCagr(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								//new requirements
								output.setPtpReturns(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								/*output.setPtpReturns(
										String.valueOf((output.getCurrentValue() / output.getInvestmentValue() - 1) * 100));*/
								output.setGains(output.getCurrentValue() - output.getInvestmentValue());
								output.setMaturityAmount(kvp.getMaturityAmount());
								output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
								output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							}

							// PO Time Deposit
							if (masterProductClassification.getId() == 29) {
								lastValue = 0.0;
								POTimeDespositService poTimeDespositService = new POTimeDespositService();
								POTimeDeposit poTimeDeposit = poTimeDespositService.getTimeDepositCalculatedList(
										clientSmallSaving.getInvestmentAmount().doubleValue(),
										clientSmallSaving.getDepositTenure(),
										clientSmallSaving.getLookupFrequency1().getId(),
										clientSmallSaving.getLookupFrequency2().getId(),
										clientSmallSaving.getStartDate(),
										clientSmallSaving.getInterestRate().doubleValue());
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc(masterProductClassification.getProductName());
								double lastTotalrecived = 0;

								for (POTimeDepositLookup poTimeDepositLookup : poTimeDeposit
										.getPoTimeDepositLookupList()) {
									variableCal.setTime(simpleDateFormat.parse(poTimeDepositLookup.getReferenceDate()));

									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {

											output.setGains(poTimeDepositLookup.getTotalInterestReceived());
										} else {

											output.setGains(lastTotalrecived);
										}
										if (output.getCurrentValue() == 0 && (simpleDateFormat.parse(
												poTimeDeposit.getPoTimeDepositLookupList().get(0).getReferenceDate())
												.after(currentDate.getTime()))) {
											output.setCurrentValue(
													clientSmallSaving.getInvestmentAmount().doubleValue());
										}
										break;
									}
									lastTotalrecived = poTimeDepositLookup.getTotalInterestReceived();
								}
								output.setCurrentValue(clientSmallSaving.getInvestmentAmount().doubleValue());
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setSubAssetClassification(setSubAssetClassificationForFixedIncome(
										Integer.valueOf(clientSmallSaving.getDepositTenure()),
										clientSmallSaving.getStartDate(), "Y"));
								output.setSubAssetClassificationId(
										setSubAssetClassificationIdForFixedIncome(
												Integer.valueOf(clientSmallSaving.getDepositTenure()),
												clientSmallSaving.getStartDate(), "Y"));

								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");

								} else {
									output.setInvestmentOrPerson("NA");
								}

								output.setInvestmentDate(simpleDateFormat.format(clientSmallSaving.getStartDate()));
								output.setInvestmentValue(clientSmallSaving.getInvestmentAmount().doubleValue());

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									output.setLockedInDate(simpleDateFormat.format(poTimeDeposit.getMaturityDate()));
									output.setMaturityDate(simpleDateFormat.format(poTimeDeposit.getMaturityDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											poTimeDeposit.getMaturityDate(), 1);
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);
								}
								output.setCagr(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));

								//new requirements
								output.setPtpReturns(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								//output.setPtpReturns(String.valueOf((output.getCurrentValue() / output.getInvestmentValue() - 1) * 100));

								// output.setMaturityAmount(poTimeDeposit.getMaturityAmount());
								output.setMaturityAmount(clientSmallSaving.getInvestmentAmount().doubleValue());
							}

							// PO MIS
							if (masterProductClassification.getId() == 30) {

								lastValue = 0.0;
								PostOfficeMonthlyIncomeSchemeService poTimeDespositService = new PostOfficeMonthlyIncomeSchemeService();
								PostOfficeMonthlyIncomeScheme postOfficeMonthlyIncomeScheme = poTimeDespositService
										.getPostOfficeMISCal(clientSmallSaving.getInvestmentAmount().doubleValue(),
												clientSmallSaving.getDepositTenure(),
												clientSmallSaving.getLookupFrequency2().getId(),
												clientSmallSaving.getStartDate());

								for (PostOfficeMonthlyIncomeSchemeLookup postOfficeMonthlyIncomeSchemeLookup : postOfficeMonthlyIncomeScheme
										.getPostOfficeMISLookupList()) {
									variableCal.setTime(simpleDateFormat
											.parse(postOfficeMonthlyIncomeSchemeLookup.getReferenceDate()));
									if (((variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR))
											&& (variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)))) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setGains(
													postOfficeMonthlyIncomeSchemeLookup.getTotalInterestReceived());
										} else {
											output.setGains(lastValue);
										}
										if (output.getCurrentValue() == 0
												&& (simpleDateFormat
														.parse(postOfficeMonthlyIncomeScheme
																.getPostOfficeMISLookupList().get(0).getReferenceDate())
														.after(currentDate.getTime()))) {
											output.setCurrentValue(
													clientSmallSaving.getInvestmentAmount().doubleValue());
										}
										break;
									}

									lastValue = postOfficeMonthlyIncomeSchemeLookup.getTotalInterestReceived();
								}
								output.setCurrentValue(clientSmallSaving.getInvestmentAmount().doubleValue());
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc(masterProductClassification.getProductName());
								output.setCurrentValue(clientSmallSaving.getInvestmentAmount().doubleValue());
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");

								} else {
									output.setInvestmentOrPerson("NA");
								}

								output.setInvestmentDate(simpleDateFormat.format(clientSmallSaving.getStartDate()));

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									output.setLockedInDate(
											simpleDateFormat.format(postOfficeMonthlyIncomeScheme.getMaturityDate()));
									output.setMaturityDate(
											simpleDateFormat.format(postOfficeMonthlyIncomeScheme.getMaturityDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											postOfficeMonthlyIncomeScheme.getMaturityDate(), 1);
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);
								}

								output.setInvestmentValue(clientSmallSaving.getInvestmentAmount().doubleValue());
								output.setCagr(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								output.setPtpReturns(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								/*output.setPtpReturns(
										String.valueOf((output.getCurrentValue() / output.getInvestmentValue() - 1) * 100));*/
								output.setMaturityAmount(clientSmallSaving.getInvestmentAmount().doubleValue());
								output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
								output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							}

							// Senior Citizens Savings Scheme
							if (masterProductClassification.getId() == 31) {

								lastValue = 0.0;
								SeniorCitizenSavingSchemeService seniorCitizenSavingSchemeService = new SeniorCitizenSavingSchemeService();
								SeniorCitizenSavingScheme seniorCitizenSavingScheme = seniorCitizenSavingSchemeService
										.getSeniorCitizenSaingSchemeCal(
												clientSmallSaving.getInvestmentAmount().doubleValue(),
												clientSmallSaving.getDepositTenure(),
												clientSmallSaving.getLookupFrequency2().getId(),
												clientSmallSaving.getStartDate());

								for (SeniorCitizenSavingSchemeLookup seniorCitizenSavingSchemeLookup : seniorCitizenSavingScheme
										.getSeniorCitizenSavingSchemeLookupsList()) {
									variableCal.setTime(
											simpleDateFormat.parse(seniorCitizenSavingSchemeLookup.getReferenceDate()));
									if (((variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR))
											&& (variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)))) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setGains(seniorCitizenSavingSchemeLookup.getTotalInterestReceived());
										} else {
											output.setGains(lastValue);
										}
										if (output.getCurrentValue() == 0 && (simpleDateFormat.parse(
												seniorCitizenSavingScheme.getSeniorCitizenSavingSchemeLookupsList()
												.get(0).getReferenceDate())
												.after(currentDate.getTime()))) {
											output.setCurrentValue(
													clientSmallSaving.getInvestmentAmount().doubleValue());
										}
										break;
									}

									lastValue = seniorCitizenSavingSchemeLookup.getTotalInterestReceived();
								}
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc(masterProductClassification.getProductName());
								output.setCurrentValue(clientSmallSaving.getInvestmentAmount().doubleValue());
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");

								} else {
									output.setInvestmentOrPerson("NA");
								}

								output.setInvestmentDate(simpleDateFormat.format(clientSmallSaving.getStartDate()));
								output.setInvestmentValue(clientSmallSaving.getInvestmentAmount().doubleValue());

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									output.setLockedInDate(
											simpleDateFormat.format(seniorCitizenSavingScheme.getMaturityDate()));
									output.setMaturityDate(
											simpleDateFormat.format(seniorCitizenSavingScheme.getMaturityDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											seniorCitizenSavingScheme.getMaturityDate(), 1);
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);
								}

								output.setCagr(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								//new requirement
								output.setPtpReturns(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								/*output.setPtpReturns(
										String.valueOf((output.getCurrentValue() / output.getInvestmentValue() - 1) * 100));*/
								output.setMaturityAmount(clientSmallSaving.getInvestmentAmount().doubleValue());
								output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
								output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							}

							// PO Recurring Deposits
							if (masterProductClassification.getId() == 28) {

								lastValue = 0.0;
								PORecurringDespositService poRecurringDespositService = new PORecurringDespositService();
								PORecurringDeposit poRecurringDeposit = poRecurringDespositService
										.getRecurringDepositCalculatedList(
												clientSmallSaving.getInvestmentAmount().doubleValue(),
												clientSmallSaving.getDepositTenure(),
												clientSmallSaving.getLookupFrequency3().getId(),
												clientSmallSaving.getLookupFrequency1().getId(),
												clientSmallSaving.getStartDate());
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc(masterProductClassification.getProductName());
								double sumTotalSum = 0;
								for (PORecurringDepositLookup poRecurringDepositLookup : poRecurringDeposit
										.getPoRecurringLookupList()) {
									variableCal.setTime(
											simpleDateFormat.parse(poRecurringDepositLookup.getReferenceDate()));

									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setCurrentValue(poRecurringDepositLookup.getClosingBalance());
										} else {
											output.setCurrentValue(lastValue);
										}
										// sumTotalSum = sumTotalSum +
										// poRecurringDepositLookup.getTotalInterestAccrued();
										if (output.getCurrentValue() == 0 && (simpleDateFormat.parse(
												poRecurringDeposit.getPoRecurringLookupList().get(0).getReferenceDate())
												.after(currentDate.getTime()))) {
											output.setCurrentValue(
													clientSmallSaving.getInvestmentAmount().doubleValue());
										}
										break;
									}
									sumTotalSum = sumTotalSum + poRecurringDepositLookup.getAmountDeposited();
									lastValue = poRecurringDepositLookup.getClosingBalance();
								}
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");

								} else {
									output.setInvestmentOrPerson("NA");
								}

								output.setInvestmentDate(simpleDateFormat.format(clientSmallSaving.getStartDate()));

								if (masterProductClassification.getLockedInFlag().equals("Y")) {
									output.setLockedInDate(
											simpleDateFormat.format(poRecurringDeposit.getMaturityDate()));
									output.setMaturityDate(
											simpleDateFormat.format(poRecurringDeposit.getMaturityDate()));
									double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
											poRecurringDeposit.getMaturityDate(), 1);
									output.setTimeToMaturity(String.valueOf(diff));
									// check st lt classification
									output = getSTLTClassificationCheck(output, diff);
								}
								output.setInvestmentValue(sumTotalSum);
								output.setInvestmentDate(simpleDateFormat.format(clientSmallSaving.getStartDate()));
								output.setCagr(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								//new requirements
								output.setPtpReturns(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								/*output.setPtpReturns(
										String.valueOf((output.getCurrentValue() / output.getInvestmentValue() - 1) * 100));*/
								output.setGains(output.getCurrentValue() - output.getInvestmentValue());
								output.setMaturityAmount(poRecurringDeposit.getMaturityAmount());
								output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
								output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							}

							// Sukanya Samriddhi Scheme
							if (masterProductClassification.getId() == 32) {
								currentDate = Calendar.getInstance();
								lastValue = 0.0;
								double sumTotalSum = 0;
								double gains = 0.0;
								SukanyaSamriddhiSchemeService sukanyaSamriddhiSchemeService = new SukanyaSamriddhiSchemeService();
								SukanyaSamriddhiScheme sukanyaSamriddhiScheme = sukanyaSamriddhiSchemeService
										.getSukanyaSamriddhiSchemeList(
												clientSmallSaving.getInvestmentAmount().doubleValue(), "",
												clientSmallSaving.getDepositTenure(),
												clientSmallSaving.getLookupFrequency3().getId(),
												clientSmallSaving.getLookupFrequency1().getId(),
												clientSmallSaving.getStartDate(),
												clientSmallSaving.getMaturityTenure());
								output.setProductName(masterProductClassification.getProductName());
								output.setProductDesc(masterProductClassification.getProductName());
								// currentDate.set(Calendar.DAY_OF_MONTH,
								// currentDate.getActualMaximum(Calendar.DAY_OF_MONTH)); //
								// System.out.println(currentDate.getTime());
								for (SukanyaSamriddhiSchemeLookup sukanyaSamriddhiSchemeLookup : sukanyaSamriddhiScheme
										.getSukanyaSamSchLookupList()) {
									variableCal.setTime(
											simpleDateFormat.parse(sukanyaSamriddhiSchemeLookup.getReferenceDate()));

									if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
											&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
										if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
												.get(Calendar.DAY_OF_MONTH)) {
											output.setCurrentValue(sukanyaSamriddhiSchemeLookup.getClosingBalance());
										} else {
											output.setCurrentValue(lastValue);
										}
										/*
										 * sumTotalSum = sumTotalSum +
										 * sukanyaSamriddhiSchemeLookup.getAmountDeposited();
										 */
										if (output.getCurrentValue() == 0
												&& (simpleDateFormat
														.parse(sukanyaSamriddhiScheme.getSukanyaSamSchLookupList()
																.get(0).getReferenceDate())
														.after(currentDate.getTime()))) {
											output.setCurrentValue(
													clientSmallSaving.getInvestmentAmount().doubleValue());
										}
										break;
									}
									sumTotalSum = sumTotalSum + sukanyaSamriddhiSchemeLookup.getAmountDeposited();
									lastValue = sukanyaSamriddhiSchemeLookup.getClosingBalance();
									gains = gains + sukanyaSamriddhiSchemeLookup.getInterestCredited();
								}
								// populating class and type description
								classLevelDescription(output, masterProductClassification);
								output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
								if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
									output.setInvestmentOrPerson("Investment Assets");

								} else {
									output.setInvestmentOrPerson("NA");
								}

								output.setInvestmentDate(simpleDateFormat.format(clientSmallSaving.getStartDate()));
								output.setInvestmentValue(sumTotalSum);
								output.setGains(gains);
								output.setLockedInDate(
										simpleDateFormat.format(sukanyaSamriddhiScheme.getDepositMaturityDate()));
								output.setMaturityDate(
										simpleDateFormat.format(sukanyaSamriddhiScheme.getDepositMaturityDate()));
								double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
										sukanyaSamriddhiScheme.getDepositMaturityDate(), 1);
								output.setTimeToMaturity(String.valueOf(diff));
								// check st lt classification
								output = getSTLTClassificationCheck(output, diff);

								output.setCagr(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								//new requirements
								output.setPtpReturns(String
										.valueOf(clientSmallSaving.getInterestRate().multiply(new BigDecimal(100))));
								/*output.setPtpReturns(
										String.valueOf((output.getCurrentValue() / output.getInvestmentValue() - 1) * 100));*/

								output.setMaturityAmount(sukanyaSamriddhiScheme.getMaturityAmount());
								output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
								output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							}
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							if(output.getCurrentValue() != 0) {
								outputList.add(output);
							}
							
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}
					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}
				// <<<<<<<<<<<<<<<<< Small Savings Ends >>>>>>>>>>>>>>>>>

				// Mutual Funds Start
				try {
					List<ClientMutualFund> clientMutualFundList = portFolioOverviewDAO.getClientMutualFundList(clientId,
							session);
					// MutualFunds
					for (ClientMutualFund clientMutualFund : clientMutualFundList) {
						PortfolioOverviewDto output = new PortfolioOverviewDto();  
						//if(clientMutualFund.getId()==170) {
						try {
							lastValue = 0.0;
							MutualFundLumpsumSipService mutualFundLumpsumSipService = new MutualFundLumpsumSipService();
							MutualFundLumpsumSip mutualFundLumpsumSip = null;
							masterProductClassification = clientMutualFund.getMasterProductClassification();
							output.setProductId(masterProductClassification.getId());
							MasterMutualFundETF masterMutualFundETF = null;
							// if not PMS
							if (masterProductClassification.getId() != 21) {
								masterMutualFundETF = portFolioOverviewDAO
										.getMasterMutualFundETF(clientMutualFund.getMasterMutualFundEtf().getIsin(), session);
							}
							output.setProductId(clientMutualFund.getId());
							populateFamilyRelation(
									clientMutualFund.getClientFamilyMember().getLookupRelation().getDescription(),
									output);
							output.setPortFolioId(clientMutualFund.getId());

							// Mutual Funds without AMFI code , database needs to be changed. currently not
							// adding the MF without AMFI code
							if (masterMutualFundETF == null) {
								masterMutualFundETF = new MasterMutualFundETF();
								masterMutualFundETF.setLookupAssetSubClass(clientMutualFund.getLookupAssetSubClass());
								masterMutualFundETF.setLookupAssetClass(
										clientMutualFund.getLookupAssetSubClass().getLookupAssetClass());
								// masterMutualFundETF.setSchemeName(clientMutualFund.getPmsID().get);
							}
							// For PMS Investment mode is null
							if (clientMutualFund.getLookupFundInvestmentMode() == null) {
								
								// * output.setCagr(String.valueOf((Math.pow(
								// * clientMutualFund.getCurrentMarketValue().doubleValue() /
								// * clientMutualFund.getInvestmentAmount().doubleValue(), 1 /
								// * FinanceUtil.YEARFRAC(clientMutualFund.getInvestmentStartDate(),
								// * Calendar.getInstance().getTime(), 1)) - 1) * 100));
								 

								double diff = FinanceUtil.YEARFRAC(clientMutualFund.getInvestmentStartDate(),
										Calendar.getInstance().getTime(), 1);
								double absoluteReturn = (clientMutualFund.getCurrentMarketValue().doubleValue()
										/ clientMutualFund.getInvestmentAmount().doubleValue()) - 1;
								if (diff <= 1) {
									output.setCagr(String.valueOf(absoluteReturn * 100));
								} else {
									double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;
									output.setCagr(String.valueOf(cagrCal * 100));
								}
								output.setPtpReturns(String.valueOf(absoluteReturn * 100));
								
								/* * output.setCagr(cagrCal(clientMutualFund.getCurrentMarketValue().doubleValue()
								 * , clientMutualFund.getInvestmentAmount().doubleValue(),
								 * clientMutualFund.getInvestmentStartDate()));*/
								 
								output.setInvestmentValue(clientMutualFund.getInvestmentAmount().doubleValue());
								output.setCurrentValue(clientMutualFund.getCurrentMarketValue().doubleValue());
								output.setLockedInDate(clientMutualFund.getMfLumpsumLockedInDate() == null ? "N/A" : ""+(clientMutualFund.getMfLumpsumLockedInDate()));
							} else { 
								// FundInvestment Mode is 1 for lumpsum
								if (clientMutualFund.getLookupFundInvestmentMode().getId() == 1 && clientMutualFund.getLumpsumUnitsPurchased() != null) {
									
							/*		
									mutualFundLumpsumSip = mutualFundLumpsumSipService.getMutualFundLumpsumCalculation(
											clientMutualFund.getInvestmentAmount().doubleValue(),
											clientMutualFund.getIsin(), clientMutualFund.getInvestmentStartDate(),
											clientMutualFund.getLumpsumUnitsPurchased().intValue());
									
									
									//It is already being multiplied with 100 in product calculator
									//output.setPtpReturns(String.valueOf(mutualFundLumpsumSip.getPtpReturns() * 100));
									output.setPtpReturns(String.valueOf(mutualFundLumpsumSip.getPtpReturns()));
									output.setInvestmentValue(clientMutualFund.getInvestmentAmount().doubleValue());
								
									for (MutualFundLumpsumSipLookup mutualFundLumpsumSipLookup : mutualFundLumpsumSip
											.getMutualFundLumpsumSipList()) {
										variableCal.setTime(mutualFundLumpsumSipLookup.getRefDate());

										if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
												&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
											if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
													.get(Calendar.DAY_OF_MONTH)) {
												output.setCurrentValue(mutualFundLumpsumSipLookup.getPortfolioValue());
											} else {
												output.setCurrentValue(lastValue);
											}
											break;
										}
										lastValue = mutualFundLumpsumSipLookup.getPortfolioValue();
									}

									//ETF
									if(clientMutualFund.getMasterProductClassification().getId()==20) {
										double diff = FinanceUtil.YEARFRAC(clientMutualFund.getInvestmentStartDate(),
												Calendar.getInstance().getTime(), 1);

										//	clientMutualFund.setCurrentMarketValue(new BigDecimal(139350));

										double absoluteReturn = (output.getCurrentValue()
												/ clientMutualFund.getInvestmentAmount().doubleValue()) - 1;
										if (diff <= 1) {
											output.setCagr(String.valueOf(absoluteReturn * 100));
										} else {
											double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;
											output.setCagr(String.valueOf(cagrCal * 100));
										}
									}else {
										output.setCagr(String.valueOf(mutualFundLumpsumSip.getCacgr() * 100));
									}
									
									*/
									
									
									///new Code
									MutualFundLumpsumSipLookup mutualFundLumpsumSipLookup=mutualFundLumpsumSipService.getMutualFundLumpsumCalculationPortfolio(clientMutualFund.getInvestmentAmount().doubleValue(),
											clientMutualFund.getMasterMutualFundEtf().getIsin(), clientMutualFund.getInvestmentStartDate(),
											clientMutualFund.getLumpsumUnitsPurchased().intValue());
									
									
									
									output.setInvestmentValue(clientMutualFund.getInvestmentAmount().doubleValue());
									if(mutualFundLumpsumSipLookup != null) {
									output.setCurrentValue(mutualFundLumpsumSipLookup.getCurrentportfoliovalue());
									output.setGains(mutualFundLumpsumSipLookup.getGainLoss());
									//output.setCagr(String.valueOf(mutualFundLumpsumSipLookup.getAnnualisedCagrReturns()*100));
									output.setPtpReturns(String.valueOf(mutualFundLumpsumSipLookup.getPTPReturn()*100));
								
									}
									output.setLockedInDate(clientMutualFund.getMfLumpsumLockedInDate() == null ? "N/A" : ""+(clientMutualFund.getMfLumpsumLockedInDate()));
								
								//	if(clientMutualFund.getMasterProductClassification().getId()==20) {
										double diff = FinanceUtil.YEARFRAC(clientMutualFund.getInvestmentStartDate(),
												Calendar.getInstance().getTime(), 1);

										//	clientMutualFund.setCurrentMarketValue(new BigDecimal(139350));

										double absoluteReturn = (output.getCurrentValue()/ output.getInvestmentValue()) - 1;
										if (diff <= 1) {
											 output.setCagr(String.valueOf(absoluteReturn * 100));
											//output.setCagr(String.valueOf(mutualFundLumpsumSipLookup.getPTPReturn()*100));
											
										} else {
											//double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;
											//output.setCagr(String.valueOf(cagrCal * 100));
											output.setCagr(String.valueOf(mutualFundLumpsumSipLookup.getXirr()*100));
										}
									//}
								
								} else {
									
									if (clientMutualFund.getSipInstalments() != null) {
										//new Code
										MutualFundLumpsumSipLookup mutualFundLumpsumSipLookup = mutualFundLumpsumSipService.getMutualFundSIPCalculationPortFolio(clientMutualFund.getInvestmentAmount().doubleValue(),
												clientMutualFund.getMasterMutualFundEtf().getIsin(), clientMutualFund.getInvestmentStartDate(),
												clientMutualFund.getSipInstalments(), clientMutualFund.getSipFrequency());
										if(mutualFundLumpsumSipLookup != null) {
										//System.out.println("ptp "+mutualFundLumpsumSipLookup.getPTPReturn());
										output.setInvestmentValue(mutualFundLumpsumSipLookup.getTotalInvestmentValue());
										output.setCurrentValue(mutualFundLumpsumSipLookup.getCurrentportfoliovalue());
										output.setGains(mutualFundLumpsumSipLookup.getGainLoss());
										output.setCagr(String.valueOf(mutualFundLumpsumSipLookup.getXirr()*100));
										output.setPtpReturns(String.valueOf(mutualFundLumpsumSipLookup.getPTPReturn()*100));
										}
										output.setLockedInDate(clientMutualFund.getMfLumpsumLockedInDate() == null ? "N/A" : ""+(clientMutualFund.getMfLumpsumLockedInDate()));
										
										/*
										mutualFundLumpsumSip = mutualFundLumpsumSipService.getMutualFundSIPCalculation(
												clientMutualFund.getInvestmentAmount().doubleValue(),
												clientMutualFund.getIsin(), clientMutualFund.getInvestmentStartDate(),
												clientMutualFund.getSipInstalments(), clientMutualFund.getSipFrequency());
										output.setCagr(String.valueOf(mutualFundLumpsumSip.getXirr() * 100));
										if (mutualFundLumpsumSip.getTotalInvestedAmount() > 0) {
											output.setPtpReturns(
													String.valueOf((mutualFundLumpsumSip.getPortfolioValue()/ mutualFundLumpsumSip.getTotalInvestedAmount() - 1)*100));
										}
										output.setLockedInDate(clientMutualFund.getMfLumpsumLockedInDate() == null ? "N/A" : ""+(clientMutualFund.getMfLumpsumLockedInDate()));
										double sumAmount = 0;
										double lastSum =0;
										Calendar displayDate = Calendar.getInstance();
										//installmentEndDate.setTime(clientMutualFund.getInvestmentStartDate());
										//installmentEndDate.add(Calendar.MONTH,clientMutualFund.getSipFrequency());
										int count = 1;
										double sipInstallmentEndInvestment = 0;
										int sipInstallmentEndInvestmentFlag = 0;
										for (MutualFundLumpsumSipLookup mutualFundLumpsumSipLookup : mutualFundLumpsumSip
												.getMutualFundLumpsumSipList()) {
											variableCal.setTime(mutualFundLumpsumSipLookup.getRefDate());
											if (variableCal.getTime().before(displayDate.getTime())) {
												sumAmount = sumAmount + mutualFundLumpsumSipLookup.getAmountInvested();
											}
											
											
											if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
													&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
												if ((variableCal.get(Calendar.DAY_OF_MONTH) < currentDate
														.get(Calendar.DAY_OF_MONTH))) {
													if(sipInstallmentEndInvestmentFlag != 1){
														output.setInvestmentValue(sumAmount);
												        }
													
													output.setCurrentValue(mutualFundLumpsumSipLookup.getPortfolioValue());
												} else {
													if(sipInstallmentEndInvestmentFlag != 1){
														output.setInvestmentValue(lastSum);
													}
													
													output.setCurrentValue(lastValue);
												}
												
												break;
											}
											lastSum = sumAmount;
											lastValue = mutualFundLumpsumSipLookup.getPortfolioValue();
											
											if(clientMutualFund.getSipInstalments() == count ) {
												System.out.println("sumAmount count  "+sumAmount);
												sipInstallmentEndInvestmentFlag = 1;
												sipInstallmentEndInvestment = sumAmount;
												output.setInvestmentValue(sipInstallmentEndInvestment);
												//break;
											}
											count ++;
										}
									*/
										
										
										
										
									}
								}
							}

							output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
							output.setProductName(masterProductClassification.getProductName());
							// for PMS
							if (masterProductClassification.getId() == 21) {
								output.setProductDesc(clientMutualFund.getPmsSchemeName());
								output.setBankIssuerName(clientMutualFund.getPmsSchemeName());
								output.setLockedInDate("N/A");
							} else {
								//fire query to accordSchemeIsinMaster by Isin
								
								// if result one
								//String series = portFolioOverviewDAO.getAccordSchemeIsinMaster(masterMutualFundETF.getIsin(), session);
								// if result more than one
								List<String> seriesList = portFolioOverviewDAO.getAccordSchemeIsinMasterList(masterMutualFundETF.getIsin(), session);
								//System.out.println("accordSchemeIsinMaster "+series);
								if(seriesList != null && masterMutualFundETF != null) {
								output.setProductDescLong(masterMutualFundETF.getSchemeName()+"-"+seriesList.get(0));
								}
								output.setProductDesc(masterMutualFundETF.getSchemeName());
								output.setBankIssuerName(masterMutualFundETF.getSchemeName());
							}
							output.setAssetClassification(masterMutualFundETF.getLookupAssetClass().getDescription());
							output.setAssetClassificationId((masterMutualFundETF.getLookupAssetClass().getId()));
							output.setSubAssetClassificationId(masterMutualFundETF.getLookupAssetSubClass().getId());
							if (output.getSubAssetClassificationId() == 1) {
								// if cash
								if(clientMutualFund.getMfLumpsumLockedInDate() == null) {
									output.setMaturityDate("N/A");
								}
							}
							output.setSubAssetClassification(
									masterMutualFundETF.getLookupAssetSubClass().getDescription());
							output.setProductType(masterProductClassification.getMasterProductType().getProductType());

							output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_INVESTMENT);

							output.setMarketLinkOrFixedReturn(FinexaConstant.MARKET_LINKED);
							output.setInvestmentDate(
									simpleDateFormat.format(clientMutualFund.getInvestmentStartDate()));
							if(clientMutualFund.getMasterMutualFundEtf() != null) {
								output.setIsin(clientMutualFund.getMasterMutualFundEtf().getIsin());
							}
							output.setGains(output.getCurrentValue() - output.getInvestmentValue());
							//gourab
							//output.setMaturityAmount(clientMutualFund.get);
							// maturity date , amount locked in date st lt
							// classification
							// remaing on hold for master data to come

							// fetch capRank
							MasterMFMarketCap masterMFMarketCap = null;
							if(clientMutualFund.getMasterMutualFundEtf() != null) {
								masterMFMarketCap = portFolioOverviewDAO.getMasterMFMarketCap(clientMutualFund.getMasterMutualFundEtf().getIsin(),
										session);
							}
							double totalCap = 0.0;
							if (masterMFMarketCap != null) {
								output.setLargeCapMFPerc(masterMFMarketCap.getLargecap() == null ? 0.0
										: masterMFMarketCap.getLargecap().doubleValue());
								totalCap = totalCap + (masterMFMarketCap.getLargecap() == null ? 0.0
										: masterMFMarketCap.getLargecap().doubleValue());
								output.setSmallcapMFPerc(masterMFMarketCap.getSmallcap() == null ? 0.0
										: masterMFMarketCap.getSmallcap().doubleValue());
								totalCap = totalCap + (masterMFMarketCap.getSmallcap() == null ? 0.0
										: masterMFMarketCap.getSmallcap().doubleValue());
								output.setMidCapMFPerc(masterMFMarketCap.getMidcap() == null ? 0.0
										: masterMFMarketCap.getMidcap().doubleValue());
								totalCap = totalCap + (masterMFMarketCap.getMidcap() == null ? 0.0
										: masterMFMarketCap.getMidcap().doubleValue());

								if (totalCap < 100.0) {
									double otherCap = 100.0 - totalCap;
									output.setOtherCapMFPerc(otherCap);
								}

								output.setCapRank(masterMFMarketCap.getCapRank());
								output.setCapRankValue(masterMFMarketCap.getInvestmentStyle());
							} else {
								output.setLargeCapMFPerc(0.0);
								output.setSmallcapMFPerc(0.0);
								output.setMidCapMFPerc(0.0);
								output.setOtherCapMFPerc(0.0);
							}
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());

							// getting maturity Profile of Mutual Fund
							Map<String, Double> masterMaturityProfileMap = new LinkedHashMap<>();
							MasterMFMaturityProfile masterMfMaturityProf = portFolioOverviewDAO
									.getMasterMFMaturityProfile(clientMutualFund.getMasterMutualFundEtf().getIsin(), session);

							if (masterMfMaturityProf != null) {/*
								masterMaturityProfileMap.put("0-1M",
										(double) ((masterMfMaturityProf.get__1m() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("1-3M",
										(double) ((masterMfMaturityProf.get__3m() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("3-6M",
										(double) ((masterMfMaturityProf.get__6m() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("6-9M",
										(double) ((masterMfMaturityProf.get__9m() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("9-12M",
										(double) ((masterMfMaturityProf.get__12m() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("1-2Y",
										(double) ((masterMfMaturityProf.get__2y() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("2-3Y",
										(double) ((masterMfMaturityProf.get__3y() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("3-5Y",
										(double) ((masterMfMaturityProf.get__5y() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("5-7Y",
										(double) ((masterMfMaturityProf.get__7y() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("7-10Y",
										(double) ((masterMfMaturityProf.get__10y() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("10-15Y",
										(double) ((masterMfMaturityProf.get_0_15y() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("15Y+",
										(double) ((masterMfMaturityProf.get_5y_() / 100) * output.getCurrentValue()));
								masterMaturityProfileMap.put("Others",
										(double) ((masterMfMaturityProf.getOthers() / 100) * output.getCurrentValue()));

							*/
								double share = output.getCurrentValue()/totalCurrentValue;
								  
								   System.out.println("masterMfMaturityProf.get__1m() "+masterMfMaturityProf.get__1m());
								   System.out.println("masterMfMaturityProf.get__1m() / 100 "+masterMfMaturityProf.get__1m() / 100);
								
								masterMaturityProfileMap.put("0-1M",
										(double) (masterMfMaturityProf.get__1m()));
								masterMaturityProfileMap.put("1-3M",
										(double) (masterMfMaturityProf.get__3m()));
								masterMaturityProfileMap.put("3-6M",
										(double) (masterMfMaturityProf.get__6m()));
								masterMaturityProfileMap.put("6-9M",
										(double) (masterMfMaturityProf.get__9m()));
								masterMaturityProfileMap.put("9-12M",
										(double) (masterMfMaturityProf.get__12m()));
								masterMaturityProfileMap.put("1-2Y",
										(double) (masterMfMaturityProf.get__2y()));
								masterMaturityProfileMap.put("2-3Y",
										(double) (masterMfMaturityProf.get__3y()));
								masterMaturityProfileMap.put("3-5Y",
										(double) (masterMfMaturityProf.get__5y()));
								masterMaturityProfileMap.put("5-7Y",
										(double) (masterMfMaturityProf.get__7y()));
								masterMaturityProfileMap.put("7-10Y",
										(double) (masterMfMaturityProf.get__10y()));
								masterMaturityProfileMap.put("10-15Y",
										(double) (masterMfMaturityProf.get_0_15y()));
								masterMaturityProfileMap.put("15Y+",
										(double) (masterMfMaturityProf.get_5y_()));
								masterMaturityProfileMap.put("Others",
										(double) (masterMfMaturityProf.getOthers()));
}
							output.setMfMaturityProfile(masterMaturityProfileMap);

							// adding risk measures of MF
							Map<String, Float> riskMeasureMap = new HashMap<>();
							MasterMFRatio masterMFRatio = portFolioOverviewDAO
									.getRiskMeasureMap(clientMutualFund.getMasterMutualFundEtf().getIsin(), session);

							if (masterMFRatio != null) {
								/*System.out.println("getBetaX "+masterMFRatio.getId().getIsin());
								System.out.println("getBetaX "+masterMFRatio.getBetaX());
								System.out.println("getSharpeX "+masterMFRatio.getSharpeX());
								System.out.println("getJalphaX "+masterMFRatio.getJalphaX());
								System.out.println("getSortinoX "+masterMFRatio.getSortinoX());
								System.out.println("getTreynorX() "+masterMFRatio.getTreynorX());
								*/
								
								riskMeasureMap.put("Beta",
										(masterMFRatio.getBetaX() == null ? 0 :  masterMFRatio.getBetaX()));
								riskMeasureMap.put("Treynor Ratio", (masterMFRatio.getTreynorX() == null ? 0
										:  masterMFRatio.getTreynorX()));
								riskMeasureMap.put("Sharpe Ratio",
										(masterMFRatio.getSharpeX() == null ? 0 :  masterMFRatio.getSharpeX()));
								riskMeasureMap.put("Alpha",
										(masterMFRatio.getJalphaX() == null ? 0 :  masterMFRatio.getJalphaX()));
								riskMeasureMap.put("Sortino", (masterMFRatio.getSortinoX() == null ? 0
										:  masterMFRatio.getSortinoX()));
							}
							output.setRiskMeasureMap(riskMeasureMap);

						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}
						if (output.getLockedInDate() != null && !output.getLockedInDate().isEmpty() && 
								!output.getLockedInDate().equals("N/A")) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date dt = sdf.parse(output.getLockedInDate()); 
							output.setMaturityDate(simpleDateFormat.format(dt));
							if (lastValue > 0) {
								output.setMaturityAmount(lastValue);
							}
						}
						if(output.getInvestmentValue() != 0) {
						outputList.add(output);
						}
					 // }//testing
					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}

				// Mutual Funds End * ********************//

				// Client Cash Starts
				try {
					List<ClientCash> clientcashList = portFolioOverviewDAO.getClientCashList(clientId, session);
					for (ClientCash clientcash : clientcashList) {
						try {
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							output.setPortFolioId(clientcash.getId());
							masterProductClassification = clientcash.getMasterProductClassification();
							output.setProductName(masterProductClassification.getProductName());
							output.setBankIssuerName(clientcash.getMasterCash() != null ? clientcash.getMasterCash().getName() : "Cash");
							populateFamilyRelation(
									clientcash.getClientFamilyMember().getLookupRelation().getDescription(), output);
							// populating class and type description
							classLevelDescription(output, masterProductClassification);
							output.setProductDesc(clientcash.getMasterCash() != null ? clientcash.getMasterCash().getName() : "");
							output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
							output.setCurrentValue(clientcash.getCurrentBalance().doubleValue());
							if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
								output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_INVESTMENT);

							} else {
								output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_PERSONAL);
							}
							output.setMarketLinkOrFixedReturn("N/A");
							output.setCagr(String.valueOf("N/A"));
							output.setPtpReturns(String.valueOf("N/A"));
							output.setInvestmentDate(simpleDateFormat.format(Calendar.getInstance().getTime()));
							output.setInvestmentValue(clientcash.getCurrentBalance().doubleValue());
							output.setLockedInDate(FinexaConstant.LOCKED_IN_DATE_AVAILABLE);
							output.setMaturityDate("N/A");
							output.setSt_ltClassification("N/A");
							output.setGains(0);
							output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
							output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							outputList.add(output);
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}
					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}

				// Client Cash Ends ****************

				// Client Precious Metal Like Jewellery Starts
				try {
					List<ClientPreciousMetal> clientPreciousMetalList = portFolioOverviewDAO
							.getClientPreciousList(clientId, session);
					for (ClientPreciousMetal clientPreciousMetal : clientPreciousMetalList) {
						try {
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							masterProductClassification = clientPreciousMetal.getMasterProductClassification();
							output.setPortFolioId(clientPreciousMetal.getId());
							output.setProductName(masterProductClassification.getProductName());
							output.setProductDesc(clientPreciousMetal.getDescription());
							output.setBankIssuerName(clientPreciousMetal.getDescription());
							populateFamilyRelation(
									clientPreciousMetal.getClientFamilyMember().getLookupRelation().getDescription(),
									output);
							// populating class and type description
							classLevelDescription(output, masterProductClassification);
							if (clientPreciousMetal.getLookupAlternateInvestmentsAssetType().getId() == 1) {
								output.setInvestmentOrPersonFlag("Y");
							} else {
								output.setInvestmentOrPersonFlag("N");
							}

							// checking locked in flag
							output = getLockedInDateCheck(output, masterProductClassification.getLockedInFlag());

							output.setInvestmentOrPerson(
									clientPreciousMetal.getLookupAlternateInvestmentsAssetType().getDescription());

							output.setMarketLinkOrFixedReturn(FinexaConstant.MARKET_LINKED);
							output.setCurrentValue(clientPreciousMetal.getCurrentValue().doubleValue());
							output.setInvestmentDate(simpleDateFormat.format(clientPreciousMetal.getInvestmentDate()));
							output.setInvestmentValue(clientPreciousMetal.getInvestmentValue().doubleValue());
							output.setGains(output.getCurrentValue() - output.getInvestmentValue());
							output.setLockedInDate(FinexaConstant.LOCKED_IN_DATE_AVAILABLE);
							output.setMaturityDate("N/A");
							output.setSt_ltClassification("-");
							output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							// custom Cagr Calculation
							double diff = FinanceUtil.YEARFRAC(clientPreciousMetal.getInvestmentDate(),
									Calendar.getInstance().getTime(), 1);
							double absoluteReturn = (clientPreciousMetal.getCurrentValue().doubleValue()
									/ clientPreciousMetal.getInvestmentValue().doubleValue()) - 1;
							if (diff < 1) {
								output.setCagr(String.valueOf(absoluteReturn * 100));
							} else {
								double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;

								output.setCagr(String.valueOf(cagrCal * 100));
							}
							output.setPtpReturns(String.valueOf((output.getCurrentValue()/output.getInvestmentValue() - 1) * 100));
							/*
							 * output.setCagr(cagrCal(clientPreciousMetal.getCurrentValue().doubleValue(),
							 * clientPreciousMetal.getInvestmentValue().doubleValue(),
							 * clientPreciousMetal.getInvestmentDate()));
							 */
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							outputList.add(output);
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}
					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}

				// *******Client Precious Metal Like Jewellery Ends ************

				// ************ Client RealEstate Starts ******
				try {
					List<ClientRealEstate> clientRealEstateList = portFolioOverviewDAO.getClientRealEstateList(clientId,
							session);
					for (ClientRealEstate clientRealEstate : clientRealEstateList) {
						try {
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							masterProductClassification = clientRealEstate.getMasterProductClassification();
							output.setPortFolioId(clientRealEstate.getId());
							output.setProductName(masterProductClassification.getProductName());
							output.setProductDesc(clientRealEstate.getDescription());
							output.setBankIssuerName(clientRealEstate.getDescription());
							output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
							output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							populateFamilyRelation(
									clientRealEstate.getClientFamilyMember().getLookupRelation().getDescription(),
									output);
							// populating class and type description
							classLevelDescription(output, masterProductClassification);

							// checking locked in flag
							output = getLockedInDateCheck(output, masterProductClassification.getLockedInFlag());
							if (clientRealEstate.getLookupAlternateInvestmentsAssetType().getId() == 1) {
								output.setInvestmentOrPersonFlag("Y");
							} else {
								output.setInvestmentOrPersonFlag("N");
							}
							output.setInvestmentOrPerson(
									clientRealEstate.getLookupAlternateInvestmentsAssetType().getDescription());

							output.setMarketLinkOrFixedReturn("Market Linked");
							output.setCurrentValue(clientRealEstate.getCurrentValue().doubleValue());
							output.setInvestmentValue(clientRealEstate.getInvestmentValue().doubleValue());
							output.setLockedInDate("Available");
							output.setGains(output.getCurrentValue() - output.getInvestmentValue());
							output.setInvestmentDate(simpleDateFormat.format(Calendar.getInstance().getTime()));
							// custom Cagr Calculation
							double diff = FinanceUtil.YEARFRAC(clientRealEstate.getInvestmentDate(),
									Calendar.getInstance().getTime(), 1);
							double absoluteReturn = (clientRealEstate.getCurrentValue().doubleValue()
									/ clientRealEstate.getInvestmentValue().doubleValue()) - 1;
							if (diff < 1) {
								output.setCagr(String.valueOf(absoluteReturn * 100));
							} else {
								double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;

								output.setCagr(String.valueOf(cagrCal * 100));
							}
							output.setPtpReturns(String.valueOf((output.getCurrentValue()/output.getInvestmentValue() - 1) * 100));

							/*
							 * output.setCagr(cagrCal(clientRealEstate.getCurrentValue().doubleValue(),
							 * clientRealEstate.getInvestmentValue().doubleValue(),
							 * clientRealEstate.getInvestmentDate()));
							 */
							output.setGains(output.getCurrentValue() - output.getInvestmentValue());
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							outputList.add(output);
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}
					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}

				// ******** Client RealEstate Ends ************

				// Client RealEstate Starts
				// only personal asset
				// if (specificRequermentStat.equals("overview")) {
				try {
					List<ClientVehicle> clientVehicleList = portFolioOverviewDAO.getClientVehicleList(clientId,
							session);
					for (ClientVehicle clientVehicle : clientVehicleList) {
						try {

							masterProductClassification = clientVehicle.getMasterProductClassification();
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							output.setProductDesc(clientVehicle.getDescription());
							output.setProductName(masterProductClassification.getProductName());
							output.setBankIssuerName(clientVehicle.getDescription());
							populateFamilyRelation(
									clientVehicle.getClientFamilyMember().getLookupRelation().getDescription(), output);
							output.setPortFolioId(clientVehicle.getId());
							// populating class and type description
							classLevelDescription(output, masterProductClassification);
							output.setInvestmentOrPersonFlag("N");
							// checking locked in flag
							output = getLockedInDateCheck(output, masterProductClassification.getLockedInFlag());
							output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_PERSONAL);
							output.setCagr(String.valueOf("N/A"));
							output.setPtpReturns("N/A");
							output.setMarketLinkOrFixedReturn("Market Linked");
							output.setCurrentValue(clientVehicle.getCurrentValue().doubleValue());
							output.setInvestmentValue(clientVehicle.getCurrentValue().doubleValue());
							output.setLockedInDate("Available");
							output.setInvestmentDate(simpleDateFormat.format(Calendar.getInstance().getTime()));
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
							output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
							outputList.add(output);
						} catch (Exception exp) {
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}
					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}
				// }
				// ******** Client RealEstate Ends *********`**

				// Client REPE(Others) Starts

				List<ClientOtherAlternateAsset> clientOtherAlternateAssetList = portFolioOverviewDAO
						.getClientOtherAlternateAssetList(clientId, session);
				for (ClientOtherAlternateAsset clientOtherAlternateAsset : clientOtherAlternateAssetList) {
					try {
						masterProductClassification = clientOtherAlternateAsset.getMasterProductClassification();
						PortfolioOverviewDto output = new PortfolioOverviewDto();
						output.setProductName(clientOtherAlternateAsset.getFundDescription());
						output.setBankIssuerName(clientOtherAlternateAsset.getFundDescription());
						output.setProductDesc(clientOtherAlternateAsset.getSchemeName());
						output.setPortFolioId(clientOtherAlternateAsset.getId());
						populateFamilyRelation(
								clientOtherAlternateAsset.getClientFamilyMember().getLookupRelation().getDescription(),
								output);
						// populating class and type description
						classLevelDescription(output, masterProductClassification);
						output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_INVESTMENT);
						output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
						// checking locked in flag
						output = getLockedInDateCheck(output, masterProductClassification.getLockedInFlag());

						output.setMarketLinkOrFixedReturn("Market Linked");
						output.setCurrentValue(clientOtherAlternateAsset.getCurrentMarketValue().doubleValue());
						output.setInvestmentValue(clientOtherAlternateAsset.getTotalInvestmentAmount().doubleValue());
						output.setLockedInDate("Available");
						output.setInvestmentDate(simpleDateFormat.format(clientOtherAlternateAsset.getInvestmentDate()));
						output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
						output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());

						// custom Cagr Calculation
						double diff = FinanceUtil.YEARFRAC(clientOtherAlternateAsset.getInvestmentDate(),
								Calendar.getInstance().getTime(), 1);
						double absoluteReturn = (clientOtherAlternateAsset.getCurrentMarketValue().doubleValue()
								/ clientOtherAlternateAsset.getTotalInvestmentAmount().doubleValue()) - 1;
						if (diff < 1) {
							output.setCagr(String.valueOf(absoluteReturn * 100));
						} else {
							double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;

							output.setCagr(String.valueOf(cagrCal * 100));
						}
						output.setPtpReturns(String.valueOf((output.getCurrentValue()/output.getInvestmentValue() - 1) * 100));

						/*
						 * output.setCagr(cagrCal(clientOtherAlternateAsset.getCurrentMarketValue().
						 * doubleValue(),
						 * clientOtherAlternateAsset.getTotalInvestmentAmount().doubleValue(),
						 * clientOtherAlternateAsset.getInvestmentDate()));
						 */
						output.setGains(output.getCurrentValue() - output.getInvestmentValue());
						if (!Double.isNaN(output.getCurrentValue())) {
							totalCurrentValue = totalCurrentValue + output.getCurrentValue();
						}
						output.setFinancialAssetName(
								output.getPortFolioId() + "-" + output.getProductName() + "-" + output.getProductDesc());
						outputList.add(output);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						FinexaBussinessException finexaBuss = new FinexaBussinessException(
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, e);
						FinexaBussinessException.logFinexaBusinessException(finexaBuss);
					}
				}

				// Client Structure Product Starts

				List<ClientStructuredProduct> clientClientStructuredProductList = portFolioOverviewDAO
						.getClientStructuredProductList(clientId, session);
				for (ClientStructuredProduct clientStructuredProduct : clientClientStructuredProductList) {
					try {
						masterProductClassification = clientStructuredProduct.getMasterProductClassification();
						PortfolioOverviewDto output = new PortfolioOverviewDto();
						output.setPortFolioId(clientStructuredProduct.getId());
						output.setProductName(masterProductClassification.getProductName());
						output.setBankIssuerName(clientStructuredProduct.getDescription());
						output.setProductDesc(clientStructuredProduct.getDescription());
						populateFamilyRelation(
								clientStructuredProduct.getClientFamilyMember().getLookupRelation().getDescription(),
								output);
						// populating class and type description
						classLevelDescription(output, masterProductClassification);
						output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
						// checking locked in flag
						output = getLockedInDateCheck(output, masterProductClassification.getLockedInFlag());
						output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_INVESTMENT);
						output.setMarketLinkOrFixedReturn("Market Linked");
						output.setCurrentValue(clientStructuredProduct.getCurrentValue().doubleValue());
						output.setInvestmentValue(clientStructuredProduct.getInvestmentValue().doubleValue());
						output.setLockedInDate("Available");
						output.setInvestmentDate(simpleDateFormat.format(clientStructuredProduct.getInvestmentStartDate()));

						// custom Cagr Calculation
						double diff = FinanceUtil.YEARFRAC(clientStructuredProduct.getInvestmentStartDate(),
								Calendar.getInstance().getTime(), 1);
						double absoluteReturn = (clientStructuredProduct.getCurrentValue().doubleValue()
								/ clientStructuredProduct.getInvestmentValue().doubleValue()) - 1;
						if (diff <= 1) {
							output.setCagr(String.valueOf(absoluteReturn * 100));
						} else {
							double cagrCal = (Math.pow((1 + absoluteReturn), (1 / diff))) - 1;

							output.setCagr(String.valueOf(cagrCal * 100));
						}
						output.setPtpReturns(String.valueOf((output.getCurrentValue()/output.getInvestmentValue() - 1) * 100));
						output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
						output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
						/*
						 * output.setCagr(cagrCal(clientStructuredProduct.getCurrentValue().doubleValue(
						 * ), clientStructuredProduct.getInvestmentValue().doubleValue(),
						 * clientStructuredProduct.getInvestmentStartDate()));
						 */
						output.setGains(output.getCurrentValue() - output.getInvestmentValue());
						if (!Double.isNaN(output.getCurrentValue())) {
							totalCurrentValue = totalCurrentValue + output.getCurrentValue();
						}
						output.setFinancialAssetName(
								output.getPortFolioId() + "-" + output.getProductName() + "-" + output.getProductDesc());
						outputList.add(output);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						FinexaBussinessException finexaBuss = new FinexaBussinessException(
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
								FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, e);
						FinexaBussinessException.logFinexaBusinessException(finexaBuss);
					}
				}

				// ********Client Structure Product END ************

				// Client LumpSum Inflow Product Starts
				if (specificRequermentStat.equals("overview")) {
					List<ClientLumpsumInflow> clientClientLumpsumInflowProductList = portFolioOverviewDAO
							.getClientLumpsumInflowsumList(clientId, session);
					for (ClientLumpsumInflow clientLumpsumInflow : clientClientLumpsumInflowProductList) {
						try {
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							output.setProductName(FinexaConstant.LUMPSUM_INFLOW_TEXT_LABEL);
							output.setBankIssuerName(clientLumpsumInflow.getInflowDesc());
							output.setProductType("Cash");
							output.setAssetClassification("Cash");
							output.setAssetClassificationId(0);
							output.setCagr("N/A");
							output.setPtpReturns("N/A");
							output.setProductId(38);
							populateFamilyRelation("Self", output);
							output.setSubAssetClassification("Cash/Liquid");
							output.setSubAssetClassificationId(1);
							output.setInvestmentOrPersonFlag("Y");
							output.setPortFolioId(clientLumpsumInflow.getId());
							output.setInvestmentOrPerson(FinexaConstant.ASSET_TYPE_INVESTMENT);
							output.setMaturityAmount(clientLumpsumInflow.getExpectedInflow().doubleValue());
							output.setCurrentValue(clientLumpsumInflow.getExpectedInflow().doubleValue());
							output.setMaturityDate(simpleDateFormat.format(clientLumpsumInflow.getExpectedInflowDate()));
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							outputList.add(output);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				// ********Client Lumpsum Inflow END ************

				// Current Portfolio weightage

				for (PortfolioOverviewDto portfolioOverviewDto : outputList) {
					portfolioOverviewDto
					.setCurrentPortfolioWeight(portfolioOverviewDto.getCurrentValue() / totalCurrentValue);

					if (portfolioOverviewDto.getCagr() != null && portfolioOverviewDto.getCagr().matches("[0-9.]*")) {
						portfolioOverviewDto.setAssetWeightReturn(portfolioOverviewDto.getCurrentPortfolioWeight()
								* Double.valueOf(portfolioOverviewDto.getCagr()));
					}

				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			FinexaBussinessException finexaBuss = new FinexaBussinessException(FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
					FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
			FinexaBussinessException.logFinexaBusinessException(finexaBuss);
		} finally {

			cacheInfoService.addCacheList(FinexaConstant.CALCULATION_TYPE_CONSTANT+token, String.valueOf(clientId),
					FinexaConstant.CALCULATION_SUB_TYPE_PORTFOLIO_CONSTANT, outputList);
		}

		return outputList;
	}

	@Override
	public PortfolioOvervieEquityDto getclientPortfolioOverviewEquity(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException {

		PortfolioOvervieEquityDto portfolioOverviewEquitydto = new PortfolioOvervieEquityDto();
	List<PortfolioOverviewDto> portfolioOverviewDtoList = getclientPortfolioOverview(token, clientId, "overview", session,
				cacheInfoService);
		
		List<PortfolioOverviewDto> outputList = new ArrayList<>();
		List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList = new ArrayList<>();
		Map<String, Double> sectorTotalHoldingMap = new TreeMap<>();
		List<PortfolioOverviewEquitySectorDto> portfolioOverviewEquitySectorDtoList = new ArrayList<>();
		PortFolioOverviewDAO portFolioOverviewDAO = new PortFolioOverviewDAOImpl();
		List<MasterIndexDailyNAV> masterindexdailynavList = new ArrayList<>();
		double totalCurrentValue = 0.0;
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		try {
			List<MasterProductRecommendationRank> masterproductrecommendationrankList = portFolioOverviewDAO
					.getMasterproductrecommendationrankList(session);
			for (PortfolioOverviewDto portfolioOverviewEquityDto : portfolioOverviewDtoList) {
				if (portfolioOverviewEquityDto.getAssetClassificationId() == 3) {
					PortfolioOverviewDto output = new PortfolioOverviewDto();
					output.setProductId(portfolioOverviewEquityDto.getProductId());
					output.setAssetClassificationId(portfolioOverviewEquityDto.getAssetClassificationId());
					output.setSubAssetClassification(portfolioOverviewEquityDto.getSubAssetClassification());
					output.setSubAssetClassificationId(portfolioOverviewEquityDto.getSubAssetClassificationId());
					output.setMarketCapId(portfolioOverviewEquityDto.getMarketCapId());
					output.setMarketCapName(portfolioOverviewEquityDto.getMarketCapName());
					output.setProductName(portfolioOverviewEquityDto.getProductName());
					output.setBankIssuerName(portfolioOverviewEquityDto.getBankIssuerName());
					output.setProductDesc(portfolioOverviewEquityDto.getProductDesc());
					output.setProductDescLong(portfolioOverviewEquityDto.getProductDescLong());
					output.setProductType(portfolioOverviewEquityDto.getProductType());
					output.setCurrentValue(portfolioOverviewEquityDto.getCurrentValue());
					output.setInvestmentValue(portfolioOverviewEquityDto.getInvestmentValue());
					output.setGains(portfolioOverviewEquityDto.getGains());
					output.setIsin(portfolioOverviewEquityDto.getIsin());
					output.setCagr(portfolioOverviewEquityDto.getCagr());
					output.setPtpReturns(portfolioOverviewEquityDto.getPtpReturns());
					output.setLockedInDate(portfolioOverviewEquityDto.getLockedInDate());
					output.setMaturityDate(portfolioOverviewEquityDto.getMaturityDate());
					output.setMaturityAmount(portfolioOverviewEquityDto.getMaturityAmount());
					output.setIsin(portfolioOverviewEquityDto.getIsin());
					output.setAmfiCode(portfolioOverviewEquityDto.getAmfiCode());
					output.setLargeCapMFPerc(portfolioOverviewEquityDto.getLargeCapMFPerc());
					output.setMidCapMFPerc(portfolioOverviewEquityDto.getMidCapMFPerc());
					output.setSmallcapMFPerc(portfolioOverviewEquityDto.getSmallcapMFPerc());
					output.setOtherCapMFPerc(portfolioOverviewEquityDto.getOtherCapMFPerc());
					output.setCapRank(portfolioOverviewEquityDto.getCapRank());
					output.setCapRankValue(portfolioOverviewEquityDto.getCapRankValue());
					totalCurrentValue = totalCurrentValue + output.getCurrentValue();
					output.setRiskMeasureMap(portfolioOverviewEquityDto.getRiskMeasureMap());
					outputList.add(output);
					
				}
			}

			for (PortfolioOverviewDto portfolioOverviewEquityDto : new ArrayList<>(outputList)) {
				portfolioOverviewEquityDto
				.setPercTotal(portfolioOverviewEquityDto.getCurrentValue() / totalCurrentValue);
				if (portfolioOverviewEquityDto.getAmfiCode() != null && portfolioOverviewEquityDto.getAmfiCode() != 0) {
					for (MasterProductRecommendationRank masterproductrecommendationrank : new ArrayList<>(
							masterproductrecommendationrankList)) {
						if (masterproductrecommendationrank.getId().getMasterMutualFundEtf()
								.getAmfiCode() == portfolioOverviewEquityDto.getAmfiCode()) {
							portfolioOverviewEquityDto.setResearchRank(masterproductrecommendationrank.getRank());
							break;
						}
					}
				}else {
					portfolioOverviewEquityDto.setResearchRank("N/A");
				}

				if (portfolioOverviewEquityDto.getProductType().equals("MF / ETF / PMS")) {
					List<MasterMFHoldingSectorWise> masterMfHoldingSectorwiseList = portFolioAssetAllocationReviewDAO
							.getMasterMfHoldingSectorwiseList(portfolioOverviewEquityDto.getIsin(), session);
					if(masterMfHoldingSectorwiseList != null) {
						for (MasterMFHoldingSectorWise masterMfHoldingSectorwise : masterMfHoldingSectorwiseList) {
							Double holdings = sectorTotalHoldingMap
									.get(masterMfHoldingSectorwise.getId().getMasterMfsector().getSector());
							if (holdings == null) {
								holdings = 0.0;
							}
							holdings = holdings
									+ ((masterMfHoldingSectorwise.getHolding()/100.0) * portfolioOverviewEquityDto.getCurrentValue());

							sectorTotalHoldingMap.put(masterMfHoldingSectorwise.getId().getMasterMfsector().getSector(),
									holdings);
						//	System.out.println("isin "+masterMfHoldingSectorwise.getId().getMasterMutualFundEtf().getIsin());
						//	System.out.println("holding "+masterMfHoldingSectorwise.getHolding());
						//	System.out.println("date "+masterMfHoldingSectorwise.getId().getDate());
						}

					}
				} else {
					MasterDirectEquity masterDirectEquity = portFolioAssetAllocationReviewDAO
							.getMasterDirectEquityList(portfolioOverviewEquityDto.getIsin(), session);
					if (masterDirectEquity != null) {
						Double holdings = sectorTotalHoldingMap.get(masterDirectEquity.getMasterMfsector().getSector());
						if (holdings == null) {
							holdings = 0.0;
						}
						holdings = holdings + portfolioOverviewEquityDto.getCurrentValue();
						sectorTotalHoldingMap.put(masterDirectEquity.getMasterMfsector().getSector(), holdings);
					}
				} 
			}

			if (!sectorTotalHoldingMap.isEmpty()) {
				double totalVal = 0.0;
				for (Map.Entry<String, Double> mapObj : sectorTotalHoldingMap.entrySet()) {
					//System.out.println("mapObj.getValue() "+mapObj.getValue());
					totalVal = totalVal + mapObj.getValue();
					//System.out.println("totalVal "+totalVal);
				}
				for (Map.Entry<String, Double> mapObj : sectorTotalHoldingMap.entrySet()) {
					if(totalVal != 0) {
					double perc = (mapObj.getValue() / totalVal) * 100.0;
					//System.out.println("perc "+perc);
					sectorTotalHoldingMap.put(mapObj.getKey(), perc);
					}
				}

			}
			portfolioOverviewEquitydto.setPortfolioOverviewEquitySectorDtoList(portfolioOverviewEquitySectorDtoList);
			portfolioOverviewEquitydto.setPortfolioOverviewList(outputList);
			Map<String, Double> capWeightageMap = new HashMap<String, Double>();
			double capsum = 0.0;
			for (PortfolioOverviewDto portfolioOverviewEquityDto : outputList) {
				if (portfolioOverviewEquityDto.getIsin() != null) {
					MasterEquityMarketCapClassification mcc = portFolioOverviewDAO
							.getMasterequitymarketcapclassificationList(portfolioOverviewEquityDto.getIsin(), session);
					Double val = mcc == null ? null : capWeightageMap.get(mcc.getMarketcapCategory());
					if (val == null) {

						val = 0.0;
					}
					val = val + portfolioOverviewEquityDto.getPercTotal();
					capsum = capsum + val;
					if (mcc != null) {
						capWeightageMap.put(mcc.getMarketcapCategory(), val);
					}
				}
			}
			capWeightageMap.put("Others", 1 - capsum);
			currentDate = Calendar.getInstance();

			currentDate.add(Calendar.MONTH, -1);
			Calendar month1 = currentDate;
			month1.set(Calendar.HOUR_OF_DAY, 0);
			month1.set(Calendar.MINUTE, 0);
			month1.set(Calendar.SECOND, 0);
			month1.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.MONTH, -3);
			Calendar month3 = currentDate;
			month3.set(Calendar.HOUR_OF_DAY, 0);
			month3.set(Calendar.MINUTE, 0);
			month3.set(Calendar.SECOND, 0);
			month3.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.MONTH, -6);
			Calendar month6 = currentDate;
			month6.set(Calendar.HOUR_OF_DAY, 0);
			month6.set(Calendar.MINUTE, 0);
			month6.set(Calendar.SECOND, 0);
			month6.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.YEAR, -1);
			Calendar year1 = currentDate;
			year1.set(Calendar.HOUR_OF_DAY, 0);
			year1.set(Calendar.MINUTE, 0);
			year1.set(Calendar.SECOND, 0);
			year1.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.YEAR, -3);
			Calendar year3 = currentDate;
			year3.set(Calendar.HOUR_OF_DAY, 0);
			year3.set(Calendar.MINUTE, 0);
			year3.set(Calendar.SECOND, 0);
			year3.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.YEAR, -5);
			Calendar year5 = currentDate;
			year5.set(Calendar.HOUR_OF_DAY, 0);
			year5.set(Calendar.MINUTE, 0);
			year5.set(Calendar.SECOND, 0);
			year5.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			currentDate.set(Calendar.MILLISECOND, 0);

			Double currentValue = 0.0;
			List<Double> totalStdCal = new ArrayList<>();
			List<MasterSectorBenchmarkMapping> benchMarkList = portFolioAssetAllocationReviewDAO
					.getMasterSectorBenchmarkList(session);
			for (Map.Entry<String, Double> entry : capWeightageMap.entrySet()) {
				PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();
				portfolioSubAssetBencmarkDto.setCapName(entry.getKey());
				portfolioSubAssetBencmarkDto.setPortfolioPerExposure(entry.getValue());
				for (MasterSectorBenchmarkMapping masterSecBenchMap : benchMarkList) {
					if (masterSecBenchMap.getMasterMfsector().getSector().equals(entry.getKey())) {
						portfolioSubAssetBencmarkDto.setBenchMark(masterSecBenchMap.getMasterIndexName().getName());
						masterindexdailynavList = portFolioAssetAllocationReviewDAO.getMasterindexdailynavList(masterSecBenchMap.getId(),session);
						for (MasterIndexDailyNAV masterindexdailynav : masterindexdailynavList) {
							if (currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								currentValue = masterindexdailynav.getNav().doubleValue();
							}

							if (masterindexdailynav.getId().getMasterIndexName().getId() == masterSecBenchMap.getId()) {

								if (month1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setMonth1Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (month3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setMonth3Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (month6.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setMonth6Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (year1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setYear1Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setYear3Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (year5.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setYear5Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if ((year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
										|| (currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
										|| (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 1
										&& currentDate.getTime()
										.compareTo(masterindexdailynav.getId().getDate()) == -1)) {
									totalStdCal.add(masterindexdailynav.getNav().doubleValue());

								}

							}

						}
					}
				}

				portfolioSubAssetBencmarkDto.setRiskStdDev(FinanceUtil.STDEV(totalStdCal));
				portfolioSubAssetBencmarkDtoList.add(portfolioSubAssetBencmarkDto);
			}
			Set<Entry<String, Double>> set = sectorTotalHoldingMap.entrySet();
			List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(set);
			Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
				public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			int count = 1;
			for (Entry<String, Double> entryList : new ArrayList<>(list)) {
				if (count > 15) {
					list.remove(entryList);
				} else {
					PortfolioOverviewEquitySectorDto portfolioOverviewEquitySectorDto = new PortfolioOverviewEquitySectorDto();
					portfolioOverviewEquitySectorDto.setSectorName(entryList.getKey());
				//	System.out.println("entryList.getValue() "+entryList.getValue());
					portfolioOverviewEquitySectorDto.setExposureInPortfolio(entryList.getValue());
					PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();
					portfolioSubAssetBencmarkDto.setCapName(entryList.getKey());
					for (MasterSectorBenchmarkMapping masterSecBenchMap : benchMarkList) {
						if (masterSecBenchMap.getMasterMfsector().getSector().equals(entryList.getKey())) {
							portfolioSubAssetBencmarkDto.setBenchMark(masterSecBenchMap.getMasterIndexName().getName());
							masterindexdailynavList = portFolioAssetAllocationReviewDAO.getMasterindexdailynavList(masterSecBenchMap.getId(),session);
							for (MasterIndexDailyNAV masterindexdailynav : masterindexdailynavList) {
								if (currentDate.getTime()
										.compareTo(masterindexdailynav.getId().getDate()) == 0) {
									currentValue = masterindexdailynav.getNav().doubleValue();
								}

								if (masterindexdailynav.getId().getMasterIndexName().getId() == masterSecBenchMap
										.getId()) {

									if (month1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setMonth1Value(
												currentValue / masterindexdailynav.getNav().doubleValue());
									}

									if (month3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setMonth3Value(
												currentValue / masterindexdailynav.getNav().doubleValue());
									}

									if (month6.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setMonth6Value(
												currentValue / masterindexdailynav.getNav().doubleValue());
									}

									if (year1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setYear1Value(
												currentValue / masterindexdailynav.getNav().doubleValue());
									}

									if (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setYear3Value(
												currentValue / masterindexdailynav.getNav().doubleValue());
									}

									if (year5.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
										portfolioSubAssetBencmarkDto.setYear5Value(
												currentValue / masterindexdailynav.getNav().doubleValue());
									}

									if ((year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
											|| (currentDate.getTime()
													.compareTo(masterindexdailynav.getId().getDate()) == 0)
											|| (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 1
											&& currentDate.getTime()
											.compareTo(masterindexdailynav.getId().getDate()) == -1)) {
										totalStdCal.add(masterindexdailynav.getNav().doubleValue());

									}

								}

							}
						}
					}

					portfolioSubAssetBencmarkDto.setRiskStdDev(FinanceUtil.STDEV(totalStdCal));
					portfolioOverviewEquitySectorDto.setPortfolioSubAssetBencmarkDto(portfolioSubAssetBencmarkDto);
					portfolioOverviewEquitySectorDtoList.add(portfolioOverviewEquitySectorDto);
				}
				count++;
			}
		} catch (

				FinexaDaoException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		portfolioOverviewEquitydto.setOverviewEquityBenchMarkList(portfolioSubAssetBencmarkDtoList);
		portfolioOverviewEquitydto.setPortfolioOverviewEquitySectorDtoList(portfolioOverviewEquitySectorDtoList);

		return portfolioOverviewEquitydto;

	}

	private static PortfolioOverviewDto classLevelDescription(PortfolioOverviewDto output,
			MasterProductClassification masterProductClassification) throws FinexaServiceException {
		output.setProductId(masterProductClassification.getId());
		if (masterProductClassification.getMasterProductType() != null) {
			output.setProductType(masterProductClassification.getMasterProductType().getProductType());
			output.setProductTypeId(masterProductClassification.getMasterProductType().getId());
		}
		if (masterProductClassification.getLookupAssetClass() != null) {
			output.setAssetClassification(masterProductClassification.getLookupAssetClass().getDescription());
			output.setAssetClassificationId(masterProductClassification.getLookupAssetClass().getId());
		}
		if (masterProductClassification.getLookupAssetSubClass() != null) {
			output.setSubAssetClassification(masterProductClassification.getLookupAssetSubClass().getDescription());
			output.setSubAssetClassificationId(masterProductClassification.getLookupAssetSubClass().getId());
		}
		return output;
	}

	@Override
	public PortfolioOvervieEquityDto getclientPortfolioOverviewFixedIncome(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException {

		PortfolioOvervieEquityDto portfolioOverviewEquitydto = new PortfolioOvervieEquityDto();
		List<PortfolioOverviewDto> portfolioOverviewDtoList = getclientPortfolioOverview(token, clientId, "overview", session,
				cacheInfoService);
		List<PortfolioOverviewDto> outputList = new ArrayList<>();
		List<PortfolioSubAssetBencmarkDto> portfolioSubAssetBencmarkDtoList = new ArrayList<>();
		Map<String, Double> sectorTotalHoldingMap = new TreeMap<>();
		List<PortfolioOverviewEquitySectorDto> portfolioOverviewEquitySectorDtoList = new ArrayList<>();
		Map<String, Double> assetQualityMap = new HashMap<String, Double>();
		List<MasterIndexDailyNAV> masterindexdailynavList = new ArrayList<>();
		double totalCurrentValue = 0.0;
		PortFolioOverviewDAO portFolioOverviewDAO = new PortFolioOverviewDAOImpl();
		PortFolioAssetAllocationReviewDAO portFolioAssetAllocationReviewDAO = new PortFolioAssetAllocationReviewDAOImpl();
		try {
			List<MasterProductRecommendationRank> masterproductrecommendationrankList = portFolioOverviewDAO
					.getMasterproductrecommendationrankList(session);
			for (PortfolioOverviewDto portfolioOverviewEquityDto : portfolioOverviewDtoList) {
				if (portfolioOverviewEquityDto.getAssetClassificationId() == 2) {
					PortfolioOverviewDto output = new PortfolioOverviewDto();
					output.setProductId(portfolioOverviewEquityDto.getProductId());
					output.setAssetClassification(portfolioOverviewEquityDto.getAssetClassification());
					output.setAssetClassificationId(portfolioOverviewEquityDto.getAssetClassificationId());
					output.setProductName(portfolioOverviewEquityDto.getProductName());
					output.setProductType(portfolioOverviewEquityDto.getProductType());
					output.setCurrentValue(portfolioOverviewEquityDto.getCurrentValue());
					output.setInvestmentValue(portfolioOverviewEquityDto.getInvestmentValue());
					output.setGains(portfolioOverviewEquityDto.getGains());
					output.setIsin(portfolioOverviewEquityDto.getIsin());
					output.setCagr(portfolioOverviewEquityDto.getCagr());
					output.setMarketLinkOrFixedReturn(portfolioOverviewEquityDto.getMarketLinkOrFixedReturn());
					output.setPtpReturns(portfolioOverviewEquityDto.getPtpReturns());
					output.setLockedInDate(portfolioOverviewEquityDto.getLockedInDate());
					output.setMaturityDate(portfolioOverviewEquityDto.getMaturityDate());
					output.setSt_ltClassification(portfolioOverviewEquityDto.getSt_ltClassification());
					output.setMaturityAmount(portfolioOverviewEquityDto.getMaturityAmount());
					output.setIsin(portfolioOverviewEquityDto.getIsin());
					output.setTimeToMaturity(portfolioOverviewEquityDto.getTimeToMaturity());
					output.setAmfiCode(portfolioOverviewEquityDto.getAmfiCode());
					output.setBankIssuerName(portfolioOverviewEquityDto.getBankIssuerName());
					output.setProductDesc(portfolioOverviewEquityDto.getProductDesc());
					output.setProductDescLong(portfolioOverviewEquityDto.getProductDescLong());
					output.setLargeCapMFPerc(portfolioOverviewEquityDto.getLargeCapMFPerc());
					output.setMidCapMFPerc(portfolioOverviewEquityDto.getMidCapMFPerc());
					output.setSmallcapMFPerc(portfolioOverviewEquityDto.getSmallcapMFPerc());
					output.setOtherCapMFPerc(portfolioOverviewEquityDto.getOtherCapMFPerc());
					output.setCapRank(portfolioOverviewEquityDto.getCapRank());
					output.setCapRankValue(portfolioOverviewEquityDto.getCapRankValue());
					totalCurrentValue = totalCurrentValue + output.getCurrentValue();
					output.setMfMaturityProfile(portfolioOverviewEquityDto.getMfMaturityProfile());
					output.setRiskMeasureMap(portfolioOverviewEquityDto.getRiskMeasureMap());
					outputList.add(output);

				}
			}

			for (PortfolioOverviewDto portfolioOverviewEquityDto : new ArrayList<>(outputList)) {
				portfolioOverviewEquityDto
				.setPercTotal(portfolioOverviewEquityDto.getCurrentValue() / totalCurrentValue);
				if (portfolioOverviewEquityDto.getAmfiCode() != null && portfolioOverviewEquityDto.getAmfiCode() != 0) {
					for (MasterProductRecommendationRank masterproductrecommendationrank : new ArrayList<>(
							masterproductrecommendationrankList)) {
						if (masterproductrecommendationrank.getId().getMasterMutualFundEtf()
								.getAmfiCode() == portfolioOverviewEquityDto.getAmfiCode()) {
							portfolioOverviewEquityDto.setResearchRank(masterproductrecommendationrank.getRank());
							break;
						}
					}

					List<MasterMFHoldingSectorWise> masterMfHoldingSectorwiseList = portFolioAssetAllocationReviewDAO
							.getMasterMfHoldingSectorwiseList(portfolioOverviewEquityDto.getIsin(), session);
					for (MasterMFHoldingSectorWise masterMfHoldingSectorwise : masterMfHoldingSectorwiseList) {
						Double holdings = sectorTotalHoldingMap
								.get(masterMfHoldingSectorwise.getId().getMasterMfsector().getSector());
						if (holdings == null) {
							holdings = 0.0;
						}
						holdings = holdings
								+ (masterMfHoldingSectorwise.getHolding() * portfolioOverviewEquityDto.getPercTotal());
						sectorTotalHoldingMap.put(masterMfHoldingSectorwise.getId().getMasterMfsector().getSector(),
								holdings);
					}

				} else {
					portfolioOverviewEquityDto.setResearchRank("N/A");
				}

			}

			portfolioOverviewEquitydto.setPortfolioOverviewList(outputList);
			Map<String, Double> timeSpanExposureMap = new HashMap<String, Double>();

			double capsum = 0.0;
			for (PortfolioOverviewDto portfolioOverviewEquityDto : outputList) {
				if (portfolioOverviewEquityDto.getTimeToMaturity() != null) {
					if (Double.parseDouble(portfolioOverviewEquityDto.getTimeToMaturity()) > 1) {
						Double val = timeSpanExposureMap.get(FinexaConstant.EXPOSURE_SPAN_GREATER_THAN_1_YEAR);
						if (val == null) {
							val = 0.0;
						}
						val = val + portfolioOverviewEquityDto.getPercTotal();
						capsum = capsum + val;
						timeSpanExposureMap.put(FinexaConstant.EXPOSURE_SPAN_GREATER_THAN_1_YEAR, val);
					}
					if (Double.parseDouble(portfolioOverviewEquityDto.getTimeToMaturity()) < 1) {
						Double val = timeSpanExposureMap.get(FinexaConstant.EXPOSURE_SPAN_LESS_THAN_1_YEAR);
						if (val == null) {
							val = 0.0;
						}
						val = val + portfolioOverviewEquityDto.getPercTotal();
						capsum = capsum + val;
						timeSpanExposureMap.put(FinexaConstant.EXPOSURE_SPAN_LESS_THAN_1_YEAR, val);
					}
				}
				//System.out.println("portfolioOverviewEquityDto "+portfolioOverviewEquityDto.getProductType());
				//System.out.println("portfolioOverviewEquityDto "+portfolioOverviewEquityDto.getProductName());
				//System.out.println("portfolioOverviewEquityDto.getMarketLinkOrFixedReturn() "+portfolioOverviewEquityDto.getMarketLinkOrFixedReturn());
				//System.out.println("portfolioOverviewEquityDto.getIsin() "+portfolioOverviewEquityDto.getIsin());
				
				
				Double val= 0.0, per = 0.0;
				if (portfolioOverviewEquityDto.getMarketLinkOrFixedReturn().equals(FinexaConstant.FIXED_RETURN)) {
					val = assetQualityMap.get("AAA & Equivalent");
					if (val == null) {
						val = 0.0;
					}
					val = val + (portfolioOverviewEquityDto.getCurrentValue() / totalCurrentValue)*100;
					assetQualityMap.put("AAA & Equivalent", val);
				} else {
					if (portfolioOverviewEquityDto.getMarketLinkOrFixedReturn().equals(FinexaConstant.MARKET_LINKED)) {
						if (portfolioOverviewEquityDto.getIsin() != null
								&& !portfolioOverviewEquityDto.getIsin().equals("")) {
							List<MasterMFHoldingRatingWise> masterMfHoldingRatingWiseList = portFolioAssetAllocationReviewDAO
									.getMasterMfHoldingRatingWiseList(portfolioOverviewEquityDto.getIsin(), session);
							for (MasterMFHoldingRatingWise masterMfHoldingRatingWise : masterMfHoldingRatingWiseList) {
								val = 0.0;
								for (Map.Entry<String, Double> mapObj : assetQualityMap.entrySet()) {
									if(mapObj.getKey().equals(masterMfHoldingRatingWise.getId().getMasterMfrating().getRating())){
										 val = assetQualityMap
												.get(masterMfHoldingRatingWise.getId().getMasterMfrating().getRating());
										if (val == null) {
											val = 0.0;
										}
										break;
									}
								}
								
								per = (portfolioOverviewEquityDto.getCurrentValue() / totalCurrentValue) * masterMfHoldingRatingWise.getHolding();
								val = val + per;
								assetQualityMap.put(masterMfHoldingRatingWise.getId().getMasterMfrating().getRating(),
											val);
							}
						} else {
							val = 0.0;
							val = assetQualityMap.get("Cash and Others");
							if (val == null) {
								val = 0.0;
							}
							val = val + (portfolioOverviewEquityDto.getCurrentValue() / totalCurrentValue);
							assetQualityMap.put("Cash and Others", val);
						}
					}
				}
			}
			/*for (Map.Entry<String, Double> mapObj : assetQualityMap.entrySet()) {
				System.out.println(mapObj.getKey()+"  "+mapObj);
			}*/

			timeSpanExposureMap.put(FinexaConstant.EXPOSURE_SPAN_OTHERS, 1 - capsum);
			currentDate = Calendar.getInstance();

			currentDate.add(Calendar.MONTH, -1);
			Calendar month1 = currentDate;
			month1.set(Calendar.HOUR_OF_DAY, 0);
			month1.set(Calendar.MINUTE, 0);
			month1.set(Calendar.SECOND, 0);
			month1.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.MONTH, -3);
			Calendar month3 = currentDate;
			month3.set(Calendar.HOUR_OF_DAY, 0);
			month3.set(Calendar.MINUTE, 0);
			month3.set(Calendar.SECOND, 0);
			month3.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.MONTH, -6);
			Calendar month6 = currentDate;
			month6.set(Calendar.HOUR_OF_DAY, 0);
			month6.set(Calendar.MINUTE, 0);
			month6.set(Calendar.SECOND, 0);
			month6.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.YEAR, -1);
			Calendar year1 = currentDate;
			year1.set(Calendar.HOUR_OF_DAY, 0);
			year1.set(Calendar.MINUTE, 0);
			year1.set(Calendar.SECOND, 0);
			year1.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.YEAR, -3);
			Calendar year3 = currentDate;
			year3.set(Calendar.HOUR_OF_DAY, 0);
			year3.set(Calendar.MINUTE, 0);
			year3.set(Calendar.SECOND, 0);
			year3.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.add(Calendar.YEAR, -5);
			Calendar year5 = currentDate;
			year5.set(Calendar.HOUR_OF_DAY, 0);
			year5.set(Calendar.MINUTE, 0);
			year5.set(Calendar.SECOND, 0);
			year5.set(Calendar.MILLISECOND, 0);

			currentDate = Calendar.getInstance();
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			currentDate.set(Calendar.MILLISECOND, 0);

			Double currentValue = 0.0;
			List<Double> totalStdCal = new ArrayList<>();
			List<MasterSectorBenchmarkMapping> benchMarkList = portFolioAssetAllocationReviewDAO
					.getMasterSectorBenchmarkList(session);
			for (Map.Entry<String, Double> entry : timeSpanExposureMap.entrySet()) {
				PortfolioSubAssetBencmarkDto portfolioSubAssetBencmarkDto = new PortfolioSubAssetBencmarkDto();
				portfolioSubAssetBencmarkDto.setCapName(entry.getKey());
				portfolioSubAssetBencmarkDto.setPortfolioPerExposure(entry.getValue());
				for (MasterSectorBenchmarkMapping masterSecBenchMap : benchMarkList) {
					if (masterSecBenchMap.getMasterMfsector().getSector().equals(entry.getKey())) {
						portfolioSubAssetBencmarkDto.setBenchMark(masterSecBenchMap.getMasterMfsector().getSector());
						masterindexdailynavList = portFolioAssetAllocationReviewDAO.getMasterindexdailynavList(masterSecBenchMap.getId(),session);
						for (MasterIndexDailyNAV masterindexdailynav : masterindexdailynavList) {
							if (masterindexdailynav.getId().getMasterIndexName().getId() == masterSecBenchMap.getId()
									&& currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
								currentValue = masterindexdailynav.getNav().doubleValue();
							}

							if (masterindexdailynav.getId().getMasterIndexName().getId() == masterSecBenchMap.getId()) {

								if (month1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setMonth1Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (month3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setMonth3Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (month6.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setMonth6Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (year1.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setYear1Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setYear3Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if (year5.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0) {
									portfolioSubAssetBencmarkDto
									.setYear5Value(currentValue / masterindexdailynav.getNav().doubleValue());
								}

								if ((year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
										|| (currentDate.getTime().compareTo(masterindexdailynav.getId().getDate()) == 0)
										|| (year3.getTime().compareTo(masterindexdailynav.getId().getDate()) == 1
										&& currentDate.getTime()
										.compareTo(masterindexdailynav.getId().getDate()) == -1)) {
									totalStdCal.add(masterindexdailynav.getNav().doubleValue());

								}

							}

						}
					}
				}

				portfolioSubAssetBencmarkDto.setRiskStdDev(FinanceUtil.STDEV(totalStdCal));
				portfolioSubAssetBencmarkDtoList.add(portfolioSubAssetBencmarkDto);
			}

		} catch (Exception e) {
		    e.printStackTrace();
			throw new FinexaBussinessException("", "", "", e);
		}
		portfolioOverviewEquitydto.setOverviewEquityBenchMarkList(portfolioSubAssetBencmarkDtoList);
		portfolioOverviewEquitydto.setPortfolioOverviewEquitySectorDtoList(portfolioOverviewEquitySectorDtoList);
		portfolioOverviewEquitydto.setAssetQualityMap(assetQualityMap);
		return portfolioOverviewEquitydto;

	}

	private static PortfolioOverviewDto getMarketLinkedCheck(PortfolioOverviewDto output, String flag) {
		if (flag.equals("Y")) {
			output.setMarketLinkOrFixedReturn(FinexaConstant.MARKET_LINKED);
		} else {
			output.setMarketLinkOrFixedReturn(FinexaConstant.FIXED_RETURN);
		}
		return output;
	}

	private static PortfolioOverviewDto getLockedInDateCheck(PortfolioOverviewDto output, String flag) {
		if (flag.equals("Y")) {
			output.setLockedInDate(FinexaConstant.LOCKED_IN_DATE_AVAILABLE);
			output.setMaturityDate("N/A");
			output.setTimeToMaturity("N/A");
			output.setSt_ltClassification("-");
		}
		return output;
	}

	private static PortfolioOverviewDto getSTLTClassificationCheck(PortfolioOverviewDto output, double difference) {
		if (difference > 3) {
			output.setSt_ltClassification("LT");
		} else {
			output.setSt_ltClassification("ST");
		}
		return output;
	}
	/*
	 * private static String cagrCal(double currentValue, double investment, Date
	 * investmentStartDate) { Double bigDecimalValue = 0d; try { double d1 =
	 * currentValue / (Double) investment; double d2 = (Double)
	 * FinanceUtil.YEARFRAC(investmentStartDate, Calendar.getInstance().getTime(),
	 * 1); if (d2 > 0) { double tobeSub = 1d; double divideBy1 = (1 / (double) d2);
	 * double power = Math.pow(d1, divideBy1); BigDecimal bigDecimal = new
	 * BigDecimal(power).setScale(2, BigDecimal.ROUND_UP); String bigValuw =
	 * bigDecimal.toPlainString(); bigDecimalValue = Double.parseDouble(bigValuw);
	 * bigDecimalValue = bigDecimalValue - tobeSub; } else { bigDecimalValue = d2; }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return
	 * String.valueOf(bigDecimalValue); }
	 */

	public static PortfolioOverviewDto populateFamilyRelation(String relation,
			PortfolioOverviewDto portfolioOverviewDto) {
		portfolioOverviewDto.setFamilyRelation(relation);
		return portfolioOverviewDto;
	}

	private String setSubAssetClassificationForFixedIncome(Integer tenure, Date investmentStartDate, String yearDays) {

		if (yearDays.equals("D")) {
			FinexaDateUtil util = new FinexaDateUtil();
			long[] getYear = util.getYearCountByDay(investmentStartDate, tenure);
			tenure = (int) getYear[0];
		}

		String subAssetClass = "";
		if (tenure < 1) {
			subAssetClass = "Short Term Debt";
		} else {
			subAssetClass = "Long Term Debt";
		}
		return subAssetClass;
	}

	private int setSubAssetClassificationIdForFixedIncome(Integer tenure, Date investmentStartDate, String yearDays) {

		if (yearDays.equals("D")) {
			FinexaDateUtil util = new FinexaDateUtil();
			long[] getYear = util.getYearCountByDay(investmentStartDate, tenure);
			tenure = (int) getYear[0];
		}

		int subAssetClass = 0;
		if (tenure < 1) {
			// Short Term Debt
			subAssetClass = 3;
		} else {
			// Long Term Debt
			subAssetClass = 4;
		}
		return subAssetClass;
	}
    

	@SuppressWarnings("unchecked")
	@Override
	public List<PortfolioOverviewDto> getclientPortfolioOverviewKV(int clientId, String specificRequermentStat,
			Session session, CacheInfoService cacheInfoService) throws FinexaBussinessException {
		List<PortfolioOverviewDto> outputList = new ArrayList<PortfolioOverviewDto>();
		PortFolioOverviewDAO portFolioOverviewDAO = new PortFolioOverviewDAOImpl();
		MasterProductClassification masterProductClassification = new MasterProductClassification();
		try {
			double totalCurrentValue  = 0;

				// Retirement Schemes
				try {
					// Client PPF
					List<ClientPPF> clientPPFList = portFolioOverviewDAO.getClientPPF(clientId, session);
					for (ClientPPF clientPPF : clientPPFList) {
						try {
							PortfolioOverviewDto output = new PortfolioOverviewDto();
							masterProductClassification = clientPPF.getMasterProductClassification();
							output.setProductName(masterProductClassification.getProductName());
							output.setProductDesc("PPF");
							// populating class and type description
							classLevelDescription(output, masterProductClassification);
							populateFamilyRelation(
									clientPPF.getClientFamilyMember().getLookupRelation().getDescription(), output);
							output.setPortFolioId(clientPPF.getId());
							PPFFixedAmountService ppffd = new PPFFixedAmountService();
							PPFFixedAmountDeposit pffixedAmount = ppffd.getPPFFixedAmountCalculationDetails(clientPPF.getCurrentBalance().doubleValue(),
									clientPPF.getPlannedDepositAmount().doubleValue(), "", clientPPF.getPpfTenure(),
									clientPPF.getLookupFrequency1().getId(), clientPPF.getLookupFrequency2().getId(),
									clientPPF.getStartDate());
							output.setInvestmentOrPersonFlag(masterProductClassification.getInvestmentAssetsFlag());
							if (masterProductClassification.getInvestmentAssetsFlag().equals("Y")) {
								output.setInvestmentOrPerson("Investment Assets");
							} else {
								output.setInvestmentOrPerson("NA");
							}

							if (masterProductClassification.getLockedInFlag().equals("Y")) {
								output.setLockedInDate(simpleDateFormat.format(pffixedAmount.getMaturityDate()));
								output.setMaturityDate(simpleDateFormat.format(pffixedAmount.getMaturityDate()));
								double diff = FinanceUtil.YEARFRAC(currentDate.getTime(),
										pffixedAmount.getMaturityDate(), 1);
								output.setTimeToMaturity(String.valueOf(diff));

								// check st lt classification
								output = getSTLTClassificationCheck(output, diff);

							}

							// check and populate market linked
							output = getMarketLinkedCheck(output, masterProductClassification.getMarketLinkedFlag());
							/*double sumAmountDeposit = 0.0;
							for (PPFFixedAmountLookup ppfFixedLookup : pffixedAmount.getPpfFixedAmountLookupList()) {
								variableCal.setTime(simpleDateFormat.parse(ppfFixedLookup.getReferenceDate()));

								if (variableCal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
										&& variableCal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
									if (variableCal.get(Calendar.DAY_OF_MONTH) == currentDate
											.get(Calendar.DAY_OF_MONTH)) {
										output.setCurrentValue(ppfFixedLookup.getClosingBalance());
									} else {
										output.setCurrentValue(lastValue);
									}
									if (output.getCurrentValue() == 0 && (simpleDateFormat.parse(
											pffixedAmount.getPpfFixedAmountLookupList().get(0).getReferenceDate())
											.after(currentDate.getTime()))) {
										output.setCurrentValue(clientPPF.getPlannedDepositAmount().doubleValue());
									}
									break;
								}
								sumAmountDeposit = sumAmountDeposit + ppfFixedLookup.getAmountDeposited();
								lastValue = ppfFixedLookup.getClosingBalance();
							}
							//System.out.println("clientPPF.getAmountDepositFrequencyExt() "+clientPPF.getAmountDepositFrequencyExt());
							sumAmountDeposit = ppffd.getPPFFixedAmountDepositDetails(
									clientPPF.getPlannedDepositAmount().doubleValue(),clientPPF.getPpfTenure(),clientPPF.getStartDate(),clientPPF.getAmountDepositFrequencyExt());*/
							//System.out.println("sumAmountDepositfgfdgfd "+sumAmountDeposit);
							output.setCurrentValue(pffixedAmount.getCurrentBalance());
							if (clientPPF.getExtensionFlag().equals("Y")) {
								pffixedAmount = new PPFFixedAmountService().getPPFExtensionCalcuation(
										clientPPF.getDepositAmountExt().doubleValue(), "Y",
										clientPPF.getExtensionTenure(), clientPPF.getAmountDepositFrequencyExt(),
										clientPPF.getExtensionCompoundingFrequency(), clientPPF.getExtensionStartDate(),
										pffixedAmount.getMaturityAmount(), pffixedAmount.getTotalAmountDeposited());
								
							}
							output.setCurrentValue(pffixedAmount.getCurrentBalance());
							output.setInvestmentDate(simpleDateFormat.format(clientPPF.getStartDate()));
							//output.setInvestmentValue(sumAmountDeposit);
							output.setInvestmentValue(0);
							output.setCagr(String.valueOf(pffixedAmount.getAnnualInterest() * 100));
							output.setPtpReturns("N/A");
							//output.setGains(output.getCurrentValue() - output.getInvestmentValue());
							output.setGains(0);
							output.setMaturityDate(simpleDateFormat.format(pffixedAmount.getMaturityDate()));
							output.setMaturityAmount(pffixedAmount.getMaturityAmount());
							if (!Double.isNaN(output.getCurrentValue())) {
								totalCurrentValue = totalCurrentValue + output.getCurrentValue();
							}
							output.setFinancialAssetName(output.getPortFolioId() + "-" + output.getProductName() + "-"
									+ output.getProductDesc());
							outputList.add(output);
						} catch (Exception exp) {
							exp.printStackTrace();
							FinexaBussinessException finexaBuss = new FinexaBussinessException(
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
									FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
							FinexaBussinessException.logFinexaBusinessException(finexaBuss);
						}

					}
				} catch (Exception exp) {
					exp.printStackTrace();
					FinexaBussinessException finexaBuss = new FinexaBussinessException(
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE,
							FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
					FinexaBussinessException.logFinexaBusinessException(finexaBuss);
				}
				//************************ End of PPF *************************//*
				

				

				// <<<<<<<<<<<< Retirement Products end >>>>>>>>>>>>>>>>

				
			
		} catch (Exception exp) {
			exp.printStackTrace();
			FinexaBussinessException finexaBuss = new FinexaBussinessException(FinexaConstant.PORTFOLIO_OVERVIEW_MODULE,
					FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_CODE, FinexaConstant.PORTFOLIO_OVERVIEW_MODULE_DESC, exp);
			FinexaBussinessException.logFinexaBusinessException(finexaBuss);
		} finally {

			
		}

		return outputList;
	}
}

