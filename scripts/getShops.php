<?php
include_once './db_connect.php';
 
if(isset($_POST['bogey']) && isset($_POST['station']) && isset($_POST['item']) && isset($_POST['train'])){

    $db = new db_connect();
    // array for json response
    $response = array();
    $train = $_POST['train'];
    $bogey = $_POST['bogey'];
    $station = $_POST['station'];
    $item = $_POST['item'];
    $response["categories"] = array();
     
    // Mysql select query
    $result = mysql_query("SELECT * FROM shop_location WHERE station = '$station' AND item = '$item' ORDER BY shop");
    if($station!= "dummy"){
        $result2 = mysql_query("SELECT * FROM train_bogey WHERE train = '$train' AND bogey = '$bogey' ORDER BY bogey");
        $row2 = mysql_fetch_array($result2);
        $bogoff  = $row2["offset"];
    }
    //$tmp2 = array();
    //$tmp2["bogeyoffset"] = $row2["offset"]; 
     
    while($row = mysql_fetch_array($result)){
        // temporary array to create single category
        $tmp = array();
        $tmp["id"] = $row["id"];
        $tmp["shop"] = $row["shop"];
        $tmp["ratingsum"] = $row["ratingsum"];
        $tmp["count"] = $row["count"];
        $tmp["offset"] = $row["offset"];
        if(station != "dummy")
            $tmp["bogeyoffset"] = $row2["offset"];
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