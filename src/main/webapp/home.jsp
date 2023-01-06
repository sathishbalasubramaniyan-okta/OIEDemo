<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="com.okta.demo.obj.LinkedIdp"%>
<%@page import="com.okta.demo.obj.UserApp"%>
<%@page import="com.okta.demo.obj.User"%>
<%@ page import="com.okta.demo.util.PropertiesUtil"%>
<%
	String googleClientId = PropertiesUtil.google_client_id;
	String googleIdp = PropertiesUtil.google_idp;
	String projectName = PropertiesUtil.project_name;
	String oktaOrg = PropertiesUtil.okta_org;
	String logo = PropertiesUtil.logo;
	String banner = PropertiesUtil.banner;
	String background = PropertiesUtil.background1;
	String customerWeb = PropertiesUtil.customer_web;
	String googleIdpName = PropertiesUtil.google_idp_name;

	//Welcome&nbsp;&nbsp;
	String name = "";
	String email = "";
	String firstName="";
	String lastName="";
	String accessToken = "";
	String idToken = "";
	
	
	if (request.getSession().getAttribute("name") != null) {
		name = (String) request.getSession().getAttribute("name");
	}
	if (request.getSession().getAttribute("accessToken") != null) {
		accessToken = (String) request.getSession().getAttribute("accessToken");
	} else {
		response.sendRedirect("http://sathish-demo-ocis.localhost:8080/oiedemo/logincustom.html");
	}
	if (request.getSession().getAttribute("idToken") != null) {
		idToken = (String) request.getSession().getAttribute("idToken");
	}
	if (request.getSession().getAttribute("email") != null) {
		email = (String) request.getSession().getAttribute("email");
	}
	if (request.getSession().getAttribute("firstName") != null) {
		firstName = (String) request.getSession().getAttribute("firstName");
	}
	if (request.getSession().getAttribute("lastName") != null) {
		lastName = (String) request.getSession().getAttribute("lastName");
	}
	
	System.out.println("In home.jsp");
%>
<html>
  <head>
      <title>OIE demo</title>
      <link rel="stylesheet" href="css/demo.css">
  </head>
  <body onload="processModal()">
    <script type="text/javascript">
      function logout() {
    	console.log("Get Signout call start ...");	
    	var pageContext = window.location.protocol + "//" + window.location.host;
    	var method = "post";
    	var url = pageContext + '/<%=projectName%>/logout';
    	var form = document.createElement("form");
    	form.setAttribute("method", method);
    	form.setAttribute("action", url);
    	document.body.appendChild(form);
    	form.submit();
      }
      
      function verifyEmail() {
    	var otp = document.getElementById("otp").value;  		     		
   		      		
   		if ((!otp) || (otp.trim() === "")) {
   			alert('otp should not be empty');
   			return false;
   		}
    	var pageContext = window.location.protocol + "//" + window.location.host;
      	var method = "post";
      	var url = pageContext + "/demoapp/verifyEmail";
      	var form = document.createElement("form");
      	form.setAttribute("method", method);
      	form.setAttribute("action", url);
      	
      	var hiddenField = document.createElement("input");
	    hiddenField.setAttribute("type", "hidden");
	    hiddenField.setAttribute("name", "passCode");
	    hiddenField.setAttribute("value", otp);
	    form.appendChild(hiddenField);
	    
      	document.body.appendChild(form);
      	form.submit();
      }
      function viewApps() {
        var appsTable = document.getElementById("appsTable");
        appsTable.style.display = "table";
      }
      function processModal() {
        // Get the modal
        var modal = document.getElementById("jwtModal");

        // Get the button that opens the modal
        var btn = document.getElementById("technicalDetails");

        // Get the <span> element that closes the modal
        var span = document.getElementsByClassName("close")[0];

        // When the user clicks on the button, open the modal 
        btn.onclick = function() {
          modal.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span.onclick = function() {
          modal.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
          if (event.target == modal) {
            modal.style.display = "none";
          }
      }
    }        
    </script>
    <header id="header">
      <div class="welcome">Welcome <%=name%>!</div>
      <a href="#" class="logout" onclick="logout()">Logout</a> 
    </header>
    <div id="jwtModal" class="jwt-modal">
      <!-- Modal content -->
      <div class="modal-content">
        <span class="close">&times;</span>
        <p>ID Token</p>
        <textarea class="token-area" id="idtoken" rows="10" cols="120"><%=idToken%></textarea>
        <p>Access Token</p>
        <textarea class="token-area" id="accesstoken" rows="10" cols="120"><%=accessToken%></textarea>
        <p>Refresh Token</p>
        <textarea class="token-area" id="refreshtoken" rows="10" cols="120"></textarea>
      </div>
    </div>
    <main id="main">
      <section id="content">
        <div class="content-box">
          <div class="name"><%=name%></div>
          <div>Here is your profile information</div>
          <div class="profile-box">
              <div class="profile-box-header">
                
                <button id="technicalDetails" class="technical-details">Technical Details</button>
              </div>
              <table class="profile-table">
                <tr>
                  <td>Email</td>
                  <td class="attrvalue"><%=email%></td>
                </tr>
                <tr>
                  <td>First Name</td>
                  <td class="attrvalue"><%=firstName%></td>
                </tr>
                <tr>
                  <td>Last Name</td>
                  <td class="attrvalue"><%=lastName%></td>
                </tr>
                <tr>
                  <td>Full Name</td>
                  <td class="attrvalue"><%=name%></td>
                </tr>
                <tr>
                  <td>Mobile Number</td>
                  <td class="attrvalue"></td>
                </tr>
              </table>
           
          </div>
        </div>
      </section>
      <aside id="sidebar">
        <div class="app-box">
            <div class="app-box-header">
              <span>List of apps</span>
              <button id="viewApps" onclick="viewApps()">View Apps</button>
            </div>
            <table id="appsTable" class="apps-table">
            <%
				ArrayList<UserApp> appList = (ArrayList<UserApp>)session.getAttribute("userApps");

				if (appList != null && appList.size() > 0) {
					for (int i = 0; i < appList.size(); i++) {
						UserApp userApp = (UserApp) appList.get(i);
						String appName = userApp.getLabel();						
			%>
              <tr>
                <td class="app-links"><a href="<%=userApp.getLinkUrl()%>" target="_blank">
                	<img src="<%=userApp.getLogoUrl()%>">
                	<span><%=appName%></span>
                </a></td>              
              </tr>
              <%
					}
				}
              %>
            </table>
          </div>
      </aside>
    </main>
    <footer id="footer">This is a demo application, not copyrighted!</footer>    
  </body>
</html>