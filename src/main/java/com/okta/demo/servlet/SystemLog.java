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
import com.okta.demo.util.DateTimeUtils;
import com.okta.demo.util.OktaSystemLogUtils;
import com.okta.demo.util.OktaUserUtils;
import com.okta.demo.util.PropertiesUtil;

/**
 * Servlet to set a users password
 */
@WebServlet("/SystemLog")
public class SystemLog extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(SystemLog.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SystemLog() {
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
		String event = "security.request.blocked";
		String published = DateTimeUtils.getCurrentDate() + "T00:00:00.000Z"; 

		
/*
 		if (request.getParameter("event") != null) {
			event = request.getParameter("event");
			logger.debug("Passed event=" + event);
		}
		if (request.getParameter("published") != null) {
			published = request.getParameter("published");
			logger.debug("Passed published=" + published);
		}
*/		
		OktaSystemLogUtils.getSystemLogEvent(published, event);
				
		// Now send the user to the Okta home page
		//response.sendRedirect("https://www.racq.com.au/");
		//request.getRequestDispatcher("/login.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
