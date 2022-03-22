package com.okta.demo.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

				propsLoaded = true;
				logger.info("Properties successfully loaded");
				logger.debug("***** Loaded Properties *****");
				logger.debug("okta_org=" + okta_org);					
				logger.debug("project_name=" + project_name);	
				logger.debug("customer_name=" + customer_name);					
				logger.debug("customer_web=" + customer_web);					
				logger.debug("client_id=" + client_id);			
				logger.debug("client_secret=" + client_secret);			
				logger.debug("server_id=" + server_id);			
				logger.debug("google_client_id=" + google_client_id);
				logger.debug("google_client_secret=" + google_client_secret);
				logger.debug("google_idp=" + google_idp);					
				logger.debug("gsuite_idp=" + gsuite_idp);			
				logger.debug("microsoft_idp=" + microsoft_idp);		
				logger.debug("logo=" + logo);					
				logger.debug("banner=" + banner);					
				logger.debug("background1=" + background1);					
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
