<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>
<title>Home</title>
<script
	src="${pageContext.request.contextPath}/resources/QueryCom/Query.js"
	charset='utf-8'></script>

<jsp:include page="body.jsp"></jsp:include>
<style>
td {
	height: 50px;
	vertical-align: bottom;
}
</style>


<style type="text/css">
.scrollbar {
	height: 80%;
	overflow: scroll;
	overflow-x: hidden;
	overflow-y: scroll;
}

table {
	border-spacing: 0;
	border-collapse: collapse;
}
</style>
</head>

<body>

	<jsp:include page="menu.jsp"></jsp:include>
	<c:set var="msg" value="${soNumber}" />
	<c:set var="arrayofmsg" value="${fn:split(msg,'#@')}" />
	<!-- // new code start -->
	<div class="container">
		<div class="adminContentWrap clearfix">
			<div class="innerBreadcrumb pull-left clearfix">
				<ul class="list-unstyled clearfix">
					<li><a href="<c:url value="configureHome"> </c:url>"><strong><i>
									QueryCom </i> </strong></a> <i class="fa fa-angle-right"></i></a></li>
					<li><a
						href="<c:url value="querycoms">
		                            <c:param name="feedId" value="${FeedId}"/> </c:url>"><strong>
								${FeedbackType} </strong> </a> <i class="fa fa-angle-right"></i></a></li>
					<li class="active"><a
						href="<c:url value="querycomChat">
		                            <c:param name="soNumber" value="${soNumber}"/>  <c:param name="feedbackType" value="${FeedId}"/></c:url>"><strong>Querycomm
								Communication </strong> </a></li>
				</ul>
			</div>
			<c:set var="userfromID" value="${userFrom}"></c:set>
			<div class="clearfix"></div>
			<div class="feedbackCommuListWrap">
				<div class="aboutDocument">
					<span class="soNumber">SO No. ${arrayofmsg[0]}</span> <span
						class="avavyaID">Avavya ID: ${arrayofmsg[1]}</span> <span
						class="documentID">Document ID: ${arrayofmsg[2]}</span> <span
						class="communicationSubject">Subject: ${subject}</span>
				</div>
				<div class="table-responsive">
					<br>
					<table width="100%" class="" id="tblData">
						<tbody>
							
								<c:forEach var="querycount" items="${queries}"
									varStatus="theCount">
									<tr style="height: 10; width: 10;">
										<td><c:if
												test="${querycount.userModelFrom.userId == userfromID}">
												<div class="operatorMessageWrap clearfix">
													<div class="operatorNameDateWrap">
														<%-- <td ></td> <td  style=" border-style:groove; ; border-color: #ff9d9d #e5e5ff;">${feedcount.feedbackText} <i style="font-size:10; text-decoration:blink; color:Black;  ">${feedcount.userfeedback.userName}</i> &nbsp;<i style="font-size:10; text-decoration:blink; color:Blue;  ">${feedcount.fdate}</i>    </td> --%>

														<div class="col-md-2 col-xs-2 avatar">
															<!--   <img src="http://www.bitrebels.com/wp-content/uploads/2011/02/Original-Facebook-Geek-Profile-Avatar-1.jpg" class=" img-responsive "> -->
														</div>
														<div class="col-md-10 col-xs-10">
															<div class="messages msg_receive"
																style="background: background: rgba(189, 227, 156, 0.34); background: -moz-linear-gradient(left, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); background: -webkit-gradient(left top, right top, color-stop(21%, rgba(189, 227, 156, 0.34)), color-stop(38%, rgba(189, 227, 156, 0.34)), color-stop(56%, rgba(189, 227, 156, 0.53)), color-stop(100%, rgba(234, 246, 223, 1))); background: -webkit-linear-gradient(left, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); background: -o-linear-gradient(left, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); background: -ms-linear-gradient(left, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); background: linear-gradient(to right, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#bde39c', endColorstr='#eaf6df', GradientType=1);">
																<p>${querycount.queryText}</p>
																<time style="color: Gray;" datetime="2009-11-13T20:00">${querycount.userquery.firstName} ${querycount.userquery.lastName}&nbsp;&nbsp;&nbsp;
																	<fmt:formatDate dateStyle="MEDIUM" pattern="dd/MM/yyyy"
																		value="${querycount.getDateofquery()}" />
																	<fmt:formatDate type="time"
																		value="${querycount.getDateofquery()}" />

																</time>
															</div>
														</div>
													</div>
												</div>

												<c:if
													test="${querycount.attachment ne null and not empty querycount.attachment  }">
													<c:set var="attachmentlist1"
														value="${querycount.attachment}" />
													<c:set var="length1"
														value="${fn:length(fn:split(attachmentlist1,'#@$'))}" />
													<c:set var="filename1"
														value="${fn:split(attachmentlist1,'#@$')}" />
													<c:forEach var="querycount1" items="${filename1}"
														varStatus="theCount">
														<div class="">
															<div class="col-md-2 col-xs-2 avatar">
																<!--   <img src="http://www.bitrebels.com/wp-content/uploads/2011/02/Original-Facebook-Geek-Profile-Avatar-1.jpg" class=" img-responsive "> -->
															</div>
															<div class="col-md-10 col-xs-10">
																<div class="messages msg_receive"
																	style="background: background: rgba(189, 227, 156, 0.34); background: -moz-linear-gradient(left, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); background: -webkit-gradient(left top, right top, color-stop(21%, rgba(189, 227, 156, 0.34)), color-stop(38%, rgba(189, 227, 156, 0.34)), color-stop(56%, rgba(189, 227, 156, 0.53)), color-stop(100%, rgba(234, 246, 223, 1))); background: -webkit-linear-gradient(left, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); background: -o-linear-gradient(left, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); background: -ms-linear-gradient(left, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); background: linear-gradient(to right, rgba(189, 227, 156, 0.34) 21%, rgba(189, 227, 156, 0.34) 38%, rgba(189, 227, 156, 0.53) 56%, rgba(234, 246, 223, 1) 100%); filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#bde39c', endColorstr='#eaf6df', GradientType=1);">

																	<c:set var="msg" value="${querycount1}" />

																	<a
																		href="<c:url value="demo">
		                            <c:param name="path" value="${querycount1}"/> </c:url>"><strong>
																			<font size="1"> ${fn:substring(msg, 13,fn:length(msg))}
																		</font><font color="red"> <i class="fa fa-download"></i></font>
																	</strong> </a>


																</div>
															</div>
														</div>
													</c:forEach>
												</c:if>
											</c:if> <c:if
												test="${querycount.userModelFrom.userId != userfromID}">
												<%-- <td style="  border-style:groove; border-color: #ff9d9d #e5e5ff;" >${feedcount.feedbackText}<i style="font-size:10; text-decoration:blink; color:Black;  ">${feedcount.userfeedback.userName}</i>&nbsp;<i style="font-size:10; text-decoration:blink; color:Blue;  ">${feedcount.getFdate()}</i></td ><td></td> --%>
												<div class="operatorMessageWrap clearfix">
													<div class="operatorNameDateWrap">
														<div class="col-md-10 col-xs-10">
															<div class="messages msg_sent"
																style="background: #f2f2f2">
																<p>${querycount.queryText}</p>
																<time style="color: Gray;" datetime="2009-11-13T20:00">${querycount.userquery.firstName} ${querycount.userquery.lastName}
																	&nbsp;&nbsp;&nbsp;
																	<fmt:formatDate dateStyle="MEDIUM" pattern="dd/MM/yyyy"
																		value="${querycount.getDateofquery()}" />
																	<fmt:formatDate type="time"
																		value="${querycount.getDateofquery()}" />

																</time>
															</div>
														</div>
														<div class="col-md-2 col-xs-2 ">
															<!--   <img src="http://www.bitrebels.com/wp-content/uploads/2011/02/Original-Facebook-Geek-Profile-Avatar-1.jpg" class=" img-responsive "> -->
														</div>
													</div>
												</div>
												<c:if
													test="${querycount.attachment ne null and not empty querycount.attachment   }">
													<c:set var="attachmentlist"
														value="${querycount.attachment}" />
													<c:set var="length"
														value="${fn:length(fn:split(attachmentlist,'#@$'))}" />
													<c:set var="filename"
														value="${fn:split(attachmentlist,'#@$')}" />
													<c:forEach var="quecount" items="${filename}"
														varStatus="theCount">

														<div class="col-md-10 col-xs-10">
															<div class="messages msg_sent"
																style="background: #f2f2f2">


																<c:set var="msg" value="${quecount}" />

																<a
																	href="<c:url value="demo">
		                            <c:param name="path" value="${quecount}"/> </c:url>"><strong>
																		<font size="1"> ${fn:substring(msg, 13,fn:length(msg))}
																	</font><font color="red"> <i class="fa fa-download"></i></font>
																</strong> </a>


															</div>
														</div>
													</c:forEach>
												</c:if>
											</c:if></td>
									</tr>
								</c:forEach>
							</div>
						</tbody>
					</table>
				</div>
				<div></div>

				<script type="text/javascript">
					$("#sonumber").val("${soNumber}");
					$("#statusId").val("${status}");
					/* 	$("#operator").val("${operator}"); */
					$(".summernote").focus();
				</script>

			</div>
			<script>
				var files = [];
			</script>
			<!-- </div>
						<div class="row">
							<div class="col-lg-4"> -->
			<div class="panel panel-default inputConversionWrap">
				<div class="panel-heading">

					<i class="fa  fa-fw"></i><strong><i>Input Conversation
							<!-- Start New Issue -->
					</i></strong>
					<div class="pull-right">
						<div class="btn-group">
							<%--  <img src="${pageContext.request.contextPath}/resources/images/query.png" height="50" width="50"></img> --%>
						</div>
					</div>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-lg">
							<form>
								<div class="form-group">

									<div class="col-xs-10">
										<input type="file" name="loader1" id="loader1"
											multiple="muliple" required  >
									</div>
									<!--  <input type="button" id="subbutton" class="form-control" value="Upload" onclick="abffffb()"/> -->


									<c:set var="msg" value="${soNumber}" />
									<c:set var="arrayofmsg" value="${fn:split(msg,'#@')}" />
									<input type="hidden" class="form-control" name="sonumber"
										id="sonumber" value="${arrayofmsg[0]}" readonly="readonly">
									<input type="hidden" class="form-control" name="avayaNumber"
										id="avayaNumber" readonly="readonly" value="${arrayofmsg[1]}">

									<input type="hidden" class="form-control" name="docid"
										id="docid" readonly="readonly" value="${arrayofmsg[2]}">

									<input type="hidden" class=" form-control operatorCodeClass"
										name="subject" id="subject" readonly="readonly"
										value="${subject}"> <br> <input type="hidden"
										id="userId" name="userId"
										value="${sessionScope.UserMessage.userId}"> <input
										type="hidden" id="companyId" name="companyId"
										value="${sessionScope.UserMessage.company.companyId}">

									<input type="hidden" class="form-control" name="operator"
										id="operator" value="${operator}"> <input
										type="hidden" class="form-control" name="userQuery"
										id="userQuery" value="${userfeedbackchat}"> <input
										type="hidden" class="form-control" name="feedbackType"
										id="feedbackType" value="${FeedbackType}"> <input
										type="hidden" class="form-control" name="userFromCounter"
										id="userFromCounter" value="${userFromCounter}"> <input
										type="hidden" class="form-control" name="userToCounter"
										id="userToCounter" value="${userToCounter}"> <input
										type="hidden" class="form-control" name="usermsg" id="usermsg">
									<c:if test="${ShowStatus == 'true'}">
										<select class="label label-default" id="statusId" style='display: none'>
											<option value="1">Open</option>
											<option value="2">Close</option>
										</select>
									</c:if>
									<!-- 	  <button type="button" class="btn btn-primary " onclick="saveFeedBack();">Submit</button> -->
									<br>
									<!--   <textarea rows="4" cols="55" class="form-control" id="chatBox"> -->
									<jsp:include page="summerNoteQueryCom.jsp"></jsp:include>

									<!--    </textarea> -->
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>




<script type="text/javascript">
	$('#dataTable1').dataTable({

		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

	});

	$(document).on('click', '.panel-heading span.icon_minim', function(e) {
		var $this = $(this);
		if (!$this.hasClass('panel-collapsed')) {
			$this.parents('.panel').find('.panel-body').slideUp();
			$this.addClass('panel-collapsed');
			$this.removeClass('glyphicon-minus').addClass('glyphicon-plus');
		} else {
			$this.parents('.panel').find('.panel-body').slideDown();
			$this.removeClass('panel-collapsed');
			$this.removeClass('glyphicon-plus').addClass('glyphicon-minus');
		}
	});
	$(document).on(
			'focus',
			'.panel-footer input.chat_input',
			function(e) {
				var $this = $(this);
				if ($('#minim_chat_window').hasClass('panel-collapsed')) {
					$this.parents('.panel').find('.panel-body').slideDown();
					$('#minim_chat_window').removeClass('panel-collapsed');
					$('#minim_chat_window').removeClass('glyphicon-plus')
							.addClass('glyphicon-minus');
				}
			});
	$(document).on('click', '#new_chat', function(e) {
		var size = $(".chat-window:last-child").css("margin-left");
		size_total = parseInt(size) + 400;
		alert(size_total);
		var clone = $("#chat_window_1").clone().appendTo(".container");
		clone.css("margin-left", size_total);
	});
	$(document).on('click', '.icon_close', function(e) {
		//$(this).parent().parent().parent().parent().remove();
		$("#chat_window_1").remove();
	});
</script>

<style>
.top-bar {
	background: #666;
	color: white;
	padding: 10px;
	position: relative;
	overflow: hidden;
}

.msg_receive {
	padding-left: 0;
	margin-left: 0;
}

.msg_sent {
	padding-bottom: 20px !important;
	margin-right: 0;
}

.messages {
	background: white;
	padding: 10px;
	border-radius: 2px;
	box-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
	max-width: 100%;
}

.messages>p {
	font-size: 13px;
	margin: 0 0 0.2rem 0;
}

.messages>time {
	font-size: 11px;
	color: #ccc;
}

.msg_container {
	padding: 10px;
	overflow: hidden;
	display: flex;
}

.avatar {
	position: relative;
}

.base_receive>.avatar:after {
	content: "";
	position: absolute;
	top: 0;
	right: 0;
	width: 0;
	height: 0;
	border: 5px solid #FFF;
	border-left-color: rgba(0, 0, 0, 0);
	border-bottom-color: rgba(0, 0, 0, 0);
}

.base_sent {
	justify-content: flex-end;
	align-items: flex-end;
}

.base_sent>.avatar:after {
	content: "";
	position: absolute;
	bottom: 0;
	left: 0;
	width: 0;
	height: 0;
	border: 5px solid white;
	border-right-color: transparent;
	border-top-color: transparent;
	box-shadow: 1px 1px 2px rgba(black, 0.2);
	//
	not
	quite
	perfect
	but
	close
}

.msg_sent>time {
	float: right;
}

.msg_container_base::-webkit-scrollbar-track {
	-webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
	background-color: #F5F5F5;
}

.msg_container_base::-webkit-scrollbar {
	width: 12px;
	background-color: #F5F5F5;
}

.msg_container_base::-webkit-scrollbar-thumb {
	-webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, .3);
	background-color: #555;
}

.btn-group.dropup {
	position: fixed;
	left: 0px;
	bottom: 0;
}
</style>




</html>