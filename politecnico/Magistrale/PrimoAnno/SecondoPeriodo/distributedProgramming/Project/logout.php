<?php // Example 26-12: logout.php
  require_once 'functions.php';
  httpsRedirect();
  //testCookie();
  session_start();
 
  
echo <<<_BEGIN
<!DOCTYPE html> 
<html>
  <head>
    <meta charset='utf-8'>
    <link rel='stylesheet' href='mystyle.css' type='text/css'>
    <script src='javascript.js'></script>
    <script src='jquery.js'></script>
   </head>
   <noscript style="color:blue;float:left;font-size: large;font-weight: bold;padding-left: 100px;padding-top: 20x;">Javascript is not enabled on your browser: the application could not work properly
            <style type="text/css">
		     .logoutcontainer { display:none; }
	         </style> 
    </noscript>
  <body> <div class='logoutcontainer'>
_BEGIN;

  if (isset($_SESSION['user']))
  {
    logout();
    echo "<br>You have been correctly logged out. Please
         <a data-transition='slide' href='index.php'>click here</a>
         to refresh the screen.</div></body></html>";
  }
  else echo "<div class='center'>You cannot log out because
             you are not logged in</div></body></html>";
?>