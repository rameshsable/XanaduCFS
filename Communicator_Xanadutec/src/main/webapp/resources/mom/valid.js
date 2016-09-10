function validateattendee(value) {

	alert(value);
	 if (/^([1-9])([0-9]){9}$/.test(val)) {
	    
	 } else {
		 var msg ="Enter numerals only in this field";
			document.getElementById("attend").innerHTML=msg;
		 
	    // alert("Invalid number; must be ten digits");
	     //number.focus()
	     return false;
	 }
}