<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $email = $_POST['email'];
    $password = $_POST['password'];

    $dbc = mysqli_connect('localhost', 'root', '');
    mysqli_select_db($dbc, 'stopsmoking');

    $sql = "SELECT * FROM user WHERE email='$email' ";

    if ($r = mysqli_query($dbc, $sql)) {
		if (mysqli_num_rows($r) > 0) {
		//retrieve and print every record
			while ($row = mysqli_fetch_array($r)) {
				$response[0]['error'] = 0;
		$response[0]['message'] = "Welcome Login ";
		}
    }
		else {
            $response[0]['error'] = 1;
			$response[0]['message'] = "User not found.";
		}
		
	}
	else {
		$response[0]['error'] = 1;
		$response[0]['message'] = "Fail to display because " . mysqli_error($dbc) . "The query was: " . $query;
	}

	echo  json_encode($response);
}


?>
