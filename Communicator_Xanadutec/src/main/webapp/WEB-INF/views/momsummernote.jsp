
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/jquery/css/jquery-ui.min.css">
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"> --%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/summernote/summernote.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/summernote/font-awesome.min.css">                   
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/summernote/codemirror.min.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/summernote/monokai.min.css" />



<script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>


<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/summernote/codemirror.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/summernote/xml.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/summernote/formatting.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/summernote/summernote.min.js"></script>


<div class="saveMom"></div>
<div class="text-center submitMomBtnWrap">
	  <button type="button" class="btn btn-primary" onclick="sendEmail();">Send Email</button>
 	  <button type="button" class="btn btn-primary" onclick="saveMom();">Submit</button>
 	  </div>

<script>


$('.saveMom').summernote(
		{
			height : 300,
			codemirror : {
				theme : 'monokai'
			},
			toolbar : [
			],
			focus : true
		});




</script>


