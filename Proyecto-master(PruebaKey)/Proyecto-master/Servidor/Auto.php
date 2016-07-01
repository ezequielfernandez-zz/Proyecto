<?php
	$MY_SERVER = "localhost";
	$MY_DB_USER = "root";
	$MY_DB_PASS = "1234";
	$MY_DB = "appAndroid";
class Auto{

	
	function insertarAutoBD($idU,$modeloAut,$capacidadAut, $patenteAut){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	 
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
			$query= "INSERT INTO `autos` (`idU`, `patente`, `modelo`, `capacidad`)  VALUES ('$idU', '$patenteAut', '$modeloAut', '$capacidadAut')";
			$conexion->query($query);
			mysqli_close($conexion);
		}
	
	}
	
	function asociarAutoaRecital($idU, $patenteAut,$idR){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
			
			$query= "INSERT INTO `pone` (`idU`, `idR`, `patente`)  VALUES ('$idU', '$idR', '$patenteAut')";
			$respuesta=$conexion->query($query);
			mysqli_close($conexion);
		}
		
	}
	
	function obtenerAutos($artista, $ciudad, $genero, $fecha){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	 
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
			$artista=trim($artista);
			$ciudad=trim($ciudad);
			$genero=trim($genero);
			$fecha=trim($fecha);
			$query= "SELECT patente,modelo,capacidad FROM pone NATURAL JOIN recitales NATURAL JOIN autos WHERE artista='$artista' AND ciudad='$ciudad' AND genero='$genero' AND fecha='$fecha'";
			$resultado=$conexion->query($query);
			$respuesta=array();
		
			while($arreglo=$resultado->fetch_array()){
				$rowArray['patente']=$arreglo['patente'];
				$rowArray['modelo']=$arreglo['modelo'];
				$rowArray['capacidad']=$arreglo['capacidad'];
				array_push($respuesta,$rowArray);
			}
			
			mysqli_close($conexion);
			echo json_encode($respuesta);
		
		}
	}
	
	
}
?>