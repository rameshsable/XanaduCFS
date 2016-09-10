
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<head>
    <title>Home</title>

<jsp:include page="body.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/resources/UserPermission/userPermission.js"charset='utf-8'>
</script>
</head>
<body>
       <jsp:include page="menu.jsp"></jsp:include>
       
       <div id="home">
	       <div class="container">
	       	<div class="adminContentWrap">
	       		<div class="alert alert-success alert-dismissable"
						id="successForm">
						<button type="button" class="close" data-dismiss="alert"
					aria-hidden="true">&times;</button>
						<strong>Warning: </strong>Permission Assigned Successfully.
				</div>
				<h3 class="page-header">User Permissions</h3>
					<div class="userPermissionInnerContent">
		                <form action="#" id="regform" novalidate="novalidate" class="userPermissionWrapper">
		 			     <label>Select Permission To - </label>
		 			     <select id="cname" name="cname"  class = "  label label-default" onchange="loadUserPermission(this.value);" >
						     <option value= "0">Select User</option>
						  	 <c:forEach var="user" items="${userModels}" varStatus="theCount">
						  	 <option value= '${user.userId}'> ${user.firstName} - ${user.lastName}</option>
						  	 </c:forEach>
					  	  </select>
						<div id="userPermissionTable"></div>
						</form>  
					</div>
				</div>
			</div>
        </div>
        
        <script type="text/javascript">
             $("#successForm").hide();
		</script>

<script type="text/javascript">
	 $('#dataTable1').dataTable({
		"bJQueryUI" : true,
		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'
	
	}); 
</script>

</body>

</html>

     

		




 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 