<?php 
 require_once 'functions.php';
 //httpsRedirect();
 session_start();
 $timeOut=checkTimeBack();
 $id =$_GET["id"];
 $status = $_GET['status'];
$conn = connectDB();
$id=sanitizeString($id, $conn);
$status=sanitizeString($status, $conn);
$currentUserId=$_SESSION['id'];
 session_write_close();
 $resultMessage="OK";//will contains desciption to be diplayed to the user as explantion of what happened 
 $returnstatus="free";//will contain the status to return to the user
 
 
    //check inactivity
    
      if($timeOut==TRUE){
      	 $return_arr = array('timeOut'=>$timeOut);
         echo json_encode($return_arr);  
         die();
      }
      
      //check id is valid one: rows>id.column and colunm >id.column
      
        $isIdValid= checkIdBoundary($id);
        if ($isIdValid==false){
        	//this should be a mallicious user so i do nothing  
            die();
        }
     
      try {
        
        mysqli_autocommit($conn,false);
          $result =genericQuery("SELECT * FROM seats WHERE id='$id' FOR UPDATE ",$conn);
          
           
          if(mysqli_num_rows($result)==0){//the seat is not yet inserted in the DB so it's free
   	           $dbStatus = "free";
   	          
           }else{
           	    $row = mysqli_fetch_assoc($result);
                $dbStatus=$row["status"];
                $userId=$row["userId"];
           }
             
               //i freet the prviously allocated momory
               freeResult($result);
            
            
                 if($status=="sold"){//this could never happen because the user cannot send a request related to a sold seat
                 	  $resultMessage="ERROR: this seat was already sold";
                 	  $returnstatus="error";
                 	  //$return_arr = array('id'=>$id,'status'=>"error",'userId'=>$currentUserId,'resultMessage'=>$resultMessage);
                 }
                 
                 
                 //user is currently seing the seat as reserved by him: YELLOW
                 if($status=="reservedbyme"){//the user want to free a reservation
                 	//$currentUserId =$_SESSION["id"];
                 	  if($dbStatus=="reserved" && $userId == $currentUserId ){
                 	  	//everything is good because the reservation was at user's name and now we cancel it
                 	  	 // $sql = "UPDATE `seats` SET `status` = 'free',`userId`= '' WHERE id='$id'";
                 	  	 $sql = "DELETE FROM `seats` WHERE id = '$id'";
                 	  	 $result=genericQuery($sql, $conn);
                 	      if( ! $result){
                 	  	  	die("ERROR executing the query "+$sql);
                 	  	  }
                 	  	 $resultMessage="The reservation has been succesfully cancelled";
                 	  	 $returnstatus="free";
                 	  	 
                 	  	// $return_arr = array('id'=>$id,'status'=>"free",'userId'=>$currentUserId,'resultMessage'=>$resultMessage);
                        // echo json_encode($return_arr);
                 	  }
                 	  else{
                 	  	     $resultMessage="The reservation has been succesfully cancelled, but you did not own it anymore!!";
                 	  	     $returnstatus=$dbStatus;
                 	  	    // $return_arr = array('id'=>$id,'status'=>$dbStatus,'userId'=>$currentUserId);
                            // echo json_encode($return_arr);  
                 	  }
                 }
                 
                 //user is currently seing the seat as free : GREEN
                 if($status=="free"){
                 	 if($dbStatus=="reserved"){//the seat is reserved but i cancel and put it to this user
                 	 	 
                 	 	 $sql = "UPDATE `seats` SET `status` = 'reserved',`userId` = '$currentUserId' WHERE id='$id'";
                 	  	 $result=genericQuery($sql, $conn);
                 	  	  if( ! $result){
                 	  	  	die("ERROR executing the query "+$sql);
                 	  	  }
                 	  	 $resultMessage="The reservation has been done succesfully";
                 	  	 $returnstatus="reservedbyme";
                 	  	  
                 	  	// $return_arr = array('id'=>$id,'status'=>"reservedbyme",'userId'=>$currentUserId,'resultMessage'=>$resultMessage);
                         //echo json_encode($return_arr);
                 	 }
                 	 else{
						          if($dbStatus=="free"){//the seat is frea so the user can reserve it
								        $userId = null;
   	                                    $vett=explode("_",$id);
                 	 	                $i=$vett[0];
   	                                    $j= $vett[1];
   	                                $result=genericQuery("INSERT INTO seats(id,line,colonna,status,userId) VALUES('$id','$i','$j','reserved','$currentUserId')",$conn);
                 	 	              //$sql = "UPDATE `seats` SET `status` = 'reserved',`userId` = '$currentUserId' WHERE id='$id'";
                 	  	              //$result=genericQuery($sql, $conn);
						                if( ! $result){
                 	  	  	              die("ERROR executing the query "+" INSERT INTO seats(id,line,colonna,status,userId) VALUES('$id','$i','$j','reserved','$currentUserId')");
                 	  	                }
                 	  	              $resultMessage="The reservation has been done succesfully";
                 	               	 $returnstatus="reservedbyme";
                 	              }
								  else{
                 	 	                   $resultMessage="The reservation cannot be done because this seat is already sold";
                 	 	                   $returnstatus=$dbStatus;
								  }		   
                 	 	    // $return_arr = array('id'=>$id,'status'=>$dbStatus,'userId'=>$currentUserId,'resultMessage'=>$resultMessage);
                            // echo json_encode($return_arr);  
                 	 }
					 
					
                 }
                 
                 // Uer is currently seing the seat as reserved by someone else: ORANGE
                 if($status=="reserved"){
                 	 if($dbStatus != "sold"){
                 	 	//$currentUserId=$_SESSION['id'];
                 	 	 $sql = "UPDATE `seats` SET `status` ='reserved',`userId` = '$currentUserId' WHERE id='$id'";
                 	  	 $result=genericQuery($sql, $conn);
                 	       if( ! $result){
                 	  	    	die("ERROR executing the query "+$sql);
                 	  	   }
                 	  	 $resultMessage="The reservation has been done succesfully";
                 	  	 $returnstatus="reservedbyme";                 	  	
                 	  	 // $return_arr = array('id'=>$id,'status'=>"reservedbyme",'userId'=>$currentUserId,'resultMessage'=>$resultMessage);
                        // echo json_encode($return_arr);
                 	 }
                 	 else{
                 	 	     $resultMessage="The reservation cannot be done because this seat is already sold";
                 	 	     $returnstatus=$dbStatus;
                 	 	    // $return_arr = array('id'=>$id,'status'=>$dbStatus,'userId'=>$currentUserId,'resultMessage'=>$resultMessage);
                            // echo json_encode($return_arr);  
                 	 }
                 }
                 
          mysqli_commit($conn);
     } catch (Exception $e) {
               mysqli_rollback($conn);
               $returnstatus= $status;
               $resultMessage="Rollback :unexpected error on DB side";
           // echo "Rollback ".$e->getMessage();
      }

      closeConnection($conn);
      $return_arr = array('id'=>$id,'status'=>$returnstatus,'userId'=>$currentUserId,'resultMessage'=>$resultMessage);
      echo json_encode($return_arr);  

?>