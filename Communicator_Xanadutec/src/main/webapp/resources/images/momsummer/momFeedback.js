function saveMom() {
	
	
	var datepicker = $('#datepicker').val();
	var attendee = $('#attendee').val();
	var subject = $('#subject').val();
	var message = $('.saveMom').code();
	


	message = message.replace(/(<([^>]+)>)/ig, " ");

	if (datepicker == "") {
		$("#dateselect").slideDown();
		$("#dateselect").fadeOut(3000);
		return false;
	}

	if (attendee == "") {
		$("#attendeeselect").slideDown();
		$("#attendeeselect").fadeOut(3000);
		return false;
	}
	if (subject == "") {
		$("#subjectselect").slideDown();
		$("#subjectselect").fadeOut(3000);
		return false;
	}

	if (message == "") {

		$("#messageselect").slideDown();
		$("#messageselect").fadeOut(3000);
		return false;
	}

	$.ajax({
		url : 'momhome',
		type : "POST",
		data : {
			datepicker : "" + datepicker,
			attendee : "" + attendee,
			subject : "" + subject,
			message : "" + message
		},
		success : function(data) {
			$("#datepicker").val("");
			$("#attendee").val("");

			$('.saveMom').code("");
			$("#subject").val("");

			$("#success").slideDown();
			$("#success").fadeOut(3000);

		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.log("Error In saveChatForfeedbackcom" + thrownError
					+ xhr.responseText);
		}
	});

}

function edit_onClick() {
	$.ajax({

		url : 'momhome',
		data : {
		// date: ""+ ${mom.date}
		}

	});
}
