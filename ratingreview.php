<?php

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['shop']) && isset($_POST['item']) && isset($_POST['station']) && isset($_POST['rating']) && isset($_POST['name']) && isset($_POST['review'])) {
    
    $shop = $_POST['shop'];
    $item = $_POST['item'];
    $station = $_POST['station'];
    $rating = $_POST['rating'];
    $name = $_POST['name'];
    $review = $_POST['review'];

    $response["shop"] = $shop;
    $response["item"] = $item;
    $response["station"] = $station;
    $response["rating"] = $rating;
    $response["name"] = $name;
    $response["review"] = $review;

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();
    $one = 1;

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO `foodexpress`.`rating_review`(`shop`,`item`,`rating`,`review`,`name`,`station`) VALUES('$shop', '$item', '$rating', '$review', '$name','$station')");
    $result2 = mysql_query("UPDATE shop_location SET `ratingsum` = (`ratingsum` + '$rating') WHERE `station` = '$station' AND `item` = '$item' AND `shop` = '$shop' ");
    $result3 = mysql_query("UPDATE shop_location SET `count` = (`count` + '$one') WHERE `station` = '$station' AND `item` = '$item' AND `shop` = '$shop' ");
    // check if row inserted or not
    if ($result && $result2 && $result3) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>