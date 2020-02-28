package com.finlabs.finexa.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MasterdailynavDTO {
	   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "IST")
		private Date date;
	    private double NAV;
	    
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public double getNAV() {
			return NAV;
		}
		public void setNAV(double nAV) {
			NAV = nAV;
		}
	    
	    
	   
	   
}
