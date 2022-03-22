package com.okta.demo.util;

import javax.servlet.http.HttpServletRequest;

public class ContextHelper {
	private static String rootContext = "";
	
	public static void setRootContect(HttpServletRequest request) {
		// Setup the correct URL Root Context
		if ((request.getServerPort() == 80) || (request.getServerPort() == 443))  {
			rootContext =
		        request.getScheme() + "://" +
		        request.getServerName() +
		        request.getContextPath();
		}
		else {
		    rootContext =
		        request.getScheme() + "://" +
		        request.getServerName() + ":" + request.getServerPort() +
		        request.getContextPath();
		}
	}
	
	public static String getRootContect() {
		return rootContext;
	}
}
