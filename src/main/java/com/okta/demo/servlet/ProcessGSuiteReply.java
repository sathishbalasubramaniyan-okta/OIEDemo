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
import com.okta.demo.util.CookieUtil;
import com.okta.demo.util.PropertiesUtil;


/**
 * Servlet implementation class GetAccessToken
 */
@WebServlet("/ProcessGSuiteReply")
public class ProcessGSuiteReply extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ProcessGSuiteReply.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessGSuiteReply() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init() {
    	//LoadProperties.loadProperties(getClass().getResourceAsStream(PROP_FILE));
    	try {
			FileInputStream fis = new FileInputStream("../webapps/moe/WEB-INF/classes/" + PROP_FILE);
			PropertiesUtil.loadProperties(fis);
		} catch (FileNotFoundException e) {
			logger.fatal("Failed to load property file: " + PROP_FILE + " cause: " + e.getMessage());
			throw new InternalException("Failed to load property file: " + PROP_FILE + " cause: " + e.getMessage());
		}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("doGet() - Start");
		
		// Setup the correct URL Root Context
		ContextHelper.setRootContect(request);
		
		//DEBUG
		CookieUtil.displayCookies(request);
       
		logger.debug("doGet() - End");
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
