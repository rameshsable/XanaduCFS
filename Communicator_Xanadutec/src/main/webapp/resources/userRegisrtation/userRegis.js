function saveUserRegistration() {

	var emailRegex = /^[A-Za-z0-9._]*\@[A-Za-z]*\.[A-Za-z]{2,5}$/;
	var email = $("#email").val();
	var fname = $("#fname").val();
	var lname = $("#lname").val();
	var uname = $("#uname").val();
	var password = $("#password").val();
	var cpassword = $("#cpassword").val();
	var mno = $("#mno").val();
	var cname = $("#cname").val();
	var filter = /^[0-9-+]+$/;

	/*
	 * if($("#fname").val() == "" ){
	 * 
	 * $("#fnameselect").slideDown(); $("#fnameselect").fadeOut(3000); return
	 * false;
	 *  } if($("#lname").val() == "" ){
	 * 
	 * //$("#errorBox").html("enter the Last Name");
	 * $("#lnameselect").slideDown(); $("#lnameselect").fadeOut(3000); return
	 * false; }
	 */
	if ($('#uname').val() == "") {

		$("#unameselect").slideDown();
		$("#unameselect").fadeOut(3000);
		return false;
	}

	if ($("#password").val() == "") {

		$("#passwordselect").slideDown();
		$("#passwordselect").fadeOut(3000);
		return false;
	}

	if (($("#password").val()) != ($("#cpassword").val())) {

		$("#cpasswordselect").slideDown();
		$("#cpasswordselect").fadeOut(3000);
		return false;
	}

	if ($("#email").val() == "") {

		$("#emailselect").slideDown();
		$("#emailselect").fadeOut(3000);
		return false;
	}
	if (!emailRegex.test(email)) {

		$("#emailselect").slideDown();
		$("#emailselect").fadeOut(3000);
		return false;
	}
	/*
	 * if($('#mno').val() == ""){
	 * 
	 * $("#mnoselect").slideDown(); $("#mnoselect").fadeOut(3000); return false; }
	 */

	if ($('#cname').val() == "0") {

		$("#cnameselect").slideDown();
		$("#cnameselect").fadeOut(3000);
		return false;

	}

	/*
	 * if (!(filter.test(mno))) { $("#mnoselect").slideDown();
	 * $("#mnoselect").fadeOut(3000); return false;
	 *  }
	 */

	var formData = $("#regform").serialize();
	$.ajax({
		url : "addUser/saveUserRegistration",
		type : "POST",
		data : formData,
		success : function(data) {
			$("#email").val("");
			$("#fname").val("");
			$("#lname").val("");
			$("#uname").val("");
			$("#password").val("");
			$("#cpassword").val("");
			$("#mno").val("");
			$("#cname").val("");

			if (data == "Available") {
				$("#userPresent").slideDown();
				$("#userPresent").fadeOut(5000);

			} else {
				$("#successForm").slideDown();
				$("#successForm").fadeOut(5000);
			}

		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("err" + thrownError + xhr.responseText);
		}
	});

}



