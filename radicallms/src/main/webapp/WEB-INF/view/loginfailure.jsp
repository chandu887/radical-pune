<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
    
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta charset="utf-8">
        <title>HTC Login Page</title>
        <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" />
        <link href="<c:url value="/resources/css/font-awesome.css"/>" rel="stylesheet" />      
    </head>
     <body class="login">
        <div class="form-container">
            <h1>Partner Login</h1>
            <span class="error"> <i class="fa fa-times" aria-hidden="true"></i> Please enter Valid Username/password</span>
            <div class="form-container-inner">
                <form:form method="post" action="login" >
                    <ul>
                        <li><input type="text" name="userName" placeholder="Username/Email"/></li>
                        <li><input type="password" name="password" placeholder="Password"/></li>
                        <li><button type="submit">LOGIN</button></li>
                    </ul>
                </form:form>
            </div>
        </div>
    </body>
</html>