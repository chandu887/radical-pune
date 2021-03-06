<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="refresh" content="600" >
<meta charset="utf-8">
<title>LMS Dashboard</title>
<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/select2.css"/>" rel="stylesheet" />
<!-- Bootstrap -->
<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/font-awesome.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/bootstrap-select.min.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/bootstrap-datepicker.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/jquery-ui.css"/>" rel="stylesheet" />
</head>

<script src="<c:url value="/resources/js/jquery1_8_1.js" />"></script>
<script src="<c:url value="/resources/js/jquery.min.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap-select.min.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap-datepicker.min.js"/>"></script>

<script src="<c:url value="/resources/js/jquery-ui.js"/>"></script>

<script src='/resources/js/tinymce.min.js'></script>
<!-- <script src="https://cloud.tinymce.com/stable/tinymce.min.js?apiKey=p031zjwoastgbi16tmvaq8m8ef3dthcs5kqhdftdbwmcv77q"></script> -->

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
  <script>
		function viewImages(reportId){
			//alert("Hi");
			window.open( basepath + "/viewImages/"+reportId);
			
			 		}
		</script>
</head>
  
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>
	<script >
	  function numbervalidation() {
      	var regex = /^[0-9\b]+$/;
      	var value = $("#mobileNo").val();
      	 if (!regex.test(value)) {
      		 $("#mobileNo").val("");
               alert("Please enter numerics only");
               return false;
            }
	  }
	  function checkEmail() {
          var email = document.getElementById('emailId');
          var emailId = email.value;
          var filter = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	            if (!filter.test(email.value)) {
	            	 alert('Please provide a valid email address');
	            }
	  }
	  
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
        
        $("#addManualName").hide();
        $("#filterCourseManualDiv").hide();
    });
	
	updateList = function() {
		  var input = document.getElementById('file');
		  var output = document.getElementById('fileList');

		  output.innerHTML = '<ul>';
		  for (var i = 0; i < input.files.length; ++i) {
		    output.innerHTML += '<li>' + input.files.item(i).name + '</li>';
		  }
		  output.innerHTML += '</ul>';
		}
	updateManulaList = function() {
		  var input = document.getElementById('manualEmailFile');
		  var output = document.getElementById('manualEmailFileList');

		  output.innerHTML = '<ul>';
		  for (var i = 0; i < input.files.length; ++i) {
		    output.innerHTML += '<li>' + input.files.item(i).name + '</li>';
		  }
		  output.innerHTML += '</ul>';
		}
	updateSendNonTemplateFileList = function() {
		  var input = document.getElementById('sendNonTemplateFile');
		  var output = document.getElementById('sendNonTemplateFileList');

		  output.innerHTML = '<ul>';
		  for (var i = 0; i < input.files.length; ++i) {
		    output.innerHTML += '<li>' + input.files.item(i).name + '</li>';
		  }
		  output.innerHTML += '</ul>';
		}
	
	function checkFileExsistsOrNot() {
		var fileValue = $("#file").val();
		if(null == fileValue || fileValue == "") {
			alert("Please select the file");
			return false;
		}
		else {
			return true;
		}
	}
	
	</script>
	<!-- <script>
	$("#fromDate").on('changeDate', function(ev){
	    $(this).datepicker('hide');
	});
		
            $(function () {
               $("#fromDate").datepicker({ autoclose: true });
               $("#toDate").datepicker({ autoclose: true });
				
            });
        </script>
 -->
<script>

function validateAddLeadform() {
	var mobileNo = $('#mobileNo').val();
	var courseId = $('#addCourseName').val();
	var manualCourse = $('#addManualNameValue').val();
	 var courseCategeoryName = $('#courseCategeoryName').val();
	 	 var leadSource = $('#leadSource').val(); 
	 	 var center = $('#center').val(); 
	 	var mode = $('#mode').val(); 
	 	var assigned = $('#assigned').val(); 
	 	var type = $('#type').val(); 
	 	var labelType = $('#labelType').val(); 
	 	var statusType = $('#statusTypeAdd').val();
	 	
	 	 var mobileNumberChecking = /^[7-9][0-9]*$/;
	 	 /* if(!isBlank(mobileNo) && !isInteger(mobileNo)) {
	          alert('Please enter a valid mobile no.');
	              $('#mobileNo').val("");
	              return false;
	     	} else if(mobileNo.length < 10 && mobileNo.length > 10) {
	         	alert('Please enter a valid mobile no.');
	                $('#mobileNo').val("");
	        	return false;
	     	} else if (!mobileNo.match(mobileNumberChecking)) {
	      		alert('Please Enter a valid Mobile No.');
	                $('#mobileNo').val("");
	        	return false;
	     	}  else  */if(assigned == 0){
	     		alert('Please select Assigned to.');
	     		return false;
	     	} else if(leadSource == null || leadSource ==0){
	     		alert('Please select Lead Source.');
	     		return false;
	     	} else if(courseCategeoryName==0 || courseCategeoryName==null){
	     		alert('Please select Category.');
	     		return false;
	     	} else if (courseCategeoryName != 21 && ( courseId == 0 || courseId == null )) {
	     		alert('Please select Course.');
	     		return false;
	     	} else if(mode == 0){
	     		alert('Please select Mode of training.');
	     		return false;
	     	} else if(type == 0){
	     		alert('Please select Type of training.');
	     		return false;
	     	} else if(center == 0){
	     		alert('Please select Location.');
	     		return false;
	     	} 
	 	 
	 	     if(labelType ==0 || labelType == null){
	 	    	$('#statusTypeAdd').val("1"); 
	 	     }
	 	     if(labelType == 0 || labelType == null){
	 	    	$('#labelType').val("0"); 
	 	     }
	 	 
	  	return true;
	  }
	 function isInteger(s) {
	 	return (s.toString().search(/^-?[0-9]+$/) == 0);
	 }
	 function isBlank(inputStr) {
	 	return !(inputStr && inputStr.length)
	 }
	
function getCourseList(courseID,categeoryID,filterNewCourseDiv,filterCourseManualNewDiv) {
	var categeoryId = $("#"+categeoryID).val();
	if(categeoryId == 21){
		$("#"+filterNewCourseDiv).hide();
		$("#"+filterCourseManualNewDiv).show();
	} else {
		$("#"+filterCourseManualNewDiv).hide();
		$("#"+filterNewCourseDiv).show();
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
 }

function getCourseListForAddAndEditLead(courseID,categeoryID,courseDiv, manualCourseDiv) {
	var categeoryId = $("#"+categeoryID).val();
	if (categeoryId == 21) {
		$("#"+courseDiv).hide();
		$("#"+manualCourseDiv).show();
	} else {
		$("#"+courseDiv).show();
		$("#"+manualCourseDiv).hide();
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
</script>

<script type="text/javascript">
var basepath = "${pageContext.request.contextPath}";
	$(document).ready(function() {
		
		$(function () {
			var availableTags = [];
			
			<c:forEach var="category" items="${courseCategories}">
				var categoryName = '${category.value}';
				availableTags.push(categoryName);
			</c:forEach>
			
			<c:forEach var="course" items="${coursesMap}">
				var courseName = '${course.value}';
				availableTags.push(courseName);
			</c:forEach>
		    $("#courseList").autocomplete({
		        source: availableTags
		    });
		});


		$("#changeStatusButton").click(function() {
			var leadIds = [];
			$.each($("input[name='leadId']:checked"), function() {
				leadIds.push($(this).val());
			});
			if (leadIds.length == 0) {
				alert("please select any enquiry");
			} else {
				$('#changeStatusLeadIds').val(leadIds.join(","));
				$("#changeStatusForm").submit();
			}
		});

		$("#leadConvertedToCloseButton").click(function() {
			var leadIds = [];
			$.each($("input[name='leadId']:checked"), function() {
				leadIds.push($(this).val());
			});
			if (leadIds.length == 0) {
				alert("please select any enquiry");
			} else {
				$('#leadConvertedLeadIds').val(leadIds.join(","));
				$('#leadConvertedStatus').val("3");
				$("#changeStatusSuccesForm").submit();
			}
		});

		$("#leadConvertedToOpenButton").click(function() {
			var leadIds = [];
			$.each($("input[name='leadId']:checked"), function() {
				leadIds.push($(this).val());
			});
			if (leadIds.length == 0) {
				alert("please select any enquiry");
			} else {
				$('#leadConvertedLeadIds').val(leadIds.join(","));
				$('#leadConvertedStatus').val("2");
				$("#changeStatusSuccesForm").submit();
			}
		});

		$("#leadFailedToConvertedToCloseButton").click(function() {
			var leadIds = [];
			$.each($("input[name='leadId']:checked"), function() {
				leadIds.push($(this).val());
			});
			if (leadIds.length == 0) {
				alert("please select any enquiry");
			} else {
				$('#leadFailedToCloseingLeadIds').val(leadIds.join(","));
				$('#leadFailedToClosingStatus').val("3");
				$("#changeStatusFailureToCloseForm").submit();
			}
		});

		$("#leadDeleteButton").click(function() {
			var leadIds = [];
			$.each($("input[name='leadId']:checked"), function() {
				leadIds.push($(this).val());
			});
			var password = $('#deletePassword').val();
			var dbPassword =  $('#deleteDbPassword').val();
			if (leadIds.length == 0) {
				alert("please select any enquiry");
			} else if((null==password || password=="")){
				alert("Please enter password ..!")
			} else if(password  != dbPassword){
				alert("Incorrect password ..! Please enter correct Password")
			} else {
				$('#deleteLeadIds').val(leadIds.join(","));
				$('#deleteStatus').val("4");
				$("#changeStatusDeleteForm").submit();
			}
		});

		$("#downloadLeadsFilter").click(function() {
			var courseCategeory =  $('#courseCategeory').val();
			var toDate = $('#toDate').val();
			var fromDate = $('#fromDate').val();
			if((null==fromDate || fromDate=="") && (null == toDate || toDate=="")/* && (null==courseCategeory || courseCategeory == 0) */){
				/*  alert("Please select dates ");/* Or course Categeory */
              	/*return false; */
              	$('#toDate').val("");
              	$('#fromDate').val("");
              	
			} else if((null==fromDate || fromDate=="") || (null == toDate || toDate=="")){
				$('#toDate').val("");
              	$('#fromDate').val("");
			} 
			$('#filterType').val("1");
			$("#filterByDateAndCourseForm").submit();
			
		});
		$("#showLeadsFilter").click(function() {
			var courseCategeory =  $('#courseCategeory').val();
						var toDate = $('#toDate').val();
						var fromDate = $('#fromDate').val();
						if((null==fromDate || fromDate=="") && (null == toDate || toDate=="")/* && (null==courseCategeory || courseCategeory == 0) */){
							/*  alert("Please select dates ");/* Or course Categeory */
			              	/*return false; */
			              	$('#toDate').val("");
			              	$('#fromDate').val("");
			              	
						} else if((null==fromDate || fromDate=="") || (null == toDate || toDate=="")){
							$('#toDate').val("");
			              	$('#fromDate').val("");
						} 
						$('#filterType').val("0");
						$("#filterByDateAndCourseForm").submit();
						
		});
		
		$("#download").click(function() {
			var leadIds = [];
			$.each($("input[name='leadId']:checked"), function() {
				leadIds.push($(this).val());
			});
			var password = $('#downloadPassword').val();
			var dbPassword =  $('#dbPassword').val();
			if (leadIds.length == 0) {
				alert("please select any enquiry");
			} else if((null==password || password=="")){
				alert("Please enter password ..!")
			} else if(password  != dbPassword){
				alert("Incorrect password ..! Please enter correct Password")
			} else {
				$('#downloadLeadIds').val(leadIds.join(","));
				$("#downloadLeadsToSheetForm").submit();
			}
		});
		
		$("#sendTemplatedMail").click(function() {
			var leadIds = [];
			$.each($("input[name='leadId']:checked"), function() {
				leadIds.push($(this).val());
			});
			if (leadIds.length == 0) {
				alert("please select any enquiry");
			} else {
			$('#sendTemplatedMailLeadIds').val(leadIds.join(","));
			$("#sendTemplatedMailForm").submit();
			}
		});
		
        $("#showingId").change(function() {
        	$("#getShowingForm").submit();
        });
        $('#emailRadio').change(function () {
            document.getElementById('smsDiv').style.display  = 'none';
            document.getElementById('mailDiv').style.display = 'block';
         });
         $('#smsRadio').change(function () {
        	 document.getElementById('mailDiv').style.display = 'none';
             document.getElementById('smsDiv').style.display = 'block';
         });
         
         $("#sendNonTemplatedMailOrSms").click(function() {
        	 var leadIds = [];
 			$.each($("input[name='leadId']:checked"), function() {
 				leadIds.push($(this).val());
 			});
 			if (leadIds.length == 0) {
 				alert("please select any enquiry");
 			} else {
 			$('#sendNonTemplatedMailLeadIds').val(leadIds.join(","));
 			$("#nonTemplatedEmailOrSmsForm").submit();
 			}
 		});
         $('#manualmailandsms').change(function() {
      	   if($(this).is(":checked")) {
      		$('#sendingemailType').val("manualmailandsms");
      		   document.getElementById('manualsms').style.display = 'block';
      		   document.getElementById('manualemail').style.display = 'block';
      	      return;
      	   }
      	 $('#sendingemailType').val("defaultmailandsms");
      	  document.getElementById('manualsms').style.display  = 'none';
  	      document.getElementById('manualemail').style.display = 'none';
      	});

	});
	
	
    function getPagination(pageno) {
    	$('#currentPage').val(pageno);
        $("#getPaginationForm").submit();
    }
    
    function getLeadInfo(leadId) {
    	$.post(basepath + "/getLeadInfo?leadId="+leadId, function(data) {
    		$('#editLeadId').val(data.leadiId);
    		$('#editStatus').val(data.status);
    		$('#editName').val(data.name);
    		$('#editMobileNo').val(data.mobileNo);
    		$('#editLandLineNo').val(data.landLineNumber);
    		$('#editEmail').val(data.emailId);
    		$('#editAddress').val(data.address);
    		$('#editArea').val(data.area);
    		$('#editCity').val(data.city);
    		$('#editCenter').selectpicker('val', data.location);
    		$('#editAssigned').selectpicker('val', data.assignedTo);
    		$('#editSource').selectpicker('val', data.leadSource);
    		$('#editCategory').selectpicker('val', data.courseCategeory);
    		$('#editLabel').selectpicker('val', data.labels);
    		$('#editTypeOFLead').selectpicker('val', data.status);
    		
    		
    		//$('#editCourse').select2({  placeholder: {  id: data.course, text: 'ELECTONICS'}});
    		$('#editCourse').selectpicker('val', data.course);
    		
    		$('#editMode').selectpicker('val', data.modeofTraining);
    		$('#editType').selectpicker('val', data.typeofTraining);
    		$('#editComments').val(data.comments);
    		$('#editManualNameValue').val(data.courseName);
    		if (data.courseCategeory == 21) {
    			$("#editCourseDiv").hide();
    			$("#editManualName").show();
    		} else {
    			$("#editCourseDiv").show();
    			$("#editManualName").hide();
    		}
    		
    	});
    }
    
    
    
    function getTemplateInfo(categoryId, mailSubject, mailBody) {
    	$('#editCourseId').val(categoryId);
    	$('#editSubject').val(mailSubject);
    	$('#editMessageBody').val(mailBody);
    }
    function validateEditLeadForm (){
    	return true;
    }
    function validateCreateTemplateForm (){
    	return true;
    }
    
   
    
    


</script>
<body>
	<div class="container-fluid">
		<div class="row">
			<section id="header">
			<div class="row">
				<div class="col-md-8">
					<span class="logo"><a href="clearFilter"><img
							src="<c:url value="/resources/images/logo.png"/>" /></a></span> <span
						class="searchform"> <form:form method="post"
							action="searchByCourse" id="searchByCourseForm"
							class="form-inline" role="form">
							<input id="courseList" name="course" type="text"
								placeholder="Search Lead" value="${dashBoardForm.searchData}">
							<button type="submit">Search</button>
						</form:form>
					</span>
				</div>
				<div class="col-md-4">
					<span class="pull-right account" style="color:blue;"> Welcome ! ${userInfo.userName}</br> <a href="logout" style="color:red;"> Logout</a></span>
				</div>
			</section>
			<c:if test="${dashBoardForm.currentStatus == 1}">
				<c:set var="newActive" value="active" />
			</c:if>

			<c:if test="${dashBoardForm.currentStatus == 2}">
				<c:set var="openActive" value="active" />
			</c:if>
			<c:if test="${dashBoardForm.currentStatus == 3}">
				<c:set var="closeActive" value="active" />
			</c:if>
			<c:if test="${dashBoardForm.currentStatus == 4}">
				<c:set var="deleteActive" value="active" />
			</c:if>
			<c:if test="${dashBoardForm.currentStatus == 5}">
				<c:set var="hotActive" value="active" />
			</c:if>
			<c:if test="${dashBoardForm.currentStatus == 0}">
				<c:set var="allActive" value="active" />
			</c:if>
			<section id="action">
			<ul class="default-filters">
				<li><a href="clearFilter"><i
				class="fa fa-home" aria-hidden="true"></i></a></li>
				<li><a href="dashboard?leadStatus=1" class="${newActive}"><i
						class="fa fa-external-link" aria-hidden="true"></i> New <span>${dashBoardForm.newCount}</span></a></li>
				<li><a href="dashboard?leadStatus=2" class="${openActive}"><i
						class="fa fa-folder-open-o" aria-hidden="true"></i> Open
						${dashBoardForm.openCount}</a></li>
				<li><a href="dashboard?leadStatus=3" class="${closeActive}"><i
						class="fa fa-folder-o" aria-hidden="true"></i> Closed
						${dashBoardForm.closedCount}</a></li>
				<li><a href="dashboard?leadStatus=4" class="${deleteActive}"><i
						class="fa fa-folder-o" aria-hidden="true"></i> Deleted
						${dashBoardForm.deleteCount}</a></li>
				<li><a href="dashboard?leadStatus=5" class="${hotActive}"><i
						class="fa fa-eye" aria-hidden="true"></i> Hot lead
						${dashBoardForm.hotCount}</a></li>
				<li><a href="dashboard?leadStatus=0" class="${allActive}"><i
						class="fa fa-bars" aria-hidden="true"></i> All
						${dashBoardForm.totalLeadsCount}</a></li>
				<!-- <li class="right"><a href="clearFilter" role="button"><i
						class="fa fa-filter" aria-hidden="true"></i> Clear Filter</a></li> -->
				<li class="right"><a data-toggle="modal" role="button"
					data-target="#filter"><i class="fa fa-filter"
						aria-hidden="true"></i> Filter</a></li>
				<li class="right"><a data-toggle="modal" role="button"
					data-target="#addlead"><i class="fa fa-plus-square"
						aria-hidden="true"></i> Add Lead</a></li>

			</ul>
			</section>

			<c:if test="${dashBoardForm.pageTotalCount != 0}">
				<section id="content">
				<div class="action-btns">
					<ul>
					<c:if test="${dashBoardForm.currentStatus != 4}">
					<li><a rel="tooltip" data-original-title='Change Status'
							role="button" data-toggle="modal" data-target="#changestatus"><i
								class="fa fa-retweet" aria-hidden="true"></i></a></li>
						<li><a rel="tooltip"
							data-original-title='Lead Marked Successfully' role="button"
							data-toggle="modal" data-target="#success"><i
								class="fa fa-thumbs-up" aria-hidden="true"></i></a></li>
						<li><a rel="tooltip" data-original-title='Lead Failed'
							role="button" data-toggle="modal" data-target="#closelead"><i
								class="fa fa-thumbs-down" aria-hidden="true"></i></a></li>
					</c:if>
						
						<c:if test="${userName == 'admin'}">
						<c:if test="${dashBoardForm.currentStatus != 4}">
						<li><a rel="tooltip" data-original-title='Delete Lead'
							role="button" data-toggle="modal" data-target="#deletlead"><i
								class="fa fa-trash" aria-hidden="true"></i></a></li>
								</c:if>
						<li><a rel="tooltip" data-original-title='Download Excel'
							role="button" data-toggle="modal" data-target="#dwnexcel"><i
								class="fa fa-download" aria-hidden="true"></i></a></li>
						</c:if>
						
						<c:if test="${dashBoardForm.currentStatus != 4}">
						<li><a rel="tooltip" data-original-title='SMA & Email'
							role="button"><i class="fa fa-envelope" aria-hidden="true"></i></a>
							<ul class="childemail">
								<li><a data-toggle="modal" data-target="#templatedemail">Templated
										Email</a></li>
								<li><a data-toggle="modal" data-target="#freeemail">Free
										Text Email/SMS</a></li>
							<c:if test="${userName == 'admin'}">
								<li><a data-toggle="modal" data-target="#createTemple">Create Category Emailer</a></li>
								<li><a href="viewTemplatedMail">View All Category
										Emailers</a></li>
										<li><a href="viewAgents">Admin Activities</a></li>
										</c:if>
							</ul>
							</li>
							</c:if>

					</ul>
				</div>
				<h2 class="sucess-messages">${messageText}</h2>
				<div class="count">
				<c:if test="${dashBoardForm.viewPage == 'viewLeads'}">
				<c:set var="showingUrl" value="getShowingData" />
				</c:if>
				<c:if test="${dashBoardForm.viewPage == 'viewMailTemplate'}">
				<c:set var="showingUrl" value="getShowingData?isFromViewMailTemplate=true" />
				</c:if>
				<form:form method="post" action="${showingUrl}"
						id="getShowingForm" class="form-inline" role="form">
				Showing <select id="showingId" name="pageLimit">
							<c:forEach items="${dashBoardForm.limitList}" var="limit">
								<option value="${limit}"
									<c:if test="${limit == dashBoardForm.pageLimit}">selected</c:if>>${limit}</option>
							</c:forEach>
						</select>
					</form:form>
				</div>
				
				<c:if test="${dashBoardForm.viewPage == 'viewLeads'}">
				
			 <table class="table table-bordered">
						<thead>
							<tr>
								<th><input type="checkbox"  id="select_all" class="action_box"></th>
								<c:if test="${dashBoardForm.currentStatus != 4}">
								<th></th>
								</c:if>
								<th>ENQ ID</th>
								<th>Mobile</th>
								<th>Email Id</th>
								<th>Course</th>
								<th>Source Lead</th>
								<th>Branch</th>
								<th>Label</th>
								<th>Status</th>
								<th>Mode Of Training</th>
								<th>Time Created</th>
								<th>Time Updated</th>
							</tr>
						</thead>
	
						<tbody>
							<c:if test="${leadsList != null}">
								<c:forEach items="${leadsList}" var="lead">
									<tr>
										<td><input type="checkbox" value="${lead.leadiId}"
											name="leadId" class="action_box checkbox"></td>
										<c:if test="${dashBoardForm.currentStatus != 4}">
										<td><a data-toggle="modal" role="button"
											data-target="#editlead" onclick="getLeadInfo(${lead.leadiId})"><i
												class="fa fa-pencil-square-o" aria-hidden="true"></i></a></td>
											</c:if>
										<td>ENQ ${lead.leadiId}</td>
										<td>${lead.mobileNo}</td>
										<td>${lead.emailId}</td>
										<c:if test="${lead.course == 0}">
											<td>${lead.courseName}</td>
										</c:if>
										<c:if test="${lead.course != 0}">
											<td><c:out value="${coursesMap[lead.course]}"/></td>
										</c:if>
										<td><c:out value="${leadSourceMapping[lead.leadSource]}"/></td>
										<td>${lead.location}</td>
										<td>${lead.labels}</td>
										<td><c:out value="${statusMap[lead.status]}"/></td>
										<td>${lead.modeofTraining}</td>
										<td><fmt:formatDate type = "both" dateStyle = "short" timeStyle = "short" value = "${lead.createdDate}" /></td>
										<td><fmt:formatDate type = "both" dateStyle = "short" timeStyle = "short" value = "${lead.lastUpdatedDate}" /></td>
									</tr>
								</c:forEach>
							</c:if>
	
						</tbody>
					</table> 
					<%-- <table class="table table-bordered">
						<thead>
							<tr>
								<th><input type="checkbox"  id="select_all" class="action_box"></th>
								<th></th>
								<th>Status</th>
								<th>ENQ ID</th>
								<th>Name</th>
								<th>Mobile</th>
								<th>Course</th>
								<th>Category</th>
								<th>Source Lead</th>
								<th>Assigned To</th>
								<th>Time Created</th>
							</tr>
						</thead>
	
						<tbody>
							<c:if test="${leadsList != null}">
								<c:forEach items="${leadsList}" var="lead">
									<tr>
										<td><input type="checkbox" value="${lead.enqID}"
											name="leadId" class="action_box checkbox"></td>
										<td><a data-toggle="modal" role="button"
											data-target="#editlead" onclick="getLeadInfo(${lead.enqID})"><i
												class="fa fa-pencil-square-o" aria-hidden="true"></i></a></td>
										<td>${lead.status}</td>
										<td>ENQ ${lead.enqID}</td>
										<td>${lead.name}</td>
										<td>${lead.mobileNo}</td>
										<td>${lead.course}</td>
										<td>${lead.categeory}</td>
										<td>${lead.sourceLead}</td>
										<td>${lead.assignedTo}</td>
										<td>${lead.createdTime}</td>
									</tr>
								</c:forEach>
							</c:if>
	
						</tbody>
					</table> --%>
				</c:if>
				<c:if test="${dashBoardForm.viewPage == 'viewMailTemplate'}">
				 <table class="table table-bordered">
					<thead>
                            <tr>
                              <!--   <th></th>
                                <th></th> -->
                                <th>Category</th>
                                <th>Time Created</th>
                                <th>View Mailer</th>
                            </tr>
                        </thead>
                        
                        <c:forEach items="${templateList}" var="template">
									<tr>
										<%-- <td><input type="checkbox" value="${template.categoryId}"
											name="leadId"></td>
										<td><a data-toggle="modal" role="button"
											data-target="#editTemple" onclick="getTemplateInfo(${template.categoryId}, '${template.mailSubject}', '${template.mailBody}')"><i
												class="fa fa-pencil-square-o" aria-hidden="true"></i></a></td> --%>
										
										<td>${template.categoryName}</td>
										<td>${template.createdTime}</td>
										<td><button type="button" 
														onclick="viewImages(${template.categoryId})">view</button></td>
									</tr>
								</c:forEach>
                    </table>	
				</c:if>
				<span class="pull-left">Showing ${dashBoardForm.startLimit}
					to ${dashBoardForm.endLimit} of ${dashBoardForm.pageTotalCount}
					entries
				</span>
					<ul class="pagination pull-right">
					<c:if test="${dashBoardForm.pageNumber ne 1}"> 
						<li>
							<a href="javascript:void(0)" onclick="getPagination(1)">First</a>
						</li>
						<li>
							<a href="javascript:void(0)" onclick="getPagination(${dashBoardForm.pageNumber-1})">Pre</a>
						</li>
					
					</c:if>
						<li>
							<a href="#">Page ${dashBoardForm.pageNumber} of ${dashBoardForm.totalPages}</a>
						</li>
					<c:if test="${dashBoardForm.pageNumber ne dashBoardForm.totalPages}">
									<li>
							<a href="javascript:void(0)" onclick="getPagination(${dashBoardForm.pageNumber+1})">Next</a>
							
						</li>
						<li>
							<a href="javascript:void(0)" onclick="getPagination(${dashBoardForm.totalPages})">Last</a>
						</li>
					</c:if>
						
					<c:if test="${dashBoardForm.viewPage == 'viewLeads'}">
					<c:set var="paginationUrl" value="getPaginationData" />
					</c:if>
					<c:if test="${dashBoardForm.viewPage == 'viewMailTemplate'}">
					<c:set var="paginationUrl" value="getPaginationData?isFromViewMailTemplate=true" />
					</c:if>
					<form:form method="post" action="${paginationUrl}"
						id="getPaginationForm" class="form-inline" role="form">
						<input type="hidden" id="currentPage" name="currentPage" value="">
					</form:form>
					</ul>
				</section>
			</c:if>

			<c:if test="${dashBoardForm.pageTotalCount == 0 && dashBoardForm.viewPage == 'viewLeads'}">
				<h3>No leads Found.</h3>
			</c:if>

				<c:if test="${dashBoardForm.pageTotalCount == 0 && dashBoardForm.viewPage == 'viewMailTemplate'}">
				<h3>No mail templates Found.</h3>
			</c:if>
			
		</div>
	</div>

	<!--Change Status-->
	<div id="changestatus" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Change Status</h4>
				</div>
				<div class="modal-body">
					<p>Do you want to change lead status?</p>
					<form:form method="post" action="changeStatus"
						id="changeStatusForm" class="form-inline" role="form">
						<input type="hidden" id="changeStatusLeadIds" name="leadIds"
							value="">
						<input type="hidden" name="reason" value="">
						<div class="form-group">
							<!-- <label class="sr-only" for="email">Email address:</label>  -->
							<select class="selectpicker" id="statusType" name="statusType">
								<option value="1">New</option>
								<option value="2">Open</option>
								<option value="3">Closed</option>
								<option value="5">Hot Lead</option>
							</select>
						</div>
						<div class="modal-footer">
							<button type="button" id="changeStatusButton"
								class="btn btn-success">Submit</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>

	<!--mark as Success-->
	<div id="success" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Close Lead As Successful</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="changeStatus"
						id="changeStatusSuccesForm" class="form-inline" role="form">
						<input type="hidden" id="leadConvertedLeadIds" name="leadIds"
							value="">
						<input type="hidden" id="leadConvertedStatus" name="statusType"
							value="">
						<input type="hidden" name="reason" value="">
						<p>It will mark this lead as 'Successfully Closed'</p>
						<p>Do you want to close this lead?</p>
						<div class="modal-footer">
							<button type="button" id="leadConvertedToCloseButton"
								class="btn btn-success">Lead Converted Successfully</button>
							<button type="button" id="leadConvertedToOpenButton"
								class="btn btn-danger">Not Yet Closed</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>


	<!--CLose Lead-->
	<div id="closelead" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Close Lead</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="changeStatus"
						id="changeStatusFailureToCloseForm" class="form-inline"
						role="form">
						<input type="hidden" id="leadFailedToCloseingLeadIds"
							name="leadIds" value="">
						<input type="hidden" id="leadFailedToClosingStatus"
							name="statusType" value="">
						<p>Select A Reason For Failed Enquiries.</p>
						<div class="form-group">
							<select class="selectpicker" name="reason">
								<option value="Admission Taken">Admission Taken</option>
								<option value="Are Not Served">Are Not Served</option>
								<option value="Customer Not Interested">Customer Not
									Interested</option>
								<option value="Duplicate Enquiry">Duplicate Enquiry</option>
								<option value="Pricing Issue">Pricing Issue</option>
							</select>
						</div>

						<div class="modal-footer">
							<button type="button" class="btn btn-success"
								id="leadFailedToConvertedToCloseButton">Failed To
								Convert</button>
							<button type="submit" class="btn btn-danger" data-dismiss="modal">Cancel</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>

	<!--mark as Success-->
	<div id="deletlead" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Delete Lead</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="changeStatus"
						id="changeStatusDeleteForm" class="form-inline" role="form">
						<input type="hidden" id="deleteLeadIds" name="leadIds" value="">
						<input type="hidden" id="deleteStatus" name="statusType" value="">
						<input type="hidden" name="reason" value="">
						<p>Are you sure you want to delete leads?</p>
						<p>Note : After deletion, you can ONLY view deleted leads but
							can't restore it or do ANY operation.</p>
							<p>Are you sure you want to d leads? Please Provide Password ..</p>
						</br>
						<input type="password" style="padding-left: 20px;" id = "deletePassword" placeholder="Profile Password"/>
						<input type = "hidden" id = "deleteDbPassword" value= "${userInfo.password}"/>
						</br>
						<div class="modal-footer">
							<button type="button" class="btn btn-success"
								id="leadDeleteButton">Delete</button>
							<button type="submit" class="btn btn-danger" data-dismiss="modal">Cancel</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>


	<!--mark as Success-->
	<div id="dwnexcel" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<form:form method="post" action="downloadLeadsToSheet"
				id="downloadLeadsToSheetForm" role="form">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Download Lead</h4>
					</div>
					<input type="hidden" id="downloadLeadIds" name="leadIds" value="">
					<div class="modal-body">
						<p>Are you sure you want to Download leads? Please Provide Password ..</p>
						</br>
						<input type="password" style="padding-left: 20px;" id = "downloadPassword" placeholder="Profile Password"/>
						<input type = "hidden" id = "dbPassword" value= "${userInfo.password}"/>
						</br>
						<div class="modal-footer">
							<button type="button" id="download" class="btn btn-success"
								data-dismiss="modal">Download</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<!--Filter-->
	<div id="filter" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<form:form method="post" action="filterByDateAndCourse"
					id="filterByDateAndCourseForm" role="form">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Filter By</h4>
					</div>
					<div class="modal-body">
					<div class="col-sm-6">
					<div class="form-group">
							<label for="email">From date</label>
							<div class="input-group date" data-provide="datepicker">
								<input type="text" class="form-control" id="fromDate" 
									name="fromDate" placeholder="Select from date">
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>
						</div>
					<div class="form-group">
							<label for="pwd">Category</label><br> <select
								class="addlead-course form-control" id="courseCategeory"
								name="courseCategeory"
								onchange="getCourseList('courseName','courseCategeory','filterCourseDiv','filterCourseManualDiv');">
								<option value="0">Select Category</option>
								<c:forEach var="category" items="${courseCategories}">
									<option value="${category.key}">${category.value}</option>
								</c:forEach>
							</select>
						</div>
						 <div class="form-group">
								<label for="pwd">Mode of Training</label><br> <select
									class="selectpicker" title="Select Training" id="filterMode"
									name="modeofTraining">
									<option value="Classroom">Classroom</option>
									<option value="Online">Online</option>
									<option value="Corporate">Corporate</option>
									<option value="Events">Events</option>
									<option value="Workshop">Workshop</option>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Weekday/Weekend</label><br> <select
									class="selectpicker" title="Select Training" id="filterTraining"
									name="filterByTraining">
									<option value="Weekend">Weekend</option>
									<option value="Weekday">Weekday</option>
								</select>
							</div>
							<!--  <div class="form-group">
								<label for="pwd">Weekday/Weekend</label><br> <select
									class="selectpicker" title="Select Type" id="filterType"
									name="typeOfTrainingByFilter">
									<option value="Weekend">Weekend</option>
									<option value="Weekday">Weekday</option>
								</select>
							</div> -->
							 
							 <div class="form-group">
								<label for="pwd">Labels</label><br> <select
									class="selectpicker" title="Select Label" id="filterLabelType"
									name="labelByFilter">
									<option value="Urgent">Urgent</option>
									<option value="FastTrack">FastTrack</option>
									<option value="Group">Group</option>
									<option value="Attended-Demo">Attended-Demo</option>
								</select>
							</div> 
							<div class="form-group">
								<label for="pwd">Type of lead</label><br> <select
									class="selectpicker" title="Select lead Type" id="filterLeadType"
									 name="filterByStatus">
								<option value="0" >Select Type</option>
								<option value="1">New</option>
								<option value="2">Open</option>
								<option value="3">Closed</option>
								<option value="5">Hot Lead</option>
								</select>
							</div>
						</div>
						<div class="col-sm-6">
						<div class="form-group">
							<label for="pwd">To Date</label>
							<div class="input-group date" data-provide="datepicker">
								<input type="text" class="form-control" id="toDate"
									name="toDate" placeholder="Select to date">
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>
						</div>
						<div class="form-group" id="filterCourseDiv">
							<label for="pwd">Course</label><br> <select
								class="addlead-course form-control" id="courseName"
								name="course">
								<option value="0">Select Course</option>
							</select>
						</div>
						<div class="form-group" id="filterCourseManualDiv">
								<label for="pwd">Course</label> <input type="text"
									class="form-control" value="" name="courseName" id="courseNameManual">
							</div>
						 <div class="form-group">
								<label for="pwd">Center Location</label><br> <select
									class="selectpicker" title="Select Location" id="filterCenter"
									name="locationCenter">
									<option value="Kharadi-Pune">Kharadi-Pune</option>
									<option value="Aundh-Pune">Aundh-Pune</option>
									<!-- <option value="Kharadi-Pune">Kharadi-Pune</option> -->
								</select>
							</div>
							
							<div class="form-group">
								<label for="pwd">Assigned to</label><br> <select
									class="selectpicker" title="Assigned to" id="filterAssigned"
									name="assignedToByFilter">
									<c:forEach items="${userssList}" var="user">
										<c:if test="${user.isActive == 1}">
												<option value="${user.userId}">${user.userName}</option>
										</c:if>
									</c:forEach>
									<c:forEach items="${agentsList}" var="agent">
										<c:if test="${agent.isActive == 1}">
												<option value="${agent.userId}">${agent.userName}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Source</label><br> <select
									class="selectpicker" title="Select source" id="filterLeadSource"
									name="leadSource">
									<c:forEach var="leadSource" items="${leadSourceMapping}">
										<option value="${leadSource.key}">${leadSource.value}</option>
									</c:forEach>
								</select>
							</div> 
							 
							
						</div>
						<input type="hidden" name="filterType" id="filterType" value="">
						<div class="modal-footer">
						<c:if test="${userName == 'admin'}">
							<button type="button" class="btn btn-success"
								id="downloadLeadsFilter" data-dismiss="modal">Download
								Report</button>
								</c:if>
							<button type="button" class="btn btn-success"
								id="showLeadsFilter">Apply Filter</button>
							<button type="submit" class="btn btn-danger" data-dismiss="modal">Cancel</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>

	</div>


 <div id="addlead" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Add Lead</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="addlead" name="addLeadForm"
						onsubmit="return validateAddLeadform()" enctype="multipart/form-data">
						<div class="col-sm-6">
							<div class="form-group">
								<label for="email">Student Name</label> <input type="text"
									class="form-control" id="name" value="" name="name">
							</div>
							<div class="form-group">
								<label for="pwd">Phone Number</label> <input type="text"
									class="form-control" id="mobileNo"  maxlength="14" value="" name="mobileNo" onkeyup="numbervalidation();">
							</div>
							<div class="form-group">
								<label for="pwd">LandLine Number</label> <input type="text"
									class="form-control" id="landLineNo" value="" name="landLineNumber">
							</div>
							<div class="form-group">
								<label for="pwd">Email ID</label> <input type="email"
									class="form-control" id="emailId" value="" name="emailId" onchange="checkEmail();">
							</div>
							<div class="form-group">
								<label for="pwd">Address</label> <input type="text"
									class="form-control" id="address" value="" name="address">
							</div>
							<div class="form-group">
								<label for="pwd">Area</label> <input type="text"
									class="form-control" id="area" value="" name="area">
							</div>
							<div class="form-group">
								<label for="pwd">City</label> <input type="text"
									class="form-control" id="city" value="" name="city">
							</div>
							<div class="form-group">
								<label for="pwd">Center Location</label><br> <select
									class="selectpicker" title="Select Location" id="center"
									name="location">
									<option value="Kharadi-Pune">Kharadi-Pune</option>
									<option value="Aundh-Pune">Aundh-Pune</option>
									<!-- <option value="Kharadi-Pune">Kharadi-Pune</option> -->
								</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label for="pwd">Assigned to</label><br> <select
									class="selectpicker" title="Assigned to" id="assigned"
									name="assignedTo">
									<c:forEach items="${userssList}" var="user">
										<c:if test="${user.isActive == 1}">
												<option value="${user.userId}">${user.userName}</option>
										</c:if>
									</c:forEach>
									<c:forEach items="${agentsList}" var="agent">
										<c:if test="${agent.isActive == 1}">
												<option value="${agent.userId}">${agent.userName}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Source</label><br> <select
									class="selectpicker" title="Select source" id="leadSource"
									name="leadSource">
									<c:forEach var="leadSource" items="${leadSourceMapping}">
										<option value="${leadSource.key}">${leadSource.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Category</label><br> <select
									class="selectpicker" title="Select Category" id="courseCategeoryName"
									name="courseCategeory"
									onchange="getCourseListForAddAndEditLead('addCourseName','courseCategeoryName', 'addCourseDiv','addManualName');">
									<option value="0">Select Category</option>
									<c:forEach var="category" items="${courseCategories}">
										<option value="${category.key}">${category.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group" id="addCourseDiv">
								<label for="pwd">Course</label><br> <select	
									class="selectpicker" title="Select Course" id="addCourseName"
									name="courseList">
									<c:forEach var="courses" items="${coursesMap}">
										<option value="${courses.key}">${courses.value}</option>
									</c:forEach>
								</select>
							</div>
							
							<div class="form-group" id="addManualName">
								<label for="pwd">Course</label> <input type="text"
									class="form-control" value="" name="courseName" id="addManualNameValue">
							</div>
							
							<div class="form-group">
								<label for="pwd">Mode of Training</label><br> <select
									class="selectpicker" title="Select Training" id="mode"
									name="modeofTraining">
									<option value="Classroom">Classroom</option>
									<option value="Online">Online</option>
									<option value="Corporate">Corporate</option>
									<option value="Events">Events</option>
									<option value="Workshop">Workshop</option>
								</select>
							</div>


							<div class="form-group">
								<label for="pwd">Weekday/Weekend</label><br> <select
									class="selectpicker" title="Select Type" id="type"
									name="typeofTraining">
									<option value="Weekend">Weekend</option>
									<option value="Weekday">Weekday</option>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Labels</label><br> <select
									class="selectpicker" title="Select Label" id="labelType"
									name="labels">
									<option value="Urgent">Urgent</option>
									<option value="FastTrack">FastTrack</option>
									<option value="Group">Group</option>
									<option value="Attended-Demo">Attended-Demo</option>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Type Of Lead</label><br> <select
									class="selectpicker" title="Select lead type" id="statusTypeAdd" name="status"
									>
								<option value="1">New</option>
								<option value="2">Open</option>
								<option value="5">Hot Lead</option>
							</select>
							</div>
						</div>
						<div class="col-sm-12" >
							<div class="form-group">
								<input type="checkbox"  id="manualmailandsms" > Please tick here to send manual mail and sms
							</div>
							<input type="hidden" id="sendingemailType" name="sendingeMailAndSmsType" value="defaultmailandsms">
							<div class="form-group" id="manualsms" style="display:none;">
									<label for="pwd">SMS</label><br>
									<textarea  maxlength="160" class="form-control" name = "nonTemplatedSms" id="smsValueId" placeholder="Please enter SMS"
										rows="5"></textarea>
							</div>
							<div class="form-group" id="manualemail" style="display:none;">
									<label for="pwd">Email</label><br> <input
										class="form-control" placeholder="Email Subject" name = "nonTemplatedEmailSubject"/>
									<label for="pwd">Email Content</label><br>
									<textarea class="form-control" id = "addLeadEmailContentTextArea"
										placeholder="Please enter Email Content" rows="10" name = "nonTemplatedEmailBody"></textarea>
										<br>
										<span class="btn btn-rounded btn-green btn-file"> 
										<span>Choose
										file</span> <input type="file" name="nonTemplateFile" id="manualEmailFile"
									onchange="javascript:updateManulaList()">
										</span>
										<div id="manualEmailFileList"></div>
							</div>
						</div>
						<div class="col-sm-12">
							<div class="form-group">
								<label for="pwd">Commentes</label><br>
								<textarea class="form-control" id="comments" name="comments"></textarea>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-success">Add
									Lead</button>
								<button type="button" class="btn btn-danger"
									data-dismiss="modal">Cancel</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	
	<!--Filter-->
	<div id="editlead" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Edit Lead</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="editlead" name="editLeadForm"
						onsubmit="return validateEditLeadForm()" role="form">
						<input type="hidden" class="form-control" id="editLeadId" value=""
							name="leadiId">
						<!-- <input type="hidden" class="form-control" id="editStatus" value=""
							name="status"> -->
						<div class="col-sm-6">
							<div class="form-group">
								<label for="email">Student Name</label> <input type="text"
									class="form-control" id="editName" value="" name="name">
							</div>
							<div class="form-group">
								<label for="pwd">Phone Number</label> <input type="text"
									class="form-control" id="editMobileNo" value="" name="mobileNo" onkeyup="numbervalidation();">
							</div>
							<div class="form-group">
								<label for="pwd">LandLine Number</label> <input type="text"
									class="form-control" id="editLandLineNo" value="" name="landLineNumber">
							</div>
							<div class="form-group">
								<label for="pwd">Email ID</label> <input type="email"
									class="form-control" id="editEmail" value="" name="emailId" onchange="checkEmail();">
							</div>
							<div class="form-group">
								<label for="pwd">Address</label> <input type="text"
									class="form-control" id="editAddress" value="" name="address">
							</div>
							<div class="form-group">
								<label for="pwd">Area</label> <input type="text"
									class="form-control" id="editArea" value="" name="area">
							</div>
							<div class="form-group">
								<label for="pwd">City</label> <input type="text"
									class="form-control" id="editCity" value="" name="city">
							</div>
							<div class="form-group">
								<label for="pwd">Labels</label><br> <select
									class="selectpicker" title="Select Label" id="editLabel"
									name="labels">
									<option value="Urgent">Urgent</option>
									<option value="FastTrack">FastTrack</option>
									<option value="Group">Group</option>
									<option value="Attended-Demo">Attended-Demo</option>
								</select>
							</div>
						
						</div>
						<div class="col-sm-6">
							 <div class="form-group">
								<label for="pwd">Assigned to</label><br> <select
									class="selectpicker" title="Assigned to" id="editAssigned"
									name="assignedTo">
									<c:forEach items="${userssList}" var="user">
										<c:if test="${user.isActive == 1}">
												<option value="${user.userId}">${user.userName}</option>
										</c:if>
									</c:forEach>
									<c:forEach items="${agentsList}" var="agent">
										<c:if test="${agent.isActive == 1}">
												<option value="${agent.userId}">${agent.userName}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							
							<div class="form-group">
								<label for="pwd">Source</label><br> <select
									class="selectpicker" title="Select source" id="editSource"
									name="leadSource">
									<c:forEach var="leadSource" items="${leadSourceMapping}">
										<option value="${leadSource.key}">${leadSource.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Category</label><br> <select
									class="selectpicker" title="Select Category" id="editCategory"
									name="courseCategeory"
									onchange="getCourseListForAddAndEditLead('editCourse','editCategory', 'editCourseDiv','editManualName');">
									<c:forEach var="category" items="${courseCategories}">
										<option value="${category.key}">${category.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group" id="editCourseDiv">
								<label for="pwd">Course</label><br> <select	
									class="selectpicker" title="Select Course" id="editCourse"
									name="course" id="editCourseName">
									<c:forEach var="courses" items="${coursesMap}">
										<option value="${courses.key}">${courses.value}</option>
									</c:forEach>
								</select>
							</div>
							
							<div class="form-group" id="editManualName">
								<label for="pwd">Course</label> <input type="text"
									class="form-control" value="" name="courseName" id="editManualNameValue">
							</div>
							<div class="form-group">
								<label for="pwd">Mode of Training</label><br> <select
									class="selectpicker" title="Select Training" id="editMode"
									name="modeofTraining">
									<option value="Classroom">Classroom</option>
									<option value="Online">Online</option>
									<option value="Corporate">Corporate</option>
									<option value="Events">Events</option>
									<option value="Workshop">Workshop</option>
								</select>
							</div>


							<div class="form-group">
								<label for="pwd">Weekday/Weekend</label><br> <select
									class="selectpicker" title="Select Type" id="editType"
									name="typeofTraining">
									<option value="Weekend">Weekend</option>
									<option value="Weekday">Weekday</option>
								</select>
							</div>
								<div class="form-group">
								<label for="pwd">Center Location</label><br> <select
									class="selectpicker" title="Select Location" id="editCenter"
									name="location">
									<!-- <option value="Bangalore-Hsr">Bangalore-Hsr</option> -->
									<option value="Aundh-Pune">Aundh-Pune</option>
									<option value="Kharadi-Pune">Kharadi-Pune</option>
									</select>
							</div>
							<div class="form-group">
								<label for="pwd">Type of lead</label><br> <select
									class="selectpicker" title="Select lead Type" id="editTypeOFLead" name="status"
									>
								<option value="1">New</option>
								<option value="2">Open</option>
								<option value="3">Closed</option>
								<option value="5">Hot Lead</option>
								</select>
							</div>
						</div>
						<div class="col-sm-12">
							<div class="form-group">
								<label for="pwd">Commentes</label><br>
								<textarea class="form-control" id="editComments" name="comments"></textarea>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-success">Update
									Lead</button>
								<button type="button" class="btn btn-danger"
									data-dismiss="modal">Cancel</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<div id="freeemail" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Send Non-Templated Email/SMS</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="nonTemplatedEmailOrSms" id="nonTemplatedEmailOrSmsForm"
						 enctype="multipart/form-data">
						<div class="col-sm-12">
						<div class="form-group">
								<label for="pwd">Select Email/SMS:</label><br>
								<div class="radio">
									<label><input type="radio" id="emailRadio" name="optradio" value="1" checked="true">Email</label>
								</div>
								<div class="radio">
									<label><input type="radio" id="smsRadio" name="optradio" value="0" >SMS</label>
								</div>
							</div>
							<div class="form-group" id="smsDiv" style="display:none;">
								<label for="pwd">SMS</label><br>
								<textarea  maxlength="160" class="form-control" name = "nonTemplatedSms" placeholder="Please enter SMS"
									rows="5"></textarea>
							</div>
							<input type="hidden" id="sendNonTemplatedMailLeadIds" name="leadIds" value="">
							<div class="form-group" id="mailDiv">
								<label for="pwd">Email</label><br> <input
									class="form-control" placeholder="Email Subject" name = "nonTemplatedEmailSubject"/>
								<label for="pwd">Email Content</label><br>
								<textarea class="form-control" id = "nonTemplateEmailContentTextArea"
									placeholder="Please enter Email Content" rows="10" name = "nonTemplatedEmailBody"></textarea>
									<br>
								<span class="btn btn-rounded btn-green btn-file"> <span>Choose
										file</span> <input type="file" name="nonTemplateFile" id="sendNonTemplateFile"
									onchange="javascript:updateSendNonTemplateFileList()">
										</span>
										<div id="sendNonTemplateFileList"></div>
							</div>
							
						</div>
						<div class="col-sm-12">
							<div class="modal-footer">
								<button type="button" class="btn btn-success" id="sendNonTemplatedMailOrSms">Send
									Template</button>
								<button type="button" class="btn btn-danger"
									data-dismiss="modal">Cancel</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>

	<!--templatedemail-->
	<div id="templatedemail" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Send Templated Email</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="sendTemplatedMail"
			      	id="sendTemplatedMailForm" role="form">
						<div class="col-sm-12">
							 <div class="form-group">
							
								<label for="pwd">Category</label><br> <select
									class="selectpicker" title="Select Category"
									id="sendTemplatedMailCategeory" name="categeoryId">
									<c:forEach var="category" items="${courseCategories}">
										<option value="${category.key}">${category.value}</option>
									</c:forEach>
								</select>
							</div>
							<%-- <div class="form-group">
								<label for="pwd">Course</label><br> <select
									class="selectpicker" title="Select Course"
									id="sendTemplatedMailCourse" name="courseId">
									<c:forEach var="courses" items="${courseTemplates}">
										<option value="${courses.courseId}">${courses.courseName}</option>
									</c:forEach>
								</select>
							</div> --%>
							<input type="hidden" id="sendTemplatedMailLeadIds" name="leadIds" value="">
							<!-- <div class="form-group">
								<label for="pwd">Select Email:</label><br>
								<div class="radio">
									<label><input type="radio" name="optradio" value="">Email</label>
								</div>
								<div class="radio">
									<label><input type="radio" name="optradio" value="">SMS</label>
								</div>
							</div> -->
						</div>
						<div class="col-sm-12">
							<div class="modal-footer">
								<button type="button" class="btn btn-success" id="sendTemplatedMail">Send
									Template</button>
								<button type="button" class="btn btn-danger"
									data-dismiss="modal">Cancel</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<div id="createTemple" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Create Category Emailer</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="createMailTemplate" 
						enctype="multipart/form-data">
						<div class="col-sm-12">
							<!-- <div class="form-group">
								<label for="email">Subject</label> <input type="text"
									class="form-control" id="subject" name="subject"
									placeholder="Please enter Subject">
							</div>
							<div class="form-group">
								<label for="pwd">Message</label>
								<textarea placeholder="Please enter message here"
									class="form-control"  id= "messageBody" name="messagebody" rows="10"></textarea>
							</div> -->
							<div class="form-group">
								<label for="pwd">Category</label><br> <select
									class="selectpicker" title="Select Category"
									id="createTempateCategory" name="categoryId"
									onchange="getCourseList('createTemplateCourse','createTempateCategory');">
									<c:forEach var="category" items="${courseCategories}">
										<option value="${category.key}">${category.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-control-static adproduct">
							<div class="drop-zone">
								<i class="font-icon font-icon-cloud-upload-2"></i>
								<div class="drop-zone-caption">Choose file to upload 
									Emailer</div>
								<span class="btn btn-rounded btn-green btn-file"> <span>Choose
										file</span> <input type="file" name="file" id="file" multiple=""
									onchange="javascript:updateList()">

								</span>
							</div>
							<br />Selected files:
							<div id="fileList"></div>
						</div>
							<%-- <div class="form-group">
								<label for="pwd">Course</label><br> <select
									class="selectpicker" title="Select Course"
									id="createTemplateCourse" name="courseId">
									<c:forEach var="courses" items="${coursesMap}">
										<option value="${courses.key}">${courses.value}</option>
									</c:forEach>
								</select>
							</div> --%>
							<button type="submit" class="btn btn-success" onclick="return checkFileExsistsOrNot()">Create Emailer</button>
								<button type="button" class="btn btn-danger"
									data-dismiss="modal">Cancel</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>

	</div>
<div id="editTemple" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Edit Templated Email/SMS</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="editMailTemplate" name="editMailTemplateForm"
						onsubmit="return validateCreateTemplateForm()" role="form">
						<input type="hidden" id="editCourseId" name="categoryId" value="">
						<div class="col-sm-12">
							<div class="form-group">
								<label for="email">Subject</label> <input type="text"
									class="form-control" id="editSubject" name="subject"
									placeholder="Please enter Subject">
							</div>
							<div class="form-group">
								<label for="pwd">Message</label>
								<textarea placeholder="Please enter message here"
									class="form-control"  id= "editMessageBody" name="messagebody" rows="10"></textarea>
							</div>
							<button type="submit" class="btn btn-success">Edit
								Template</button>
								<button type="button" class="btn btn-danger"
									data-dismiss="modal">Cancel</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>

	</div>

</body>
</html>