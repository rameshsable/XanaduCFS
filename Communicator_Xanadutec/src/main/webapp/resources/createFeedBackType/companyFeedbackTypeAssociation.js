function loadCompanyFeedBackTypeAssociation(compId){
	
	 
	$.ajax({
		dataType : 'json',
		url : "getFeedbackCompanyAssociation",
		type : "GET",
		data :"companyId="+compId,
		success : function(data) {
			//$("#userPermissionTable").html(data);
			$("#association").show();

			// remove all selected checkboxes
						 $("#CompanyFeedBackTypeAssociationTable tr td").each(function() {
					     	
						       $(this).find(":checkbox").prop('checked',false);	
						       
						 });
						 
		       // now check only required checkboxes
						 
						 for(j=0;j<data.length;j++){
								$('#'+data[j].feedBackType.id).prop('checked', true);	
							}
			
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("err" + thrownError + xhr.responseText);
		}
	});
}

function CompanyFeedBackTypeAssociation(){

	companyId=$("#companieslst").val();
	
	if(companyId=="0"){
		$("#selectCompany").slideDown();
		$("#selectCompany").fadeOut(3000);
		
		return ;
	}
	var formData = $("#CompanyFeedBackTypeAssociationForm").serialize();
	formData += "&companyId=" + encodeURIComponent(companyId);
	
	$.ajax({
		url : "saveCompanyFeedBackTypeAssociation",
		type : "GET",
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