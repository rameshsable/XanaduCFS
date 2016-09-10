<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
var permissionCompanyId=new Array();
</script>
<c:forEach var="userp" items="${userPermissions}" varStatus="theCount">
<script>
permissionCompanyId.push("${userp.company.companyId}");
</script>
</c:forEach>

	<h4>Assign Permission To user</h4>
	<div id="settings">
		<form id="userpe" class="userPermissionListing">
			<ul class="list-unstyled clearfix">
				<c:forEach var="company" items="${companies}" varStatus="theCount">
					<li>
						<label>
							<input type="checkbox" class="checkbox" id="${company.companyId}" name="${company.companyId}" />
							<span>${company.companyName}</span>
						</label>
					</li>			  	 
				 </c:forEach>
			</ul>
		 </form>
	</div>
	<div class="clearfix"></div>


<script>
for(j=0;j<permissionCompanyId.length;j++){
	$('#'+permissionCompanyId[j]).prop('checked', true);	
}

</script>
 
<div align="center">
	<input type="Button" value="Save User Permissions" onclick="saveUserPermission();" class="btn btn-primary" />
</div>


