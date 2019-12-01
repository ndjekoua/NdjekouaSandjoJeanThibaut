$(document).ready(function() {
	
	
	var dt = new Date();
    dt.setSeconds(dt.getSeconds() + 60);
    document.cookie = "cookietest=1; expires=" + dt.toGMTString();
    var cookiesEnabled = document.cookie.indexOf("cookietest=") != -1;
    if(!cookiesEnabled){
    	location.replace("blockNavigation.php");
    }else{
	
	  //alert("cookies enabled");
	  
	 var selectedSeats = [];
	$('.free').click(function(){
		 var myClass = $(this).attr("class");
		 var myId= $(this).attr("id");
		 var obj;
		
			// alert("Old class "+myClass+" new one "+" Reserved");
			  //the user is performing a reservation
			  selectedSeats[myId] = "reserved"; //add the seat to the list of seats for this user
		
			 // alert("sent status "+myClass);
			    //AJAX call
			  $.ajax({
	                url: 'updateDb.php',
	                type: 'get',
	                data: { id:myId, status:myClass },
	                dataType: 'JSON',
	                context:this,
	                success: function(response){
	                	
	                	/* alert(response.status);*/
	                	// alert("received one "+response.status);
	                	    if(response.timeOut==true){
	                	    	 //alert("login.php");         
	                	         location.replace("login.php");	
	                	     }
	                	    else{
	                	            $(this).attr("class",response.status);
	                	            alert(response.resultMessage);
	                	    }
	                    	                   
	                }, complete : function(response){
	                	if(status=="reservebyme"){selectedSeats[myId] = "reserved"; }//add the seat to the list of seats for this user
	                	if(status=="free"){delete selectedSeats[myId] ; }//the user decided to remove this from his set
	                }
	            }); 
	});
	
	$('.sold').click(function(){
		
		alert("ERROR: this seat is already sold");
	});
	
	$('.reserved').click(function(){
		var myClass = $(this).attr("class");
		 var myId= $(this).attr("id");
		 var obj;
		
			
			    //AJAX call
			  $.ajax({
	                url: 'updateDb.php',
	                type: 'get',
	                data: { id:myId, status:myClass },
	                dataType: 'JSON',
	                context:this,
	                success: function(response){
	                	
	                	if(response.timeOut==true){
               	    	    //alert("login.php");         
               	           location.replace("login.php");	
               	        }
	                	 $(this).attr("class",response.status);
	                	 alert(response.resultMessage);	
	                	 
	                }, complete : function(response){
	                	if(status=="reservebyme"){selectedSeats[myId] = "reserved"; }//add the seat to the list of seats for this user
	                	if(status=="free"){delete selectedSeats[myId] ; }//the user decided to remove this from his set
	                }
	            }); 
	});
	
	$('.reservedbyme').click(function(){
		var myClass = $(this).attr("class");
		 var myId= $(this).attr("id");
		 var obj;
		
			 
			    //AJAX call
			  $.ajax({
	                url: 'updateDb.php',
	                type: 'get',
	                data: { id:myId, status:myClass },
	                dataType: 'JSON',
	                context:this,
	                success: function(response){
	                	 
	                	if(response.timeOut==true){
               	    	   // alert("login.php");         
               	            location.replace("login.php");	
               	        }
	                	
	                	 $(this).attr("class",response.status);
	                	 alert(response.resultMessage);	                   
	                }, complete : function(response){
	                	if(status=="reservebyme"){selectedSeats[myId] = "reserved"; }//add the seat to the list of seats for this user
	                	if(status=="free"){delete selectedSeats[myId] ; }//the user decided to remove this from his set
	                }
	            }); 
	});
	
	$('#buy').click(function(){
		 var selectedSeats = [];
		
		$('tr td').each(function(index) { 
			 var myClass= $(this).attr("class"); 
			 var myId= $(this).attr("id");
			
			  if(myClass == "reservedbyme"){
				  selectedSeats.push(myId);
			  }
		});
		
	        if(selectedSeats.length <=0){//user pressed "buy" without selecting any seat
			      alert("you should select at least one seat in order to perform a buy operation!!");
			     //event.preventDefault();
	       }else{
	    	  
		     $.ajax({
	               url: 'buy.php',
	               type: 'get',
	               data: {'seats':JSON.stringify(selectedSeats)},
	               dataType: 'JSON',
	               //contentType: 'application/json',
	               context:this,
	               //error: function(reponse){ alert("response "+response.result);},
	               success: function(response){
	            	    
	            	   
	            	   if(response.timeOut==true){
               	    	   // alert("login.php");         
               	            location.replace("login.php");	
               	        }
	            	     
	            	     if(response.result==true){
	            	    	 alert("Succesfully completed buy operation");
	            	     }else{
	            	    	 alert("ERROR: at least one seat among the chosen ones was already sold or you was not the owner of the reservation, Please retry!!");
	            	     }
	            	     //alert(response.id);
	                }, complete : function(response){
	                	        //update the page by reloading it contains from the sever.
	        	               location.reload(true);
	        	
	                    }
	          }); 
	    	   
	    	  //location.replace("buy.php?seats="+selectedSeats);
	       }
	    });
}
	
});

