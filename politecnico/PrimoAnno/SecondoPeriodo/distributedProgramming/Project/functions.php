<?php // Example 26-1: functions.php
require_once 'dbController.php';
define("rows", 10);
define("column",6);
define("free", 0);
define("reserved", 1);
define("sold", 2);
$i=$j=$totFree=$totReserved=0;
$totSold=$totReservedByMe=0;
$array = array("0"=>"A","1"=>"B","2"=>"C","3"=>"D","4"=>"E","5"=>"F",
               "6"=>"G","7"=>"H","8"=>"I","9"=>"J","10"=>"K","11"=>"L",
                "12"=>"M","13"=>"N","14"=>"O","15"=>"P","16"=>"Q","17"=>"R",
                "18"=>"S","19"=>"T","20"=>"U","21"=>"V","22"=>"W","23"=>"X","24"=>"Y","25"=>"Z"); 

   //destry the session associated to this user
  function destroySession()
  {
    $_SESSION=array();

    if (session_id() != "" || isset($_COOKIE[session_name()]))
      setcookie(session_name(), '', time()-2592000, '/');

    session_destroy();
  }

  //prevent all INJECTIONS:SQL,HTML,script
  function sanitizeString($var,$conn)
  {
    
    $var = strip_tags($var);
    $var = htmlentities($var);
    if (get_magic_quotes_gpc())
      $var = stripslashes($var);
    return mysqli_real_escape_string($conn,$var);
  }

  
  //query the DB and return the status of a seat
  function getStatus($id)
  {
  	$conn =connectDB();
  	$returnStatus="";
  	$sql = "SELECT * FROM seats WHERE id='$id'";
    $result=genericQuery($sql,$conn);
    
         //the seat is not yet in the DB so it's status should b free
        if (mysqli_num_rows($result) == 0){ $returnStatus="free"; }
          $row = mysqli_fetch_assoc($result);   
            
          //check if the seat is reserved in the name of the current user
        if(isset($_SESSION['id'])){
        	  if($row['userId'] == $_SESSION['id'] && $row['status']=="reserved"){
        	  	  $returnStatus="reservedbyme";
        	  }
        }
        
        //if true, i return the DB status which could be either RESERVED or SOLD
        if($returnStatus==""){
        	$returnStatus=$row['status'];
        }
    closeConnection($conn);
    return $returnStatus;
  }
  
  //update the global variables according to the state of the seat
  function updateTotal($status){
  	if($status=="free"){$GLOBALS['totFree']++;}
    if($status=="reserved"){$GLOBALS['totReserved']++;}
    if($status=="sold"){$GLOBALS['totSold']++;}
    if($status=="reservedbyme"){$GLOBALS['totReservedByMe']++;}
  }
  function initizializeTotal(){
  	$GLOBALS['totFree']=0;
  	$GLOBALS['totSold']=0;
  	$GLOBALS['totReserved']=0;
  	$GLOBALS['totReservedByMe']=0;
  }
  
  
  //print the map of the seat colorating each with the correct status
  function printMapSeat(){
echo <<<_index
<div id="description">
 <h2> EXAM JUNE 2019 </h2>
</div>
<table class="table">
_index;
//first i print the header of the table
echo "<thead> <tr>";
echo "<td></td>";//this for presentation style 
   for($i=0;$i<column;$i++){
	  echo "<td>".$GLOBALS['array'][$i]."</td>";
   }
echo "</tr></thead>";

//content of the table
echo"<tbody>";
  initizializeTotal();
 for($i=1;$i<rows+1;$i++){
 	echo "<tr><td>".$i."</td>";
 	for($j=1;$j<column+1;$j++){
 		$id= $i."_".($j);
 		//$status=getStatus($id);
 		
 		$status= getStatus($id);
 		 updateTotal($status);
 		  echo '<td id='.$id.' class= '.$status.' ></td>';
 		   
 		  if($status==reserved){}  
 	}
 	echo "</tr>";
 }
      $totSeats=$GLOBALS['totSold']+$GLOBALS['totFree']+$GLOBALS['totReserved']+$GLOBALS['totReservedByMe'];
echo '<nav id="sidediv">
    <h4> here is a summary about the current situation:</h4>
    <p>Total number of free seats: '.$GLOBALS['totFree'].
   '<p>Total number reserved seats: '.$GLOBALS['totReserved'].
   '<p>Total number of sold seats: '.$GLOBALS['totSold'].
   '<p>Total number you reserved: '.$GLOBALS['totReservedByMe'].
   '<p>Total number of seats: '.$totSeats;
  }
  
  //checks that the username is  valid email address
  function checkUSerName($username){
  	return filter_var($username, FILTER_VALIDATE_EMAIL) && strlen($username)<=255 && htmlentities($username)==$username;
  }
  

  
// Does the logout, destroying cookies and session
function logout(){
	if (ini_get("session.use_cookies")) {
        $params = session_get_cookie_params();
        setcookie(session_name(), '', time() - 3600*24,
            $params["path"], $params["domain"],
            $params["secure"], $params["httponly"]
        );
    }
	session_destroy();
}


// REDIRECTION FUNCTIONS
// Redirection to $location
function redirect($location){
	header('Location: '.$location);
	exit();	
}


// AUTHENTICATION
// Checks if the user authentication is still valid.
// An authentication is valid if it has last less than 2 
// minutes since the last page load
function checkTime(){
	$diff=time()-$_SESSION['time'];
	if($diff>2*60){
		logout();	
		redirect('login.php?msg=timeout');
	}
	$_SESSION['time']=time();	
}

function checkTimeBack(){
	$diff=time()-$_SESSION['time'];
	if($diff>2*60){
		//logout();	
		//redirect('login.php?msg=timeout');
		return TRUE;
	}
	$_SESSION['time']=time();	
	return false;
}



  //check the input password correspond to the requested pattern
  function checkPassword($pass){
           $totUpper=0;
           $totLower=0;
           $totNumeric=0;
           $i=0;
           $character='';
            while ($i < strlen($pass) ){
              $character = $pass[$i];
             
                    if (ctype_digit($character)){$totNumeric++;}
                    else{
                        if (ctype_upper($character)) {$totUpper++;}
                        if (ctype_lower($character)){$totLower++;}
                     }
                     $i++;
            }
            
             if($totLower==0 || ($totUpper==0 && $totNumeric==0) || strlen($pass)<2){
                return false;
             }
             return true;
  }
  
  // Redirect using HTTPS
function httpsRedirect(){
	if(!isset($_SERVER['HTTPS']) || $_SERVER['HTTPS']==='off'){
		$redirect_url = "https://" . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI'];
	    header("Location: $redirect_url");
	    exit();
	}	
}


//receives as parameter a seatId and check that line and colunm are in the boudaries rows and column
function checkIdBoundary($id){
	$vett=explode("_",$id);
	
	//they star from 1,not from 0
    $line=$vett[0];
   	$column= $vett[1];
	
   	if($line > rows || $column > column){
   		return false;
   	}
   	else{
   		return true;
   	}
}

?>
