package com.finlabs.finexa.dto.clientinfo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientMasterDTO {
	private int id;
	@Min(value = 1, message = "User Id can not be blank/Zero")
	private int userId;
	@NotEmpty(message = "Salution can not be Empty")
	private String salutation;
	@NotEmpty(message = "First Name can not be Empty")
	private String firstName;
	private String middleName;
	private String lastName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	@Past(message = "Date of Birth should be past date")
	private Date birthDate;
	private String dateOfBirth;
	@NotEmpty(message = "Gender should not be null")
	private String gender;
	@NotEmpty(message = "Pan should not be null")
	private String pan;
	@Digits(integer = 12, fraction = 0, message = "Aadhar should be of 12 Digits")
	private long aadhar;
	@Range(min = 1, max = 3, message = "Marital status id should be between 1 and 3")
	private byte maritalStatus;
	private String maritalStatusDesc;

	private String otherMaritalStatus;
	private byte eduQualification;
	private String otherEduQualification;
	private byte employmentType;
	private String otherEmploymentType;
	private String orgName;
	private String currDesignation;
	private byte residentType;
	private String otherResidentType;
	private int countryOfResidence;
	@NotEmpty(message = "Retirement Flag should not be null")
	private String retiredFlag;
	private Integer retirementAge;
	private Integer lifeExpectancy;
	private Integer riskProfileScore;
	private String riskProfileName;
	private String activeFlag;

	private int ClientGuardianId;
	private int familyMemberId;
	private int age;

	private ClientContactDTO clientContactDTO;
	private ClientGuardianDTO clientGuardianDTO;
	private ClientGuardianContactDTO clientGuardianContactDTO;
	private ClientFamilyMemberDTO clientFamilyMemberDTO;

	// For Multiple Family Member
	private List<ClientFamilyMemberDTO> clientFamilyMembersDTO;

	// For Multiple Contact Address
	private List<ClientContactDTO> clientContactsDTO;

	private ClientFamilyIncomeDTO clientFamilyIncomeDTO;

	private LifeExpectancyDTO clientLifeExpDTO;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Timestamp lastUpdatedOn;
	
	private String clientFullName;
	private String residentTypeName;

	public List<ClientContactDTO> getClientContactsDTO() {
		return clientContactsDTO;
	}

	public void setClientContactsDTO(List<ClientContactDTO> clientContactsDTO) {
		this.clientContactsDTO = clientContactsDTO;
	}

	public List<ClientFamilyMemberDTO> getClientFamilyMembersDTO() {
		return clientFamilyMembersDTO;
	}

	public void setClientFamilyMembersDTO(List<ClientFamilyMemberDTO> clientFamilyMembersDTO) {
		this.clientFamilyMembersDTO = clientFamilyMembersDTO;
	}

	public ClientMasterDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public long getAadhar() {
		return aadhar;
	}

	public void setAadhar(long aadhar) {
		this.aadhar = aadhar;
	}

	public byte getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(byte maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getOtherMaritalStatus() {
		return otherMaritalStatus;
	}

	public void setOtherMaritalStatus(String otherMaritalStatus) {
		this.otherMaritalStatus = otherMaritalStatus;
	}

	public byte getEduQualification() {
		return eduQualification;
	}

	public void setEduQualification(byte eduQualification) {
		this.eduQualification = eduQualification;
	}

	public String getOtherEduQualification() {
		return otherEduQualification;
	}

	public void setOtherEduQualification(String otherEduQualification) {
		this.otherEduQualification = otherEduQualification;
	}

	public byte getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(byte employmentType) {
		this.employmentType = employmentType;
	}

	public String getOtherEmploymentType() {
		return otherEmploymentType;
	}

	public void setOtherEmploymentType(String otherEmploymentType) {
		this.otherEmploymentType = otherEmploymentType;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCurrDesignation() {
		return currDesignation;
	}

	public void setCurrDesignation(String currDesignation) {
		this.currDesignation = currDesignation;
	}

	public byte getResidentType() {
		return residentType;
	}

	public void setResidentType(byte residentType) {
		this.residentType = residentType;
	}

	public String getOtherResidentType() {
		return otherResidentType;
	}

	public void setOtherResidentType(String otherResidentType) {
		this.otherResidentType = otherResidentType;
	}

	public int getCountryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(int countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public String getRetiredFlag() {
		return retiredFlag;
	}

	public void setRetiredFlag(String retiredFlag) {
		this.retiredFlag = retiredFlag;
	}

	public Integer getRetirementAge() {
		return retirementAge;
	}

	public void setRetirementAge(Integer retirementAge) {
		this.retirementAge = retirementAge;
	}

	public Integer getLifeExpectancy() {
		return lifeExpectancy;
	}

	public void setLifeExpectancy(Integer lifeExpectancy) {
		this.lifeExpectancy = lifeExpectancy;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public int getClientGuardianId() {
		return ClientGuardianId;
	}

	public void setClientGuardianId(int clientGuardianId) {
		ClientGuardianId = clientGuardianId;
	}

	public ClientContactDTO getClientContactDTO() {
		return clientContactDTO;
	}

	public void setClientContactDTO(ClientContactDTO clientContactDTO) {
		this.clientContactDTO = clientContactDTO;
	}

	public ClientFamilyMemberDTO getClientFamilyMemberDTO() {
		return clientFamilyMemberDTO;
	}

	public void setClientFamilyMemberDTO(ClientFamilyMemberDTO clientFamilyMemberDTO) {
		this.clientFamilyMemberDTO = clientFamilyMemberDTO;
	}

	public ClientFamilyIncomeDTO getClientFamilyIncomeDTO() {
		return clientFamilyIncomeDTO;
	}

	public void setClientFamilyIncomeDTO(ClientFamilyIncomeDTO clientFamilyIncomeDTO) {
		this.clientFamilyIncomeDTO = clientFamilyIncomeDTO;
	}

	public LifeExpectancyDTO getClientLifeExpDTO() {
		return clientLifeExpDTO;
	}

	public void setClientLifeExpDTO(LifeExpectancyDTO clientLifeExpDTO) {
		this.clientLifeExpDTO = clientLifeExpDTO;
	}

	public Integer getRiskProfileScore() {
		return riskProfileScore;
	}

	public String getRiskProfileName() {
		return riskProfileName;
	}

	public void setRiskProfileScore(Integer riskProfileScore) {
		this.riskProfileScore = riskProfileScore;
	}

	public void setRiskProfileName(String riskProfileName) {
		this.riskProfileName = riskProfileName;
	}

	public int getFamilyMemberId() {
		return familyMemberId;
	}

	public void setFamilyMemberId(int familyMemberId) {
		this.familyMemberId = familyMemberId;
	}

	public ClientGuardianDTO getClientGuardianDTO() {
		return clientGuardianDTO;
	}

	public void setClientGuardianDTO(ClientGuardianDTO clientGuardianDTO) {
		this.clientGuardianDTO = clientGuardianDTO;
	}

	public ClientGuardianContactDTO getClientGuardianContactDTO() {
		return clientGuardianContactDTO;
	}

	public void setClientGuardianContactDTO(ClientGuardianContactDTO clientGuardianContactDTO) {
		this.clientGuardianContactDTO = clientGuardianContactDTO;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public String getClientFullName() {
		return clientFullName;
	}

	public void setClientFullName(String clientFullName) {
		this.clientFullName = clientFullName;
	}

	public String getResidentTypeName() {
		return residentTypeName;
	}

	public void setResidentTypeName(String residentTypeName) {
		this.residentTypeName = residentTypeName;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getMaritalStatusDesc() {
		return maritalStatusDesc;
	}

	public void setMaritalStatusDesc(String maritalStatusDesc) {
		this.maritalStatusDesc = maritalStatusDesc;
	}

	@Override
	public String toString() {
		return "ClientMasterDTO [id=" + id + ", userId=" + userId + ", salutation=" + salutation + ", firstName="
				+ firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", birthDate=" + birthDate
				+ ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", pan=" + pan + ", aadhar=" + aadhar
				+ ", maritalStatus=" + maritalStatus + ", maritalStatusDesc=" + maritalStatusDesc
				+ ", otherMaritalStatus=" + otherMaritalStatus + ", eduQualification=" + eduQualification
				+ ", otherEduQualification=" + otherEduQualification + ", employmentType=" + employmentType
				+ ", otherEmploymentType=" + otherEmploymentType + ", orgName=" + orgName + ", currDesignation="
				+ currDesignation + ", residentType=" + residentType + ", otherResidentType=" + otherResidentType
				+ ", countryOfResidence=" + countryOfResidence + ", retiredFlag=" + retiredFlag + ", retirementAge="
				+ retirementAge + ", lifeExpectancy=" + lifeExpectancy + ", riskProfileScore=" + riskProfileScore
				+ ", riskProfileName=" + riskProfileName + ", activeFlag=" + activeFlag + ", ClientGuardianId="
				+ ClientGuardianId + ", familyMemberId=" + familyMemberId + ", age=" + age + ", clientContactDTO="
				+ clientContactDTO + ", clientGuardianDTO=" + clientGuardianDTO + ", clientGuardianContactDTO="
				+ clientGuardianContactDTO + ", clientFamilyMemberDTO=" + clientFamilyMemberDTO
				+ ", clientFamilyMembersDTO=" + clientFamilyMembersDTO + ", clientContactsDTO=" + clientContactsDTO
				+ ", clientFamilyIncomeDTO=" + clientFamilyIncomeDTO + ", clientLifeExpDTO=" + clientLifeExpDTO
				+ ", lastUpdatedOn=" + lastUpdatedOn + ", clientFullName=" + clientFullName + ", residentTypeName="
				+ residentTypeName + "]";
	}

	
	

}
