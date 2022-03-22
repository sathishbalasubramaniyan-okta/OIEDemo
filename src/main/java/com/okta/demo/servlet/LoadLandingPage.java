package com.okta.demo.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.okta.demo.exception.CommunicationException;
import com.okta.demo.exception.InternalException;
import com.okta.demo.util.ContextHelper;
import com.okta.demo.util.CookieUtil;
import com.okta.demo.util.JwtParser;
import com.okta.demo.util.PropertiesUtil;
import com.okta.demo.util.OktaAppUtils;
import com.okta.demo.util.OktaAuthUtils;
import com.okta.demo.util.OktaIdpUtils;
import com.okta.demo.util.OktaLinkedObjUtils;

import com.okta.idx.sdk.api.client.IDXAuthenticationWrapper;
import com.okta.idx.sdk.api.client.ProceedContext;
import com.okta.idx.sdk.api.model.AuthenticationStatus;
import com.okta.idx.sdk.api.response.AuthenticationResponse;
import com.okta.idx.sdk.api.response.TokenResponse;

/**
 * Servlet 
 */
@WebServlet("/LoadLandingPage")
public class LoadLandingPage extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(LoadLandingPage.class.getName());
	//private static String accessToken = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadLandingPage() {
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
		
		IDXAuthenticationWrapper idxAuthenticationWrapper = (IDXAuthenticationWrapper)request.getSession().getAttribute("idxAuthenticationWrapper");
		
		ProceedContext proceedContext = (ProceedContext)request.getSession().getAttribute("proceedContext");
		
		String interactionCode = request.getParameter("interaction_code");
		
		AuthenticationResponse authenticationResponse =
				idxAuthenticationWrapper.fetchTokenWithInteractionCode("https://" + PropertiesUtil.okta_org + "/oauth2/" + PropertiesUtil.server_id, proceedContext, interactionCode);
		
		
		List<String> errors = authenticationResponse.getErrors();
		
		if (errors != null && errors.size()>0) {
			System.out.println("Errors: " + errors.toString());
		} else {
			System.out.println("No errors in authenticate");
		}
		
		if (authenticationResponse.getTokenResponse() != null) {
			TokenResponse tokenResponse = authenticationResponse.getTokenResponse();
			String idToken = tokenResponse.getIdToken();
			String accessToken = tokenResponse.getAccessToken();
			JSONObject jObj = JwtParser.parseJWT(idToken);
			if (jObj.has("name")) {
				request.getSession().setAttribute("name", jObj.getString("name"));
			}
			if (jObj.has("email")) {
				request.getSession().setAttribute("email", jObj.getString("email"));
			}
			if (jObj.has("firstName")) {
				request.getSession().setAttribute("firstName", jObj.getString("firstName"));
			}
			if (jObj.has("lastName")) {
				request.getSession().setAttribute("lastName", jObj.getString("lastName"));
			}
			request.getSession().setAttribute("idToken", idToken);
			request.getSession().setAttribute("accessToken", accessToken);
			//request.getSession().setAttribute("tokenResponse", tokenResponse);
			request.getRequestDispatcher("/home.jsp").forward(request, response);
		}
		else {
			System.out.println("Authentication was non success");
		}
		
		logger.debug("doGet() - End");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
        	
}
