function fillGraphValues(feedName , OpenCount , closeCount , inProgressCount){
	$('#container1').highcharts({
		chart : {
			type : 'column',

			margin : [ 50, 50, 100, 80 ]
		},
		title : {
			text : 'Analyse Flow'
		},
		xAxis : {
			categories : feedName,

			title : {
				text : 'Category'
			},
			labels : {
				rotation : -45,
				align : 'right',
				style : {
					fontSize : '10px',
					fontFamily : 'Verdana, sans-serif'
				}
			}
		},
		yAxis : {
			min : 0,

			title : {
				text : 'ISSUE TRACKER'
			}
		},
		legend : {
			reversed : true
		},
		tooltip : {
			pointFormat : 'ISSUE: <b>{point.y:.1f}</b>',
		},
		plotOptions : {
			series : {
				stacking : 'normal',

				cursor : 'pointer',
				pointWidth : 15,
				point : {
					events : {
						click : function() {
						}
					}
				}
			}
		},
		series : [ {
			name : 'INPROGRESS',
			data : inProgressCount,
			color : '#466600',
			dataLabels : {

				enabled : false,
				rotation : -90,
				color : '#FFFFFF',
				align : 'right',
				x : 2,
				y : 0,
				style : {

					fontSize : '10px',
					fontFamily : 'Verdana, sans-serif',
					textShadow : '0 0 0px black'
				}
			}

		}, {
			name : 'OPEN',
			data : OpenCount,
			color : '#092d3d',
			dataLabels : {

				enabled : false,
				rotation : -90,
				color : '#FFFFFF',
				align : 'right',
				x : 2,
				y : 0,
				style : {

					fontSize : '10px',
					fontFamily : 'Verdana, sans-serif',
					textShadow : '0 0 0px black'
				}
			}

		}, {
			name : 'CLOSE',
			data : closeCount,
			color : '#c5161d',
			dataLabels : {

				enabled : false,
				rotation : -90,
				color : '#FFFFFF',
				align : 'right',
				x : 2,
				y : 0,
				style : {

					fontSize : '10px',
					fontFamily : 'Verdana, sans-serif',
					textShadow : '0 0 0px black'
				}
			}
		} ]
	});
}

function loadFeedCom(feedId) {
	$.ajax({
		url : 'feedbackcom',
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
function loadfeedcomChatting(soNumber, feedbackType) {

	var type = "Feedcom";
	var userId = "2";

	$.ajax({
		url : 'feedcomChat',
		type : "POST",
		dataType : 'text',
		data : "soNumber=" + soNumber + "&feedbackType=" + feedbackType,
		async : false,
		success : function(data) {

			/*
			 * console.log('calling last Function'); // here I am calling last
			 * visit Function $.ajax({ url : 'saveLastVisitTime', type : "POST",
			 * //dataType : 'text', data : "userId=" + userId + "&type=" + type,
			 * async : false, success : function(data) { }, error :
			 * function(errorThrown) { console.log("Error: " + errorThrown); }
			 * });
			 */
			$("#NewFeedComChatting").hide();
			$("#firstView").html(data);

		},
		error : function(errorThrown) {
			console.log("Error: " + errorThrown);
		}
	});

}

function saveFeedBack() {

	var userId = $("#userId").val();
	var companyId = $("#companyId").val();
	var nameOfFile = null;
	var feedBackName = $("#feedbackType").val();

	var tempsonumber = $("#sonumber").val();
	var avayNum = $("#avayaNumber").val();
	var docNum = $("#docid").val();

	var soNumber = tempsonumber.concat('#@', avayNum, '#@', docNum);
	

	var status = $("#statusId").val();
	var chatBox = $('.feedcomMessage').code();

	/*chatBox = chatBox.replace(/(<([^>]+)>)/ig, " ");
	chatBox = chatBox.replace(/&nbsp;/g, ' ');*/

	var chatBox1 = chatBox;
	var feedbackType = $("#feedbackType").val();
	var operator = $("#operator").val();
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

	var userfeedback = $("#userfeedback").val();

	var userFromCounter = $("#userFromCounter").val();
	;
	var userToCounter = $("#userToCounter").val();
	$("#usermsg").val(chatBox);

	var actualsubStr = $("#subject").val();

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
			+ "" + userfeedback + "&nbsp;&nbsp;&nbsp; " + time + "  </time> "
			+ "       </div></div></div>    " + "</div>"
			+ " </div> </td> </tr>";
	$("#tblData tbody").append(tds);

	var redflag;
	if ($("#checkboxThreeInput").prop("checked") == true) {
		redflag = "Active";

	} else {
		redflag = "DisActive";
	}
	var subject = $("#subject").val();
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
		document.getElementById('light1').style.display = 'block';
		document.getElementById('fade1').style.display = 'block';
		
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
							url : 'saveChatForfeedbackcom',
							type : "POST",
							data : {
								soNumber : "" + soNumber,
								status : "" + status,
								subject : "" + subject,
								attachment : "" + fileNames,
								chatBox : "" + chatBox,
								feedbackType : "" + feedbackType,
								operator : "" + operator,
								redflag : "" + redflag
							},
							success : function(data) {
								// $("#chatBox").focus();

								document.getElementById('light1').style.display = 'none';
								document.getElementById('fade1').style.display = 'none';
								
								var counter = data.split("#");
								$("#userFromCounter").val(counter[0]);
								$("#userToCounter").val(counter[1]);
								$(".feedcomMessage").code("");

							},
							error : function(xhr, ajaxOptions, thrownError) {

								console.log("Error In saveChatForfeedbackcom"
										+ thrownError + xhr.responseText);
							}
						});

						$.ajax({
							url : 'replyemailfeedbackcom',
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

						// ramesh attach to drop box if name is Report
						// console.log(nameOfFile);
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
							var abc = nameOfFile.split('#@$');
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

						document.getElementById("loader1").value = ""; // removes
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
						files = [];
					},
					error : function(result) {
						// console.log('error'+JSON.stringify(result));
					}
				});

	} else {

		$.ajax({
			url : 'saveChatForfeedbackcom',
			type : "POST",
			data : {
				soNumber : "" + soNumber,
				status : "" + status,
				subject : "" + subject,
				attachment : "" + fileNames,
				chatBox : "" + chatBox,
				feedbackType : "" + feedbackType,
				operator : "" + operator,
				redflag : "" + redflag
			},
			success : function(data) {
				var counter = data.split("#");
				$("#userFromCounter").val(counter[0]);
				$("#userToCounter").val(counter[1]);
				$(".feedcomMessage").code("");
			},
			error : function(xhr, ajaxOptions, thrownError) {
				console.log("Error In saveChatForfeedbackcom" + thrownError
						+ xhr.responseText);
			}
		});

		$.ajax({
			url : 'replyemailfeedbackcom',
			type : "POST",
			data : {
				subject : "" + actualsubStr,
				chatBox : "" + chatBox,
				attachment : "" + fileNames
			},
			success : function(data) {
			},
			error : function(xhr, ajaxOptions, thrownError) {
				console.log("Error In saveChatForfeedbackcom" + thrownError
						+ xhr.responseText);
			}
		});

	}

}
function liveChat() {

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
							url : "livechat",
							data : "soNumber=" + soNumber + "&feedbackType="
									+ feedbackType + "&userToCounter="
									+ userToCounter,
							cache : false,
							dataType : "json",
							success : function(data) {

								if (data == "") {
									console.log("empty");
								} else {
									console.log(data);
									for (i = 0; i < data.length; i++) {
										var date = new Date(data[i].fdate);
										var c = date.getDate();
										month = date.getMonth() + 1; // current
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
										var userfeedback = $("#userfeedback")
												.val();
										var userFromCounter = $(
												"#userFromCounter").val();
										var userToCounter = $("#userToCounter")
												.val();
										$("#usermsg").val(
												$('.feedcomMessage').code());

										var tds = "<tr> <td>";
										tds += "<div >"
												+ "<div class='operatorMessageWrap clearfix'>"
												+ "<div class='operatorNameDateWrap'>"
												+ "   <div class='col-md-10 col-xs-10'>"
												+ " <div class='messages msg_sent' style='background:#f2f2f2;'>"
												+ " <p>"
												+ data[i].feedbackText
												+ "</p>  <time style='color: Gray;' datetime='2009-11-13T20:00'>"
												+ ""
												+ data[i].userfeedback.firstName
												+ " "
												+ data[i].userfeedback.lastName
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

										$("#userToCounter")
												.val(
														data[i].counter.feedbackCounter);

									}

								}
							},
							error : function(xhr, ajaxOptions, thrownError) {
								console.log("Error In Live Chat" + thrownError
										+ xhr.responseText);
							}
						});

				$
						.ajax({
							type : "POST",
							url : "livechatUserFrom",
							data : "soNumber=" + soNumber + "&feedbackType="
									+ feedbackType + "&userFromCounter="
									+ userFromCounter,
							cache : false,
							dataType : "json",
							success : function(data) {
								if (data == "") {
									console.log("empty");
								} else {
									console.log(data);
									for (i = 0; i < data.length; i++) {
										var date = new Date(data[i].fdate);
										var c = date.getDate();
										month = date.getMonth() + 1; // current
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

										var userfeedback = $("#userfeedback")
												.val();
										var userFromCounter = $(
												"#userFromCounter").val();
										;
										var userToCounter = $("#userToCounter")
												.val();
										$("#usermsg").val(
												$('.feedcomMessage').code());

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
												+ data[i].feedbackText
												+ "</p>  <time style='color: Gray;' datetime='2009-11-13T20:00'>"
												+ ""
												+ data[i].userfeedback.firstName
												+ " "
												+ data[i].userfeedback.lastName
												+ "  &nbsp;&nbsp;&nbsp;  "
												+ time
												+ "</time></div></div></div></div>"
												+

												"</div> </td> </tr>";
										$("#tblData tbody").append(tds);

										var splen = data[i].attachment
												.split("#@$").length;
										var sp = data[i].attachment
												.split("#@$");
										for (var spl = 0; spl < splen - 1; spl++) {
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

											"</div> </td> </tr>";
											$("#tblData tbody").append(tds);
										}

										$("#userFromCounter")
												.val(
														data[i].counter.feedbackCounter);

									}

								}
							},
							error : function(xhr, ajaxOptions, thrownError) {
								console.log("Error In Live Chat" + thrownError
										+ xhr.responseText);
							}
						});
			}, 1000);
}

function operatorfirstName(abc) {
	for (j = 0; j < operators.length; j++) {
		if (abc == operators[j]) {
			$("#operatorId").val(operatorsId[j]);
		}
	}
}

function saveNewFeedFeedBack() {


	
	var counterdatatable = 0;
	$("#dataTable1 tr").each(function() {

		if (counterdatatable == 1) {
			var datatblvalue = $(this).find("td:first").text();

			if (datatblvalue == "No data available in table") {
				$(this).closest('tr').remove();
			}

		}
		counterdatatable = counterdatatable + 1;

	});

	var userId = $("#userId").val();
	var companyId = $("#companyId").val();
	var availabilityCheck = null;
	var name = null;
	var avayNum = $("#avayaNumber").val();
	var docNum = $("#docid").val();
	var tempsonumber = $("#sonumber").val();

	var feedbackType = $("#feedbackType").val();
	var id = $("#feedbackType").val();

	var chatBox = $(".feedcomMessage").code();
/*	chatBox = chatBox.replace(/(<([^>]+)>)/ig, " ");
	chatBox = chatBox.replace(/&nbsp;/g, ' ');*/

	var feedBackName = $("#feedBackName").val();

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
		tempsonumber = 0+"-"+ new Date().getTime();
	}

	
	
	var sonumber = tempsonumber.concat('#@', avayNum, '#@', docNum);

	var operatorId = $("#operatorId").val();
	var feedbackType = $("#feedbackType").val();
	var chatBox = $(".feedcomMessage").code();

	var rowCount = $('#dataTable1 tr').length;
	
	// avayNum docNum tempsonumber
	var subject = $("#subject").val();
	
	if(subject==""){
		$("#submsg").slideDown();
		$("#submsg").fadeOut(5000);
		$("#subject").focus();
		return false;
	}

	if (sonumber == "") {
		$("#soNumberSel").slideDown();
		$("#soNumberSel").fadeOut(5000);
		$("#sonumber").focus();

		return false;
	}
	if (operatorId == "") {
		$("#OperatorSel").slideDown();
		$("#OperatorSel").fadeOut(5000);
		$("#operator").focus();
		// console.log(operatorId);
		$("#chatBox").focus();
		return false;
	}
	if (chatBox == "") {
		$("#chatBoxSel").slideDown();
		$("#chatBoxSel").fadeOut(5000);
		$("#chatBox").focus();
		return false;
	}


	// create 13 digit date and assign it as token to subject
	var now = new Date();
	var d = Date.UTC(now.getFullYear(), now.getMonth(), now.getDate(), now
			.getHours(), now.getMinutes(), now.getSeconds(), now
			.getMilliseconds());

	/*
	 * var subject2 = 'FeedCom'.concat(feedBackName, '[', avayNum, '-', docNum,
	 * '-', tempsonumber, ']');
	 */

	/* subject2 = subject.concat(subject2); */
	var subject2 = subject.concat(" " + d).trim();
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

	/* end of email check permission */

	var oMyForm = new FormData();
	var inp = document.getElementById('loader1');
	var flag = 0;
	for (var i = 0; i < inp.files.length; ++i) {
		var name = inp.files.item(i).name;
		feedBackFiles[i] = inp.files.item(i).name;
		flag = 1;
		oMyForm.append("file" + i, loader1.files[i]);
	}

	
	if(flag==1){
		document.getElementById('light1').style.display = 'block';
		document.getElementById('fade1').style.display = 'block';
	}
	
	
	$.ajax({
		dataType : 'json',
		url : "uploadMyFile",
		data : oMyForm,
		type : "POST",
		enctype : 'multipart/form-data',
		processData : false,
		contentType : false,
		success : function(result) {
			name = result; // assigning name of files

			fileNames = JSON.stringify(result);
			fileNames = fileNames.slice(1, -1);
			document.getElementById("loader1").value = "";
			$.ajax({
				url : 'Newlivechat',
				type : "POST",
				dataType : 'text',
				data : {
					soNumber : "" + sonumber,
					feedbackType : "" + feedbackType,
					Operator : "" + operatorId,
					chatBox : "" + chatBox,
					attachement : "" + fileNames,
					subject : "" + subject2
				},
				async : false,
				success : function(data) {
					availabilityCheck = data;
					if (data == "AlreadyAvailable") {
						$("#soAvailable").slideDown();
						$("#soAvailable").fadeOut(5000);
						$("#sonumber").focus();

						$("#avayaNumber").val(" ");
						$("#docid").val(" ");
						$("#sonumber").val(" ");
						$(".feedcomMessage").code("");
						/* $("#operatorId").val(" "); */
						$("#subject").val(" ");
						$("#loader1").val(" ");
						$('input').val("");
						
						document.getElementById('light1').style.display = 'none';
						document.getElementById('fade1').style.display = 'none';

					} else {

						/*
						 * If data is all ready available you dont need to send
						 * Email or it not suite to bussiness logic
						 */
						document.getElementById('light1').style.display = 'none';
						document.getElementById('fade1').style.display = 'none';
						
						$.ajax({
							url : "sendFeedBackEmail",
							type : "POST",
							data : {
								tempsonumber : "" + tempsonumber,
								avayNum : "" + avayNum,
								docNum : "" + docNum,
								subject : "" + subject2,
								attachment : "" + fileNames,
								chatBox : "" + chatBox,
								feedBackName : "" + feedBackName
							},
							success : function(data) {
								console.log("Send Email Log " + data);
							},
							error : function(xhr, ajaxOptions, thrownError) {
								console.log("err" + thrownError
										+ xhr.responseText);

								return false;
							}
						});

						if (feedBackName == 'Report') {
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
						// console.log(feedBackName+'name of feed');
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

						var redflags = $("#redflag").val();
						if (redflags == "Active") {
							$.ajax({
								url : 'saveRedFlag',
								type : "POST",
								dataType : 'text',
								data : "soNumber=" + sonumber
										+ "&feedbackType=" + feedbackType,
								async : false,
								success : function(data1) {

								},
								error : function(errorThrown) {
									console.log("Error: " + errorThrown);
								}
							});

						} else {

						}
					}
				},
				error : function(errorThrown) {
					console.log("Error: " + errorThrown);
				}
			});

			if (availabilityCheck != "AlreadyAvailable") {
				
				
				
				feedBackFiles = [];
				sonumber = tempsonumber.concat('%23%40', avayNum, '%23%40',
						docNum);
				var tbl = "<tr><td align=center>";

				tbl += "<a href=feedcomChat?soNumber=" + sonumber
						+ "&feedbackType=" + id + ">" + rowCount + "</a></td>";

				tbl += "<td ><a href=feedcomChat?soNumber=" + sonumber
						+ "&feedbackType=" + id + ">" + tempsonumber
						+ "</a></td>" + "<td><a href=feedcomChat?soNumber="
						+ sonumber + "&feedbackType=" + id + ">" + avayNum
						+ "</a></td>" + "<td><a href=feedcomChat?soNumber="
						+ sonumber + "&feedbackType=" + id + ">" + docNum
						+ "</a></td>" + "<td><a href=feedcomChat?soNumber="
						+ sonumber + "&feedbackType=" + id + ">" + chatBox
						+ "</a></td><td>"+$("#userMessageForAssinee").val()+"</td><td>/-</td></tr>";
				$('#dataTable1').append(tbl);
				$("#avayaNumber").val(" ");
				$("#docid").val(" ");
				$("#sonumber").val(" ");
				$(".feedcomMessage").code("");
				/* $("#operatorId").val(" "); */
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
	// $( "#dataTable1" ).DataTable();

}