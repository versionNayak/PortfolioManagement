package com.finlabs.finexa.pm.util;

import java.text.DecimalFormat;
import java.util.stream.IntStream;

public class PortfolioReportUtil {
	
	public static String convertNullToEmpty(String name) {
		return (name == null) ? "" : name;
	}
	
	// Method to convert the string
	public static String capitailizeWord(String str) {
		StringBuilder s = new StringBuilder();

		// Declare a character of space
		// To identify that the next character is the starting
		// of a new word
		char ch = ' ';
		for (int i = 0; i < str.length(); i++) {

			// If previous character is space and current
			// character is not space then it shows that
			// current letter is the starting of the word
			if (ch == ' ' && str.charAt(i) != ' ')
				s.append(Character.toUpperCase(str.charAt(i)));
			else
				s.append(str.charAt(i));
			ch = str.charAt(i);
		}

		// Return the string with trimming
		return s.toString().trim();
	}
	
	public static double roundOff(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.000");
		df.setMaximumFractionDigits(places);
		df.setMinimumFractionDigits(0);
		String valueString = df.format(value);
		value = Double.parseDouble(valueString);
		return value;
	}
	
	public static Integer findIndexOfdoubleArr(double arr[], double t) {
		int len = arr.length; 
        return IntStream.range(0, len) 
            .filter(i -> t == arr[i]) 
            .findFirst()
            .orElse(-1);
	}
	
	public static Integer findIndexOfDoubleArr(Double arr[], Double t) { 
        int len = arr.length; 
        return IntStream.range(0, len) 
            .filter(i -> t == arr[i]) 
            .findFirst()
            .orElse(-1);
    } 
	
	public static Integer findIndexOfStringArr(String arr[], String t) {
		int len = arr.length;
		return IntStream.range(0, len)
				.filter(i -> t == arr[i])
				.findFirst()
				.orElse(-1);
	}

}
