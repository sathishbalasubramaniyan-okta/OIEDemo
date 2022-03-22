package com.okta.demo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.okta.demo.exception.CommunicationException;
import com.okta.demo.exception.ConfigurationException;
import com.okta.demo.exception.InternalException;

/**
 * Utilities to 
 * 1. Enroll SMS Factor
 * 2. Activate SMS factor
 * 
 * @author mark.smith@okta.com
 *
 */
public class OktaMfaUtils {
	private static final Logger logger = LogManager.getLogger(OktaMfaUtils.class.getName());

	public static String enrollSMS(String id, String mobilePhone) throws InternalException, CommunicationException, ConfigurationException {
		logger.debug("enrollSMS() - Start");
		logger.debug("Passed id = " + id);
		if (id == null || id.length() == 0) {
			logger.fatal("The passed user id is null or empty");
			throw new ConfigurationException("The passed user id is null or empty");
		}
		logger.debug("Passed mobilePhone = " + mobilePhone);
		if (mobilePhone == null || mobilePhone.length() == 0) {
			logger.fatal("The passed mobilePhone is null or empty");
			throw new ConfigurationException("The passed mobilePhone is null or empty");
		}
		
		// Set up the country code if not already set
		if (mobilePhone.startsWith("0")) {
			mobilePhone = "+61-" + mobilePhone.substring(1, mobilePhone.length());
		}

		String activateURL = "";
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + id + "/factors";
		
		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");
	
			// Add Request Headers
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);

			String jsonBody = "{\"factorType\": \"sms\",\"provider\": \"OKTA\",\"profile\": {\"phoneNumber\": \"" + mobilePhone + "\" }}";
			
			final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			bfw.write(jsonBody);
			bfw.flush();
			bfw.close();
			
			logger.debug("Sending 'POST' request to URL : " + url);
			int responseCode = con.getResponseCode();
			logger.debug("Response Code : " + responseCode);

			// Ensure we got a 200
			if (responseCode != 200) {
				logger.fatal("Response code when getting user: " + responseCode);
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
			System.out.println(getResponse.toString());			
		
			// Load the user object
			JSONObject jObj = new JSONObject(getResponse.toString());
			try {
				activateURL = jObj.getJSONObject("_links").getJSONObject("activate").getString("href");
				logger.debug("Returned Activate URL = " + activateURL);
			} 
			catch (Exception e) {
				logger.fatal("Failed to extract id from user profile: " + e.getMessage());
				throw new InternalException("Failed to extract id from user profile: " + e.getMessage());
			}			
			
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
		catch (JSONException e) {
			logger.fatal("Caught JSONException:" + e.getMessage());
			throw new InternalException("Caught JSONException:" + e.getMessage());
		}
		
		logger.debug("enrollSMS() - End");
		return activateURL;
	}
	
	
	public static String enrollEmail(String id, String email) throws InternalException, CommunicationException, ConfigurationException {
		logger.debug("enrollEmail() - Start");
		logger.debug("Passed id = " + id);
		if (id == null || id.length() == 0) {
			logger.fatal("The passed user id is null or empty");
			throw new ConfigurationException("The passed user id is null or empty");
		}
		logger.debug("Passed Email = " + email);
		if (email == null || email.length() == 0) {
			logger.fatal("The passed email is null or empty");
			throw new ConfigurationException("The passed email is null or empty");
		}
		
		String activateURL = "";
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + id + "/factors?tokenLifetimeSeconds=86400";
		
		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");
	
			// Add Request Headers
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);

			String jsonBody = "{\"factorType\": \"email\",\"provider\": \"OKTA\",\"profile\": {\"email\": \"" + email + "\" }}";
			
			final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			bfw.write(jsonBody);
			bfw.flush();
			bfw.close();
			
			logger.debug("Sending 'POST' request to URL : " + url);
			int responseCode = con.getResponseCode();
			logger.debug("Response Code : " + responseCode);

			// Ensure we got a 200
			if (responseCode != 200) {
				logger.fatal("Response code when getting user: " + responseCode);
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
			System.out.println(getResponse.toString());			
		
			// Load the user object
			JSONObject jObj = new JSONObject(getResponse.toString());
			try {
				activateURL = jObj.getJSONObject("_links").getJSONObject("activate").getString("href");
				logger.debug("Returned Activate URL = " + activateURL);
			} 
			catch (Exception e) {
				logger.fatal("Failed to extract id from user profile: " + e.getMessage());
				throw new InternalException("Failed to extract id from user profile: " + e.getMessage());
			}			
			
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
		catch (JSONException e) {
			logger.fatal("Caught JSONException:" + e.getMessage());
			throw new InternalException("Caught JSONException:" + e.getMessage());
		}
		
		logger.debug("enrollEmail() - End");
		return activateURL;
	}
	
	
	public static void activateSMS(String activateURL, String passCode) throws InternalException, CommunicationException, ConfigurationException {
		logger.debug("activateSMS() - Start");
		logger.debug("activateURL = " + activateURL);
		if (activateURL == null || activateURL.length() == 0) {
			logger.fatal("The passed activateURL is null or empty");
			throw new ConfigurationException("The passed activateURL is null or empty");
		}
		logger.debug("Passed passCode = " + passCode);
		if (passCode == null || passCode.length() == 0) {
			logger.fatal("The passed passCode is null or empty");
			throw new ConfigurationException("The passed passCode is null or empty");
		}
	
		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(activateURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");

			// Add Request Headers
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);

			String jsonBody = "{\"passCode\": \"" + passCode + "\"}";	
			
			final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			bfw.write(jsonBody);
			bfw.flush();
			bfw.close();
			
			logger.debug("Sending 'POST' request to URL : " + activateURL);
			int responseCode = con.getResponseCode();
			logger.debug("Response Code : " + responseCode);

			// Ensure we got a 200
			if (responseCode != 200) {
				logger.fatal("Response code when getting user: " + responseCode);
				throw new CommunicationException("Response code when getting user: " + responseCode);
			}
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
			
		logger.debug("activateSMS() - End");
	}
	
	public static void activateEmail(String activateURL, String passCode) throws InternalException, CommunicationException, ConfigurationException {
		logger.debug("activateEmail() - Start");
		logger.debug("activateURL = " + activateURL);
		if (activateURL == null || activateURL.length() == 0) {
			logger.fatal("The passed activateURL is null or empty");
			throw new ConfigurationException("The passed activateURL is null or empty");
		}
		logger.debug("Passed passCode = " + passCode);
		if (passCode == null || passCode.length() == 0) {
			logger.fatal("The passed passCode is null or empty");
			throw new ConfigurationException("The passed passCode is null or empty");
		}
	
		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(activateURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");

			// Add Request Headers
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);

			String jsonBody = "{\"passCode\": \"" + passCode + "\"}";	
			
			final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			bfw.write(jsonBody);
			bfw.flush();
			bfw.close();
			
			logger.debug("Sending 'POST' request to URL : " + activateURL);
			int responseCode = con.getResponseCode();
			logger.debug("Response Code : " + responseCode);

			// Ensure we got a 200
			if (responseCode != 200) {
				logger.fatal("Response code when getting user: " + responseCode);
				throw new CommunicationException("Response code when getting user: " + responseCode);
			}
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
			
		logger.debug("activateEmail() - End");
	}
	
	public static JSONArray listEnrolledFactors(String id) throws InternalException, CommunicationException, ConfigurationException {
		logger.debug("listEnrolledFactors() - Start");
		logger.debug("uid = " + id);
		if (id == null || id.length() == 0) {
			logger.fatal("The passed uid is null or empty");
			throw new ConfigurationException("The passed uid is null or empty");
		}
		
	
		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + id + "/factors";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");

			// Add Request Headers
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);

			
			logger.debug("Sending 'GET' request to URL : " + url);
			int responseCode = con.getResponseCode();
			logger.debug("Response Code : " + responseCode);

			// Ensure we got a 200
			if (responseCode != 200) {
				logger.fatal("Response code when getting enrolled factors: " + responseCode);
				throw new CommunicationException("Response code when getting enrolled factors: " + responseCode);
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
			logger.debug(getResponse.toString());			
		
			JSONArray jObj = new JSONArray(getResponse.toString());
			logger.debug("listEnrolledFactors() - End");
			return jObj;
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
			
		
		
	}
	
	public static void main(String[] args) {
    	try {
			FileInputStream fis = new FileInputStream("./WebContent/WEB-INF/classes/" + "okta-api.properties");
			PropertiesUtil.loadProperties(fis);
		} catch (FileNotFoundException e) {
			logger.fatal("Failed to load property file: " + "okta-api.properties" + " cause: " + e.getMessage());
			throw new InternalException("Failed to load property file: " + "okta-api.properties" + " cause: " + e.getMessage());
		}
		//OktaMfaUtils.enrollSMS("00u3r943g9AXz6okZ2p7", "0434184367");
		OktaMfaUtils.activateSMS("https://oktaapac.okta.com/api/v1/users/00u3r943g9AXz6okZ2p7/factors/mbl3r93vagheln6bH2p7/lifecycle/activate", "040697");
	}

}
