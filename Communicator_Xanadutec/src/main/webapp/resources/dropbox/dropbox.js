
function uploadFile()

{
		var fileNames="";
		var nameOfFile=null;
		
		 var oMyForm = new FormData();
		  var inp = document.getElementById('loader1');
		  var flag=0;
		  for (var i = 0; i < inp.files.length; ++i) {
			  var name = inp.files.item(i).name;
			 
			 files[i]=inp.files.item(i).name;
			 
			flag=1;
		      oMyForm.append("file"+i, loader1.files[i]);
		      
			}
		  
		 // var desc= $("#description").val();
		
	
		   if(flag==0)
			   {
			     $("#failselect").slideDown();
			     $("#failselect").fadeOut(3000);
			     return false;
			   
			   }
		  
		  if(flag==1){
			  
				document.getElementById('light').style.display = 'block';
				document.getElementById('fade').style.display = 'block';
				
			  $.ajax({
				   	       dataType : 'json',
				           url : "uploadDropBoxFile",
				           data : oMyForm,
				           type : "POST",
				           enctype: 'multipart/form-data',
				           processData: false,
				           contentType:false,
				          success : function(result) {
				          nameOfFile=result;
				          fileNames=JSON.stringify(result);
				          fileNames = fileNames.slice(1, -1);
				          files=[];
				        
						   
						  // var name=result;
						   var abc= nameOfFile.split('#@$');
							  for(var i=0;i < abc.length-1;i++)
								 {
						   
										   $.ajax({
								      			url : "DropBoxFileDetail",
								      			type : "POST",
								      			data : {
								      				name:"" +abc[i]
								      				},
								      			success : function(data) {
								      				document.getElementById('light').style.display = 'none';
								      				document.getElementById('fade').style.display = 'none';
								      				location.reload(true);
								      			},
								      			error : function(xhr, ajaxOptions, thrownError) {
								      				console.log("err" + thrownError + xhr.responseText);
				
								      				return false;
								      			}
								      		});
								 }
						   
					
						   
		          },
	          error : function(result){
	             console.log('error'+JSON.stringify(result));
	             return false;
	          } 
		          
			  });
			  //DropBoxFileDetail
		  }

		  
		
}

function uploadDocumentFile()

{
	
		
		
		var fileNames="";
		var nameOfFile=null;
		 var oMyForm = new FormData();
		  var inp = document.getElementById('loader11');
		  var flag=0;
		  for (var i = 0; i < inp.files.length; ++i) {
			  var name = inp.files.item(i).name;
			 
			 files[i]=inp.files.item(i).name;
			 
			flag=1;
		      oMyForm.append("file"+i, loader11.files[i]);
		      
			}
		  
		  
		   if(flag==0)
			   {
			     $("#failselect").slideDown();
			     $("#failselect").fadeOut(3000);
			     return false;
			   
			   }
		  
		
		    if(flag==1){
		    	document.getElementById('light').style.display = 'block';
				document.getElementById('fade').style.display = 'block';
			  $.ajax({
				   	       dataType : 'json',
				           url : "uploadDocumentFile",
				           data : oMyForm,
				           type : "POST",
				           enctype: 'multipart/form-data',
				           processData: false,
				           contentType:false,
				          success : function(result) {
				        	  nameOfFile=result;
				          fileNames=JSON.stringify(result);
				          fileNames = fileNames.slice(1, -1);
				          files=[];
				        
						  // console.log('file name is'+result);
						   //var name=result;
				          
				          var abc= nameOfFile.split('#@$');
						  for(var i=0;i < abc.length-1;i++)
							 {
					   
								   $.ajax({
						      			url : "DocumentFileDetail",
						      			type : "POST",
						      			data : {
						      				name:"" +abc[i] 
						      				
						      				},
						      			success : function(data) {
						      			
						      				document.getElementById('light').style.display = 'none';
						    				document.getElementById('fade').style.display = 'none';
						      				location.reload(true);
						      			},
						      			error : function(xhr, ajaxOptions, thrownError) {
						      				console.log("err" + thrownError + xhr.responseText);
		
						      				return false;
						      			}
						      		});
						  
							 }
					
						   
		          },
	          error : function(result){
	             console.log('error'+JSON.stringify(result));
	             return false;
	          } 
		          
			  });
			  //DocumentFileDetails
		  }

		  
		
}


