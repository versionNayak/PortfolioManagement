/**
 * 
 */
package com.finlabs.finexa.pm.util;

/**
 * @author vishwajeet
 *
 */
public class FinexaConstant {

	/****************** portfolio management Constants **************/
	public static final String ASSET_TYPE_INVESTMENT = "Investment Assets";
	public static final String ASSET_TYPE_PERSONAL = "Personal Assets";
	public static final String LIABILITIES = "LIABILITIES";
	public static final String MARKET_LINKED = "Market Linked";
	public static final String FIXED_RETURN = "Fixed / Guaranteed";
	public static final String ST_CLASSIFICATION = "ST";
	public static final String LT_CLASSIFICATION = "LT";
	public static final String EXPOSURE_SPAN_GREATER_THAN_1_YEAR = ">1 year";
	public static final String EXPOSURE_SPAN_LESS_THAN_1_YEAR = "<1 year";
	public static final String EXPOSURE_SPAN_OTHERS = "Others";
	public static final String LOCKED_IN_DATE_AVAILABLE = "Available";
	public static final String NETWORTH_HEADING_ASSETS = "Assets";
	public static final String NETWORTH_HEADING_LIABILITY = "Liabilities";
	public static final String LUMPSUM_INFLOW_TEXT_LABEL = "LUMPSUM_INFLOW";
	/****************** portfolio management Constants **************/

	/**************************
	 * Budget Management Constants
	 **************************/
	public static final int BANK_FIXED_DEPOSITS_ID = 22;
	public static final int BANK_RECURRING_DEPOSITS_ID = 23;
	public static final int PO_RECURRING_DEPOSITS_ID = 28;
	public static final int BONDS_DEBENTURES_ID = 24;
	public static final int PO_TIME_DEPOSIT_ID = 29;
	public static final int PO_MIS_ID = 30;
	public static final int SCSS_ID = 31;
	public static final int SUKANYA_SAMRIDDHI_SCHEME_ID = 32;
	public static final int SUKANYA_SAMRIDDHI_PAYMENT_TENURE = 15;
	public static final int SUKANYA_SAMRIDDHI_MATURITY_TENURE = 21;
	public static final int BOND_TYPE_BOND_WITH_COUPON_ID = 3;
	public static final int BOND_TYPE_PERPETUAL_BOND_ID = 2;

	/******************************* Cache Constant **************************/
	public static final String CALCULATION_TYPE_CONSTANT = "CALCULATION";
	public static final String MASTER_TYPE_CONSTANT = "MASTER";

	public static final String CALCULATION_SUB_TYPE_PORTFOLIO_CONSTANT = "PORTFOLIO-CAL";
	public static final String CALCULATION_SUB_TYPE_FUTURELOAN_CONSTANT = "FUTURELOAN-CAL";
	public static final String CALCULATION_SUB_TYPE_NETSURPLUS_CONSTANT = "NETSURPLUS-CAL";
	/**************************************************************************/

	/**********************
	 * Portfolio Exception Constant
	 **********************************/
	public static final String PORTFOLIO_OVERVIEW_MODULE = "Portfolio Overview Module";
	public static final String PORTFOLIO_OVERVIEW_MODULE_CODE = "POMC";
	public static final String PORTFOLIO_OVERVIEW_MODULE_DESC = "Failed to GET Portfolio Overview . Please see log for details.";
	
	public static final String PORTFOLIO_NETWORTH_MODULE = "Portfolio networth module";
	public static final String PORTFOLIO_NETWORTH_MODULE_CODE = "PNMC";
	public static final String PORTFOLIO_NETWORTH_MODULE_DESC = "Failed to GET Portfolio networth. Please see log for details.";


	//public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 1*60*60;
	//public static final String SIGNING_KEY = "finlabs123r";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String PORTFOLIO_TRACKER_SPECIFIC_REQUIREMENT_STAT = "tracker";
    public static final String PERSONAL_REPORT = "Personal";
    public static final String LIABILITIES_REPORT = "Liabilities";
    public static final String INVESTMENT_REPORT = "Investment";
    public static final String ASSETS_REPORT = "Assets";
    public static final String INVESTMENT_ASSETS_REPORT = "Investment Assets";
    public static final String PERSONAL_ASSETS_REPORT = "Personal Assets";
    public static final String LOAN_REPORT = "Loan";
    public static final String RISK_PROFILE_CONSERVATIVE = "Conservative";
    public static final String RISK_PROFILE_CONSERVATIVE_TEXT = "The investor top priority is safety of capital and is willing to accept minimal risks and hence prepared to receive minimum or low returns. Risk profile 1 to 3 lie in the Conservative band with 1 being more conservative than 3.";
    public static final String RISK_PROFILE_MODERATE = "Moderate";
    public static final String RISK_PROFILE_MODERATE_TEXT = "The investor is willing to accept moderate levels of risk in exchange for higher potential returns over the medium to long term Risk Profile 4 to 6 lie in Moderate band with 4 being more conservative than 6.";
    public static final String RISK_PROFILE_AGGRESSIVE = "Aggressive";
    public static final String RISK_PROFILE_AGGRESSIVE_TEXT = "The investor is willing to accept significant risk to maximize his potential returns over the long term. They are aware that they may lose a significant part of their capital and are prepared to make good the losses over and above the principal sum invested in certain cases Risk Profile 7 to 10 lie in Aggressive band with 7 being more conservative than 10.";
    public static final String CURRENT_AA_PIE_TITLE = "Current AA";
    public static final String CURRENT_SUB_AA_PIE_TITLE = "Current Sub AA";
    public static final String CURRENT_ALLOCATION = "Current Allocation";
    public static final String RECOMMENDED_ALLOCATION = "Recommended Allocation";

}
