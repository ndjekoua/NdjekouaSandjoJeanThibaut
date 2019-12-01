<?php 
   $servername = "localhost";
   $username = "root";
   $password = "";
   $db="s256770";
  
     
     
// DB CONNECTION
// Connects to the DB
    function connectDB(){
	   global $servername, $username, $password, $db;
         // Create connection
           $conn = mysqli_connect($servername, $username, $password,$db);

          // Check connection
           if (!$conn) {
             die("Connection failed: " . mysqli_connect_error());
           }
	  return $conn;
    }
    
  function createTable($name, $query)
  {
    queryMysql("CREATE TABLE IF NOT EXISTS $name($query)");
    echo "Table '$name' created or already exists.<br>";
  }

function freeResult($result){
 	 mysqli_free_result($result);
 }
 
  function genericQuery($query,$conn)
  {
    //global $conn;
    $result = mysqli_query($conn, $query);
     if(!$result){
     	die("query ".$query." failed!!");
     }
    return $result;
  }
  
 
  function closeConnection($conn){
  	//global $conn;
  	mysqli_close($conn);
  }
?>