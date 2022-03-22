package com.okta.demo.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.okta.demo.exception.CommunicationException;
import com.okta.demo.exception.InternalException;
import com.okta.demo.obj.SystemLogEvent;

/**
 * Utilities to 
 * 1. Get system log events by type and date
 * 
 * @author mark.smith@okta.com
 *
 */public class OktaSystemLogUtils {

	private static final Logger logger = LogManager.getLogger(OktaSystemLogUtils.class.getName());

	/**
	 * Get system log events by type and date
	 * 
	 * @param published - From Date in format 2019-04-21T02:00:00.000Z
	 * @param event - Event type eg. security.request.blocked
	 * @throws InternalException CommunicationException
	 */
	public static ArrayList<SystemLogEvent> getSystemLogEvent(String published, String event) throws InternalException, CommunicationException {
		logger.debug("getSystemLogEvent() - Start");
		logger.debug("Searching System Log for events of type " + event + " from " + published);

		SystemLogEvent systemLogEvent;
		ArrayList<SystemLogEvent> logEventArray = new ArrayList<SystemLogEvent>();
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/logs?since=" + published + "&filter=eventType+eq+%22" + event + "%22";

		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");
	
			// Add Request Headers
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);	
			con.setDoOutput(true);
						
			int responseCode = con.getResponseCode();
			logger.debug("Sending 'GET' request to URL : " + url);
			logger.debug("Response Code : " + responseCode);

			// Ensure we got a 200
			if (responseCode != 200) {
				logger.fatal("Response code when getting user: " + responseCode);
				logger.fatal("Response message: " + con.getResponseMessage());
				throw new CommunicationException("Response code when getting user: " + responseCode);
			}

			// Extract the response
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer getResponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				getResponse.append(inputLine);
			}
			in.close();
			logger.debug("GET completed ...");			
			
			//DEBUG ONLY
			//System.out.println(getResponse.toString());			

			// Get array of System Log Events
			JSONArray jArray = new JSONArray(getResponse.toString());
	        int n = jArray.length();
	        logger.debug("Found " + n + " System Log Events of type " + event);
	        int i = 0;
	        while (i < n) {
	        	JSONObject jObj = jArray.getJSONObject(i);
	        	systemLogEvent = new SystemLogEvent();
	        	try {
	        		//logger.debug("displayMessage=" + jObj.getString("displayMessage"));
	        		systemLogEvent.setdisplayMessage(jObj.getString("displayMessage"));
	        	} catch(Exception e) {}
	        	try {
		        	//logger.debug("eventType=" + jObj.getString("eventType"));
		        	systemLogEvent.setEventType(jObj.getString("eventType"));
	        	} catch(Exception e) {}
	        	try {
		        	//logger.debug("Result=" + jObj.getJSONObject("outcome").getString("result"));
		        	systemLogEvent.setResult(jObj.getJSONObject("outcome").getString("result"));
	        	} catch(Exception e) {}
	        	try {		        	
		        	//logger.debug("Reason=" + jObj.getJSONObject("outcome").getString("reason"));
		        	systemLogEvent.setReason(jObj.getJSONObject("outcome").getString("reason"));
	        	} catch(Exception e) {}
	        	try {		        	
		        	//logger.debug("published=" + jObj.getString("published"));
		        	systemLogEvent.setPublished(jObj.getString("published"));
	        	} catch(Exception e) {}
	        	try {		        	
		        	//logger.debug("User=" + jObj.getJSONObject("actor").getString("alternateId"));
		        	systemLogEvent.setReason(jObj.getJSONObject("actor").getString("alternateId"));
	        	} catch(Exception e) {}
	        	try {
		        	//logger.debug("Ip=" + jObj.getJSONObject("client").getString("ipAddress"));
		        	systemLogEvent.setReason(jObj.getJSONObject("client").getString("ipAddress"));
	        	} catch(Exception e) {}
	
	        	logEventArray.add(systemLogEvent);
	        	++i;
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	        
	    logger.debug("getSystemLogEvent() - End");
		return logEventArray;
	}
	
	public static void main(String[] args) {
    	try {
			FileInputStream fis = new FileInputStream("./WebContent/WEB-INF/classes/" + "okta-api.properties");
			PropertiesUtil.loadProperties(fis);
		} catch (FileNotFoundException e) {
			logger.fatal("Failed to load property file: " + "okta-api.properties" + " cause: " + e.getMessage());
			throw new InternalException("Failed to load property file: " + "okta-api.properties" + " cause: " + e.getMessage());
		}

    	OktaSystemLogUtils.getSystemLogEvent("2019-04-29T00:00:00.000Z", "security.request.blocked");
		//OktaSysyemLogUtils.getSecurityRequestBlocked("https://oktaapac.okta.com/api/v1/logs?since=OktaSystemLogUtilsZ&filter=eventType+eq+%22security.request.blocked%22");
	}
}
