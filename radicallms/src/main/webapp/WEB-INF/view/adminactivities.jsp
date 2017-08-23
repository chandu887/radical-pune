<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
 
<meta charset="utf-8">
<title>LMS Dashboard</title>
<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/select2.css"/>"
	rel="stylesheet" />
<!-- Bootstrap -->
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.css">
</head>

<script src="<c:url value="/resources/js/jquery1_8_1.js" />"></script>
<script src="<c:url value="/resources/js/jquery.min.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap-select.min.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap-datepicker.min.js"/>"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.0/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
<script src='https://cloud.tinymce.com/stable/tinymce.min.js'></script>
<script src="https://cloud.tinymce.com/stable/tinymce.min.js?apiKey=p031zjwoastgbi16tmvaq8m8ef3dthcs5kqhdftdbwmcv77q"></script>
 <!--  <script>
  tinymce.init({
   
    
  });
  </script> -->
   <script>
  /* tinymce.init({
	  selector:''
  }); */
  tinymce.init({
	  selector: '#emailContentTextArea',
	  height: 200,
	  menubar: false,
	  plugins: [
	    'advlist autolink lists link image charmap print preview anchor',
	    'searchreplace visualblocks code fullscreen',
	    'insertdatetime media table contextmenu paste code'
	  ],
	  toolbar: 'undo redo | insert | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
	  content_css: '//www.tinymce.com/css/codepen.min.css'
	});
  tinymce.init({
	  selector:'#messageBody',
	  height: 200,
	  menubar: false,
	  plugins: [
	    'advlist autolink lists link image charmap print preview anchor',
	    'searchreplace visualblocks code fullscreen',
	    'insertdatetime media table contextmenu paste code'
	  ],
	  toolbar: 'undo redo | insert | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
	  content_css: '//www.tinymce.com/css/codepen.min.css'
	});
  </script>
  
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
	<script >
	$(document).ready(function(){
        $('#select_all').on('click',function(){
            if(this.checked){
            $('.checkbox').each(function(){
                this.checked = true;
            });
            }else{
                $('.checkbox').each(function(){
                    this.checked = false;
            });
                }
            });
    });
	</script>
	
<script>

function isBlank(inputStr) {
 	return !(inputStr && inputStr.length)
 }
function getCourseList(courseID,categeoryID) {
	var categeoryId = $("#"+categeoryID).val();
	var coursedropdown= $("#"+courseID);
	coursedropdown.empty();
	if (categeoryID == 'courseCategeory') {
		coursedropdown.append($('<option>', { value: '0', text: 'Select Course'}, '</option>'));
	}
	
    $.ajax({
        type : "post", 
        url : basepath + "/getCoursesBasedOnCategoryId", 
        data : "categeoryId=" + categeoryId,
        success : function(data) {
             for (i=0 ; i<data.length;i++) {
            	 coursedropdown.append($('<option>', { value: data[i].courseId, text: data[i].courseName}, '</option>'));
             }
             $("#"+courseID).selectpicker('refresh');
        },
	    error : function(e) {
         alert('Error: ' + e); 
        }
       });
      }

$(function () {
	                $("[rel='tooltip']").tooltip();
	            });
	            $(document).ready(function () {
	                $('.selectpicker').selectpicker();
	                $('.datepicker').datepicker({autoclose: true,});
	                $(".addlead-course").select2({
	                		                    placeholder: "Select a Course",
	                		                    allowClear: true,
												autoclose: true
												
	    	                  });
	            });
	            </script>

<script type="text/javascript">
var basepath = "${pageContext.request.contextPath}";
	$(document).ready(function() {

		
	});
	
	function validateAddAgentForm() {
		var name = $('#addName').val();
		var password = $('#addPassword').val();
		var email = $('#addEmail').val();
	 	 if(isBlank(name)) {
	          alert('Please enter name');
	              $('#addName').val("");
	              return false;
	     } else if(isBlank(password)) {
	         	alert('Please enter a valid password');
	                $('#addPassword').val("");
	        	return false;
	     }
	     else if(isBlank(email)) {
	      	alert('Please enter a email');
	             $('#addEmail').val("");
	     	return false;
	  	}	 	
	}
	
	function checkUserName() {
		var name = $('#addName').val();
		 $.ajax({
	  	        type : "post", 
	  	        url : basepath + "/isUserExits", 
	  	        data : "userName=" + name,
	  	        success : function(data) {
	  	             if (data == 'yes') {
	  	            	 alert('UserName already exits. Please enter another username');
	  	            	 $('#addName').val("");
	  	              	 return false;
	  	             }
	  	        },
	  		    error : function(e) {
	  	         alert('Error: ' + e); 
	  	        }
	  	       });
	}
	function getAgentInfo(userId) {
    	$.post(basepath + "/getAgentInfo?userId="+userId, function(data) {
    		$('#editUserId').val(data.userId);
    		$('#editName').val(data.userName);
    		$('#editPassword').val(data.password);
    		$('#editEmail').val(data.email);
    	});
    }
</script>
<body>
	<div class="container-fluid">
		<div class="row">
			<section id="header">
			<div class="row">
				<div class="col-md-8">
					<span class="logo"><a href="clearFilter"><img
							src="<c:url value="/resources/images/logo.png"/>" /></a></span>
				</div>
				<div class="col-md-4">
					<span class="pull-right account" style="color:blue;"> Welcome ! ${userInfo.userName}</br> <a href="logout" style="color:red;"> Logout</a></span>
				</div>
			</section>
			<section id="action">
			<ul class="default-filters">
				<li><a href="clearFilter"><i
				class="fa fa-home" aria-hidden="true"></i></a></li>
				<li><a href="dashboard?leadStatus=1" class="${newActive}"> Add/Edit Agents </a></li>
				<li><a href="dashboard?leadStatus=2" class="${openActive}"> Add/Edit Categories </a></li>
				<li><a href="dashboard?leadStatus=2" class="${openActive}"> Add/Edit Courses </a></li>
				
			</ul>
			</section>
		<a data-toggle="modal" role="button" data-target="#addAgent" class="btn btn-success">Add Agent</a>
		<p></p>
		<table class="table table-bordered">
						<thead>
							<tr>
								<th>Name</th>
								<th>Email</th>
								<th>Edit</th>
								<th>Activate/Deactivate</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${agentsList != null}">
								<c:forEach items="${agentsList}" var="agent">
									<tr>
										<td>${agent.userName}</td>
										<td>${agent.email}</td>
										<td><a data-toggle="modal" role="button"
											data-target="#editAgent" onclick="getAgentInfo(${agent.userId})"><i
												class="fa fa-pencil-square-o" aria-hidden="true"></i></a></td>
										<td>
										<c:if test="${agent.isActive == 0}">
										<a href="updateAgent?isActive=1" ><i class="fa fa-external-link" aria-hidden="true"></i> Activate</a>
										</c:if>
										<c:if test="${agent.isActive == 1}">
										<a href="updateAgent?isActive=0" ><i class="fa fa-external-link" aria-hidden="true"></i> Deactivate</a>
										</c:if>
										</td>
									</tr>
								</c:forEach>
							</c:if>
	
						</tbody>
					</table> 
		</div>
	</div>

	
	<div id="addAgent" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Add Agent</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="addAgent" id="addAgentForm" name="addAgentForm"
						onsubmit="return validateAddAgentForm()" role="form">
							<div class="form-group">
								<label for="email">Name</label> <input type="text"
									class="form-control" id="addName" name="userName"
									onchange="checkUserName()" 
									placeholder="Please enter Name">
							</div>
							<div class="form-group">
								<label for="email">Password</label> <input type="text"
									class="form-control" id="addPassword" name="password"
									placeholder="Please enter Password">
							</div>
							<div class="form-group">
								<label for="email">Email</label> <input type="text"
									class="form-control" id="addEmail" name="email"
									placeholder="Please enter email">
							</div>
							<button type="submit" class="btn btn-success">Add Agent</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	
	<!--Edit Agent-->
	<div id="editAgent" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Edit Agent</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="editAgent" name="editAgentForm"
						onsubmit="return validateEditAgentForm()" role="form">
						<input type="hidden" class="form-control" id="editUserId" value=""
							name="userId">
							<div class="form-group">
								<label for="email">Name</label> <input type="text"
									class="form-control" id="editName" name="userName"
									placeholder="Please enter Name">
							</div>
							<div class="form-group">
								<label for="email">Password</label> <input type="text"
									class="form-control" id="editPassword" name="password"
									placeholder="Please enter Password">
							</div>
							<div class="form-group">
								<label for="email">Email</label> <input type="text"
									class="form-control" id="editEmail" name="email"
									placeholder="Please enter email">
							</div>
							<button type="submit" class="btn btn-success">Update Agent</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
					</form:form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>