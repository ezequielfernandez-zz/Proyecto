<?php
	$MY_SERVER = "localhost";
	$MY_DB_USER = "root";
	$MY_DB_PASS = "1234";
	$MY_DB = "appAndroid";
 class Recital{
	
	function existe($conexion, $artista, $ciudad, $fecha){
		$query="SELECT * FROM recitales WHERE artista=$artista, ciudad=$ciudad, fecha=$fecha";
		 $resultado =$conexion->query($query);
		 //$arreglo=$resultado->fetch_array();
		if($resultado!=null){
			return 1;
			}
		else{
			return 0;
		}
	}
		
	function insertRecitalBD($nombre_art, $genero, $fecha, $ciudad){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
		
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		//Nos fijamos si el usuario ya es parte de la BD
		
		
		$rec= new Recital();
		$resultado =$rec->existe($conexion,$nombre_art, $ciudad, $fecha);
		if ($resultado == 0){
			mysqli_close($conexion);
			return 0;
		}
		else{
			// INSERTAMOS LO VALORES OBTENIDOS
			$query= "INSERT INTO `recitales` (`artista`, `genero`, `fecha`, `ciudad`)  VALUES ('$nombre_art', '$genero', '$fecha', '$ciudad')";
			$conexion->query($query);
			mysqli_close($conexion);
			return 1;
		}
		
	
	}
	function obtenerIDR($artista, $genero, $fecha, $ciudad){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	 
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
			$query="SELECT idR FROM recitales WHERE artista=$artista, ciudad=$ciudad, fecha=$fecha, genero=$genero";
			$resultado =$conexion->query($query);
			$arreglo=$resultado->fetch_array();			
			return $arreglo['idR'];
			
		}
	}
	
}

?>