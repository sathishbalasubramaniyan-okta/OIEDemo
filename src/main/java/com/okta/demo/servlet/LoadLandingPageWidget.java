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
import org.json.JSONObject;

import com.okta.demo.exception.InternalException;
import com.okta.demo.util.JwtParser;
import com.okta.demo.util.PropertiesUtil;

/**
 * Servlet implementation class LoadLandingPageWidget
 */
@WebServlet("/LoadLandingPageWidget")
public class LoadLandingPageWidget extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(LoadLandingPageWidget.class.getName());
	
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
     * @see HttpServlet#HttpServlet()
     */
    public LoadLandingPageWidget() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String idToken = request.getParameter("idToken");
		String accessToken = request.getParameter("accessToken");
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
		request.getSession().setAttribute("widgetUsed", "true");
		//request.getSession().setAttribute("tokenResponse", tokenResponse);
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
