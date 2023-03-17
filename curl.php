<?php
$query=$_GET['query'];
$query=rawurlencode($query);
    $url = 'https://api.api.ai/api/query?v=20150910&query='.$query.'&lang=en&sessionId=79b4c9c5-af0d-4575-9d2f-73bd146c1f3f&timezone=2017-02-08T12:22:13+0530';
    //echo $url;
    $request_headers = array();
        
        $request_headers[] = 'Authorization:Bearer e9dd1eec04ed49628a6c7793266dc6d9';
//  Initiate curl
$ch = curl_init();
// Disable SSL verification
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
// Will return the response, if false it print the response
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
// Set the url
curl_setopt($ch, CURLOPT_URL,$url);
curl_setopt($ch, CURLOPT_HTTPHEADER,$request_headers);
// Execute
$result=curl_exec($ch);
// Closing
curl_close($ch);
echo urldecode(stripslashes($result));

// Will dump a beauty json :3
//var_dump($result);
?>