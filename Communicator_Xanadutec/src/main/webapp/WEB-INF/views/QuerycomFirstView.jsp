<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>
<title>Home</title>


<script
	src="${pageContext.request.contextPath}/resources/QueryCom/Query.js"
	charset='utf-8'></script>

<jsp:include page="body.jsp"></jsp:include>


<script>
	var files = [];
</script>
<body>
	<jsp:include page="menu.jsp"></jsp:include>
	
	<div class="container">
		<div class="adminContentWrap clearfix">
			<div class="innerBreadcrumb pull-left clearfix">
				<ul class="list-unstyled clearfix">
					<li><a href="<c:url value="configureHome"> </c:url>"><strong><i>
									QueryCom </i> </strong></a> <i class="fa fa-angle-right"></i></a></li>
					<li> <a
									href="<c:url value="querycoms">
		                            <c:param name="feedId" value="${feedId}"/> </c:url>">
		                            ${feedBackName} 
									  <i class="fa fa-angle-right"></i></a></li>
					
				</ul>
			</div>
						<div class="clearfix"></div>
				<c:if test="${admin == 'admin'}">
			<div class="feedbackCommuListWrap">
			</c:if>
				<div class="">
											<table class="table table-bordered table-hover table-striped"
												id="dataTable1">
												<thead>
													<tr>
														<th>Sr No</th>
														<th>So Num</th>
														<th>Avaya Id</th>
														<th>Doc Id</th>
														<th>Query</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach var="querycount" items="${QueryList}"
														varStatus="theCount">
														<tr>
															<c:set var="msg" value="${querycount.soNumber}" />
															<c:set var="arrayofmsg" value="${fn:split(msg,'#@')}" />

															<td align="center" width="10%">${theCount.count }</td>
															<td width="25%"><a
																href="<c:url value="querycomChat">
		                            <c:param name="soNumber" value="${querycount.soNumber}"/>   <c:param name="feedbackType" value="${feedId}"/>  </c:url>">${arrayofmsg[0]}</a></td>


															<td width="5%"><a
																href="<c:url value="querycomChat">
		                            <c:param name="soNumber" value="${querycount.soNumber}"/>   <c:param name="feedbackType" value="${feedId}"/>  </c:url>">${arrayofmsg[1]}</a></td>


															<td width="5%"><a
																href="<c:url value="querycomChat">
                                                   
		                            <c:param name="soNumber" value="${querycount.soNumber}"/>   <c:param name="feedbackType" value="${feedId}"/>  </c:url>">${arrayofmsg[2]}</a></td>

															<td><a
																href="<c:url value="querycomChat">
                                          <c:param name="soNumber" value="${querycount.soNumber}"/>   <c:param name="feedbackType" value="${feedId}"/>  </c:url>">${querycount.query.queryText}</a></td>
														</tr>

													</c:forEach>
												</tbody>
											</table>
											</div>
										 <input type="hidden" id="userId" name="userId" value="${sessionScope.UserMessage.userId}"> 
										 <input type="hidden" id="companyId" name="companyId" value="${sessionScope.UserMessage.company.companyId}"> 
									     
										</div>
										<!-- /.table-responsive -->
								
			
				<c:if test="${admin == 'admin'}">
						<div id="NewFeedComChatting">
							<div class="panel panel-default inputConversionWrap">
								<div class="panel-heading">
									  Start New Query 
								</div>
								<div class="alert alert-danger alert-dismissable" id="subjectSel">
									<button type="button" class="close" data-dismiss="alert"
										aria-hidden="true">&times;</button>
									<strong>Warning: </strong>please Enter Subject.
								</div>
								<div class="alert alert-danger alert-dismissable" id="OperatorSel">
									<button type="button" class="close" data-dismiss="alert"
										aria-hidden="true">&times;</button>
									<strong>Warning: </strong>Please select Operator.
								</div>
								<div class="alert alert-danger alert-dismissable" id="chatBoxSel">
									<button type="button" class="close" data-dismiss="alert"
										aria-hidden="true">&times;</button>
									<strong>Warning: </strong>Please Enter Message.
								</div>
								<div class="alert alert-danger alert-dismissable" id="feelAtLeastOneField">
									<button type="button" class="close" data-dismiss="alert"
										aria-hidden="true">&times;</button>
									<strong>Warning: </strong>please Enter At Least One Field.
								</div>
								
								<div class="alert alert-danger alert-dismissable" id="soAvailable">
									<button type="button" class="close" data-dismiss="alert"
										aria-hidden="true">&times;</button>
									<strong>Warning: </strong>So Number Already Available For This
									FeedBack Type.
								</div>
								<script type="text/javascript">
									$("#subjectSel").hide();
									$("#OperatorSel").hide();
									$("#chatBoxSel").hide();
									$("#soAvailable").hide();
									$("#feelAtLeastOneField").hide();
								</script>
								<div class="panel-body ">
											<form action="querycomChat" class="querycomChatForm">
												 
													<div class="form-group uploadFileWrap">
														<input type="file" name="loader1" id="loader1"
															multiple="muliple" / />
													</div>
													<div class="form-group">
														<span class="label label-default">SO Num</span> <input
															type="text" class="form-control" name="soNumber"
															id="sonumber"  onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.keyCode === 8 || event.keyCode === 9 ' >
													</div>
													<div class="form-group">
														<span class="label label-default">Avaya Num</span> <input
															type="text" class="form-control" name="avayaNumber"
															id="avayaNumber"   onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.keyCode === 8 || event.keyCode === 9 '>
													</div>
													<div class="form-group">
														<span class="label label-default">Doc Id</span> <input
															type="text" class="form-control" name="docid" id="docid" onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.keyCode === 8 || event.keyCode === 9 '>

													</div>
													<div class="form-group">
														<span class="label label-default">Subject</span> <input
															type="text" class=" form-control"
															name="subject" id="subject"> <br>
													</div>

													<input type="hidden" class="form-control"
														name="feedbackType" id="feedbackType"
														value="${feedBackTypeId}"> <input type="hidden"
														class="form-control" name="feedBackName" id="feedBackName"
														value="${feedBackName}">
													 

												<div class="feedcomMessage"></div>
												<button type="button" class="btn btn-primary btn-block  "
													onclick="saveNewQuery();">Submit</button>


												<!-- <button type="submit" class="btn btn-primary center-block" >Submit</button> -->


												<script>
													$('.feedcomMessage')
															.summernote(
																	{
																		disableDragAndDrop:false,
																		height : 250,
																		
																		codemirror : {
																			theme : 'monokai'
																		},
																		toolbar : [
																				
																		],
																		focus : true
																	});
												</script>
												<!-- 
				          <button type="submit" class="btn btn-primary center-block" onclick=" ">Submit</button> -->

											</form>
										 
									 
								</div>
							</div>
						</div>
				</c:if>
				</div>
			</div>
		</div>
<script type="text/javascript">
	$("#chatBox").focus();
</script>
<script type="text/javascript">
	$('#dataTable1').dataTable({

		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

	});
</script>
</body>
</html>