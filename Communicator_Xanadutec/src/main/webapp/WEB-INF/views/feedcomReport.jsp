
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
    <title>Home</title>

<jsp:include page="body.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/resources/Report/feedcomReport.js"charset='utf-8'>
</script>
</head>
<body>
   <jsp:include page="menu.jsp"></jsp:include> 
    <div id="home">
    <div id="wrapper" >
     <div>&nbsp;</div>
    <div id="page-wrapper" >
      <div id="container-fluid" >
        <div id="row-fluid" >
       
        <div class="span2"><br>
       
        <h1 class="page-header"> feedcom Report</h1>
                
         </div>
       <!--   <form action="#" id="regform" novalidate="novalidate">
                <div class="form-group">
                <div class="col-xs-4">
                					     <select  id="feedCriteria" name="feedCriteria"  class = "label label-default" onchange="feedComCriteria(this.value);">
									     <option value="0">Select Criteria</option>
									     <option value="1">Open</option>
									     <option value="2">Close</option>
									     <option value="3">Operator</option>
									  	 </select>
				</div>
                </div>
				</form>   -->
				
				   <div class="table-responsive">
            	<table class="table table-bordered table-hover table-striped" id="dataTable1">
	                	<thead>
	                    	<tr>
	                        	<th>Sr No</th>
	                            <th>FeedBack</th>
	                            <th>Open</th>
	                            <th>Close </th>
	                            
	                           
	                        </tr>
	                    </thead>
	                    <tbody>
	                    	<c:forEach var="feedcount" items="${FeedBackCount}" varStatus="theCount">
		  						<tr>
		    						<td style="width:2%;">${theCount.count }</td> 
		                            <td style="width: 15%;">${feedcount.feedBackTypeTable.feedbackType}</td>
		                            <td  style="width: 5%;"><a href="<c:url value="feedcomOpenCriteria">
		                            <c:param name="feedId" value="${feedcount.feedBackTypeTable.id}"/> </c:url>"><button type="button" class="btn btn-info center-block">View</button></a></td>
		                            <td  style="width: 5%;"><a href="<c:url value="feedcomCloseCriteria">
		                            <c:param name="feedId" value="${feedcount.feedBackTypeTable.id}"/> </c:url>"><button type="button" class="btn btn-info center-block">View</button></a></td>
		                            
		                        </tr>
	                       </c:forEach>
	                   </tbody>
                </table>
            </div>
				
				<div id="result">
				</div>
	</div>
    </div>
    </div>
        </div>
        </div>
    
    <div id="wrapper">
	       <div id="page-wrapper" >
		        <div id="newhome">
		        </div>
	       </div>
   </div>
       

</body>
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
</html>

       

		



