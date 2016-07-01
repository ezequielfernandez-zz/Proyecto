<?php
include_once "Usuario.php";
insertarAsistencia();
function insertarAsistencia(){

	
	if (isset($_POST['Asiste'])) {
		
		$jsonStr = urldecode($_POST['Asiste']);
		$jsonObj = json_decode($jsonStr);
		$nombre_art = $jsonObj->{'artista'};
		$genero = $jsonObj->{'genero'};
		$fecha = $jsonObj->{'fecha'};
		$ciudad = $jsonObj->{'ciudad'};
		$date = new DateTime($fecha);
		$fecha =$date->format('Y-m-d');
		$idU =  $jsonObj->{'idU'};
		if ($nombre_art != null && $genero != null && $fecha != null && $ciudad != null && $idU != null) {
			$usu=new Usuario();
			$usu->asistirRecital($nombre_art, $genero, $fecha, $ciudad, $idU);
		}
	}
}
?>