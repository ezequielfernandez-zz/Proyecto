<?php
include_once "Usuario.php";
agregarTelefono();

function agregarTelefono(){
	if (isset($_POST['Telefono'])) {
		$jsonStr = urldecode($_POST['Telefono']);
		$jsonObj = json_decode($jsonStr);
		$tel=$jsonObj->{'telefono'};
		$idU=$jsonObj->{'idU'};
	
		if($tel!= null && $idU!=null){
			$usu=new Usuario();
			$usu->postTelefono($tel, $idU);
			echo "OK";
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