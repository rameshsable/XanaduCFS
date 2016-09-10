<%@page import="com.xanadutec.coreflex.model.UserModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<title>Home</title>

<jsp:include page="body.jsp"></jsp:include>
<%-- <script src="${pageContext.request.contextPath}/resources/FeedbackCom/Feedback.js"charset='utf-8'> --%>
<script
	src="${pageContext.request.contextPath}/resources/momsummer/momFeedback.js"
	charset='utf-8'></script>

</head>
<script>

function searchMOM(){

	var datepkr =$("#datepicker").val();
	var datepkrTo=$("#datepickerTo").val();
	
	$.ajax({
		type : "POST",
		url : "getMomDateWise",
		data : "date=" + datepkr + "&dateTo="+ datepkrTo ,
		cache : false,
		dataType : "json",
		success : function(data) {
			var tds="";
			$("#reportData > tbody ").html("");
			if (data == "") {
				console.log("empty");
			} else {
				console.log(data);
				
				for (i = 0; i < data.length; i++) {
					
				 tds += "<tr> <td>"+Number(i+1)+"</td>"+
					"<td><a href='#' onClick=viewMom("+data[i].id+"); data-toggle='modal' data-target='#myModal'>"+data[i].date+"</a></td>"+
					"<td><a href='#' onClick=viewMom("+data[i].id+"); data-toggle='modal' data-target='#myModal'>"+data[i].attendee+"</a></td>"+
					"<td><a href='#' onClick=viewMom("+data[i].id+"); data-toggle='modal' data-target='#myModal'>"+data[i].userfeedback.firstName+" "+data[i].userfeedback.lastName  +"</a></td>"+
					"<td><a href='#' onClick=viewMom("+data[i].id+"); data-toggle='modal' data-target='#myModal'>"+data[i].subject  +"</a></td>"+
					
					"</tr>";
 
				}
				
				$("#reportData  tbody").append(tds);

			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.log("Error In Live Chat" + thrownError
					+ xhr.responseText);
		}
	}); 
	 
}
</script>

<body>
	<jsp:include page="menu.jsp"></jsp:include>

	<div class="container">
		<div class="adminContentWrap">
			<h3 class="pull-left">List of Meeting Minutes</h3>
			<a class="btn btn-primary btn-small createNewMomBtn"
				href="${pageContext.request.contextPath}/momController">Create
				New</a>
			<table class="table table-bordered table-hover table-striped"
				id="dataTable1" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>Sr No <i class="fa fa-sort-amount-asc"></i></th>
						<th>Date <i class="fa fa-sort-amount-asc"></i></th>
						<th>Attendee <i class="fa fa-sort-amount-asc"></i></th>
						<th>Created By <i class="fa fa-sort-amount-asc"></i></th>
						<th>Subject <i class="fa fa-sort-amount-asc"></i></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="mom" items="${momList}" varStatus="theCount">
						<tr>
							<td><a href="#">${theCount.count }</a></td>
							<td><a href="#" onClick="viewMom('${mom.id}')"
								data-toggle="modal" data-target="#myModal">${mom.date}</a></td>
							<td><a href="#" onClick="viewMom('${mom.id}')"
								data-toggle="modal" data-target="#myModal">${mom.attendee}</a></td>
							<td><a href="#" onClick="viewMom('${mom.id}')"
								data-toggle="modal" data-target="#myModal">${mom.userfeedback.firstName}   ${mom.userfeedback.lastName}  </a></td>
							<td><a href="#" onClick="viewMom('${mom.id}')"
								data-toggle="modal" data-target="#myModal">${mom.subject}</a></td>
							<%-- <td width="70%" onClick="viewMom('${mom.id}')"><a href="#">${mom.keypoints}</a></td> --%>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<!-- Button trigger modal -->
			<!-- <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
				  Launch demo modal
				</button> -->

			<!-- Modal -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="momPopupWrap">
							<div class="popupHeader">
								<img class="logo"
									src="${pageContext.request.contextPath}/resources/images/coreflex-logo.jpg">
								<button type="button" class="close popupClose"
									data-dismiss="modal" aria-label="Close">
									<i class="fa fa-times"></i>
								</button>
								<div class="clearfix"></div>
								<h2 class="text-center" id="MomSubject">Meeting Notes
									Subject</h2>
								<span class="meetingNoteDate text-center" id="MomDate">07.05.2016</span>
								<span class="text-center meetingCreatedBy" id="MomCreatedBy">Created
									By: Name Surname, 08-05-2016</span>
							</div>
							<div class="meetingContent">
								<h4>Attendees</h4>
								<p class="attendeesNames">
									<span id="MomAttendees"> </span>
								</p>

								<div class="momKeyPointsWrap">
									<h4>Key Points -</h4>
									<p>
										<span id="MomKeyPoints"> </span>
									</p>
									<ul class="list-unstyled">
										<li>&nbsp;</li>
										<li>&nbsp;</li>

									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="adminContentWrap"  style="border:0px;"> 
			<button data-toggle="collapse" data-target="#demo"
				class="btn btn-primary btn-block" >Search MOM By Date</button>
			<div id="demo" class="collapse">
				<br>
					From Date <input type="text" name="datepicker" id="datepicker"
						readonly="readonly" /> To Date <input type="text"
						name="datepickerTo" id="datepickerTo" readonly="readonly" />
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary " onclick="searchMOM();">Search
					</button>

					<table id="reportData"  class="table table-bordered table-hover table-striped"
				 cellspacing="0" width="100%">
					<thead>
					<tr>
						<th>Sr No <i class="fa fa-sort-amount-asc"></i></th>
						<th>Date <i class="fa fa-sort-amount-asc"></i></th>
						<th>Attendee <i class="fa fa-sort-amount-asc"></i></th>
						<th>Created By <i class="fa fa-sort-amount-asc"></i></th>
						<th>Subject <i class="fa fa-sort-amount-asc"></i></th>
					</tr>
				</thead>
						<tbody>

						</tbody>
					</table>
				</div>
</div>


</div>


	</div>
	<script>
	 
		function viewMom(id) {

			$.ajax({
				url : '${pageContext.request.contextPath}/getMomById',
				type : "POST",
				dataType : "json",
				data : {
					id : "" + id
				},
				success : function(data) {

					var date = new Date(data.dateTime);
					var c = date.getDate();
					month = date.getMonth() + 1; // current
					year = date.getFullYear();
					var hours = date.getHours() > 12 ? date.getHours() - 12
							: date.getHours();
					var am_pm = date.getHours() >= 12 ? "PM" : "AM";
					hours = hours < 10 ? "0" + hours : hours;
					var minutes = date.getMinutes() < 10 ? "0"
							+ date.getMinutes() : date.getMinutes();
					var seconds = date.getSeconds() < 10 ? "0"
							+ date.getSeconds() : date.getSeconds();
					time = hours + ":" + minutes + " " + am_pm;
					time = c + "/" + month + "/" + year + " " + time;

					$("#MomCreatedBy").text(
							"Created By: " + data.userfeedback.firstName + " "
									+ data.userfeedback.lastName + " " + time);

					date = new Date(data.date);
					var c = date.getDate();
					month = date.getMonth() + 1; // current
					year = date.getFullYear();
					var hours = date.getHours() > 12 ? date.getHours() - 12
							: date.getHours();
					var am_pm = date.getHours() >= 12 ? "PM" : "AM";
					hours = hours < 10 ? "0" + hours : hours;
					var minutes = date.getMinutes() < 10 ? "0"
							+ date.getMinutes() : date.getMinutes();
					var seconds = date.getSeconds() < 10 ? "0"
							+ date.getSeconds() : date.getSeconds();
					time = hours + ":" + minutes + " " + am_pm;
					time = c + "/" + month + "/" + year + " ";
					$("#MomDate").text(time);
					$("#MomSubject").text(data.subject);
					$("#MomAttendees").text(data.attendee);
					/* $("#MomKeyPoints").text("<HTML>"+data.keypoints+"</HTML>"); */
					document.getElementById("MomKeyPoints").innerHTML = "<HTML>"+data.keypoints+"</HTML>";
					/* for ( var i = 0; i < data.length; i++) { 
					  $("#MomAttendees").val(data[i].attendee);
					  alert(data[i].attendee);
					} */

					/* $("#MomAttendees").val(data.attendee); */

					/* document.getElementById('light').style.display = 'block';
					document.getElementById('fade').style.display = 'block';
					
					$("#light").html(data);
					 */
				},
				error : function(errorThrown) {
					alert("Error: " + errorThrown);
				}
			});
		}

		function closePopUp() {

			document.getElementById('light').style.display = 'none';
			document.getElementById('fade').style.display = 'none';

			return false;
		}
		window.onkeyup = function(e) {

			var key = e.keyCode ? e.keyCode : e.which;

			if (key == 27) {
				closePopUp();
			}

		};
	</script>

	</div>

	<!-- Remove this code if not needed -->
	<div id="light" class="white_content"></div>
	<div id="fade" class="black_overlay"></div>
	</div>

</body>
<script type="text/javascript">
	$('#dataTable1').dataTable({

		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

	});
	
	
	
	
	/* $('#dataTable2').dataTable({
		"bJQueryUI" : true,
		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

	}); */
	
	
	
</script>



<script
	src="${pageContext.request.contextPath}/resources/MOMJS/jquery-1.10.2.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/MOMJS/jquery-ui.js"></script>



<link
	href="${pageContext.request.contextPath}/resources/MOMJS/jquerysctipttop.css"
	rel="stylesheet" type="text/css">
<script>
	$(function() {
		$("#datepicker").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2015:2099'

		}).datepicker('setDate', new Date());
	});

	$(function() {
		$("#datepickerTo").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2015:2099'

		}).datepicker('setDate', new Date());
	});
	
	
	
</script>


</html>
