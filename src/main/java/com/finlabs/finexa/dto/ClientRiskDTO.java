package com.finlabs.finexa.dto;

import java.io.Serializable;

public class ClientRiskDTO  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int clientId;
	private Integer riskProfile;
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public Integer getRiskProfile() {
		return riskProfile;
	}
	public void setRiskProfile(Integer riskProfile) {
		this.riskProfile = riskProfile;
	}
	
}