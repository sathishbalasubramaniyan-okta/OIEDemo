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
<!DOCTYPE html>
<html>
<!-- 
This html file is only used to extract the hashed access token parameter. Nothing else!!
It will then submit an auto generated form with the access token as a standard parameter
so it can be read by the processing servlet at /nzqa/load-user-home.html    
--> 
<head>
<meta charset="UTF-8">
<title>Initialize</title>
<script src="https://global.oktacdn.com/okta-signin-widget/7.1.3/js/okta-sign-in.min.js" type="text/javascript"></script>
<link href="https://global.oktacdn.com/okta-signin-widget/7.1.3/css/okta-sign-in.min.css" type="text/css" rel="stylesheet"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script>

// This function extracts the Access Token from the hashed parameters and then submits an 
// auto-geneted form.
$(function() {
	var interactionCode = null;
	var pageContext = window.location.protocol + "//" + window.location.host;
	
	var params = new URLSearchParams(window.location.search);
	
	// If hashed parameters have been returned
	if (params.has('interaction_code')) {
		console.log("Found interaction code!!!");
		interactionCode = params.get('interaction_code');
		//console.log("access_token=" + accessToken)
	}
	
	var orgUrl = 'https://<%=oktaOrg%>';
    var pageContext = window.location.protocol + "//" + window.location.host;
    var signIn = new OktaSignIn({
      baseUrl : orgUrl,
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
    
  
    	
   	signIn.authClient.token.parseFromUrl()
   	.then(function(res) {
   	  var state = res.state; // passed to getWithRedirect(), can be any string

   	  // manage token or tokens
   	  var tokens = res.tokens;
   	  
   	  var method = "post";
	  var url = pageContext + "/<%=projectName%>/landingwidget.html";
	  var form = document.createElement("form");
	  form.setAttribute("method", method);
	  form.setAttribute("action", url);
	  
	  var hiddenField = document.createElement("input");
	    hiddenField.setAttribute("type", "hidden");
	    hiddenField.setAttribute("name", "idToken");
	    hiddenField.setAttribute("value", tokens.idToken.idToken);
	    form.appendChild(hiddenField);
	    
	    var hiddenField = document.createElement("input");
	    hiddenField.setAttribute("type", "hidden");
	    hiddenField.setAttribute("name", "accessToken");
	    hiddenField.setAttribute("value", tokens.accessToken.accessToken);
	    form.appendChild(hiddenField);
	    
	    document.body.appendChild(form);
		form.submit();

   	 
   	})
   	.catch(function(err) {
   	  // handle OAuthError
   	});

});
</script>
</head>
<body>
</body>
</html>