<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>

<meta charset="utf-8">
<title>LMS Login Page</title>
<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/font-awesome.css"/>"
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
    $(document).ready(function() {
        $("#changeStatusButton").click(function(){
            var leadIds = [];
            $.each($("input[name='leadId']:checked"), function(){            
            	leadIds.push($(this).val());
            });
            $('#leadIds').val(leadIds.join(","));
            $("#changeStatusForm").submit();
        });
    });
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
			<section id="action">
			<ul class="default-filters">
				<li><a href="dashboard?leadStatus=1" class="active"><i
						class="fa fa-external-link" aria-hidden="true"></i> New <span>${dashBoardForm.newCount}</span></a></li>
				<li><a href="dashboard?leadStatus=2"><i
						class="fa fa-folder-open-o" aria-hidden="true"></i> Open
						${dashBoardForm.openCount}</a></li>
				<li><a href="dashboard?leadStatus=3"><i
						class="fa fa-folder-o" aria-hidden="true"></i> Closed
						${dashBoardForm.closedCount}</a></li>
				<li><a href="dashboard?leadStatus=0"><i class="fa fa-bars"
						aria-hidden="true"></i> All ${dashBoardForm.totalLeadsCount}</a></li>


				<li class="right"><a data-toggle="modal" role="button"
					data-target="#filter"><i class="fa fa-filter"
						aria-hidden="true"></i> Filter</a></li>
				<li class="right"><a data-toggle="modal" role="button"
					data-target="#addlead"><i class="fa fa-plus-square"
						aria-hidden="true"></i> Add Lead</a></li>

			</ul>
			</section>
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
				Showing <select>
					<option value ="5">5</option>
					<option value ="10">10</option>
					<option value ="20">20</option>
					<option value ="30">30</option>
				</select>
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
								<td><input type="checkbox" value="${lead.enqID}" name="leadId"></td>
								<td><a href="#"><i class="fa fa-pencil-square-o"
										aria-hidden="true"></i></a></td>
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
					<c:if test="${leadsList == null}">
						<tr>
							<h3>No leads Found.</h3>
						</tr>
					</c:if>

				</tbody>
			</table>
			<span class="pull-left">Showing 1 to 50 of 76,179 entries</span>
			<ul class="pagination pull-right">
				<li><a href="#">1</a></li>
				<li class="active"><a href="#">2</a></li>
				<li><a href="#">3</a></li>
				<li><a href="#">4</a></li>
				<li><a href="#">5</a></li>
			</ul>
			</section>
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
					<form:form method="post" action="changeStatus" id="changeStatusForm" class="form-inline" role="form">
						<input type="hidden" id="leadIds" name ="leadIds" value="">
							<div class="form-group">
								<label class="sr-only" for="email">Email address:</label> 
								<select	class="selectpicker">
									<option value ="1">New</option>
									<option value ="2">Open</option>
									<option value ="3">Closed</option>
								</select>
							</div>
							<div class="modal-footer">
							<button type="button" id ="changeStatusButton" class="btn btn-success">Submit</button>
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
					<p>It will mark this lead as 'Successfully Closed'</p>
					<p>Do you want to close this lead?</p>
					<div class="modal-footer">
						<button type="submit" class="btn btn-success">Lead
							Converted Successfully</button>
						<button type="submit" class="btn btn-danger">Not Yet
							Closed</button>
					</div>
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
					<p>Select A Reason For Failed Enquiries.</p>
					<form class="form-inline" role="form">
						<div class="form-group">
							<label class="sr-only" for="email">Email address:</label> <select
								class="selectpicker">
								<option>Admission Taken</option>
								<option>Are Not Served</option>
								<option>Customer Not Interested</option>
								<option>Duplicate Enquiry</option>
								<option>Pricing Issue</option>
							</select>
						</div>
					</form>
					<div class="modal-footer">
						<button type="submit" class="btn btn-success">Failed To
							Convert</button>
						<button type="submit" class="btn btn-danger">Cancel</button>
					</div>
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
					<p>Are you sure you want to delete leads?</p>
					<p>Note : After deletion, you can ONLY view deleted leads but
						can't restore it or do ANY operation.</p>
					<div class="modal-footer">
						<button type="submit" class="btn btn-success">Delete</button>
						<button type="submit" class="btn btn-danger">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</div>


	<!--mark as Success-->
	<div id="dwnexcel" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Download Lead</h4>
				</div>
				<div class="modal-body">
					<p>Are you sure you want to Download leads?</p>
					<div class="modal-footer">
						<button type="submit" class="btn btn-success">Download</button>
						<button type="submit" class="btn btn-danger">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!--Filter-->
	<div id="filter" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Filter By</h4>
				</div>
				<div class="modal-body">
					<form role="form">
						<div class="form-group">
							<label for="email">From date</label> <input type="email"
								class="form-control" id="email">
						</div>
						<div class="form-group">
							<label for="pwd">To Date</label> <input type="password"
								class="form-control" id="pwd">
						</div>
						<div class="form-group">
							<label for="pwd">Course</label><br> <select
								class="selectpicker">
								<option>Course 1</option>
								<option>Course 1</option>
								<option>Course 1</option>
								<option>Course 1</option>
								<option>Course 1</option>
								<option>Course 1</option>
							</select>
						</div>
					</form>
					<div class="modal-footer">
						<button type="submit" class="btn btn-success">Download
							Report</button>
						<button type="submit" class="btn btn-success">Apply
							Filter</button>
						<button type="submit" class="btn btn-danger">Cancel</button>
					</div>
				</div>
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
					<form role="form">
						<div class="form-group">
							<label for="email">Student Name</label> <input type="text"
								class="form-control" id="email">
						</div>
						<div class="form-group">
							<label for="pwd">Phone Number</label> <input type="text"
								class="form-control" id="pwd">
						</div>
						<div class="form-group">
							<label for="pwd">Email ID</label> <input type="email"
								class="form-control" id="pwd">
						</div>
						<div class="form-group wd50">
							<label for="pwd">Course</label><br> <select
								class="selectpicker" multiple title="Select Course">
								<option>Course 1</option>
								<option>Course 2</option>
								<option>Course 3</option>
								<option>Course 4</option>
								<option>Course 5</option>
							</select>
						</div>
						<div class="form-group wd50">
							<label for="pwd">Source Lead</label><br> <select
								class="selectpicker" title="Select Source">
								<option>Yet 5</option>
								<option>Suleka</option>
								<option>Justdial</option>
								<option>Walkin</option>
								<option>Phone Enquiry</option>
							</select>
						</div>
						<div class="form-group">
							<label for="pwd">Commentes</label><br>
							<textarea class="form-control"></textarea>
						</div>
					</form>
					<div class="modal-footer">
						<button type="submit" class="btn btn-success">Add Lead</button>
						<button type="submit" class="btn btn-danger">Cancel</button>
					</div>
				</div>
			</div>
		</div>
	</div>


</body>
</html>