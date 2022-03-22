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
 * Servlet implementation class VerifyAuthenticator
 */
@WebServlet("/VerifyAuthenticator")
public class VerifyAuthenticator extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(VerifyAuthenticator.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyAuthenticator() {
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
		String otp = request.getParameter("otp");
		VerifyAuthenticatorOptions verifyAuthenticatorOptions = new VerifyAuthenticatorOptions(otp);
		
		AuthenticationResponse authenticationResponse =
				   idxAuthenticationWrapper.verifyAuthenticator(proceedContext, verifyAuthenticatorOptions);
		
		List<String> errors = authenticationResponse.getErrors();
		
		if (errors != null && errors.size()>0) {
			System.out.println("Errors: " + errors.toString());
		} else {
			System.out.println("No errors in authenticate");
		}
		
		AuthenticationStatus authenticationStatus = authenticationResponse.getAuthenticationStatus();
		if (authenticationStatus == AuthenticationStatus.SUCCESS) {
			System.out.println("Authentication Status is success");
			TokenResponse tokenResponse = authenticationResponse.getTokenResponse();
			String idToken = tokenResponse.getIdToken();
			String accessToken = tokenResponse.getAccessToken();
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
			request.getSession().setAttribute("idxAuthenticationWrapper", idxAuthenticationWrapper);
			//request.getSession().setAttribute("tokenResponse", tokenResponse);
			request.getRequestDispatcher("/home.jsp").forward(request, response);
		} else if (authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_ENROLLMENT_SELECTION) {
			System.out.println("AuthenticationStatus is AWAITING_AUTHENTICATOR_ENROLLMENT_SELECTION");
			List<Authenticator> authenticators = authenticationResponse.getAuthenticators();
			session.setAttribute("proceedContext", authenticationResponse.getProceedContext());
			session.setAttribute("authenticators", authenticators);
			request.getRequestDispatcher("/selectauthenticator.jsp").forward(request, response);
			
		} else if (authenticationStatus == AuthenticationStatus.AWAITING_PASSWORD_RESET) {
			System.out.println("AuthenticationStatus is AWAITING_PASSWORD_RESET");
			session.setAttribute("proceedContext", authenticationResponse.getProceedContext());
			request.getRequestDispatcher("/resetpassword.html").forward(request, response);
			
		} else {
			System.out.println("Authentication Status non success" + authenticationStatus);
			if (errors != null && errors.size()>0) {
				if (errors.get(0).startsWith("Invalid code")) {
					request.getRequestDispatcher("/verifyauthenticator.html").forward(request, response);
				}
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
