<?php
 
if ($_SERVER['REQUEST_METHOD']=='POST'){
 
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $amt_cigarette = $_POST['amt_cigarette'];
    $price_cigarette = $_POST['price_cigarette'];
 
    $password = password_hash($password, PASSWORD_DEFAULT);

    $conn = mysqli_connect("localhost", "root", "", "stopsmoking");
 
    $sql = "INSERT INTO user (name, email, password, amt_cigarette, price_cigarette) VALUES ('$name', '$email', '$password', '$amt_cigarette', '$price_cigarette')";
 
    if (mysqli_query($conn, $sql)) {
   
        echo "Success! Congratulation your account has been register!";
        mysqli_close($conn);

    }else{

        echo "Fail to registration your account!";
        mysqli_close($conn);

    }
 
}
?>