package com.okta.demo.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.okta.demo.exception.InternalException;
import com.okta.demo.util.ContextHelper;
import com.okta.demo.util.OktaUserUtils;
import com.okta.demo.util.PropertiesUtil;
import com.okta.demo.util.EmailValidator;
import com.okta.demo.util.JwtParser;
import com.okta.demo.obj.User;

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
 * Servlet implementation class LoginCustomPage
 */
@WebServlet("/LoginCustomPage")
public class LoginCustomPage extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(LoginCustom.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginCustomPage() {
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
		logger.debug("doGet() - Start");
		// Setup the correct URL Root Context
		ContextHelper.setRootContect(request);
		String username = null;
		String password = null; 
		
			
		
		System.out.println("In LoginCustomPage servlet");
		
		IDXAuthenticationWrapper idxAuthenticationWrapper = (IDXAuthenticationWrapper)request.getSession().getAttribute("idxAuthenticationWrapper");
		
		ProceedContext proceedContext = (ProceedContext)request.getSession().getAttribute("proceedContext");
		
			
		if (request.getParameter("username") != null) {
			username = request.getParameter("username");
		}
		if (request.getParameter("password") != null) {
			password = request.getParameter("password");			
		}
		
		AuthenticationResponse authenticationResponse =
                idxAuthenticationWrapper.authenticate(new AuthenticationOptions(username, password.toCharArray()), proceedContext);
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
			//request.getSession().setAttribute("tokenResponse", tokenResponse);
			request.getRequestDispatcher("/home.jsp").forward(request, response);
		} else if ((authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_SELECTION) || (authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_ENROLLMENT_SELECTION) || (authenticationStatus == AuthenticationStatus.AWAITING_AUTHENTICATOR_VERIFICATION_DATA)) {
			System.out.println("Authentication Status is AWAITING_AUTHENTICATOR_SELECTION / AWAITING_AUTHENTICATOR_ENROLLMENT_SELECTION / AWAITING_AUTHENTICATOR_VERIFICATION_DATA");
			List<Authenticator> authenticators = authenticationResponse.getAuthenticators();
			request.getSession().setAttribute("proceedContext", authenticationResponse.getProceedContext());
			request.getSession().setAttribute("authenticators", authenticators);
			request.getRequestDispatcher("/selectauthenticator.jsp").forward(request, response);
		} else {
			System.out.println("Authentication Status non success" + authenticationStatus);
		}
		
		
		/*if (EmailValidator.emailValidator(username)) {
			user = OktaUserUtils.getUserByNameOrEmail(username);
			username = user.getUsername();
			
		}
		
		System.out.println("Test Statement");
		
		String sessionToken = OktaUserUtils.authenticateUser(username, password);
		
		String redirectURL = "https://" + PropertiesUtil.okta_org + "/oauth2/" + PropertiesUtil.server_id + "/v1/authorize?client_id=" + PropertiesUtil.client_id + "&response_type=code&redirect_uri=http://localhost:8080/demoapp/initialize.html&nonce=foo6&state=test&scope=openid profile email address offline_access&sessionToken=" + sessionToken;
		
		System.out.println("RedirectURL" + redirectURL);
				
		// Now send the user to the Okta home page
		//response.sendRedirect("https://www.racq.com.au/");
		response.sendRedirect(redirectURL);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
