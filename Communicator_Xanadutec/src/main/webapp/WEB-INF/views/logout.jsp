

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<html>


<head>

<script type="text/javascript">
    window.history.forward();

    function noBack() {
      window.history.forward();
    }
  </script>
</head>
	<body  onload="noBack();" onpageshow="if (event.persisted) noBack();">

	
	
	<% session.invalidate(); %>
<% RequestDispatcher dispatcher=request.getRequestDispatcher("/login"); 
System.out.print("logout");
dispatcher.forward(request, response);
%>
</body>
</html>