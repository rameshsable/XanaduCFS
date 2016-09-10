<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script
	src="${pageContext.request.contextPath}/resources/createFeedBackType/companyFeedbackTypeAssociation.js"
	charset='utf-8'></script>
<jsp:include page="body.jsp"></jsp:include>

</head>
<script>
	var files = [];
</script>

<body>
	<jsp:include page="menu.jsp"></jsp:include>
	<div id="home">
	<div class="container">
		<div class="adminContentWrap clearfix">
		 	<ul class="feedbackTabs list-unstyled clearfix">
				<li class="active"
					onclick="switchView('createFeedBackType', this);"><a
					href="#">Create FeedBack Type</a></li>

				<li onclick="switchView('assignFeedBackType', this);"><a
					href="#">Assign FeedBack Type To Company</a></li>
			</ul>
			<div class="clearfix"></div>
					<div id="createFeedBackType" class="tabsContentWrapper">
 							<div id="createFeedBackTypeFirst">
 								<div class="feedbackTypeWrap">
 									<div class="saveFeedback clearfix">										
										<label>FeedBack Type</label>
										<input class="feedbackTypeInput" type="text" id="feedTypeId" name="id="feedTypeName"> 
										<input type="button" class="btn btn-primary" value="Save FeedBack Type" onclick="return addFeedBackTypeInTable();"> 
									</div>
										<h4>Feedback Types : </h4>
										<table class="table table-bordered table-hover table-striped" id="dataTable1">
											<thead>
											<tr>
												<th>Sr No <i class="fa fa-sort-amount-asc"></i></th>
												<th>FeedBack Type <i class="fa fa-sort-amount-asc"></i></th>
											</tr>
										</thead>
										<tbody>
												<c:forEach var="feedobj" items="${FeedBackList}"
												varStatus="theCount">
												<tr>
													<td>${theCount.count}</td>
													<td>${feedobj.feedbackType}</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
							 	</div>
							</div>
	
							<div class="col-lg-3 centerDiv center-block"
								id="createFeedBackTypeSecond"></div>
	
							<div class="col-lg-4 centerDiv center-block"
								id="createFeedBackTypeSecond">
	
								<form>
									<div class="form-group">
										<div class="col-xs-10">
											<br>
										</div>
	
									</div>
								</form>
	
							</div>
	
 
					</div>
	
					<div id="assignFeedBackType"  class="tabsContentWrapper">
						<div class="feedbackTypeWrap">
							<div id="documetntFirst">
									<div class="selectCompanyWrap">
										<label> Company </label> <select id="companieslst"
										class="  label label-default"
										onchange="loadCompanyFeedBackTypeAssociation(this.value);">
										<option value="0">Select Company
											</option>
										<c:forEach var="companies" items="${companiesList}"
											varStatus="theCount">
											<option value="${companies.companyId}">${companies.companyName}
											</option>
		
										</c:forEach>
									</select>
								</div>
								</div>
							 <div class="tab-pane active" id="association">
								<form id="CompanyFeedBackTypeAssociationForm">
									<div class="assignFeedbackCompanyList" id="CompanyFeedBackTypeAssociationTable">
										<%
											int count = 1;
										%>
	
										<!-- -----------ID: 1 -->
	
										<c:forEach var="feedobj" items="${FeedBackList}"
											varStatus="theCount">
											<%
												if (count == 1) {
											%>
											<ul class="list-unstyled clearfix">
	
												<li>
													<label>
														<input type="checkbox"
														class="checkbox" id="${feedobj.id}"
														name="${feedobj.id}" />
													<span>${feedobj.feedbackType}</span>
													</label>
												</li>
	
	
												<%
													count++;
														} else if (count <= 4) {
												%>
												<li>
													<label>
														<input type="checkbox"
														class="checkbox" id="${feedobj.id}"
														name="${feedobj.id}" />
														<span>${feedobj.feedbackType}</span>
													</label>
												</li>
	
												<%
													count++;
														}
														if (count == 5) {
												%>
											</ul>
											<%
												count = 1;
													}
											%>
										</c:forEach>
									</div>
									<div align="center">
										<input type="Button"
											value="Assign FeedbackType To Company"
											onclick="CompanyFeedBackTypeAssociation();"
											class="btn btn-small btn-primary " />
									</div>
								</form>
							</div>
							
							</div>
									</div>
		</div>
	</div>
	</div>	
	
	<div class="alert alert-success alert-dismissable" id="successselect">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		<strong>Success: </strong>File Uploaded successFully.
	</div>
	<div class="alert alert-danger alert-dismissable" id="failselect">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		<strong>Warning: </strong>please Select File .
	</div>
	<script type="text/javascript">
		$("#successselect").hide();
		$("#failselect").hide();
	</script>
	<div style="margin-top: 20px;" class="alert alert-success "
		id="feedbackTypeSaved">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		<strong>Success: </strong>FeedBack Type Saved SuccessFully.
	</div>

	<div style="margin-top: 20px;" class="alert alert-danger "
		id="feedbackTypeAlready">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		<strong>Warning: </strong>FeedBack Type Already Availabe.
	</div>


	<div style="margin-top: 20px;" class="alert alert-success "
		id="successForm">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		<strong>Success: </strong>FeedBack Type Allocated to Company
		SuccessFully.
	</div>
	<div style="margin-top: 20px;" class="alert alert-danger "
		id="selectCompany">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		<strong>Warning: </strong>Select Company First.
	</div>

	<script type="text/javascript">
		$("#feedbackTypeSaved").hide();
		$("#feedbackTypeAlready").hide();
		$("#successForm").hide();
		$("#selectCompany").hide();
		
	</script>
<script type="text/javascript">
	$('#datacreateFeedBackType').dataTable({

		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

	});

	var table = $('#dataTable1').DataTable({

		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

	});

	function addFeedBackTypeInTable() {

		var actFeedName = $("#feedTypeId").val();
		var r = 1;
		var flag = 0;
		// datatable  iterate  all table
		table.column(1).data().each(function(value, index) {

			if (actFeedName == value) {
				//	alert('Data in index: ' + index + ' is: ' + value);
				$("#feedbackTypeAlready").slideDown();
				$("#feedbackTypeAlready").fadeOut(5000);
				flag = 1;

			}
			r++;
		});

		if (flag == 0) {

			// datatable add new row  
			var rowNode = table.row.add([ r, actFeedName ]).draw();

			$
					.ajax({
						url : "saveFeedBackTypeTable",
						type : "GET",
						data : {
							feedbackType : "" + actFeedName
						},
						success : function(data) {
							$("#feedbackTypeSaved").slideDown();
							$("#feedbackTypeSaved").fadeOut(5000);

							var x = "<td><label><input type='checkbox' class='checkbox' id='"+data+"' name='"+data+"'' />"
									+ actFeedName + "</label></td>";
							$(
									"#CompanyFeedBackTypeAssociationTable")
									.append(x);
							$("#feedTypeId").val("");
						},
						error : function(xhr, ajaxOptions,
								thrownError) {

						}
					});
		}

	}
</script>
<script>
	function hideAllViews() {

		$("#association").hide();
		$("#createFeedBackType").hide();

		$("#assignFeedBackType").hide();

	}

	function switchView(currentSubTabView, currentLi) {
		hideAllViews();

		$("#" + currentSubTabView).show();

		$(".feedbackTabs li").removeClass("active");

		$(currentLi).addClass("active");
	}

	function loadDataAfterLoadingPage() {

		switchView('createFeedBackType');

		$(".feedbackTabs li:first-child").addClass("active");

		$("#teacher_distribute_product").hide();
		$("#class_distribute_product").hide();

		$("#classNotSelectedAlert").hide();
		$("#sectionNotSelectedAlert").hide();
		$("#studentNotSelectedAlert").hide();
		$("#teacherNotSelectedAlert").hide();
		$("#dateNotSelectedAlert").hide();
	}

	loadDataAfterLoadingPage();
</script>

<script>
$('#myTabs a').click(function (e) {
	  e.preventDefault()
	  $(this).tab('show')
	})
</script>
</body>
</html>



