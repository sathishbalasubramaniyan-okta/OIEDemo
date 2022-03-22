<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.okta.demo.util.PropertiesUtil"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.io.FileNotFoundException"%>
<%@ page import="com.okta.demo.exception.InternalException"%>

    
<%
	// Load all properties and set up the redirect uri
	String absolutePath = this.getClass().getClassLoader().getResource("").getPath();
    System.out.println("absolutePath=" + absolutePath);
    FileInputStream fis = new FileInputStream(absolutePath + "okta-api.properties");
	PropertiesUtil.loadProperties(fis);
	
	String clientId = PropertiesUtil.client_id_widget;
	String redirectUrl = PropertiesUtil.redirect_url_widget;
	String serverId = PropertiesUtil.server_id;
	String projectName = PropertiesUtil.project_name;
	String customerName = PropertiesUtil.customer_name;
	String customer_web = PropertiesUtil.customer_web;
	String oktaOrg = PropertiesUtil.okta_org;
	String logo = PropertiesUtil.logo;
	String banner = PropertiesUtil.banner;
	String background = PropertiesUtil.background1;
	String googleServerId = PropertiesUtil.google_idp;
%>
<!doctype html>
<html>
  <head>
      	<title>Demo app</title>
      	<meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	
	    
	    <script src="https://global.oktacdn.com/okta-signin-widget/5.13.0/js/okta-sign-in.min.js" type="text/javascript"></script>
	
	    <link href="https://global.oktacdn.com/okta-signin-widget/5.13.0/css/okta-sign-in.min.css" type="text/css" rel="stylesheet"/>
	    
	    <link rel="stylesheet" href="css/login.css">
  </head>
  <body>

      <div id="okta-login-container"></div> 
        <script type="text/javascript">
          var orgUrl = 'https://<%=oktaOrg%>';
          var pageContext = window.location.protocol + "//" + window.location.host;
          var signIn = new OktaSignIn({
        	el: '#okta-login-container',
            baseUrl : orgUrl,
            logo : "images/<%=logo%>",
            clientId : '<%=clientId%>',
            redirectUri : '<%=redirectUrl%>',
            useInteractionCodeFlow: true,
            authParams : {                  
              issuer: orgUrl + '/oauth2/<%=serverId%>',
              authorizeUrl: orgUrl + '/oauth2/<%=serverId%>/v1/authorize',
              // `display: page` will initiate the OAuth2 page redirect flow
              display : 'page',
              pkce: true,
              scopes : [  'openid','email','profile']
            },            
            features : {
              registration : true,
              rememberMe : true,
              //smsRecovery : true,
              //multiOptionalFactorEnroll : true,
              selfServiceUnlock : true,
              autoPush : true,
              idpDiscovery: false,
              //passwordlessAuth: true
            }
          });

          signIn.showSignInAndRedirect().catch(function(error) {
        	  // This function is invoked with errors the widget cannot recover from:
        	  // Known errors: CONFIG_ERROR, UNSUPPORTED_BROWSER_ERROR
        	});
        </script>
     
  </body>
</html>