
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>


<link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico"/>
<title>HOME</title>

<jsp:include page="body.jsp"></jsp:include>

<script
	src="${pageContext.request.contextPath}/resources/JS_NEW/jquery.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/JS_NEW/highcharts.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/JS_NEW/jquery.dataTables.min.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/FeedbackCom/Feedback.js"
	charset='utf-8'>
	
</script>
<script
	src="${pageContext.request.contextPath}/resources/QueryCom/Query.js"
	charset='utf-8'>
	
</script>
<script
	src="${pageContext.request.contextPath}/resources/toster/jquery.toaster.js"
	charset='utf-8'></script>

<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
</head>

<body>
	<jsp:include page="menu.jsp"></jsp:include>
	<script>
		var feedBackName = new Array();
		var feedBackCount = new Array();
		var queryCount = new Array();
	</script>


	<c:forEach var="prod" items="${FeedBackName}">
		<script>
			feedBackName.push("${prod.feedbackType}");
		</script>
	</c:forEach>

	<c:forEach var="prod" items="${FeedBackCount}">
		<script>
			feedBackCount.push("${prod.count}");
		</script>
	</c:forEach>

	<c:forEach var="prod" items="${QueryCount}">
		<script>
			queryCount.push("${prod.count}");
		</script>
	</c:forEach>
	<div class="clearfix"></div>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="companyInfoSection feedComWrapper">
					<h3 class="underlineRed">Issue Tracker</h3>

					<table class="table table-bordered table-hover table-striped"
						id="dataTable1">
						<thead>
							<tr>
								<th>Sr No <i class="fa fa-sort-amount-asc"></i></th>
								<th>Issue Bucket <i class="fa fa-sort-amount-asc"></i></th>
								<th>Total Issue<i class="fa fa-sort-amount-asc"></i></th>
								<th>Open Issue<i class="fa fa-sort-amount-asc"></i></th>
								<th>Closed Issue<i class="fa fa-sort-amount-asc"></i></th>


								<!--      <th>RedFlag Count</th> -->
							</tr>
						</thead>
						<tbody>
							<c:forEach var="feedcount" items="${FeedBackCount}"
								varStatus="theCount">
								<tr>
									<td>${theCount.count }</td>
									<td><a
										href="<c:url value="feedbackcoms">
                       <c:param name="feedId" value="${feedcount.feedBackTypeTable.id}"/> 
                                          
                       </c:url>">${feedcount.feedBackTypeTable.feedbackType}</a></td>
									<td><a
										href="<c:url value="feedbackcoms">
                       <c:param name="feedId" value="${feedcount.feedBackTypeTable.id}"/>
                        </c:url>">${feedcount.totalCount}</a></td>
									<td>${feedcount.count}</td>
									<td>${feedcount.closeCount}</td>
									<%--  <td>${feedcount.redflagCount}</td> --%>

								</tr>
							</c:forEach>

						</tbody>
					</table>
					<div class="col-lg">
						<div id="container1"
							style="min-width: 100px; height: 250px; margin: 0 auto"></div>
					</div>
				</div>

			</div>
		</div>
	</div>
</body>
</html>


<style>
.underlineRed {
	border-bottom: 0.5px solid red;
	display: inline-block;
}
</style>









<script>
	var arrText = new Array();
	var arrValue = new Array();
	var arrValueclose = new Array();
</script>
<c:forEach var="feedcount" items="${FeedBackCount}" varStatus="theCount">
	<script>
		arrText.push("${feedcount.feedBackTypeTable.feedbackType}");
		var value = "${feedcount.count}";
		arrValue.push(Number(value));

		var value = "${feedcount.closeCount}";
		arrValueclose.push(Number(value));
	</script>
</c:forEach>

<script>
	var arrText1 = new Array();
	var arrValue1 = new Array();
</script>
<c:forEach var="feedcount" items="${QueryCount}" varStatus="theCount">
	<script>
		arrText1.push("${feedcount.feedBackTypeTable.feedbackType}");
		var value1 = "${feedcount.count}";
		arrValue1.push(Number(value1));
	</script>
</c:forEach>




<script>
	$(function() {
		$('#container1').highcharts({
			chart : {
				type : 'column',

				margin : [ 50, 50, 100, 80 ]
			},
			title : {
				text : 'Analyse Flow'
			},
			xAxis : {
				categories : arrText,

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
								//    alert('Category: ' + this.category + ', value: ' + this.y);
							}
						}
					}
				}
			},
			/* 	series : [ {
					name : 'Paid Fee',
					data : arrValue,

					dataLabels : {

						enabled : true,
						rotation : -90,
						color : '#FFFFFF',
						align : 'right',
						x : 0,
						y : 8,
						style : {

							fontSize : '10px',
							fontFamily : 'Verdana, sans-serif',
							textShadow : '0 0 30px black'
						}
					}
				} ] */
			series : [ {
				name : 'OPEN',
				data : arrValue,
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
				data : arrValueclose,
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
	});
</script>



<script type="text/javascript">
	$('#dataTable1').dataTable({

		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

	});
	$('#dataTable2').dataTable({

		"sPaginationType" : "full_numbers",
		"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

	});
</script>
<script>
	$(function() {
		$('#container3')
				.highcharts(
						{
							title : {
								text : 'Monthly Average Temperature',
								x : -20
							//center
							},
							subtitle : {
								text : 'Source: WorldClimate.com',
								x : -20
							},
							xAxis : {
								categories : [ 'Jan', 'Feb', 'Mar', 'Apr',
										'May', 'Jun', 'Jul', 'Aug', 'Sep',
										'Oct', 'Nov', 'Dec' ]
							},
							yAxis : {
								title : {
									text : 'Temperature (°C)'
								},
								plotLines : [ {
									value : 0,
									width : 1,
									color : '#808080'
								} ]
							},
							tooltip : {
								valueSuffix : '°C'
							},
							legend : {
								layout : 'vertical',
								align : 'right',
								verticalAlign : 'middle',
								borderWidth : 0
							},
							series : [
									{
										name : 'Tokyo',
										data : [ 7.0, 6.9, 9.5, 14.5, 18.2,
												21.5, 25.2, 26.5, 23.3, 18.3,
												13.9, 9.6 ]
									},
									{
										name : 'New York',
										data : [ -0.2, 0.8, 5.7, 11.3, 17.0,
												22.0, 24.8, 24.1, 20.1, 14.1,
												8.6, 2.5 ]
									},
									{
										name : 'Berlin',
										data : [ -0.9, 0.6, 3.5, 8.4, 13.5,
												17.0, 18.6, 17.9, 14.3, 9.0,
												3.9, 1.0 ]
									},
									{
										name : 'London',
										data : [ 3.9, 4.2, 5.7, 8.5, 11.9,
												15.2, 17.0, 16.6, 14.2, 10.3,
												6.6, 4.8 ]
									} ]
						});
	});
</script>
