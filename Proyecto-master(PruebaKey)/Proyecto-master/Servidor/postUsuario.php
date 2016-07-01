<?php

include_once 'Usuario.php';
insertarUsuario();
function insertarUsuario(){
	if (isset($_POST['Usuario'])) {
		
		$jsonStr = urldecode($_POST['Usuario']);
		$jsonObj = json_decode($jsonStr);
		$id=$jsonObj->{'id'};
		$nombre_usu = $jsonObj->{'nombre'};
		$apellido_usu = $jsonObj->{'apellido'};

		if ($nombre_usu != null && $apellido_usu != null &&  $id != null ) {
				$usu=new Usuario();
				$resultado=$usu->insertUsuarioBD($id, $nombre_usu, $apellido_usu);
				if($resultado == 1)
						echo "Usuario agregado";
			
		} else {
			echo "KO: Usuario Object not received";
		}
	}
	else
		echo "KO: Nada para hacer";
	
}



?> 	