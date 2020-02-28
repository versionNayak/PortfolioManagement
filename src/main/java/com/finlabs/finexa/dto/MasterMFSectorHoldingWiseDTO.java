package com.finlabs.finexa.dto;

import java.util.Date;

public class MasterMFSectorHoldingWiseDTO {
	
	private String isin;
	private String sectorName;
	private float holding;
	private Date date;
	
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	public float getHolding() {
		return holding;
	}
	public void setHolding(float holding) {
		this.holding = holding;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
