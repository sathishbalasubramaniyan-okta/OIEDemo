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
 * Servlet implementation class VerifyAuthenticatorEnrollment
 */
@WebServlet("/VerifyAuthenticatorEnrollment")
public class VerifyAuthenticatorEnrollment extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(VerifyAuthenticatorEnrollment.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyAuthenticatorEnrollment() {
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
		ProceedContext proceedContext = (ProceedContext)session.getAttribute("proceedContext");
		
		String phonenumber = request.getParameter("phonenumber");
		System.out.println("Phone number: " + phonenumber);
		//VerifyAuthenticatorOptions verifyAuthenticatorOptions = new VerifyAuthenticatorOptions(phonenumber);
		
		//AuthenticationResponse authenticationResponse = idxAuthenticationWrapper.verifyAuthenticator(proceedContext, verifyAuthenticatorOptions);
		
		AuthenticationResponse authenticationResponse = idxAuthenticationWrapper.submitPhoneAuthenticator(proceedContext,
				phonenumber, getFactorFromMethod(session, "sms"));
		
		List<String> errors = authenticationResponse.getErrors();
		
		if (errors != null && errors.size()>0) {
			System.out.println("Errors: " + errors.toString());
		} else {
			System.out.println("No errors in authenticate");
		}
		
		AuthenticationStatus authenticationStatus = authenticationResponse.getAuthenticationStatus();
		System.out.println("Authentication Status is: " + authenticationStatus);
		
		if ((authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_VERIFICATION) || (authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_ENROLLMENT)) {
			System.out.println("AuthenticationStatus is AWAITING_AUTHENTICATOR_VERIFICATION / AWAITING_AUTHENTICATOR_ENROLLMENT");
			session.setAttribute("proceedContext", authenticationResponse.getProceedContext());
			request.getRequestDispatcher("/verifyauthenticator.html").forward(request, response);
		}
	}
	
	 private Authenticator.Factor getFactorFromMethod(final HttpSession session, final String method) {
		List<Authenticator> authenticators = (List<Authenticator>) session.getAttribute("authenticators");
		for (Authenticator authenticator : authenticators) {
			for (Authenticator.Factor factor : authenticator.getFactors()) {
				if (factor.getMethod().equals(method)) {
				return factor;
				}
			}
		}
		throw new IllegalStateException("Factor not found: " + method);
	 }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
