<?php 

$intentName = $_GET['intentName'];
$parameter = $_GET['parameter'];
$query = $_GET['query'];

require_once('connectivity.php');

$sql = "SELECT speech,displayText,imageURL,websiteURL,location FROM chatbot  WHERE intentName='$intentName' AND parameter='$parameter' LIMIT 1";

function utf8ize($d) {
    if (is_array($d)) {
        foreach ($d as $k => $v) {
            $d[$k] = utf8ize($v);
        }
    } else if (is_string ($d)) {
        return utf8_encode($d);
    }
    return $d;
}
//$sql = "SELECT * FROM chatbot";
$r = mysqli_query($con,$sql);
$num_rows = mysqli_num_rows($r);
$result = array();

if($num_rows>0)
{

	while($row = mysqli_fetch_array($r)){
    	array_push($result,array(
        	'speech'=>$row['speech'],
        	'displayText'=>$row['displayText'],
        	'imageURL'=>$row['imageURL'],
        	'websiteURL'=>$row['websiteURL'],
        	'location'=>$row['location']
         
    	));


	}

	echo json_encode(utf8ize(array('result'=>$result)));

}
else
{
	$sql = "SELECT speech,displayText,imageURL,websiteURL,location FROM chatbot  WHERE intentName='Default Fallback Intent' AND parameter='' LIMIT 1";
	$r = mysqli_query($con,$sql);
	$num_rows = mysqli_num_rows($r);
	$result = array();

	while($row = mysqli_fetch_array($r)){
    	array_push($result,array(
        	'speech'=>$row['speech'],
        	'displayText'=>$row['displayText'],
        	'imageURL'=>$row['imageURL'],
        	'websiteURL'=>$row['websiteURL'],
        	'location'=>$row['location']
         
    	));


	}

    $sql1 = "INSERT INTO unanswered (query) values ('$query');";
    mysqli_query($con,$sql1);
	echo json_encode(utf8ize(array('result'=>$result)));
	
}




mysqli_close($con);
?>