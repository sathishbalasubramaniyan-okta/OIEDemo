package com.okta.demo.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import javax.net.ssl.HttpsURLConnection;


import org.json.JSONException;
import org.json.JSONObject;

import com.okta.demo.exception.CommunicationException;
import com.okta.demo.exception.ConfigurationException;

public class PropertiesUtil {

	private static final Logger logger = LogManager.getLogger(PropertiesUtil.class.getName());
	public static String okta_org = "";
	public static String project_name = "";
	public static String customer_name = "";
	public static String customer_web = "";
	public static String api_key = "";
	public static String client_id = "";
	public static String client_secret = "";
	public static String client_id_widget = "";
	public static String redirect_url = "";
	public static String redirect_url_widget = "";
	public static String post_logout_redirect_url = "";
	public static String post_logout_redirect_url_widget = "";
	public static String server_id = "";
	public static String linked_obj_associate = "";
	public static String google_client_id = "";
	public static String google_client_secret = "";
	public static String google_idp = "";
	public static String google_idp_name = "";
	public static String gsuite_idp = "";
	public static String microsoft_idp = "";
	public static String logo = "";
	public static String banner = "";
	public static String background1 = "";
	public static String background2 = "";
	public static boolean propsLoaded = false;
	//Demo platform properties
	public static String demo_api_client_id = "";
	public static String demo_api_client_secret = "";
	public static String demo_api_token_url = "";
	public static String demo_api_audience = "";
	public static String base_uri = "";
	public static String demo_name = "";

	public static void loadProperties(InputStream propStream) throws ConfigurationException {

		Properties props = new Properties();

		if (propsLoaded) {
			logger.info("Properties previously loaded ...");
		}
		else {
			logger.info("About to load properties ...");
			try {
				props.load(propStream);
				
				// okta_org
				if (props.getProperty("okta_org") == null || props.getProperty("okta_org").length() == 0) {
					logger.fatal("Okta API property file missing value for okta_org");
					throw new ConfigurationException("Okta API property file missing value for okta_org");
				}
				else {
					okta_org = props.getProperty("okta_org");
				}

				// project_name
				if (props.getProperty("project_name") == null || props.getProperty("project_name").length() == 0) {
					logger.fatal("Okta API property file missing value for project_name");
					throw new ConfigurationException("Okta API property file missing value for project_name");
				}
				else {
					project_name = props.getProperty("project_name");
				}

				// customer_name
				if (props.getProperty("customer_name") == null || props.getProperty("customer_name").length() == 0) {
					logger.fatal("Okta API property file missing value for customer_name");
					throw new ConfigurationException("Okta API property file missing value for customer_name");
				}
				else {
					customer_name = props.getProperty("customer_name");
				}
				
				// redirect_url
				if (props.getProperty("redirect_url") == null || props.getProperty("redirect_url").length() == 0) {
					logger.fatal("Okta API property file missing value for redirect_url");
					throw new ConfigurationException("Okta API property file missing value for redirect_url");
				}
				else {
					redirect_url = props.getProperty("redirect_url");
				}
				
				// redirect_url_widget
				if (props.getProperty("redirect_url_widget") == null || props.getProperty("redirect_url_widget").length() == 0) {
					logger.fatal("Okta API property file missing value for redirect_url_widget");
					throw new ConfigurationException("Okta API property file missing value for redirect_url_widget");
				}
				else {
					redirect_url_widget = props.getProperty("redirect_url_widget");
				}

				// customer_web
				if (props.getProperty("customer_web") == null || props.getProperty("customer_web").length() == 0) {
					logger.fatal("Okta API property file missing value for customer_web");
					throw new ConfigurationException("Okta API property file missing value for customer_web");
				}
				else {
					customer_web = props.getProperty("customer_web");
				}

				// api_key
				if (props.getProperty("api_key") == null || props.getProperty("api_key").length() == 0) {
					logger.fatal("Okta API property file missing value for api_key");
					throw new ConfigurationException("Okta API property file missing value for api_key");
				}
				else {
					api_key = props.getProperty("api_key");
				}

				// client_id
				if (props.getProperty("client_id") == null || props.getProperty("client_id").length() == 0) {
					logger.fatal("Okta API property file missing value for client_id");
					throw new ConfigurationException("Okta API property file missing value for client_id");
				}
				else {
					client_id = props.getProperty("client_id");
				}
				
				// client_secret
				if (props.getProperty("client_secret") == null || props.getProperty("client_secret").length() == 0) {
					logger.fatal("Okta API property file missing value for client_secret");
					throw new ConfigurationException("Okta API property file missing value for client_secret");
				}
				else {
					client_secret = props.getProperty("client_secret");
				}
				
				// client_id
				if (props.getProperty("client_id_widget") == null || props.getProperty("client_id_widget").length() == 0) {
					logger.fatal("Okta API property file missing value for client_id_widget");
					throw new ConfigurationException("Okta API property file missing value for client_id_widget");
				}
				else {
					client_id_widget = props.getProperty("client_id_widget");
				}

				// server_id
				if (props.getProperty("server_id") == null || props.getProperty("server_id").length() == 0) {
					logger.fatal("Okta API property file missing value for server_id");
					throw new ConfigurationException("Okta API property file missing value for server_id");
				}
				else {
					server_id = props.getProperty("server_id");
				}

				// linked_obj_associate
				if (props.getProperty("linked_obj_associate") == null || props.getProperty("linked_obj_associate").length() == 0) {
					logger.fatal("Okta API property file missing value for linked_obj_associate");
					throw new ConfigurationException("Okta API property file missing value for linked_obj_associate");
				}
				else {
					linked_obj_associate = props.getProperty("linked_obj_associate");
				}				
				
				// google_client_id
				if (props.getProperty("google_client_id") == null || props.getProperty("google_client_id").length() == 0) {
					logger.fatal("Okta API property file missing value for google_client_id");
					throw new ConfigurationException("Okta API property file missing value for google_client_id");
				}
				else {
					google_client_id = props.getProperty("google_client_id");
				}
				
				// google_client_secret
				if (props.getProperty("google_client_secret") == null || props.getProperty("google_client_secret").length() == 0) {
					logger.fatal("Okta API property file missing value for google_client_secret");
					throw new ConfigurationException("Okta API property file missing value for google_client_secret");
				}
				else {
					google_client_secret = props.getProperty("google_client_secret");
				}
				
				// google_idp
				if (props.getProperty("google_idp") == null || props.getProperty("google_idp").length() == 0) {
					logger.fatal("Okta API property file missing value for google_idp");
					throw new ConfigurationException("Okta API property file missing value for google_idp");
				}
				else {
					google_idp = props.getProperty("google_idp");
				}

				// google_idp_name
				if (props.getProperty("google_idp_name") == null || props.getProperty("google_idp_name").length() == 0) {
					logger.fatal("Okta API property file missing value for google_idp_name");
					throw new ConfigurationException("Okta API property file missing value for google_idp_name");
				}
				else {
					google_idp_name = props.getProperty("google_idp_name");
				}

				// gsuite_idp
				if (props.getProperty("gsuite_idp") == null || props.getProperty("gsuite_idp").length() == 0) {
					logger.fatal("Okta API property file missing value for gsuite_idp");
					throw new ConfigurationException("Okta API property file missing value for gsuite_idp");
				}
				else {
					gsuite_idp = props.getProperty("gsuite_idp");
				}

				// microsoft_idp
				if (props.getProperty("microsoft_idp") == null || props.getProperty("microsoft_idp").length() == 0) {
					logger.fatal("Okta API property file missing value for microsoft_idp");
					throw new ConfigurationException("Okta API property file missing value for microsoft_idp");
				}
				else {
					microsoft_idp = props.getProperty("microsoft_idp");
				}
				
				// logo
				if (props.getProperty("logo") == null || props.getProperty("logo").length() == 0) {
					logger.fatal("Okta API property file missing value for logo");
					throw new ConfigurationException("Okta API property file missing value for logo");
				}
				else {
					logo = props.getProperty("logo");
				}

				// banner
				if (props.getProperty("banner") == null || props.getProperty("banner").length() == 0) {
					logger.fatal("Okta API property file missing value for banner");
					throw new ConfigurationException("Okta API property file missing value for banner");
				}
				else {
					banner = props.getProperty("banner");
				}
				
				// background1
				if (props.getProperty("background1") == null || props.getProperty("background1").length() == 0) {
					logger.fatal("Okta API property file missing value for background1");
					throw new ConfigurationException("Okta API property file missing value for background1");
				}
				else {
					background1 = props.getProperty("background1");
				}
				
				// background2
				if (props.getProperty("background2") == null || props.getProperty("background2").length() == 0) {
					logger.fatal("Okta API property file missing value for background2");
					throw new ConfigurationException("Okta API property file missing value for background2");
				}
				else {
					background2 = props.getProperty("background2");
				}
				
				if (props.getProperty("demo_api_client_id") == null || props.getProperty("demo_api_client_id").length() == 0) {
					logger.fatal("Okta API property file missing value for demo_api_client_id");
					throw new ConfigurationException("Okta API property file missing value for demo_api_client_id");
				}
				else {
					demo_api_client_id = props.getProperty("demo_api_client_id");
				}
				
				if (props.getProperty("demo_api_client_secret") == null || props.getProperty("demo_api_client_secret").length() == 0) {
					logger.fatal("Okta API property file missing value for demo_api_client_secret");
					throw new ConfigurationException("Okta API property file missing value for demo_api_client_secret");
				}
				else {
					demo_api_client_secret = props.getProperty("demo_api_client_secret");
				}
				
				if (props.getProperty("demo_api_token_url") == null || props.getProperty("demo_api_token_url").length() == 0) {
					logger.fatal("Okta API property file missing value for demo_api_token_url");
					throw new ConfigurationException("Okta API property file missing value for demo_api_token_url");
				}
				else {
					demo_api_token_url = props.getProperty("demo_api_token_url");
				}
				
				if (props.getProperty("demo_api_audience") == null || props.getProperty("demo_api_audience").length() == 0) {
					logger.fatal("Okta API property file missing value for demo_api_audience");
					throw new ConfigurationException("Okta API property file missing value for demo_api_audience");
				}
				else {
					demo_api_audience = props.getProperty("demo_api_audience");
				}
				
				if (props.getProperty("base_uri") == null || props.getProperty("base_uri").length() == 0) {
					logger.fatal("Okta API property file missing value for base_uri");
					throw new ConfigurationException("Okta API property file missing value for base_uri");
				}
				else {
					base_uri = props.getProperty("base_uri");
				}
				
				StringBuilder encodedUrl = new StringBuilder("");
				encodedUrl.append("grant_type=client_credentials");
				encodedUrl.append("&client_id=" + demo_api_client_id);
				encodedUrl.append("&client_secret=" + demo_api_client_secret);
				encodedUrl.append("&scope=bootstrap");
				encodedUrl.append("&audience=" + demo_api_audience);
				URL obj = new URL(demo_api_token_url);
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				con.setDoOutput(true);
				final BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
				bfw.write(encodedUrl.toString());
				bfw.flush();
				bfw.close();
				int responseCode = con.getResponseCode();
				logger.debug("Response Code from demo api token end-point: " + responseCode);
				if (responseCode != 200) {
					logger.fatal("Response code when getting access token from demo api token end-point: " + responseCode);
					throw new CommunicationException("Response code when getting access token from demo api token end-point: " + responseCode);
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer getResponse = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					getResponse.append(inputLine);
				}
				in.close();
				
				logger.debug("Response from demo api token end-point="+getResponse.toString());
				JSONObject jObj = new JSONObject(getResponse.toString());
				String accessToken = jObj.getString("access_token");
				jObj = JwtParser.parseJWT(accessToken);
				String applicationId = jObj.getString("applicationId");
				
				String bootstrapUrl = "https://api.demo.okta.com/bootstrap/" + applicationId + "/" + demo_name;
				obj = new URL(bootstrapUrl);
				con = (HttpsURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("Authorization", "Bearer " + accessToken);
				con.setDoOutput(true);
				responseCode = con.getResponseCode();
				if (responseCode != 200) {
					logger.fatal("Response code when calling bootstrap end-point: " + responseCode);
				}
				
				// Extract the response
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				getResponse = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					getResponse.append(inputLine);
				}
				in.close();
				
				logger.debug("Response from bootstrap end-point = "+getResponse.toString());
				jObj = new JSONObject(getResponse.toString());
				client_id = jObj.getJSONObject("oidc_configuration").getString("client_id");
				client_secret = jObj.getJSONObject("oidc_configuration").getString("client_secret");
				String issuer = jObj.getJSONObject("oidc_configuration").getString("issuer");
				okta_org = issuer.substring(8, issuer.indexOf("/oauth2/default"));
				System.out.println("okta org from bootstrap API: " + okta_org);
				redirect_url = "http://" + demo_name + "." + base_uri + "/oiedemo/initialize.html";
				redirect_url_widget = "http://" + demo_name + "." + base_uri + "/oiedemo/initializewidget.jsp";
				post_logout_redirect_url = "http://" + demo_name + "." + base_uri + "/oiedemo/logincustom.html";
				post_logout_redirect_url_widget = "http://" + demo_name + "." + base_uri + "/oiedemo";
				propsLoaded = true;
				logger.info("Properties successfully loaded");
				logger.debug("***** Loaded Properties *****");
				logger.debug("okta_org=" + okta_org);					
				logger.debug("project_name=" + project_name);	
				logger.debug("customer_name=" + customer_name);					
				logger.debug("customer_web=" + customer_web);					
				logger.debug("client_id=" + client_id);			
				logger.debug("client_secret=" + client_secret);	
				logger.debug("client_id_widget=" + client_id_widget);
				logger.debug("redirect_url=" + redirect_url);	
				logger.debug("redirect_url_widget=" + redirect_url_widget);	
				logger.debug("post_logout_redirect_url=" + post_logout_redirect_url);	
				logger.debug("post_logout_redirect_url_widget=" + post_logout_redirect_url_widget);	
				logger.debug("server_id=" + server_id);			
				logger.debug("google_client_id=" + google_client_id);
				logger.debug("google_client_secret=" + google_client_secret);
				logger.debug("google_idp=" + google_idp);					
				logger.debug("gsuite_idp=" + gsuite_idp);			
				logger.debug("microsoft_idp=" + microsoft_idp);		
				logger.debug("logo=" + logo);					
				logger.debug("banner=" + banner);					
				logger.debug("background1=" + background1);	
				logger.debug("background2=" + background2);	
				logger.debug("demo_api_client_id=" + demo_api_client_id);	
				logger.debug("demo_api_client_secret=" + demo_api_client_secret);	
				logger.debug("demo_api_token_url=" + demo_api_token_url);	
				logger.debug("demo_api_audience=" + demo_api_audience);	
				logger.debug("base_uri=" + base_uri);	
				logger.debug("*****************************");
			} 
			catch (IOException e) {
				logger.fatal("Caught IOException: "+ e.getMessage() + " - " + e.getLocalizedMessage());
				logger.fatal("Currect working directory=" + System.getProperty("user.dir"));
				throw new ConfigurationException(e.getMessage() + " - " + e.getLocalizedMessage());
			}
		}
	}
	
	
    public static void main(final String[] args) {
    	try {
			FileInputStream fis = new FileInputStream("./WebContent/WEB-INF/classes/okta-api.properties");
			PropertiesUtil.loadProperties(fis);

		} catch (FileNotFoundException e) {
			System.out.println("Currect working directory=" + System.getProperty("user.dir"));
			System.out.println("");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }   


}
