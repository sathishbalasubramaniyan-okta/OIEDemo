package com.okta.demo.util;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateTimeUtils {

	/**
	 * Get Todays Date
	 * @return
	 */
	public static String getCurrentDate() {
		String currentDate = FastDateFormat.getInstance("yyyy-MM-dd").format(System.currentTimeMillis( ));
		return currentDate;
	}
	
	/**
	 * Get Todays Date and Time
	 * @return
	 */
	public static String getCurrentDateTime() {
		String currentDate = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis( ));
		return currentDate.replaceFirst(" ", "T");
	}

	/**
	 * min - Subtract Minutes from current Date and Time
	 * Get Todays Date and Time
	 * @return yyyy-MM-ddTHH:mm:ss
	 */
	public static String getCurrentDateTime(int min) {
		long newTime = System.currentTimeMillis( ) - (min * 60 * 1000);
		String currentDate = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(newTime);
		return currentDate.replaceFirst(" ", "T");
	}

	/**
	 * For testing only
	 */
	public static void main(String[] args) {
		System.out.println(getCurrentDateTime(10));
	}

}
