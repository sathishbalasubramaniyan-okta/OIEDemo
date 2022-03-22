package com.okta.demo.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.okta.demo.exception.InternalException;
import com.okta.demo.util.ContextHelper;
import com.okta.demo.util.CookieUtil;
import com.okta.demo.util.GoogleIdpUtils;
import com.okta.demo.util.JsonFormatter;
import com.okta.demo.util.JwtParser;
import com.okta.demo.util.PropertiesUtil;
import com.okta.demo.util.OktaAppUtils;
import com.okta.demo.util.OktaIdpUtils;

/**
 * Servlet implementation class GetAccessToken
 */
@WebServlet("/ProcessGoogleReply")
public class ProcessGoogleReply extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ProcessGoogleReply.class.getName());
	//private static String accessToken = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessGoogleReply() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() {
    	//LoadProperties.loadProperties(getClass().getResourceAsStream(PROP_FILE));
    	
    	if(!PropertiesUtil.propsLoaded) {
	    	String absolutePath = this.getClass().getClassLoader().getResource("").getPath();
	    	try {
				FileInputStream fis = new FileInputStream(absolutePath + PROP_FILE);
				PropertiesUtil.loadProperties(fis);
			} catch (FileNotFoundException e) {
				logger.fatal("Failed to load property file: " + PROP_FILE + " cause: " + e.getMessage());
				throw new InternalException("Failed to load property file: " + PROP_FILE + " cause: " + e.getMessage());
			}
    	}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("doGet() - Start");
		
		String sub = "";
		String uid = "";
		String accessToken = "";
		
		// Setup the correct URL Root Context
		ContextHelper.setRootContect(request);
		
		//DEBUG
		CookieUtil.displayCookies(request);
		
		// Establish user context via token or session
		if (request.getParameter("access_token") != null) {
			logger.debug("Access Token passed as a parameter ...");
			accessToken = request.getParameter("access_token");
			JSONObject jObj = JwtParser.parseJWT(accessToken);
			logger.debug(JsonFormatter.format(jObj));
			sub = jObj.getString("sub");
			logger.debug("sub=" + sub);
			uid = jObj.getString("uid");
			logger.debug("uid=" + uid);
			CookieUtil.addCookie(response, "accessToken", accessToken);
			//LoadUserHome.accessToken = accessToken;

		}
		// TODO - Replace static storage with user session or cookie
		//else if (LoadUserHome.accessToken != null && LoadUserHome.accessToken.length() > 0) {
		else if (CookieUtil.containsCookie(request, "accessToken")) {
			logger.debug("Access Token found in storeage ...");
			accessToken = CookieUtil.getCookieValue(request, "accessToken");
			logger.debug("Access Token=" + accessToken);
			JSONObject jObj = JwtParser.parseJWT(accessToken);
			sub = jObj.getString("sub");
			logger.debug("sub=" + sub);
			uid = jObj.getString("uid");
			logger.debug("uid=" + uid);			
		}
		// TODO - Add exception handling below
		else {
			logger.warn("Access Token not found ...");
			String error = "Account Not Found ... If Authenticating with a Social Provider, please link your account first";
			response.sendRedirect(ContextHelper.getRootContect() + "/login.jsp?error=" + URLEncoder.encode(error, "UTF-8").replace("+", "%20"));
			return;
		}
		
		// Process the returned Auth Code from a Social Provider
		if (request.getParameter("code") != null) {
			// TODO - Allow for multiple social providers
			String authCode = request.getParameter("code"); 
			logger.debug("authCode=" + authCode);	
			// Retrieve the token using the authentication code
			JSONObject googleJsonObj = GoogleIdpUtils.retrieveGoogleToken(authCode, 
									   PropertiesUtil.google_client_id, 
									   PropertiesUtil.google_client_secret, 
									   ContextHelper.getRootContect() + "/google-reply.html");
			// Extract the user google uid from the token
			String googleUserId = GoogleIdpUtils.extractGoogleUserId(googleJsonObj);
			// Link the google idp to the user Okta account
			OktaIdpUtils.addLinkedIdp(PropertiesUtil.google_idp, uid, googleUserId);
		}		
		
		// Get the apps and idps for this user
		request.getSession().setAttribute("sub", sub);
		request.getSession().setAttribute("uid", uid);
		OktaAppUtils.getUserApps(uid, request.getSession());
		OktaIdpUtils.getLinkedIdps(uid, request.getSession());
       
		logger.debug("doGet() - End");
		request.getRequestDispatcher("/landing.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
        	
}
