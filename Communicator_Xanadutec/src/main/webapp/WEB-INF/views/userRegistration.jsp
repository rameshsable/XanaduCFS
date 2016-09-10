
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<head>
    <title>User Registration</title>

<jsp:include page="body.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/resources/FeedbackCom/Feedback.js"charset='utf-8'>
</script>
<script src="${pageContext.request.contextPath}/resources/userRegisrtation/userRegis.js"charset='utf-8'>
</script>

<!--  validation form -->
<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script> -->
</head>
<body>
       <jsp:include page="menu.jsp"></jsp:include>
<div id="home">
	<div class="container">
		<div class="adminContentWrap createNewUserWrapper clearfix">
			<div class="col-md-6 col-md-offset-3">
				<label id="errorBox"></label>
				<h3>Create New User</h3>
				<div class="newUserFormWrapper verticleFormWrapper">
				<form action="#" id="regform" novalidate="novalidate">
				  <div class="form-group clearfix">
				    <label for="fname" class="control-label">First Name</label>
				    <div class="col-sm-8">
				      <input type="text" name="fname" class="form-control" size="20" id=fname />
				    </div>
				  </div>
				  <div class="form-group clearfix">
				    <label for="lname" class="control-label">Last Name</label>
				    <div class="col-sm-8">
				      <input type="text" name="lname" class="form-control" size="20"  id="lname"/>
				    </div>
				  </div>
				  <div class="form-group clearfix">
				    <label for="lname" class="control-label">User Name</label>
				    <div class="col-sm-8">
				      <input type="text" name="uname" class="form-control" size="20" id="uname"/>
				    </div>
				  </div>
				  <div class="form-group clearfix">
				    <label for="password" class="control-label">Password</label>
				    <div class="col-sm-8">
				      <input type="password" name="password" class="form-control" size="20" id="password" />
				    </div>
				  </div>
				  <div class="form-group clearfix">
				    <label for="cpassword" class="control-label">Confirm Password</label>
				    <div class="col-sm-8">
				      <input type="password" name="cpassword" class="form-control" size="20" id="cpassword"/>
				    </div>
				  </div>
				  <div class="form-group clearfix">
				    <label for="email" class="control-label">Email</label>
				    <div class="col-sm-8">
				      <input type="text" name="Email" class="form-control" size="20" id="email"/>
				    </div>
				  </div>
				  <div class="form-group clearfix">
				    <label for="mno" class="control-label">Mobile Number</label>
				    <div class="col-sm-8">
				      <input type="text" name="mno" class="form-control" size="20" id="mno" maxlength="10" onkeypress="return isNumberKey(event)"/>
				    </div>
				  </div>
				  <div class="form-group clearfix">
				    <label for="cname" class="control-label">Company</label>
				    <div class="col-sm-8">
				      <select id="cname" name="cname">
					     <option value= "0">Company</option>
					  	 <c:forEach var="company" items="${companies}" varStatus="theCount">
					  	 <option value= '${company.companyId}'> ${company.companyName}</option>
					  	 </c:forEach>
				  	  </select>
				    </div>
				  </div>
				  <div class="text-center">
				  	<input type="reset" value="clear" class="btn btn-primary btn-small ">
					<input type="Button" value="submit" id="submit" onclick="return saveUserRegistration();" class="btn btn-small btn-secondary"/>
				  </div>
				  </form>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="container">
	<div class="block users scrollBox table-responsive">
		<div class="alert alert-danger alert-dismissable"
			id="fnameselect">
			 <button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>please Enter first name.
		</div>
		<div class="alert alert-danger alert-dismissable"
			id="lnameselect">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>Please Enter Last Name.
			
		</div>
		<div class="alert alert-danger alert-dismissable"
			id="unameselect">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>Please Enter  User name.
		</div>
		 <div class="alert alert-danger alert-dismissable"
			id="passwordselect">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>Please Enter Password.
		</div> 
		 <div class="alert alert-danger alert-dismissable"
			id="cpasswordselect">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>password And confirm password must be same.
		</div>
		
		<div class="alert alert-danger alert-dismissable"
			id="emailselect">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>Please Enter valid Email ID.
		</div>
		
		<div class="alert alert-danger alert-dismissable"
			id="mnoselect">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>Please enter Mobile number.
			<strong>Warning: </strong>Or it must be 10 digit.
		</div>
		
		<div class="alert alert-danger alert-dismissable"
			id="cnameselect">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>please Select Company .
		</div>
		<div class="alert alert-success alert-dismissable"
			id="successeselect">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>form submitted.
		</div>
		<div class="alert alert-success alert-dismissable"
			id="userPresent">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>Warning: </strong>UserName Already Available
		</div>
		<div class="alert alert-success alert-dismissable"
			id="successForm">
			<button type="button" class="close" data-dismiss="alert"
		aria-hidden="true">&times;</button>
			<strong>success: </strong>form submitted Successfully.
		</div>
	</div>
</div>
				
 <script type="text/javascript">
     $("#fnameselect").hide();
     $("#lnameselect").hide();
     $("#unameselect").hide();
     $("#passwordselect").hide();
     $("#emailselect").hide();
     $("#cpasswordselect").hide();
     $("#mnoselect").hide();
     $("#cnameselect").hide();
     $("#successeselect").hide();
     $("#userPresent").hide();
     $("#successForm").hide();
</script>
<div class="container">
   <table class="table table-bordered table-hover table-striped" id="dataTable1"  >
       <thead>
	        <tr>
	         <th>Sr No <i class="fa fa-sort-amount-asc"></i></th>
	         <th>First Name <i class="fa fa-sort-amount-asc"></i></th>
	         <th>Last Name <i class="fa fa-sort-amount-asc"></i></th>
	         <th>User Name <i class="fa fa-sort-amount-asc"></i></th>
	         <th>PassWord <i class="fa fa-sort-amount-asc"></i></th>
	         <th>Email <i class="fa fa-sort-amount-asc"></i></th>
	         <th>Company <i class="fa fa-sort-amount-asc"></i></th>
	        </tr>
       </thead>
       <tbody>
     <c:forEach var="userlistobj" items="${userlist}" varStatus="theCount">
   <tr>
			    	<td><a href="#">${theCount.count }</a></td> 
					<td><a href="#">${userlistobj.firstName}</a></td>
					<td><a href="#" >${userlistobj.lastName}</a></td>
					<td><a href="#" >${userlistobj.userName}</a></td>
					<td><a href="#" >${userlistobj.password}</a></td>
					<td><a href="#" >${userlistobj.email}</a></td>
					<td><a href="#" >${userlistobj.company.companyName}</a></td>
					<%-- <td width="70%" onClick="viewMom('${mom.id}')"><a href="#">${mom.keypoints}</a></td> --%>
			</tr> 
   </c:forEach>
</tbody>
</table>
 </div>
         <script type="text/javascript">
				 $('#dataTable1').dataTable({
				
					"sPaginationType" : "full_numbers",
					"sDom" : 'T<"clear">lfrtip<"clear spacer">T'
			
				}); 

			</script>
</body>
</html>
