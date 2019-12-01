<?php // Example 26-5: signup.php
  require_once 'functions.php';
  httpsRedirect();
  //testCookie();
  session_start();
  
echo <<<_END
<html>
<head>
 <script type="text/javascript" src="jquery.js"></script>
 <script type="text/javascript" src="javascript.js"></script>
<link href="mystyle.css" rel="stylesheet" />
  <script type="text/javascript">
  $(document).ready(function() {
	
   $("#submit").submit(function(event){
          password = document.getElementById("password").value;
          userName = document.getElementById("userName").value;
           var totUpper=0;
           var totLower=0;
           var totNumeric=0;
            var i=0;
            var character='';
            while (i < password.length){
              character = password.charAt(i);
                    if(!isNaN(parseInt(character))){
                         totNumeric++;
                    }
                    else{
                        if (character == character.toUpperCase()) {totUpper++;}
                        if (character == character.toLowerCase()){totLower++;}
                     }
                     i++;
            }
            
            //alert(totNumeric+" "+totLower+" "+totUpper);
            if( totLower==0 || (totUpper==0  && totNumeric==0) || password.length<2){
               alert("the password does not meet the requirement!!");
               event.preventDefault();
            }
	       
		});
   });
  </script >
  </head>
    <noscript style="color:blue;float:left;font-size: large;font-weight: bold;padding-left: 100px;padding-top: 20x;">Javascript is not enabled on your browser: the application could not work properly
            <style type="text/css">
		     .registercontainer { display:none; }
	         </style> 
    </noscript>
  <body><div class="registercontainer">  
_END;

  $error = $user = $pass = "";
  if (isset($_SESSION['user'])) destroySession();
     if (isset($_POST['user']))
     {
     	     $conn= connectDB();
             $user = sanitizeString($_POST['user'],$conn);
             $pass = $_POST['pass']; //useless to sanitize string since we will use MD5
             $result=checkUserName($user);
             $result2=checkPassword($pass);
       if($result==false || $user == "" || $pass == "" || $result2==false){
       	 echo "<p> you have inserted wrong credentials. please click to the following link to retry &nbsp";
       	 echo '<a href="index.php"> Go Back</a> &nbsp</p></div></body></html>';
       	 die();
       }    
       
       
       try {
       	         mysqli_autocommit($conn,false);
       	         
                $sql = "SELECT * FROM users WHERE userName='$user' FOR UPDATE";
                $result=genericQuery($sql,$conn);
              if (mysqli_num_rows($result) > 0){
                	 echo "<p> this userName already exist. please click to the following link to retry &nbsp";
                 	 echo '<a href="index.php"> Go Back</a> &nbsp</p></div></body></html>';
                 	  closeConnection($conn);
       	             die();
              } 
              else
              {        
                       $pass = md5($pass);
      	               $result=genericQuery("INSERT INTO users(userName,password) VALUES('$user', '$pass')",$conn);
                  if ($result){
                  	        mysqli_commit($conn);//save the changes to ensure persitency.
                  	        closeConnection($conn);
                           die("<p>you have been succesfully registered click &nbsp<a href=index.php>here</a>&nbspto continue to the Login</p></div></body></html>");
                           
                 } else {
                 	  throw new Exception("Error: INSERT INTO users VALUES('$user', '$pass')" ."<br>" . mysqli_error($conn));      
                 }
              }  
              
            
       } catch (Exception $e) {
       	   mysqli_rollback($conn);
       	   closeConnection($conn);
       }    
  }
  
echo <<<_END
      <form method='post' action="register.php" id="submit">
            <div class="container">
             <h1>Register</h1>
               <p>Please fill in this form to create an account.</p>
              <hr>

            <label for="email"><b>Email</b></label>
            <input type="email" id= 'userName' placeholder="example@yahoo.fr" name="user" required>

            <label for="psw"><b>Password</b></label>
            <input type="password" id='password' placeholder="Enter Password" name="pass" pattern="^(?=.*[a-z])((?=.*\d)|(?=.*[A-Z])).{2,}" title="at least one Lower case letter, with one Upper case letter or a numeric value" required>


          <input type="submit" class="registerbtn" value="Register">
           </div>
  
            <div class="container signin">
            <p>Already have an account? <a href="login.php">Login</a>.</p>
          </div>
     </form>
   </div>
  </body>
 </html>    

_END
?>
