package com.finlabs.finexa.jasper.report;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finlabs.finexa.dto.ClientRiskDTO;
import com.finlabs.finexa.dto.PortfolioAssetAllocationReviewDto;
import com.finlabs.finexa.dto.PortfolioCurrentAssetAllocationForReportDTO;
import com.finlabs.finexa.dto.PortfolioCurrentAssetAllocationPieDataDTO;
import com.finlabs.finexa.dto.PortfolioCurrentSubAssetAllocationPieDataDTO;
import com.finlabs.finexa.dto.PortfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO;
import com.finlabs.finexa.dto.PortfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO;
import com.finlabs.finexa.dto.PortfolioNetWorthAssetPieDataDTO;
import com.finlabs.finexa.dto.PortfolioNetWorthForReportDTO;
import com.finlabs.finexa.dto.PortfolioNetWorthLiabilitiesPieDataDTO;
import com.finlabs.finexa.dto.PortfolioOvervieEquityDto;
import com.finlabs.finexa.dto.PortfolioOverviewByAssetMaturityBarDataDTO;
import com.finlabs.finexa.dto.PortfolioOverviewBySectorsBarDataDTO;
import com.finlabs.finexa.dto.PortfolioOverviewDebtByAssetQualityPieDataDTO;
import com.finlabs.finexa.dto.PortfolioOverviewDebtByMarketTypePieDataDTO;
import com.finlabs.finexa.dto.PortfolioOverviewDebtTableDataForReportDTO;
import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.dto.PortfolioOverviewEquityByMarketTypePieDataDTO;
import com.finlabs.finexa.dto.PortfolioOverviewEquityByProductTypePieDataDTO;
import com.finlabs.finexa.dto.PortfolioOverviewEquitySectorDto;
import com.finlabs.finexa.dto.PortfolioOverviewEquityTableDataForReportDTO;
import com.finlabs.finexa.dto.PortfolioOverviewForReportDTO;
import com.finlabs.finexa.dto.PortfolioRatiosDto;
import com.finlabs.finexa.dto.PortfolioRecommendationForReportDTO;
import com.finlabs.finexa.dto.PortfolioRecommendedAssetAllocationForReportDTO;
import com.finlabs.finexa.dto.PortfolioReportDTO;
import com.finlabs.finexa.dto.PortfolioWealthRatiosForReportDTO;
import com.finlabs.finexa.dto.ProductRecommendationDTO;
import com.finlabs.finexa.dto.ProductRecommendationForReportDTO;
import com.finlabs.finexa.dto.clientinfo.ClientContactDTO;
import com.finlabs.finexa.dto.clientinfo.ClientFamilyMemberDTO;
import com.finlabs.finexa.dto.clientinfo.ClientMasterDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.PortfolioNetWorthDto;
import com.finlabs.finexa.pm.util.FinexaConstant;
import com.finlabs.finexa.pm.util.PortfolioReportUtil;
import com.finlabs.finexa.repository.AdvisorProductRecoRepository;
import com.finlabs.finexa.repository.ClientMasterRepository;
import com.finlabs.finexa.service.PortFolioAssetAllocationReviewService;
import com.finlabs.finexa.service.PortFolioNetWorthService;
import com.finlabs.finexa.service.PortFolioOverviewService;
import com.finlabs.finexa.service.PortFolioRatiosService;
import com.finlabs.finexa.service.impl.PortFolioAssetAllocationReviewServiceImpl;
import com.finlabs.finexa.service.impl.PortFolioNetWorthServiceImpl;
import com.finlabs.finexa.service.impl.PortFolioOverviewServiceImpl;
import com.finlabs.finexa.service.impl.PortFolioRatiosServiceImpl;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@RestController
public class PortfolioManagementReportController {

	Logger log = Logger.getLogger(PortfolioManagementReportController.class.getName());
	
	private Format numberFormatter = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("hi", "in"));
	
	private static final String CURRENT_PAGE_NUMBER = "${CURRENT_PAGE_NUMBER}";
	private static final String TOTAL_PAGE_NUMBER = "${TOTAL_PAGE_NUMBER}";
	private static final String NOTES_AND_DISCLAIMER_PAGE_NUMBER = "${NOTES_AND_DISCLAIMER_PAGE_NUMBER}";
	private static final String INTRODUCTION_PAGE_NUMBER = "${INTRODUCTION_PAGE_NUMBER}";
	private static final String PERSONAL_DETAILS_PAGE_NUMBER = "${PERSONAL_DETAILS_PAGE_NUMBER}";
	private static final String PORTFOLIO_TRACKER_PAGE_NUMBER = "${PORTFOLIO_TRACKER_PAGE_NUMBER}";
	private static final String PORTFOLIO_NETWORTH_PAGE_NUMBER = "${PORTFOLIO_NETWORTH_PAGE_NUMBER}";
	private static final String PORTFOLIO_WEALTH_RATIOS_AND_RISK_PROFILE = "${PORTFOLIO_WEALTH_RATIOS_AND_RISK_PROFILE_PAGE_NUMBER}";
	private static final String PORTFOLIO_CURRENT_ASSET_ALLOCATION = "${PORTFOLIO_CURRENT_ASSET_ALLOCATION_PAGE_NUMBER}";
	private static final String PORTFOLIO_RECOMMENDED_ASSET_ALLOCATION = "${PORTFOLIO_RECOMMENDED_ASSET_ALLOCATION_PAGE_NUMBER}";
	private static final String PORTFOLIO_OVERVIEW_EQUITY = "${PORTFOLIO_OVERVIEW_EQUITY_PAGE_NUMBER}";
	private static final String PORTFOLIO_OVERVIEW_DEBT = "${PORTFOLIO_OVERVIEW_DEBT_PAGE_NUMBER}";
	private static final String PORTFOLIO_RECOMMENDATION = "${PORTFOLIO_RECOMMENDATION_PAGE_NUMBER}";
	private static final String PRODUCT_RECOMMENDATION = "${PRODUCT_RECOMMENDATION_PAGE_NUMBER}";

	private static String clientNameForFile = "";
	private static String reportFileNamePart = "";
	
	@Autowired
	Mapper mapper;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private SessionFactory sessionfactory;
	
	@Autowired
	private CacheInfoService cacheInfoService;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;
	
	@Autowired
	private AdvisorProductRecoRepository advisorProductRecoRepository;
	
	@RequestMapping(value = "/generatePortfolioManagementReport", method = RequestMethod.POST)
	public ResponseEntity<?> generatePortfolioManagementReport(HttpServletResponse response, @RequestBody PortfolioReportDTO portfolioReportDTO, HttpServletRequest request) throws IOException, JRException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException, FinexaBussinessException {
		
		Session session = null;
		String header = "", token = "";
		try {
			session = sessionfactory.openSession();
			header = request.getHeader(FinexaConstant.HEADER_STRING);
			token = cacheInfoService.getToken(header);
			portfolioReportDTO = generateReport(token, session, response, portfolioReportDTO);
			return new ResponseEntity<PortfolioReportDTO>(portfolioReportDTO, HttpStatus.OK);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinexaBussinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new FinexaBussinessException("Financial Planning", "FP-R01", e.toString());
		} finally {
			session.close();
		}
		
		return new ResponseEntity<PortfolioReportDTO>(portfolioReportDTO, HttpStatus.OK);
		
	}
	
	private PortfolioReportDTO generateReport(String token, Session session, HttpServletResponse response, PortfolioReportDTO portfolioReportDTO) throws IOException, JRException, FinexaBussinessException {
		
		String returnStatus = "";
		String fileName = "";
		
		try {
			PortFolioOverviewService portFolioOverviewService = null;
			List<PortfolioOverviewDto> portfolioOverviewDtoList = null;
			List<PortfolioRatiosDto> portfolioRatiosDtoList = null;
			double totalCurrentVal = 0.0d;
			double totalInvestVal = 0.0d;
			double percentageOfTotal = 0.0d;
			double totalGainLoss = 0.0d;
			double totalCagrXirr = 0.0d;
			
			PortfolioOverviewForReportDTO portfolioOverviewForReportDTO = null;
			PortfolioNetWorthDto portfolioNetworthDto = null;
			PortFolioNetWorthService portFolioNetWorthService = null;
			PortFolioRatiosService portFolioRatiosService = null;
			PortfolioNetWorthForReportDTO portfolioNetWorthForReportDTO = null;
			PortfolioNetWorthAssetPieDataDTO portfolioNetWorthAssetPieDataDTO = null;
			PortfolioNetWorthLiabilitiesPieDataDTO portfolioNetWorthLiabilitiesPieDataDTO = null;
			PortfolioAssetAllocationReviewDto portfolioAssetAllocationReviewDto = null;
			PortFolioAssetAllocationReviewService portFolioAssetAllocationReviewService = null;
			PortfolioCurrentAssetAllocationForReportDTO portfolioCurrentAssetAllocationForReportDTO = null;
			ClientRiskDTO riskOutput = null;
			List<PortfolioAssetAllocationReviewDto> portfolioAssetAllocationReviewDtoList = null;
			List<PortfolioRecommendedAssetAllocationForReportDTO> portfolioRecommendedAssetAllocationForReportDTOList = null;
			PortfolioRecommendedAssetAllocationForReportDTO portfolioRecommendedAssetAllocationForReportDTO = null;
			List<PortfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO> portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTOList = null;
			PortfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO = null;
			List<PortfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO> portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTOList = null;
			PortfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO = null;
			PortfolioOvervieEquityDto portfolioOvervieEquityDto = null;
			PortfolioOverviewEquityTableDataForReportDTO portfolioOverviewEquityTableDataForReportDTO = null;
			List<PortfolioOverviewEquityTableDataForReportDTO> portfolioOverviewEquityTableDataForReportDTOList = null;
			PortfolioOverviewEquityByProductTypePieDataDTO portfolioOverviewEquityByProductTypePieDataDTO = null;
			List<PortfolioOverviewEquityByProductTypePieDataDTO> portfolioOverviewEquityByProductTypePieDataDTOList = null;
			PortfolioOverviewEquityByMarketTypePieDataDTO portfolioOverviewEquityByMarketTypePieDataDTO = null;
			List<PortfolioOverviewEquityByMarketTypePieDataDTO> portfolioOverviewEquityByMarketTypePieDataDTOList = null;
			PortfolioOverviewBySectorsBarDataDTO portfolioOverviewBySectorsBarDataDTO = null;
			List<PortfolioOverviewBySectorsBarDataDTO> portfolioOverviewBySectorsBarDataDTOList = null;
			PortfolioOverviewDebtTableDataForReportDTO portfolioOverviewDebtTableDataForReportDTO = null;
			List<PortfolioOverviewDebtTableDataForReportDTO> portfolioOverviewDebtTableDataForReportDTOList = null;
			PortfolioOverviewDebtByMarketTypePieDataDTO portfolioOverviewDebtByMarketTypePieDataDTO = null;
			List<PortfolioOverviewDebtByMarketTypePieDataDTO> portfolioOverviewDebtByMarketTypePieDataDTOList = null;
			PortfolioOverviewDebtByAssetQualityPieDataDTO portfolioOverviewDebtByAssetQualityPieDataDTO = null;
			List<PortfolioOverviewDebtByAssetQualityPieDataDTO> portfolioOverviewDebtByAssetQualityPieDataDTOList = null;
			PortfolioOverviewByAssetMaturityBarDataDTO portfolioOverviewByAssetMaturityBarDataDTO = null;
			List<PortfolioOverviewByAssetMaturityBarDataDTO> portfolioOverviewByAssetMaturityBarDataDTOList = null;
			PortfolioRecommendationForReportDTO portfolioRecommendationForReportDTO = null;
			List<PortfolioRecommendationForReportDTO> portfolioRecommendationForReportDTOList = null;
			List<ProductRecommendationDTO> productRecommendationDTOList = null;
			ProductRecommendationForReportDTO productRecommendationForReportDTO = null;
			List<ProductRecommendationForReportDTO> productRecommendationForReportDTOList = null;
			String clientFirstName = "";
			String clientMiddleName = "";
			String clientLastName = "";
			String clientName = "";
			
			ClientMaster cm = clientMasterRepository.findOne(portfolioReportDTO.getClientId());
			
			reportFileNamePart = portfolioReportDTO.getTimestamp();
			
			if(cm != null) {
				clientNameForFile = cm.getSalutation() + " " + cm.getFirstName() + " " + (cm.getMiddleName().equals("")?"":cm.getMiddleName() + " ") + cm.getLastName();
				response.setContentType("application/x-download");
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"CrispPMReport_"+ portfolioReportDTO.getTimestamp() + "(" + clientNameForFile + ").docx\""));
				fileName = "CrispPMReport_" + portfolioReportDTO.getTimestamp() + "(" + clientNameForFile + ").docx";
			}
			
			LinkedList<JasperPrint> jasperPrintList = new LinkedList<>();
			
			String advisorName = null;
			Map<String, Object> parameters = null;
			BufferedImage pageHeaderCenterRightElement = null;;
			try {
				/********Title Page Starts********/
				String advisorFirstName = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getAdvisorDTO().getFirstName());
				String advisorMiddleName = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getAdvisorDTO().getMiddleName());
				String advisorLastName = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getAdvisorDTO().getLastName());
				advisorName = (advisorFirstName == null ? "" : advisorFirstName + " ") 
						+ (advisorMiddleName == null ? "" : advisorMiddleName + " ")
						+ (advisorLastName == null ? "" : advisorLastName + " ");
				
				String advisorOrgName = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getAdvisorDTO().getOrgName());
				String advisorEmailId = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getAdvisorDTO().getEmailID());
				String advisorMobileNo = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getAdvisorDTO().getPhoneNo());
				
				parameters = new HashMap<>();
				String path = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportTitle.jrxml").getURI().getPath();
				JasperReport jasperReportTitle = JasperCompileManager.compileReport(path);
				
				BufferedImage logoImage = null;
				
				if(cm != null) {
					if(cm.getAdvisorUser().getLogo() != null) {
						ByteArrayInputStream bis = new ByteArrayInputStream(cm.getAdvisorUser().getLogo());
						logoImage = ImageIO.read(bis);
						parameters.put("logoImage", logoImage);
					}
				}
				
				BufferedImage titleImage = ImageIO.read(new File(resourceLoader.getResource("classpath:images/portfolioManagementCoverPageImage.png").getURI().getPath()));
				BufferedImage finexaLogoImage = ImageIO.read(new File(resourceLoader.getResource("classpath:images/finexa-logo.jpg").getURI().getPath()));
				pageHeaderCenterRightElement = ImageIO.read(new File(resourceLoader.getResource("classpath:images/pageHeaderCenterRightElement.png").getURI().getPath()));
				
				parameters.put("titleBg", titleImage);
				parameters.put("finexaLogo", finexaLogoImage);
				parameters.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				parameters.put("advisorOrg", advisorOrgName);
				parameters.put("advisorEmail", advisorEmailId);
				parameters.put("advisorMobile", advisorMobileNo);
				
				JasperPrint jasperPrintTitle = JasperFillManager.fillReport(jasperReportTitle, parameters, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintTitle);
				/********Title Page Ends********/
			} catch (Exception e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
			
			try {
				/********Table Of Contents Page Starts********/
				String pathTOCReport = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportTOC.jrxml").getURI().getPath();
				JasperReport jasperReportTOC = JasperCompileManager.compileReport(pathTOCReport);
				
				JasperPrint jasperPrintTOC = JasperFillManager.fillReport(jasperReportTOC, parameters, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintTOC);
				/********Table Of Contents Page Ends********/
			} catch (Exception e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
			
			try {
				/********Notes & Disclaimer Page Starts********/
				String pathNotesAndDisclaimer = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportNotesAndDisclaimers.jrxml").getURI().getPath();
				JasperReport jasperReportNotesAndDisclaimer = JasperCompileManager.compileReport(pathNotesAndDisclaimer);
				Map<String, Object> parametersNotesAndDisclaimer = new HashMap<>();
				parametersNotesAndDisclaimer.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				JasperPrint jasperPrintNotesAndDisclaimer = JasperFillManager.fillReport(jasperReportNotesAndDisclaimer, parametersNotesAndDisclaimer, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintNotesAndDisclaimer);
				/********Notes & Disclaimer Page Ends********/
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			try {
				/********Introduction Page Starts********/
				String pathIntroduction = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportIntroduction.jrxml").getURI().getPath();
				JasperReport jasperReportIntroduction = JasperCompileManager.compileReport(pathIntroduction);
				
				Map<String, Object> parametersIntroduction = new HashMap<>();
				
				//String clientSalutation = convertNullToEmpty(financialReportDTO.getClientMasterDTO().getSalutation());

				clientFirstName = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getClientMasterDTO().getFirstName());

				clientMiddleName = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getClientMasterDTO().getMiddleName());

				clientLastName = PortfolioReportUtil.convertNullToEmpty(portfolioReportDTO.getClientMasterDTO().getLastName());

				clientName = clientFirstName.trim() + " "
						+ (clientMiddleName == null ? "" : clientMiddleName.trim() + " ") + clientLastName.trim();
				
				parametersIntroduction.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				parametersIntroduction.put("clientName", clientName);
				parametersIntroduction.put("advisorName", advisorName);
				JasperPrint jasperPrintIntroduction = JasperFillManager.fillReport(jasperReportIntroduction, parametersIntroduction, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintIntroduction);
				/********Introduction Page Ends********/
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			try {
				/********Personal Details Page Starts********/
				List<ClientMasterDTO> clientMasterDTOList = new ArrayList<ClientMasterDTO>();
				
				ClientMasterDTO clientMasterDTO = new ClientMasterDTO();
				clientMasterDTO.setClientFullName(clientName);
				clientMasterDTO.setGender(portfolioReportDTO.getClientMasterDTO().getGender());
				clientMasterDTO.setDateOfBirth(portfolioReportDTO.getClientMasterDTO().getDateOfBirth());
				clientMasterDTO.setMaritalStatusDesc(portfolioReportDTO.getClientMasterDTO().getMaritalStatusDesc());
				clientMasterDTO.setResidentTypeName(portfolioReportDTO.getClientMasterDTO().getResidentTypeName());
				clientMasterDTO.setRetiredFlag(portfolioReportDTO.getClientMasterDTO().getRetiredFlag());
				clientMasterDTO.setRetirementAge(portfolioReportDTO.getClientMasterDTO().getRetirementAge());
				
				clientMasterDTOList.add(clientMasterDTO);
				JRBeanCollectionDataSource jrBeanCollectionDataSourceClientDetails = new JRBeanCollectionDataSource(clientMasterDTOList);
				String pathClientDetailsSR = resourceLoader.getResource("classpath:portfolioManagementReports/clientDetailsSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportClientDetailsSR = JasperCompileManager.compileReport(pathClientDetailsSR);
				
				List<ClientFamilyMemberDTO> clientFamilyMembersDTOList = portfolioReportDTO.getClientMasterDTO().getClientFamilyMembersDTO();
				List<ClientFamilyMemberDTO> clientFamilyMemberDTOListForReport = new ArrayList<ClientFamilyMemberDTO>();
				if (clientFamilyMembersDTOList.size() > 0) {
				    for (ClientFamilyMemberDTO clientFamilyMemberDTO : clientFamilyMembersDTOList) {
				    	if(clientFamilyMemberDTO.getRelationID() != 0) {
				    		ClientFamilyMemberDTO familyMemberDTO = new ClientFamilyMemberDTO();
				    		String famFirstName = PortfolioReportUtil.convertNullToEmpty(clientFamilyMemberDTO.getFirstName());
				    		String famMiddleName = PortfolioReportUtil.convertNullToEmpty(clientFamilyMemberDTO.getMiddleName());
				    		String famLastName = PortfolioReportUtil.convertNullToEmpty(clientFamilyMemberDTO.getLastName());
				    		String famFullName = famFirstName.trim() + " "
									+ (famMiddleName == null ? "" : famMiddleName.trim() + " ") + famLastName.trim();
				    		familyMemberDTO.setFmFullName(famFullName);
				    		familyMemberDTO.setRelationName(clientFamilyMemberDTO.getRelationName());
				    		familyMemberDTO.setDateOfBirth(clientFamilyMemberDTO.getDateOfBirth());
				    		familyMemberDTO.setDependentFlag(clientFamilyMemberDTO.getDependentFlag());
				    		clientFamilyMemberDTOListForReport.add(familyMemberDTO);
				    	}
				    }
				}
				JRBeanCollectionDataSource jrBeanCollectionDataSourceFamilyDetails = new JRBeanCollectionDataSource(clientFamilyMemberDTOListForReport);
				String pathFamilyDetailsSR = resourceLoader.getResource("classpath:portfolioManagementReports/familyDetailsSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportFamilyDetailsSR = JasperCompileManager.compileReport(pathFamilyDetailsSR);
				
				List<ClientContactDTO> clientContactDTOListForReport = new ArrayList<ClientContactDTO>();
				if (portfolioReportDTO.getClientMasterDTO().getClientContactsDTO() != null) {
					ClientContactDTO clientContactDTOForReport = new ClientContactDTO();
					ClientContactDTO clientContactDTO = (ClientContactDTO) portfolioReportDTO.getClientMasterDTO()
				            .getClientContactsDTO().get(0);

				    String address = ((clientContactDTO.getPermanentAddressLine1() != null && !clientContactDTO.getPermanentAddressLine1().isEmpty())
				            ? clientContactDTO.getPermanentAddressLine1()
				            : "");
				    address += ((clientContactDTO.getPermanentAddressLine2() != null && !clientContactDTO.getPermanentAddressLine2().isEmpty())
				            ? "," + clientContactDTO.getPermanentAddressLine2()
				            : "");
				    address += ((clientContactDTO.getPermanentAddressLine3() != null && !clientContactDTO.getPermanentAddressLine3().isEmpty())
				            ? "," + clientContactDTO.getPermanentAddressLine3()
				            : "");
				    address += ((PortfolioReportUtil.capitailizeWord(clientContactDTO.getPermanentState()) != null) ? "," + PortfolioReportUtil.capitailizeWord(clientContactDTO.getPermanentState())
				    : "");

				    address += ((clientContactDTO.getPermanentCity() != null) ? "," + clientContactDTO.getPermanentCity() : "");
				   
				    address += ((clientContactDTO.getPermanentPincode() > 0) ? "-" + clientContactDTO.getPermanentPincode()
				    : "");
				    
				    if(clientContactDTO.getPermanentAddressLine1().isEmpty()) {
				    	address = "Not Available";
				    }
				    
				    System.out.println("address: " + address);
				    
				    clientContactDTOForReport.setAddress(address);
				    clientContactDTOForReport.setEmailID(clientContactDTO.getEmailID());
				    clientContactDTOForReport.setPhoneNumber(clientContactDTO.getMobile().toString());
				 
				    clientContactDTOListForReport.add(clientContactDTOForReport);
				    
				}
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceContactDetails = new JRBeanCollectionDataSource(clientContactDTOListForReport);
				String pathContactDetailsSR = resourceLoader.getResource("classpath:portfolioManagementReports/contactDetailsSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportContactDetailsSR = JasperCompileManager.compileReport(pathContactDetailsSR);
				
				String pathPersonalDetails = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportPersonalDetails.jrxml").getURI().getPath();
				JasperReport jasperReportPersonalDetails = JasperCompileManager.compileReport(pathPersonalDetails);
				
				Map<String, Object> parametersPersonalDetails = new HashMap<>();
				parametersPersonalDetails.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				parametersPersonalDetails.put("jasperReportClientDetailsSR", jasperReportClientDetailsSR);
				parametersPersonalDetails.put("clientDetailsSubReportDetails", jrBeanCollectionDataSourceClientDetails);
				parametersPersonalDetails.put("jasperReportFamilyDetailsSR", jasperReportFamilyDetailsSR);
				parametersPersonalDetails.put("familyDetailsSubReportDetails", jrBeanCollectionDataSourceFamilyDetails);
				parametersPersonalDetails.put("jasperReportContactDetailsSR", jasperReportContactDetailsSR);
				parametersPersonalDetails.put("contactDetailsSubReportDetails", jrBeanCollectionDataSourceContactDetails);
				
				JasperPrint jasperPrintPersonalDetails = JasperFillManager.fillReport(jasperReportPersonalDetails, parametersPersonalDetails, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintPersonalDetails);
				/********Personal Details Page Ends********/
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//services
			portFolioOverviewService = new PortFolioOverviewServiceImpl();
			portFolioNetWorthService = new PortFolioNetWorthServiceImpl();
			portFolioRatiosService = new PortFolioRatiosServiceImpl();
			portFolioAssetAllocationReviewService = new PortFolioAssetAllocationReviewServiceImpl();
			
			try {
				/********Portfolio Tracker Page Starts********/
				List<PortfolioOverviewForReportDTO> portfolioOverviewForReportDTOList = new ArrayList<PortfolioOverviewForReportDTO>();
				portfolioOverviewDtoList = portFolioOverviewService.getclientPortfolioOverview(token, portfolioReportDTO.getClientId(),
						FinexaConstant.PORTFOLIO_TRACKER_SPECIFIC_REQUIREMENT_STAT, session, cacheInfoService);
				
				if(portfolioOverviewDtoList != null && portfolioOverviewDtoList.size() > 0) {
					for(PortfolioOverviewDto portfolioTracker : portfolioOverviewDtoList) {
						if (portfolioTracker.getInvestmentOrPersonFlag().equals("Y")) {
							totalCurrentVal = totalCurrentVal + portfolioTracker.getCurrentValue();
							totalInvestVal = totalInvestVal + portfolioTracker.getInvestmentValue();
						}
					}
					
					for(PortfolioOverviewDto portfolioTracker : portfolioOverviewDtoList) {
						if(portfolioTracker.getInvestmentOrPersonFlag().equals("Y")) {
							portfolioOverviewForReportDTO = new PortfolioOverviewForReportDTO();
							portfolioOverviewForReportDTO.setProductName(portfolioTracker.getProductName());
							portfolioOverviewForReportDTO.setProductType(portfolioTracker.getProductType());
							String currentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioTracker.getCurrentValue(), 0));
							String currentValueToShow = currentValueFormatted.substring(0,  currentValueFormatted.length() - 3);
							portfolioOverviewForReportDTO.setCurrentValue(currentValueToShow);
							percentageOfTotal = (portfolioTracker.getCurrentValue() / totalCurrentVal) * 100;
							portfolioOverviewForReportDTO.setPercentageOfTotal(String.valueOf(PortfolioReportUtil.roundOff(percentageOfTotal, 2)));
							
							if(portfolioTracker.getProductId() == 12 || portfolioTracker.getProductId() == 13 || portfolioTracker.getProductId() == 14 || portfolioTracker.getProductId() == 33){
								portfolioOverviewForReportDTO.setInvestmentValue("N/A");
								portfolioOverviewForReportDTO.setGainLoss("N/A");
							}else{
								if(portfolioTracker.getInvestmentValue() < 0) {
									String investmentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioTracker.getInvestmentValue(), 0));
									String investmentValueToShow = investmentValueFormatted.substring(0, investmentValueFormatted.length() - 3);
									portfolioOverviewForReportDTO.setInvestmentValue(investmentValueToShow);
								} else {
									if(portfolioTracker.getInvestmentValue() == 0) {
										portfolioOverviewForReportDTO.setInvestmentValue("N/A");
									} else {
										String investmentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioTracker.getInvestmentValue(), 0));
										String investmentValueToShow = investmentValueFormatted.substring(0, investmentValueFormatted.length() - 3);
										portfolioOverviewForReportDTO.setInvestmentValue(investmentValueToShow);
									}
								}
								
								if(portfolioTracker.getGains() < 0) {
									String gainLossValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioTracker.getGains(), 0));
									String gainLossValueToShow = gainLossValueFormatted.substring(0,  gainLossValueFormatted.length() - 3);
									portfolioOverviewForReportDTO.setGainLoss(gainLossValueToShow);
								} else {
									if(portfolioTracker.getGains() == 0) {
										portfolioOverviewForReportDTO.setGainLoss("0");
									} else {
										String gainLossValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioTracker.getGains(), 0));
										String gainLossValueToShow = gainLossValueFormatted.substring(0,  gainLossValueFormatted.length() - 3);
										portfolioOverviewForReportDTO.setGainLoss(gainLossValueToShow);
									}
								}
								
								totalGainLoss += Math.round(portfolioTracker.getGains());
							}
							
							if(portfolioTracker.getCagr() != null && !portfolioTracker.getCagr().equals("N/A")) {
								totalCagrXirr += (Double.parseDouble(portfolioTracker.getCagr())/100) * percentageOfTotal;
							}
							
							if(!portfolioTracker.getCagr().equals("N/A")) {
								if(portfolioTracker.getCagr() != null && PortfolioReportUtil.roundOff(Double.parseDouble(portfolioTracker.getCagr()), 0) < 0) {
									portfolioOverviewForReportDTO.setCagrXirr(String.valueOf(PortfolioReportUtil.roundOff(Double.parseDouble(portfolioTracker.getCagr()), 2)));
								} else {
									if(portfolioTracker.getCagr().equals("-1") || portfolioTracker.getCagr() == null) {
										portfolioOverviewForReportDTO.setCagrXirr("N/A");
									} else {
										if(portfolioTracker.getCagr().equals("0")) {
											portfolioOverviewForReportDTO.setCagrXirr("0.00");
										} else {
											portfolioOverviewForReportDTO.setCagrXirr(String.valueOf(PortfolioReportUtil.roundOff(Double.parseDouble(portfolioTracker.getCagr()), 2)));
										}
									}
								}
							}else {
								portfolioOverviewForReportDTO.setCagrXirr("N/A");
							}
							
							
							portfolioOverviewForReportDTOList.add(portfolioOverviewForReportDTO);
						}
					}
					
					portfolioOverviewForReportDTO = new PortfolioOverviewForReportDTO();
					portfolioOverviewForReportDTO.setProductName(" Total");
					portfolioOverviewForReportDTO.setProductType("");
					String totalCurrentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalCurrentVal, 0));
					String totalCurrentValueToShow = totalCurrentValueFormatted.substring(0, totalCurrentValueFormatted.length() - 3);
					portfolioOverviewForReportDTO.setCurrentValue(totalCurrentValueToShow);
					portfolioOverviewForReportDTO.setPercentageOfTotal("100");
					String totalInvestmentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalInvestVal, 0));
					String totalInvestmentValueToShow = totalInvestmentValueFormatted.substring(0, totalInvestmentValueFormatted.length() - 3);
					portfolioOverviewForReportDTO.setInvestmentValue(totalInvestmentValueToShow);
					String totalGainLossFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalGainLoss, 0));
					String totalGainLossToShow = totalGainLossFormatted.substring(0, totalGainLossFormatted.length() - 3);
					portfolioOverviewForReportDTO.setGainLoss(totalGainLossToShow);
					String totalCagrXirrFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalCagrXirr, 0));
					String totamCagrXirrToShow = totalCagrXirrFormatted.substring(0, totalCagrXirrFormatted.length() - 3);
					portfolioOverviewForReportDTO.setCagrXirr(totamCagrXirrToShow);
					portfolioOverviewForReportDTOList.add(portfolioOverviewForReportDTO);
				}
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourcePortfolioTracker = new JRBeanCollectionDataSource(portfolioOverviewForReportDTOList);
				String pathPortfolioTrackerSR = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioTrackerSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioTrackerSR = JasperCompileManager.compileReport(pathPortfolioTrackerSR);
				
				Map<String, Object> parametersPortfolioTracker = new HashMap<>();
				parametersPortfolioTracker.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				parametersPortfolioTracker.put("jasperReportPortfolioTrackerSR", jasperReportPortfolioTrackerSR);
				parametersPortfolioTracker.put("portfolioTrackerSubReportDetails", jrBeanCollectionDataSourcePortfolioTracker);
				
				String pathPortfolioTracker = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportPortfolioTracker.jrxml").getURI().getPath();			
				JasperReport jasperReportPortfolioTracker = JasperCompileManager.compileReport(pathPortfolioTracker);
				
				JasperPrint jasperPrintPortfolioTracker = JasperFillManager.fillReport(jasperReportPortfolioTracker, parametersPortfolioTracker, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintPortfolioTracker);
				/********Portfolio Tracker Page Ends********/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/********Net Worth Page Starts********/
			try {
				Double totalAssetValue = 0.0d, personal = 0.0d, liabilities = 0.0d, investment = 0.0d;
				List<PortfolioNetWorthForReportDTO> portfolioNetWorthDetails = new ArrayList<PortfolioNetWorthForReportDTO>();
				portfolioNetworthDto = portFolioNetWorthService.getclientPortfolioNetWorth(token, portfolioReportDTO.getClientId(), session, cacheInfoService);
				
				for(Map.Entry<String, Double> totalTypeValueMapEntry : portfolioNetworthDto.getTotaltypeValueMap().entrySet()) {
					if(totalTypeValueMapEntry.getKey().equals(FinexaConstant.PERSONAL_REPORT)) {
						personal = totalTypeValueMapEntry.getValue();
					}
					if(totalTypeValueMapEntry.getKey().equals(FinexaConstant.LIABILITIES_REPORT)) {
						liabilities = totalTypeValueMapEntry.getValue();
					}
					if(totalTypeValueMapEntry.getKey().equals(FinexaConstant.INVESTMENT_REPORT)) {
						investment = totalTypeValueMapEntry.getValue();
					}
				}
				
				totalAssetValue = personal + investment;
				
				List<PortfolioNetWorthAssetPieDataDTO> assetPieChartDataList = new ArrayList<PortfolioNetWorthAssetPieDataDTO>();
				List<PortfolioNetWorthLiabilitiesPieDataDTO> liabilitiesPieChartDataList = new ArrayList<PortfolioNetWorthLiabilitiesPieDataDTO>();
				
				for(Map.Entry<String, Map<String, Map<String, List<PortfolioNetWorthDto>>>> rootMapEntry : portfolioNetworthDto.getRootMap().entrySet()) {
					if(rootMapEntry.getKey().equalsIgnoreCase(FinexaConstant.ASSETS_REPORT)) {
						for(Map.Entry<String, Map<String, List<PortfolioNetWorthDto>>> assetMapEntry : rootMapEntry.getValue().entrySet()) {
							if(assetMapEntry.getKey().equalsIgnoreCase(FinexaConstant.INVESTMENT_ASSETS_REPORT)) {
								double totalAssetType = 0.0d;
								portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
								portfolioNetWorthForReportDTO.setAssets(" " + assetMapEntry.getKey() + " (A)");
								portfolioNetWorthForReportDTO.setCurrentValue("");
								portfolioNetWorthForReportDTO.setPercentOfTotal("");
								portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
								
								for(Map.Entry<String, List<PortfolioNetWorthDto>> investmentAssetsMapEntry : assetMapEntry.getValue().entrySet()) {
									double totalProductType = 0.0d;
									portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
									if(investmentAssetsMapEntry.getKey().trim().equals("FixedIncome")) {
										portfolioNetWorthForReportDTO.setAssets("   Fixed Income");
									} else {
										portfolioNetWorthForReportDTO.setAssets("   " + investmentAssetsMapEntry.getKey().trim());
									}
									
									for(PortfolioNetWorthDto portfolioNetWorthDto : investmentAssetsMapEntry.getValue()) {
										totalProductType = ((totalProductType > 0)? totalProductType : 0.00)+((portfolioNetWorthDto.getCurrentValue() > 0)? portfolioNetWorthDto.getCurrentValue() : 0.00);
									}
									String currentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalProductType, 0));
									String currentValueToShow = currentValueFormatted.substring(0, currentValueFormatted.length() - 3);
									portfolioNetWorthForReportDTO.setCurrentValue(currentValueToShow);
									if(totalProductType > 0 && totalAssetValue > 0) {
										String percentOfTotalFormatted = numberFormatter.format(PortfolioReportUtil.roundOff((totalProductType/totalAssetValue) * 100, 2));
										String percentOfTotalToShow = percentOfTotalFormatted.substring(1, percentOfTotalFormatted.length());
										portfolioNetWorthForReportDTO.setPercentOfTotal(percentOfTotalToShow+"%");
									}
									portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
									totalAssetType =  ((totalAssetType > 0)? totalAssetType : 0.00)+((totalProductType > 0)? totalProductType : 0.00);
									//inserting pie data
									if(PortfolioReportUtil.roundOff((totalProductType/totalAssetValue) * 100, 2) > 5) {
										portfolioNetWorthAssetPieDataDTO = new PortfolioNetWorthAssetPieDataDTO();
										if(investmentAssetsMapEntry.getKey().trim().equals("FixedIncome")) {
											portfolioNetWorthAssetPieDataDTO.setAssetsKey("Fixed Income");
										} else {
											portfolioNetWorthAssetPieDataDTO.setAssetsKey(investmentAssetsMapEntry.getKey().trim());
										}
										portfolioNetWorthAssetPieDataDTO.setAssetsValue(PortfolioReportUtil.roundOff((totalProductType/totalAssetValue) * 100, 2));
										assetPieChartDataList.add(portfolioNetWorthAssetPieDataDTO);
									}
								}
								portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
								portfolioNetWorthForReportDTO.setAssets(" Total " + assetMapEntry.getKey());
								String totalCurrentValueInvestmentFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalAssetType, 0));
								String totalCurrentValueInvestmentToShow = totalCurrentValueInvestmentFormatted.substring(0, totalCurrentValueInvestmentFormatted.length() - 3);
								portfolioNetWorthForReportDTO.setCurrentValue(totalCurrentValueInvestmentToShow);
								if(totalAssetType > 0 && totalAssetValue > 0) {
									String totalPercentOfTotalInvestmentFormatted = numberFormatter.format(PortfolioReportUtil.roundOff((totalAssetType/totalAssetValue) * 100, 2));
									String totalPercentOfTotalInvestmentToShow = totalPercentOfTotalInvestmentFormatted.substring(1, totalPercentOfTotalInvestmentFormatted.length());
									portfolioNetWorthForReportDTO.setPercentOfTotal(totalPercentOfTotalInvestmentToShow+"%");
								}
								portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
							}
							
							if(assetMapEntry.getKey().equalsIgnoreCase(FinexaConstant.PERSONAL_ASSETS_REPORT)) {
								double totalAssetType = 0.0d;
								portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
								portfolioNetWorthForReportDTO.setAssets(" " + assetMapEntry.getKey() + " (B)");
								portfolioNetWorthForReportDTO.setCurrentValue("");
								portfolioNetWorthForReportDTO.setPercentOfTotal("");
								portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
								
								for(Map.Entry<String, List<PortfolioNetWorthDto>> personalAssetsMapEntry : assetMapEntry.getValue().entrySet()) {
									double totalProductType = 0.0d;
									portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
									for(PortfolioNetWorthDto portfolioNetWorthDto : personalAssetsMapEntry.getValue()) {
										if(portfolioNetWorthDto.getNetworthDetails() != null) {
											for(PortfolioNetWorthDto netWorthDto : portfolioNetWorthDto.getNetworthDetails()) {
												totalProductType = ((totalProductType > 0)? totalProductType : 0.00)+((netWorthDto.getCurrentValue() > 0)? netWorthDto.getCurrentValue() : 0.00);
											}
										}
										portfolioNetWorthForReportDTO.setAssets("   " + portfolioNetWorthDto.getProductType().trim());
										String currentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalProductType, 0));
										String currentValueToShow = currentValueFormatted.substring(0, currentValueFormatted.length() - 3);
										portfolioNetWorthForReportDTO.setCurrentValue(currentValueToShow);
										if(totalProductType > 0 && totalAssetValue > 0) {
											String percentOfTotalFormatted = numberFormatter.format(PortfolioReportUtil.roundOff((totalProductType/totalAssetValue) * 100, 2));
											String percentOfTotalToShow = percentOfTotalFormatted.substring(1, percentOfTotalFormatted.length());
											portfolioNetWorthForReportDTO.setPercentOfTotal(percentOfTotalToShow+"%");
										}
										portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
										totalAssetType =  ((totalAssetType > 0)? totalAssetType : 0.00)+((totalProductType > 0)? totalProductType : 0.00);
										//inserting pie data
										if(PortfolioReportUtil.roundOff((totalProductType/totalAssetValue) * 100, 2) > 5) {
											portfolioNetWorthAssetPieDataDTO = new PortfolioNetWorthAssetPieDataDTO();
											portfolioNetWorthAssetPieDataDTO.setAssetsKey(personalAssetsMapEntry.getKey().trim());
											portfolioNetWorthAssetPieDataDTO.setAssetsValue(PortfolioReportUtil.roundOff((totalProductType/totalAssetValue) * 100, 2));
											assetPieChartDataList.add(portfolioNetWorthAssetPieDataDTO);
										}
									}
								}
								
								portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
								portfolioNetWorthForReportDTO.setAssets(" Total " + assetMapEntry.getKey());
								String totalCurrentValuePersonalFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalAssetType, 0));
								String totalCurrentValuePersonalToShow = totalCurrentValuePersonalFormatted.substring(0, totalCurrentValuePersonalFormatted.length() - 3);
								portfolioNetWorthForReportDTO.setCurrentValue(totalCurrentValuePersonalToShow);
								if(totalAssetType > 0 && totalAssetValue > 0) {
									String totalPercentOfTotalPersonalFormatted = numberFormatter.format(PortfolioReportUtil.roundOff((totalAssetType/totalAssetValue) * 100, 2));
									String totalPercentOfTotalPersonalToShow = totalPercentOfTotalPersonalFormatted.substring(1, totalPercentOfTotalPersonalFormatted.length());
									portfolioNetWorthForReportDTO.setPercentOfTotal(totalPercentOfTotalPersonalToShow+"%");
								}
								portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
							}
							
						}
					}
				}
				
				portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
				portfolioNetWorthForReportDTO.setAssets(" Total Asset : (A) + (B)");
				String totalAssetValueFormatted = numberFormatter.format(((totalAssetValue > 0)? PortfolioReportUtil.roundOff(totalAssetValue, 0) : 0.00));
				String totalAssetValueForReport = totalAssetValueFormatted.substring(0, totalAssetValueFormatted.length() - 3);
				portfolioNetWorthForReportDTO.setCurrentValue(totalAssetValueForReport);
				portfolioNetWorthForReportDTO.setPercentOfTotal("100%");
				portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
				
				portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
				portfolioNetWorthForReportDTO.setAssets("");
				portfolioNetWorthForReportDTO.setCurrentValue("");
				portfolioNetWorthForReportDTO.setPercentOfTotal("");
				portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
				
				portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
				portfolioNetWorthForReportDTO.setAssets(" Liabilities:");
				portfolioNetWorthForReportDTO.setCurrentValue("Current Value");
				portfolioNetWorthForReportDTO.setPercentOfTotal("% of Total");
				portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
				
				portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
				portfolioNetWorthForReportDTO.setAssets(" Loans");
				portfolioNetWorthForReportDTO.setCurrentValue("");
				portfolioNetWorthForReportDTO.setPercentOfTotal("");
				portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
				
				for(Map.Entry<String, Double> typeValueMapEntry : portfolioNetworthDto.getTypeValueMap().entrySet()) {
					if(typeValueMapEntry.getKey().contains(FinexaConstant.LOAN_REPORT)) {
						portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
						portfolioNetWorthForReportDTO.setAssets(" " + typeValueMapEntry.getKey().replace("Loan", "").trim());
						String currentValueFormatted = numberFormatter.format(((typeValueMapEntry.getValue() > 0)? PortfolioReportUtil.roundOff(typeValueMapEntry.getValue(), 0) : 0));
						String currentValueToShow = currentValueFormatted.substring(0, currentValueFormatted.length() - 3);
						portfolioNetWorthForReportDTO.setCurrentValue(currentValueToShow);
						String percentOfTotalFormatted = numberFormatter.format(((typeValueMapEntry.getValue() > 0 && liabilities > 0)? PortfolioReportUtil.roundOff((typeValueMapEntry.getValue()/liabilities) * 100, 2) : 0.00));
						String percentOfTotalToShow = percentOfTotalFormatted.substring(1, percentOfTotalFormatted.length());
						portfolioNetWorthForReportDTO.setPercentOfTotal(percentOfTotalToShow+"%");
						portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
						//inserting pie data
						if(typeValueMapEntry.getValue() > 0 && liabilities > 0) {
							if(PortfolioReportUtil.roundOff((typeValueMapEntry.getValue()/liabilities) * 100, 2) > 5) {
								portfolioNetWorthLiabilitiesPieDataDTO = new PortfolioNetWorthLiabilitiesPieDataDTO();
								portfolioNetWorthLiabilitiesPieDataDTO.setLiabilitiesKey(typeValueMapEntry.getKey().replace("Loan", "").trim());
								portfolioNetWorthLiabilitiesPieDataDTO.setLiabilitiesValue((typeValueMapEntry.getValue() > 0 && liabilities > 0)? PortfolioReportUtil.roundOff((typeValueMapEntry.getValue()/liabilities) * 100, 2) : 0.00);
								liabilitiesPieChartDataList.add(portfolioNetWorthLiabilitiesPieDataDTO);
							}
						}
					}
				}
				
				portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
				portfolioNetWorthForReportDTO.setAssets(" Total Liabilities");
				String totalCurrentValueLiabilitiesFormatted = numberFormatter.format(((liabilities > 0)? PortfolioReportUtil.roundOff(liabilities, 0) : 0));
				String totalCurrentValueLiabilitiesToShow = totalCurrentValueLiabilitiesFormatted.substring(0, totalCurrentValueLiabilitiesFormatted.length() - 3);
				portfolioNetWorthForReportDTO.setCurrentValue(totalCurrentValueLiabilitiesToShow);
				portfolioNetWorthForReportDTO.setPercentOfTotal("100%");
				portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
				
				portfolioNetWorthForReportDTO = new PortfolioNetWorthForReportDTO();
				portfolioNetWorthForReportDTO.setAssets(" Networth");
				if(portfolioNetworthDto.getNetworthValue() != null) {
					String netWorthValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioNetworthDto.getNetworthValue(), 0));
					String netWorthValueToShow = netWorthValueFormatted.substring(0, netWorthValueFormatted.length() - 3);
					portfolioNetWorthForReportDTO.setCurrentValue(netWorthValueToShow);
				}
				portfolioNetWorthForReportDTO.setPercentOfTotal("");
				portfolioNetWorthDetails.add(portfolioNetWorthForReportDTO);
				
				Map<String, Object> parametersPortfolioNetWorth = new HashMap<>();
				parametersPortfolioNetWorth.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourcePortfolioNetWorth = new JRBeanCollectionDataSource(portfolioNetWorthDetails);
				String pathPortfolioNetWorthSR = resourceLoader.getResource("classpath:portfolioManagementReports/netWorthSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioNetWorthSR = JasperCompileManager.compileReport(pathPortfolioNetWorthSR);
				parametersPortfolioNetWorth.put("jasperReportPortfolioNetWorthSR", jasperReportPortfolioNetWorthSR);
				parametersPortfolioNetWorth.put("portfolioNetWorthSubReportDetails", jrBeanCollectionDataSourcePortfolioNetWorth);

				JRBeanCollectionDataSource jrBeanCollectionDataSourceAssetsNetWorth = new JRBeanCollectionDataSource(assetPieChartDataList);
				String pathAssetsNetWorthSR = resourceLoader.getResource("classpath:portfolioManagementReports/assetsNetWorthSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportAssetsNetWorthSR = JasperCompileManager.compileReport(pathAssetsNetWorthSR);
				parametersPortfolioNetWorth.put("pieChartTitleAssets", "Assets");
				parametersPortfolioNetWorth.put("jasperReportAssetsNetWorthSR", jasperReportAssetsNetWorthSR);
				parametersPortfolioNetWorth.put("assetsNetWorthPieChartData", jrBeanCollectionDataSourceAssetsNetWorth);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceLiabilitiesNetWorth = new JRBeanCollectionDataSource(liabilitiesPieChartDataList);
				String pathLiabilitiesNetWorthSR = resourceLoader.getResource("classpath:portfolioManagementReports/liabilitiesNetWorthSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportLiabilitiesNetWorthSR = JasperCompileManager.compileReport(pathLiabilitiesNetWorthSR);
				parametersPortfolioNetWorth.put("pieChartTitleLiabilities", "Liabilities");
				parametersPortfolioNetWorth.put("jasperReportLiabilitiesNetWorthSR", jasperReportLiabilitiesNetWorthSR);
				parametersPortfolioNetWorth.put("liabilitiesNetWorthPieChartData", jrBeanCollectionDataSourceLiabilitiesNetWorth);
				
				String pathPortfolioNetWorth = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportNetWorth.jrxml").getURI().getPath();			
				JasperReport jasperReportPortfolioNetWorth = JasperCompileManager.compileReport(pathPortfolioNetWorth);
				JasperPrint jasperPrintPortfolioNetWorth = JasperFillManager.fillReport(jasperReportPortfolioNetWorth, parametersPortfolioNetWorth, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintPortfolioNetWorth);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/********Net Worth Page Ends********/
			
			/********Wealth Ratios & Risk Profile Page Starts********/
			try {
				portfolioRatiosDtoList = portFolioRatiosService.getclientPortfolioRatios(token, portfolioReportDTO.getClientId(), session, cacheInfoService);
				List<PortfolioWealthRatiosForReportDTO> portfolioWealthRatiosForReportDTOList = new ArrayList<PortfolioWealthRatiosForReportDTO>();
				for(PortfolioRatiosDto portfolioRatiosDto : portfolioRatiosDtoList) {
					PortfolioWealthRatiosForReportDTO portfolioWealthRatiosForReportDTO = new PortfolioWealthRatiosForReportDTO();
					portfolioWealthRatiosForReportDTO.setWealthRatioName(portfolioRatiosDto.getRatios());
					portfolioWealthRatiosForReportDTO.setWealthRatioLogicRationale(portfolioRatiosDto.getLogicRational());
					portfolioWealthRatiosForReportDTO.setWealthRatio(PortfolioReportUtil.roundOff(portfolioRatiosDto.getValue() * 100, 2));
					portfolioWealthRatiosForReportDTO.setWealthRatioComment(portfolioRatiosDto.getCommentMaster());
					portfolioWealthRatiosForReportDTOList.add(portfolioWealthRatiosForReportDTO);
				}
				
				Map<String, Object> parametersPortfolioWealthRatiosAndRiskProfile = new HashMap<>();
				parametersPortfolioWealthRatiosAndRiskProfile.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceWealthRatios = new JRBeanCollectionDataSource(portfolioWealthRatiosForReportDTOList);
				String pathWealthRatiosSR = resourceLoader.getResource("classpath:portfolioManagementReports/wealthRatiosSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportWealthRatiosSR = JasperCompileManager.compileReport(pathWealthRatiosSR);
				parametersPortfolioWealthRatiosAndRiskProfile.put("jasperReportWealthRatiosSR", jasperReportWealthRatiosSR);
				parametersPortfolioWealthRatiosAndRiskProfile.put("wealthRatiosDetails", jrBeanCollectionDataSourceWealthRatios);
				
				riskOutput = portFolioRatiosService.getRiskOutput(token, portfolioReportDTO.getClientId(), session);
				if(riskOutput.getRiskProfile() > 0 && riskOutput.getRiskProfile() <= 3.4){
					parametersPortfolioWealthRatiosAndRiskProfile.put("riskProfile", FinexaConstant.RISK_PROFILE_CONSERVATIVE);
					parametersPortfolioWealthRatiosAndRiskProfile.put("riskText", FinexaConstant.RISK_PROFILE_CONSERVATIVE_TEXT);
				} else if(riskOutput.getRiskProfile() > 3.4 && riskOutput.getRiskProfile() <= 7){
					parametersPortfolioWealthRatiosAndRiskProfile.put("riskProfile", FinexaConstant.RISK_PROFILE_MODERATE);
					parametersPortfolioWealthRatiosAndRiskProfile.put("riskText", FinexaConstant.RISK_PROFILE_MODERATE_TEXT);
				} else if(riskOutput.getRiskProfile() > 7 && riskOutput.getRiskProfile() <= 10){
					parametersPortfolioWealthRatiosAndRiskProfile.put("riskProfile", FinexaConstant.RISK_PROFILE_AGGRESSIVE);
					parametersPortfolioWealthRatiosAndRiskProfile.put("riskText", FinexaConstant.RISK_PROFILE_AGGRESSIVE_TEXT);
				}
				
				String pathPortfolioWealthRatiosAndRiskProfile = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportWealthRatiosAndRiskProfile.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioWealthRatiosAndRiskProfile = JasperCompileManager.compileReport(pathPortfolioWealthRatiosAndRiskProfile);
				JasperPrint jasperPrintWealthRatiosAndRiskProfile = JasperFillManager.fillReport(jasperReportPortfolioWealthRatiosAndRiskProfile, parametersPortfolioWealthRatiosAndRiskProfile, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintWealthRatiosAndRiskProfile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/********Wealth Ratios & Risk Profile Page Starts********/
			
			/********Current Asset Allocation Page Starts********/
			double totalCurrentAllo = 0.0d;
			double totalRecoAllo = 0.0d;
			try {
				List<PortfolioCurrentAssetAllocationForReportDTO> portfolioCurrentAssetAllocationForReportDTOList = new ArrayList<PortfolioCurrentAssetAllocationForReportDTO>();
				List<PortfolioCurrentAssetAllocationPieDataDTO> portfolioCurrentAssetAllocationPieDataDTOList = new ArrayList<PortfolioCurrentAssetAllocationPieDataDTO>();
				List<PortfolioCurrentSubAssetAllocationPieDataDTO> portfolioCurrentSubAssetAllocationPieDataDTOList = new ArrayList<PortfolioCurrentSubAssetAllocationPieDataDTO>();
				portfolioRecommendedAssetAllocationForReportDTOList = new ArrayList<PortfolioRecommendedAssetAllocationForReportDTO>();
				portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTOList = new ArrayList<PortfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO>();
				portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTOList = new ArrayList<PortfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO>();
				portfolioAssetAllocationReviewDto = portFolioAssetAllocationReviewService.getclientPortfolioSubAssetAllocationReview(token, portfolioReportDTO.getClientId(), session, cacheInfoService);
				Double totalCagrOfClass = 0.0d;
				
				//Map<String, List<PortfolioAssetAllocationReviewDto>> sortedPortfolioAssetListMap = new TreeMap<String, List<PortfolioAssetAllocationReviewDto>>(portfolioAssetAllocationReviewDto.getPortfolioAssetListMap());
				
				for(Map.Entry<String, List<PortfolioAssetAllocationReviewDto>> portfolioAssetListMapEntry : portfolioAssetAllocationReviewDto.getPortfolioAssetListMap().entrySet()) {
					double currentValOfClass = 0.0d;
					double investmentValOfClass = 0.0d;
					double gainLossOfClass = 0.0d;
					Double cagrOfClass = 0.0d;
					double currentAlloClass = 0.0d;
					double recoAlloClass = 0.0d;
					
					for(PortfolioAssetAllocationReviewDto dto : portfolioAssetListMapEntry.getValue()) {
						currentValOfClass = currentValOfClass + (dto.getCurrentValue() == null ? 0 : dto.getCurrentValue());
						investmentValOfClass = investmentValOfClass + (dto.getInvestmentValue() == null ? 0 : dto.getInvestmentValue());
						gainLossOfClass = gainLossOfClass + (dto.getProfitLoss() == null ? 0 : dto.getProfitLoss());
						currentAlloClass = currentAlloClass + (dto.getPortFoliototalPercentage() == null ? 0 : dto.getPortFoliototalPercentage());
						recoAlloClass = recoAlloClass + (dto.getRecomentTotalPercentage() == null ? 0 : dto.getRecomentTotalPercentage());
					}
					
					for(PortfolioAssetAllocationReviewDto dto : portfolioAssetListMapEntry.getValue()) {
						if(dto.getCurrentValue() != null) {
							double percentOfTotal = PortfolioReportUtil.roundOff(dto.getCurrentValue()/currentValOfClass, 2);
							if (dto.getCagr_xirr() != null && !dto.getCagr_xirr().isNaN()) {
								cagrOfClass = cagrOfClass + (dto.getCagr_xirr() * percentOfTotal);
							}
						}
					}
					
					totalCagrOfClass = totalCagrOfClass + cagrOfClass;
					
					if (currentValOfClass > 0) {
						portfolioCurrentAssetAllocationForReportDTO = new PortfolioCurrentAssetAllocationForReportDTO();
						if(portfolioAssetListMapEntry.getKey().equals("FixedIncome")) {
							portfolioCurrentAssetAllocationForReportDTO.setSubAssetClass("Fixed Income Total");
						} else {
							portfolioCurrentAssetAllocationForReportDTO.setSubAssetClass(portfolioAssetListMapEntry.getKey() + " Total");
						}
						String currentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(currentValOfClass, 0));
						String currentValueToShow = currentValueFormatted.substring(0, currentValueFormatted.length() - 3);
						portfolioCurrentAssetAllocationForReportDTO.setCurrentValue(currentValueToShow);
						String investmentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(investmentValOfClass, 0));
						String investmentValueToShow = investmentValueFormatted.substring(0, investmentValueFormatted.length() - 3);
						portfolioCurrentAssetAllocationForReportDTO.setInvestmentValue(investmentValueToShow);
						String gainLossFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(gainLossOfClass, 0));
						String gainLossToShow = gainLossFormatted.substring(0, gainLossFormatted.length() - 3);
						portfolioCurrentAssetAllocationForReportDTO.setGainLoss(gainLossToShow);
						if(!cagrOfClass.isNaN()) {
							String cagrXirrFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(cagrOfClass, 2));
							String cagrXirrToShow = cagrXirrFormatted.substring(1, cagrXirrFormatted.length());
							portfolioCurrentAssetAllocationForReportDTO.setCagrXirr(cagrXirrToShow);
						} else {
							portfolioCurrentAssetAllocationForReportDTO.setCagrXirr("0");
						}
						portfolioCurrentAssetAllocationForReportDTOList.add(portfolioCurrentAssetAllocationForReportDTO);
						
						//inserting pie data
						if(PortfolioReportUtil.roundOff(currentValOfClass/portfolioAssetAllocationReviewDto.getCurrentValue(), 2) > 0) {
							PortfolioCurrentAssetAllocationPieDataDTO portfolioCurrentAssetAllocationPieDataDTO = new PortfolioCurrentAssetAllocationPieDataDTO();
							if(portfolioAssetListMapEntry.getKey().equals("FixedIncome")) {
								portfolioCurrentAssetAllocationPieDataDTO.setAssetAllocationKey("Fixed Income");
							} else {
								portfolioCurrentAssetAllocationPieDataDTO.setAssetAllocationKey(portfolioAssetListMapEntry.getKey());
							}
							portfolioCurrentAssetAllocationPieDataDTO.setAssetAllocationValue(PortfolioReportUtil.roundOff(currentValOfClass/portfolioAssetAllocationReviewDto.getCurrentValue(), 2));
							portfolioCurrentAssetAllocationPieDataDTOList.add(portfolioCurrentAssetAllocationPieDataDTO);
						}
						
						//recommended asset allocation
						portfolioRecommendedAssetAllocationForReportDTO = new PortfolioRecommendedAssetAllocationForReportDTO();
						if(portfolioAssetListMapEntry.getKey().equals("FixedIncome")) {
							portfolioRecommendedAssetAllocationForReportDTO.setSubAssetClass("Fixed Income Total");
						} else {
							portfolioRecommendedAssetAllocationForReportDTO.setSubAssetClass(portfolioAssetListMapEntry.getKey() + " Total");
						}
						String currentAllocationFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(currentAlloClass, 2));
						String currentAllocationToShow = currentAllocationFormatted.substring(1, currentAllocationFormatted.length());
						portfolioRecommendedAssetAllocationForReportDTO.setCurrentAllocation(currentAllocationToShow);
						String recommendedAllocationFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(recoAlloClass, 2));
						String recommendedAllocationToShow = recommendedAllocationFormatted.substring(1, recommendedAllocationFormatted.length());
						portfolioRecommendedAssetAllocationForReportDTO.setRecommendedAllocation(recommendedAllocationToShow);
						portfolioRecommendedAssetAllocationForReportDTOList.add(portfolioRecommendedAssetAllocationForReportDTO);
						
						totalCurrentAllo = totalCurrentAllo + currentAlloClass;
						totalRecoAllo = totalRecoAllo + recoAlloClass;
						for(PortfolioAssetAllocationReviewDto dto : portfolioAssetListMapEntry.getValue()) {
							if(dto.getCurrentValue() != null && dto.getCurrentValue() > 0) {
								portfolioCurrentAssetAllocationForReportDTO = new PortfolioCurrentAssetAllocationForReportDTO();
								portfolioCurrentAssetAllocationForReportDTO.setSubAssetClass(dto.getInvestmentSubAssetClass());
								String currentValueInvestmentSubAssetClassFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getCurrentValue(), 0));
								String currentValueInvestmentSubAssetClassToShow = currentValueInvestmentSubAssetClassFormatted.substring(0, currentValueInvestmentSubAssetClassFormatted.length() - 3);
								portfolioCurrentAssetAllocationForReportDTO.setCurrentValue(currentValueInvestmentSubAssetClassToShow);
								String investmentValueInvestmentSubAssetClassFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getInvestmentValue(), 0));
								String investmentValueInvestmentSubAssetClassToShow = investmentValueInvestmentSubAssetClassFormatted.substring(0, investmentValueInvestmentSubAssetClassFormatted.length() - 3);
								portfolioCurrentAssetAllocationForReportDTO.setInvestmentValue(investmentValueInvestmentSubAssetClassToShow);
								String profitLossInvestmentSubAssetClassFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getProfitLoss(), 0));
								String profitLossInvestmentSubAssetClassToShow = profitLossInvestmentSubAssetClassFormatted.substring(0, profitLossInvestmentSubAssetClassFormatted.length() - 3);
								portfolioCurrentAssetAllocationForReportDTO.setGainLoss(profitLossInvestmentSubAssetClassToShow);
								String cagrXirrInvestmentSubAssetClassFormatted = numberFormatter.format(PortfolioReportUtil.roundOff((dto.getCagr_xirr() == null ? 0.00 : dto.getCagr_xirr()), 2));
								String cagrXirrInvestmentSubAssetClassToShow = cagrXirrInvestmentSubAssetClassFormatted.substring(1, cagrXirrInvestmentSubAssetClassFormatted.length());
								portfolioCurrentAssetAllocationForReportDTO.setCagrXirr(cagrXirrInvestmentSubAssetClassToShow);
								portfolioCurrentAssetAllocationForReportDTOList.add(portfolioCurrentAssetAllocationForReportDTO);
								//inserting pie data
								if(PortfolioReportUtil.roundOff(dto.getCurrentValue()/portfolioAssetAllocationReviewDto.getCurrentValue(), 2) > 0) {
									PortfolioCurrentSubAssetAllocationPieDataDTO portfolioCurrentSubAssetAllocationPieDataDTO = new PortfolioCurrentSubAssetAllocationPieDataDTO();
									portfolioCurrentSubAssetAllocationPieDataDTO.setSubAssetAllocationKey(dto.getInvestmentSubAssetClass());
									portfolioCurrentSubAssetAllocationPieDataDTO.setSubAssetAllocationValue(PortfolioReportUtil.roundOff(dto.getCurrentValue()/portfolioAssetAllocationReviewDto.getCurrentValue(), 2));
									portfolioCurrentSubAssetAllocationPieDataDTOList.add(portfolioCurrentSubAssetAllocationPieDataDTO);
								}
							}
							
							//recommended asset allocation
							portfolioRecommendedAssetAllocationForReportDTO = new PortfolioRecommendedAssetAllocationForReportDTO();
							portfolioRecommendedAssetAllocationForReportDTO.setSubAssetClass(dto.getInvestmentSubAssetClass());
							String currentAllocationInvestmentSubAssetFormatted = numberFormatter.format((dto.getPortFoliototalPercentage() == null ? 0 : PortfolioReportUtil.roundOff(dto.getPortFoliototalPercentage(), 2)));
							String currentAllocationInvestmentSubAssetToShow = currentAllocationInvestmentSubAssetFormatted.substring(1, currentAllocationInvestmentSubAssetFormatted.length());
							portfolioRecommendedAssetAllocationForReportDTO.setCurrentAllocation(currentAllocationInvestmentSubAssetToShow);
							String recommendedAllocationInvestmentSubAssetFormatted = numberFormatter.format((dto.getRecomentTotalPercentage() == null ? 0 : PortfolioReportUtil.roundOff(dto.getRecomentTotalPercentage(), 2)));
							String recommendedAllocationInvestmentSubAssetToShow = recommendedAllocationInvestmentSubAssetFormatted.substring(1, recommendedAllocationInvestmentSubAssetFormatted.length());
							portfolioRecommendedAssetAllocationForReportDTO.setRecommendedAllocation(recommendedAllocationInvestmentSubAssetToShow);
							portfolioRecommendedAssetAllocationForReportDTOList.add(portfolioRecommendedAssetAllocationForReportDTO);
							
							//Stacked Bar Chart
							if(dto.getPortFoliototalPercentage() > 5 || dto.getRecomentTotalPercentage() > 5) {
								portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO = new PortfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO();
								portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO.setCategory("Current Sub AA");
								portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO.setValue(PortfolioReportUtil.roundOff(dto.getPortFoliototalPercentage(), 0));
								portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO.setKey(dto.getInvestmentSubAssetClass());
								portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTOList.add(portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO);
								if(dto.getRecomentTotalPercentage() > 5){
									portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO = new PortfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO();
									portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO.setCategory("Recommended Sub AA");
									portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO.setValue(PortfolioReportUtil.roundOff(dto.getRecomentTotalPercentage(), 0));
									portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO.setKey(dto.getInvestmentSubAssetClass());
									portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTOList.add(portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTO);
								}
							}
						}
					}
					
					double currentAlloPerc = PortfolioReportUtil.roundOff((currentValOfClass / portfolioAssetAllocationReviewDto.getCurrentValue()) * 100, 2);
					if(currentAlloPerc > 5 && recoAlloClass > 5) {
						//Stacked Bar Chart
						portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO = new PortfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO();
						if(portfolioAssetListMapEntry.getKey().equals("FixedIncome")) {
							portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO.setKey("Fixed Income");
						} else {
							portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO.setKey(portfolioAssetListMapEntry.getKey());
						}
						portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO.setValue(PortfolioReportUtil.roundOff(currentAlloPerc, 0));
						portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO.setCategory("Current AA");
						portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTOList.add(portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO);
						if(recoAlloClass > 5) {
							portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO = new PortfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO();
							portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO.setCategory("Recommended AA");
							if(portfolioAssetListMapEntry.getKey().equals("FixedIncome")) {
								portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO.setKey("Fixed Income");
							} else {
								portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO.setKey(portfolioAssetListMapEntry.getKey());
							}
							portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO.setValue(PortfolioReportUtil.roundOff(recoAlloClass, 0));
							portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTOList.add(portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTO);
						}
					}
					
					
				}
				
				portfolioCurrentAssetAllocationForReportDTO = new PortfolioCurrentAssetAllocationForReportDTO();
				portfolioCurrentAssetAllocationForReportDTO.setSubAssetClass("Total");
				String currentValueTotalFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioAssetAllocationReviewDto.getCurrentValue(), 0));
				String currentValueTotalToShow = currentValueTotalFormatted.substring(0, currentValueTotalFormatted.length() - 3);
				portfolioCurrentAssetAllocationForReportDTO.setCurrentValue(currentValueTotalToShow);
				String investmentValueTotalFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioAssetAllocationReviewDto.getInvestmentValue(), 0));
				String investmentValueTotalToShow = investmentValueTotalFormatted.substring(0, investmentValueTotalFormatted.length() - 3);
				portfolioCurrentAssetAllocationForReportDTO.setInvestmentValue(investmentValueTotalToShow);
				String gainLossTotalFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(portfolioAssetAllocationReviewDto.getProfitLoss(), 0));
				String gainLossTotalToShow = gainLossTotalFormatted.substring(0, gainLossTotalFormatted.length() - 3);
				portfolioCurrentAssetAllocationForReportDTO.setGainLoss(gainLossTotalToShow);
				if(!totalCagrOfClass.isNaN()) {
					String cagrXirrTotalFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalCagrOfClass, 2));
					String cagrXirrTotalToShow = cagrXirrTotalFormatted.substring(1, cagrXirrTotalFormatted.length());
					portfolioCurrentAssetAllocationForReportDTO.setCagrXirr(cagrXirrTotalToShow + "%");
				}
				portfolioCurrentAssetAllocationForReportDTOList.add(portfolioCurrentAssetAllocationForReportDTO);
				
				Map<String, Object> parametersPortfolioCurrentAssetAllocation = new HashMap<>();
				parametersPortfolioCurrentAssetAllocation.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceCurrentAssetAllocation = new JRBeanCollectionDataSource(portfolioCurrentAssetAllocationForReportDTOList);
				String pathCurrentAssetAllocationSR = resourceLoader.getResource("classpath:portfolioManagementReports/currentAssetAllocationSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportCurrentAssetAllocationSR = JasperCompileManager.compileReport(pathCurrentAssetAllocationSR);
				parametersPortfolioCurrentAssetAllocation.put("jasperReportCurrentAssetAllocationSR", jasperReportCurrentAssetAllocationSR);
				parametersPortfolioCurrentAssetAllocation.put("currentAssetAllocationSRDetails", jrBeanCollectionDataSourceCurrentAssetAllocation);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceCurrentAssetAllocationPie = new JRBeanCollectionDataSource(portfolioCurrentAssetAllocationPieDataDTOList);
				String pathCurrentAssetAllocationPieSR = resourceLoader.getResource("classpath:portfolioManagementReports/currentAssetAllocationPieSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportCurrentAssetAllocationPieSR = JasperCompileManager.compileReport(pathCurrentAssetAllocationPieSR);
				parametersPortfolioCurrentAssetAllocation.put("pieChartTitleAssetAllocation", FinexaConstant.CURRENT_AA_PIE_TITLE);
				parametersPortfolioCurrentAssetAllocation.put("jasperReportCurrentAssetAllocationPieSR", jasperReportCurrentAssetAllocationPieSR);
				parametersPortfolioCurrentAssetAllocation.put("currentAssetAllocationPieSRDetails", jrBeanCollectionDataSourceCurrentAssetAllocationPie);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceCurrentSubAssetAllocationPie = new JRBeanCollectionDataSource(portfolioCurrentSubAssetAllocationPieDataDTOList);
				String pathCurrentSubAssetAllocationPieSR = resourceLoader.getResource("classpath:portfolioManagementReports/currentSubAssetAllocationPieSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportCurrentSubAssetAllocationPieSR = JasperCompileManager.compileReport(pathCurrentSubAssetAllocationPieSR);
				parametersPortfolioCurrentAssetAllocation.put("pieChartTitleSubAssetAllocation", FinexaConstant.CURRENT_SUB_AA_PIE_TITLE);
				parametersPortfolioCurrentAssetAllocation.put("jasperReportCurrentSubAssetAllocationPieSR", jasperReportCurrentSubAssetAllocationPieSR);
				parametersPortfolioCurrentAssetAllocation.put("currentSubAssetAllocationPieSRDetails", jrBeanCollectionDataSourceCurrentSubAssetAllocationPie);
				
				String pathPortfolioCurrentAssetAllocation = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportCurrentAssetAllocation.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioCurrentAssetAllocation = JasperCompileManager.compileReport(pathPortfolioCurrentAssetAllocation);
				JasperPrint jasperPrintCurrentAssetAllocation = JasperFillManager.fillReport(jasperReportPortfolioCurrentAssetAllocation, parametersPortfolioCurrentAssetAllocation, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintCurrentAssetAllocation);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/********Current Asset Allocation Page Ends********/
			
			/********Recommended Asset Allocation Page Starts********/
			try {
				portfolioAssetAllocationReviewDtoList = portFolioAssetAllocationReviewService.getclientPortfolioAssetAllocationReview(token, portfolioReportDTO.getClientId(), session, cacheInfoService);
				
				portfolioRecommendedAssetAllocationForReportDTO = new PortfolioRecommendedAssetAllocationForReportDTO();
				portfolioRecommendedAssetAllocationForReportDTO.setSubAssetClass("Total");
				String totalCurrentAllocationFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalCurrentAllo, 2));
				String totalCurrentAllocationToShow = totalCurrentAllocationFormatted.substring(1, totalCurrentAllocationFormatted.length());
				portfolioRecommendedAssetAllocationForReportDTO.setCurrentAllocation(totalCurrentAllocationToShow);
				String totalRecommendedAllocationFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalRecoAllo, 2));
				String totalRecommendedAllocationToShow = totalRecommendedAllocationFormatted.substring(1, totalRecommendedAllocationFormatted.length());
				portfolioRecommendedAssetAllocationForReportDTO.setRecommendedAllocation(totalRecommendedAllocationToShow);
				portfolioRecommendedAssetAllocationForReportDTOList.add(portfolioRecommendedAssetAllocationForReportDTO);
				
				portfolioRecommendedAssetAllocationForReportDTO = new PortfolioRecommendedAssetAllocationForReportDTO();
				portfolioRecommendedAssetAllocationForReportDTO.setSubAssetClass("");
				portfolioRecommendedAssetAllocationForReportDTO.setCurrentAllocation(FinexaConstant.CURRENT_ALLOCATION);
				portfolioRecommendedAssetAllocationForReportDTO.setRecommendedAllocation(FinexaConstant.RECOMMENDED_ALLOCATION);
				portfolioRecommendedAssetAllocationForReportDTOList.add(portfolioRecommendedAssetAllocationForReportDTO);
				
				for(PortfolioAssetAllocationReviewDto dto : portfolioAssetAllocationReviewDtoList) {
					if(dto.getInvestmentAssetClass().equals("Total")) {
						if(dto.getCagr_xirr() != null && !dto.getCagr_xirr().isNaN()) {
							portfolioRecommendedAssetAllocationForReportDTO = new PortfolioRecommendedAssetAllocationForReportDTO();
							portfolioRecommendedAssetAllocationForReportDTO.setSubAssetClass("Expected Portfolio Return (%)");
							String expectedPortfolioReturnCurrentAllocationFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getCagr_xirr(), 2));
							String expectedPortfolioReturnCurrentAllocationToShow = expectedPortfolioReturnCurrentAllocationFormatted.substring(1, expectedPortfolioReturnCurrentAllocationFormatted.length());
							portfolioRecommendedAssetAllocationForReportDTO.setCurrentAllocation(expectedPortfolioReturnCurrentAllocationToShow);
							String expectedPortfolioReturnRecommendedAllocationFormatted = numberFormatter.format((dto.getTotalExpectedRecommed() == null ? 0 : PortfolioReportUtil.roundOff(dto.getTotalExpectedRecommed(), 2)));
							String expectedPortfolioReturnRecommendedAllocationToShow = expectedPortfolioReturnRecommendedAllocationFormatted.substring(1, expectedPortfolioReturnRecommendedAllocationFormatted.length());
							portfolioRecommendedAssetAllocationForReportDTO.setRecommendedAllocation(expectedPortfolioReturnRecommendedAllocationToShow);
							portfolioRecommendedAssetAllocationForReportDTOList.add(portfolioRecommendedAssetAllocationForReportDTO);
						}
						
						portfolioRecommendedAssetAllocationForReportDTO = new PortfolioRecommendedAssetAllocationForReportDTO();
						portfolioRecommendedAssetAllocationForReportDTO.setSubAssetClass("Expected Portfolio Risk (%)");
						String expectedPortfolioRiskCurrentAllocationFormatted = numberFormatter.format((dto.getTotalRiskExpectedCurrent() == null ? 0 : PortfolioReportUtil.roundOff(dto.getTotalRiskExpectedCurrent(), 2)));
						String expectedPortfolioRiskCurrentAllocationToShow = expectedPortfolioRiskCurrentAllocationFormatted.substring(1, expectedPortfolioRiskCurrentAllocationFormatted.length());
						portfolioRecommendedAssetAllocationForReportDTO.setCurrentAllocation(expectedPortfolioRiskCurrentAllocationToShow);
						String expectedPortfolioRiskRecommendedAllocationFormatted = numberFormatter.format((dto.getTotalRiskExpectedRecommed() == null ? 0 : PortfolioReportUtil.roundOff(dto.getTotalRiskExpectedRecommed(), 2)));
						String expectedPortfolioRiskRecommendedAllocationToShow = expectedPortfolioRiskRecommendedAllocationFormatted.substring(1, expectedPortfolioRiskRecommendedAllocationFormatted.length());
						portfolioRecommendedAssetAllocationForReportDTO.setRecommendedAllocation(expectedPortfolioRiskRecommendedAllocationToShow);
						portfolioRecommendedAssetAllocationForReportDTOList.add(portfolioRecommendedAssetAllocationForReportDTO);
					}
				}
				
				Map<String, Object> parametersPortfolioRecommendedAssetAllocation = new HashMap<>();
				parametersPortfolioRecommendedAssetAllocation.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceRecommendedAssetAllocation = new JRBeanCollectionDataSource(portfolioRecommendedAssetAllocationForReportDTOList);
				String pathRecommendedAssetAllocationSR = resourceLoader.getResource("classpath:portfolioManagementReports/recommendedAssetAllocationSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportRecommendedAssetAllocationSR = JasperCompileManager.compileReport(pathRecommendedAssetAllocationSR);
				parametersPortfolioRecommendedAssetAllocation.put("jasperReportRecommendedAssetAllocationSR", jasperReportRecommendedAssetAllocationSR);
				parametersPortfolioRecommendedAssetAllocation.put("recommendedAssetAllocationSRDetails", jrBeanCollectionDataSourceRecommendedAssetAllocation);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceCurrentVsRecommendedSR = new JRBeanCollectionDataSource(portfolioCurrentVsRecommendedAssetAllocationStackedBarDataDTOList);
				String pathCurrentVsRecommendedSR = resourceLoader.getResource("classpath:portfolioManagementReports/currentVsRecommendedAssetAllocationSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportCurrentVsRecommendedSR = JasperCompileManager.compileReport(pathCurrentVsRecommendedSR);
				parametersPortfolioRecommendedAssetAllocation.put("currentVsRecommendedAssetAllocationSubReportTitle", "Current v/s Recommended - AA");
				parametersPortfolioRecommendedAssetAllocation.put("jasperReportCurrentVsRecommendedSR", jasperReportCurrentVsRecommendedSR);
				parametersPortfolioRecommendedAssetAllocation.put("currentVsRecommendedAssetAllocationSRDetails", jrBeanCollectionDataSourceCurrentVsRecommendedSR);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceCurrentVsRecommendedSubAssetSR = new JRBeanCollectionDataSource(portfolioCurrentVsRecommendedSubAssetAllocationStackedBarDataDTOList);
				String pathCurrentVsRecommendedSubAssetSR = resourceLoader.getResource("classpath:portfolioManagementReports/currentVsRecommendedSubAssetAllocationSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportCurrentVsRecommendedSubAssetSR = JasperCompileManager.compileReport(pathCurrentVsRecommendedSubAssetSR);
				parametersPortfolioRecommendedAssetAllocation.put("currentVsRecommendedSubAssetAllocationSubReportTitle", "Current v/s Recommended - Sub AA");
				parametersPortfolioRecommendedAssetAllocation.put("jasperReportCurrentVsRecommendedSubAssetSR", jasperReportCurrentVsRecommendedSubAssetSR);
				parametersPortfolioRecommendedAssetAllocation.put("currentVsRecommendedSubAssetAllocationSRDetails", jrBeanCollectionDataSourceCurrentVsRecommendedSubAssetSR);
				
				String pathPortfolioRecommendedAssetAllocation = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportRecommendedAssetAllocation.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioRecommendedAssetAllocation = JasperCompileManager.compileReport(pathPortfolioRecommendedAssetAllocation);
				JasperPrint jasperPrintPortfolioRecommendedAssetAllocation = JasperFillManager.fillReport(jasperReportPortfolioRecommendedAssetAllocation, parametersPortfolioRecommendedAssetAllocation, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintPortfolioRecommendedAssetAllocation);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/********Recommended Asset Allocation Page Ends********/
			
			/********Portfolio Overview Equity Page Starts********/
			try {
				portfolioOvervieEquityDto = portFolioOverviewService.getclientPortfolioOverviewEquity(token, portfolioReportDTO.getClientId(), session, cacheInfoService);
				portfolioOverviewEquityTableDataForReportDTOList = new ArrayList<PortfolioOverviewEquityTableDataForReportDTO>();
				portfolioOverviewEquityByProductTypePieDataDTOList = new ArrayList<PortfolioOverviewEquityByProductTypePieDataDTO>();
				portfolioOverviewEquityByMarketTypePieDataDTOList = new ArrayList<PortfolioOverviewEquityByMarketTypePieDataDTO>();
				portfolioOverviewBySectorsBarDataDTOList = new ArrayList<PortfolioOverviewBySectorsBarDataDTO>();
				double currentValSum = 0.0d, investmentValSum = 0.0d, gainLossSum = 0.0d, cagrSum = 0.0d, ptpSum = 0.0d, totalCurrentVal2 = 0.0d, 
						equitySum = 0.0d, internationalSum = 0.0d, mfSum = 0.0d, equityLarge = 0.0d, equityMid = 0.0d, equitySmall = 0.0d, others = 0.0d,
						directEquityPerc = 0.0d, mfPerc = 0.0d, interPerc = 0.0d;
				if(portfolioOvervieEquityDto.getPortfolioOverviewList().size() > 0) {
					for(PortfolioOverviewDto dto : portfolioOvervieEquityDto.getPortfolioOverviewList()) {
						totalCurrentVal2 = totalCurrentVal2 + dto.getCurrentValue();
					}
					
					for(PortfolioOverviewDto dto : portfolioOvervieEquityDto.getPortfolioOverviewList()) {
						portfolioOverviewEquityTableDataForReportDTO = new PortfolioOverviewEquityTableDataForReportDTO();
						String productName =  (dto == null ? dto.getProductName() : dto.getBankIssuerName());
						String productType = (dto == null ? dto.getProductType() : dto.getProductName());
						if((dto.getProductName().equals("Stocks") && dto.getIsin() != null) || (dto.getProductName().equals("ESOPs") && dto.getIsin() != null)) {
							String fullNameOfProduct = (dto.getBankIssuerName() == null ? dto.getProductName() : (dto.getBankIssuerName()));
							portfolioOverviewEquityTableDataForReportDTO.setProduct(fullNameOfProduct);
						} else if (dto.getProductType().equals("MF / ETF / PMS")) {
							if(dto.getProductName().equals("Mutual Funds")) {
								productName = dto.getProductDescLong() == null ? dto.getProductName() : dto.getProductDescLong();
							}
							String fullNameOfProduct = (dto.getBankIssuerName() == null ? dto.getProductName() : (dto.getBankIssuerName()));
							portfolioOverviewEquityTableDataForReportDTO.setProduct(fullNameOfProduct);
						} else {
							portfolioOverviewEquityTableDataForReportDTO.setProduct(productName);
						}
						
						portfolioOverviewEquityTableDataForReportDTO.setProductType(productType);
						String currentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getCurrentValue(), 0));
						String currentValueToShow = currentValueFormatted.substring(0, currentValueFormatted.length() - 3);
						portfolioOverviewEquityTableDataForReportDTO.setCurrentValue(currentValueToShow);
						String investmentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getInvestmentValue(), 0));
						String investmentValueToShow = investmentValueFormatted.substring(0, investmentValueFormatted.length() - 3);
						portfolioOverviewEquityTableDataForReportDTO.setInvestmentValue(investmentValueToShow);
						String gainLossFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getGains(), 0));
						String gainLossToShow = gainLossFormatted.substring(0, gainLossFormatted.length() - 3);
						portfolioOverviewEquityTableDataForReportDTO.setGainLoss(gainLossToShow);
						portfolioOverviewEquityTableDataForReportDTO.setCagrXirr((dto.getCagr().equals("N/A") ? "N/A" : String.valueOf(PortfolioReportUtil.roundOff(Double.parseDouble(dto.getCagr()), 2))));
						portfolioOverviewEquityTableDataForReportDTO.setPtpReturns((dto.getPtpReturns().equals("N/A") ? "N/A" : String.valueOf(PortfolioReportUtil.roundOff(Double.parseDouble(dto.getPtpReturns()), 2))));
						portfolioOverviewEquityTableDataForReportDTO.setLockedInUptoDate((dto.getLockedInDate() == null ? "N/A" : dto.getLockedInDate()));
						portfolioOverviewEquityTableDataForReportDTOList.add(portfolioOverviewEquityTableDataForReportDTO);
						currentValSum = currentValSum + dto.getCurrentValue();
						investmentValSum = investmentValSum + dto.getInvestmentValue();
						gainLossSum = gainLossSum + dto.getGains();
						double percentOfTotal = PortfolioReportUtil.roundOff(dto.getCurrentValue()/totalCurrentVal2, 2);
						if (!dto.getCagr().equalsIgnoreCase("nan")) {
							cagrSum = cagrSum + (Double.parseDouble(dto.getCagr()) * percentOfTotal);
						}
						
						if (!dto.getPtpReturns().equalsIgnoreCase("nan")) {
							ptpSum = ptpSum + (Double.parseDouble(dto.getPtpReturns()) * percentOfTotal);
						}
						
						if (dto.getProductType().equals("Direct Equity")) {
							equitySum = equitySum + dto.getCurrentValue();
						}
						
						if (dto.getProductType().equals("International Equity")) {
							internationalSum = internationalSum + dto.getCurrentValue();
						}
						
						if (dto.getProductType().equals("MF / ETF / PMS")) {
							mfSum = mfSum + dto.getCurrentValue();
							if (dto.getLargeCapMFPerc() != null) {
								equityLarge = equityLarge + (dto.getCurrentValue() * (dto.getLargeCapMFPerc()/100));
							}
							if (dto.getMidCapMFPerc() != null) {
								equityMid = equityMid + (dto.getCurrentValue() *  (dto.getMidCapMFPerc()/100));
							}
							if (dto.getSmallcapMFPerc() != null) {
								equitySmall = equitySmall + (dto.getCurrentValue() *  (dto.getSmallcapMFPerc()/100));
							} 
							if (dto.getOtherCapMFPerc() != null) {
								others = others + (dto.getCurrentValue() *  (dto.getOtherCapMFPerc()/100));
							}
						} else {
							if (dto.getMarketCapId() == 1) {
								equityLarge = equityLarge  + dto.getCurrentValue() ;
							}
							if (dto.getMarketCapId() == 2) {
								equitySmall = equitySmall  + dto.getCurrentValue();
							}
							if (dto.getMarketCapId() == 3) {
								equityMid = equityMid  + dto.getCurrentValue();
							}
							if (dto.getProductType().equals("International Equity")) {
								others = others + dto.getCurrentValue();
							}
						}
					}
					
					portfolioOverviewEquityTableDataForReportDTO = new PortfolioOverviewEquityTableDataForReportDTO();
					portfolioOverviewEquityTableDataForReportDTO.setProduct("Total");
					portfolioOverviewEquityTableDataForReportDTO.setProductType("");
					String currentValueSumFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(currentValSum, 0));
					String currentValueSumToShow = currentValueSumFormatted.substring(0, currentValueSumFormatted.length() - 3);
					portfolioOverviewEquityTableDataForReportDTO.setCurrentValue(currentValueSumToShow);
					String investmentValueSumFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(investmentValSum, 0));
					String investmentValueSumToShow = investmentValueSumFormatted.substring(0, investmentValueSumFormatted.length() - 3);
					portfolioOverviewEquityTableDataForReportDTO.setInvestmentValue(investmentValueSumToShow);
					String gainLossSumFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(gainLossSum, 0));
					String gainLossSumToShow = gainLossSumFormatted.substring(0, gainLossSumFormatted.length() - 3);
					portfolioOverviewEquityTableDataForReportDTO.setGainLoss(gainLossSumToShow);
					String cagrXirrSumFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(cagrSum, 0));
					String cagrXirrSumToShow = cagrXirrSumFormatted.substring(0, cagrXirrSumFormatted.length() - 3);
					portfolioOverviewEquityTableDataForReportDTO.setCagrXirr(cagrXirrSumToShow);
					String ptpReturnsSumFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(ptpSum, 0)) ;
					String ptpReturnsSumToShow = ptpReturnsSumFormatted.substring(0, ptpReturnsSumFormatted.length() - 3);
					portfolioOverviewEquityTableDataForReportDTO.setPtpReturns(ptpReturnsSumToShow);
					portfolioOverviewEquityTableDataForReportDTO.setLockedInUptoDate("");
					portfolioOverviewEquityTableDataForReportDTOList.add(portfolioOverviewEquityTableDataForReportDTO);
					
					directEquityPerc = (equitySum / currentValSum) * 100;
					mfPerc = (mfSum / currentValSum) * 100;
					interPerc = (internationalSum  / currentValSum) * 100;
					
					if(directEquityPerc > 5) {
						portfolioOverviewEquityByProductTypePieDataDTO = new PortfolioOverviewEquityByProductTypePieDataDTO();
						portfolioOverviewEquityByProductTypePieDataDTO.setKey("Direct Equity");
						portfolioOverviewEquityByProductTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(directEquityPerc, 0));
						portfolioOverviewEquityByProductTypePieDataDTOList.add(portfolioOverviewEquityByProductTypePieDataDTO);
					}
					
					if (interPerc > 5) {
						portfolioOverviewEquityByProductTypePieDataDTO = new PortfolioOverviewEquityByProductTypePieDataDTO();
						portfolioOverviewEquityByProductTypePieDataDTO.setKey("International Equity");
						portfolioOverviewEquityByProductTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(interPerc, 0));
						portfolioOverviewEquityByProductTypePieDataDTOList.add(portfolioOverviewEquityByProductTypePieDataDTO);
					}
					
					if (mfPerc > 5) {
						portfolioOverviewEquityByProductTypePieDataDTO = new PortfolioOverviewEquityByProductTypePieDataDTO();
						portfolioOverviewEquityByProductTypePieDataDTO.setKey("Mutul Fund / ETF / PMS");
						portfolioOverviewEquityByProductTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(mfPerc, 0));
						portfolioOverviewEquityByProductTypePieDataDTOList.add(portfolioOverviewEquityByProductTypePieDataDTO);
					}
					
					double equityLargePer = (equityLarge / currentValSum) * 100;
					double equitySmallPer = (equitySmall / currentValSum) * 100;
					double equityMidPer = (equityMid / currentValSum) * 100;
					double equityOthersPer = (others / currentValSum) * 100;

					if (equityLargePer > 5) {
						portfolioOverviewEquityByMarketTypePieDataDTO = new PortfolioOverviewEquityByMarketTypePieDataDTO();
						portfolioOverviewEquityByMarketTypePieDataDTO.setKey("Large Cap");
						portfolioOverviewEquityByMarketTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(equityLargePer, 0));
						portfolioOverviewEquityByMarketTypePieDataDTOList.add(portfolioOverviewEquityByMarketTypePieDataDTO);
					}

					if (equitySmallPer > 5) {
						portfolioOverviewEquityByMarketTypePieDataDTO = new PortfolioOverviewEquityByMarketTypePieDataDTO();
						portfolioOverviewEquityByMarketTypePieDataDTO.setKey("Small Cap");
						portfolioOverviewEquityByMarketTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(equitySmallPer, 0));
						portfolioOverviewEquityByMarketTypePieDataDTOList.add(portfolioOverviewEquityByMarketTypePieDataDTO);
					}

					if (equityMidPer > 5) {
						portfolioOverviewEquityByMarketTypePieDataDTO = new PortfolioOverviewEquityByMarketTypePieDataDTO();
						portfolioOverviewEquityByMarketTypePieDataDTO.setKey("Mid Cap");
						portfolioOverviewEquityByMarketTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(equityMidPer, 0));
						portfolioOverviewEquityByMarketTypePieDataDTOList.add(portfolioOverviewEquityByMarketTypePieDataDTO);
					}

					if (equityOthersPer > 5) {
						portfolioOverviewEquityByMarketTypePieDataDTO = new PortfolioOverviewEquityByMarketTypePieDataDTO();
						portfolioOverviewEquityByMarketTypePieDataDTO.setKey("Others");
						portfolioOverviewEquityByMarketTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(equityOthersPer, 0));
						portfolioOverviewEquityByMarketTypePieDataDTOList.add(portfolioOverviewEquityByMarketTypePieDataDTO);
					}
					
					for(PortfolioOverviewEquitySectorDto portfolioOverviewEquitySectorDto : portfolioOvervieEquityDto.getPortfolioOverviewEquitySectorDtoList()) {
						if(portfolioOverviewEquitySectorDto.getExposureInPortfolio() != 0) {
							if(portfolioOverviewBySectorsBarDataDTOList.size() < 10) {
								portfolioOverviewBySectorsBarDataDTO = new PortfolioOverviewBySectorsBarDataDTO();
								portfolioOverviewBySectorsBarDataDTO.setCategory(portfolioOverviewEquitySectorDto.getSectorName());
								portfolioOverviewBySectorsBarDataDTO.setValue(PortfolioReportUtil.roundOff(portfolioOverviewEquitySectorDto.getExposureInPortfolio(), 1));
								portfolioOverviewBySectorsBarDataDTOList.add(portfolioOverviewBySectorsBarDataDTO);
							}
						}
					}
					
				}
				
				Map<String, Object> parametersPortfolioOverviewEquityFistPage = new HashMap<>();
				parametersPortfolioOverviewEquityFistPage.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourcePortfolioOverviewEquityTableData = new JRBeanCollectionDataSource(portfolioOverviewEquityTableDataForReportDTOList);
				String pathPortfolioOverviewEquityTableSR = resourceLoader.getResource("classpath:portfolioManagementReports/potfolioOverviewEquityTableSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioOverviewEquityTableSR = JasperCompileManager.compileReport(pathPortfolioOverviewEquityTableSR);
				parametersPortfolioOverviewEquityFistPage.put("jasperReportPortfolioOverviewEquityTableSR", jasperReportPortfolioOverviewEquityTableSR);
				parametersPortfolioOverviewEquityFistPage.put("portfolioOverviewEquityTableData", jrBeanCollectionDataSourcePortfolioOverviewEquityTableData);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceEquityOverviewByProductType = new JRBeanCollectionDataSource(portfolioOverviewEquityByProductTypePieDataDTOList);
				String pathEquityOverviewByProductTypeSR = resourceLoader.getResource("classpath:portfolioManagementReports/equityOverviewByProductTypeSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportEquityOverviewByProductTypeSR = JasperCompileManager.compileReport(pathEquityOverviewByProductTypeSR);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceEquityOverviewByMarketType = new JRBeanCollectionDataSource(portfolioOverviewEquityByMarketTypePieDataDTOList);
				String pathEquityOverviewByMarketTypeSR = resourceLoader.getResource("classpath:portfolioManagementReports/equityOverviewByMarketTypeSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportEquityOverviewByMarketTypeSR = JasperCompileManager.compileReport(pathEquityOverviewByMarketTypeSR);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceOverviewBySectors = new JRBeanCollectionDataSource(portfolioOverviewBySectorsBarDataDTOList);
				String pathOverviewBySectorsSR = resourceLoader.getResource("classpath:portfolioManagementReports/overviewBySectorsSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportOverviewBySectorsSR = JasperCompileManager.compileReport(pathOverviewBySectorsSR);
				
				//if(portfolioOverviewEquityTableDataForReportDTOList.size() > 3) {
					String pathPortfolioOverviewEquityFirstPage = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportPortfolioOverviewEquityFirstPage.jrxml").getURI().getPath();
					JasperReport jasperReportPortfolioOverviewEquityFirstPage = JasperCompileManager.compileReport(pathPortfolioOverviewEquityFirstPage);
					JasperPrint jasperPrintPortfolioOverviewEquityFirstPage = JasperFillManager.fillReport(jasperReportPortfolioOverviewEquityFirstPage, parametersPortfolioOverviewEquityFistPage, new JREmptyDataSource());
					jasperPrintList.add(jasperPrintPortfolioOverviewEquityFirstPage);
					
					Map<String, Object> parametersPortfolioOverviewEquitySecondPage = new HashMap<>();
					parametersPortfolioOverviewEquitySecondPage.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
					parametersPortfolioOverviewEquitySecondPage.put("pieChartTitleEquityOverviewByProductType", "Equity Overview by Product Type");
					parametersPortfolioOverviewEquitySecondPage.put("jasperReportEquityOverviewByProductTypeSR", jasperReportEquityOverviewByProductTypeSR);
					parametersPortfolioOverviewEquitySecondPage.put("equityOverviewByProductTypeDetails", jrBeanCollectionDataSourceEquityOverviewByProductType);
					parametersPortfolioOverviewEquitySecondPage.put("pieChartTitleEquityOverviewByMarketType", "Equity Overview by Market Type");
					parametersPortfolioOverviewEquitySecondPage.put("jasperReportEquityOverviewByMarketTypeSR", jasperReportEquityOverviewByMarketTypeSR);
					parametersPortfolioOverviewEquitySecondPage.put("equityOverviewByMarketTypeDetails", jrBeanCollectionDataSourceEquityOverviewByMarketType);
					parametersPortfolioOverviewEquitySecondPage.put("overviewBySectorsBarTitle", "Overview By Sectors");
					parametersPortfolioOverviewEquitySecondPage.put("jasperReportOverviewBySectorsSR", jasperReportOverviewBySectorsSR);
					parametersPortfolioOverviewEquitySecondPage.put("overviewBySectorsBarDetails", jrBeanCollectionDataSourceOverviewBySectors);
					
					String pathPortfolioOverviewEquitySecondPage = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportPortfolioOverviewEquitySecondPage.jrxml").getURI().getPath();
					JasperReport jasperReportPortfolioOverviewEquitySecondPage = JasperCompileManager.compileReport(pathPortfolioOverviewEquitySecondPage);
					JasperPrint jasperPrintPortfolioOverviewEquitySecondPage = JasperFillManager.fillReport(jasperReportPortfolioOverviewEquitySecondPage, parametersPortfolioOverviewEquitySecondPage, new JREmptyDataSource());
					jasperPrintList.add(jasperPrintPortfolioOverviewEquitySecondPage);
				/*} else {
					Map<String, Object> parametersPortfolioOverviewEquityCompactPage = new HashMap<>();
					parametersPortfolioOverviewEquityCompactPage.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
					parametersPortfolioOverviewEquityCompactPage.put("jasperReportPortfolioOverviewEquityTableSR", jasperReportPortfolioOverviewEquityTableSR);
					parametersPortfolioOverviewEquityCompactPage.put("portfolioOverviewEquityTableData", jrBeanCollectionDataSourcePortfolioOverviewEquityTableData);
					parametersPortfolioOverviewEquityCompactPage.put("pieChartTitleEquityOverviewByProductType", "Equity Overview by Product Type");
					parametersPortfolioOverviewEquityCompactPage.put("jasperReportEquityOverviewByProductTypeSR", jasperReportEquityOverviewByProductTypeSR);
					parametersPortfolioOverviewEquityCompactPage.put("equityOverviewByProductTypeDetails", jrBeanCollectionDataSourceEquityOverviewByProductType);
					parametersPortfolioOverviewEquityCompactPage.put("pieChartTitleEquityOverviewByMarketType", "Equity Overview by Market Type");
					parametersPortfolioOverviewEquityCompactPage.put("jasperReportEquityOverviewByMarketTypeSR", jasperReportEquityOverviewByMarketTypeSR);
					parametersPortfolioOverviewEquityCompactPage.put("equityOverviewByMarketTypeDetails", jrBeanCollectionDataSourceEquityOverviewByMarketType);
					parametersPortfolioOverviewEquityCompactPage.put("overviewBySectorsBarTitle", "Overview By Sectors");
					parametersPortfolioOverviewEquityCompactPage.put("jasperReportOverviewBySectorsSR", jasperReportOverviewBySectorsSR);
					parametersPortfolioOverviewEquityCompactPage.put("overviewBySectorsBarDetails", jrBeanCollectionDataSourceOverviewBySectors);
					
					String pathPortfolioOverviewEquityCompact = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportPortfolioOverviewEquityCompact.jrxml").getURI().getPath();
					JasperReport jasperReportPortfolioOverviewEquityCompactPage = JasperCompileManager.compileReport(pathPortfolioOverviewEquityCompact);
					JasperPrint jasperPrintPortfolioOverviewEquityCompactPage = JasperFillManager.fillReport(jasperReportPortfolioOverviewEquityCompactPage, parametersPortfolioOverviewEquityCompactPage, new JREmptyDataSource());
					jasperPrintList.add(jasperPrintPortfolioOverviewEquityCompactPage);
				}*/
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/********Portfolio Overview Equity Page Ends********/
			
			/********Portfolio Overview Debt Page Starts********/
			try {
				portfolioOvervieEquityDto = portFolioOverviewService.getclientPortfolioOverviewFixedIncome(token, portfolioReportDTO.getClientId(), session, cacheInfoService);
				portfolioOverviewDebtTableDataForReportDTOList = new ArrayList<PortfolioOverviewDebtTableDataForReportDTO>();
				portfolioOverviewDebtByMarketTypePieDataDTOList = new ArrayList<PortfolioOverviewDebtByMarketTypePieDataDTO>();
				portfolioOverviewDebtByAssetQualityPieDataDTOList = new ArrayList<PortfolioOverviewDebtByAssetQualityPieDataDTO>();
				portfolioOverviewByAssetMaturityBarDataDTOList = new ArrayList<PortfolioOverviewByAssetMaturityBarDataDTO>();
				double totalCurrentVal3 = 0.0d, currentValSum = 0.0d, investmentValSum = 0.0d, gainLossSum = 0.0d, cagrSum = 0.0d,
						rosSum = 0.0d, bondDepositSum = 0.0d, smallSavingsSum = 0.0d, mutualFundSum = 0.0d, rosPer = 0.0d, bondPer = 0.0d,
						smallPer = 0.0d, mfPer = 0.0d, percentOfCurrentTotal = 0.0d, share = 0.0d, m0to1 = 0.0d, m1to3 = 0.0d, m3to6 = 0.0d,
						m6to9 = 0.0d, m9to12 = 0.0d, y1to2 = 0.0d, y2to3 = 0.0d, y3to5 = 0.0d, y5to7 = 0.0d, y7to10 = 0.0d, y10to15 = 0.0d,
						y15Above = 0.0d, otherCategory = 0.0d, months = 0.0d, assetMaturitySum = 0.0d;
				if(portfolioOvervieEquityDto.getPortfolioOverviewList().size() > 0) {
					for(PortfolioOverviewDto dto : portfolioOvervieEquityDto.getPortfolioOverviewList()) {
						totalCurrentVal3 = totalCurrentVal3 + dto.getCurrentValue();
					}
					
					for(PortfolioOverviewDto dto : portfolioOvervieEquityDto.getPortfolioOverviewList()) {
						portfolioOverviewDebtTableDataForReportDTO = new PortfolioOverviewDebtTableDataForReportDTO();
						String name = dto.getBankIssuerName() == null ? dto.getProductName() : dto.getBankIssuerName(); 
						String type = dto.getBankIssuerName() == null ? dto.getProductType() : dto.getProductName();
						String prodName = dto.getBankIssuerName() == null ? dto.getProductName() : (dto.getBankIssuerName() + "-" + dto.getProductName());
						
						System.out.println("name: " + name);
						
						if(dto.getProductType().equals("MF / ETF / PMS")) {
							if(!dto.getLockedInDate().equals("N/A")){
								portfolioOverviewDebtTableDataForReportDTO.setLockedInUptoDate(dto.getLockedInDate());
							} else {
								portfolioOverviewDebtTableDataForReportDTO.setLockedInUptoDate(dto.getLockedInDate());
							}
							
							if(dto.getProductName().equals("Mutual Funds")){
								 name = dto.getProductDescLong() == null ? dto.getProductName() : dto.getProductDescLong(); 
							}
							
							String fullNameOfProduct = (dto.getBankIssuerName() == null ? dto.getProductName() : (dto.getBankIssuerName()));
							portfolioOverviewDebtTableDataForReportDTO.setProduct(fullNameOfProduct);
						} else {
							portfolioOverviewDebtTableDataForReportDTO.setProduct(name);
							portfolioOverviewDebtTableDataForReportDTO.setLockedInUptoDate(dto.getLockedInDate());
						}
						
						if(dto.getProductId() == 12 || dto.getProductId() == 13 || dto.getProductId() == 14 || dto.getProductId() == 33){
							portfolioOverviewDebtTableDataForReportDTO.setInvestmentValue("N/A");
					    }else{
					    	String investmentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getInvestmentValue(), 0));
					    	String investmentValueToShow = investmentValueFormatted.substring(0, investmentValueFormatted.length() - 3);
					    	portfolioOverviewDebtTableDataForReportDTO.setInvestmentValue(investmentValueToShow);
					    }
						
						if(dto.getProductId() == 12 || dto.getProductId() == 13 || dto.getProductId() == 14 || dto.getProductId() == 33){
							portfolioOverviewDebtTableDataForReportDTO.setGainLoss("N/A");
					    }else{
					    	String gainLossFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getGains(), 0));
					    	String gainLossToShow = gainLossFormatted.substring(0, gainLossFormatted.length() - 3);
					    	portfolioOverviewDebtTableDataForReportDTO.setGainLoss(gainLossToShow);
					    }
						
						portfolioOverviewDebtTableDataForReportDTO.setProductType(type);
						String currentValueFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getCurrentValue(), 0));
						String currentValueToShow = currentValueFormatted.substring(0, currentValueFormatted.length() - 3);
						portfolioOverviewDebtTableDataForReportDTO.setCurrentValue(currentValueToShow);
						portfolioOverviewDebtTableDataForReportDTO.setReturnType(dto.getMarketLinkOrFixedReturn());
						if(dto.getCagr() != null) {
							if(!dto.getCagr().equals("N/A")) {
								String cagrXirrFormatted = String.valueOf(PortfolioReportUtil.roundOff(Double.parseDouble(dto.getCagr()), 2));
								portfolioOverviewDebtTableDataForReportDTO.setCagrXirr(cagrXirrFormatted);
							}
						} else {
							portfolioOverviewDebtTableDataForReportDTO.setCagrXirr("N/A");
						}
						
						if(dto.getMaturityAmount() == 0.00) {
							portfolioOverviewDebtTableDataForReportDTO.setMaturityAmount("N/A"); 
						} else {
							portfolioOverviewDebtTableDataForReportDTO.setMaturityDate((dto.getMaturityDate() == null ? "N/A": dto.getMaturityDate()));
							String maturityAmountFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getMaturityAmount(), 0));
							String maturityAmountToShow = maturityAmountFormatted.substring(0, maturityAmountFormatted.length() - 3);
							portfolioOverviewDebtTableDataForReportDTO.setMaturityAmount(maturityAmountToShow); 
						}
						
						portfolioOverviewDebtTableDataForReportDTOList.add(portfolioOverviewDebtTableDataForReportDTO);
						
						currentValSum = currentValSum + dto.getCurrentValue();
						investmentValSum = investmentValSum + dto.getInvestmentValue();
						gainLossSum = gainLossSum + dto.getGains();
						
						double percentOfTotal = PortfolioReportUtil.roundOff(dto.getCurrentValue()/totalCurrentVal3, 2);
						
						if(!dto.getCagr().equals("N/A")) {
							cagrSum = cagrSum + (PortfolioReportUtil.roundOff(Double.parseDouble(dto.getCagr()), 2) * percentOfTotal);
						}
						
						if (dto.getProductType().equals("Retirement Oriented Schemes")) {
							rosSum = rosSum + dto.getCurrentValue();
						}
						if (dto.getProductType().equals("Deposit/Bonds")) {
							bondDepositSum = bondDepositSum + dto.getCurrentValue();
						}
						if (dto.getProductType().equals("Small Saving Schemes")) {
							smallSavingsSum = smallSavingsSum + dto.getCurrentValue();
						}
						if (dto.getProductType().equals("MF / ETF / PMS")) {
							mutualFundSum = mutualFundSum + dto.getCurrentValue();
						}
						
						//overview by asset maturity
						percentOfCurrentTotal = PortfolioReportUtil.roundOff((dto.getCurrentValue()/totalCurrentVal3) * 100, 2);
						if(!dto.getProductType().equals("MF / ETF / PMS")) {
							if(dto.getTimeToMaturity() != null) {
								months = Double.parseDouble(dto.getTimeToMaturity()) * 12;
							} else {
								months = 1;
							}
							share = percentOfCurrentTotal;
							if(months <= 12) {
								if(months > 0 && months <= 1) {
									m0to1 = m0to1 + share;
								}
								if (months > 1 && months <= 3) {
									m1to3 = m1to3 + share;
								}
								if (months > 3 && months <= 6) {
									m3to6 = m3to6 + share;
								}
								if (months > 6 && months <= 9) {
									m6to9 = m6to9 + share;
								}
								if (months > 9 && months <= 12) {
									m9to12 = m9to12 + share;
								}
							} else {
								if(Double.parseDouble(dto.getTimeToMaturity()) > 1 && Double.parseDouble(dto.getTimeToMaturity()) <= 2) {
									y1to2 = y1to2 + share;
								}
								if(Double.parseDouble(dto.getTimeToMaturity()) > 2 && Double.parseDouble(dto.getTimeToMaturity()) <= 3) {
									y2to3 = y2to3 + share;
								}
								if(Double.parseDouble(dto.getTimeToMaturity()) > 3 && Double.parseDouble(dto.getTimeToMaturity()) <= 5) {
									y3to5 = y3to5 + share;
								}
								if(Double.parseDouble(dto.getTimeToMaturity()) > 5 && Double.parseDouble(dto.getTimeToMaturity()) <= 7) {
									y5to7 = y5to7 + share;
								}
								if(Double.parseDouble(dto.getTimeToMaturity()) > 7 && Double.parseDouble(dto.getTimeToMaturity()) <= 10) {
									y7to10 = y7to10 + share;
								}
								if(Double.parseDouble(dto.getTimeToMaturity()) > 10 && Double.parseDouble(dto.getTimeToMaturity()) <= 15) {
									y10to15 = y10to15 + share;
								}
								if(Double.parseDouble(dto.getTimeToMaturity()) > 15) {
									y15Above = y15Above + share;
								}
							}
						} else {
							if(dto.getProductName().equals("PMS") || dto.getProductName().equals("ETF")) {
								share = percentOfCurrentTotal;
								m0to1 = m0to1 + share;
							}
							
							for(Map.Entry<String, Double> mfMaturityProfileEntry : dto.getMfMaturityProfile().entrySet()) {
								share = (mfMaturityProfileEntry.getValue() * percentOfCurrentTotal)/100;
								System.out.println("mfMaturityProfileEntry key: " + mfMaturityProfileEntry.getKey());
								if(mfMaturityProfileEntry.getKey().equals("0-1M")) {
									m0to1 = m0to1 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("1-3M")) {
									m1to3 = m1to3 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("3-6M")) {
									m3to6 = m3to6 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("6-9M")) {
									m6to9 = m6to9 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("9-12M")) {
									m9to12 = m9to12 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("1-2Y")) {
									y1to2 = y1to2 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("2-3Y")) {
									y2to3 = y2to3 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("3-5Y")) {
									y3to5 = y3to5 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("5-7Y")) {
									y5to7 = y5to7 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("7-10Y")) {
									y7to10 = y7to10 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("10-15Y")) {
									y10to15 = y10to15 + share;
								} else if(mfMaturityProfileEntry.getKey().equals("15Y+")) {
									y15Above = y15Above + share;
								} else if(mfMaturityProfileEntry.getKey().equals("Others")) {
									otherCategory = otherCategory + share;
								}
							}
						}
					}
					
					portfolioOverviewDebtTableDataForReportDTO = new PortfolioOverviewDebtTableDataForReportDTO();
					portfolioOverviewDebtTableDataForReportDTO.setProduct("Total");
					portfolioOverviewDebtTableDataForReportDTO.setProductType("");
					String currentValSumFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(currentValSum, 0));
					String currentValSumToShow = currentValSumFormatted.substring(0, currentValSumFormatted.length() - 3);
					portfolioOverviewDebtTableDataForReportDTO.setCurrentValue(currentValSumToShow);
					String investmentValSumFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(investmentValSum, 0));
					String investmentValSumToShow = investmentValSumFormatted.substring(0, investmentValSumFormatted.length() - 3);
					portfolioOverviewDebtTableDataForReportDTO.setInvestmentValue(investmentValSumToShow);
					String gainLossSumFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(gainLossSum, 0));
					String gainLossSumToShow = gainLossSumFormatted.substring(0, gainLossSumFormatted.length() - 3);
					portfolioOverviewDebtTableDataForReportDTO.setGainLoss(gainLossSumToShow);
					portfolioOverviewDebtTableDataForReportDTO.setReturnType("");
					String cagrSumFormatted = String.valueOf(PortfolioReportUtil.roundOff(cagrSum, 2));
					portfolioOverviewDebtTableDataForReportDTO.setCagrXirr(cagrSumFormatted);
					portfolioOverviewDebtTableDataForReportDTO.setLockedInUptoDate("");
					portfolioOverviewDebtTableDataForReportDTO.setMaturityDate("");
					portfolioOverviewDebtTableDataForReportDTO.setMaturityAmount("");
					portfolioOverviewDebtTableDataForReportDTOList.add(portfolioOverviewDebtTableDataForReportDTO);
					
					rosPer = (rosSum/currentValSum) * 100;
					bondPer = (bondDepositSum/currentValSum) * 100;
					smallPer = (smallSavingsSum/currentValSum) * 100;
					mfPer = (mutualFundSum/currentValSum) * 100;
					
					if (rosPer > 5) {
						portfolioOverviewDebtByMarketTypePieDataDTO = new PortfolioOverviewDebtByMarketTypePieDataDTO();
						portfolioOverviewDebtByMarketTypePieDataDTO.setKey("Retirement Oriented");
						portfolioOverviewDebtByMarketTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(rosPer, 0));
						portfolioOverviewDebtByMarketTypePieDataDTOList.add(portfolioOverviewDebtByMarketTypePieDataDTO);
					}

					if (bondPer > 5) {
						portfolioOverviewDebtByMarketTypePieDataDTO = new PortfolioOverviewDebtByMarketTypePieDataDTO();
						portfolioOverviewDebtByMarketTypePieDataDTO.setKey("Deposits / Bonds");
						portfolioOverviewDebtByMarketTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(bondPer, 0));
						portfolioOverviewDebtByMarketTypePieDataDTOList.add(portfolioOverviewDebtByMarketTypePieDataDTO);
					}

					if (smallPer > 5) {
						portfolioOverviewDebtByMarketTypePieDataDTO = new PortfolioOverviewDebtByMarketTypePieDataDTO();
						portfolioOverviewDebtByMarketTypePieDataDTO.setKey("Small Savings Scheme");
						portfolioOverviewDebtByMarketTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(smallPer, 0));
						portfolioOverviewDebtByMarketTypePieDataDTOList.add(portfolioOverviewDebtByMarketTypePieDataDTO);
					}

					if (mfPer > 5) {
						portfolioOverviewDebtByMarketTypePieDataDTO = new PortfolioOverviewDebtByMarketTypePieDataDTO();
						portfolioOverviewDebtByMarketTypePieDataDTO.setKey("Mutual Funds");
						portfolioOverviewDebtByMarketTypePieDataDTO.setValue(PortfolioReportUtil.roundOff(mfPer, 0));
						portfolioOverviewDebtByMarketTypePieDataDTOList.add(portfolioOverviewDebtByMarketTypePieDataDTO);
					}
					
					if(portfolioOvervieEquityDto.getAssetQualityMap().size() > 0) {
						for(Map.Entry<String, Double> assetQualityMapEntry : portfolioOvervieEquityDto.getAssetQualityMap().entrySet()) {
							portfolioOverviewDebtByAssetQualityPieDataDTO = new PortfolioOverviewDebtByAssetQualityPieDataDTO();
							portfolioOverviewDebtByAssetQualityPieDataDTO.setKey(assetQualityMapEntry.getKey());
							portfolioOverviewDebtByAssetQualityPieDataDTO.setValue(PortfolioReportUtil.roundOff(assetQualityMapEntry.getValue(), 0));
							portfolioOverviewDebtByAssetQualityPieDataDTOList.add(portfolioOverviewDebtByAssetQualityPieDataDTO);
						}
					}
					
					assetMaturitySum = assetMaturitySum + m0to1 + m1to3 + m3to6 + m6to9 + m9to12 +
							y1to2 + y2to3 + y3to5 + y5to7 + y7to10 + y10to15 + y15Above + otherCategory;
					
					if(m0to1 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("0-1M");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(m0to1);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(m1to3 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("1-3M");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(m1to3);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(m3to6 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("3-6M");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(m3to6);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(m6to9 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("6-9M");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(m6to9);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(m9to12 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("9-12M");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(m9to12);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(y1to2 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("1-2Y");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(y1to2);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(y2to3 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("2-3Y");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(y2to3);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(y3to5 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("3-5Y");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(y3to5);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(y5to7 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("5-7Y");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(y5to7);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(y7to10 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("7-10Y");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(y7to10);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(y10to15 > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("10-15Y");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(y10to15);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(y15Above > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("15Y+");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(y15Above);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
					
					if(otherCategory > 0.09) {
						portfolioOverviewByAssetMaturityBarDataDTO = new PortfolioOverviewByAssetMaturityBarDataDTO();
						portfolioOverviewByAssetMaturityBarDataDTO.setCategory("Others");
						portfolioOverviewByAssetMaturityBarDataDTO.setValue(otherCategory);
						portfolioOverviewByAssetMaturityBarDataDTOList.add(portfolioOverviewByAssetMaturityBarDataDTO);
					}
				}
				
				Map<String, Object> parametersPortfolioOverviewDebtFistPage = new HashMap<>();
				parametersPortfolioOverviewDebtFistPage.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceOverviewDebtTableData = new JRBeanCollectionDataSource(portfolioOverviewDebtTableDataForReportDTOList);
				String pathOverviewDebtTableSR = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioOverviewDebtTableSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportOverviewDebtTableSR = JasperCompileManager.compileReport(pathOverviewDebtTableSR);
				parametersPortfolioOverviewDebtFistPage.put("jasperReportOverviewDebtTableSR", jasperReportOverviewDebtTableSR);
				parametersPortfolioOverviewDebtFistPage.put("overviewDebtTableDetails", jrBeanCollectionDataSourceOverviewDebtTableData);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceDebtOverviewByMarketType = new JRBeanCollectionDataSource(portfolioOverviewDebtByMarketTypePieDataDTOList);
				String pathDebtOverviewByMarketTypeSR = resourceLoader.getResource("classpath:portfolioManagementReports/debtOverviewByMarketTypeSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportDebtOverviewByMarketTypeSR = JasperCompileManager.compileReport(pathDebtOverviewByMarketTypeSR);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceDebtOverviewByAssetQuality = new JRBeanCollectionDataSource(portfolioOverviewDebtByAssetQualityPieDataDTOList);
				String pathDebtOverviewByAssetQualitySR = resourceLoader.getResource("classpath:portfolioManagementReports/debtOverviewByAssetQualitySubReport.jrxml").getURI().getPath();
				JasperReport jasperReportDebtOverviewByAssetQualitySR = JasperCompileManager.compileReport(pathDebtOverviewByAssetQualitySR);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceOverviewByAssetMaturity = new JRBeanCollectionDataSource(portfolioOverviewByAssetMaturityBarDataDTOList);
				String pathOverviewByAssetMaturitySR = resourceLoader.getResource("classpath:portfolioManagementReports/overviewByAssetMaturitySubReport.jrxml").getURI().getPath();
				JasperReport jasperReportOverviewByAssetMaturitySR = JasperCompileManager.compileReport(pathOverviewByAssetMaturitySR);
				
				
				String pathPortfolioOverviewDebtFirstPage = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportPortfolioOverviewDebtFirstPage.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioOverviewDebtFirstPage = JasperCompileManager.compileReport(pathPortfolioOverviewDebtFirstPage);
				JasperPrint jasperPrintPortfolioOverviewDebtFirstPage = JasperFillManager.fillReport(jasperReportPortfolioOverviewDebtFirstPage, parametersPortfolioOverviewDebtFistPage, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintPortfolioOverviewDebtFirstPage);
				
				Map<String, Object> parametersPortfolioOverviewDebtSecondPage = new HashMap<>();
				parametersPortfolioOverviewDebtSecondPage.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				parametersPortfolioOverviewDebtSecondPage.put("pieChartTitleDebtOverviewByMarketType", "Overview by Market Type");
				parametersPortfolioOverviewDebtSecondPage.put("jasperReportDebtOverviewByMarketTypeSR", jasperReportDebtOverviewByMarketTypeSR);
				parametersPortfolioOverviewDebtSecondPage.put("debtOverviewByMarketTypeDetails", jrBeanCollectionDataSourceDebtOverviewByMarketType);
				parametersPortfolioOverviewDebtSecondPage.put("pieChartTitleDebtOverviewByAssetQuality", "Overview by Asset Quality");
				parametersPortfolioOverviewDebtSecondPage.put("jasperReportDebtOverviewByAssetQualitySR", jasperReportDebtOverviewByAssetQualitySR);
				parametersPortfolioOverviewDebtSecondPage.put("debtOverviewByAssetQualityDetails", jrBeanCollectionDataSourceDebtOverviewByAssetQuality);
				parametersPortfolioOverviewDebtSecondPage.put("overviewByAssetMaturityBarTitle", "Overview by Asset Maturity");
				parametersPortfolioOverviewDebtSecondPage.put("jasperReportOverviewByAssetMaturitySR", jasperReportOverviewByAssetMaturitySR);
				parametersPortfolioOverviewDebtSecondPage.put("overviewByAssetMaturityDetails", jrBeanCollectionDataSourceOverviewByAssetMaturity);
				
				String pathPortfolioOverviewDebtSecondPage = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportPortfolioOverviewDebtSecondPage.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioOverviewDebtSecondPage = JasperCompileManager.compileReport(pathPortfolioOverviewDebtSecondPage);
				JasperPrint jasperPrintPortfolioOverviewDebtSecondPage = JasperFillManager.fillReport(jasperReportPortfolioOverviewDebtSecondPage, parametersPortfolioOverviewDebtSecondPage, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintPortfolioOverviewDebtSecondPage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/********Portfolio Overview Debt Page Ends********/
			
			/********Portfolio Recommendation Page Starts********/
			try {
				double totalPerc = 0.0d;
				portfolioRecommendationForReportDTOList = new ArrayList<PortfolioRecommendationForReportDTO>();
				portfolioAssetAllocationReviewDto = portFolioAssetAllocationReviewService.getclientPortfolioSubAssetAllocationReview(token, portfolioReportDTO.getClientId(), session, cacheInfoService);
				if(portfolioAssetAllocationReviewDto.getPortfolioAssetListMap().size() > 0) {
					for(Map.Entry<String, List<PortfolioAssetAllocationReviewDto>> portfolioAssetListMapEntry : portfolioAssetAllocationReviewDto.getPortfolioAssetListMap().entrySet()) {
						for(PortfolioAssetAllocationReviewDto dto : portfolioAssetListMapEntry.getValue()) {
							portfolioRecommendationForReportDTO = new PortfolioRecommendationForReportDTO();
							if(portfolioAssetListMapEntry.getKey().equals("FixedIncome")) {
								portfolioRecommendationForReportDTO.setAssetClass("Fixed Income");
							} else {
								portfolioRecommendationForReportDTO.setAssetClass(portfolioAssetListMapEntry.getKey());
							}
							portfolioRecommendationForReportDTO.setSubAssetClass(dto.getInvestmentSubAssetClass());
							String allocationPercentageFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getRecomentTotalPercentage(), 2));
							String allocationPercentageToShow = allocationPercentageFormatted.substring(1, allocationPercentageFormatted.length() - 3);
							portfolioRecommendationForReportDTO.setAllocationPercentage(allocationPercentageToShow);
							double rebalancingAmount = PortfolioReportUtil.roundOff(((dto.getRecomentTotalPercentage() - dto.getPortFoliototalPercentage())/100) * portfolioAssetAllocationReviewDto.getCurrentValue(), 0);
							//optional to show rebalancing amount in brackets if negative value
							if(rebalancingAmount < 0) {
								String rebalancingAmountFormatted2 = numberFormatter.format(rebalancingAmount);
								String rebalancingAmountToShow2 = "("+rebalancingAmountFormatted2.substring(1, rebalancingAmountFormatted2.length() - 3)+")";
								portfolioRecommendationForReportDTO.setRebalancingAmount(rebalancingAmountToShow2);
							} else {
								String rebalancingAmountFormatted = numberFormatter.format(rebalancingAmount);
								String rebalancingAmountToShow = rebalancingAmountFormatted.substring(0, rebalancingAmountFormatted.length() - 3);
								portfolioRecommendationForReportDTO.setRebalancingAmount(rebalancingAmountToShow);
							}
							portfolioRecommendationForReportDTOList.add(portfolioRecommendationForReportDTO);
							totalPerc = totalPerc + dto.getRecomentTotalPercentage();
						}
					}
					
					portfolioRecommendationForReportDTO = new PortfolioRecommendationForReportDTO();
					portfolioRecommendationForReportDTO.setAssetClass("Total");
					portfolioRecommendationForReportDTO.setSubAssetClass("");
					String totalPercFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(totalPerc, 2));
					String totalPercToShow = totalPercFormatted.substring(1, totalPercFormatted.length() - 3);
					portfolioRecommendationForReportDTO.setAllocationPercentage(totalPercToShow);
					portfolioRecommendationForReportDTO.setRebalancingAmount("");
					portfolioRecommendationForReportDTOList.add(portfolioRecommendationForReportDTO);
				}
				
				Map<String, Object> parametersPortfolioRecommendation = new HashMap<>();
				parametersPortfolioRecommendation.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourcePortfolioRecommendation = new JRBeanCollectionDataSource(portfolioRecommendationForReportDTOList);
				String pathPortfolioRecommendationSR = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioRecommendationTableSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioRecommendationSR = JasperCompileManager.compileReport(pathPortfolioRecommendationSR);
				parametersPortfolioRecommendation.put("jasperReportPortfolioRecommendationSR", jasperReportPortfolioRecommendationSR);
				parametersPortfolioRecommendation.put("portfolioRecommendationDetails", jrBeanCollectionDataSourcePortfolioRecommendation);
				
				String pathPortfolioRecommendation = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportPortfolioRecommendation.jrxml").getURI().getPath();
				JasperReport jasperReportPortfolioRecommendation = JasperCompileManager.compileReport(pathPortfolioRecommendation);
				JasperPrint jasperPrintPortfolioRecommendation = JasperFillManager.fillReport(jasperReportPortfolioRecommendation, parametersPortfolioRecommendation, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintPortfolioRecommendation);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/********Portfolio Recommendation Page Ends********/
			
			/********Product Recommendation Page Starts********/
			try {
				productRecommendationDTOList = getLastSavedProductRecommendation(Integer.parseInt(portfolioReportDTO.getAdvisorDTO().getId()), portfolioReportDTO.getClientId(), "PM");
				productRecommendationForReportDTOList = new ArrayList<ProductRecommendationForReportDTO>();
				if(productRecommendationDTOList != null && productRecommendationDTOList.size() > 0) {
					for(ProductRecommendationDTO dto : productRecommendationDTOList) {
						for(String s : dto.getProductName()) {
							if(dto.getProductValue()[PortfolioReportUtil.findIndexOfStringArr(dto.getProductName(), s)] > 0) {
								productRecommendationForReportDTO = new ProductRecommendationForReportDTO();
								productRecommendationForReportDTO.setSubAssetClass(dto.getSubAssetClass());
								productRecommendationForReportDTO.setRecommendedProduct(s);
								String productAllocationFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getProductValue()[PortfolioReportUtil.findIndexOfStringArr(dto.getProductName(), s)], 0));
								String productAllocationToShow = productAllocationFormatted.substring(1, productAllocationFormatted.length() - 3);
								productRecommendationForReportDTO.setProductAllocationPercentage(productAllocationToShow);
								String lumpsumAmountFormatted = numberFormatter.format(PortfolioReportUtil.roundOff(dto.getLumpsumAmt()[PortfolioReportUtil.findIndexOfStringArr(dto.getProductName(), s)], 0));
								String lumpsumAmountToShow = lumpsumAmountFormatted.substring(0, lumpsumAmountFormatted.length() - 3);
								productRecommendationForReportDTO.setLumpsumAmount(lumpsumAmountToShow);
								productRecommendationForReportDTOList.add(productRecommendationForReportDTO);
							}
						}
					}
				}
				
				Map<String, Object> parametersProductRecommendation = new HashMap<>();
				parametersProductRecommendation.put("pageHeaderCenterRightImage", pageHeaderCenterRightElement);
				
				JRBeanCollectionDataSource jrBeanCollectionDataSourceProductRecommendation = new JRBeanCollectionDataSource(productRecommendationForReportDTOList);
				String pathProductRecommendationSR = resourceLoader.getResource("classpath:portfolioManagementReports/productRecommendationTableSubReport.jrxml").getURI().getPath();
				JasperReport jasperReportProductRecommendationSR = JasperCompileManager.compileReport(pathProductRecommendationSR);
				parametersProductRecommendation.put("jasperReportProductRecommendationSR", jasperReportProductRecommendationSR);
				parametersProductRecommendation.put("productRecommendationTableDetails", jrBeanCollectionDataSourceProductRecommendation);
				
				String pathProductRecommendation = resourceLoader.getResource("classpath:portfolioManagementReports/portfolioManagementReportProductRecommendation.jrxml").getURI().getPath();
				JasperReport jasperReportProductRecommendation = JasperCompileManager.compileReport(pathProductRecommendation);
				JasperPrint jasperPrintProductRecommendation = JasperFillManager.fillReport(jasperReportProductRecommendation, parametersProductRecommendation, new JREmptyDataSource());
				jasperPrintList.add(jasperPrintProductRecommendation);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/********Product Recommendation Page Ends********/
			
			//First loop on all reports to get total page number
			int totalPageNumber=0;
			int extraBlankPageCount=0;
			for (JasperPrint jp : jasperPrintList) {
				totalPageNumber += jp.getPages().size();
				 List<JRPrintPage> pages = jp.getPages();
				 for (Iterator<JRPrintPage> i=pages.iterator(); i.hasNext();) {
			          JRPrintPage page = i.next();
			          System.out.println("page.getElements().size(): " + page.getElements().size());
			          if (page.getElements().size() == 1) {
			        	  i.remove();
			        	  extraBlankPageCount++;
			          }
			    }
			}

			System.out.println("extraBlankPageCount: " + extraBlankPageCount);
			
			//Second loop all reports to replace our markers with current and total number
			int currentPage = 1;
			int pageCount = 1;
			for (JasperPrint jp : jasperPrintList) {
			    List<JRPrintPage> pages = jp.getPages();
			    //Loop all pages of report
			    for (JRPrintPage jpp : pages) {
			        List<JRPrintElement> elements = jpp.getElements();
			        //Loop all elements on page
			        for (JRPrintElement jpe : elements) {
			            //Check if text element
			            if (jpe instanceof JRPrintText){
			                JRPrintText jpt = (JRPrintText) jpe;
			                
			                //Check if current page marker
			                if (CURRENT_PAGE_NUMBER.equals(jpt.getValue())){
			                    jpt.setText(" " + currentPage); //Replace marker
			                    continue;
			                }
			                
			                if(pageCount == 2) {
			                
			                	if(NOTES_AND_DISCLAIMER_PAGE_NUMBER.equals(jpt.getValue())) {
				                	jpt.setText(" " + (totalPageNumber-(totalPageNumber - 2)+1));
				                	continue;
				                }
			                	
			                	if(INTRODUCTION_PAGE_NUMBER.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 3)+1));
			                		continue;
			                	}
			                	
			                	if(PERSONAL_DETAILS_PAGE_NUMBER.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 4)+1));
			                		continue;
			                	}
			                	
			                	if(PORTFOLIO_TRACKER_PAGE_NUMBER.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 5)+1));
			                		continue;
			                	}
			                	
			                	if(PORTFOLIO_NETWORTH_PAGE_NUMBER.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 6)+1));
			                		continue;
			                	}
			                	
			                	if(PORTFOLIO_WEALTH_RATIOS_AND_RISK_PROFILE.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 7)+1));
			                		continue;
			                	}
			                	
			                	if(PORTFOLIO_CURRENT_ASSET_ALLOCATION.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 8)+1));
			                		continue;
			                	}
			                	
			                	if(PORTFOLIO_RECOMMENDED_ASSET_ALLOCATION.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 9)+1));
			                		continue;
			                	}
			                	
			                	if(PORTFOLIO_OVERVIEW_EQUITY.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 10)+1));
			                		continue;
			                	}
			                	
			                	/*if(portfolioOverviewEquityTableDataForReportDTOList.size() > 3) {
			                		
			                	} else {
			                		if(PORTFOLIO_OVERVIEW_DEBT.equals(jpt.getValue())) {
				                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 11)+1));
				                		continue;
				                	}
			                	}*/
			                	
			                	if(PORTFOLIO_OVERVIEW_DEBT.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 12)+1));
			                		continue;
			                	}
			                	
			                	if(PORTFOLIO_RECOMMENDATION.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 14)+1));
			                		continue;
			                	}
			                	
			                	if(PRODUCT_RECOMMENDATION.equals(jpt.getValue())) {
			                		jpt.setText(" " + (totalPageNumber-(totalPageNumber - 15)+1));
			                		continue;
			                	}
			                	
			                }
			                	                
			                //Check if total page marker
			                if (TOTAL_PAGE_NUMBER.equals(jpt.getValue())){
			                    jpt.setText(" " + totalPageNumber); //Replace marker
			                }
			                
			            }
			        }
			        currentPage++;
			        pageCount++;
			    }
			}
			
			exportReportToDocx(jasperPrintList);
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			returnStatus = "Failure";
			e.printStackTrace();
		}
		
		portfolioReportDTO.setFileName(fileName);
		return portfolioReportDTO;
	}
	
	private List<ProductRecommendationDTO> getLastSavedProductRecommendation(int advsiorId, int clientId, String module) throws FinexaBussinessException {
		// TODO Auto-generated method stub
		// =========== start Product recommendation=====================
		List<ProductRecommendationDTO> productRecommendationDTOList = null;
		try {
			Date lastSavedDate = null;
			lastSavedDate = advisorProductRecoRepository.getMaxDateOfSavedProductRecoPM(advsiorId, clientId, module);
			if (lastSavedDate != null) {
				String productPlan = advisorProductRecoRepository.getLastSavedProductPlan(advsiorId, clientId, module, lastSavedDate);
				if (productPlan != "") {
					productRecommendationDTOList = new ObjectMapper().readValue(productPlan, new TypeReference<List<ProductRecommendationDTO>>(){});
				}
			}
			return productRecommendationDTOList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productRecommendationDTOList;
	}
	
	@RequestMapping(path = "/downloadPortfolioReport", method = RequestMethod.GET)
	public ResponseEntity<?> downloadPortfolioReport(HttpServletResponse response,
			@RequestParam(value = "filename") String filename)  throws RuntimeException{

		InputStreamResource resource = null;
		HttpHeaders headers = null;
		File file = null;
		try {
			headers = new HttpHeaders();
			String tempDir = System.getProperty("java.io.tmpdir");
			String rootPathLocation = tempDir + "/" + filename;
			
			file = new File(rootPathLocation);
		  
			resource = new InputStreamResource(new FileInputStream(file));

			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");

			response.setHeader("Content-disposition", "attachment; filename=" + filename);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType
						.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
				.body(resource);
	}
	
	@RequestMapping(value = "/reportDelete", method = RequestMethod.GET)
	public ResponseEntity<?> reportDelete(HttpServletResponse response,
			@RequestParam(value = "filename") String filename) {
		PortfolioReportDTO portfolioReportDTO = new PortfolioReportDTO();
		try {
			File file = null;

			String tempDir = System.getProperty("java.io.tmpdir");
			String rootPathLocation = tempDir + "/" + filename;

			file = new File(rootPathLocation);

			if (file.exists()) {
				boolean f = file.delete();
				if (f) {
					portfolioReportDTO.setMsg("deleted");
				} else {
					portfolioReportDTO.setMsg("not deleted");
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new ResponseEntity<PortfolioReportDTO>(portfolioReportDTO, HttpStatus.OK);
	}
	
	protected byte[] exportReportToDocx(List<JasperPrint> jasperPrintList) throws JRException{
	   JRDocxExporter exporter = new JRDocxExporter();
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	   exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
	   exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "CrispPMReport_"+ reportFileNamePart + "(" + clientNameForFile + ").docx"));
	   exporter.exportReport(); 
	   return baos.toByteArray();
	}
	
	
	
}