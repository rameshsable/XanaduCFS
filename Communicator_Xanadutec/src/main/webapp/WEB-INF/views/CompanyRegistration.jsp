
<%@page import="com.xanadutec.coreflex.userTask.UserService"%>
<%@page import="java.util.List"%>
<%@page import="com.xanadutec.coreflex.model.UserModel"%>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
    <title>Home</title>
<script src="${pageContext.request.contextPath}/resources/Company/company.js"charset='utf-8'></script>
<jsp:include page="body.jsp"></jsp:include>

<script type="text/javascript">
					var userid =new Array();
					var firstName = new Array();   //firstName  lastName  userName pass  email mob  companyId
					var lastName = new Array();
					var userName=new Array();
					var pass = new Array();
					var Email = new Array();
					var mob = new Array();
					var companyId=new Array();
</script>

<c:forEach var="customer1" items="${adminList}">
					<script>
								userid.push("${customer1.userId}");
								firstName.push("${customer1.firstName}");
								lastName.push("${customer1.lastName}");// it enters child name into array named customer
								userName.push("${customer1.userName}");
								pass.push("${customer1.password}");
								Email.push("${customer1.email}");
								mob.push("${customer1.mobileNo}");
								companyId.push("${customer1.company.companyId}")
					</script>
</c:forEach>
</head>
<body>
   <jsp:include page="menu.jsp"></jsp:include> 

	<div class="container">
		<div class="row adminContentWrap ">
			<form class="form-horizontal" id="companyForm" method="post">
				<div class="col-md-6">
					<div class="companyInfoSection">
						<h3>Company Information</h3>
						<div class="formWrapper">
						  <div class="form-group">
						    <label for="companyname" class="col-sm-2 control-label">Company Name</label>
						    <div class="col-sm-8">
						      <input type="text" name="companyname" id="companyname" class="form-control" >
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="contact" class="col-sm-2 control-label">Contact Number</label>
						    <div class="col-sm-8">
						      <input type="text"  id="contact" name="contact" class="form-control" >
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="contact" class="col-sm-2 control-label">Email Id</label>
						    <div class="col-sm-8">
						      <input type="text"  id="email" name="email" class="form-control" >
						    </div>
						  </div>
						  <div class="form-group addressWrapper">
						    <label for="address" class="col-sm-2 control-label">Address</label>
						    <div class="col-sm-8">
						    	<input type="text"  id="address" name="address" class="form-control" >
						      	<input type="text"  id="address" name="address" class="form-control" >
						    </div>
						  </div>
						  <div class="form-group uploadLogoWrap">
						    <label for="uploadLogo" class="col-sm-2 control-label">Upload Logo</label>
						    <div class="col-sm-8">
						    	<input type="file"  id="uploadLogo" name="uploadLogo" class="form-control" >
						      	
						    </div>
						  </div>
						</div>
					</div>
					</div>
				<div class="col-md-6">
					<div class="companyInfoSection">
						<h3>Account Holder Information</h3>
						<div class="formWrapper">
						  <div class="form-group">
						    <label for="fname" class="col-sm-2 control-label">First Name</label>
						    <div class="col-sm-8">
						      <input type="text" name="fname" class="form-control" size="20" id=fname />
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="lname" class="col-sm-2 control-label">Last Name</label>
						    <div class="col-sm-8">
						      <input type="text" name="lname" class="form-control" size="20"  id="lname"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="uname" class="col-sm-2 control-label">Username</label>
						    <div class="col-sm-8">
						      <input type="text" name="uname" class="form-control" size="20" id="uname"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="password" class="col-sm-2 control-label">Password</label>
						    <div class="col-sm-8">
						      <input type="password" name="password" class="form-control" size="20" id="password" />
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="password" class="col-sm-2 control-label">Confirm Password</label>
						    <div class="col-sm-8">
						      <input type="password" name="cpassword" class="form-control" size="20" id="cpassword"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="Email" class="col-sm-2 control-label">Email ID</label>
						    <div class="col-sm-8">
						      <input type="text" name="Email" class="form-control" size="20" id="emailval"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="mno" class="col-sm-2 control-label">Mobile Number</label>
						    <div class="col-sm-8">
						      <input type="text" name="mno" class="form-control" size="20" id="mno" maxlength="10" onkeypress="return isNumberKey(event)"/>
						    </div>
						  </div>
						</div>
					</div>
				</div>
			<div class="text-center">
				<button type="button" id ="save" onclick="return saveCompany();" name="save" class="btn btn-small btn-primary ">Save</button>
		  		<button type="button" id="update" onclick="return updateCompany();" class="btn btn-small btn-secondary ">Update</button>
	  		</div>
	  		<div class="alert alert-danger alert-dismissable" id="name1">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					<strong>Warning: </strong>please Enter Company Name.
			</div>
			<div class="alert alert-danger alert-dismissable" id="contact1">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Please Enter Contact Number. 
			</div>
			<div class="alert alert-danger alert-dismissable" id="email1">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Please Enter Email-Id.
			</div>
			<div class="alert alert-danger alert-dismissable" id="address1">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Please Enter Address .
			</div>
			<div class="alert alert-danger alert-dismissable" id="samename">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Company Name Already Available.
			</div>
			<div class="alert alert-success alert-dismissable" id="successalt">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					<strong>Success: </strong>Company Registration Successfully.
			</div>
			<div class="alert alert-success alert-dismissable" id="updatealt">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					<strong>Success: </strong>Company Registration Updated Successfully.
			</div>
			<div class="alert alert-danger alert-dismissable" id="fnameselect">
			 	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					<strong>Warning: </strong>please Enter first name.
				</div>
				<div class="alert alert-danger alert-dismissable" id="lnameselect">
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
	  		  
				</form>
		</div>
		 <div class="companyListingTable">
            <table  class="table table-striped"  id="dataTable1"  >
               <thead>
                   <tr>
                     <th>Name <i class="fa fa-sort-amount-asc"></i></th>
                     <th>Contact <i class="fa fa-sort-amount-asc"></i></th>
                     <th>Email <i class="fa fa-sort-amount-asc"></i></th>
                     <th>Address <i class="fa fa-sort-amount-asc"></i></th>
                     <th>Edit <i class="fa fa-sort-amount-asc"></i></th> 
                   </tr>
               </thead>
                      <tbody>
				<c:forEach var="company"   items="${companies}"  varStatus="theCount">
 					<tr>      
 					<!-- trial demo -->		
                   <td width="25%"><a href="#">${company.companyName}</a></td>
                   <td><a href="#">  ${company.companyContact}</a></td>
                   <td><a href="#">  ${company.companyEmail}</a></td>
                   <td><a href="#">  ${company.companyAddress}</a></td>
                   <td class="text-center">
                   <a href="# " onclick=" editCompany('${company.companyId}','${company.companyName}','${company.companyContact}','${company.companyEmail}','${company.companyAddress}','${company.companyId}');">
				    <i class="fa fa-pencil-square-o"></i>
				   </a>
			   	</td>											                  
                   </tr>                
                   </c:forEach>
				</tbody>
			</table>
		 	<input type="hidden" name="comId" Id="comId" value="">
		 	<input type="hidden" name="id" Id="id" value="">
		 	<input type="hidden" name="userid" Id="userid" value="">
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
			<script type="text/javascript">
	              $("#name1").hide();
	              $("#contact1").hide();
	              $("#email1").hide();
	              $("#address1").hide();
	              $("#samename").hide();
	              $("#successalt").hide();
	              $("#updatealt").hide();
          	</script>
			<script type="text/javascript">
			 $("#update").prop('disabled',true);
				 $('#dataTable1').dataTable({
					"bJQueryUI" : true,
					"sPaginationType" : "full_numbers",
					"sDom" : 'T<"clear">lfrtip<"clear spacer">T'
			
				}); 
			</script>

</body>

</html>

       

		



