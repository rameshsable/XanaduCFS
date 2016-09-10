<Meta http-equiv=Pragma content=cache>
         <Meta http-equiv=Cache-Control content=no-cache >
         <Meta http-equiv=Expires content=0>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<body>
	
	 <%
           response.setHeader("Pragma","no-cache");
           response.setHeader("Cache-control","no-cache");
           response.setDateHeader("Expires",0);
 
 
       %>
		<h1 id="banner">Unauthorized Access !!</h1>
	
		<hr />
	
		<c:if test="${not empty error}">
			<div style="color:red">
				Your fake login attempt was bursted, dare again !!<br /> 
				Caused : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
			</div>
		</c:if>
	
		<p class="message">Access denied!</p>
		<a href="/login">Go back to login page</a>
		
		 
	</body>
</html>