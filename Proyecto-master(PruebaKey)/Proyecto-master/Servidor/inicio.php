<?php

include_once "Recital.php";
mostrarRecitales();

function mostrarRecitales(){
	
	if (isset($_POST['Recital'])) {
		
		$jsonStr = urldecode($_POST['Recital']);
		$jsonObj = json_decode($jsonStr);
		$idU = $jsonObj->{'idU'};
		$gustos= $jsonObj->{'gustos'};
		$ubicacion= $jsonObj->{'ciudad'};
		
		
		
		if ($idU != null && $gustos!=null && $ubicacion!=null) {
				$rec=new Recital();
				$rec->obtenerRecitales($idU, $gustos, $ubicacion);
					echo "OK";
		}
	}
	else
		echo "KO: Nada para hacer";
	
}



	


?>