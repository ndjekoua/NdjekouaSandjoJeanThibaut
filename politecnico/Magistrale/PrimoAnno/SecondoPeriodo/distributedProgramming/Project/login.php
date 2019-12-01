<?php // Example 26-7: login.php
   require_once 'functions.php';
   httpsRedirect();
   session_start();
  
echo <<<_BEGIN
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
                    if (!isNaN(character * 1)){totNumeric++;}
                    else{
                        if (character == character.toUpperCase()) {totUpper++;}
                        if (character == character.toLowerCase()){totLower++;}
                     }
                     i++;
            }
            if( totLower==0 || (totUpper==0  && totNumeric==0) || password.length<2){
               alert("wrong credentials, please retry!!");
               event.preventDefault();
            }
	       
		});
   });
  </script >
  </head>
    <noscript style="color:blue;float:left;font-size: large;font-weight: bold;padding-left: 100px;padding-top: 20x;">Javascript is not enabled on your browser: the application could not work properly
            <style type="text/css">
		     .formcontainer { display:none; }
	         </style> 
           </noscript>
  <body><div class="formcontainer"> 
_BEGIN;
  $error = $user = $pass = "";
  if (isset($_SESSION['user'])) destroySession();
  if (isset($_POST['user']))
  {
  	  $conn= connectDB();
     $user = sanitizeString($_POST['user'],$conn);
     $pass = $_POST['pass'];
     $result=checkUserName($user);
     $result2=checkPassword($pass);
      if($result==false || $user == "" || $pass == "" || $result2==false){
       	 echo "<p> you have inserted wrong credentials. please click to the following link to retry Login&nbsp";
       	 echo '<a href="index.php"> Go Back</a> &nbsp</p> </div></body></html>';
       	 die();
       }
    else
    {
        $md5Password =md5($pass);    	
        $result =genericQuery("SELECT * FROM users
        WHERE userName='$user' AND password='$md5Password'",$conn);

      if (mysqli_num_rows($result) == 0){
       	 echo "<p> wrong userName and password, please click on the following link to retry &nbsp";
       	 echo '<a href="index.php"> Go Back</a> &nbsp</p></div></body></html>';
       	 die();
       } 
      else
      {
      	$row = mysqli_fetch_assoc($result);
        $_SESSION['user'] = $user;
        $_SESSION['time']=time();
        $_SESSION['id'] = $row['id'];
        session_write_close();
        die("<div class='center'>You are now logged in. Please
             <a data-transition='slide' href='index.php'>click here</a>
             to continue.</div></div></body></html>");
      }
    }
  }

echo <<<_END
   
      <form method='post' id="submit" action='login.php'>
        <div data-role='fieldcontain'>
          <label></label>
          Please enter your details to log in
        </div>
        <div data-role='fieldcontain'>
          <label>Username</label>
          <input type='email'  name='user' id="userName" placeholder="example@yahoo.fr">
        </div>
        <div data-role='fieldcontain'>
          <label>Password</label>
          <input type='password'  name='pass' id="password" placeholder="Enter Password">
        </div>
        <div data-role='fieldcontain'>
          <label></label>
          <input type='submit' value='Login' class="loginbtn">
        </div>
      </form>
    </div>
  </body>
</html>
_END;
  

?>
