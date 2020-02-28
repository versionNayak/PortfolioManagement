package com.finlabs.finexa.dto;

import java.io.Serializable;

public class PortfolioHistoricalReturnOutput implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int year;
	private double assetReturn;
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public double getAssetReturn() {
		return assetReturn;
	}
	public void setAssetReturn(double assetReturn) {
		this.assetReturn = assetReturn;
	}

}
