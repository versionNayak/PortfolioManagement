package com.finlabs.finexa.dto;

public class PortfolioCurrentSubAssetAllocationPieDataDTO {
	
	private String subAssetAllocationKey;
	private Double subAssetAllocationValue;
	
	public String getSubAssetAllocationKey() {
		return subAssetAllocationKey;
	}
	public void setSubAssetAllocationKey(String subAssetAllocationKey) {
		this.subAssetAllocationKey = subAssetAllocationKey;
	}
	
	public Double getSubAssetAllocationValue() {
		return subAssetAllocationValue;
	}
	public void setSubAssetAllocationValue(Double subAssetAllocationValue) {
		this.subAssetAllocationValue = subAssetAllocationValue;
	}
	
	@Override
	public String toString() {
		return "PortfolioCurrentSubAssetAllocationPieDataDTO [subAssetAllocationKey=" + subAssetAllocationKey
				+ ", subAssetAllocationValue=" + subAssetAllocationValue + "]";
	}
}
