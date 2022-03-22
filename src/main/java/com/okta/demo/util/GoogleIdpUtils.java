package com.okta.demo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * 1. Retrieve Google Token with Authentication Code
 * 2. Extract the Users Google Id from the ID Token
 * 
 * @author marksmith
 *
 */
public class GoogleIdpUtils {

	private static final Logger logger = LogManager.getLogger(GoogleIdpUtils.class.getName());

	/**
	 * Retrieve a Google Token using an Authentication Code
	 * @param googleCode
	 * @param googleClientId
	 * @param googleClientSecret
	 * @param redirectUri
	 * @return JSONObject - Parsed Google Token Response
	 * @throws IOException
	 */
	public static JSONObject retrieveGoogleToken(String googleCode, String googleClientId, String googleClientSecret, String redirectUri) throws IOException {
		logger.debug("retrieveGoogleToken() - Start");
		logger.debug("Passed googleCode = " + googleCode);
		logger.debug("Passed redirectUri = " + redirectUri);
		// Setup the correct url to retrieve the passed users linked IDP's
		String url = "https://www.googleapis.com/oauth2/v4/token";
		
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");

		// Add Request Headers
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setDoOutput(true);
		
		StringBuilder encodedUrl = new StringBuilder("");
		encodedUrl.append("code=" + googleCode);
		encodedUrl.append("&client_id=" + googleClientId);
		encodedUrl.append("&client_secret=" + googleClientSecret);
		encodedUrl.append("&redirect_uri=" + redirectUri);
		encodedUrl.append("&grant_type=" + "authorization_code");

		final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
		bfw.write(encodedUrl.toString());
		bfw.flush();
		bfw.close();
		
		logger.debug("Sending 'POST' request to URL : " + url);
		int responseCode = con.getResponseCode();
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

		logger.debug("Response=" + getResponse.toString());
		JSONObject jObj = new JSONObject(getResponse.toString());
		
		logger.debug("retrieveGoogleToken() - End");
		return jObj;
	}
	
	/**
	 * Extracts the users google id from a google token response
	 * @param jObj - Parsed Google Token Response
	 * @return
	 */
	public static String extractGoogleUserId(JSONObject jObj) {
		logger.debug("extractGoogleUserId() - Start");
		
		// TODO Add exception handling for parse error
		
		//System.out.println("Google Token Response = " + JsonFormatter.format(jObj));
		String idToken = jObj.getString("id_token");
		//System.out.println("idToken = " + idToken);
		JSONObject idTokenJsonObj = JwtParser.parseJWT(idToken);
		//System.out.println("Google ID Token = " + JsonFormatter.format(idTokenJsonObj));
		String sub = idTokenJsonObj.getString("sub");
		logger.debug("Google User ID = " + sub);
		
		logger.debug("extractGoogleUserId() - End");

		return sub;
	}

}
