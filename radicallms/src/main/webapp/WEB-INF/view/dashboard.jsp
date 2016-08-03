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
<link href="<c:url value="/resources/css/font-awesome.css"/>"
	rel="stylesheet" />
<script>
	function validateCreateNDform() {
		return true;
	}
</script>

<!-- Bootstrap -->
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">
	
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.6.1/css/bootstrap-datepicker.css">
</head>

<script src="<c:url value="/resources/js/jquery1_8_1.js" />"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>
<script>
	$(function() {
		$("[rel='tooltip']").tooltip();
	});
	$(document).ready(function() {
		$('.selectpicker').selectpicker();
	});

	function validateStatusForm() {

	}
</script>

<script type="text/javascript">
var basepath = "${pageContext.request.contextPath}";
	$(document).ready(function() {

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
			$('#filterType').val("1");
			$("#filterByDateAndCourseForm").submit();
		});
		$("#showLeadsFilter").click(function() {
			$('#filterType').val("0");
			$("#filterByDateAndCourseForm").submit();
		});
		
		$("#download").click(function() {
			$("#downloadLeadsToSheetForm").submit();
		});
		
        $("#showingId").change(function() {
        	$("#getShowingForm").submit();
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
    		$('#editCourse').selectpicker('val', data.course);
    		$('#editMode').selectpicker('val', data.modeofTraining);
    		$('#editType').selectpicker('val', data.typeofTraining);
    		$('#editComments').val(data.comments);
    	});
    }
    
    function validateEditLeadForm (){
    	return true;
    }

</script>
<body>
	<div class="container-fluid">
		<div class="row">
			<section id="header">
			<div class="row">
				<div class="col-md-6">
					<span class="logo"><img
						src="http://www.radicaltechnologies.co.in/wp-content/themes/radical/images/logo.png" /></span>
					<span class="searchform"> <input type="text"
						placeholder="Search Lead">
						<button type="submit">Search</button>
					</span>
				</div>
				<div class="col-md-6">
					<span class="pull-right account"><a href="#">LMS</a> | <a
						href="#">Administration</a> | <a href="#">Profile</a> | <a
						href="logout">Logout</a></span>
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
							<li><a>Templated Email</a></li>
							<li><a>Custom Email</a></li>
						</ul></li>

				</ul>
			</div>
			<div class="count">
				<form:form method="post" action="getShowingData" id="getShowingForm"
					class="form-inline" role="form">
				Showing <select id="showingId" name="pageLimit">
						<c:forEach items="${dashBoardForm.limitList}" var="limit">
							<option value="${limit}"
								<c:if test="${limit == dashBoardForm.pageLimit}">selected</c:if>>${limit}</option>
						</c:forEach>
					</select>
				</form:form>
			</div>
			
			<table class="table table-bordered">
				<thead>
					<tr>
						<th></th>
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
									name="leadId"></td>
								<td><a data-toggle="modal" role="button" data-target="#editlead" onclick="getLeadInfo(${lead.enqID})"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a></td>
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
			<span class="pull-left">Showing ${dashBoardForm.startLimit} to
				${dashBoardForm.endLimit} of ${dashBoardForm.pageTotalCount}
				entries</span>
			<ul class="pagination pull-right">
				<c:forEach items="${dashBoardForm.pageList}" var="page"
					varStatus="pageIndex">
					<li
						class="<c:if test="${dashBoardForm.pageNumber == page}">active</c:if>">
						<a href="javascript:void(0)" onclick="getPagination(${page})">${page}</a>
					</li>
				</c:forEach>
				<form:form method="post" action="getPaginationData"
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
			<form:form method="post" action="downloadLeadsToSheet" id ="downloadLeadsToSheetForm" role="form">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Download Lead</h4>
					</div>
					<div class="modal-body">
						<p>Are you sure you want to Download leads?</p>
						<div class="modal-footer">
							<button type="button" id="download" class="btn btn-success" data-dismiss="modal">Download</button>
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
                                    <input type="text" class="form-control"  id="fromDate" name="fromDate" placeholder="Select from date">
                                    <div class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </div>
                                </div>
						</div>
						<div class="form-group">
							<label for="pwd">To Date</label> <input type="text"
								class="form-control" id="toDate" name="toDate">
						</div>
						<div class="form-group">
							<label for="pwd">Course</label><br> <select
								class="selectpicker" title="Select Course" id="course"
								name="course">
								<c:forEach var="courses" items="${coursesMap}">
									<option value="${courses.key}">${courses.value}</option>
								</c:forEach>
							</select>
						</div>
						<input type="hidden" name="filterType" id="filterType" value="">
						<div class="modal-footer">
							<button type="button" class="btn btn-success"
								id="downloadLeadsFilter" data-dismiss="modal">Download Report</button>
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
						onsubmit="return validateCreateNDform()">
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
							<label for="pwd">Course</label><br> <select
								class="selectpicker" multiple title="Select Course" id="course"
								name="course">
								<c:forEach var="courses" items="${coursesMap}">
									<option value="${courses.key}">${courses.value}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group wd50">
							<label for="pwd">Source Lead</label><br> <select
								class="selectpicker" title="Select Source" id="leadSource"
								name="leadSource">
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
							<button type="submit" class="btn btn-danger" data-dismiss="modal">Cancel</button>
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
							<input type="hidden" class="form-control" id="editLeadId" value="" name = "leadiId">
							<input type="hidden" class="form-control" id="editStatus" value="" name = "status">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="email">Student Name</label>
                                    <input type="text" class="form-control" id="editName" value="" name = "name">
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Phone Number</label>
                                    <input type="text" class="form-control" id="editMobileNo" value="" name = "mobileNo">
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Email ID</label>
                                    <input type="email" class="form-control" id="editEmail" value="" name = "emailId">
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Address</label>
                                    <input type="text" class="form-control" id="editAddress" value="" name = "address">
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Area</label>
                                    <input type="text" class="form-control" id="editArea" value="" name = "area" >
                                </div>
                                <div class="form-group">
                                    <label for="pwd">City</label>
                                    <input type="text" class="form-control" id="editCity" value="" name = "city">
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Center Location</label><br>
                                    <select class="selectpicker" title="Select Location" id="editCenter" name = "location">
                                        <option value ="Location 1">Location 1</option>
                                        <option value ="Location 2">Location 2</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pwd">Assigned to</label><br>
                                    <select class="selectpicker" multiple title="Assigned to" id="editAssigned" name = "assignedTo">
                                        <option value="1">Person 1</option>
                                        <option value="2">Person 2</option>
                                        <option value="3">Person 3</option>
                                        <option value="4">Person 4</option>
                                        <option value="5">Person 5</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Source</label><br>
                                    <select class="selectpicker" title="Select source" id = "editSource" name = "leadSource">
                                   	 <c:forEach var="leadSource" items="${leadSourceMapping}">
										<option value="${leadSource.key}">${leadSource.value}</option>
									 </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Category</label><br>
                                    <select class="selectpicker" title="Select Category" id ="editCategory" name = "courseCategeory">
                                     <c:forEach var="category" items="${courseCategories}">
										<option value="${category.key}">${category.value}</option>
									 </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Course</label><br>
                                    <select class="selectpicker" title="Select Course" id ="editCourse" name = "course">
                                    <c:forEach var="courses" items="${coursesMap}">
										<option value="${courses.key}">${courses.value}</option>
									</c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="pwd">Mode of Training</label><br>
                                    <select class="selectpicker" title="Select Training" id= "editMode" name = "modeofTraining">
                                        <option value="Classroom">Classroom</option>
                                        <option value="Online">Online</option>
                                    </select>
                                </div>
                                
                                
                                <div class="form-group">
                                    <label for="pwd">Weekday/Weekend</label><br>
                                    <select class="selectpicker" title="Select Type" id = "editType" name = "typeofTraining">
                                        <option value = "Weekend">Weekend</option>
                                        <option value ="Weekday">Weekday</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pwd">Commentes</label><br>
                                    <textarea class="form-control" id ="editComments" name = "comments"></textarea>
                                </div>
                                <div class="modal-footer">
                            <button type="submit" class="btn btn-success">Update Lead</button>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                        	</div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
</body>
</html>