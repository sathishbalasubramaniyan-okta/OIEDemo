package com.okta.demo.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.okta.demo.exception.InternalException;
import com.okta.demo.util.ContextHelper;
import com.okta.demo.util.OktaUserUtils;
import com.okta.demo.util.PropertiesUtil;

/**
 * Servlet to set a users password
 */
@WebServlet("/SetPassword")
public class SetPassword extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(SetPassword.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetPassword() {
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
		String password = ""; 
		
		
		if (request.getSession().getAttribute("id") != null) {
			id = (String)request.getSession().getAttribute("id");
			logger.debug("Passed id=" + id);
		}
		if (request.getParameter("password") != null) {
			password = request.getParameter("password");
			logger.debug("Passed password=" + password);
		}
		
		// Set the user password
		OktaUserUtils.setPassword(id, password);
		OktaUserUtils.activateUser(id, "false");
		
		String username = (String)request.getSession().getAttribute("username");
		
		String sessionToken = OktaUserUtils.authenticateUser(username, password);
		
		request.getSession().setAttribute("sessionToken", sessionToken);
				
		// Now send the user to the Okta home page
		//response.sendRedirect("https://www.racq.com.au/");
		request.getRequestDispatcher("/setemail.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
