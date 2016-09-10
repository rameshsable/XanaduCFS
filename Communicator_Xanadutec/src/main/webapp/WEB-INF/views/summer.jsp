
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>




<div class="feedcomMessage"></div>
<button type="button" class="btn btn-primary btn-block  "
	onclick="saveFeedBack();">Submit</button>

<script>
	$('.feedcomMessage').summernote({
		disableDragAndDrop : false,
		height : 250,
		codemirror : {
			theme : 'monokai'
		},
		toolbar : [  
		             
		],
		focus : true
	});

	try {
		liveChat();
	} catch (excp) {
		console.log("Live Chat " + excp);
	}
</script>


