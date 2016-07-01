<?php
$MY_SERVER = "localhost";
$MY_DB_USER = "root";
$MY_DB_PASS = "1234";
$MY_DB = "appAndroid";
include_once "Recital.php";
 class Usuario{
	

	function insertUsuarioBD($id, $nombre_usu, $apellido_usu){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	 
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{//si se loguea por primera vez
			
			$query="INSERT INTO usuarios (id, nombre, apellido) VALUES ($id,'$nombre_usu', '$apellido_usu')";
			$resultado=$conexion->query($query);
			mysqli_close($conexion);
			return 1;
		}
	}

	function obtenerAutos($idU){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	 
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
	
			$query="SELECT patente FROM pone WHERE idU=$idU";
			$resultado=$conexion->query($query);
			mysqli_close($conexion);
			$respuesta=array();
			while($arreglo=$resultado->fetch_array()){
				$row_array['patente']=$arreglo['patente'];
				array_push($respuesta,$row_array);
			}
			return $respuesta;	
		}
	}
	
	function postTelefono($tel, $idU){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
			$query="UPDATE usuarios SET telefono='$tel' WHERE id='$idU'";
			$resultado=$conexion->query($query);
		
			mysqli_close($conexion);
		}
		
	}
	
	function getTelefono($idU){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
			
			$query="SELECT telefono FROM usuarios WHERE id='$idU'";
			$resultado=$conexion->query($query);
			$arreglo=$resultado->fetch_array();
			$resutaltado=$arreglo['telefono'];
			
			echo "Telefono $resutaltado";	
		
			mysqli_close($conexion);
		}
		
	}
	
	function asistirRecital($nombre_art, $genero, $fecha, $ciudad, $idU){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
		
			$rec=new Recital();
			$idR=$rec->obtenerIDR($nombre_art, $genero, $fecha, $ciudad);
			$idA=$idR+$idU;
			$query="INSERT INTO asiste (idA,idU,idR) VALUES ('$idA','$idU','$idR')";
			
			$resultado=$conexion->query($query);
			mysqli_close($conexion);
			echo"Confirmada asistencia";
			
		}
		
	}
}

?>