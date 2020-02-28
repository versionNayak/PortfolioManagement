package com.finlabs.finexa.dto.clientinfo;

import java.util.Date;


import javax.validation.constraints.NotNull;


import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientFamilyMemberDTO {

	private int id;
	@NotNull(message="First Name can not be Empty")
	private String firstName;

	private String middleName;

	private String lastName;
	@NotNull(message="relationID can not be Empty")
	@Range(min=0,max=8, message="relationID should be between 0 and 8")
	private int relationID;

	private String relationName;

	private String otherRelation;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy",timezone="IST")
	@NotNull(message="birthDate can not be Empty")
	private Date birthDate;
	
	private String dateOfBirth;

	private String pan;

	private String aadhar;
	@NotNull(message="dependentFlag can not be Empty")
	private String dependentFlag;

	private String retiredFlag;

	private Integer lifeExpectancy;

	private Integer retirementAge;

	private String isTobaccoUser;
	
	private String isProperBMI;
	
	private String hasDiseaseHistory;
	
	private String hasNormalBP;

	private String gender;

	private int clientID;
	
	private double totalAmount;
	
	private String fmFullName;
	
	
	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getRelationID() {
		return relationID;
	}

	public void setRelationID(int relationID) {
		this.relationID = relationID;
	}

	public String getOtherRelation() {
		return otherRelation;
	}

	public void setOtherRelation(String otherRelation) {
		this.otherRelation = otherRelation;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAadhar() {
		return aadhar;
	}

	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}

	public String getDependentFlag() {
		return dependentFlag;
	}

	public void setDependentFlag(String dependentFlag) {
		this.dependentFlag = dependentFlag;
	}

	public String getRetiredFlag() {
		return retiredFlag;
	}

	public void setRetiredFlag(String retiredFlag) {
		this.retiredFlag = retiredFlag;
	}

	public Integer getLifeExpectancy() {
		return lifeExpectancy;
	}

	public void setLifeExpectancy(Integer lifeExpectancy) {
		this.lifeExpectancy = lifeExpectancy;
	}

	public Integer getRetirementAge() {
		return retirementAge;
	}

	public void setRetirementAge(Integer retirementAge) {
		this.retirementAge = retirementAge;
	}

	public String getIsTobaccoUser() {
		return isTobaccoUser;
	}

	public void setIsTobaccoUser(String isTobaccoUser) {
		this.isTobaccoUser = isTobaccoUser;
	}

	public String getIsProperBMI() {
		return isProperBMI;
	}

	public void setIsProperBMI(String isProperBMI) {
		this.isProperBMI = isProperBMI;
	}

	public String getHasDiseaseHistory() {
		return hasDiseaseHistory;
	}

	public void setHasDiseaseHistory(String hasDiseaseHistory) {
		this.hasDiseaseHistory = hasDiseaseHistory;
	}

	public String getHasNormalBP() {
		return hasNormalBP;
	}

	public void setHasNormalBP(String hasNormalBP) {
		this.hasNormalBP = hasNormalBP;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getFmFullName() {
		return fmFullName;
	}

	public void setFmFullName(String fmFullName) {
		this.fmFullName = fmFullName;
	}

	@Override
	public String toString() {
		return "ClientFamilyMemberDTO [id=" + id + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", relationID=" + relationID + ", relationName=" + relationName
				+ ", otherRelation=" + otherRelation + ", birthDate=" + birthDate + ", dateOfBirth=" + dateOfBirth
				+ ", pan=" + pan + ", aadhar=" + aadhar + ", dependentFlag=" + dependentFlag + ", retiredFlag="
				+ retiredFlag + ", lifeExpectancy=" + lifeExpectancy + ", retirementAge=" + retirementAge
				+ ", isTobaccoUser=" + isTobaccoUser + ", isProperBMI=" + isProperBMI + ", hasDiseaseHistory="
				+ hasDiseaseHistory + ", hasNormalBP=" + hasNormalBP + ", gender=" + gender + ", clientID=" + clientID
				+ ", totalAmount=" + totalAmount + ", fmFullName=" + fmFullName + "]";
	}

	

}
