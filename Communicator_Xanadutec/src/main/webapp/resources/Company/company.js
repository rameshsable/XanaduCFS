function saveCompany(){
	
	
	var name=$("#companyname").val();
	var contact=$("#contact").val();
	var email=$("#email").val();
	var address=$("#address").val();
	var flag=0;
	/*$("#dataTable1 tr td").each(function() {
		var texto = $(this).text();
	  if(texto==name){
		  $("#samename").slideDown();
			$("#samename").fadeOut(5000);
		  $("#companyname").focus();
		  flag=1;
		  return false;
	  }
	});*/
	
	if(name==""){
		$("#name1").slideDown();
		$("#name1").fadeOut(5000);
		$("#companyname").focus();
		 flag=1;
		return false;	
	}
	if(contact==""){
		$("#contact1").slideDown();
		$("#contact1").fadeOut(5000);
		$("#contact").focus();
		 flag=1;
		return false;
	
		}
	if(email==""){
		$("#email1").slideDown();
		$("#email1").fadeOut(5000);
		$("#email").focus();
		 flag=1;
		return false;
	}
	if(address==""){
		$("#address1").slideDown();
		$("#address1").fadeOut(5000);
		$("#address").focus();
		 flag=1;
		return false;
	}

	var emailRegex = /^[A-Za-z0-9._]*\@[A-Za-z]*\.[A-Za-z]{2,5}$/;
	 var emailval = $("#emailval").val();
	 var fname = $("#fname").val();
	 var lname = $("#lname").val();
	 var uname = $("#uname").val();
	 var password = $("#password").val();
	 var cpassword = $("#cpassword").val();
	 var mno = $("#mno").val();
	 var cname = $("#cname").val();
	 var filter = /^[0-9-+]+$/;

   if($('#uname').val() == ""){
		 
			   $("#unameselect").slideDown();
			   $("#unameselect").fadeOut(3000);
		    return false;
		  }
   
   
	if($("#password").val() == ""){
		
		    $("#passwordselect").slideDown();
			 $("#passwordselect").fadeOut(3000);
		    return false;
		  }
	
	 if(($("#password").val())!= ($("#cpassword").val())){
		  
		    $("#cpasswordselect").slideDown();
			 $("#cpasswordselect").fadeOut(3000);
		    return false;
		  }

		   if($("#emailval").val() == "" ){
			  
			  
			    $("#emailselect").slideDown();
				 $("#emailselect").fadeOut(3000);
			    return false;
			  }
		  /* if(!emailRegex.test(email)){
			   
			    $("#emailselect").slideDown();
				 $("#emailselect").fadeOut(3000);
			    return false;
			  }*/
		   if(!emailRegex.test(emailval)){
			   
			    $("#emailselect").slideDown();
				 $("#emailselect").fadeOut(3000);
			    return false;
			  }
		  
		   if($('#cname').val() == "0"){
		   
		    $("#cnameselect").slideDown();
			   $("#cnameselect").fadeOut(3000);
		    return false;
		  
		   }
	
	if( flag==0){
		
		var formData = $("#companyForm").serialize();
		$.ajax({
			url : "saveCompany",
			type : "POST",
			data :formData,
			success : function(data) {
				$("#successalt").slideDown();
				$("#successalt").fadeOut(5000);
				$("#companyname").val("");
				$("#contact").val("");
				$("#email").val("");
				$("#address").val("");
				
				location.reload(true);
			},
			error : function(xhr, ajaxOptions, thrownError) {
				console.log("Error occured");
			}
		});
	}	
}


function editCompany(id,name,contact,email,address,comid){
	
	//firstName  lastName  userName pass  email mob  companyId
	
	
	$("#save").prop('disabled',true);
	$("#update").prop('disabled',false);
	$("#companyname").val(name);
	$("#contact").val(contact);
	$("#email").val(email);
	$("#address").val(address);
	$("#id").val(id);
	$("#comId").val(comid);
	
	
	
	for(var i=0;i<userName.length;i++)
		{
				if(comid == companyId[i])
					{
					$("#fname").val(firstName[i]);
					$("#lname").val(lastName[i]);
					$("#uname").val(userName[i]);
					$("#emailval").val(Email[i]);
					$("#mno").val(mob[i]);
					$("#password").val(pass[i]);
					$("#cpassword").val(pass[i]);
					$("#userid").val(userid[i]);
							
					}
		
		}
	
	
	
	
}

function updateCompany(){
	
	var name=$("#companyname").val();
	var contact=$("#contact").val();
	var email=$("#email").val();
	var address=$("#address").val();
	var id=$("#id").val();
	var fname=$("#fname").val();
	var lname=$("#lname").val();
	var uname=$("#uname").val();
	var Email=$("#emailval").val();
	var mob=$("#mno").val();
	var password=$("#password").val();
	var userid =$("#userid").val();

	
	
	var flag=0;
		
	if(name==""){
		$("#name1").slideDown();
		$("#name1").fadeOut(5000);
		$("#companyname").focus();
		 flag=1;
		return false;	
	}
	if(contact==""){
		$("#contact1").slideDown();
		$("#contact1").fadeOut(5000);
		$("#contact").focus();
		return false;
		 flag=1;
		}
	if(email==""){
		$("#email1").slideDown();
		$("#email1").fadeOut(5000);
		$("#email").focus();
		 flag=1;
		return false;
	}
	if(address==""){
		$("#address1").slideDown();
		$("#address1").fadeOut(5000);
		$("#address").focus();
		 flag=1;
		return false;
	}
	
	 if(($("#password").val())!= ($("#cpassword").val())){
		  
		    $("#cpasswordselect").slideDown();
			 $("#cpasswordselect").fadeOut(3000);
			 flag=1;
		    return false;
		  }

	if( flag==0){
		$.ajax({
			url : "updateCompany",
			type : "POST",
			data :"id="+id +"&name="+name+"&contact="+contact+"&email="+email+"&address="+address+"&fname="+
			fname+"&lname="+lname+"&uname="+uname+"&Email="+Email+"&mno="+mob+"&password="+password+"&userid="+userid,
			success : function(data) {
				$("#updatealt").slideDown();
				$("#updatealt").fadeOut(5000);
				$("#companyname").val("");
				$("#contact").val("");
				$("#email").val("");
				$("#address").val("");
				
				location.reload(true);
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert("err" + thrownError + xhr.responseText);
			}
		});
	}
	 
	
}

	

