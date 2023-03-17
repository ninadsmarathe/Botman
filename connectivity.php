<?php

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "chatbot_api";

@$con=mysqli_connect($servername,$username,$password);

if(@!$con || !mysqli_select_db($con,$dbname)){
	
	die("Could not connect");
} else{
}

?>