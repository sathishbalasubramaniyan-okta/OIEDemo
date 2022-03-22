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

import com.okta.demo.exception.CommunicationException;
import com.okta.demo.exception.InternalException;
import com.okta.demo.obj.User;

/**
 * Utilities to 
 * 1. Get get the associated linked objects for a user
 * 
 * @author mark.smith@okta.com
 *
 */public class OktaLinkedObjUtils {

	private static final Logger logger = LogManager.getLogger(OktaLinkedObjUtils.class.getName());

	/**
	 * Get the logged on users linked objects.
	 * 
	 * @param uid - Users Okta User Id
	 * @param associatedName - Name of the associated linked objects
	 * @param session - Users linked objects added to the session
	 * @throws IOException
	 */
	public static void getUserLinkedObjects(String uid, String associatedName, HttpSession session) throws InternalException, CommunicationException {
		logger.debug("getUserLinkedObjects() - Start");
		logger.debug("uid=" + uid);
		logger.debug("associatedName=" + associatedName);
		ArrayList<User> linkedUsers = new ArrayList<User>();
		ArrayList<String> linkedHrefs = getLinkedUsers(uid, associatedName);
		
		for (int i=0; i<linkedHrefs.size(); i++) {
			String href = (String)linkedHrefs.get(i);
			linkedUsers.add(OktaUserUtils.getUserViaHref(href));
		}
		
        // Load all IDP's in session
        session.setAttribute("linkedUsers", linkedUsers);
		logger.debug("getUserLinkedObjects() - End");
	}
	
	private static ArrayList<String> getLinkedUsers(String uid, String associatedName) {
		logger.debug("getLinkedUsers() - Start");
		ArrayList<String> linkedHrefs = new ArrayList<String>();
		
		// Setup the correct url to retrieve the users linked objects
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + uid + "/linkedObjects/" + associatedName;
		
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
				logger.fatal("Response code when getting linked objects: " + responseCode);
				throw new CommunicationException("Response code when getting linked objects: " + responseCode);
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
			
			JSONArray jArray = new JSONArray(getResponse.toString());
	        int n = jArray.length();
	        int i = 0;
	        while (i < n) {
	        	JSONObject jObj = jArray.getJSONObject(i);
	        	linkedHrefs.add(jObj.getJSONObject("_links").getJSONObject("self").getString("href"));
	            ++i;
	        }
		}
		catch (IOException e) {
			logger.fatal("Caught IOException:" + e.getMessage());
			throw new InternalException("Caught IOException:" + e.getMessage());
		}
		
		logger.debug("getLinkedUsers() - End");
		return linkedHrefs;
	}
}
