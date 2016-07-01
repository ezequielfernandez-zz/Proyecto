<?php
$MY_SERVER = "localhost";
$MY_DB_USER = "root";
$MY_DB_PASS = "1234";
$MY_DB = "appAndroid";
include_once "Recital.php";
include_once "Auto.php";

agregarAutoyRecital();

function agregarAutoyRecital(){
	//echo"está seteado? ".isset($_POST['Auto']);
	if (isset($_POST['Auto'])) {
		$jsonStr = urldecode($_POST['Auto']);
		$jsonObj = json_decode($jsonStr);
		$idU = $jsonObj->{'idU'};
		$ciudad= $jsonObj->{'ciudad'};
		$artista= $jsonObj->{'artista'};
		$genero= $jsonObj->{'genero'};
		$fecha= $jsonObj->{'fecha'};
		$modeloAut= $jsonObj->{'modelo'};
		$capacidadAut= $jsonObj->{'capacidad'};
		$patenteAut= $jsonObj->{'patente'};
		
		if ($idU != null && $ciudad!=null && $artista!=null && $genero!=null && $fecha!=null && $modeloAut!=null && $capacidadAut!=null && $patenteAut!=null) {
			agregarAutoyRecitalBD($idU,$ciudad,$artista,$genero,$fecha,$modeloAut,$capacidadAut,$patenteAut);
			echo "Se agregó el auto";
		}
		else{
			echo "error";
		}
		
	}
	else{
		echo "KO, nada para hacer";
	}
}
function agregarAutoyRecitalBD($idU,$ciudad,$artista,$genero,$fecha,$modeloAut,$capacidadAut,$patenteAut){
		$Recital=new Recital();
		$idR=$Recital->obtenerIDR($artista, $genero, $fecha, $ciudad);
		$aut=new Auto();
		$aut->insertarAutoBD($idU,$modeloAut,$capacidadAut, $patenteAut);
		if($idR!=null){
			echo "estoy acá";
			$aut->asociarAutoaRecital($idU, $patenteAut,$idR);
		}
		
	}




	


?>
