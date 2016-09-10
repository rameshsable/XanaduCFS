<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script src="${pageContext.request.contextPath}/resources/dropbox/dropbox.js"charset='utf-8'></script>
<jsp:include page="body.jsp"></jsp:include>

</head>
<script>
  var files=[];
</script>

<script>
 

function deleteDropBox(filename,abc){ 
	
	
		
	$.ajax({
		url : "dropBoxDelete",
		type : "GET",
		data : {
			path : "" + filename
		},
		success : function(data) {
				
		},
		error : function(xhr, ajaxOptions, thrownError) {
			
		}
	});
	
	var p = abc.parentNode.parentNode.rowIndex;
	document.getElementById("dataTable1").deleteRow(p);
		
}







</script>
<body>



<jsp:include page="menu.jsp"></jsp:include>
    <div id="home">
    <div id="wrapper" >
     <div>&nbsp;</div>
    <div id="page-wrapper" >
    			<div class="block users scrollBox table-responsive">
				<div style="margin-top: 20px;"
					class="alert alert-success alert-dismissable"
					id="successselect">
					<button type="button" class="close" data-dismiss="alert"
				   aria-hidden="true">&times;</button>
					<strong>Success: </strong>File Uploaded successFully.
				
				</div>
				
				<div style="margin-top: 20px;"
					class="alert alert-danger alert-dismissable"
					id="failselect">
					<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">&times;</button>
					<strong>Warning: </strong>please Select File .
				</div>
				  </div>
				  <script type="text/javascript">
	               $("#successselect").hide();
	               $("#failselect").hide();
	               
	              </script>

 <div id="container-fluid" >
        <div id="row-fluid" >     
       <br>
        <div class="span2">
              <h1 class="page-header">Document Details </h1>
             	 
             	 <div class="col-lg-7 centerDiv center-block" id="dropBoxFirst"  >
             	 <div class="table-responsive">  <!-- dropBoxlist -->
             								
                                        <table class="table table-bordered table-hover table-striped" id="dataTable1"  >
                                        
                                            	<thead>
                                                <tr>
                                                
                                                 <th> </th>
                                                 <th> </th>
                                                  <th> </th>
                                               </tr>
                                            </thead>
                                            <tbody>
                                         
                                        
                                         
                                         
											<c:forEach var="feedcount" items="${docList}" varStatus="theCount">
												 
                                           
                                            	<c:set var="attachmentlist1" value="${feedcount.name}"/>
												<c:set var="length1" value="${fn:length(fn:split(attachmentlist1,'#@$'))}"/>
												<c:set var="filename1" value="${fn:split(attachmentlist1,'#@$')}"/>
												<c:forEach var="feedcount1" items="${filename1}" varStatus="theCount">
												 <tr >
					  										<td>
					  										<c:set var="msg" value="${feedcount1}"/>
					  										<c:set var="length" value="	${fn:length(msg)}"/>
					  									        ${fn:substring(msg, 13,fn:length(msg))} &nbsp; <sub><font color="red">${feedcount.userfeedback.userName}</font></sub>
					  										</td>        
					  										 <td><a href="<c:url value="documentDownload">
		                            							<c:param name="path" value="${feedcount1}"/> </c:url>"><strong ><input type="button" value="Download" class="btn btn-primary"> </strong> </a>
		                            						</td>
					  										 <td><input type="button" value="Delete" class="btn btn-primary " onclick="deleteDropBox('${feedcount1}',this);">
		                            						</td> 
		                            				   </tr>
		                            	</c:forEach>	
		                            						  									
									 </c:forEach>		
									
										</tbody>
								
										</table>
                                    </div>
             	 </div>
             	 
             	 <div class="col-lg-3 centerDiv center-block" id="dropBoxSecond"  >
             
             </div>
             	 <div class="col-lg-4 centerDiv center-block" id="dropBoxSecond"  ></div>
             	 <div class="col-lg-4 centerDiv center-block" id="dropBoxSecond"  ></div>
             	 <div class="col-lg-4 centerDiv center-block" id="dropBoxSecond"  ></div>
             	 <div class="col-lg-4 centerDiv center-block" id="dropBoxSecond"  ></div>
             	 <div class="col-lg-4 centerDiv center-block" id="dropBoxSecond"  ></div>
             	 <div class="col-lg-4 centerDiv center-block" id="dropBoxSecond"  >
             	 
             	      <form action="querycomChat"  >
							<div class="form-group" >
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
										<div class="col-xs-10"><br></div>
									<div class="col-xs-10"><br></div>
				  			 		<div class="col-xs-10">
								   
								
								    </div>
								   
									</div>
					</form>
            
				</div>			
				
		       		
         </div>
        </div>
    </div>
    </div>
        </div>
        </div>
			<script type="text/javascript">
				 $('#dataDropBox').dataTable({
			
					"sPaginationType" : "full_numbers",
					"sDom" : 'T<"clear">lfrtip<"clear spacer">T'
			
				}); 
				
			</script>

</body>
</html>