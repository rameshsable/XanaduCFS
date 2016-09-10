
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>



<div class="querycomMessage"></div>
 	  <button type="button" class="btn btn-primary btn-block  " onclick="saveQuery();">Submit</button>

<script>


$('.querycomMessage').summernote(
		{
			disableDragAndDrop:false,
			height : 300,
			codemirror : {
				theme : 'monokai'
			},
			toolbar : [ 
			           
					//['picture', ['picture']]
						],
						focus : true
					});

try{		
	liveChatQueryCom();
}catch(excp){
	console.log("Live Chat "+excp);
}

</script>


<script>
 $(document).ready(function() {
  $('.summernote').summernote();
}); 
</script>