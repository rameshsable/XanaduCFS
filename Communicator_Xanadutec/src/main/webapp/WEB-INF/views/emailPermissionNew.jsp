
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<head>
<title>Home</title>


<script
	src="${pageContext.request.contextPath}/resources/userRegisrtation/EmailSendReceivePermission.js"
	charset='utf-8'></script>
<jsp:include page="body.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="menu.jsp"></jsp:include>
	<div id="home">
		<div class="container">
			<div class="adminContentWrap">
				<div class="alert alert-success" id="successForm">
					<button type="button" class="close" data-dismiss="alert"
						aria-hidden="true">&times;</button>
					<strong>Success: </strong>Email Permission Assigned Success.
				</div>
					<script type="text/javascript">
						$("#successForm").hide();						
					</script>
					<h3>Email Send and Receive Permissions</h3>
					<div class="userPermissionInnerContent emailPermissionWrapper">
						<form action="#" id="regform" novalidate="novalidate">
							<div class="form-group">
								<select id="cname" name="cname"
									class="  label label-default"
									onchange="loadEmailSendReceivePermissionsByCompanyId(this.value);">
									<option value="0">Select Company</option>
									<c:forEach var="company" items="${companiesList}"
										varStatus="theCount">
										<option value='${company.companyId}'>
											${company.companyName}</option>
									</c:forEach>
								</select>
								</div>
						</form>
						<div id="association">
								<form id="assignEmailPermissionForm">
									<div id="assignEmailPermissionTable">
									</div>
									<div class="clearfix"></div>
									<div align="center">
										<input type="Button" value="Assign Email Permissions To User"
											onclick="assignEmailPermission();" class="btn btn-primary " />
									</div>
								</form>
								</div>
						</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$('#dataTable1').dataTable({

			"sPaginationType" : "full_numbers",
			"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

		});
	</script>
</body>
</html>










































































