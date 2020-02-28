package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author vishwajeet
 *
 */
public class AssetQualityMapDTO implements Serializable {
	
	private String ratingName;
	
	public String getRatingName() {
		return ratingName;
	}

	public void setRatingName(String ratingName) {
		this.ratingName = ratingName;
	}

	public double getRatingVal() {
		return ratingVal;
	}

	public void setRatingVal(double ratingVal) {
		this.ratingVal = ratingVal;
	}

	private double ratingVal;
}