<%@ page language="java" contentType="text/html; charset=ISO-8859-1"    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Images</title>
<style>
		body {
				margin: 0; 
				padding: 0;
				font-family: tahoma; 
				background: #e4e4e4
			}
		.container {
				width: 794px; 
				margin: 0 auto; 
				background: #fff;
			}
		.pannel-header {
				padding: 15px 25px;
			}
		.pannel-header .title {
				float: right; 
				margin: 5px 0 0 0;
			}
		.location_info{}.location_info ul {
				margin: 0; 
				list-style: none;
				text-align: center; 
				padding: 5px 0; 
				color: #fff;
			}
		.location_info li { 
				display: inline-block; 
				margin: 0 15px 0 0;
			}
		.panel-body {
				padding: 25px 0;
			}
		.panel-body ul {
				margin: 0; 
				padding: 0; 
				list-style: none; 
				text-align: center;
			}
		.panel-body ul li { 
				width: 320px;
				margin: 0 18px 30px 18px; 
				display: inline-block; 
			/*	border:5px solid #8bc750; 
				border-radius: 5px; */
			}
		.panel-body ul li img {
				width: 320px; 
				height: 250px; 
				display: block; 
				/* border-radius: 5px 5px 0 0; */
				border:5px solid #8bc750; 
				border-radius: 5px;
			}
		.panel-body ul li .title { 
				background: #8bc750; 
				text-align: center; 
				padding: 10px 0; 
				width: 100%; 
				display: block; 
				color: #fff;
			}
		.footer { 
				background: #646464; 
				text-align: center;
				padding: 10px 0; 
				color: #fff;
			}
	</style>
</head>
<body>
 <form:form method="post" action="viewImages" name="viewImagesForm"	commandName="viewImagesForm">
	<div class='container'>
		<div class='pannel-header'>
	 	<!-- <img src='http://148.72.249.144/MCNEW/resources/images/logo.png' /> -->	
		 	<img src='/resources/images/logo.png' /> 	
		<span class='title'>Radical Mailer</span>
		</div>
		<div class='location_info' style='background-color: red; padding: 5px 0; height: 50px; width: 100%;'>
			</div>
				<div class='panel-body'>
					<ul> 
						<%-- <c:forEach items="${reportImagesList}" var="reportEntiy"> --%>
							<li><img src='${reportImagesList}'/> 
							<%-- <p>${reportEntiy.type}</p>
							</li>	
						  
						</c:forEach>
					</ul> --%>
				</div>
		<div class='footer'>Radical Technologies</div>
	</div>
</form:form>	
</body>
</html>