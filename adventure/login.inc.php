<?php

$servername = 'localhost';
$username = 'root';
$passwort = '';
$db = 'adventure';

// Create Connection
$con = new mysqli($servername, $username, $passwort, $db);

// Check Connection
if($con->connect_error){
    return die('Connection failed: '. $con->connnect_error);
}

?>