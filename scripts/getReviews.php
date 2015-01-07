<?php
include_once './db_connect.php';
 
if(isset($_POST['shop']) && isset($_POST['station']) && isset($_POST['item'])){

    $db = new db_connect();
    // array for json response
    $response = array();
    $shop = $_POST['shop'];
    $station = $_POST['station'];
    $item = $_POST['item'];
    $response["categories"] = array();
     
    // Mysql select query
    $result = mysql_query("SELECT * FROM rating_review WHERE station = '$station' AND item = '$item' AND shop = '$shop' AND review != '' ORDER BY id DESC");
     
    while($row = mysql_fetch_array($result)){
        // temporary array to create single category
        $tmp = array();
        $tmp["id"] = $row["id"];
        $tmp["rating"] = $row["rating"];
        $tmp["review"] = $row["review"];
        $tmp["name"] = $row["name"];
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