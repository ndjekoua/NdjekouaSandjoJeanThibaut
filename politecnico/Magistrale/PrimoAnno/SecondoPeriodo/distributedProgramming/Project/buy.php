<?php 
require_once 'functions.php';
 httpsRedirect();
 session_start();
 $timeOut=checkTimeBack();
 //$param =explode(",", $_GET['seats']);
$param = json_decode($_GET['seats']);
$conn = connectDB();
$returnResult = TRUE;  
$length= count($param);
  $currentUserId =$_SESSION["id"];
  session_write_close();  

      //check inactivity
    
     if($timeOut==TRUE){
      	 $return_arr = array('timeOut'=>$timeOut);
         echo json_encode($return_arr);  
         die();
      }
      
     try {
        //autocommit($conn, false);
        mysqli_autocommit($conn,false);
          $result =genericQuery("SELECT * FROM seats FOR UPDATE ",$conn);
         // echo mysqli_num_rows($result);
          if(mysqli_num_rows($result)<=0){
          	   $returnResult=false;
   	           throw new Exception("command 1 failed");
           }
                
                //put all the returned result in an array
                
                  $data = array(); 
                  $index = 0;
                while($row = mysqli_fetch_assoc($result)) {
              	    $data[$index++]=$row;     
                }
                 freeResult($result);
                        
      	      for($i=0;$i<$length;$i++){ 
      	      	     $seatId = sanitizeString($param[$i], $conn);
      	      	     $present = false;//if the seat is not present in the DB(FREE) the user can buy it
      	      	     
      	      	     //check for a given seat wether it's present or not and if it's possible to perform the buy
      	         	for($j=0;$j<$index;$j++){
      	         		
      	             //i first sanitize the received string: NEVER trust the user
              	       $id= $data[$j]['id'];
      	         	 
                       if($id == $seatId){ 
                    	   $present =true;
                           $status=$data[$j]["status"];
                           $userId=$data[$j]["userId"];  
                           
                          
                          if($status=="sold" || ($status == "reserved" && $userId != $currentUserId )){//this could never happen because the user cannot send a request related to a sold seat
                      	    //sleep(3);
                        	  $returnResult=false; 
                              throw new Exception("tried to buy a sold seat or without having a resrvation");
                          }
                      
                         
                           //if reach here, either the status is free or the reservervation is made by this user
                           
                            $sql = "UPDATE `seats` SET `status` = 'sold',`userId`= '$currentUserId' WHERE id='$seatId'";
                 	  	    genericQuery($sql, $conn);
                 	  	    
                 	  	    break;
                     } 
                }
                    
                    if($present == false){
                       //the seat is not present in DB(it's FREE) so the user can buy it!!!	
                    	 $vett=explode("_",$seatId);
                 	 	 $r=$vett[0];
   	                     $c= $vett[1];
                         $sql = "INSERT INTO seats(id,line,colonna,status,userId) VALUES('$seatId','$r','$c','sold','$currentUserId')";
                 	  	 genericQuery($sql, $conn);
                    }
              }

              
          mysqli_commit($conn);
          
       } catch (Exception $e) {
               mysqli_rollback($conn);
               
               //i free up all the seats previuosly reserved by this user.
               $sql = "DELETE FROM `seats` WHERE userId = '$currentUserId' AND status= 'reserved'";
                 	  	 $result=genericQuery($sql, $conn);
                 	  	 mysqli_commit($conn);
           // echo "Rollback ".$e->getMessage();
            
       }

      closeConnection($conn);
     
  
 $return_arr = array('result'=>$returnResult,'timeOut'=>$timeOut);
   echo json_encode($return_arr); 
?>