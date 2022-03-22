<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.okta.idx.sdk.api.model.FormValue"%>
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
          <h3 class="reglabel">Enter the registration details.</h3>
          <form action="/oiedemo/registeruser" method="POST" class="userform"> 
          	<%
	            FormValue[] formValues = (FormValue[])session.getAttribute("formValues");
	            for (FormValue formValue: formValues) {
	        %>           
	            <input type="text" id="<%=formValue.getName()%>" name="<%=formValue.getName()%>" class="username" placeholder="<%=formValue.getLabel()%>">
	            <br>  
             <% 
	            }
	         %>          
            <input type="submit" value="Sign up" class="submit">
            <br>
            <br>
            <span class="alreadyhaveaccount">Already have an account? </span><a href="/oiedemo/logincustom.html" class="signup">Sign in</a>
        </form> 
      </div>
    </div>      
  </body>
</html>