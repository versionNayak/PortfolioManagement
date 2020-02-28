package com.finlabs.finexa.dto;

public class ProductRecommendationForReportDTO {
	
	private String subAssetClass;
	private String recommendedProduct;
	private String productAllocationPercentage;
	private String lumpsumAmount;
	
	public String getSubAssetClass() {
		return subAssetClass;
	}
	public void setSubAssetClass(String subAssetClass) {
		this.subAssetClass = subAssetClass;
	}
	
	public String getRecommendedProduct() {
		return recommendedProduct;
	}
	public void setRecommendedProduct(String recommendedProduct) {
		this.recommendedProduct = recommendedProduct;
	}
	
	public String getProductAllocationPercentage() {
		return productAllocationPercentage;
	}
	public void setProductAllocationPercentage(String productAllocationPercentage) {
		this.productAllocationPercentage = productAllocationPercentage;
	}
	
	public String getLumpsumAmount() {
		return lumpsumAmount;
	}
	public void setLumpsumAmount(String lumpsumAmount) {
		this.lumpsumAmount = lumpsumAmount;
	}
	
	@Override
	public String toString() {
		return "ProductRecommendationForReportDTO [subAssetClass=" + subAssetClass + ", recommendedProduct="
				+ recommendedProduct + ", productAllocationPercentage=" + productAllocationPercentage
				+ ", lumpsumAmount=" + lumpsumAmount + "]";
	}
}
