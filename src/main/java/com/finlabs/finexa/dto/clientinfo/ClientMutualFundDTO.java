package com.finlabs.finexa.dto.clientinfo;

import java.math.BigDecimal;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;


public class ClientMutualFundDTO {

	private int id;
	@NotNull(message = "fundTypeID must not be null")
	private byte fundTypeID;
	private int aifID;
	private String amfiCode;
	private String closeEndedFlag;
	private BigDecimal currentMarketValue;
	@NotNull(message = "investmentAmount must not be null")
	@DecimalMin(value="0.01", message="investmentAmount cannot be 0")
	private BigDecimal investmentAmount;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="IST")
	@NotNull(message = "investmentStartDate must not be null")
	private Date investmentStartDate;
	private BigDecimal lumpsumUnitsPurchased;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone="IST")
	private Date mfLumpsumLockedInDate;
	private int pmsID;
	private byte sipFrequency;
	private byte sipInstalments;
	private byte fundCategoryID;
	private byte investmentModeID;
	private int clientID;
	private int familyMemberId;
	private String ownerName;
	private String fundHouse;
	private String fundTypeName;
	private String schemeName;
	private String schemeNamePMS;
	private String providerName;
	private String investmentModeName;
	private Byte investmentModeIDETF;
	
	

	public ClientMutualFundDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAifID() {
		return aifID;
	}

	public void setAifID(int aifID) {
		this.aifID = aifID;
	}

	public String getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(String amfiCode) {
		this.amfiCode = amfiCode;
	}

	public String getCloseEndedFlag() {
		return closeEndedFlag;
	}

	public void setCloseEndedFlag(String closeEndedFlag) {
		this.closeEndedFlag = closeEndedFlag;
	}

	public BigDecimal getCurrentMarketValue() {
		return currentMarketValue;
	}

	public void setCurrentMarketValue(BigDecimal currentMarketValue) {
		this.currentMarketValue = currentMarketValue;
	}

	public BigDecimal getInvestmentAmount() {
		return investmentAmount;
	}

	public void setInvestmentAmount(BigDecimal investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	public Date getInvestmentStartDate() {
		return investmentStartDate;
	}

	public void setInvestmentStartDate(Date investmentStartDate) {
		this.investmentStartDate = investmentStartDate;
	}

	public BigDecimal getLumpsumUnitsPurchased() {
		return lumpsumUnitsPurchased;
	}

	public void setLumpsumUnitsPurchased(BigDecimal lumpsumUnitsPurchased) {
		this.lumpsumUnitsPurchased = lumpsumUnitsPurchased;
	}

	public Date getMfLumpsumLockedInDate() {
		return mfLumpsumLockedInDate;
	}

	public void setMfLumpsumLockedInDate(Date mfLumpsumLockedInDate) {
		this.mfLumpsumLockedInDate = mfLumpsumLockedInDate;
	}

	public int getPmsID() {
		return pmsID;
	}

	public void setPmsID(int pmsID) {
		this.pmsID = pmsID;
	}

	public byte getSipFrequency() {
		return sipFrequency;
	}

	public void setSipFrequency(byte sipFrequency) {
		this.sipFrequency = sipFrequency;
	}

	public byte getSipInstalments() {
		return sipInstalments;
	}

	public void setSipInstalments(byte sipInstalments) {
		this.sipInstalments = sipInstalments;
	}

	public byte getFundTypeID() {
		return fundTypeID;
	}

	public void setFundTypeID(byte fundTypeID) {
		this.fundTypeID = fundTypeID;
	}

	public byte getFundCategoryID() {
		return fundCategoryID;
	}

	public void setFundCategoryID(byte fundCategoryID) {
		this.fundCategoryID = fundCategoryID;
	}

	public byte getInvestmentModeID() {
		return investmentModeID;
	}

	public void setInvestmentModeID(byte investmentModeID) {
		this.investmentModeID = investmentModeID;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getFundHouse() {
		return fundHouse;
	}

	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}

	public String getFundTypeName() {
		return fundTypeName;
	}

	public void setFundTypeName(String fundTypeName) {
		this.fundTypeName = fundTypeName;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getInvestmentModeName() {
		return investmentModeName;
	}

	public void setInvestmentModeName(String investmentModeName) {
		this.investmentModeName = investmentModeName;
	}

	public Byte getInvestmentModeIDETF() {
		return investmentModeIDETF;
	}

	public void setInvestmentModeIDETF(Byte investmentModeIDETF) {
		this.investmentModeIDETF = investmentModeIDETF;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getSchemeNamePMS() {
		return schemeNamePMS;
	}

	public void setSchemeNamePMS(String schemeNamePMS) {
		this.schemeNamePMS = schemeNamePMS;
	}

	public int getFamilyMemberId() {
		return familyMemberId;
	}

	public void setFamilyMemberId(int familyMemberId) {
		this.familyMemberId = familyMemberId;
	}

	@Override
	public String toString() {
		return "ClientMutualFundDTO [id=" + id + ", fundTypeID=" + fundTypeID + ", aifID=" + aifID + ", amfiCode="
				+ amfiCode + ", closeEndedFlag=" + closeEndedFlag + ", currentMarketValue=" + currentMarketValue
				+ ", investmentAmount=" + investmentAmount + ", investmentStartDate=" + investmentStartDate
				+ ", lumpsumUnitsPurchased=" + lumpsumUnitsPurchased + ", mfLumpsumLockedInDate="
				+ mfLumpsumLockedInDate + ", pmsID=" + pmsID + ", sipFrequency=" + sipFrequency + ", sipInstalments="
				+ sipInstalments + ", fundCategoryID=" + fundCategoryID + ", investmentModeID=" + investmentModeID
				+ ", clientID=" + clientID + ", ownerName=" + ownerName + ", fundHouse=" + fundHouse + ", fundTypeName="
				+ fundTypeName + ", schemeName=" + schemeName + ", schemeNamePMS=" + schemeNamePMS + ", providerName="
				+ providerName + ", investmentModeName=" + investmentModeName + ", investmentModeIDETF="
				+ investmentModeIDETF + ", familyMemberId=" + familyMemberId + "]";
	}
	
	

}