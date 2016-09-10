


<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
    <title>MOM</title>

 <jsp:include page="body.jsp"></jsp:include> 
<script src="${pageContext.request.contextPath}/resources/momsummer/momFeedback.js"charset='utf-8'></script>
 
 
<!--  <link href="http://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel="stylesheet"> -->
      <script src="${pageContext.request.contextPath}/resources/MOMJS/jquery-1.10.2.js"></script>
      <script src="${pageContext.request.contextPath}/resources/MOMJS/jquery-ui.js"></script>
      
      
      
   <link href="${pageContext.request.contextPath}/resources/MOMJS/jquerysctipttop.css" rel="stylesheet" type="text/css">

<link href="${pageContext.request.contextPath}/resources/mom/jquery.flexdatalist.css" rel="stylesheet" type="text/css">

      <link href="${pageContext.request.contextPath}/resources/mom/jquery.flexdatalist.css" rel="stylesheet" type="text/css">
      
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">  


<script type="text/javascript">
$(document).ready(function(){
	$('#submit').click(function(){
	});
});
</script>



  <script> 
  
$(function() {
	$(function(){
	
		$("#datepicker").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2015:2099'

		}).datepicker('setDate',new Date());
		
		
	});
    /* $( "#datepicker" ).datepicker(); */
});


</script>



<script type="text/javascript">


var userId=new Array();
var userName=new Array();
var dummyuserName=new Array();
var dummyuserId=new Array();
</script>


 <c:forEach  var="user" items="${userList}">
<script>
userId.push("${user.userId}");
userName.push("${user.firstName} ${user.lastName}");

</script>
</c:forEach> 


<script>



</script>


<!-- 
<script type="text/javascript">
$(function () {
$('#attendee').keydown(function (e) {
if (e.shiftKey || e.ctrlKey || e.altKey) {
e.preventDefault();
} else {
var key = e.keyCode;
if (!((key == 8) || (key == 32) || (key == 46)|| (key == 9) ||(key == 190) ||(key == 188) || (key >= 35 && key <= 40) || (key >= 65 && key <= 90))) {
e.preventDefault();
}
}
});
});
</script> -->

</head>
<body>
       <jsp:include page="menu.jsp"></jsp:include>
       <div class="container">
       		<div class="adminContentWrap">
	       		<div class="alert alert-danger alert-dismissable"
					id="dateselect">
					<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Select Date First.
				</div>
				<div class="alert alert-danger alert-dismissable"
					id="attendeeselect">
					<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Please Type Attendee.
				</div>
				<div class="alert alert-danger alert-dismissable"
					id="subjectselect">
					<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Please Type The Subject.
				</div>
				 <div class="alert alert-danger alert-dismissable"
					id="messageselect">
					<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Please Enter Meeting Keypoints and Discussion.
				</div> 
				<div class="alert alert-success alert-dismissable"
					id="success">
					<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
					<strong>Warning: </strong>MOM submitted successfully.
				</div> 
				
				<div class="alert alert-success alert-dismissable"
					id="emailSend">
					<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
					<strong>Warning: </strong>Email sent successfully.
				</div>
				
					<div class="innerBreadcrumb pull-left clearfix">
						<ul class="list-unstyled clearfix">
							<li><a href="">Meeting Minutes <i class="fa fa-angle-right"></i></a></li>
							<li class="active">Create New Minutes</li>
						</ul>
					</div>
					<div class="clearfix"></div>
					<form action="${pageContext.request.contextPath}/momhome"method="post"  id="mform" >
						
						<ul class="list-unstyled momFormWrapper">
							<li class="subjectWrapper clearfix">
								<div class="momSubjectWrap">
									<input type="text" name="subject" placeholder="Type your Subject here" id="subject"size="40" class="form-control"/>
								</div>
								<div class="momDateWrap">
								<label>Date :</label>
									<input type="text"  name="date" id="datepicker" size="10" readonly="readonly" />
									<i class="fa fa-calendar"></i>
								</div>
							</li>
							
							
							
						
							<li class="attendeeWrap">
								 <label >Attendee</label> 
						 		<!-- <input type="text" name="attendee" id="attendee" size="40"  class="form-control"/ > -->
						 		
						 		
						 		<input type='text'
       placeholder='Enter Name'
       class='flexdatalist form-control'
       data-min-length='1'
       data-searchContain='true'
       multiple='multiple'
       list='skills'
       name='skill'
       id="attendee"  onchange="userIdForMom()" >
       
			    <datalist id="skills" >
			    
			    	<select>
					    <c:forEach  var="user" items="${userList}">
									
					    <option value="${user.firstName} ${user.lastName}">${user.userId}</option>
					   
					    </c:forEach>
					    </select>
			</datalist>
							</li>
							<li class="keyPointsWrap clearfix">
								<label>Key Points</label>
								<div class="keyPointsInputWrap">
						 			<jsp:include page="momsummernote.jsp"></jsp:include>
						 		</div>	
							</li>
						</ul>
						 <p class="redMessages">Highlighted fields are mandatory.</p>
						 
						 
						 



				 </form> 
					<!-- If this code is not needed please remove -->
					 <div id="nikhil">
               		 </div>
					<c:forEach var="usermodels" items="${models}" varStatus="theCount">
                    <!-- /.panel -->
                        <div class="panel-body">
	                        <div class="btn-group"> <i class="fa fa-bar-chart-o fa-fw"></i>
						<%-- <a href="#" onclick="AdminCommunicator(${usermodels.companyId});">${usermodels.companyName}</a>  --%>
							</div>
						</div>
	               	</c:forEach>
	               	<!-- If this code is not needed please remove -->
       		</div>
       </div>
       
       
	<script type="text/javascript">
	$("#dateselect").hide();
	$("#attendeeselect").hide();
	$("#subjectselect").hide();
	$("#messageselect").hide();
	$("#success").hide();
	$("#emailSend").hide();
	
	
</script>


<script>

function userIdForMom(){
	
	var xx=$('.flexdatalist').val();
	

	dummyuserId.length=0;
	
		dummyuserName=xx.split(",");
		
		for(var i=0;i<userName.length;i++)
			{
				
				var name=userName[i];
				
				for(var j=0;j<dummyuserName.length;j++)
					{
						
						if(name==dummyuserName[j])
							{
							
							dummyuserId.push(userId[i])	
							}
					}
				
			}
		

}
/* 
$(document).on('change', 'input', function(){
    var options = $('skills').options;
    var val= $('#skills option:selected').val();

        alert(val);
}); */

</script>

<!-- <script src="http://code.jquery.com/jquery-1.12.1.min.js"></script> -->
<script src="${pageContext.request.contextPath}/resources/mom/jquery.flexdatalist.js"></script>


</body>
</html> 












