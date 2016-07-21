<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
    
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta charset="utf-8">
        <title>LMS Login Page</title>
        <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" />
        <link href="<c:url value="/resources/css/font-awesome.css"/>" rel="stylesheet" /> 
        <script src="<c:url value="/resources/js/jquery1_8_1.js" />"></script> 
       <script>
         function checkLoginDetails() {
        	var userName = $('#userName').val();
        	var password = $('#password').val();
        	if(userName == ""){
        		alert("Please enter userName");
        		return false;
        	} else if (password == "") {
        		alert("Please enter password");
        		return false;
        	}
        	return true;
        }
     </script>         
    </head>
     <body class="login">
        <div class="form-container">
            <h1>Admin Login</h1>
            <div class="form-container-inner">
                <form:form method="post" action="login" onsubmit="return checkLoginDetails()">
                    <ul>
                        <li><input type="text" name="userName" id="userName" placeholder="Username/Email"/></li>
                        <li><input type="password" name="password" id="password" placeholder="Password"/></li>
                        <li><button type="submit">LOGIN</button></li>
                    </ul>
                </form:form>
                
               <!--   <a href="${pageContext.request.contextPath}/download/1">test</a> -->
                
            </div>
        </div>
    </body>
</html>