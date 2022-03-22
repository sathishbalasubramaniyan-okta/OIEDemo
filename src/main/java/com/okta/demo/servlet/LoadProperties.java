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
import com.okta.demo.util.PropertiesUtil;

/**
 * Servlet implementation class LoadProperties
 */
@WebServlet("/LoadProperties")
public class LoadProperties extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(LoadProperties.class.getName());       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadProperties() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.debug("doGet() - Start");
		String rootContext = (String)request.getParameter("rootContext");
		logger.debug("rootContext=" + rootContext);
		String sender = (String)request.getParameter("sender");
		logger.debug("sender=" + sender);
		// Only load properties if they have not previously been loaded
		if (!PropertiesUtil.propsLoaded) {
	    	//LoadProperties.loadProperties(getClass().getResourceAsStream(PROP_FILE));
	    	try {
				FileInputStream fis = new FileInputStream("../webapps/" + rootContext + "/WEB-INF/classes/" + PROP_FILE);
				PropertiesUtil.loadProperties(fis);
			} catch (FileNotFoundException e) {
				logger.fatal("Failed to load property file: " + PROP_FILE + " cause: " + e.getMessage());
				throw new InternalException("Failed to load property file: " + PROP_FILE + " cause: " + e.getMessage());
			}
		}
		
    	logger.debug("doGet() - End");
    	response.setContentType("text/html");
		String params = "oktaOrg=" + PropertiesUtil.okta_org;
		params += "&serverId=" + PropertiesUtil.server_id;
		params += "&clientId=" + PropertiesUtil.client_id;
		params += "&customerName=" + PropertiesUtil.customer_name;
		params += "&googleServerId=" + PropertiesUtil.google_idp;
		
		response.sendRedirect(sender + "?" + params);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
