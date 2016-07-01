<?php
$MY_SERVER = "localhost";
$MY_DB_USER = "root";
$MY_DB_PASS = "1234";
$MY_DB = "appAndroid";
include_once "Recital.php";
misRecitales();

function misRecitales(){
	if (isset($_POST['mRecital'])) {
		
		$jsonStr = urldecode($_POST['mRecital']);
		$jsonObj = json_decode($jsonStr);
		$idUsuario = $jsonObj->{'idU'};
		
	
		
		if ($idUsuario != null ) {
			$rect=new Recital();
			$idU=intval($idUsuario);
			$rect->obtenerMisRecitales($idU);
		}
		else
			echo "error al pasar parámetros";
	}
	else{
		echo"KO: Nada para hacer";
	}
}



?>