<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring MVC - Upload File</title>
<!--   <script src="http://code.jquery.com/jquery-1.9.1.js"></script> -->

</head>
<body>
<input type="file" name="loader1" id="loader1" />
<input type="button" id="subbutton" value="Upload"/>


  <script>
$(document).ready(function(){
  $("#subbutton").click(function(){
          processFileUpload();
  });

  $("#loader1").on('change',prepareLoad);
  var files;
  function prepareLoad(event)
  {
      console.log(' event fired'+event.target.files[0].name);
      files=event.target.files;
  }
  function processFileUpload()
  {
      console.log("fileupload clicked");
      var oMyForm = new FormData();
      oMyForm.append("file", files[0]);
     $.ajax({dataType : 'json',
            url : "${pageContext.request.contextPath}/uploadMyFile",
            data : oMyForm,
            type : "POST",
            enctype: 'multipart/form-data',
            processData: false,
            contentType:false,
            success : function(result) {
            alert('success'+JSON.stringify(result));
            },
            error : function(result){
                alert('error'+JSON.stringify(result));
            }
        });
  }
});


$("#loader1").on('change',prepareLoad);
var files;
function prepareLoad(event)
{
    console.log(' event fired'+event.target.files[0].name);
    files=event.target.files;
}
</script>
</body>
</html>  