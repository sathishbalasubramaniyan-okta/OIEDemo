package com.okta.demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.okta.demo.obj.UserApp;

/**
 * Utilities to 
 * 1. Get assigned apps for the logged on user
 * 
 * @author mark.smith@okta.com
 *
 */public class OktaAppUtils {

	private static final Logger logger = LogManager.getLogger(OktaAppUtils.class.getName());

	/**
	 * Get the logged on users assigned applications.
	 * 
	 * @param session - Users apps added to the session
	 * @throws IOException
	 */
	public static void getUserAppsOld(HttpSession session) throws IOException {
		logger.debug("getUserApps() - Start");
		
		// Setup the correct url to retrieve the passed users linked IDP's
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/me/home/tabs?expand=items%2Citems.resource";
		
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");

		// Add Request Headers
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-type", "application/json");
		con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);

		int responseCode = con.getResponseCode();
		logger.debug("Sending 'GET' request to URL : " + url);
		logger.debug("Response Code : " + responseCode);

		// TODO - Add exception handling for bad return code
		
		// Extract the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer getResponse = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			getResponse.append(inputLine);
		}
		in.close();
		logger.debug("GET completed ...");
		
		//print result
		//System.out.println(getResponse.toString());
		
		JSONArray jArray = new JSONArray(getResponse.toString());
		// The users apps will be in the first object in the returned array.
		JSONObject jResourceObj = jArray.getJSONObject(0);
		JSONArray jItemsArray = jResourceObj.getJSONObject("_embedded").getJSONArray("items");
		
		ArrayList<UserApp> userApps = new ArrayList<UserApp>();
        int n = jItemsArray.length();
        int i = 0;
        while (i < n) {
        	UserApp userApp = new UserApp();
            JSONObject jObj = jItemsArray.getJSONObject(i);
            userApp.setLabel(jObj.getJSONObject("_embedded").getJSONObject("resource").getString("label"));           
            logger.debug("label =" + userApp.getLabel());
            userApp.setLogoUrl(jObj.getJSONObject("_embedded").getJSONObject("resource").getString("logoUrl"));           
            logger.debug("logoUrl =" + userApp.getLogoUrl());
            userApp.setLinkUrl(jObj.getJSONObject("_embedded").getJSONObject("resource").getString("linkUrl"));           
            logger.debug("linkUrl =" + userApp.getLinkUrl());

            // Save each IDP
            userApps.add(userApp);
            ++i;
        }

        // Load all IDP's in session
        session.setAttribute("userApps", userApps);
		logger.debug("getUserApps() - End");
	}
	
	/**
	 * Get the logged on users assigned applications.
	 * 
	 * @param uid - Users Okta User Id
	 * @param session - Users apps added to the session
	 * @throws IOException
	 */
	public static void getUserApps(String uid, HttpSession session) throws IOException {
		logger.debug("getUserApps() - Start");
		logger.debug("uid=" + uid);
		// Setup the correct url to retrieve the passed users linked IDP's
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + uid + "/appLinks";
		
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");

		// Add Request Headers
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-type", "application/json");
		con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
		con.setDoOutput(true);
		
		int responseCode = con.getResponseCode();
		logger.debug("Sending 'GET' request to URL : " + url);
		logger.debug("Response Code : " + responseCode);

		// TODO - Add exception handling for bad return code
		
		// Extract the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer getResponse = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			getResponse.append(inputLine);
		}
		in.close();
		logger.debug("GET completed ...");
		
		//print result
		//System.out.println(getResponse.toString());
		
		JSONArray jArray = new JSONArray(getResponse.toString());
		
		ArrayList<UserApp> userApps = new ArrayList<UserApp>();
        int n = jArray.length();
        int i = 0;
        while (i < n) {
        	UserApp userApp = new UserApp();
            JSONObject jObj = jArray.getJSONObject(i);
            userApp.setLabel(jObj.getString("label"));           
            logger.debug("label =" + userApp.getLabel());
            userApp.setLogoUrl(jObj.getString("logoUrl"));           
            logger.debug("logoUrl =" + userApp.getLogoUrl());
            userApp.setLinkUrl(jObj.getString("linkUrl"));           
            logger.debug("linkUrl =" + userApp.getLinkUrl());

            // Save each IDP
            userApps.add(userApp);
            ++i;
        }

        // Load all IDP's in session
        session.setAttribute("userApps", userApps);
		logger.debug("getUserApps() - End");
	}
	
}
