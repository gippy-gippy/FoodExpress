<?php
include_once './db_connect.php';
 
if(isset($_POST['train']) && isset($_POST['id1']) && isset($_POST['id2'])){
    
    $db = new db_connect();
    // array for json response
    $response = array();
    $train = $_POST['train'];
    $response["categories"] = array();
     
    // Mysql select query
    $result = mysql_query("SELECT * FROM train_bogey WHERE train = '$train' ORDER BY bogey");
     
    while($row = mysql_fetch_array($result)){
        // temporary array to create single category
        $tmp = array();
        $tmp["id"] = $row["id"];
        $tmp["bogey"] = $row["bogey"];
        $tmp["offset"] = $row["offset"]; 
        // push category to final json array
        array_push($response["categories"], $tmp);
    }
} 
else{
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
}
// keeping response header to json
header('Content-Type: application/json');
// echoing json result
echo json_encode($response);

?>