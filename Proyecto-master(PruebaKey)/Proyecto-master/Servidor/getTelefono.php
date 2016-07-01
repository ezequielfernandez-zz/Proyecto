<?php
include_once "Usuario.php";
obtenerTelefono();

function obtenerTelefono(){
	if (isset($_POST['Telefono'])) {
		$jsonStr = urldecode($_POST['Telefono']);
		$jsonObj = json_decode($jsonStr);
		$idU=$jsonObj->{'idU'};
	
		if($idU!=null){
			$usu=new Usuario();
			$usu->getTelefono($idU);
		}
		else{
			echo "error";
		}
	}
	else{
		echo "KO: nada para hacer";
	}
	
}

?>