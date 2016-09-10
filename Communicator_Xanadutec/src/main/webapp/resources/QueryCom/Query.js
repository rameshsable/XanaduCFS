function loadQueryCom(feedId) {
	$.ajax({
		url : 'querycom',
		type : "POST",
		dataType : 'text',
		data : "feedId=" + feedId,
		async : false,
		success : function(data) {
			$("#msg").html(data);
		},
		error : function(errorThrown) {
			console.log("Error: " + errorThrown);
		}
	});
}

function loadQueryComChatting(soNumber, feedbackType) {

	$.ajax({
		url : 'querycomChat',
		type : "POST",
		dataType : 'text',
		data : "soNumber=" + soNumber + "&feedbackType=" + feedbackType,
		async : false,
		success : function(data) {

			$("#NewFeedComChatting").hide();
			$("#firstViewQuery").html(data);

		},
		error : function(errorThrown) {
			console.log("Error: " + errorThrown);
		}
	});

}

function AdminCommunicatorQueryCom(companyId) {

	$.ajax({
		url : 'adminfeedbackcom',
		type : "POST",
		dataType : 'text',
		data : "userId=" + companyId,
		async : false,
		success : function(data) {
			$("#home").hide();
			$("#newhome").html(data);
		},
		error : function(errorThrown) {
			console.log("Error: " + errorThrown);
		}
	});

}

function saveQuery() {

	var userId = $("#userId").val();
	var companyId = $("#companyId").val();
	var nameOfFile = null;
	var feedBackName = $("#feedbackType").val();

	var tempsonumber = $("#sonumber").val();
	var avayNum = $("#avayaNumber").val();
	var docNum = $("#docid").val();

	var soNumber = tempsonumber.concat('#@', avayNum, '#@', docNum);
	var status = $("#statusId").val();
	/* var chatBox=$("#chatBox").val(); */
	var chatBox = $('.querycomMessage').code();
	chatBox = chatBox.replace(/(<([^>]+)>)/ig, " ");
	chatBox = chatBox.replace(/&nbsp;/g, ' ');
	
	var chatBox1 = $('.querycomMessage').code();
	chatBox1 = chatBox.replace(/(<([^>]+)>)/ig, " ");
	chatBox1 = chatBox.replace(/&nbsp;/g, ' ');
	
	var feedbackType = $("#feedbackType").val();
	var d = new Date();

	var userQuery = $("#userQuery").val();
	var userFromCounter = $("#userFromCounter").val();
	;
	var userToCounter = $("#userToCounter").val();
	$("#usermsg").val($('.querycomMessage').code());

	var actualsubStr = $("#subject").val();

	var date = new Date();
	var c = date.getDate();
	month = date.getMonth() + 1; // current month
	year = date.getFullYear();
	var hours = date.getHours() > 12 ? date.getHours() - 12 : date.getHours();
	var am_pm = date.getHours() >= 12 ? "PM" : "AM";
	hours = hours < 10 ? "0" + hours : hours;
	var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date
			.getMinutes();
	var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date
			.getSeconds();
	time = hours + ":" + minutes + " " + am_pm;

	time = c + "/" + month + "/" + year + " " + time;

	var tds = "<tr> <td id=" + userFromCounter + ">";
	tds += "<div  > "
			+ "<div class='operatorMessageWrap clearfix'>"
			+ "<div class='operatorNameDateWrap'>"
			+ "  <div class='col-md-2 col-xs-2 avatar'>   </div>"
			+ "     <div class='col-md-10 col-xs-10'>  "
			+ "     <div class='messages msg_receive' style='background: background: rgba(189,227,156,0.34);		"
			+ "		background: -moz-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);	"
			+ "		background: -webkit-gradient(left top, right top, color-stop(21%, rgba(189,227,156,0.34)),"
			+ " 		color-stop(38%, rgba(189,227,156,0.34)), color-stop(56%, rgba(189,227,156,0.53)), color-stop(100%, rgba(234,246,223,1)));			"
			+ "		background: -webkit-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
			+ "	 	background: -o-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);			"
			+ "		background: -ms-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);		"
			+ "		background: linear-gradient(to right, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
			+ "		filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#bde39c', endColorstr='#eaf6df', GradientType=1 );' >  "
			+ "		 <p >" + chatBox
			+ "		</p><time style='color: Gray;' datetime='2009-11-13T20:00'>"
			+ "" + userQuery + "&nbsp;&nbsp;&nbsp; " + time + "  </time> "
			+ "       </div></div></div>    " + "</div>"
			+ " </div> </td> </tr>";
	$("#tblData tbody").append(tds);

	var subject = $("#subject").val();
	var fileNames = "";

	/* code to check email permission of user and assign permission to user */

	$.ajax({
		dataType : 'json',
		url : "getListOfUserAccordingToCompanyIdForEmailPermission",
		type : "GET",
		data : "companyId=" + companyId,
		success : function(data) {

			flag = 0;
			var jsonData = JSON.parse(JSON.stringify(data));
			var counter = jsonData.AssignedPermissionList;
			var cntForiterateobj = JSON.parse(counter);
			for (var c = 0; c < cntForiterateobj.length; c++) {

				if (cntForiterateobj[c].userModel.userId == userId) {
					flag = 1;
				}

			}

			if (flag == 0) {

				$.ajax({
					url : "saveEmailPermissionByUserId",
					type : "POST",
					data : {
						userId : "" + userId,
						companyId : "" + companyId
					},
					success : function(data) {

					},
					error : function(xhr, ajaxOptions, thrownError) {
						console.log("err" + thrownError + xhr.responseText);

						return false;
					}
				});

			}

		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.log("err" + thrownError + xhr.responseText);
		}
	});

	/* end of email check permission */

	var oMyForm = new FormData();
	var inp = document.getElementById('loader1');
	var flag = 0;
	for (var i = 0; i < inp.files.length; ++i) {
		var name = inp.files.item(i).name;

		files[i] = inp.files.item(i).name;
		flag = 1;
		oMyForm.append("file" + i, loader1.files[i]);
	}

	if (flag == 1) {
		$
				.ajax({
					dataType : 'json',
					url : "uploadMyFile",
					data : oMyForm,
					type : "POST",
					enctype : 'multipart/form-data',
					processData : false,
					contentType : false,
					success : function(result) {
						nameOfFile = result;
						fileNames = JSON.stringify(result);
						fileNames = fileNames.slice(1, -1);

						flag = 0;

						$.ajax({
							url : 'saveChatForquerycom',
							type : "POST",
							data : {
								soNumber : "" + soNumber,
								status : "" + status,
								chatBox : "" + chatBox,
								feedbackType : "" + feedbackType,
								subject : "" + actualsubStr,
								attachment : "" + fileNames,
							},
							success : function(data) {

								var counter = data.split("#");
								$("#userFromCounter").val(counter[0]);
								$("#userToCounter").val(counter[1]);
								$(".querycomMessage").code("");
							},
							error : function(xhr, ajaxOptions, thrownError) {
								console.log("Error In saveChatForfeedbackcom"
										+ thrownError + xhr.responseText);
							}
						});

						$.ajax({
							url : 'replyemailQuerycom',
							type : "POST",
							data : {
								subject : "" + actualsubStr,
								chatBox : "" + chatBox,
								attachment : "" + fileNames
							},
							success : function(data) {

							},
							error : function(xhr, ajaxOptions, thrownError) {
								console.log("Error In saveChatForfeedbackcom"
										+ thrownError + xhr.responseText);
							}
						});

						if (feedBackName == 'Report') {

							var abc = nameOfFile.split('#@$');
							for (var i = 0; i < abc.length - 1; i++) {
								$.ajax({
									url : "DropBoxFileDetail",
									type : "POST",
									data : {
										name : "" + abc[i]

									},
									success : function(data) {

									},
									error : function(xhr, ajaxOptions,
											thrownError) {
										console.log("err" + thrownError
												+ xhr.responseText);

										return false;
									}
								});
							}
						}

						if (feedBackName == 'Document') {
							var abc = nameOfFile.split('#@$');
							for (var i = 0; i < abc.length - 1; i++) {

								$.ajax({
									url : "DocumentFileDetail",
									type : "POST",
									data : {
										name : "" + abc[i]

									},
									success : function(data) {

									},
									error : function(xhr, ajaxOptions,
											thrownError) {
										console.log("err" + thrownError
												+ xhr.responseText);

										return false;
									}
								});
							}
						}

						document.getElementById("loader1").value = "";
						var splen = fileNames.split("#@$").length;
						var sp = fileNames.split("#@$");
						for (var spl = 0; spl < splen - 1; spl++) {

							var file1 = sp[spl]
							var len = 13;
							var len2 = file1.length;
							var file2 = file1.substr(len, len2);
							tds = "<tr> <td id=" + userFromCounter + ">";
							tds += "<div  > "
									+ "  <div class='col-md-2 col-xs-2 avatar'>   </div>"
									+ "     <div class='col-md-10 col-xs-10'>  "
									+ "     <div class='messages msg_receive' style='background: background: rgba(189,227,156,0.34);		"
									+ "		background: -moz-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);	"
									+ "		background: -webkit-gradient(left top, right top, color-stop(21%, rgba(189,227,156,0.34)),"
									+ " 		color-stop(38%, rgba(189,227,156,0.34)), color-stop(56%, rgba(189,227,156,0.53)), color-stop(100%, rgba(234,246,223,1)));			"
									+ "		background: -webkit-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
									+ "	 	background: -o-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);			"
									+ "		background: -ms-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);		"
									+ "		background: linear-gradient(to right, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
									+ "		filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#bde39c', endColorstr='#eaf6df', GradientType=1 );' >  "
									+ "<a href=demo"
									+ "?"
									+ "path="
									+ sp[spl].replace(/ /g, '+')
									+ ">"
									+ "<strong ><font size='1'> "
									+ file2
									+ "<strong ></font><font color='red'>  <i class='fa fa-download'></i></font> </a>"
							"       </div>    " + "</div>"
									+ " </div> </td> </tr>";
							$("#tblData tbody").append(tds);
						}

					},
					error : function(xhr, ajaxOptions, thrownError) {
						// console.log("err" + thrownError + xhr.responseText);
						console.log("Error In saveChatForfeedbackcom"
								+ thrownError + xhr.responseText);
					}
				});
	}

	else {

		var EmptyFileName = "";

		$.ajax({
			url : 'saveChatForquerycom',
			type : "POST",
			data : {
				soNumber : "" + soNumber,
				status : "" + status,
				chatBox : "" + chatBox,
				feedbackType : "" + feedbackType,
				subject : "" + subject,
				attachment : "" + EmptyFileName,
			},
			success : function(data) {
				// $("#chatBox").focus();

				var counter = data.split("#");
				$("#userFromCounter").val(counter[0]);
				$("#userToCounter").val(counter[1]);
				$(".querycomMessage").code("");
				// console.log(counter);
			},
			error : function(xhr, ajaxOptions, thrownError) {
				// console.log("err" + thrownError + xhr.responseText);
				console.log("Error In saveChatForfeedbackcom" + thrownError
						+ xhr.responseText);
			}
		});

		$.ajax({
			url : 'replyemailQuerycom',
			type : "POST",
			data : {
				subject : "" + actualsubStr,
				chatBox : "" + chatBox,
				attachment : "" + EmptyFileName
			},
			success : function(data) {
				// console.log("success");
				// console.log(counter);

			},
			error : function(xhr, ajaxOptions, thrownError) {
				// console.log("err" + thrownError + xhr.responseText);
				console.log("Error In saveChatForfeedbackcom" + thrownError
						+ xhr.responseText);
			}
		});

	}

}
function liveChatQueryCom() {

	// console.log(i);

	setInterval(
			function() {

				var tempsonumber = $("#sonumber").val();
				var avayNum = $("#avayaNumber").val();
				var docNum = $("#docid").val();

				var soNumber = tempsonumber.concat('#@', avayNum, '#@', docNum);
				var feedbackType = $("#feedbackType").val();
				var userToCounter = $("#userToCounter").val();
				var userFromCounter = $("#userFromCounter").val();
				$
						.ajax({
							type : "POST",
							url : "liveQueryChat",
							data : "soNumber=" + soNumber + "&feedbackType="
									+ feedbackType + "&userToCounter="
									+ userToCounter,
							cache : false,
							dataType : "json",
							success : function(data) {
								if (data == "") {
									// console.log("empty");
									console.log("empty");
								} else {
									// console.log(data);
									console.log(data);
									for (i = 0; i < data.length; i++) {

										var date = new Date(data[i].dateofquery);
										var c = date.getDate();
										month = date.getMonth() + 1; // current
										// month
										year = date.getFullYear();
										var hours = date.getHours() > 12 ? date
												.getHours() - 12 : date
												.getHours();
										var am_pm = date.getHours() >= 12 ? "PM"
												: "AM";
										hours = hours < 10 ? "0" + hours
												: hours;
										var minutes = date.getMinutes() < 10 ? "0"
												+ date.getMinutes()
												: date.getMinutes();
										var seconds = date.getSeconds() < 10 ? "0"
												+ date.getSeconds()
												: date.getSeconds();
										time = hours + ":" + minutes + " "
												+ am_pm;

										time = c + "/" + month + "/" + year
												+ " " + time;

										var tds = "<tr> <td>";
										tds += "<div >"
												+ "<div class='operatorMessageWrap clearfix'>"
												+ "<div class='operatorNameDateWrap'>"
												+ "   <div class='col-md-10 col-xs-10'>"
												+ " <div class='messages msg_sent' style='background:#f2f2f2;'>"
												+ " <p>"
												+ data[i].queryText
												+ "</p>  <time style='color: Gray;' datetime='2009-11-13T20:00'>"
												+ ""
												+ data[i].userquery.firstName+" "+data[i].userquery.lastName
												+ "  &nbsp;&nbsp;&nbsp;  "
												+ time
												+ "</time></div></div>"
												+ "<div class='col-md-2 col-xs-2 '> "
												+ "</div></div></div></div> </td> </tr>";
										$("#tblData tbody").append(tds);

										var splen = data[i].attachment
												.split("#@$").length;
										var sp = data[i].attachment
												.split("#@$");
										for (var spl = 0; spl < splen - 1; spl++) {

											var file1 = sp[spl]
											var len = 13;
											var len2 = file1.length;
											var file2 = file1.substr(len, len2);
											var tds = "<tr> <td>";
											tds += "<div >"
													+ "   <div class='col-md-10 col-xs-10'>"
													+ " <div class='messages msg_sent' style='background:#f2f2f2;'>"
													+ "<a href=demo"
													+ "?"
													+ "path="
													+ sp[spl]
													+ ">"
													+ "<font size='1'>"
													+ sp[spl]
															.replace(/ /g, '+')
															.substring(13)
													+ "<strong ></font><font color='red'>  <i class='fa fa-download'></i></font> </a>"

											"</div></div>"
													+ "<div class='col-md-2 col-xs-2 '> "
													+ "</div></div> </td> </tr>";
											$("#tblData tbody").append(tds);
										}

										$("#userToCounter").val(
												data[i].counter.queryCounter);

									}

								}
							},
							error : function(xhr, ajaxOptions, thrownError) {
								console.log("Error In Live Chat" + thrownError
										+ xhr.responseText);
								// console.log("err" + thrownError +
								// xhr.responseText);
							}
						});

				$
						.ajax({
							type : "POST",
							url : "liveQueryChatUserFrom",
							data : "soNumber=" + soNumber + "&feedbackType="
									+ feedbackType + "&userFromCounter="
									+ userFromCounter,
							cache : false,
							dataType : "json",
							success : function(data) {
								if (data == "") {
									// console.log("empty");
									console.log("empty");
								} else {
									// console.log(data);
									console.log(data);
									for (i = 0; i < data.length; i++) {

										var date = new Date(data[i].dateofquery);
										var c = date.getDate();
										month = date.getMonth() + 1; // current
										// month
										year = date.getFullYear();
										var hours = date.getHours() > 12 ? date
												.getHours() - 12 : date
												.getHours();
										var am_pm = date.getHours() >= 12 ? "PM"
												: "AM";
										hours = hours < 10 ? "0" + hours
												: hours;
										var minutes = date.getMinutes() < 10 ? "0"
												+ date.getMinutes()
												: date.getMinutes();
										var seconds = date.getSeconds() < 10 ? "0"
												+ date.getSeconds()
												: date.getSeconds();
										time = hours + ":" + minutes + " "
												+ am_pm;

										time = c + "/" + month + "/" + year
												+ " " + time;

										var tds = "<tr> <td>";
										tds += "<div >"
												+ "<div class='operatorMessageWrap clearfix'>"
												+ "<div class='operatorNameDateWrap'>"
												+ "<div class='col-md-2 col-xs-2 avatar'> </div> "
												+ "   <div class='col-md-10 col-xs-10'>"
												+ " <div class='messages msg_receive' style='background: background: rgba(189,227,156,0.34);"
												+ "background: -moz-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
												+ "background: -webkit-gradient(left top, right top, color-stop(21%, rgba(189,227,156,0.34)), color-stop(38%, rgba(189,227,156,0.34)), color-stop(56%, rgba(189,227,156,0.53)), color-stop(100%, rgba(234,246,223,1)));"
												+ "background: -webkit-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
												+ "background: -o-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
												+ "background: -ms-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
												+ "background: linear-gradient(to right, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
												+ "filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#bde39c', endColorstr='#eaf6df', GradientType=1 );'>"
												+ " <p>"
												+ data[i].queryText
												+ "</p>  <time style='color: Gray;' datetime='2009-11-13T20:00'>"
												+ ""
												+ data[i].userquery.firstName+" "+data[i].userquery.lastName
												+ "  &nbsp;&nbsp;&nbsp;  "
												+ time + "</time></div></div></div></div>"
												+ "</div> </td> </tr>";
										$("#tblData tbody").append(tds);

										var splen = data[i].attachment
												.split("#@$").length;
										var sp = data[i].attachment
												.split("#@$");
										for (var spl = 0; spl < splen - 1; spl++) {

											var file1 = sp[spl]
											var len = 13;
											var len2 = file1.length;
											var file2 = file1.substr(len, len2);

											var tds = "<tr> <td>";
											tds += "<div >"
													+ "<div class='col-md-2 col-xs-2 avatar'> </div> "
													+ "   <div class='col-md-10 col-xs-10'>"
													+ " <div class='messages msg_receive' style='background: background: rgba(189,227,156,0.34);"
													+ "background: -moz-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
													+ "background: -webkit-gradient(left top, right top, color-stop(21%, rgba(189,227,156,0.34)), color-stop(38%, rgba(189,227,156,0.34)), color-stop(56%, rgba(189,227,156,0.53)), color-stop(100%, rgba(234,246,223,1)));"
													+ "background: -webkit-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
													+ "background: -o-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
													+ "background: -ms-linear-gradient(left, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
													+ "background: linear-gradient(to right, rgba(189,227,156,0.34) 21%, rgba(189,227,156,0.34) 38%, rgba(189,227,156,0.53) 56%, rgba(234,246,223,1) 100%);"
													+ "filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#bde39c', endColorstr='#eaf6df', GradientType=1 );'>"
													+ "<a href=demo"
													+ "?"
													+ "path="
													+ sp[spl]
													+ ">"
													+ "<font size='1'>"
													+ sp[spl]
															.replace(/ /g, '+')
															.substring(13)
													+ "<strong ></font><font color='red'>  <i class='fa fa-download'></i></font> </a>"

											"</div></div>"
													+ "</div> </td> </tr>";
											$("#tblData tbody").append(tds);
										}

										$("#userFromCounter").val(
												data[i].counter.queryCounter);

									}

								}
							},
							error : function(xhr, ajaxOptions, thrownError) {
								console.log("Error In Live Chat" + thrownError
										+ xhr.responseText);
								// console.log("err" + thrownError +
								// xhr.responseText);
							}
						});
			}, 1000);
}


function saveNewQuery() {

	
	var counterdatatable=0;
	 $("#dataTable1 tr").each(function() {
	    	
		 if(counterdatatable==1){
			 var datatblvalue=$(this).find("td:first").text();	
	         
	         if(datatblvalue=="No data available in table"){
	        	 $(this).closest('tr').remove();
	         }
	         
		 }
		 counterdatatable=counterdatatable+1;
        
        
		});
	 
	 
	var userId = $("#userId").val();
	var companyId = $("#companyId").val();
	var name = null;
	var availabilityCheck = null;
	var rowCount = $('#dataTable1 tr').length;
	var avayNum = $("#avayaNumber").val();
	var docNum = $("#docid").val();
	var tempsonumber = $("#sonumber").val();
	var feedBackName = $("#feedBackName").val();
	var feedbackType = $("#feedbackType").val();
	var chatBox = $(".feedcomMessage").code();
	chatBox = chatBox.replace(/(<([^>]+)>)/ig, " ");
	chatBox = chatBox.replace(/&nbsp;/g, ' ');
	var subject = $("#subject").val();
	var tempsonumber2 = $("#sonumber").val();

	var flagAvayNum = 0, FlagDocNum = 0, flagTempsonumber = 0;
	if (avayNum == "" || avayNum == undefined) {

		avayNum = 0;
		flagAvayNum = 1;
	}

	if (docNum == "" || docNum == undefined) {
		docNum = 0;
		FlagDocNum = 1;
	}

	if (tempsonumber == "" || tempsonumber == undefined) {
		tempsonumber = '0';
		flagTempsonumber = 1;
	}

	
	if (avayNum == 0 && docNum == 0 && tempsonumber == 0) {
		$("#feelAtLeastOneField").slideDown();
		$("#feelAtLeastOneField").fadeOut(5000);
		$("#feelAtLeastOneField").focus();
		tempsonumber = 0;

		return false;

	}

	if (flagAvayNum == 1 && FlagDocNum == 1 && flagTempsonumber == 1) {
		$("#feelAtLeastOneField").slideDown();
		$("#feelAtLeastOneField").fadeOut(5000);
		$("#feelAtLeastOneField").focus();
		flagAvayNum = 0, FlagDocNum = 0, flagTempsonumber = 0;
		return false;
	}

	if (subject == "") {
		$("#subjectSel").slideDown();
		$("#subjectSel").fadeOut(5000);
		$("#sonumber").focus();

		return false;
	}

	if (chatBox == "") {
		$("#chatBoxSel").slideDown();
		$("#chatBoxSel").fadeOut(5000);
		$("#chatBox").focus();
		return false;
	}

	var sonumber = tempsonumber.concat('#@', avayNum, '#@', docNum);

/*	var subject2 = 'QueryCom'.concat(feedBackName, '[', avayNum, '-', docNum,
			'-', tempsonumber, ']');
	subject2 = subject.concat(subject2);
	*/
	var now = new Date();
	var d = Date.UTC(now.getFullYear(), now.getMonth(), now.getDate(), now
			.getHours(), now.getMinutes(), now.getSeconds(), now
			.getMilliseconds());
	
	var subject2 = subject.concat(" "+d).trim();
	var fileNames = "";

	/* code to check email permission of user and assign permission to user */

	$.ajax({
		dataType : 'json',
		url : "getListOfUserAccordingToCompanyIdForEmailPermission",
		type : "GET",
		data : "companyId=" + companyId,
		success : function(data) {
			// AssignedPermissionList

			flag = 0;
			var jsonData = JSON.parse(JSON.stringify(data));
			var counter = jsonData.AssignedPermissionList;
			var cntForiterateobj = JSON.parse(counter);
			// check checkbox
			for (var c = 0; c < cntForiterateobj.length; c++) {

				if (cntForiterateobj[c].userModel.userId == userId) {
					flag = 1;
				}

			}

			if (flag == 0) {

				$.ajax({
					url : "saveEmailPermissionByUserId",
					type : "POST",
					data : {
						userId : "" + userId,
						companyId : "" + companyId
					},
					success : function(data) {

					},
					error : function(xhr, ajaxOptions, thrownError) {
						console.log("err" + thrownError + xhr.responseText);

						return false;
					}
				});

			}

		},
		error : function(xhr, ajaxOptions, thrownError) {
			console.log("err" + thrownError + xhr.responseText);
		}
	});

	var oMyForm = new FormData();
	var inp = document.getElementById('loader1');
	var flag = 0;
	for (var i = 0; i < inp.files.length; ++i) {
		var name = inp.files.item(i).name;
		files[i] = inp.files.item(i).name;
		flag = 1;
		oMyForm.append("file" + i, loader1.files[i]);
	}

	if (flag == 1) {

		$.ajax({
			dataType : 'json',
			url : "uploadMyFile",
			data : oMyForm,
			type : "POST",
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false,
			success : function(result) {
				name = result;
				fileNames = JSON.stringify(result);
				fileNames = fileNames.slice(1, -1);

				document.getElementById("loader1").value = ""; // removes files
				// elements
				// means reset
				// the values

				$.ajax({
					url : 'NewliveQuerychat',
					type : "POST",
					dataType : 'text',
					data : {
						soNumber : "" + sonumber,
						feedbackType : "" + feedbackType,
						attachment : "" + fileNames,
						chatBox : "" + chatBox,
						subject2 : "" + subject2
					},
					async : false,
					success : function(data) {
						availabilityCheck = data;
						if (data == "AlreadyAvailable") {
							$("#soAvailable").slideDown();
							$("#soAvailable").fadeOut(7000);
							$("#sonumber").focus();

							$("#avayaNumber").val(" ");
							$("#docid").val(" ");
							$("#sonumber").val(" ");
							$(".feedcomMessage").code("");
							$("#subject").val(" ");
							$("#loader1").val(" ");
							$('input').val("");

							return false;

						} else {

							$
									.ajax({
										url : "sendQueryEmail",
										type : "POST",
										data : {
											tempsonumber2 : "" + tempsonumber2,
											avayNum : "" + avayNum,
											docNum : "" + docNum,
											subject : "" + subject2,
											attachment : "" + fileNames,
											chatBox : "" + chatBox,
											feedBackName : "" + feedBackName
										},
										success : function(data) {

										},
										error : function(xhr, ajaxOptions,
												thrownError) {
											console.log("err" + thrownError
													+ xhr.responseText);

											return false;
										}
									});

							// console.log(feedBackName);
							if (feedBackName == 'Report') {

								// console.log('sending drop box detw');

								var abc = name.split('#@$');
								for (var i = 0; i < abc.length - 1; i++) {
									$.ajax({
										url : "DropBoxFileDetail",
										type : "POST",
										data : {
											name : "" + abc[i]

										},
										success : function(data) {

											// location.reload(true);
										},
										error : function(xhr, ajaxOptions,
												thrownError) {
											console.log("err" + thrownError
													+ xhr.responseText);

											return false;
										}
									});
								}
							}

							if (feedBackName == 'Document') {
								var abc = name.split('#@$');
								for (var i = 0; i < abc.length - 1; i++) {

									$.ajax({
										url : "DocumentFileDetail",
										type : "POST",
										data : {
											name : "" + abc[i]

										},
										success : function(data) {

											// location.reload(true);
										},
										error : function(xhr, ajaxOptions,
												thrownError) {
											console.log("err" + thrownError
													+ xhr.responseText);

											return false;
										}
									});
								}
							}

							return true;
						}
					},
					error : function(errorThrown) {
						console.log("Error: " + errorThrown);
					}
				});

				if (availabilityCheck != "AlreadyAvailable") {
					queryfiles = [];
					sonumber = tempsonumber.concat('%23%40', avayNum, '%23%40',
							docNum);
					var tbl = "<tr><td align=center>";

					tbl += "<a href=querycomChat?soNumber=" + sonumber
							+ "&feedbackType=" + feedbackType + ">" + rowCount
							+ "</a></td>";

					tbl += "<td ><a href=querycomChat?soNumber=" + sonumber
							+ "&feedbackType=" + feedbackType + ">"
							+ tempsonumber + "</a></td>"
							+ "<td><a href=querycomChat?soNumber=" + sonumber
							+ "&feedbackType=" + feedbackType + ">" + avayNum
							+ "</a></td>"
							+ "<td><a href=querycomChat?soNumber=" + sonumber
							+ "&feedbackType=" + feedbackType + ">" + docNum
							+ "</a></td>"
							+ "<td><a href=querycomChat?soNumber=" + sonumber
							+ "&feedbackType=" + feedbackType + ">" + chatBox
							+ "</a></td></tr>";
					$('#dataTable1').append(tbl);
					$("#avayaNumber").val(" ");
					$("#docid").val(" ");
					$("#sonumber").val(" ");
					$(".feedcomMessage").code("");
					$("#operatorId").val(" ");
					$("#subject").val(" ");
					$("#loader1").val(" ");
					$('input').val("");
				}
			},
			error : function(result) {
				console.log('error' + JSON.stringify(result));

				return false;
			}

		});

	}

	else {

		var emptyFiles = "";

		$
				.ajax({
					url : 'NewliveQuerychat',
					type : "POST",
					dataType : 'text',
					data : {
						soNumber : "" + sonumber,
						feedbackType : "" + feedbackType,
						attachment : "" + emptyFiles,
						chatBox : "" + chatBox,
						subject2 : "" + subject2
					},
					async : false,
					success : function(data) {
						availabilityCheck = data;
						if (data == "AlreadyAvailable") {
							$("#soAvailable").slideDown();
							$("#soAvailable").fadeOut(7000);
							$("#sonumber").focus();

							$("#avayaNumber").val(" ");
							$("#docid").val(" ");
							$("#sonumber").val(" ");
							$(".feedcomMessage").code("");
							$("#subject").val(" ");
							$("#loader1").val(" ");
							$('input').val("");

							return false;

						} else {

							$
									.ajax({
										url : "sendQueryEmail",
										type : "POST",
										data : {
											tempsonumber2 : "" + tempsonumber2,
											avayNum : "" + avayNum,
											docNum : "" + docNum,
											subject : "" + subject2,
											attachment : "" + emptyFiles,
											chatBox : "" + chatBox,
											feedBackName : "" + feedBackName
										},
										success : function(data) {

										},
										error : function(xhr, ajaxOptions,
												thrownError) {
											console.log("err" + thrownError
													+ xhr.responseText);

											return false;
										}
									});
							return true;
						}
					},
					error : function(errorThrown) {
						console.log("Error: " + errorThrown);
					}
				});

		if (availabilityCheck != "AlreadyAvailable") {
			queryfiles = [];
			sonumber = tempsonumber.concat('%23%40', avayNum, '%23%40', docNum);
			var tbl = "<tr><td align=center>";

			tbl += "<a href=querycomChat?soNumber=" + sonumber
					+ "&feedbackType=" + feedbackType + ">" + rowCount
					+ "</a></td>";

			tbl += "<td ><a href=querycomChat?soNumber=" + sonumber
					+ "&feedbackType=" + feedbackType + ">" + tempsonumber
					+ "</a></td>" + "<td><a href=querycomChat?soNumber="
					+ sonumber + "&feedbackType=" + feedbackType + ">"
					+ avayNum + "</a></td>"
					+ "<td><a href=querycomChat?soNumber=" + sonumber
					+ "&feedbackType=" + feedbackType + ">" + docNum
					+ "</a></td>" + "<td><a href=querycomChat?soNumber="
					+ sonumber + "&feedbackType=" + feedbackType + ">"
					+ chatBox + "</a></td></tr>";
			$('#dataTable1').append(tbl);
			$("#avayaNumber").val(" ");
			$("#docid").val(" ");
			$("#sonumber").val(" ");
			$(".feedcomMessage").code("");
			$("#operatorId").val(" ");
			$("#subject").val(" ");
			$("#loader1").val(" ");
			$('input').val("");
		}
	}

}