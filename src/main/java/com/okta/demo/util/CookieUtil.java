package com.okta.demo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CookieUtil {
	private static final Logger logger = LogManager.getLogger(CookieUtil.class.getName());
	
	public static Cookie getCookie(HttpServletRequest request, String name) {
		logger.debug("getCookie() - Start");
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals(name)) {
	        		logger.debug("getCookie() - End");
	                return cookie;
	            }
	        }
	    }
		logger.warn("Cookie not found - Returning null" );
	    return null;
	}
	
	public static boolean containsCookie(HttpServletRequest request, String name) {
		logger.debug("containsCookie() - Start");
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals(name)) {
	            	logger.debug("containsCookie() - End");
	                return true;
	            }
	        }
	    }
		logger.warn("Cookie not found - Returning false" );
	    return false;
	}

	public static String getCookieValue(HttpServletRequest request, String name) {
		logger.debug("getCookieValue() - Start");
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals(name)) {
	            	logger.debug("getCookieValue() - End");
	                return cookie.getValue();
	            }
	        }
	    }
		logger.warn("Cookie not found - Returning null" );
	    return null;
	}

	public static void addCookie(HttpServletResponse response, String name, String value) {
		logger.debug("addCookie() - Start");
		logger.debug("Cookie name = " + name);
		Cookie cookie = new Cookie(name, value);
		//add some description to be viewed in browser cookie viewer
		cookie.setComment(name);
		//setting max age to be 1 day
		cookie.setMaxAge(24*60*60);
		//adding cookie to the response
		response.addCookie(cookie);
		logger.debug("addCookie() - End");
	}
		
	public static void eraseCookie(HttpServletResponse response, String name) {
		logger.debug("eraseCookie() - Start");
		Cookie cookie = new Cookie(name, "");
		//setting max age to be zero
		cookie.setMaxAge(0);
		//adding cookie to the response
		response.addCookie(cookie);
		logger.debug("eraseCookie() - End");
	}
	
	public static void invalidateSession(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("invalidateSession() - Start");
		 
	    response.setContentType("text/html");
	 
	    Cookie[] cookies = request.getCookies();
	 
	    // Delete all the cookies
	    if (cookies != null) {
	 
	        for (int i = 0; i < cookies.length; i++) {
	 
	            Cookie cookie = cookies[i];
	            cookies[i].setValue(null);
	            cookies[i].setMaxAge(0);
	            response.addCookie(cookie);
	        }
	    }
		logger.debug("invalidateSession() - End");
	}
	
	public static void displayCookies(HttpServletRequest request) {
		logger.debug("displayCookies() - Start");
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	        	logger.debug("Cookie Name: " + cookie.getName());
	        	logger.debug("Cookie Value: " + cookie.getValue());	            
	        }
	    }
		logger.debug("displayCookies() - End");
	}

}
