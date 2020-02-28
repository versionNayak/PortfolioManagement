package com.finlabs.finexa.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PortfolioEquityDailyPriceDto {

	
	private double closingPrice;
 //  @JsonFormat(pattern="yyyy-MM-dd")
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
	private Date date;

	public double getClosingPrice() {
		return closingPrice;
	}

	public void setClosingPrice(double closingPrice) {
		this.closingPrice = closingPrice;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
