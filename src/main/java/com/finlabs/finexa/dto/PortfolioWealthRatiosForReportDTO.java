package com.finlabs.finexa.dto;

public class PortfolioWealthRatiosForReportDTO {
	
	private String wealthRatioName;
	private Double wealthRatio;
	private String wealthRatioLogicRationale;
	private String wealthRatioComment;
	
	public String getWealthRatioName() {
		return wealthRatioName;
	}
	public void setWealthRatioName(String wealthRatioName) {
		this.wealthRatioName = wealthRatioName;
	}
	
	public Double getWealthRatio() {
		return wealthRatio;
	}
	public void setWealthRatio(Double wealthRatio) {
		this.wealthRatio = wealthRatio;
	}
	
	public String getWealthRatioLogicRationale() {
		return wealthRatioLogicRationale;
	}
	public void setWealthRatioLogicRationale(String wealthRatioLogicRationale) {
		this.wealthRatioLogicRationale = wealthRatioLogicRationale;
	}
	
	public String getWealthRatioComment() {
		return wealthRatioComment;
	}
	public void setWealthRatioComment(String wealthRatioComment) {
		this.wealthRatioComment = wealthRatioComment;
	}
	
	@Override
	public String toString() {
		return "PortfolioWealthRatiosForReportDTO [wealthRatioName=" + wealthRatioName + ", wealthRatio=" + wealthRatio
				+ ", wealthRatioLogicRationale=" + wealthRatioLogicRationale + ", wealthRatioComment="
				+ wealthRatioComment + "]";
	}
	
}
