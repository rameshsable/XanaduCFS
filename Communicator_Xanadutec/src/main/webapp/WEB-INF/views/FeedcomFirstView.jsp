<%@page import="org.springframework.util.FileCopyUtils"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.net.URLConnection"%>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<title>Home</title>
<head>

<script>
	var arrTotal = new Array();
	var feedBackFiles = [];
	/* var operators = new Array();
	var operatorsId = new Array(); */
	var redflagList = new Array();
</script>

<c:forEach var="fla" items="${flags}">
	<script>
		redflagList.push("${fla.soNumber}");
	</script>
</c:forEach>

<c:forEach var="dist" items="${DistincYearList}">
	<script>
		var value = "${dist}";
		arrTotal.push(Number(value))
	</script>
</c:forEach>


<script type="text/javascript">
	$(document).ready(function() {

		$('input[type="checkbox"]').click(function() {

			if ($(this).prop("checked") == true) {
				//    alert("Checkbox is checked.");
				$("#redflag").val("Active");
			}

			else if ($(this).prop("checked") == false) {
				//       alert("Checkbox is unchecked.");
				$("#redflag").val("Disactive");
			}

		});

	});
</script>




<script
	src="${pageContext.request.contextPath}/resources/FeedbackCom/Feedback.js"
	charset='utf-8'>
	
</script>
<jsp:include page="body.jsp"></jsp:include>

<script
	src="${pageContext.request.contextPath}/resources/JS_NEW/highcharts.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/JS_NEW/jquery.dataTables.min.js"></script>


</head>


<jsp:include page="menu.jsp"></jsp:include>
<body>


	<!-- // new code start -->
	<div class="container">
		<div class="adminContentWrap clearfix">
			<div class="innerBreadcrumb pull-left clearfix">
				<ul class="list-unstyled clearfix">
					<li><a href="<c:url value="configureHome"> </c:url>"><strong><i>
									Issue Tracker </i> </strong></a> <i class="fa fa-angle-right"></i></a></li>
					<li><a
						href="<c:url value="feedbackcoms">
		                            <c:param name="feedId" value="${feedId}"/> </c:url>">
							${feedBackName} <i class="fa fa-angle-right"></i>
					</a></li>

				</ul>
			</div>
			<c:set var="userfromID" value="${userFrom}"></c:set>
			<div class="clearfix"></div>
			<div class="feedbackCommuListWrap">
				<div class="">
					<table width="100%"
						class="table table-bordered table-hover table-striped"
						id="dataTable1">
						<thead>
							<tr>
								<th>Sr No</th>
								<th>So Num</th>
								<th>Avaya Id</th>
								<th>Doc Id</th>
								<th>Feedback</th>
								<th>Initiated By</th>
								<th>Closed By</th>
								<!-- <th>Flag</th> -->
							</tr>
						</thead>
						<tbody>

							<c:forEach var="feedcount" items="${FeedBackList}"
								varStatus="theCount">
								<c:if test="${not empty  feedcount}">

									<tr>

										<c:set var="msg" value="${feedcount.soNumber}" />
										<c:set var="arrayofmsg" value="${fn:split(msg,'#@')}" />
										<td align="center" width="3%">${theCount.count }</td>
										<td width="5%"><a
											href="<c:url value="feedcomChat">
		                            <c:param name="soNumber" value="${feedcount.soNumber}"/>   <c:param name="feedbackType" value="${feedId}"/>  </c:url>">${arrayofmsg[0]}</a></td>

										<td width="5%"><a
											href="<c:url value="feedcomChat">
		                            <c:param name="soNumber" value="${feedcount.soNumber}"/>   <c:param name="feedbackType" value="${feedId}"/>  </c:url>">${arrayofmsg[1]}</a></td>
										<td width="5%"><a
											href="<c:url value="feedcomChat">
                                                   
		                            <c:param name="soNumber" value="${feedcount.soNumber}"/>   <c:param name="feedbackType" value="${feedId}"/>  </c:url>">${arrayofmsg[2]}</a></td>
										<td width="25%"><a
											href="<c:url value="feedcomChat">
                                                    
		                            <c:param name="soNumber" value="${feedcount.soNumber}"/>   <c:param name="feedbackType" value="${feedId}"/>  </c:url>">
												<div>${feedcount.feedback.feedbackText}</div>
										</a></td>
										<td align="center" width="10%">${feedcount.feedback.userfeedback.firstName}
											- ${feedcount.feedback.userfeedback.lastName} /
											${feedcount.feedback.userfeedback.company.companyName}</td>

										<td align="center" width="3%">${feedcount.feedback_close.userfeedback.firstName}
											- ${feedcount.feedback_close.userfeedback.lastName} /
											${feedcount.feedback_close.userfeedback.company.companyName}</td>

									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>

				</div>
				<input type="hidden" id="userId" name="userId"
					value="${sessionScope.UserMessage.userId}"> <input
					type="hidden" id="companyId" name="companyId"
					value="${sessionScope.UserMessage.company.companyId}">
				<div></div>

				<input type="hidden" id="userMessageForAssinee" name="userMessageForAssinee"
					value="${userMessageForAssinee}">
				

				<!-- // new code end -->


				<script>
					for (j = 0; j < redflagList.length; j++) {
						$("#" + redflagList[j]).show();

					}
				</script>


				<div class="col-lg">
					<div id="container1"
						style="min-width: 100px; height: 250px; margin: 0 auto"></div>
					<!-- <div id="detail" style="min-width: 400px; height: 300px; margin: 0 auto"> -->
				</div>

			</div>


			<!--  
            <div class="row">
            <div class="col-lg-4"> -->

			<div id="NewFeedComChatting">
				<div class="panel panel-default inputConversionWrap">
					<div class="panel-heading">
						<i class="fa  fa-fw"></i><strong><i>Start New Feedcom
								<!-- Start New Issue -->
						</i></strong>
						<div class="pull-right">
							<div class="btn-group">
								<%--  <img src="${pageContext.request.contextPath}/resources/images/query.png" height="50" width="50"></img> --%>
							</div>
						</div>
					</div>
					<div style="margin-top: 20px;"
						class="alert alert-danger alert-dismissable" id="soNumberSel">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Warning: </strong>please Enter So Number.
					</div>
					<div style="margin-top: 20px;"
						class="alert alert-danger alert-dismissable"
						id="feelAtLeastOneField">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Warning: </strong>please Enter At Least One Field.
					</div>


					<div style="margin-top: 20px;"
						class="alert alert-danger alert-dismissable" id="OperatorSel">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Warning: </strong>Please select Operator.
					</div>
					<div style="margin-top: 20px;"
						class="alert alert-danger alert-dismissable" id="chatBoxSel">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Warning: </strong>Please Enter Message.
					</div>
					<div style="margin-top: 20px;"
						class="alert alert-danger alert-dismissable" id="soAvailable">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Warning: </strong>So Number Already Available For This
						FeedBack Type.
					</div>
					<div style="margin-top: 20px;"
						class="alert alert-danger alert-dismissable" id="submsg">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Warning: </strong>Please enter subject .	</div>
					<script type="text/javascript">
						$("#soNumberSel").hide();
						$("#OperatorSel").hide();
						$("#chatBoxSel").hide();
						$("#soAvailable").hide();
						$("#feelAtLeastOneField").hide();
						$("#submsg").hide();
						
					</script>

					<form action="feedcomChat">
						<div class="form-group">
							<div class="col-xs-10">
								<input type="file" name="loader1" id="loader1"
									multiple="muliple" / />
								<!-- <input type="button" id="subbutton" class="form-control" value="Upload" onclick="abffffb()"/> -->
								<br>
							</div>
							<div class="col-xs-12">
								<span class="label label-default">SO Num</span> <input
									type="text" class="form-control" name="soNumber" id="sonumber"
									onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.keyCode === 8 || event.keyCode === 9 '>
							</div>
							<div class="col-xs-12">
								<span class="label label-default">Avaya Num</span> <input
									type="text" class="form-control" name="avayaNumber"
									id="avayaNumber"
									onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.keyCode === 8 || event.keyCode === 9'>
							</div>
							<div class="col-xs-12">
								<span class="label label-default">Doc Id</span> <input
									type="text" class="form-control" name="docid" id="docid"
									onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.keyCode === 8 || event.keyCode === 9 '>

							</div>
							<div class="col-xs-12">
								<span class="label label-default">Subject</span> <input
									type="text" class=" form-control" name="subject" id="subject">
							</div>

							<div class="col-xs-12">
								<%-- <span class="label label-default">Operator</span> <input
																type="text" class=" form-control operatorCodeClass"
																name="operator" id="operator" value="${operator}"
																onChange="operatorfirstName(this.value);"
																onkeydown="operatorfirstName(this.value)"
																autocomplete="on"><br> --%>


								<select class="label label-default" id="operatorId">

									<c:forEach var="operat" items="${operatorlist}">
										<option value=${operat.operatorId}>${operat.userName}</option>
									</c:forEach>
								</select>
							</div>

							<input type="hidden" class="form-control" name="operatorId"
								id="operatorId"> <input type="hidden"
								class="form-control" name="feedbackType" id="feedbackType"
								value="${feedBackTypeId}"> <input type="hidden"
								class="form-control" name="feedBackName" id="feedBackName"
								value="${feedBackName}"> <input type="hidden"
								class="form-control" name="redflag" id="redflag">


							<div class="parent" ng-app="myApp">
								<!-- <textarea rows="4" cols="55"  id="chatBox" ng-model="textareaText">
					        </textarea>  -->
							</div>
							<!-- <div class="checkboxThree pull-right" >

				  <input type="checkbox" value="1" id="checkboxThreeInput" name=""  />
				
				  <label for="checkboxThreeInput"></label>
				
				  </div> -->

						</div>


						<div class="feedcomMessage"></div>
						<!-- 	  <button type="submit" class="btn btn-primary btn-block  ">Submit</button>  feedcomChat -->
						<button type="button" class="btn btn-primary btn-block  "
							onclick="saveNewFeedFeedBack();">Submit</button>


						<!-- <button type="submit" class="btn btn-primary center-block" >Submit</button> -->
				</div>

				<c:forEach var="feedcount" items="${QueryCount}"
					varStatus="theCount">
					<script>
						arrText1
								.push("${feedcount.feedBackTypeTable.feedbackType}");
						var value1 = "${feedcount.count}";
						arrValue1.push(Number(value1));
					</script>
				</c:forEach>



			</div>
			<!-- Modal -->
			<!-- open of pop up month wise graph  -->
			<div id="light" class="white_content"
				style="position: relative; right: 40px; left: 40px; min-width: 100px; height: 500px; margin: 0 auto">

				<input type="hidden" id="yearValue">
				<div class="popupHeader">
					<img class="logo"
						src="${pageContext.request.contextPath}/resources/images/coreflex-logo.jpg">
					<button type="button" class="close popupClose"
						onclick="closePopUp();">
						<i class="fa fa-times"></i>
					</button>
				</div>
				<div class="meetingContent">

					<div class="col-lg">
						<input type="button" id="hideDayWiseGraph"
							class="pull-right btn btn-primary " onclick="showMonthGraph();"
							value="BACK">
						<div id="containerMonthWiseGraph"
							style="min-width: 90%; height: 45%; margin: 0 auto"></div>
						<div id="containerDayWiseGraph"
							style="min-width: 90%; height: 45%; margin: 0 auto">

						</div>
						<!-- <div id="detail" style="min-width: 400px; height: 300px; margin: 0 auto"> -->
					</div>

				</div>
			</div>
		</div>
		<div id="fade" class="black_overlay"></div>
		<!-- close of pop up month wise graph  -->


		<div id="light1" class="white_content"
			style="position: relative; left: 500px; float: left; width: 5%; height:; padding: 0px; margin: 0px; top: 30%; left: 50%; overflow: hidden">

			<input type="hidden" id="yearValue">

			<div class="meetingContent">


				<img class="logo"
					src="${pageContext.request.contextPath}/resources/images/preloader.gif"
					style="width: 90px;">


			</div>
			File Uploading
		</div>
		<div id="fade1" class="black_overlay"></div>


	</div>


	<script>
		$('.feedcomMessage').summernote({
			disableDragAndDrop : false,
			height : 250,

			codemirror : {
				theme : 'monokai'
			},
			toolbar : [

			],
			focus : true,
			onImageUpload : function(files, editor, welEditable) {
				sendFile(files[0], editor, welEditable);
			}
		});
	</script>

	</form>

</body>


<script>
	var arrText = new Array();
	var arrValue = new Array();
</script>



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


<script type="text/javascript">
	$("#chatBox").focus();
</script>
<script>
	var arryear = new Array();
	var arrcount = new Array();
</script>
<c:forEach var="feedcount" items="${YearWithTotalIssueCount}"
	varStatus="theCount">
	<script>
		var value = "${feedcount.key}";
		arryear.push(Number(value));

		var value = "${feedcount.value}";
		arrcount.push(Number(value));
	</script>
</c:forEach>

<script>
	$('#container1').highcharts({
		chart : {
			type : 'column',

			margin : [ 50, 50, 100, 80 ]
		},
		title : {
			text : 'Analyse Flow'
		},
		xAxis : {
			categories : arryear,

			title : {
				text : 'Year'
			},
			labels : {
				rotation : -45,
				align : 'right',
				style : {
					fontSize : '10px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		},
		yAxis : {
			min : 0,

			title : {
				text : 'Total Issues'
			}
		},
		legend : {
			enabled : false
		},
		tooltip : {
			pointFormat : 'Total FeedBack: <b>{point.y:.1f}</b>',
		},
		plotOptions : {
			series : {
				color : '#092d3d',
				cursor : 'pointer',
				pointWidth : 15,
				point : {
					events : {
						click : function() {
							//alert('Category: ' + this.category + ', value: ' + this.y);
							renderSecond(this.category);

						}
					}
				}
			}
		},
		series : [ {
			name : 'All',
			data : arrcount,
			color : '#092d3d',
			dataLabels : {

				enabled : false,
				rotation : -90,
				color : '#FFFFFF',
				align : 'right',
				x : 2,
				y : 0,
				style : {

					fontSize : '10px',
					fontFamily : 'Verdana, sans-serif',
					textShadow : '0 0 0px black'
				}
			}

		} ]
	});

	function renderSecond(year) {

		$("#yearValue").val(year);

		$("#hideDayWiseGraph").hide();

		$.ajax({
			dataType : 'json',
			url : "getCountOfMonthAccordingToYear",
			type : "Post",
			data : {
				year : "" + year,
				feedId : "" + "${feedId}"
			},
			success : function(data) {

				var arrmonth = new Array();
				var arropen = new Array();
				var arrclose = new Array();

				var jsonData = JSON.parse(JSON.stringify(data));
				var info = JSON.parse(JSON.stringify(data));

				// month List
				var infoJSON = info.monthName;
				var xx = JSON.parse(infoJSON);
				for (key1 in xx) {
					arrmonth.push(xx[key1]);
				}

				// open List
				var infoJSON = info.openCount;
				var xx = JSON.parse(infoJSON);
				for (key1 in xx) {
					arropen.push(Number(xx[key1]));
				}

				// close List		
				var infoJSON = info.closeCount;
				var xx = JSON.parse(infoJSON);
				for (key1 in xx) {
					arrclose.push(Number(xx[key1]));
				}

				$('#containerMonthWiseGraph').show();
				$('#containerDayWiseGraph').hide();
				showsecondlevelMonthwiseGraph(arrmonth, arropen, arrclose);

			},
			error : function(xhr, ajaxOptions, thrownError) {
				console.log("err" + thrownError + xhr.responseText);
			}
		});

	}

	function showMonthGraph() {
		$('#containerMonthWiseGraph').show();
		$('#containerDayWiseGraph').hide();
		$('#hideDayWiseGraph').hide();

	}

	function renderThird(month) {

		var year = $("#yearValue").val();
		$("#hideDayWiseGraph").show();

		$.ajax({
			dataType : 'json',
			url : "getCountOfDayAccordingToMonthYear",
			type : "Post",
			data : {
				year : "" + year,
				month : "" + month,
				feedId : "" + "${feedId}"
			},
			success : function(data) {

				var arrmonth = new Array();
				var arropen = new Array();
				var arrclose = new Array();

				var jsonData = JSON.parse(JSON.stringify(data));
				var info = JSON.parse(JSON.stringify(data));

				// month List
				var infoJSON = info.day;
				var xx = JSON.parse(infoJSON);
				for (key1 in xx) {
					arrmonth.push(xx[key1]);
				}

				// open List
				var infoJSON = info.openCount;
				var xx = JSON.parse(infoJSON);
				for (key1 in xx) {
					arropen.push(Number(xx[key1]));
				}

				// close List		
				var infoJSON = info.closeCount;
				var xx = JSON.parse(infoJSON);
				for (key1 in xx) {
					arrclose.push(Number(xx[key1]));
				}

				$('#containerMonthWiseGraph').hide();
				$('#containerDayWiseGraph').show();
				showThirdlevelDaywiseGraph(arrmonth, arropen, arrclose);

			},
			error : function(xhr, ajaxOptions, thrownError) {
				console.log("err" + thrownError + xhr.responseText);
			}
		});

	}

	function showThirdlevelDaywiseGraph(month, open, close) {

		$('#containerDayWiseGraph').highcharts({
			chart : {
				type : 'column',

				margin : [ 50, 50, 100, 80 ]
			},
			title : {
				text : 'Daywise Analyse Flow'
			},
			xAxis : {
				categories : month,

				title : {
					text : 'Category'
				},
				labels : {
					rotation : -45,
					align : 'right',
					style : {
						fontSize : '10px',
						fontFamily : 'Verdana, sans-serif'
					}
				}
			},
			yAxis : {
				min : 0,

				title : {
					text : 'ISSUE TRACKER'
				}
			},
			legend : {
				reversed : true
			},
			tooltip : {
				pointFormat : 'ISSUE: <b>{point.y:.1f}</b>',
			},
			plotOptions : {
				series : {
					stacking : 'normal',

					cursor : 'pointer',
					pointWidth : 15,
					point : {
						events : {
							click : function() {

								///renderThird(this.category);
							}
						}
					}
				}
			},
			series : [ {
				name : 'OPEN',
				data : open,
				color : '#092d3d',
				dataLabels : {

					enabled : false,
					rotation : -90,
					color : '#FFFFFF',
					align : 'right',
					x : 2,
					y : 0,
					style : {

						fontSize : '10px',
						fontFamily : 'Verdana, sans-serif',
						textShadow : '0 0 0px black'
					}
				}

			}, {
				name : 'CLOSE',
				data : close,
				color : '#c5161d',
				dataLabels : {

					enabled : false,
					rotation : -90,
					color : '#FFFFFF',
					align : 'right',
					x : 2,
					y : 0,
					style : {

						fontSize : '10px',
						fontFamily : 'Verdana, sans-serif',
						textShadow : '0 0 0px black'
					}
				}
			} ]
		});

		document.getElementById('light').style.display = 'block';
		document.getElementById('fade').style.display = 'block';
	}

	function showsecondlevelMonthwiseGraph(month, open, close) {
		$('#containerMonthWiseGraph').highcharts({
			chart : {
				type : 'column',

				margin : [ 50, 50, 100, 80 ]
			},
			title : {
				text : 'Monthwise Analyse Flow'
			},
			xAxis : {
				categories : month,

				title : {
					text : 'Category'
				},
				labels : {
					rotation : -45,
					align : 'right',
					style : {
						fontSize : '10px',
						fontFamily : 'Verdana, sans-serif'
					}
				}
			},
			yAxis : {
				min : 0,

				title : {
					text : 'ISSUE TRACKER'
				}
			},
			legend : {
				reversed : true
			},
			tooltip : {
				pointFormat : 'ISSUE: <b>{point.y:.1f}</b>',
			},
			plotOptions : {
				series : {
					stacking : 'normal',

					cursor : 'pointer',
					pointWidth : 15,
					point : {
						events : {
							click : function() {

								renderThird(this.category);
							}
						}
					}
				}
			},
			series : [ {
				name : 'OPEN',
				data : open,
				color : '#092d3d',
				dataLabels : {

					enabled : false,
					rotation : -90,
					color : '#FFFFFF',
					align : 'right',
					x : 2,
					y : 0,
					style : {

						fontSize : '10px',
						fontFamily : 'Verdana, sans-serif',
						textShadow : '0 0 0px black'
					}
				}

			}, {
				name : 'CLOSE',
				data : close,
				color : '#c5161d',
				dataLabels : {

					enabled : false,
					rotation : -90,
					color : '#FFFFFF',
					align : 'right',
					x : 2,
					y : 0,
					style : {

						fontSize : '10px',
						fontFamily : 'Verdana, sans-serif',
						textShadow : '0 0 0px black'
					}
				}
			} ]
		});

		document.getElementById('light').style.display = 'block';
		document.getElementById('fade').style.display = 'block';
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



</html>