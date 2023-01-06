package com.okta.demo.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.okta.demo.exception.InternalException;
import com.okta.demo.util.JwtParser;
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
 * Servlet implementation class RecoverPassword
 */
@WebServlet("/RecoverPassword")
public class RecoverPassword extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(RecoverPassword.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecoverPassword() {
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
		String username = request.getParameter("email");
		HttpSession session = request.getSession();
		
		System.out.println("In RecoverPassword servlet, email: " + username);
		
		IDXAuthenticationWrapper idxAuthenticationWrapper = (IDXAuthenticationWrapper)session.getAttribute("idxAuthenticationWrapper");
		ProceedContext proceedContext = (ProceedContext)session.getAttribute("proceedContext");
		
		AuthenticationResponse authenticationResponse =
			    idxAuthenticationWrapper.recoverPassword(username, proceedContext);
		
		AuthenticationStatus authenticationStatus = authenticationResponse.getAuthenticationStatus();
		
		System.out.println("authentication status in RecoverPassword: " + authenticationStatus);
		
		if (authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_SELECTION || authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_VERIFICATION_DATA) {
			System.out.println("Authentication Status is AWAITING_AUTHENTICATOR_SELECTION");
			List<Authenticator> authenticators = authenticationResponse.getAuthenticators();
			request.getSession().setAttribute("proceedContext", authenticationResponse.getProceedContext());
			request.getSession().setAttribute("authenticators", authenticators);
			request.getRequestDispatcher("/selectauthenticator.jsp").forward(request, response);
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
