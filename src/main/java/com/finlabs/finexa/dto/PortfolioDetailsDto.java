package com.finlabs.finexa.dto;

import java.util.Date;

public class PortfolioDetailsDto {

	private String holdings;
	private Double percOfPortfolio;
	private String sector;
	private Date assetDate;

	public String getHoldings() {
		return holdings;
	}

	public void setHoldings(String holdings) {
		this.holdings = holdings;
	}

	public Double getPercOfPortfolio() {
		return percOfPortfolio;
	}

	public void setPercOfPortfolio(Double percOfPortfolio) {
		this.percOfPortfolio = percOfPortfolio;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public Date getAssetDate() {
		return assetDate;
	}

	public void setAssetDate(Date assetDate) {
		this.assetDate = assetDate;
	}

}
