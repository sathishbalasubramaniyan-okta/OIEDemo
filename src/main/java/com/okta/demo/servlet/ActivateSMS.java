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
import com.okta.demo.util.ContextHelper;
import com.okta.demo.util.OktaMfaUtils;
import com.okta.demo.util.PropertiesUtil;
import com.okta.demo.exception.CommunicationException;

/**
 * Servlet to activate a SMS enrollment
 */
@WebServlet("/ActivateSMS")
public class ActivateSMS extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ActivateSMS.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActivateSMS() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() {
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
		// Setup the correct URL Root Context
		ContextHelper.setRootContect(request);		
		String id = ""; 
		String activateURL = ""; 		
		String passCode = "";
		HttpSession session = request.getSession();
		
		if (session.getAttribute("id") != null) {
			id = (String)session.getAttribute("id");
			logger.debug("Passed id=" + id);
		}
		if (session.getAttribute("activateURL") != null) {
			activateURL = (String)session.getAttribute("activateURL");
			logger.debug("Passed activateURL=" + activateURL);
		}
		
		if (request.getParameter("otp") != null) {
			passCode = request.getParameter("otp");
			logger.debug("Passed passCode=" + passCode);
		}
		
		// Activate the SMS enrollment with the passed pass code
		try {
			OktaMfaUtils.activateSMS(activateURL, passCode);
			// Now send the user to set their password
			request.getRequestDispatcher("/setpassword.html").forward(request, response);
		}
		catch (CommunicationException ce) {
			response.sendRedirect(ContextHelper.getRootContect() + "/verifyotp.html?error=invalid_otp");
		}
								
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
