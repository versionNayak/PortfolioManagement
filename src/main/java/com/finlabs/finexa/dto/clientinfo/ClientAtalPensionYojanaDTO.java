package com.finlabs.finexa.dto.clientinfo;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;


public class ClientAtalPensionYojanaDTO {

	private int id;	
	private Integer clientID;	
	private Integer familyMemberID;	
	@NotNull(message = "financialAssetType must not be null")
	@Min(value=1,message = "financialAssetType must not be 0")
	private Byte financialAssetType;
	@NotNull(message = "investmentFrequency must not be null")
	private Byte investmentFrequency;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy", timezone="IST")
	@NotNull(message = "apyStartDate must not be null")
	private Date apyStartDate;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getClientID() {
		return clientID;
	}

	public void setClientID(Integer clientID) {
		this.clientID = clientID;
	}

	public Integer getFamilyMemberID() {
		return familyMemberID;
	}

	public void setFamilyMemberID(Integer familyMemberID) {
		this.familyMemberID = familyMemberID;
	}

	public Byte getFinancialAssetType() {
		return financialAssetType;
	}

	public void setFinancialAssetType(Byte financialAssetType) {
		this.financialAssetType = financialAssetType;
	}

	public Byte getInvestmentFrequency() {
		return investmentFrequency;
	}

	public void setInvestmentFrequency(Byte investmentFrequency) {
		this.investmentFrequency = investmentFrequency;
	}

	public Date getApyStartDate() {
		return apyStartDate;
	}

	public void setApyStartDate(Date apyStartDate) {
		this.apyStartDate = apyStartDate;
	}

	public BigDecimal getMonthlyPensionRequired() {
		return monthlyPensionRequired;
	}

	public void setMonthlyPensionRequired(BigDecimal monthlyPensionRequired) {
		this.monthlyPensionRequired = monthlyPensionRequired;
	}

	private BigDecimal monthlyPensionRequired;

	public ClientAtalPensionYojanaDTO() {
	}

	@Override
	public String toString() {
		return "ClientAtalPensionYojanaDTO [id=" + id + ", clientID=" + clientID + ", familyMemberID=" + familyMemberID
				+ ", financialAssetType=" + financialAssetType + ", investmentFrequency=" + investmentFrequency
				+ ", apyStartDate=" + apyStartDate + ", monthlyPensionRequired=" + monthlyPensionRequired + "]";
	}



	

}