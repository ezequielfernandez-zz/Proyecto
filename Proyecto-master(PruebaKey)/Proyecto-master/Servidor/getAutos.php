<?php
include_once "Auto.php";
obtenerAutos();
function obtenerAutos(){

	if (isset($_POST['Autos'])) {
		
		$jsonStr = urldecode($_POST['Autos']);
		$jsonObj = json_decode($jsonStr);
		$nombre_art = $jsonObj->{'artista'};
		$genero = $jsonObj->{'genero'};
		$fecha = $jsonObj->{'fecha'};
		$ciudad = $jsonObj->{'ciudad'};
		$date = new DateTime($fecha);
		$fecha =$date->format('Y-m-d');
		
		
		if ($nombre_art != null && $genero != null && $fecha != null && $ciudad != null) {
			$auto= new Auto();
			$auto->obtenerAutos($nombre_art, $ciudad, $genero, $fecha);
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