package com.okta.demo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.okta.demo.exception.InternalException;
import com.okta.demo.obj.LinkedIdp;

/**
 * Utilities to 
 * 1. Add linked IDP's for an Okta user
 * 2. List linked IDP's for an Okta user
 * 3. Remove linked IDP's for an Okta user
 * 
 * @author mark.smith@okta.com
 *
 */
public class OktaIdpUtils {

	private static final Logger logger = LogManager.getLogger(OktaIdpUtils.class.getName());
	
	/**
	 * Link and external IDP to the passed Okta user
	 * @param idp - Okta IDP Identifier
	 * @param uid - Okta User Identifier
	 * @param externalUid - External User Identifier
	 * @throws IOException
	 */
	public static void addLinkedIdp(String idp, String uid, String externalUid) throws IOException {
		logger.debug("addLinkedIdp() - Start");

		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/idps/" + idp + "/users/" + uid;
		
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");

		// Add Request Headers
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);
		con.setDoOutput(true);
		
        String jsonBody = "{\"externalId\": \"" + externalUid + "\"}";

		final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		bfw.write(jsonBody);
		bfw.flush();
		bfw.close();
		
		logger.debug("Sending 'POST' request to URL : " + url);
		int responseCode = con.getResponseCode();
		logger.debug("Response Code : " + responseCode);

		// TODO - Add exception handling for bad response code
		
		logger.debug("addLinkedIdp() - End");
	}
	
	
	/**
	 * Get a users linked IDPs and load the result in the current session.
	 * @param uid - Okta User Identifier
	 * @param session - HttpSession
	 * @throws IOException
	 */
	public static void getLinkedIdps(String uid, HttpSession session) throws IOException {
		logger.debug("getLinkedIdps() - Start");
		// Setup the correct url to retrieve the passed users linked IDP's
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/users/" + uid + "/idps";
		
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

		// TODO - Add exception handling for bad response code
		
		// Extract the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer getResponse = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			getResponse.append(inputLine);
		}
		in.close();
		
		// If linked IDP's are returned, then extract each IDP
		JSONArray jArray = new JSONArray(getResponse.toString());
		HashMap<String, LinkedIdp> linkedIdps = new HashMap<String, LinkedIdp>();
        int n = jArray.length();
        int i = 0;
        while (i < n) {
        	LinkedIdp linkedIdp = new LinkedIdp();
            JSONObject jObj = jArray.getJSONObject(i);
            linkedIdp.setId(jObj.getString("id"));
            //System.out.println("id =" + linkedIdp.getId());
            linkedIdp.setType(jObj.getString("type"));
            //System.out.println("type =" + linkedIdp.getType());
            linkedIdp.setName(jObj.getString("name"));
            //System.out.println("name =" + linkedIdp.getName());
            linkedIdp.setStatus(jObj.getString("status"));
            //System.out.println("status =" + linkedIdp.getStatus());
            linkedIdp.setCreated(jObj.getString("created"));
            //System.out.println("created =" + linkedIdp.getCreated());
            // Save each IDP
            linkedIdps.put(jObj.getString("name"),linkedIdp);
            ++i;
        }

        // Load all IDP's in session
        session.setAttribute("linkedIdps", linkedIdps);
        
        logger.debug("getLinkedIdps() - End");
	}	
	
	/**
	 * Unlink the passed user from the passed social idp.
	 * @param uid - Okta User Identifier
	 * @param idpName - Social IDP Name
	 * @throws IOException
	 */
	public static void unLinkedIdp(String uid, String idpName) throws IOException {
		logger.debug("unLinkedIdp() - Start");
		String idp = "";
		
		if (idpName.compareToIgnoreCase(PropertiesUtil.google_idp_name) == 0) {
			logger.debug("Unlinking Google IDP for user: " + uid);
			idp = PropertiesUtil.google_idp;
		}
		else if (idpName.compareToIgnoreCase("Lakeside - GSuite") == 0) {
			logger.debug("Unlinking GSuite for user: " + uid);
			idp = PropertiesUtil.gsuite_idp;
		}
		else if (idpName.compareToIgnoreCase("Summerfield College - O365") == 0) {
			logger.debug("Unlinking Microsoft for user: " + uid);
			idp = PropertiesUtil.microsoft_idp;
		}
		else {
			logger.warn("Unsupported IDP: " + idpName);
			// Unsupported IDP
			throw new InternalException("Unlink IDP - Unsupported IDP: " + idpName);
		}
		
		// Setup the correct url to unlink the user from the respective IDP
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/idps/" + idp + "/users/" + uid;
		
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");

		// Add Request Headers
		con.setRequestMethod("DELETE");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Content-type", "application/json");
		con.setRequestProperty("Authorization", "SSWS " + PropertiesUtil.api_key);

		logger.debug("Sending 'DELETE' request to URL : " + url);
		int responseCode = con.getResponseCode();
		logger.debug("Response Code : " + responseCode);
		
		// TODO - Error handling for bad error code
		
		logger.debug("unLinkedIdp() - End");
	}
	
	/**
	 * Retrieves an Okta User Id based on an external User ID
	 * @param idp - Unique ID for configured external IDP
	 * @param externalUserId - External user identifier
	 * @return Okta Username
	 */
	public static String getOktaUsername(String idp, String externalUserId) throws IOException {
		logger.debug("getOktaUserId() - Start");
		String username = "";
		
		// Setup the correct url to retrieve the linked users for the passed idp
		String url = "https://" + PropertiesUtil.okta_org + "/api/v1/idps/" + idp + "/users?expand=user";
		
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

		// Extract the response
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer getResponse = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			getResponse.append(inputLine);
		}
		in.close();
		
		// Get array of users linked to this IDP
		JSONArray jArray = new JSONArray(getResponse.toString());
        int n = jArray.length();
        int i = 0;
        while (i < n) {
        	JSONObject jObj = jArray.getJSONObject(i);
        	logger.debug("externalId=" + jObj.getString("externalId"));
        	if (jObj.getString("externalId").compareTo(externalUserId) == 0) {
        		logger.debug("id=" + jObj.getString("id"));
        		username = jObj.getJSONObject("_embedded").getJSONObject("user").getJSONObject("profile").getString("login");
        		logger.debug("username=" + username);
        		break;
        	}
        	++i;
        }
		
        if (username.length() == 0) {
        	logger.warn("Linked user with external id: " + externalUserId + " Not Found");
        }
        logger.debug("getOktaUserId() - End");
		return username;
	}

}
