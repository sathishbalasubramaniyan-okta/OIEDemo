package com.okta.demo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.okta.demo.exception.CommunicationException;

public class OktaAuthUtils {

	private static final Logger logger = LogManager.getLogger(OktaAuthUtils.class.getName());

	/**
	 * Primary Authentication
	 * @param username
	 * @param password
	 * @return Session Token
	 * @throws IOException
	 */
	public static String authenticateUser(String username, String password) throws IOException {
		logger.debug("getOktaUserId() - Start");
		logger.debug("Authenticating user: " + username);
		
		String sessionToken = "";
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/authn";
		
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");

		// Add Request Headers
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		
		String jsonBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
		logger.debug("jsonBody=" + jsonBody);
			
		final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		bfw.write(jsonBody);
		bfw.flush();
		bfw.close();
		
		logger.debug("Sending 'POST' request to URL : " + url);
		int responseCode = con.getResponseCode();
		logger.debug("Response Code : " + responseCode);

		if (responseCode != 200) {
			logger.warn("Response code not equal 200 !!!");
		}
		
		// Extract the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer getResponse = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			getResponse.append(inputLine);
		}
		in.close();

		JSONObject jObj = new JSONObject(getResponse.toString());
		
		sessionToken = jObj.getString("sessionToken");
		
		logger.debug("getOktaUserId() - End");
		return sessionToken;
	}
	
	public static void getAccessToken(String sessionToken, String redirectUri) throws IOException {
		logger.debug("getAccessToken() - Start");

		String url = "https://" + PropertiesUtil.okta_org + "/oauth2/v1/authorize";
		StringBuilder encodedUrl = new StringBuilder("");
		encodedUrl.append("client_id=" + PropertiesUtil.client_id);
		encodedUrl.append("&redirect_uri=" + redirectUri);
		encodedUrl.append("&response_type=" + "token");
		encodedUrl.append("&response_mode=" + "fragment");
		encodedUrl.append("&state=" + "userprimaryauth");
		encodedUrl.append("&nonce=" + "nonce");
		encodedUrl.append("&display=" + "page");
		encodedUrl.append("&sessionToken=" + sessionToken);
		encodedUrl.append("&scope=" + "openid+email+profile+address+phone");
		
		url += "?" + encodedUrl.toString();
		
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");
		
		// Add Request Headers
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setDoOutput(true);

//		final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
//		bfw.write(encodedUrl.toString());
//		bfw.flush();
//		bfw.close();
		
		logger.debug("Sending 'GET' request to URL : " + url);
		int responseCode = con.getResponseCode();
		logger.debug("Response Code : " + responseCode);

		if (responseCode != 200) {
			logger.warn("Response code not equal 200 !!!");
		}			
		
		// Extract the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer getResponse = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			getResponse.append(inputLine);
		}
		in.close();
		
		logger.debug("Response="+getResponse.toString());
		
		logger.debug("getAccessToken() - End");
	}
	
	public static JSONObject getAccessTokenFromAuthCode(String authCode, String redirect_uri) throws IOException {
		logger.debug("getAccessTokenFromAuthCode() - Start");

		String url = "https://" + PropertiesUtil.okta_org + "/oauth2/";
		url += PropertiesUtil.server_id + "/v1/token";
		StringBuilder encodedUrl = new StringBuilder("");
		encodedUrl.append("grant_type=authorization_code");
		encodedUrl.append("&redirect_uri=" + redirect_uri);
		encodedUrl.append("&code=" + authCode);
		encodedUrl.append("&client_id=" + PropertiesUtil.client_id);
		encodedUrl.append("&client_secret=" + PropertiesUtil.client_secret);
		
		logger.debug("Encoded URL: " + encodedUrl.toString());
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");
		
		// Add Request Headers
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setDoOutput(true);

		final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		bfw.write(encodedUrl.toString());
		bfw.flush();
		bfw.close();
		
		logger.debug("Sending 'POST' request to URL : " + url);
		int responseCode = con.getResponseCode();
		logger.debug("Response Code : " + responseCode);

		if (responseCode != 200) {
			logger.fatal("Response code when getting access token from auth code: " + responseCode);
			throw new CommunicationException("Response code when getting access token from auth code: " + responseCode);
		}			
		
		// Extract the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer getResponse = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			getResponse.append(inputLine);
		}
		in.close();
		
		logger.debug("Response="+getResponse.toString());
		JSONObject jObj = new JSONObject(getResponse.toString());
		logger.debug("getAccessTokenFromAuthCode() - End");
		return jObj;
	}
	
	
    public static void main(final String[] args) throws JSONException{
        try {
        	FileInputStream fis = new FileInputStream("./WebContent/WEB-INF/classes/okta-api.properties");
        	PropertiesUtil.loadProperties(fis);
			String SessionToken = authenticateUser("test1@acme.com", "Okta123!");
			System.out.println("SessionToken="+SessionToken);
			
			getAccessToken(SessionToken, "https://localhost:8443/moe/initialize.html");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   

	
}
