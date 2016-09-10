<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script
	src="${pageContext.request.contextPath}/resources/dropbox/dropbox.js"
	charset='utf-8'></script>
<jsp:include page="body.jsp"></jsp:include>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/confirmBox/jquery-confirm.css"
	media="screen">
<script
	src="${pageContext.request.contextPath}/resources/confirmBox/jquery-confirm.js"
	type="text/javascript"></script>

     <script src="${pageContext.request.contextPath}/resources/MOMJS/jquery-1.10.2.js"></script>
      <script src="${pageContext.request.contextPath}/resources/MOMJS/jquery-ui.js"></script>
      
      
      
   <link href="${pageContext.request.contextPath}/resources/MOMJS/jquerysctipttop.css" rel="stylesheet" type="text/css">
<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head>

<script>
	var files = [];
	function deleteDropBox(filename,abc){ 
		
		
		//var panelText=$( "div#panel-heading"+abc ).text();
		
	    $.confirm({
	        content: 'Are You Sure Want To Delete File',
	        theme: 'white',
	        animationSpeed: 2000 ,
	        confirmButtonClass: 'btn-info',
	        cancelButtonClass: 'btn-danger',
	            
	        onOpen: function(){
	         
	            
	            this.$content.find('button.examplebutton').click(function(){
	               
	            });
	        },
	        onClose: function(){
	           
	        },
	        onAction: function(action){
	          
	        	
	        	if('confirm'==action)
	        		{
	        		
	        		$.ajax({
	        			url : "reportDelete",
	        			type : "GET",
	        			data : {
	        				path : "" + filename
	        			},
	        			success : function(data) {
	        				
	        			 $("#deleteSuccess").slideDown();
	       			     $("#deleteSuccess").fadeOut(3000);
	       			  	 $("#reportPanel-heading"+abc).hide();
	       			  
	        					
	        			},
	        			error : function(xhr, ajaxOptions, thrownError) {
	        				
	        			}
	        		});
	        				
	        		}
	        	
	        }
	    });
	
		
	/* 	var p = abc.parentNode.parentNode.rowIndex;
		document.getElementById("dataTable1").deleteRow(p); */
			
	}
</script>

<body>
	<jsp:include page="menu.jsp"></jsp:include>
	<div id="home">
		<div class="container">
			<div class="adminContentWrap clearfix">
				<div class="col-md-9">
					<div class="alert alert-success alert-dismissable"
						id="successselect">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Success: </strong>File Uploaded successFully.
					</div>

					<div class="alert alert-danger alert-dismissable" id="failselect">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Warning: </strong>please Select File .
					</div>

					<div class="alert alert-success alert-dismissable"
						id="deleteSuccess">
						<button type="button" class="close" data-dismiss="alert"
							aria-hidden="true">&times;</button>
						<strong>Success: </strong>File Deleted SuccessFully.
					</div>

					<div class="reportDocTabs feedbackTabs"
						id="student_specific_details_tab">
						<ul class="pagination">
							<li class="active" onclick="switchView('DropBox', this);"><a
								href="#">Report</a></li>
							<li onclick="switchView('Documentation', this);"><a href="#">Documentation</a></li>
						</ul>
						<div class="tabsContentWrapper dropboxContentWrapper clearfix"
							id="DropBox">

							<div id="dropBoxFirst">
								<div id="dataTable1">

									<c:forEach var="map" items="${mapCountOfDistinctReportByDate}"
										varStatus="theCount">
										<ul class="list-unstyled reportListingWrap clearfix">
											<li>
												<div class="panel-heading clearfix"
													id="panel-heading${theCount.count}">
													<div class="folderTypeWrap pull-left">
														<i class="fa fa-folder fa-3x"
															id="reportCloseIcon${theCount.count}"
															onclick="show(${theCount.count});"></i> <input
															type="hidden" value="0" id="report${theCount.count}">
														<i class="fa fa-folder-open  fa-3x"
															id="reportOpenIcon${theCount.count}"
															onclick="show(${theCount.count});"></i>
													</div>
													<div class="reportDateFilesWrap clearfix pull-left">
														<fmt:formatDate type="date" value="${map.key}" />
														<div class="reportFileDetails">
															<ul class="list-unstyled">
																<div class="reportHeading report${theCount.count}">
																	<c:forEach var="innermap" items="${map.value}">
																		<c:forEach var="innermapList"
																			items="${innermap.value}">
																			<div class="panel-heading"
																				id="reportPanel-heading${theCount.count}">
																				<c:set var="msg" value="${innermapList.name}" />
																				<c:set var="length" value="	${fn:length(msg)}" />
																				<a
																					href="<c:url value="reportDownload"><c:param name="path" value="${innermapList.name}"/> </c:url>">
																					${fn:substring(msg, 13,fn:length(msg))} <i
																					class="fa fa-download "></i>
																				</a>
																				${innermapList.userfeedback.firstName}&nbsp;${innermapList.userfeedback.lastName}

																			</div>
																		</c:forEach>
																	</c:forEach>
																</div>
															</ul>
														</div>
													</div>
													<div class="reportFileCount pull-right">
														<c:forEach var="innermap" items="${map.value}">
														 ${innermap.key} Files 
														<i class="fa fa-download fa-1x"></i>
														</c:forEach>
													</div>
												</div>
											</li>
										</ul>
										<script type="text/javascript">
											$(".report${theCount.count}").hide();
											$("#reportOpenIcon${theCount.count}").hide();
										</script>
									</c:forEach>
								</div>
							</div>
						</div>
						<div class="tabsContentWrapper documentContentWrap clearfix"
							id="Documentation">
							<div id="documetntFirst">
								<div class="table-responsive">
									<div id="dataTable1">
										<c:forEach var="map"
											items="${mapCountOfDistinctDocumentListByDate}"
											varStatus="theCount">
											<ul class="list-unstyled reportListingWrap clearfix">
												<li>
													<div class="panel-heading clearfix"
														id="panel-heading${theCount.count}">
														<div class="folderTypeWrap pull-left">
															<i class="fa fa-folder fa-3x"
																id="documentCloseIcon${theCount.count}"
																onclick="showDocument(${theCount.count});"></i> <input
																type="hidden" value="0" id="document${theCount.count}" />
															<i class="fa fa-folder-open  fa-3x"
																id="documentOpenIcon${theCount.count}"
																onclick="showDocument(${theCount.count});"></i>
														</div>

														<div class="reportDateFilesWrap clearfix pull-left">
															<fmt:formatDate type="date" value="${map.key}" />
															<div class="reportFileDetails">
																<ul class="list-unstyled">
																	<div class="reportHeading document${theCount.count}">
																		<c:forEach var="innermap" items="${map.value}">
																			<c:forEach var="innermapList"
																				items="${innermap.value}">
																				<div class="panel-heading"
																					id="reportPanel-heading${theCount.count}">
																					<c:set var="msg" value="${innermapList.name}" />
																					<c:set var="length" value="	${fn:length(msg)}" />
																					<a
																						href="<c:url value="reportDownload"><c:param name="path" value="${innermapList.name}"/> </c:url>">
																						${fn:substring(msg, 13,fn:length(msg))} <i
																						class="fa fa-download "></i>
																					</a>
																					${innermapList.userfeedback.firstName}&nbsp;${innermapList.userfeedback.lastName}

																				</div>
																			</c:forEach>
																		</c:forEach>
																	</div>
																</ul>
															</div>
														</div>
														<div class="reportFileCount pull-right">
															<c:forEach var="innermap" items="${map.value}">
													    ${innermap.key} Files
															<i class="fa fa-download fa-1x"></i>
															</c:forEach>
														</div>
													</div>

												</li>
											</ul>
											<script type="text/javascript">
											$(".document${theCount.count}").hide();
											$("#documentOpenIcon${theCount.count}").hide();
										
								</script>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-3" id="documentDiv">
					<div id="documentSecond" class="uploadNewReportForm">
						<h4>Upload Document</h4>
						<form>
							<div class="form-group">
								<input type="file" name="loader11" id="loader11"
									multiple="muliple" /> <input type="button"
									class="btn btn-primary btn-block " value="submit"
									onclick="  uploadDocumentFile() " />
							</div>
						</form>
					</div>
				</div>

				<div class="col-md-3" id="reportDiv">
					<div id="documentSecond" class="uploadNewReportForm">
						<h4>Upload Report</h4>
						<form>
							<div class="form-group">
								<input type="file" name="loader1" id="loader1"
									multiple="muliple" /> <input type="button"
									class="btn btn-primary btn-block " value="submit"
									onclick="  uploadFile() " />


							</div>

							<%-- <div>	<label for="loader1"><img src="${pageContext.request.contextPath}/resources/images/desktop.png"></label></div> --%>
						</form>
						
					
					</div>
				</div>

				<!-- custom  search  for dropbox  -->
				<!--  model  -->
				<div id="light" class="white_content" style="position: relative;left: 500px; float:left;  width:5%; height: ;  padding:0px;   margin:0px; top : 30%; left: 50%;    overflow:hidden">
		
					<input type="hidden" id="yearValue" >
					
					<div class="meetingContent">
					
						
						<img class="logo"
							src="${pageContext.request.contextPath}/resources/images/preloader.gif" style="width: 90px;">
							
	
					</div>
					File Uploading
				</div>
				<div id="fade" class="black_overlay"></div>



			</div>
			<button data-toggle="collapse" data-target="#demo"
				class="btn btn-primary btn-block" style="float: right;">Search
				Report And Document</button>
			<div id="demo" class="collapse">
				<br>
				<div class="adminContentWrap clearfix">
					<input type="checkbox" name="CheckReport" class="chk">
					Report &nbsp;&nbsp;
					 <input type="checkbox" name="CheckDocument"
						class="chkDoc"> Document&nbsp;&nbsp; <select
						id="searchCriteria" onchange="searchForReportAndDoc(this.value);">
						<option value="1">ByDate</option>
						<option value="2">ByName</option>
					</select>
					 <input type="text" name="datepicker" id="datepicker"
						readonly="readonly" />
						<input type="text" name="datepickerTo" id="datepickerTo"
						readonly="readonly" />
						 <input type="text" name="filename"
						id="filename" value="File Name" /> &nbsp;&nbsp;
					<button class="btn btn-primary " onclick="searchReportAndDoc()">Search
					</button>
					
					
					<table id="reportData">
					
						<tbody>
						
				</tbody>
				</table>
				</div>
				
				
				
			</div>
		</div>
	</div>

	</div>

	<script>
        $('[type="file"]').ezdz({
            text: 'drop a picture',
            validators: {
                maxWidth:  600,
                maxHeight: 400
            },
            reject: function(file, errors) {
                if (errors.mimeType) {
                    alert(file.name + ' must be an image.');
                }

                if (errors.maxWidth) {
                    alert(file.name + ' must be width:600px max.');
                }

                if (errors.maxHeight) {
                    alert(file.name + ' must be height:400px max.');
                }
            }
        });
    </script>

	<script>
    $("#filename").hide();

    function searchForReportAndDoc(value){
    	if(value=="2"){
    		$("#filename").show();
    		$("#datepicker").hide();
    		$("#datepickerTo").hide();
    		
    	}
    	if(value=="1"){
    		$("#datepicker").show();
    		$("#datepickerTo").show();
    		$("#filename").hide();
    	}
    }
    
    function searchReportAndDoc(){
    	var datepkr =$("#datepicker").val();
    	var datepkrTo=$("#datepickerTo").val();
    	var filen=$("#filename").val();
    	var cri=$("#searchCriteria").val();
    	
    	var flag=0;
	    	var type=new Array();
	    	$(".chk:checked").each(function() {
	    					type.push($(this).val());
	    					flag=1;
	    	});
	    	
	    	var typeDoc=new Array();
	    	$(".chkDoc:checked").each(function() {
	    		typeDoc.push($(this).val());
	    		flag=1;
			});
	    	if(cri=="2"){
	    		if(filen==""||filen=="File Name"){
	    			alert("Empty file");
	    			return;
	    		}
	    	}
	    	if(flag==0){
	    		alert("please select type flag");
	    	}
	    	else{
	    		
	    		$.ajax({
        			url : "SearchReport",
        			type : "POST",
        			data : "date=" + datepkr+"&dateTo="
					+ datepkrTo +"&FileName="
					+ filen +"&Criteria="+ cri+"&report="+ type+"&Document="+ typeDoc, 
        			cache : false,
					dataType : "json",
        			success : function(data) {
        				var jsonData = JSON.parse(JSON.stringify(data));
    					var info = JSON.parse(JSON.stringify(data));
        				var infoJSON = info.report;
						var xx = JSON.parse(infoJSON);
						var tds="<table> <tbody>";
						
						
						$("#reportData > tbody").html("");
						for (key1 in xx) {
							
							var file1 = xx[key1].name
							var len = 13;
							var len2 = file1.length;
							var file2 = file1.substr(len, len2);
							
							var date = new Date(xx[key1].date);
							var c = date.getDate();
							month = date.getMonth() + 1; // current
							year = date.getFullYear();
							var hours = date.getHours() > 12 ? date
									.getHours() - 12 : date
									.getHours();
							var am_pm = date.getHours() >= 12 ? "PM"
									: "AM";
							hours = hours < 10 ? "0" + hours
									: hours;
							var minutes = date.getMinutes() < 10 ? "0"
									+ date.getMinutes()
									: date.getMinutes();
							var seconds = date.getSeconds() < 10 ? "0"
									+ date.getSeconds()
									: date.getSeconds();
							time = hours + ":" + minutes + " "
									+ am_pm;
							time = c + "/" + month + "/" + year;
							
							tds += "<tr> <td>"+time+"</td><td>&nbsp;&nbsp;</td><td>"
							+ "<div><a href=demo"
							+ "?"
							+ "path="
							+ xx[key1].name.replace(/ /g, '+')
							+ ">"
							+ "<strong ><font size='1'> "
							+ file2
							+"<strong ></font><font color='red'>  <i class='fa fa-download'></i></font> </a>"
							+ " </div> </td> "
							+"</td> <td>&nbsp;&nbsp;&nbsp;&nbsp; Report</td></tr>";
							
						}
						
						
						var infoJSON = info.document;
						var xx = JSON.parse(infoJSON);
						
						for (key1 in xx) {
							/* alert(key1);
							alert( xx[key1].name); */

							
							var file1 = xx[key1].name
							var len = 13;
							var len2 = file1.length;
							var file2 = file1.substr(len, len2);
							
							var date = new Date(xx[key1].date);
							var c = date.getDate();
							month = date.getMonth() + 1; // current
							year = date.getFullYear();
							var hours = date.getHours() > 12 ? date
									.getHours() - 12 : date
									.getHours();
							var am_pm = date.getHours() >= 12 ? "PM"
									: "AM";
							hours = hours < 10 ? "0" + hours
									: hours;
							var minutes = date.getMinutes() < 10 ? "0"
									+ date.getMinutes()
									: date.getMinutes();
							var seconds = date.getSeconds() < 10 ? "0"
									+ date.getSeconds()
									: date.getSeconds();
							time = hours + ":" + minutes + " "
									+ am_pm;
							time = c + "/" + month + "/" + year;
							
							tds += "<tr> <td width='20'>"+time+"</td><td>&nbsp;&nbsp;</td><td>"
							+ "<div><a href=demo"
							+ "?"
							+ "path="
							+ xx[key1].name.replace(/ /g, '+')
							+ ">"
							+ "<strong ><font size='1'> "
							+ file2
							+"<strong ></font><font color='red'>  <i class='fa fa-download'></i></font> </a>"
							+ " </div> </td> "
							+"</td><td>&nbsp;&nbsp;&nbsp;&nbsp; Document</td> </tr>";
							
						}
						
						$("#reportData tbody").append(tds);
        			},
        			error : function(xhr, ajaxOptions, thrownError) {
        				
        			}
        		});
	    	}
	    	
	    			
   }
    </script>

	<script type="text/javascript">
		$("#successselect").hide();
		$("#failselect").hide();
		$("#deleteSuccess").hide();
	</script>
	<script type="text/javascript">
		$('#dataDropBox').dataTable({

			"sPaginationType" : "full_numbers",
			"sDom" : 'T<"clear">lfrtip<"clear spacer">T'

		});
	</script>
	<script>
	
	function show(valId){
		
		$(".report"+valId).slideToggle();
		if ($("#report"+valId).val()=='0'){	
			
			
			
			             
			$("#reportCloseIcon"+valId).hide();
			$("#reportOpenIcon"+valId).show();
			$("#report"+valId).val('1');
		}else{
		


			$("#reportCloseIcon"+valId).show();
			$("#reportOpenIcon"+valId).hide();
			$("#report"+valId).val('0');
		}
	}
	function showDocument(valId){
			
			$(".document"+valId).slideToggle();
			if ($("#document"+valId).val()=='0'){	
				
				$("#documentCloseIcon"+valId).hide();
				$("#documentOpenIcon"+valId).show();
				$("#document"+valId).val('1');
			}else{
	
				$("#documentCloseIcon"+valId).show();
				$("#documentOpenIcon"+valId).hide();
				$("#document"+valId).val('0');
			}
		}
		function hideAllViews() {
			$("#DropBox").hide();

			$("#Documentation").hide();

		}

		function switchView(currentSubTabView, currentLi) {
			hideAllViews();
			
			
			// show submit of button from according to type
			if(currentSubTabView=="DropBox"){
				
				$.ajax({
	    			url : "SaveOrUpdateUserLastVisitedTimeForReport",
	    			type : "GET",
	    			success : function(data) {
	    			},
	    			error : function(xhr, ajaxOptions, thrownError) {
	    			}
	    		});
				
				$("#documentDiv").hide();
				$("#reportDiv").show();
			}if(currentSubTabView=="Documentation"){
			
				$.ajax({
	    			url : "SaveOrUpdateUserLastVisitedTimeForDocument",
	    			type : "GET",
	    			success : function(data) {
	    			},
	    			error : function(xhr, ajaxOptions, thrownError) {
	    			}
	    		});
				
				$("#reportDiv").hide();
				$("#documentDiv").show();
				
			}

			$("#" + currentSubTabView).show();

			$(".pagination li").removeClass("active");

			$(currentLi).addClass("active");
		}

		function loadDataAfterLoadingPage() {
			switchView('DropBox');

			$(".pagination li:first-child").addClass("active");

			$("#teacher_distribute_product").hide();
			$("#class_distribute_product").hide();

			$("#classNotSelectedAlert").hide();
			$("#sectionNotSelectedAlert").hide();
			$("#studentNotSelectedAlert").hide();
			$("#teacherNotSelectedAlert").hide();
			$("#dateNotSelectedAlert").hide();
		}

		loadDataAfterLoadingPage();
	</script>
	<script> 
  
	$(function(){
		$("#datepicker").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2015:2099'

		}).datepicker('setDate',new Date());
	});
    
	$(function(){
		$("#datepickerTo").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2015:2099'

		}).datepicker('setDate',new Date());
	});

</script>

</body>
</html>

