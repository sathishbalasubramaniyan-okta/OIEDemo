package com.okta.demo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


import com.okta.demo.exception.CommunicationException;
import com.okta.demo.exception.ConfigurationException;
import com.okta.demo.exception.InternalException;
import com.okta.demo.obj.User;

/**
 * Utilities to 
 * 1. Get get the user profile via a href
 * 2. Create use without password
 * 
 * @author mark.smith@okta.com
 *
 */
public class OktaUserUtils {

	private static final Logger logger = LogManager.getLogger(OktaUserUtils.class.getName());

	/**
	 * Get the user profile via a href
	 * 
	 * @param href - Fully formatted href to retrieve the users profile
	 * @throws InternalException CommunicationException
	 */
	public static User getUserViaHref(String href) throws InternalException, CommunicationException {
		logger.debug("getUserViaHref() - Start");
		logger.debug("href=" + href);
		User user = new User();

		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(href);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");
	
			// Add Request Headers
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);
			
			int responseCode = con.getResponseCode();
			logger.debug("Sending 'GET' request to URL : " + href);
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
				user.setId(jObj.getString("id"));
			} 
			catch (Exception e) {
				logger.fatal("Failed to extract id from user profile: " + e.getMessage());
				throw new InternalException("Failed to extract id from user profile: " + e.getMessage());
			}
			
			// username
			try {
				user.setUsername(jObj.getJSONObject("profile").getString("login"));
			} catch (Exception e) {}
			
			// displayName
			try {
				user.setDisplayName(jObj.getJSONObject("profile").getString("displayName"));
			} catch (Exception e) {
				try {
					user.setDisplayName(jObj.getJSONObject("profile").getString("firstName") + " " + jObj.getJSONObject("profile").getString("lastName"));
				}
				catch (Exception e2) {logger.warn("Failed to extract displayName from user profile");}
			}
			
			// mobilePhone
			try {
				user.setMobilePhone(jObj.getJSONObject("profile").getString("mobilePhone"));
			} catch (Exception e) {logger.warn("Failed to extract mobilePhone from user profile");}
			
			// email
			try {
				user.setEmail(jObj.getJSONObject("profile").getString("email"));
			} catch (Exception e) {logger.warn("Failed to extract email from user profile");}
			
			// propertyAddress
			try {
				user.setPropertyAddress(jObj.getJSONObject("profile").getString("propertyAddress"));
			} catch (Exception e) {logger.warn("Failed to extract propertyAddress from user profile");}
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
		catch (JSONException e) {
			logger.fatal("Caught JSONException:" + e.getMessage());
			throw new InternalException("Caught JSONException:" + e.getMessage());
		}
			
		logger.debug("getUserViaHref() - End");
		return user;
	}
	
	
	
	public static User getUserByNameOrEmail(String attrValue) throws InternalException, CommunicationException {
		logger.debug("getUserFromAttribute() - Start");
		
		User user = new User();

		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users?q=" + attrValue;
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");
	
			// Add Request Headers
			con.setRequestMethod("GET");
			
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);
			
			int responseCode = con.getResponseCode();
			logger.debug("Sending 'GET' request to URL : " + url);
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
			JSONArray jArr = new JSONArray(getResponse.toString());
			JSONObject jObj = jArr.getJSONObject(0);
			
			user.setId(jObj.getString("id"));
			user.setUsername(jObj.getJSONObject("profile").getString("login"));			
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
		catch (JSONException e) {
			logger.fatal("Caught JSONException:" + e.getMessage());
			throw new InternalException("Caught JSONException:" + e.getMessage());
		}
			
		logger.debug("getUserFromAttribute() - End");
		return user;
	}
	
	/**\
	 * Create a user without a password
	 * @param emailAddress - Mandatory
	 * @param firstName - Mandatory
	 * @param lastName - Mandatory
	 * @param mobileNumber - Optional
	 * @param secondEmail - Optional
	 * @return Created Users Okta Id
	 */
	public static String createUserWithoutPassword(String emailAddress, String username) {
		logger.debug("createUserWithoutPassword() - Start");
		String id = "";
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users?activate=false";
		
		logger.debug("emailAddress = " + emailAddress);
		if (emailAddress == null || emailAddress.length() == 0) {
			logger.fatal("The passed emailAddress is null or empty");
			throw new ConfigurationException("The passed emailAddress is null or empty");
		}		
				
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
			
			String jsonBody = "{\"profile\": {\"email\": \"" + emailAddress + 					
					"\", \"login\": \"" + username + 
					"\"}}";

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
				id = jObj.getString("id");
				logger.debug("Returned User Id = " + id);
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
			
		logger.debug("createUserWithoutPassword() - End");
		
		return id;
	}
	
	public static String createUserWithPassword(String emailAddress, String username, String password) {
		logger.debug("createUserWithPassword() - Start");
		String id = "";
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users?activate=true";
		
		logger.debug("emailAddress = " + emailAddress);
		if (emailAddress == null || emailAddress.length() == 0) {
			logger.fatal("The passed emailAddress is null or empty");
			throw new ConfigurationException("The passed emailAddress is null or empty");
		}
		
		if (username == null || username.length() == 0) {
			logger.fatal("The passed username is null or empty");
			throw new ConfigurationException("The passed username is null or empty");
		}
		
		if (password == null || password.length() == 0) {
			logger.fatal("The passed password is null or empty");
			throw new ConfigurationException("The passed password is null or empty");
		}
		
				
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
			
			String jsonBody = "{\"profile\": {\"email\": \"" + emailAddress + 					
					"\", \"login\": \"" + username + 
					"\"},\"credentials\": {\"password\":{\"value\":\"" + password + 
					"\" }}}";
			System.out.println("CreateUserWithPassword JSON body: " + jsonBody);

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
				id = jObj.getString("id");
				logger.debug("Returned User Id = " + id);
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
			
		logger.debug("createUserWithPassword() - End");
		
		return id;
	}
	
	public static void activateUser(String id, String sendEmail) {
		logger.debug("activateUser() - Start");		
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + id + "/lifecycle/activate?sendEmail=" + sendEmail;		
		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");
	
			// Add Request Headers
			con.setRequestMethod("POST");			
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);
			
			logger.debug("Sending 'POST' request to URL : " + url);
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
		catch (JSONException e) {
			logger.fatal("Caught JSONException:" + e.getMessage());
			throw new InternalException("Caught JSONException:" + e.getMessage());
		}
			
		logger.debug("activateUser() - End");

	}
	
	public static void setPassword(String id, String password) {
		logger.debug("setPassword() - Start");

		logger.debug("id = " + id);
		if (id == null || id.length() == 0) {
			logger.fatal("The passed user id is null or empty");
			throw new ConfigurationException("The passed user id is null or empty");
		}
		if (password == null || password.length() == 0) {
			logger.fatal("The passed password is null or empty");
			throw new ConfigurationException("The passed password is null or empty");
		}
		
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + id;

		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");
	
			// Add Request Headers
			con.setRequestMethod("PUT");
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
			con.setDoOutput(true);
			
			String jsonBody = "{\"credentials\": {\"password\" : { \"value\": \"" + password + "\" }}}";
					
			final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			bfw.write(jsonBody);
			bfw.flush();
			bfw.close();
			
			logger.debug("Sending 'PUT' request to URL : " + url);
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
	
		logger.debug("setPassword() - End");
	}
	
	
	public static void updateUser(User user) {
		logger.debug("updateUser() - Start");
		
		String id = user.getId();
		String username = user.getUsername();
		String email = user.getEmail();
		String emailVerified = user.getEmailVerified();
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + id;

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
			
			String jsonBody = "{\"profile\": {\"login\" :\"" + username + "\",\"email\": \"" + email + "\",\"mobilePhone\": \"" + username + "\",\"emailVerified\":\"" + emailVerified + "\"}}";
					
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

		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
	
		logger.debug("updateUser() - End");
	}
	
	public static String authenticateUser(String login, String password) {
		logger.debug("authenticateUser() - Start");
		String sessionToken = "";
		if (login == null || login.length() == 0) {
			logger.fatal("The passed user login is null or empty");
			throw new ConfigurationException("The passed user login is null or empty");
		}
		if (password == null || password.length() == 0) {
			logger.fatal("The passed password is null or empty");
			throw new ConfigurationException("The passed password is null or empty");
		}

		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/authn";

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
			con.setDoOutput(true);
			
			String jsonBody = "{\"username\": \"" + login + "\",\"password\": \"" + password + "\"}";
					
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
			logger.debug("POST completed ...");			
			
			//DEBUG ONLY
			System.out.println(getResponse.toString());			
		
			// Load the user object
			JSONObject jObj = new JSONObject(getResponse.toString());
			try {
				sessionToken = jObj.getString("sessionToken");
				logger.debug("Returned sessionToken = " + sessionToken);
			} 
			catch (Exception e) {
				logger.fatal("Failed to extract sessionToken from auth response: " + e.getMessage());
				throw new InternalException("Failed to extract id from auth response: " + e.getMessage());
			}			
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
		
		logger.debug("authenticateUser() - End");
		
		return sessionToken;
	}
	
	/**
	 * GET https://oktaapac.okta.com/oauth2/aus2irqydkmdUESVW2p7/v1/authorize?
	 * client_id=0oa2ir27iipaJYxM42p7&
	 * redirect_uri=https%3A%2F%2Foktaapac.okta.com%2Fapp%2FUserHome&
	 * response_type=token&
	 * response_mode=fragment&
	 * state=Ob176uW3VkFY0bLUFJZuLlFDHK2sFqbPLT2ZtwpsV3j4uGNCpJ6YAAjsoNpRVJSa&
	 * nonce=cjs3deBeF0SItvnuZ6JUTE5N0gGMXDYLsipVQ1iyAU57bA1W88jnNflWh4UsMZFc&display=page&
	 * sessionToken=20111DE4IR8l_Sb3N01SCdSasCB0IilYO2BC9pyVFlDZ0j4Ecn5Vkyd&
	 * scope=openid%20email%20profile%20address%20phone
	 * @throws UnsupportedEncodingException 
	 */
	public static void getAccessToken(String redirectURI, String sessionToken)  {
		logger.debug("getAccessToken() - Start");
		
		logger.debug("redirectURI = " + redirectURI);
		if (redirectURI == null || redirectURI.length() == 0) {
			logger.fatal("The passed redirectURI is null or empty");
			throw new ConfigurationException("The passed redirectURI is null or empty");
		}
		logger.debug("sessionToken = " + sessionToken);
		if (sessionToken == null || sessionToken.length() == 0) {
			logger.fatal("The passed sessionToken is null or empty");
			throw new ConfigurationException("The passed sessionToken is null or empty");
		}

		String url = "https://" + PropertiesUtil.okta_org + "/oauth2/";
		url += PropertiesUtil.server_id + "/v1/authorize";
		url += "?client_id=" + PropertiesUtil.client_id;
		try {
			url += "&redirect_uri=" + URLEncoder.encode(redirectURI, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		url += "&response_type=token";
		url += "&response_mode=fragment";		
		url += "&state=state";
		url += "&nonce=nonce";
		url += "&sessionToken=" + sessionToken;
		url += "&scope=openid%20profile";
	
		try {
			// Establish the Connection
			logger.debug("About to establish connection to server ...");
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			logger.debug("Connection established with server ...");
	
			// Add Request Headers
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			con.setDoOutput(true);
			
			int responseCode = con.getResponseCode();
			logger.debug("Sending 'GET' request to URL : " + url);
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

		logger.debug("getAccessToken() - End");
	}	
	
	/**
	 * For testing only
	 */
	public static void main(String[] args) {
    	try {
			FileInputStream fis = new FileInputStream("./WebContent/WEB-INF/classes/" + "okta-api.properties");
			PropertiesUtil.loadProperties(fis);
		} catch (FileNotFoundException e) {
			logger.fatal("Failed to load property file: " + "okta-api.properties" + " cause: " + e.getMessage());
			throw new InternalException("Failed to load property file: " + "okta-api.properties" + " cause: " + e.getMessage());
		}
		//OktaUserUtils.createUserWithoutPassword("isaac@racq.com.au", "Isaac", "Brock", "0434184367");
		//OktaUserUtils.setPassword("00u3r943g9AXz6okZ2p7", "Okta123!");
    	//OktaUserUtils.authenticateUser("isaac@racq.com.au", "Okta123!");
    	OktaUserUtils.getAccessToken("https://oktaapac.okta.com/app/UserHome", "20111DE4IR8l_Sb3N01SCdSasCB0IilYO2BC9pyVFlDZ0j4Ecn5Vkyd");
	}
	
}
