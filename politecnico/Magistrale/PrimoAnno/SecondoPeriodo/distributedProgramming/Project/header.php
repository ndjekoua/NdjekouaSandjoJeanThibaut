<?php // Example 26-2: header.php
  require_once 'functions.php';
   httpsRedirect();
  // testCookie();
   session_start();
   
  //logged in user
  if (isset($_SESSION['user']))
  {
  	httpsRedirect();
    $loggedin = TRUE;

  }
  else $loggedin = FALSE;

  if ($loggedin)
  {
  	checkTime();
echo <<<_LOGGEDINFIRSTPART
  
     <!DOCTYPE html> 
      <html>
       <body> 
         <head>
          <script type="text/javascript" src="jquery.js"></script>
           <script type="text/javascript" src="javascript.js"></script>
           <link href="mystyle.css" rel="stylesheet" /><base>
        </head>
        <noscript style="color:blue;float:left;font-size: large;font-weight: bold;padding-left: 100px;padding-top: 20x;">Javascript is not enabled on your browser: the application could not work properly
            <style type="text/css">
		     .container { display:none; }
	         </style> 
           </noscript>
       <div class="container"> 
             
_LOGGEDINFIRSTPART;
   printMapSeat();//there is the last div to be closed!!
echo <<<_LOGGEDINSECONDPART
          <br><br>
          <a data-role='button' data-inline='true' data-icon='home'
            data-transition="slide" href='index.php'>Home</a><br><br>
          <a data-role='button' data-inline='true' data-icon='action'
            data-transition="slide" href='logout.php'>Log out</a><br><br>
          <a data-role='button' data-inline='true' data-icon='action'
            data-transition="slide" href='index.php'>Update</a><br><br>
            <input type="button" value="Buy" class="buy" id="buy"></input><br><br>
        </nav>
       </div>  
       </body>
      </html>  
_LOGGEDINSECONDPART;
  }
  else
  {
echo <<<_GUESTFIRSTPART

     <!DOCTYPE html> 
      <html>
       <body> 
       <head>
          <link href="mystyle.css" rel="stylesheet" />
          <script type="text/javascript" src="jquery.js"> </script>
           <script type="text/javascript">
           $(document).ready(function() {
             var dt = new Date();
                  dt.setSeconds(dt.getSeconds() + 60);
                  document.cookie = "cookietest=1; expires=" + dt.toGMTString();
                  var cookiesEnabled = document.cookie.indexOf("cookietest=") != -1;
                  if(!cookiesEnabled)
                    	location.replace("blockNavigation.php");
             });       	
           </script>
       </head>
       <noscript style="color:blue;float:left;font-size: large;font-weight: bold;padding-left: 100px;padding-top: 20x;">Javascript is not enabled on your browser: the application could not work properly
            <style type="text/css">
		     #container { display:none; }
	         </style> 
           </noscript>
       <div class="container" id="container">    
        
_GUESTFIRSTPART;
 printMapSeat();
echo <<<_GUESTSECONDPART
    <br><br>
    <a data-role='button' data-inline='true' data-icon='home'
            data-transition='slide' href='index.php'>Home</a>&nbsp&nbsp
          <a data-role='button' data-inline='true' data-icon='plus'
            data-transition="slide" href='register.php'>Register</a>&nbsp&nbsp
          <a data-role='button' data-inline='true' data-icon='check'
            data-transition="slide" href='login.php'>Log In</a>
        </div>
       </div>
     </body>
    </html>  
_GUESTSECONDPART;
  }
?>

