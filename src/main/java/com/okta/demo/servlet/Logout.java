package com.okta.demo.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.okta.demo.exception.InternalException;
import com.okta.demo.util.PropertiesUtil;

import com.okta.idx.sdk.api.client.IDXAuthenticationWrapper;
import com.okta.idx.sdk.api.response.TokenResponse;

import com.okta.idx.sdk.api.model.TokenType;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Logout.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
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
		// TODO Auto-generated method stub
		String idToken = null;
		String widgetUsed = null;
		String redirectURL = null;
		try {
			HttpSession session = request.getSession();
			idToken = (String)session.getAttribute("idToken");
			
			if (session.getAttribute("widgetUsed")!= null) {
				widgetUsed = (String)session.getAttribute("widgetUsed");
			}
			
			session.invalidate();
			logger.debug("Invalidated Session");
		}
		catch (java.lang.IllegalStateException e) {
			logger.warn("Invalidate Session threw exception");
			e.printStackTrace();
		}
		
		if ((widgetUsed != null) && (widgetUsed.equals("true"))) {
			redirectURL = "https://" + PropertiesUtil.okta_org + "/oauth2/" + PropertiesUtil.server_id + "/v1/logout?id_token_hint=" + idToken + "&post_logout_redirect_uri=" + PropertiesUtil.post_logout_redirect_url_widget;
		} else {
			redirectURL = "https://" + PropertiesUtil.okta_org + "/oauth2/" + PropertiesUtil.server_id + "/v1/logout?id_token_hint=" + idToken + "&post_logout_redirect_uri=" + PropertiesUtil.post_logout_redirect_url;
		}
		
		
		
		System.out.println("RedirectURL" + redirectURL);
				
		// Now send the user to the Okta home page
		//response.sendRedirect("https://www.racq.com.au/");
		response.sendRedirect(redirectURL);
		/*HttpSession session = request.getSession();
		
		String accessToken = (String)session.getAttribute("accessToken");
		IDXAuthenticationWrapper idxAuthenticationWrapper = (IDXAuthenticationWrapper)session.getAttribute("idxAuthenticationWrapper");
		
		if (accessToken != null) {
            // revoke access token
            logger.info("Revoking access token");
            idxAuthenticationWrapper.revokeToken(TokenType.ACCESS_TOKEN, accessToken);
        }
		
		try {
			session.invalidate();
		} catch (java.lang.IllegalStateException e) {
			logger.warn("Invalidate Session threw exception");
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("/logincustom.html").forward(request, response);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
