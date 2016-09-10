
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
    <title>Home</title>

<jsp:include page="body.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/resources/FeedbackCom/Feedback.js"charset='utf-8'>
</script>
 <script src="${pageContext.request.contextPath}/resources/QueryCom/Query.js"charset='utf-8'>
						   </script>
</head>
<body>
   <jsp:include page="menu.jsp"></jsp:include> 
    <div id="home">
    <div class="containerBg">
    	<div class="container">
    		<div class="adminContentWrap">
    			<h3 class="pull-left">List of Companies</h3>
    			<div class="searchWrapper pull-right">
    				<i class="fa fa-search"></i> <input type="text" placeholder="Search"/>
    			</div>
    			<div class="clearfix"></div>
    			<div class="row listCompanyWrap">
    			<c:forEach var="usermodels" items="${models}" varStatus="theCount">
    				<div class="col-md-6">	
    				<a href="<c:url value="adminfeedbackcom">
		             <c:param name="userId" value="${usermodels.companyId}"/> </c:url>">
	    				<div class="companyInfoWrap clearfix">
							<div class="companyLogo">
								<img src="${pageContext.request.contextPath}/resources/images/image-icon.jpg" alt="">
							</div>
							<div class="companyInfo">
								<h4>${usermodels.companyName}</h4>
							</div>
						</div>
						</a>
					</div>
				</c:forEach>
    			</div>
    		</div>
    	</div>
   	</div>
</body>
