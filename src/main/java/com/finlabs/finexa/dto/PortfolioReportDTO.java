package com.finlabs.finexa.dto;

import com.finlabs.finexa.dto.clientinfo.AdvisorDTO;
import com.finlabs.finexa.dto.clientinfo.ClientMasterDTO;

public class PortfolioReportDTO {

	private Integer clientId;
	private ClientMasterDTO clientMasterDTO;
	private String timestamp;
	private AdvisorDTO advisorDTO;
	private String fileName;
	private String msg;

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public ClientMasterDTO getClientMasterDTO() {
		return clientMasterDTO;
	}

	public void setClientMasterDTO(ClientMasterDTO clientMasterDTO) {
		this.clientMasterDTO = clientMasterDTO;
	}

	public AdvisorDTO getAdvisorDTO() {
		return advisorDTO;
	}

	public void setAdvisorDTO(AdvisorDTO advisorDTO) {
		this.advisorDTO = advisorDTO;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
