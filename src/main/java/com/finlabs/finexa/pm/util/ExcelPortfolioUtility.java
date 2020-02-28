package com.finlabs.finexa.pm.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.finlabs.finexa.dto.PortfolioOverviewDto;
import com.finlabs.finexa.model.PortfolioNetWorthDto;



public class ExcelPortfolioUtility {
	public static XSSFWorkbook writeExcelOutputData(File file, List<PortfolioOverviewDto> portfolioOverviewDtoList)
			throws IOException {
		DecimalFormat df = new DecimalFormat("#.##");
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue("Product");
		headRow.createCell(1).setCellValue("Product Type");
		headRow.createCell(2).setCellValue("Current Value(Rs.)");
		headRow.createCell(3).setCellValue("% of Total");
		headRow.createCell(4).setCellValue("Investment value(Rs.)");
		headRow.createCell(5).setCellValue("Gain/Loss");
		headRow.createCell(6).setCellValue("CAGR/XIRR");
	
		int rownum = 2;
		
		double totalCurrentValue = 0,totalInvestVal = 0;
		for (PortfolioOverviewDto output : portfolioOverviewDtoList) {
			if (output.getInvestmentOrPersonFlag().equals("Y")) {
				totalCurrentValue = totalCurrentValue + output.getCurrentValue();
				totalInvestVal = totalInvestVal + output.getInvestmentValue();
			}
		}
		System.out.println("portfolioOverviewDtoList "+portfolioOverviewDtoList.size());
		for (PortfolioOverviewDto output : portfolioOverviewDtoList) {
			/*XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getProductName());
			row.createCell(1).setCellValue(output.getProductType());
			row.createCell(2).setCellValue(ExcelPortfolioUtility.getRowValue(output.getCurrentValue()));
			row.createCell(3).setCellValue(df.format((output.getCurrentPortfolioWeight()) * 100));
			if(output.getProductId()==17 || output.getProductId()==13 ) {
			row.createCell(4).setCellValue(output.getInvestmentValueEshopEPF());	
			}else {
		       row.createCell(4).setCellValue(ExcelPortfolioUtility.getRowValue((output.getInvestmentValue())));
			}
			if(output.getProductId()==12 || output.getProductId()==13 || output.getProductId()==14 || output.getProductId()==33) {
			row.createCell(5).setCellValue("N/A");
			}else {
			row.createCell(5).setCellValue(ExcelPortfolioUtility.getRowValue(output.getGains()));	
			}
			if(!output.getCagr().equals("N/A")) {
				row.createCell(6).setCellValue(ExcelPortfolioUtility.getRowValue(Double.parseDouble(output.getCagr())));
			}else {
				row.createCell(6).setCellValue(output.getCagr());
			}*/
			
		
			//rownum++;
			XSSFRow row = sheet.createRow(rownum);
			String name = "",type = "",percOfTotal = "";
			double currentValue = 0, perc = 0;
			System.out.println("output.getInvestmentOrPersonFlag() "+output.getInvestmentOrPerson());
			if (output.getInvestmentOrPersonFlag().equals("Y")) {
				System.out.println("========================= ");
				name = output.getBankIssuerName() == null ? output.getProductName() : output.getBankIssuerName();
				//new
				System.out.println("output.getProductName() "+output.getProductName());
				if(output.getProductName().equals("Mutual Funds")){
				name = output.getProductDescLong() == null ? output.getProductName() : output.getProductDescLong(); 
				}
				System.out.println("name "+name);
				
				type = output.getBankIssuerName() == null ? output.getProductType() :  output.getProductName();
				System.out.println("type "+type);
				row.createCell(0).setCellValue(name);
				row.createCell(1).setCellValue(type);			
				row.createCell(2).setCellValue(ExcelPortfolioUtility.getRowValue(output.getCurrentValue()));
                perc = output.getCurrentValue()/totalCurrentValue;
                perc = perc * 100;
                percOfTotal = ExcelPortfolioUtility.getRowValue(perc);
				row.createCell(3).setCellValue(percOfTotal);
				
				if(output.getProductId() == 12 || output.getProductId() == 13 || output.getProductId() == 14 || output.getProductId() == 33){
				 row.createCell(4).setCellValue("N/A");
				}else {
				 row.createCell(4).setCellValue(ExcelPortfolioUtility.getRowValue((output.getInvestmentValue())));
				}
				
				if(output.getProductId() == 12 || output.getProductId() == 13 || output.getProductId() == 14 || output.getProductId() == 33){
				row.createCell(5).setCellValue("N/A");
				}else {
				row.createCell(5).setCellValue(ExcelPortfolioUtility.getRowValue(output.getGains()));	
				}
				
				if(!output.getCagr().equals("N/A")) {
					row.createCell(6).setCellValue(ExcelPortfolioUtility.getRowValue(Double.parseDouble(output.getCagr())));
				}else {
					row.createCell(6).setCellValue(output.getCagr());
				}
				
			}
			rownum++;
		}

		fis.close();
		return workbook;
	} 
	
	public static XSSFWorkbook writeExcelOutputDataAsset(File file, List<PortfolioOverviewDto> portfolioOverviewDtoList)
			throws IOException {
		DecimalFormat df = new DecimalFormat("#");    
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue("Product");
		headRow.createCell(1).setCellValue("Provider Name");
		headRow.createCell(2).setCellValue("Maturity / Renewal date");
		headRow.createCell(3).setCellValue("Maturity amount");
	
	
		int rownum = 2;
		for (PortfolioOverviewDto output : portfolioOverviewDtoList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getProductName());
			row.createCell(1).setCellValue(output.getBankIssuerName());
			row.createCell(2).setCellValue(output.getMaturityDate());
			row.createCell(3).setCellValue(ExcelPortfolioUtility.getRowValue(output.getMaturityAmount()));
			
		
			rownum++;
		}

		fis.close();
		return workbook;
	}
	/*public static XSSFWorkbook writeExcelOutputLoanData(File file, List<ClientFamilyLoanOutput> clientFamilyLoanList)
			throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue(" Year");
		headRow.createCell(1).setCellValue(" Beginning Balance");
		headRow.createCell(2).setCellValue(" Interest Payment");
		headRow.createCell(3).setCellValue(" Principal Payment");
		headRow.createCell(4).setCellValue(" Ending Balance");
		headRow.createCell(5).setCellValue(" EMI Amount");
		// headRow.createCell(6).setCellValue(" Total Principal Paid to Date");
		// headRow.createCell(7).setCellValue(" Total Interest Paid to Date");
		int rownum = 2;
		for (ClientFamilyLoanOutput output : clientFamilyLoanList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getProjectionYear());
			row.createCell(1).setCellValue(output.getBegningBal());
			row.createCell(2).setCellValue(output.getInterestPay());
			row.createCell(3).setCellValue(output.getPrincipalPay());
			row.createCell(4).setCellValue(output.getEndBal());
			row.createCell(5).setCellValue(output.getEmiAmount());
			// row.createCell(6).setCellValue(output.getTotalPrincipalPaid());
			// row.createCell(7).setCellValue(output.getTotalInterestPaid());
			rownum++;
		}

		fis.close();
		return workbook;
	}

	public static XSSFWorkbook writeExcelOutputExpenseData(File file,
			List<AnnualExpensesDetailed> clientAnnualExpenseDetailsList) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue(" Year");
		headRow.createCell(1).setCellValue(" Living Expenses");
		headRow.createCell(2).setCellValue(" Discretionary Expenses");
		headRow.createCell(3).setCellValue(" Total Expenses");
		int rownum = 2;
		for (AnnualExpensesDetailed output : clientAnnualExpenseDetailsList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getFinYear());
			row.createCell(1).setCellValue(output.getLivingExpense());
			row.createCell(2).setCellValue(output.getDiscretionaryExpense());
			row.createCell(3).setCellValue(output.getTotalExpense());
			rownum++;
		}

		fis.close();
		return workbook;
	}

	public static XSSFWorkbook writeExcelOutputExpenseDataDetailed(File file,
			List<AnnualExpensesDetailed> clientAnnualExpenseDetailsList) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue(" Year");
		headRow.createCell(1).setCellValue(" Groceries");
		headRow.createCell(2).setCellValue(" Utilities (Electricity, Gas etc.)");
		headRow.createCell(3).setCellValue(" Transport (Petrol, Driver, Vehicle maintenance, Local conveyance etc.)");
		headRow.createCell(4).setCellValue(" Household & Personal Care");
		headRow.createCell(5).setCellValue(" Housing & Maintenance (Rent, Flat maintenance, Household maid etc.)");
		headRow.createCell(6).setCellValue(" Communication (Mobile, Telephone, Internet etc.)");
		headRow.createCell(7).setCellValue(" Lifestyle & Entertainment (Dining out, Movies, Vacation, Cable etc.)");
		headRow.createCell(8).setCellValue(" Apparels & Accessories");
		headRow.createCell(9).setCellValue(" Children Fees (School, tution, books etc.)");
		headRow.createCell(10).setCellValue(" Healthcare Expenses (Doctor fees, medicines, path lab fees etc.)");
		headRow.createCell(11).setCellValue(" Others (Charity, etc)");
		headRow.createCell(12).setCellValue(" Living Expenses");
		headRow.createCell(14).setCellValue(" Total Expenses");
		int rownum = 2;
		for (AnnualExpensesDetailed output : clientAnnualExpenseDetailsList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getFinYear());
			row.createCell(1).setCellValue(output.getGroceries_amt());
			row.createCell(2).setCellValue(output.getUtilities_amt());
			row.createCell(3).setCellValue(output.getTransport_amt());
			row.createCell(4).setCellValue(output.getHouseHoldPersonal_amt());
			row.createCell(5).setCellValue(output.getHousing_amt());
			row.createCell(6).setCellValue(output.getCommunication_amt());
			row.createCell(7).setCellValue(output.getLifeStyle_amt());
			row.createCell(8).setCellValue(output.getApparels_amt());
			row.createCell(9).setCellValue(output.getChildrenFees_amt());
			row.createCell(10).setCellValue(output.getHealthCare_amt());
			row.createCell(11).setCellValue(output.getOthers_amt());
			row.createCell(12).setCellValue(output.getTotalFamilyExpense());
			row.createCell(14).setCellValue(output.getTotalExpense());
			rownum++;
		}

		fis.close();
		return workbook;
	}

	public static XSSFWorkbook writeExcelOutputCommitedOutFlowData(File file,
			List<CommittedOutFlowOutput> commitedOutFlowList) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue(" Year");
		headRow.createCell(1).setCellValue(" Life Insurance Premium ");
		headRow.createCell(2).setCellValue(" Health Insurance Premium");
		headRow.createCell(3).setCellValue(" General Insurance Premium");
		headRow.createCell(4).setCellValue(" Investments");
		headRow.createCell(5).setCellValue(" Total");

		int rownum = 2;
		for (CommittedOutFlowOutput output : commitedOutFlowList) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getProjectionYear());
			row.createCell(1).setCellValue(output.getPremiumAMount());
			row.createCell(2).setCellValue(output.getPremiumAmountHealth());
			row.createCell(3).setCellValue(output.getPremiumAmountGeneral());
			row.createCell(4).setCellValue(output.getInvestmentAmount());
			row.createCell(5).setCellValue(output.getTotalOutFlow());
			rownum++;
		}

		fis.close();
		return workbook;
	}

	public static XSSFWorkbook writeExcelNetSurplusOutputData(File file,
			List<ClientFamilyNetSurplusOutput> clientFamilyNetSurplusOutput) throws IOException {

		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.createRow(1);
		headRow.createCell(0).setCellValue("Year");
		headRow.createCell(1).setCellValue("Income");
		headRow.createCell(2).setCellValue("Expense");
		headRow.createCell(3).setCellValue("Committed Outflows");
		headRow.createCell(4).setCellValue("Loan Outflow");
		headRow.createCell(5).setCellValue("Net Surplus");
		int rownum = 2;
		for (ClientFamilyNetSurplusOutput output : clientFamilyNetSurplusOutput) {
			XSSFRow row = sheet.createRow(rownum);
			row.createCell(0).setCellValue(output.getFinYear());
			row.createCell(1).setCellValue(output.getIncome());
			row.createCell(2).setCellValue(output.getExpense());
			row.createCell(3).setCellValue(output.getCommitted_outflows());
			row.createCell(4).setCellValue(output.getLoan_outflows());
			row.createCell(5).setCellValue(output.getNet_surplus());
			rownum++;
		}

		fis.close();
		return workbook;
	}*/

	public static XSSFWorkbook writeExcelOutputDataAssetForAdvisor(File file,
			List<PortfolioOverviewDto> portfolioOverviewDtoList) {
		DecimalFormat df = new DecimalFormat("#");    
		FileInputStream fis = null;
		XSSFWorkbook workbook = null;
		
		try {
			fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow headRow = sheet.createRow(1);
			headRow.createCell(0).setCellValue("Client Name");
			headRow.createCell(1).setCellValue("Product Type");
			headRow.createCell(2).setCellValue("Product");
			headRow.createCell(3).setCellValue("Provider");
			headRow.createCell(4).setCellValue("Current Value");
			headRow.createCell(5).setCellValue("Maturity Value");
			headRow.createCell(6).setCellValue("Maturity Date");


			int rownum = 2;
			for (PortfolioOverviewDto output : portfolioOverviewDtoList) {
				String strDate=output.getMaturityDate();
				DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

				XSSFRow row = sheet.createRow(rownum);
				row.createCell(0).setCellValue(output.getClientName());
				row.createCell(1).setCellValue(output.getProductType());
				row.createCell(2).setCellValue(output.getProductName());
				row.createCell(3).setCellValue(output.getBankIssuerName());
				row.createCell(4).setCellValue(ExcelPortfolioUtility.getRowValue(output.getCurrentValue()));
				row.createCell(5).setCellValue(ExcelPortfolioUtility.getRowValue(output.getMaturityAmount()));
				row.createCell(6).setCellValue(formatter1.format((Date)formatter.parse(output.getMaturityDate())));

				rownum++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workbook;
	}

	public static XSSFWorkbook writeExcelOutputDataAssetClassWiseAUM(File file,
			List<PortfolioNetWorthDto> portfolioNetworthDtoList) {

		DecimalFormat df = new DecimalFormat("#.##");
		FileInputStream fis = null;
		XSSFWorkbook workbook = null;
		 double totalinvestment = 0d;
         double total = 0d;
         double equity = 0,fixedIncome= 0,alternate = 0, cash = 0;
         XSSFCell  cell2,cell3,cell4,cell5;
     
         
		try {
			fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			
			CellStyle s = null;
			s = workbook.createCellStyle();
			s.setAlignment(HorizontalAlignment.CENTER);
			
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			XSSFRow row = sheet.createRow((short) 1);
			XSSFCell cell = row.createCell((short) 1);
			cell.setCellValue("AUM(Rs Crores)");
			cell.setCellStyle(s);
 
			sheet.addMergedRegion(new CellRangeAddress(1,(short)1,1,(short)6));
			
			
			
			XSSFRow headRow = sheet.createRow(2);
			//headRow.createCell(0).setCellValue("Client ID");
			XSSFCell cell1 = headRow.createCell(1);
			cell1.setCellValue("Client Name");
			cell1.setCellStyle(s);
			cell1 = headRow.createCell(2);
			cell1.setCellValue("Equity");
			cell1.setCellStyle(s);
			cell1 = headRow.createCell(3);
			cell1.setCellValue("Debt");
			cell1.setCellStyle(s);
			cell1 = headRow.createCell(4);
			cell1.setCellValue("Alternatives");
			cell1.setCellStyle(s);
			cell1 = headRow.createCell(5);
			cell1.setCellValue("Cash");
			cell1.setCellStyle(s);
			cell1 = headRow.createCell(6);
			cell1.setCellValue("Total");
			cell1.setCellStyle(s);
			
			
			int rownum = 3;
			int investmentAssetListSize = 0;
			for (PortfolioNetWorthDto portfolioNetWorthDto : portfolioNetworthDtoList) {
			  total = 0;
			   row = sheet.createRow(rownum);
			//  row.createCell(0).setCellValue(portfolioNetWorthDto.getClientID());
			   cell1 = row.createCell(1);
			   cell1.setCellValue(portfolioNetWorthDto.getName());
			   System.out.println("portfolioNetWorthDto.getName() "+portfolioNetWorthDto.getName());
			  
			  Map<String, Map<String, Map<String, List<PortfolioNetWorthDto>>>> rootMap = portfolioNetWorthDto.getRootMap();
			  for (Entry<String, Map<String, Map<String, List<PortfolioNetWorthDto>>>> assetLiabilityMap : rootMap
	                    .entrySet()) {

	                if (assetLiabilityMap.getKey().equals("Assets")) {
	                  System.out.println("asset " + assetLiabilityMap.getKey());
	                  
	                    Map<String, Map<String, List<PortfolioNetWorthDto>>> assetliablityList = assetLiabilityMap.getValue();

	                    for (Entry<String, Map<String, List<PortfolioNetWorthDto>>> assetTypeMapList : assetliablityList.entrySet()) {
	                    	 Map<String, List<PortfolioNetWorthDto>> assetTypeList = assetTypeMapList.getValue();
		                     
	                        if (assetTypeMapList.getKey().equals("Investment Assets")) {
	                        String assetName = "";
	                        
	                        investmentAssetListSize = assetTypeList.size();
		                    System.out.println("investmentAssetListSize "+investmentAssetListSize);
	                      
	                       
	                        for (Entry<String, List<PortfolioNetWorthDto>> assetTypeMap : assetTypeList.entrySet()) {
	                        	totalinvestment = 0;
	                            assetName = assetTypeMap.getKey();
	                            System.out.println("assetName "+assetName);

	                            List<PortfolioNetWorthDto> productList = assetTypeMap.getValue();
	                           
	                            for (PortfolioNetWorthDto asset : productList) {
	                
	                               List<PortfolioNetWorthDto> netWorthDetails= asset.getNetworthDetails();
	                               for(PortfolioNetWorthDto netWorth:netWorthDetails) {
	                              // System.out.println("productName "+netWorth.getProductName()); 
	                              // System.out.println("netWorth.getCurrentValue() "+netWorth.getCurrentValue()); 
	                               totalinvestment += netWorth.getCurrentValue();
	                             }
	                              
	                          }
	                            
	                          System.out.println("assetName "+assetName);
	                          if(assetName.equalsIgnoreCase("Equity")) {
	                        	
	                        	  equity = totalinvestment/10000000;
	   		                      total += (totalinvestment/10000000);   
	   		                      System.out.println("total Equity "+df.format(totalinvestment/10000000));  
	   		                   }
	   	                      if(assetName.equalsIgnoreCase("FixedIncome")) {
	   	                
	   	                    	fixedIncome = totalinvestment/10000000;
	   			                total += (totalinvestment/10000000);   
	   			                System.out.println("total FixedIncome "+df.format(totalinvestment/10000000));  
	   			               }
	   	                      if(assetName.equalsIgnoreCase("Alternate")) {
	   	                    	
	   	                    	alternate = totalinvestment/10000000;
	   			                total += (totalinvestment/10000000); 
	   			                System.out.println("total Alternate "+df.format(totalinvestment/10000000));  
	   			               }
	   	                      if(assetName.equalsIgnoreCase("Cash")) {
	   	                    	  
	   	                    	cash = totalinvestment/10000000;
	   			                total += (totalinvestment/10000000); 
	   			                System.out.println("total Cash "+df.format(totalinvestment/10000000));  
	   			               }
     
	                      } 
	                 }
	                        System.out.println("total Equity "+df.format(equity));  
	                        System.out.println("total fixedIncome "+df.format(fixedIncome));  
	                        System.out.println("df.format(alternate) "+df.format(alternate));  
	                        System.out.println("df.format(cash) "+df.format(cash));  
	                       if(investmentAssetListSize != 0) {
	                       cell2 = row.createCell(2);
	      				   cell2.setCellStyle(s);
	                       cell2.setCellValue(df.format(equity));
	                     	  
	                       cell3 = row.createCell(3);
	      				   cell3.setCellStyle(s);
                           cell3.setCellValue(df.format(fixedIncome));
	          			  
                           cell4 = row.createCell(4);
	      				   cell4.setCellStyle(s);
	          			   cell4.setCellValue(df.format(alternate));
	          			  
	          			   cell5 = row.createCell(5);
	          			   cell5.setCellStyle(s);
	          			   cell5.setCellValue(df.format(cash));
	                       }
	                        
	               }
			      
	          } 
			
	                  
			}
			  System.out.println("investmentAssetListSize "+investmentAssetListSize);  
			  if(investmentAssetListSize == 0) {
				
				  cell2 = row.createCell(2);
				  cell2.setCellStyle(s);
				  cell2.setCellValue(0); 
				 
				  cell3 = row.createCell(3);
				  cell3.setCellStyle(s);
				  cell3.setCellValue(0); 
				
				  cell4 = row.createCell(4); 
				  cell4.setCellStyle(s);
				  cell4.setCellValue(0); 
				  
				  cell5 = row.createCell(5);
				  cell5.setCellStyle(s);
				  cell5.setCellValue(0); 
				
				 
			}
			 
           	  
		

			
			  XSSFCell cell6 = row.createCell(6);
			  cell6.setCellValue(df.format(total)); 
			  cell6.setCellStyle(s);
		
			  rownum++;
		  
		 }	
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workbook;
	}

	public static XSSFWorkbook writeExcelOutputDataProductTypeWiseAUM(File file,
			List<PortfolioNetWorthDto> portfolioNetworthDtoList) {
		 DecimalFormat df = new DecimalFormat("#.##");    
		FileInputStream fis = null;
		XSSFWorkbook workbook = null;
		 double totalinvestment = 0d;
         double total = 0d;
         double directEquity = 0;
         double MFETFPMS = 0;
         double depositBonds = 0;
         double SmallSavingSchemes = 0;
         double retirementSchemes = 0;
         double cash = 0;
         double alternateInvesments= 0;
     
		
		try {
			fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			
			CellStyle s = null;
			s = workbook.createCellStyle();
			s.setAlignment(HorizontalAlignment.CENTER);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			XSSFRow row = sheet.createRow((short) 1);
			XSSFCell cell = row.createCell((short) 1);
			cell.setCellValue("AUM(Rs Crores)");
			cell.setCellStyle(s);
			
			sheet.addMergedRegion(new CellRangeAddress(1,(short)1,1,(short)9));
			
			XSSFRow headRow =  sheet.createRow(2);
	
			cell = headRow.createCell(1);
			cell.setCellValue("Client Name");
			cell.setCellStyle(s);
			cell = headRow.createCell(2);
			cell.setCellValue("Direct Equity");
			cell.setCellStyle(s);
			cell = headRow.createCell(3);
			cell.setCellValue("MF / ETF / PMS");
			cell.setCellStyle(s);
			cell = headRow.createCell(4);
			cell.setCellValue("Deposit/Bonds");
			cell.setCellStyle(s);
			cell = headRow.createCell(5);
			cell.setCellValue("Small Saving Schemes");
			cell.setCellStyle(s);
			cell = headRow.createCell(6);
			cell.setCellValue("Retirement Schemes");
			cell.setCellStyle(s);
			cell = headRow.createCell(7);
			cell.setCellValue("Cash");
			cell.setCellStyle(s);
			cell = headRow.createCell(8);
			cell.setCellValue("Alternate Investment");
			cell.setCellStyle(s);
			cell = headRow.createCell(9);
			cell.setCellValue("Total");
			cell.setCellStyle(s);
			
			
			int rownum = 3;
			for (PortfolioNetWorthDto portfolioNetWorthDto : portfolioNetworthDtoList) {
			
			 
			  directEquity = 0;
		      MFETFPMS = 0;
		      depositBonds = 0;
		      SmallSavingSchemes = 0;
		      
		      retirementSchemes = 0;
		      cash = 0;
		      alternateInvesments= 0;
		      total = 0;
			  row = sheet.createRow(rownum);
			  
			  //row.createCell(0).setCellValue(portfolioNetWorthDto.getClientID());
			  row.createCell(1).setCellValue(portfolioNetWorthDto.getName());
			  
			  Map<String, Map<String, Map<String, List<PortfolioNetWorthDto>>>> rootMap = portfolioNetWorthDto.getRootMap();
			  for (Entry<String, Map<String, Map<String, List<PortfolioNetWorthDto>>>> assetLiabilityMap : rootMap
	                    .entrySet()) {

	                if (assetLiabilityMap.getKey().equals("Assets")) {
	                //  System.out.println("asset " + assetLiabilityMap.getKey());
	                  
	                    Map<String, Map<String, List<PortfolioNetWorthDto>>> assetliablityList = assetLiabilityMap.getValue();

	                    for (Entry<String, Map<String, List<PortfolioNetWorthDto>>> assetTypeMapList : assetliablityList.entrySet()) {
	                    	 Map<String, List<PortfolioNetWorthDto>> assetTypeList = assetTypeMapList.getValue();
		                     int investmentAssetListSize = assetTypeList.size();
		                    // System.out.println("investmentAssetListSize "+investmentAssetListSize);
	                      
	                        if (assetTypeMapList.getKey().equals("Investment Assets")) {
	                        String assetName = "";
	                       
	                       
	                        for (Entry<String, List<PortfolioNetWorthDto>> assetTypeMap : assetTypeList.entrySet()) {
	                        	
	                            assetName = assetTypeMap.getKey();
	                         //   System.out.println("assetName "+assetName);

	                            List<PortfolioNetWorthDto> productList = assetTypeMap.getValue();
	                           
	                            for (PortfolioNetWorthDto asset : productList) {
	                               String productType = asset.getProductType();
	                           //    System.out.println("productType "+productType); 
	                               
	                               List<PortfolioNetWorthDto> netWorthDetails= asset.getNetworthDetails();
	                               for(PortfolioNetWorthDto netWorth:netWorthDetails) {
	                            	   
	                             //  System.out.println("productName "+netWorth.getProductName()); 
	                             //  System.out.println("CurrentValue() "+netWorth.getCurrentValue()); 
	                             //  totalinvestment += netWorth.getCurrentValue();
	                               
	                             
	                               
	                               if(productType.equals("Direct Equity")) {
	                            	 //  System.out.println(productType+" "+netWorth.getProductName()+" "+netWorth.getCurrentValue()); 
	                            	   directEquity += netWorth.getCurrentValue();
	                            	 //  System.out.println("directEquity "+directEquity);
	                               }
	                               if(productType.equals("MF / ETF / PMS")) {
	                            	 //  System.out.println(productType+" "+netWorth.getProductName()+" "+netWorth.getCurrentValue()); 
	                            	   MFETFPMS += netWorth.getCurrentValue();
	                            	//   System.out.println("MFETFPMS "+MFETFPMS);
	                               }
	                               if(productType.equals("Cash")) {
	                            	//   System.out.println(productType+" "+netWorth.getProductName()+" "+netWorth.getCurrentValue()); 
	                            	   cash +=  netWorth.getCurrentValue();
	                            	//   System.out.println("cash "+cash);
	                               }
	                               if(productType.equals("Retirement Oriented Schemes")) {
	                            	 //  System.out.println(productType+" "+netWorth.getProductName()+" "+netWorth.getCurrentValue()); 
	                            	   retirementSchemes += netWorth.getCurrentValue();
	                            	 //  System.out.println("retirementSchemes "+retirementSchemes);
	                               }
	                               if(productType.equals("Deposit/Bonds")) {
	                            	//   System.out.println(productType+" "+netWorth.getProductName()+" "+netWorth.getCurrentValue()); 
	                            	   depositBonds += netWorth.getCurrentValue();
	                            	//   System.out.println("retirementSchemes "+retirementSchemes);
	                               }
	                               if(productType.equals("Small Saving Schemes")) {
	                            	//   System.out.println(productType+" "+netWorth.getProductName()+" "+netWorth.getCurrentValue()); 
	                            	   SmallSavingSchemes +=  netWorth.getCurrentValue();
	                            	//   System.out.println("SmallSavingSchemes "+SmallSavingSchemes);
	                               }
	                               if(productType.equals("Alternate Investments")) {
		                            	//   System.out.println(productType+" "+netWorth.getProductName()+" "+netWorth.getCurrentValue()); 
	                            	   alternateInvesments +=  netWorth.getCurrentValue();
		                            	//   System.out.println("SmallSavingSchemes "+SmallSavingSchemes);
		                             }
	                             }
	                              
	                          }
	                          
	                      } 
	                   }
	                        
	               }
			      
	          } 
			
			}
			  /*System.out.println(portfolioNetWorthDto.getName());
			  System.out.println("directEquity "+directEquity);
			  System.out.println("MFETFPMS "+MFETFPMS);
			  System.out.println("cash "+cash);
			  System.out.println("retirementSchemes "+retirementSchemes);
			  System.out.println("depositBonds "+depositBonds);
			  System.out.println("SmallSavingSchemes "+SmallSavingSchemes);
			  System.out.println("cash "+cash);*/
			 
			 
			  total = (directEquity/10000000)+(MFETFPMS/10000000)+(depositBonds/10000000)+(SmallSavingSchemes/10000000)+(retirementSchemes/10000000)+(cash/10000000)+(alternateInvesments/10000000);
			 
			  
			  cell = row.createCell(2);
			  cell.setCellValue(df.format(directEquity/10000000));
			  cell.setCellStyle(s);
			  
			  cell = row.createCell(3);
			  cell.setCellValue(df.format(MFETFPMS/10000000));
			  cell.setCellStyle(s);
			  
			  cell = row.createCell(4);
			  cell.setCellValue(df.format(depositBonds/10000000));
			  cell.setCellStyle(s);
			  
			  cell = row.createCell(5);
			  cell.setCellValue(df.format(SmallSavingSchemes/10000000));
			  cell.setCellStyle(s);
			  
			  cell = row.createCell(6);
			  cell.setCellValue(df.format(retirementSchemes/10000000));
			  cell.setCellStyle(s);
			  
			  cell = row.createCell(7);
			  cell.setCellValue(df.format(cash/10000000));
			  cell.setCellStyle(s);
			  
			  cell = row.createCell(8);
			  cell.setCellValue(df.format(alternateInvesments/10000000));
			  cell.setCellStyle(s);
			  
			  cell = row.createCell(9);
			  cell.setCellValue(df.format(total));   
			  cell.setCellStyle(s);
			  
			  rownum++;
			
		 }			
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workbook;
	}
	public static String getRowValue(double value) {
		String strValue;
		Format df2 = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		if(value >= 0) {
			strValue = df2.format(Math.round(value)).substring(1,df2.format(Math.round(value)).indexOf("."));
		//	System.out.println("strpveValue "+strValue);
		}else {
			strValue =df2.format(Math.round(value)).substring(2,df2.format(Math.round(value)).indexOf("."));
			strValue=strValue.substring(1);
			strValue = "-("+strValue+")";
			
		}
		return strValue;
		
	}
	
	/*	public static XSSFWorkbook writeExcelOutputDataProductTypeWiseAUM(File file,
	List<PortfolioNetWorthDto> portfolioNetworthDtoList) {
DecimalFormat df = new DecimalFormat("#");    
FileInputStream fis = null;
XSSFWorkbook workbook = null;
 double totalinvestment = 0d;
 double total = 0d;
 double directEquity = 0;
 double MFETFPMS = 0;
 double depositBonds = 0;
 double SmallSavingSchemes = 0;
 double retirementSchemes = 0;
 double cash = 0;


try {
	fis = new FileInputStream(file);
	workbook = new XSSFWorkbook(fis);
	XSSFSheet sheet = workbook.getSheetAt(0);
	XSSFRow headRow = sheet.createRow(1);
	
	headRow.createCell(0).setCellValue("Client Name");
	headRow.createCell(2).setCellValue("Direct Equity");
	headRow.createCell(3).setCellValue("MF / ETF / PMS");
	headRow.createCell(4).setCellValue("Deposit/Bonds");
	headRow.createCell(5).setCellValue("Small Saving Schemes");
	headRow.createCell(6).setCellValue("Retirement Schemes");
	headRow.createCell(6).setCellValue("Cash");
	headRow.createCell(6).setCellValue("Total");
	
	
	int rownum = 2;
	for (PortfolioNetWorthDto portfolioNetWorthDto : portfolioNetworthDtoList) {
	  total = 0;
	  directEquity = 0;
      MFETFPMS = 0;
      depositBonds = 0;
      SmallSavingSchemes = 0;
      retirementSchemes = 0;
      cash = 0;
	  XSSFRow row = sheet.createRow(rownum);
	  row.createCell(0).setCellValue(portfolioNetWorthDto.getClientID());
	  row.createCell(1).setCellValue(portfolioNetWorthDto.getName());
	  
	  Map<String, Map<String, Map<String, List<PortfolioNetWorthDto>>>> rootMap = portfolioNetWorthDto.getRootMap();
	  for (Entry<String, Map<String, Map<String, List<PortfolioNetWorthDto>>>> assetLiabilityMap : rootMap
                .entrySet()) {

            if (assetLiabilityMap.getKey().equals("Assets")) {
            //  System.out.println("asset " + assetLiabilityMap.getKey());
              
                Map<String, Map<String, List<PortfolioNetWorthDto>>> assetliablityList = assetLiabilityMap.getValue();

                for (Entry<String, Map<String, List<PortfolioNetWorthDto>>> assetTypeMapList : assetliablityList.entrySet()) {
                	 Map<String, List<PortfolioNetWorthDto>> assetTypeList = assetTypeMapList.getValue();
                     int investmentAssetListSize = assetTypeList.size();
                     System.out.println("investmentAssetListSize "+investmentAssetListSize);
                  
                    if (assetTypeMapList.getKey().equals("Investment Assets")) {
                    String assetName = "";
                   
                   
                    for (Entry<String, List<PortfolioNetWorthDto>> assetTypeMap : assetTypeList.entrySet()) {
                    	totalinvestment = 0;
                        assetName = assetTypeMap.getKey();
                        System.out.println("assetName "+assetName);

                        List<PortfolioNetWorthDto> productList = assetTypeMap.getValue();
                       
                        for (PortfolioNetWorthDto asset : productList) {
                           String productType = asset.getProductType();
                           List<PortfolioNetWorthDto> netWorthDetails= asset.getNetworthDetails();
                           for(PortfolioNetWorthDto netWorth:netWorthDetails) {
                        	   
                           System.out.println("productName "+netWorth.getProductName()); 
                           System.out.println("CurrentValue() "+netWorth.getCurrentValue()); 
                           totalinvestment += netWorth.getCurrentValue();
                           
                          directEquity = 0;
             		      MFETFPMS = 0;
             		      depositBonds = 0;
             		      SmallSavingSchemes = 0;
             		      retirementSchemes = 0;
                           
                           if(productType.equals("Direct Equity")) {
                        	   directEquity += directEquity + netWorth.getCurrentValue();
                           }
                           if(productType.equals("MF / ETF / PMS")) {
                        	   MFETFPMS += MFETFPMS + netWorth.getCurrentValue();
                           }
                           if(productType.equals("Cash")) {
                        	   cash += cash + netWorth.getCurrentValue();
                           }
                           if(productType.equals("Retirement Oriented Schemes")) {
                        	   retirementSchemes += retirementSchemes + netWorth.getCurrentValue();
                           }
                           if(productType.equals("Deposit/Bonds")) {
                        	   depositBonds += depositBonds + netWorth.getCurrentValue();
                           }
                           if(productType.equals("Small Saving Schemes")) {
                        	   SmallSavingSchemes += SmallSavingSchemes + netWorth.getCurrentValue();
                           }
                          
                         }
                          
                      }
                      if(assetName.equalsIgnoreCase("Equity")) {
		                     row.createCell(2).setCellValue(totalinvestment);
		                     total += totalinvestment;   
		                     System.out.println("total Equity "+total);  
		                   }
	                      if(assetName.equalsIgnoreCase("FixedIncome")) {
			                 row.createCell(3).setCellValue(totalinvestment);
			                 total += totalinvestment;  
			                 System.out.println("total FixedIncome "+total);  
			               }
	                      if(assetName.equalsIgnoreCase("Alternate")) {
			                 row.createCell(4).setCellValue(totalinvestment);
			                 total += totalinvestment; 
			                 System.out.println("total Alternate "+total);  
			               }
	                      if(assetName.equalsIgnoreCase("Cash")) {
			                 row.createCell(5).setCellValue(totalinvestment);
			                 total += totalinvestment;  
			                 System.out.println("total Cash "+total);  
			               }

                  } 
             }
                    
           }
	      
      } 
	
	}
	  row.createCell(6).setCellValue(total);      
	  rownum++;
  
 }			
	
}
catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} 
try {
	fis.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return workbook;
}
*/


}
