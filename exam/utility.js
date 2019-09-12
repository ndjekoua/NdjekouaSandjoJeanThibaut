
$(document).ready(function(){
	alert("ready document");
	$("button#checkOut").click(function(){
		var p1= document.getElementsByName("p1");
		var q1=document.getElementsByName("q1");
		var p2= document.getElementsByName("p2");
		var q2=document.getElementsByName("q2");
		var p3= document.getElementsByName("p3");
		var q3=document.getElementsByName("q3");
		alert(prova);
		if(!isNumeric(p1) |!isNumeric(q1) | !isNumeric(p2) | !isNumeric(q2) |!isNumeric(p3) | !isNumeric(q3)){
			alert("wrong input: pleasecheck your input data ");
		}
		
	});

/* JQuery code */});

function checkOut(){
	alert("ready document");
	
		var p1= document.getElementsByName("p1");
		var q1=document.getElementsByName("q1");
		var p2= document.getElementsByName("p2");
		var q2=document.getElementsByName("q2");
		var p3= document.getElementsByName("p3");
		var q3=document.getElementsByName("q3");
		alert(prova);
		if(!isNumeric(p1) |!isNumeric(q1) | !isNumeric(p2) | !isNumeric(q2) |!isNumeric(p3) | !isNumeric(q3)){
			alert("wrong input: pleasecheck your input data ");
		
      }
};		
    function isNumeric(n) {
	  return !isNaN(parseFloat(n)) && isFinite(n);
	};