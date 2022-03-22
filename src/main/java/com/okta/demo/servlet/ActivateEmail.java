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

import com.okta.demo.exception.CommunicationException;
import com.okta.demo.exception.InternalException;
import com.okta.demo.obj.User;
import com.okta.demo.util.ContextHelper;
import com.okta.demo.util.OktaMfaUtils;
import com.okta.demo.util.OktaUserUtils;
import com.okta.demo.util.PropertiesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class ActivateEmail
 */
@WebServlet("/ActivateEmail")
public class ActivateEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PROP_FILE = "okta-api.properties";	
	private static final Logger logger = LogManager.getLogger(ActivateEmail.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActivateEmail() {
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
		// TODO Auto-generated method stub
		logger.debug("doGet() - Start");
		// Setup the correct URL Root Context
		ContextHelper.setRootContect(request);		
		String activateURL = ""; 		
		String passCode = "";
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		String username = (String)session.getAttribute("username");
		String email = (String)session.getAttribute("email");
		
		User user = new User();
		user.setId(id);
		user.setUsername(username);
		user.setEmail(email);
		user.setEmailVerified("Verified");
		
		if (session.getAttribute("activateURL") != null) {
			activateURL = (String)session.getAttribute("activateURL");
			logger.debug("Passed activateURL=" + activateURL);
		} else {
			JSONArray jArr = OktaMfaUtils.listEnrolledFactors(id);
			for (int i=0; i<jArr.length(); i++) {
				JSONObject jObj = jArr.getJSONObject(i);
				if (jObj.has("factorType") && jObj.getString("factorType").equals("email")) {
					activateURL = jObj.getJSONObject("_links").getJSONObject("activate").getString("href");
					break;
				}
			}
		}
		
		if (request.getParameter("passCode") != null) {
			passCode = request.getParameter("passCode");
			logger.debug("Passed passCode=" + passCode);
		}
		
		
		// Activate the SMS enrollment with the passed pass code
		try {
			OktaMfaUtils.activateEmail(activateURL, passCode);
			OktaUserUtils.updateUser(user);
			OktaUserUtils.updateUser(user);
			session.setAttribute("emailVerified", "Verified");
		}
		catch (CommunicationException ce) {
			logger.fatal("Error when activating email or updating user: " + ce.getMessage());
		}
		request.getRequestDispatcher("/home.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
