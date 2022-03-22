<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.okta.idx.sdk.api.client.Authenticator"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>OIE demo</title>
		<link rel="stylesheet" href="css/logincustom.css">
	</head>
	<body>
		<script>
	      	function validateForm() {      		
	      		var authenticators = document.getElementsByName('authenticators');
	      		var selectedauthenticator = document.getElementById("selectedauthenticator");
	      		for(var i = 0; i < authenticators.length; i++) {
	                if(authenticators[i].checked) {
	                	selectedauthenticator.value = authenticators[i].value;
	                }
	            }
	      		
	      		if (!selectedauthenticator.value) {
	      			alert('Please select an authenticator');
	      			return false;
	      		}
	      	}
	     </script>
		<div class="formcontainer">
	      <div class="formsubcontainer">
	          <img src="images/rsz_acmelogo.png" alt="CS logo" width="120" class="logo">
	          <h3 class="getstarted">Select Authenticator</h3>
	          <form action="/oiedemo/selectauthenticator" onsubmit="return validateForm()" method="POST" class="userform">    
	            <%
	            List<Authenticator> authenticators = (List<Authenticator>)request.getSession().getAttribute("authenticators");
	            for (Authenticator authenticator: authenticators) {
	            %>
	            	<input type="radio" name="authenticators" value="<%=authenticator.getId() %>"><%=authenticator.getLabel() %>
	            	<br>
	            <% 
	            }
	            %>        
	            <br>
	            <input type="submit" value="Proceed" class="submit">
	            <br>
	            <input type="hidden" id="selectedauthenticator" name="selectedauthenticator">
	        </form> 
	      </div>
	    </div>
	</body>
</html>