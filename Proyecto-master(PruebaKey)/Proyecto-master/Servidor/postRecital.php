<?php
include_once "Recital.php";
insertarRecital();

function insertarRecital(){

	
	if (isset($_POST['AgregarRecital'])) {
		
		$jsonStr = urldecode($_POST['AgregarRecital']);
		$jsonObj = json_decode($jsonStr);
		$nombre_art = $jsonObj->{'artista'};
		$genero = $jsonObj->{'genero'};
		$fecha = $jsonObj->{'fecha'};
		$ciudad = $jsonObj->{'ciudad'};
		$date = new DateTime($fecha);
		$fecha =$date->format('Y-m-d');
		
		
		if ($nombre_art != null && $genero != null && $fecha != null && $ciudad != null) {
				$rec=new Recital();
				$resultado=$rec->insertRecitalBD($nombre_art, $genero, $fecha, $ciudad);
				echo "Recital creado";
		} else {
			echo "KO: Recital Object not received";
		}
	}
	else{
		echo "KO: Nada para hacer";
	}
	
}

	



?>