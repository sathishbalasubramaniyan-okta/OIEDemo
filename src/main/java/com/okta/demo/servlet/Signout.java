package com.okta.demo.servlet;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.okta.demo.util.CookieUtil;
import com.okta.demo.util.PropertiesUtil;

/**
 * Servlet implementation class Signoff
 */
@WebServlet("/Signout")
public class Signout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(Signout.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("doGet() - Start");
		
		// Setup the correct url sign out the user
		String url = "https://" + PropertiesUtil.okta_org + "/login/signout";
		// Establish the Connection
		logger.debug("About to establish connection to server ...");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		logger.debug("Connection established with server ...");
		// Add Request Headers
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		logger.debug("Response Code : " + responseCode);

		logger.debug("doGet() - End");

		CookieUtil.invalidateSession(request, response);
		request.getRequestDispatcher("/login.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
