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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.okta.demo.exception.InternalException;
import com.okta.demo.util.PropertiesUtil;
import com.okta.idx.sdk.api.client.Authenticator;
import com.okta.idx.sdk.api.client.IDXAuthenticationWrapper;
import com.okta.idx.sdk.api.client.ProceedContext;
import com.okta.idx.sdk.api.model.AuthenticationOptions;
import com.okta.idx.sdk.api.model.AuthenticationStatus;
import com.okta.idx.sdk.api.model.FormValue;
import com.okta.idx.sdk.api.model.UserProfile;
import com.okta.idx.sdk.api.model.VerifyAuthenticatorOptions;
import com.okta.idx.sdk.api.response.AuthenticationResponse;
import com.okta.idx.sdk.api.response.TokenResponse;

import com.okta.idx.sdk.api.exception.ProcessingException;

/**
 * Servlet implementation class SelectAuthenticator
 */
@WebServlet("/SelectAuthenticator")
public class SelectAuthenticator extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(SelectAuthenticator.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectAuthenticator() {
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
		HttpSession session = request.getSession();
		IDXAuthenticationWrapper idxAuthenticationWrapper = (IDXAuthenticationWrapper)session.getAttribute("idxAuthenticationWrapper");
		//AuthenticationResponse beginResponse = idxAuthenticationWrapper.begin();
		//ProceedContext proceedContext = beginResponse.getProceedContext();
		ProceedContext proceedContext = (ProceedContext)session.getAttribute("proceedContext");
		List<Authenticator> authenticators = (List<Authenticator>)session.getAttribute("authenticators");
		
		String selectedAuthenticatorId = request.getParameter("selectedauthenticator");
		System.out.println("Selected authenticator ID: " + selectedAuthenticatorId);
		Authenticator selectedAuthenticator = null;
		
		for (Authenticator authenticator: authenticators) {
			if (authenticator.getId().equals(selectedAuthenticatorId)) {
				selectedAuthenticator = authenticator;
			}
		}
		
		System.out.println("Selected authenticator ID: " + selectedAuthenticator.getId());
		System.out.println("Selected authenticator Label: " + selectedAuthenticator.getLabel());
		
		AuthenticationResponse authenticationResponse = idxAuthenticationWrapper.selectAuthenticator(proceedContext, selectedAuthenticator);
		session.setAttribute("proceedContext", authenticationResponse.getProceedContext());
		AuthenticationStatus authenticationStatus = authenticationResponse.getAuthenticationStatus();
		System.out.println("Authentication Status is: " + authenticationStatus);
		if (authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_VERIFICATION) {
			System.out.println("AuthenticationStatus is AWAITING_AUTHENTICATOR_VERIFICATION");
			request.getRequestDispatcher("/verifyauthenticator.html").forward(request, response);
		} else if (authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_ENROLLMENT_DATA) {
			System.out.println("AuthenticationStatus is AWAITING_AUTHENTICATOR_ENROLLMENT_DATA");
			request.getRequestDispatcher("/enrollauthenticator.html").forward(request, response);
		} else if (authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_ENROLLMENT) {
			System.out.println("AuthenticationStatus is AWAITING_AUTHENTICATOR_ENROLLMENT");
			if (selectedAuthenticator.getLabel().equals("Password")) {
				request.getRequestDispatcher("/collectpassword.html").forward(request, response);
			} else {
				request.getRequestDispatcher("/verifyauthenticator.html").forward(request, response);
			}
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
