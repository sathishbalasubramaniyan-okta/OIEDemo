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
import com.okta.idx.sdk.api.model.Idp;
import com.okta.idx.sdk.api.model.VerifyAuthenticatorOptions;
import com.okta.idx.sdk.api.response.AuthenticationResponse;
import com.okta.idx.sdk.api.response.TokenResponse;

import com.okta.idx.sdk.api.exception.ProcessingException;
/**
 * Servlet implementation class LoginCustom
 */
@WebServlet("/LoginCustom")
public class LoginCustom extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(LoginCustom.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginCustom() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() {
    	//LoadProperties.loadProperties(getClass().getResourceAsStream(PROP_FILE));
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("doGet() - Start");
		// Setup the correct URL Root Context
		ContextHelper.setRootContect(request);
				
		Set<String> scopes = new HashSet<String>();
		scopes.add("openid");
		scopes.add("profile");
		scopes.add("email");
		
		
		System.out.println("In LoginCustom servlet");
		if(!PropertiesUtil.propsLoaded) {
	    	String absolutePath = this.getClass().getClassLoader().getResource("").getPath();
	    	try {
	    		String host = request.getHeader("host");
	    		String demoName = host.substring(0, host.indexOf("."));
	    		System.out.println("Demo Name: " + demoName);
	    		PropertiesUtil.demo_name = demoName;
				FileInputStream fis = new FileInputStream(absolutePath + PROP_FILE);
				PropertiesUtil.loadProperties(fis);
			} catch (FileNotFoundException e) {
				logger.fatal("Failed to load property file: " + PROP_FILE + " cause: " + e.getMessage());
				throw new InternalException("Failed to load property file: " + PROP_FILE + " cause: " + e.getMessage());
			}
    	}
		
		IDXAuthenticationWrapper idxAuthenticationWrapper = new IDXAuthenticationWrapper("https://" + PropertiesUtil.okta_org + "/oauth2/" + PropertiesUtil.server_id,PropertiesUtil.client_id,PropertiesUtil.client_secret,scopes,PropertiesUtil.redirect_url);
		
		AuthenticationResponse beginResponse = idxAuthenticationWrapper.begin();
		ProceedContext proceedContext = beginResponse.getProceedContext();
		
		List<Idp> idps = beginResponse.getIdps();
		
		request.setAttribute("idps", idps);
		
		
		request.getSession().setAttribute("idxAuthenticationWrapper", idxAuthenticationWrapper);
		request.getSession().setAttribute("proceedContext", proceedContext);
		
		request.getRequestDispatcher("/logincustom.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
