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

function validateAddLeadform() {
	var mobileNo = $('#mobileNo').val();
	var courseId = $('#addCourseName').val();
	 var courseCategeoryName = $('#courseCategeoryName').val();
	 	 var leadSource = $('#leadSource').val(); 
	  	
	 	 var mobileNumberChecking = /^[7-9][0-9]*$/;
	 	 if(!isBlank(mobileNo) && !isInteger(mobileNo)) {
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
	     	}  else if(courseCategeoryName==0 || courseCategeoryName==null){
	     		alert('Please select Category.');
	     		return false;
	     	}  else if (courseId == null){
	     		alert('Please select Course.');
	     		return false;
	     	} else if(leadSource == null || leadSource ==0){
	     		alert('Please select Lead Source.');
	     		return false;
	     	}
	  	return true;
	  }
	 function isInteger(s) {
	 	return (s.toString().search(/^-?[0-9]+$/) == 0);
	 }
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
	                $('.datepicker').datepicker();
	                $(".addlead-course").select2({
	                		                    placeholder: "Select a Course",
	                		                    allowClear: true
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
			if (leadIds.length == 0) {
				alert("please select any enquiry");
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
			if((null==fromDate || fromDate=="") && (null == toDate || toDate=="")&& (null==courseCategeory || courseCategeory == 0)){
				alert("Please select date Or course Categeory");
              	return false;
			} else if((null==fromDate || fromDate=="") || (null == toDate || toDate=="")){
				alert("Please select from date and to date");
				return false;
			} else if(null == courseCategeory || courseCategeory == 0){
				alert("Please select courseCategeory");
              	return false;
			} else {
			$('#filterType').val("1");
			$("#filterByDateAndCourseForm").submit();
			}
		});
		$("#showLeadsFilter").click(function() {
			var courseCategeory =  $('#courseCategeory').val();
						var toDate = $('#toDate').val();
						var fromDate = $('#fromDate').val();
						if((null==fromDate || fromDate=="") && (null == toDate || toDate=="")&& (null==courseCategeory || courseCategeory == 0)){
							alert("Please select date Or course Categeory");
			              	return false;
						} else if((null==fromDate || fromDate=="") || (null == toDate || toDate=="")){
							alert("Please select from date and to date");
							return false;
						} else if(null == courseCategeory || courseCategeory == 0){
							alert("Please select courseCategeory");
			              	return false;
						} else {
			$('#filterType').val("0");
			$("#filterByDateAndCourseForm").submit();
			}
		});
		
		$("#download").click(function() {
			var leadIds = [];
			$.each($("input[name='leadId']:checked"), function() {
				leadIds.push($(this).val());
			});
			if (leadIds.length == 0) {
				alert("please select any enquiry");
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
    		$('#editEmail').val(data.emailId);
    		$('#editAddress').val(data.address);
    		$('#editArea').val(data.area);
    		$('#editCity').val(data.city);
    		$('#editCenter').selectpicker('val', data.location);
    		$('#editAssigned').selectpicker('val', data.assignedTo);
    		$('#editSource').selectpicker('val', data.leadSource);
    		$('#editCategory').selectpicker('val', data.courseCategeory);
    		
    		//$('#editCourse').select2({  placeholder: {  id: data.course, text: 'ELECTONICS'}});
    		$('#editCourse').selectpicker('val', data.course);
    		
    		$('#editMode').selectpicker('val', data.modeofTraining);
    		$('#editType').selectpicker('val', data.typeofTraining);
    		$('#editComments').val(data.comments);
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
					<span class="pull-right account"><a href="logout">Logout</a></span>
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
			<c:if test="${dashBoardForm.currentStatus == 0}">
				<c:set var="allActive" value="active" />
			</c:if>
			<section id="action">
			<ul class="default-filters">
				<li><a href="dashboard?leadStatus=1" class="${newActive}"><i
						class="fa fa-external-link" aria-hidden="true"></i> New <span>${dashBoardForm.newCount}</span></a></li>
				<li><a href="dashboard?leadStatus=2" class="${openActive}"><i
						class="fa fa-folder-open-o" aria-hidden="true"></i> Open
						${dashBoardForm.openCount}</a></li>
				<li><a href="dashboard?leadStatus=3" class="${closeActive}"><i
						class="fa fa-folder-o" aria-hidden="true"></i> Closed
						${dashBoardForm.closedCount}</a></li>
				<li><a href="dashboard?leadStatus=0" class="${allActive}"><i
						class="fa fa-bars" aria-hidden="true"></i> All
						${dashBoardForm.totalLeadsCount}</a></li>
				<li class="right"><a href="clearFilter" role="button"><i
						class="fa fa-filter" aria-hidden="true"></i> Clear Filter</a></li>
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
						<li><a rel="tooltip" data-original-title='Delete Lead'
							role="button" data-toggle="modal" data-target="#deletlead"><i
								class="fa fa-trash" aria-hidden="true"></i></a></li>
						<li><a rel="tooltip" data-original-title='Download Excel'
							role="button" data-toggle="modal" data-target="#dwnexcel"><i
								class="fa fa-download" aria-hidden="true"></i></a></li>
						<li><a rel="tooltip" data-original-title='SMA & Email'
							role="button"><i class="fa fa-envelope" aria-hidden="true"></i></a>
							<ul class="childemail">
								<li><a data-toggle="modal" data-target="#templatedemail">Templated
										Email</a></li>
								<li><a data-toggle="modal" data-target="#freeemail">Free
										Text Email/SMS</a></li>
								<li><a data-toggle="modal" data-target="#createTemple">Create
										Templated Email</a></li>
								<li><a href="viewTemplatedMail">View All Templated
										Email</a></li>
							</ul></li>

					</ul>
				</div>

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
					</table>
				</c:if>
				<c:if test="${dashBoardForm.viewPage == 'viewMailTemplate'}">
				 <table class="table table-bordered">
					<thead>
                            <tr>
                                <th></th>
                                <th></th>
                                <th>Category</th>
                                <th>Subject</th>
                                <th>Time Created</th>
                            </tr>
                        </thead>
                        
                        <c:forEach items="${templateList}" var="template">
									<tr>
										<td><input type="checkbox" value="${template.categoryId}"
											name="leadId"></td>
										<td><a data-toggle="modal" role="button"
											data-target="#editTemple" onclick="getTemplateInfo(${template.categoryId}, '${template.mailSubject}', '${template.mailBody}')"><i
												class="fa fa-pencil-square-o" aria-hidden="true"></i></a></td>
										
										<td>${template.categoryName}</td>
										<td>${template.mailSubject}</td>
										<td>${template.createdTime}</td>
										
									</tr>
								</c:forEach>
                    </table>
				</c:if>
				<span class="pull-left">Showing ${dashBoardForm.startLimit}
					to ${dashBoardForm.endLimit} of ${dashBoardForm.pageTotalCount}
					entries</span>
					<ul class="pagination pull-right">
					<c:forEach items="${dashBoardForm.pageList}" var="page"
						varStatus="pageIndex">
						<li
							class="<c:if test="${dashBoardForm.pageNumber == page}">active</c:if>">
							<a href="javascript:void(0)" onclick="getPagination(${page})">${page}</a>
						</li>
					</c:forEach>
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

			<c:if test="${dashBoardForm.pageTotalCount == 0}">
				<h3>No leads Found.</h3>
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
						<p>Are you sure you want to Download leads?</p>
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
							<label for="pwd">To Date</label>
							<div class="input-group date" data-provide="datepicker">
								<input type="text" class="form-control" id="toDate"
									name="toDate" placeholder="Select to date">
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="pwd">Category</label><br> <select
								class="addlead-course form-control" id="courseCategeory"
								name="courseCategeory"
								onchange="getCourseList('courseName','courseCategeory');">
								<option value="0">Select Category</option>
								<c:forEach var="category" items="${courseCategories}">
									<option value="${category.key}">${category.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label for="pwd">Course</label><br> <select
								class="addlead-course form-control" id="courseName"
								name="course">
								<option value="0">Select Course</option>
							</select>
						</div>
						<input type="hidden" name="filterType" id="filterType" value="">
						<div class="modal-footer">
							<button type="button" class="btn btn-success"
								id="downloadLeadsFilter" data-dismiss="modal">Download
								Report</button>
							<button type="button" class="btn btn-success"
								id="showLeadsFilter">Apply Filter</button>
							<button type="submit" class="btn btn-danger" data-dismiss="modal">Cancel</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>

	</div>


	<!--Filter-->
	<div id="addlead" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Add Lead</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="addlead" name="addLeadForm"
						onsubmit="return validateAddLeadform()">
						<div class="form-group">
							<label for="email">Student Name</label> <input type="text"
								class="form-control" id="name" name="name">
						</div>
						<div class="form-group">
							<label for="pwd">Phone Number</label> <input type="text"
								class="form-control" id="mobileNo" name="mobileNo">
						</div>
						<div class="form-group">
							<label for="pwd">Email ID</label> <input type="email"
								class="form-control" id="emailId" name="emailId">
						</div>
						<div class="form-group wd50">
							<label for="pwd">Category</label><br> <select
								class="addlead-course form-control" id="courseCategeoryName"
								name="courseCategeory"
								onchange="getCourseList('addCourseName','courseCategeoryName');">
								<option value="0">Select Category</option>
								<c:forEach var="category" items="${courseCategories}">
									<option value="${category.key}">${category.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group wd50">
							<label for="pwd">Course</label><br> <select
								class="addlead-course form-control" multiple
								title="Select Course" id="addCourseName" name="courseList">
								<!-- <option value="0">Select Course</option> -->
							</select>
						</div>
						<%-- <div class="form-group wd50">
							<label for="pwd">Course</label><br> <select
								class="selectpicker" multiple title="Select Course" id="course"
								name="course">
								<c:forEach var="courses" items="${coursesMap}">
									<option value="${courses.key}">${courses.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group wd50">
							<label for="pwd">Course</label><br> <select
								class="selectpicker" multiple title="Select Course" id="course"
								name="course">
								<c:forEach var="courses" items="${coursesMap}">
									<option value="${courses.key}">${courses.value}</option>
								</c:forEach>
							</select>
						</div> --%>
						<div class="form-group">
							<label for="pwd">Source Lead</label> <select class="selectpicker"
								title="Select Source" id="leadSource" name="leadSource">
								<c:forEach var="leadSource" items="${leadSourceMapping}">
									<option value="${leadSource.key}">${leadSource.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group">
							<label for="pwd">Commentes</label><br>
							<textarea class="form-control" id="comments" name="comments"></textarea>
						</div>

						<div class="modal-footer">
							<button type="submit" class="btn btn-success">Add Lead</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
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
						<input type="hidden" class="form-control" id="editStatus" value=""
							name="status">
						<div class="col-sm-6">
							<div class="form-group">
								<label for="email">Student Name</label> <input type="text"
									class="form-control" id="editName" value="" name="name">
							</div>
							<div class="form-group">
								<label for="pwd">Phone Number</label> <input type="text"
									class="form-control" id="editMobileNo" value="" name="mobileNo">
							</div>
							<div class="form-group">
								<label for="pwd">Email ID</label> <input type="email"
									class="form-control" id="editEmail" value="" name="emailId">
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
								<label for="pwd">Center Location</label><br> <select
									class="selectpicker" title="Select Location" id="editCenter"
									name="location">
									<option value="Bangalore-Hsr">Bangalore-Hsr</option>
									<option value="Aundh-Pune">Aundh-Pune</option>
									<option value="Kharadi-Pune">Kharadi-Pune</option>
								</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label for="pwd">Assigned to</label><br> <select
									class="selectpicker" title="Assigned to" id="editAssigned"
									name="assignedTo">
									<option value="2">Agent 1</option>
									<option value="3">Agent 2</option>
									<option value="4">Agent 3</option>
									<option value="5">Agent 4</option>
									<option value="6">Agent 5</option>
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
									onchange="getCourseList('editCourse','editCategory');">
									<c:forEach var="category" items="${courseCategories}">
										<option value="${category.key}">${category.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Course</label><br> <select
									class="selectpicker" title="Select Course" id="editCourse"
									name="course">
									<c:forEach var="courses" items="${coursesMap}">
										<option value="${courses.key}">${courses.value}</option>
									</c:forEach>
								</select>
							</div>
							<div class="form-group">
								<label for="pwd">Mode of Training</label><br> <select
									class="selectpicker" title="Select Training" id="editMode"
									name="modeofTraining">
									<option value="Classroom">Classroom</option>
									<option value="Online">Online</option>
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
						 role="form">
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
								<textarea class="form-control"
									placeholder="Please enter Email Content" rows="10" name = "nonTemplatedEmailBody"></textarea>
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
							<%-- <div class="form-group">
							
								<label for="pwd">Category</label><br> <select
									class="selectpicker" title="Select Category"
									id="sendTemplatedMailCategeory" name="categeoryId"
									onchange="getCourseList('"sendTemplatedMailCourse"','"sendTemplatedMailCategeory"');">
									<c:forEach var="category" items="${courseCategories}">
										<option value="${category.key}">${category.value}</option>
									</c:forEach>
								</select>
							</div> --%>
							<div class="form-group">
								<label for="pwd">Course</label><br> <select
									class="selectpicker" title="Select Course"
									id="sendTemplatedMailCourse" name="courseId">
									<c:forEach var="courses" items="${courseTemplates}">
										<option value="${courses.courseId}">${courses.courseName}</option>
									</c:forEach>
								</select>
							</div>
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
					<h4 class="modal-title">Send Templated Email/SMS</h4>
				</div>
				<div class="modal-body">
					<form:form method="post" action="createMailTemplate" name="createMailTemplateForm"
						onsubmit="return validateCreateTemplateForm()" role="form">
						<div class="col-sm-12">
							<div class="form-group">
								<label for="email">Subject</label> <input type="text"
									class="form-control" id="subject" name="subject"
									placeholder="Please enter Subject">
							</div>
							<div class="form-group">
								<label for="pwd">Message</label>
								<textarea placeholder="Please enter message here"
									class="form-control"  id= "messageBody" name="messagebody" rows="10"></textarea>
							</div>
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
							<%-- <div class="form-group">
								<label for="pwd">Course</label><br> <select
									class="selectpicker" title="Select Course"
									id="createTemplateCourse" name="courseId">
									<c:forEach var="courses" items="${coursesMap}">
										<option value="${courses.key}">${courses.value}</option>
									</c:forEach>
								</select>
							</div> --%>
							<button type="submit" class="btn btn-success">Create
								Template</button>
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