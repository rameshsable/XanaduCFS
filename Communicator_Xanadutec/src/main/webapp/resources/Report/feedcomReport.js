function feedComCriteria(value){
	if(value==0){
		
	}else if(value==1){
		$.ajax({
			url : 'feedcomOpenCriteria',
			type : "POST",
			async:false,
			success:function(data) {
				$("#result").html(data);
			},
			error : function(errorThrown) {
				console.log("Error: " + errorThrown);
			}
		});
	}else if(value==2){
		$.ajax({
			url : 'feedcomCloseCriteria',
			type : "POST",
			async:false,
			success:function(data) {
				$("#result").html(data);
			},
			error : function(errorThrown) {
				console.log("Error: " + errorThrown);
			}
		});
	}else if(value==3){
		$.ajax({
			url : 'feedcomOperatorCriteria',
			type : "POST",
			async:false,
			success:function(data) {
				$("#result").html(data);
			},
			error : function(errorThrown) {
				console.log("Error: " + errorThrown);
			}
		});
	}
}

function showDateWiseReport(){
	
	var date1=	$("#datepicker").val();
	var date2=	$("#datepicker1").val();
	var feedbackType=	$("#feedbackType").val();
	
//	alert(date1 +"" +date2);
	$.ajax({
		url : 'feedcomsOpenShowDateWiseReport',
		type : "POST",
		data : "date1=" + date1 + "&date2=" + date2+"&feedbackType="+feedbackType,
		async:false,
		success:function(data) {
			$("#datewise").html(data);
		},
		error : function(errorThrown) {
			console.log("Error: " + errorThrown);
		}
	});
	
}



function showCloseDateWiseReport(){
	var date1=	$("#datepicker").val();
	var date2=	$("#datepicker1").val();
	var feedbackType=	$("#feedbackType").val();
	
//	alert(date1 +"" +date2);
	$.ajax({
		url : 'feedcomscloseShowDateWiseReport',
		type : "POST",
		data : "date1=" + date1 + "&date2=" + date2+"&feedbackType="+feedbackType,
		async:false,
		success:function(data) {
			$("#datewise").html(data);
		},
		error : function(errorThrown) {
			console.log("Error: " + errorThrown);
		}
	});
}

function showQueryDateWiseReport(){
	var date1=	$("#datepicker").val();
	var date2=	$("#datepicker1").val();
	var feedbackType=	$("#feedbackType").val();
	
//	alert(date1 +"" +date2);
	$.ajax({
		url : 'queryOpenShowDateWiseReport',
		type : "POST",
		data : "date1=" + date1 + "&date2=" + date2+"&feedbackType="+feedbackType,
		async:false,
		success:function(data) {
			$("#datewise").html(data);
		},
		error : function(errorThrown) {
			console.log("Error: " + errorThrown);
		}
	});
}
function showQueryCloseDateWiseReport(){
	var date1=	$("#datepicker").val();
	var date2=	$("#datepicker1").val();
	var feedbackType=	$("#feedbackType").val();
	
//	alert(date1 +"" +date2);
	$.ajax({
		url : 'querycloseShowDateWiseReport',
		type : "POST",
		data : "date1=" + date1 + "&date2=" + date2+"&feedbackType="+feedbackType,
		async:false,
		success:function(data) {
			$("#datewise").html(data);
		},
		error : function(errorThrown) {
			console.log("Error: " + errorThrown);
		}
	});
}

