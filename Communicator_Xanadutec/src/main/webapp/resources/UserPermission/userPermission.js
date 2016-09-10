function loadUserPermission(userId){
	$.ajax({
		url : "getUserPermission",
		type : "POST",
		data :"userId="+userId,
		success : function(data) {
			$("#userPermissionTable").html(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("err" + thrownError + xhr.responseText);
		}
	});
}

function saveUserPermission(){
	userId=$("#cname").val();
	var formData = $("#userpe").serialize();
	formData += "&userId=" + encodeURIComponent(userId);
	$.ajax({
		url : "saveUserPermissiontable",
		type : "POST",
		data :formData,
		success : function(data) {
			if(data=="success")
			{
				$("#successForm").slideDown();
				$("#successForm").fadeOut(3000);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("err" + thrownError + xhr.responseText);
		}
	});
}