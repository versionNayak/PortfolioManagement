package com.finlabs.finexa.dto;

import java.io.Serializable;

public class PortfolioRatiosDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int serialNo;
	private String ratios;
	private int ratioId;
	private String logicRational;
	private int logicRationalId;
	private Double value;
	private String commentMaster;

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getRatios() {
		return ratios;
	}

	public void setRatios(String ratios) {
		this.ratios = ratios;
	}

	public int getRatioId() {
		return ratioId;
	}

	public void setRatioId(int ratioId) {
		this.ratioId = ratioId;
	}

	public String getLogicRational() {
		return logicRational;
	}

	public void setLogicRational(String logicRational) {
		this.logicRational = logicRational;
	}

	public int getLogicRationalId() {
		return logicRationalId;
	}

	public void setLogicRationalId(int logicRationalId) {
		this.logicRationalId = logicRationalId;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getCommentMaster() {
		return commentMaster;
	}

	public void setCommentMaster(String commentMaster) {
		this.commentMaster = commentMaster;
	}

}
