package com.okta.demo.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.okta.demo.exception.InternalException;
import com.okta.demo.util.ContextHelper;
import com.okta.demo.util.OktaMfaUtils;
import com.okta.demo.util.OktaUserUtils;
import com.okta.demo.util.PropertiesUtil;
import com.okta.demo.util.PasswordGenerator;
import com.okta.demo.obj.User;


/**
 * Servlet implementation class UpdateUser
 */
@WebServlet("/UpdateUser")
public class UpdateUser extends HttpServlet {
	private static final String PROP_FILE = "okta-api.properties";
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(UpdateUser.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUser() {
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
		logger.debug("doGet() - Start");
		String id = ""; 
		String activateURL = ""; 		
		String username = "";
		String sessionToken = "";
		String email = "";
		
		// Setup the correct URL Root Context
		ContextHelper.setRootContect(request);
		
		if (request.getParameter("email") != null) {
			email = request.getParameter("email");
			logger.debug("Passed email=" + email);
		}
		
		HttpSession session = request.getSession();
		id = (String)session.getAttribute("id");
		username = (String)session.getAttribute("username");
		sessionToken = (String)session.getAttribute("sessionToken");
		
		User user = new User();
		user.setId(id);
		user.setUsername(username);
		user.setEmail(email);
		user.setEmailVerified("Unverified");
		
		OktaUserUtils.updateUser(user);
		OktaUserUtils.updateUser(user);
		activateURL = OktaMfaUtils.enrollEmail(id, email);
		
		
		session.setAttribute("activateURL", activateURL);
		
		String redirectURL = "https://" + PropertiesUtil.okta_org + "/oauth2/" + PropertiesUtil.server_id + "/v1/authorize?client_id=" + PropertiesUtil.client_id + "&response_type=code&redirect_uri=http://localhost:8080/Humm/initialize.html&nonce=foo6&state=test&scope=openid profile email address offline_access&sessionToken=" + sessionToken;
		
		System.out.println("RedirectURL" + redirectURL);
				
		// Now send the user to the Okta home page
		//response.sendRedirect("https://www.racq.com.au/");
		response.sendRedirect(redirectURL);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
