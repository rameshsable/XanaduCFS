/**
 * 
 */

function loadEmailSendReceivePermissionsByCompanyId(companyId) {

	$
			.ajax({
				dataType : 'json',
				url : "getListOfUserAccordingToCompanyIdForEmailPermission",
				type : "GET",
				data : "companyId=" + companyId,
				success : function(data) {
					$("#association").show();
					$('#assignEmailPermissionTable td').empty();

					// AssignedPermissionList
					var jsonData = JSON.parse(JSON.stringify(data));
					var counter = jsonData.userList;
					var cntForiterateobj = JSON.parse(counter);
					for (var c = 0; c < cntForiterateobj.length; c++) {
						var x = "<td><div class='col-lg-3 centerDiv center-block'>" +
								"<label><input type='checkbox' class='checkbox' id='"
								+ cntForiterateobj[c].userId
								+ "' name='"
								+ cntForiterateobj[c].userId
								+ "'' />"
								+ cntForiterateobj[c].firstName +" "+cntForiterateobj[c].lastName
								+ "</label></div></td>";
						$("#assignEmailPermissionTable").append(x);
						
					}
					var counter = jsonData.AssignedPermissionList;
					var cntForiterateobj = JSON.parse(counter);
					// check checkbox 
					for (var c = 0; c < cntForiterateobj.length; c++) {

						$('#' + cntForiterateobj[c].userModel.userId).prop(
								'checked', true);
					}

					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert("err" + thrownError + xhr.responseText);
				}
			});
}

function assignEmailPermission() {
	companyId = $("#cname").val();
	var formData = $("#assignEmailPermissionForm").serialize();
	formData += "&companyId=" + encodeURIComponent(companyId);
	$.ajax({
		url : "saveUserListWithCompanyIdForEmailPermission",
		type : "GET",
		data : formData,
		success : function(data) {
			if (data == "success") {
				$("#successForm").slideDown();
				$("#successForm").fadeOut(3000);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("err" + thrownError + xhr.responseText);
		}
	});
}