<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.okta.idx.sdk.api.model.Idp"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
  <head>
      <title>OIE demo</title>
      <link rel="stylesheet" href="css/logincustom.css">
  </head>
  <body>
    <div class="formcontainer">
      <div class="formsubcontainer">
          <img src="images/rsz_acmelogo.png" alt="CS logo" width="120" class="logo">
          <h3 class="getstarted">Let's get you started.</h3>
          <form action="/oiedemo/logincustom" onsubmit="return validateForm()" method="POST" class="userform">            
            <input type="text" id="username" name="username" class="username" placeholder="Username or Email">
            <br>
            <input type="password" id="password" name="password" class="username" placeholder="Password">
            <br>
            <input type="submit" value="Sign in" class="submit">
            <br>
            <br>
            <%
	            List<Idp> idps = (List<Idp>)request.getAttribute("idps");
	            for (Idp idp: idps) {
	        %>
	            	<input type="button" class="idpbuttons" onclick='location.href="<%=idp.getHref()%>";' value="Sign in with <%=idp.getType()%>" />
	            	<br>
	            	<br>
	         <% 
	            }
	         %>
            <span class="newtohumm">New here? </span><a href="/oiedemo/signup.html" class="signup">Sign up</a>
            <br>
            <br>
            <a href="/oiedemo/forgotpassword.html" class="idps">Forgot Password?</a>
            <br>
            <br>
        </form> 
      </div>
    </div>      
  </body>
</html>