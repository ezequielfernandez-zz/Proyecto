<?php
$MY_SERVER = "localhost";
$MY_DB_USER = "root";
$MY_DB_PASS = "1234";
$MY_DB = "appAndroid";
include_once "Usuario.php";
 class Recital{
	
	function existe($conexion, $artista, $ciudad, $fecha){
		$query="SELECT * FROM recitales WHERE artista='$artista' AND ciudad='$ciudad'AND fecha='$fecha'";
		 $resultado =$conexion->query($query);	 
		 $arreglo=$resultado->fetch_array();
		if($arreglo!=null){
			return 0;
			}
		else{
			return 1;
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
			$artista=trim($artista);
			$ciudad=trim($ciudad);
			$genero=trim($genero);
			$fecha=trim($fecha);
			$query="SELECT * FROM recitales WHERE artista='$artista' AND ciudad='$ciudad' AND fecha='$fecha' AND genero='$genero'";
			$resultado =$conexion->query($query);
			$arreglo=$resultado->fetch_array();	
			mysqli_close($conexion);
			return $arreglo['idR'];
			
		}
	}
	
	function obtenerMisRecitales($idUsuario){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	 
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
			
			$query = "SELECT * FROM asiste NATURAL JOIN recitales WHERE idU='$idUsuario'";
			$resultado =$conexion->query($query);
			$respuesta=array();
			
			array_push($respuesta, "misRecitales");
			while($arreglo=$resultado->fetch_array()){
				
				$row_array['artista']=$arreglo['artista'];
				$row_array['genero']=$arreglo['genero'];
				$row_array['fecha']=$arreglo['fecha'];
				$row_array['ciudad']=$arreglo['ciudad'];
				array_push($respuesta,$row_array);
			}
			mysqli_close($conexion);
			
			echo json_encode($respuesta);
			
		}
		
	}
	
	function obtenerRecitales($idU, $gustos, $ubicacion){
		global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
		$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	 
		if($conexion->connect_errno) {
			die("Fallo al conectarse con la base de datos");
			exit();	
		}
		else{
			$query = "SELECT * FROM recitales";
			$resultado =$conexion->query($query);
			$aux=array();
			while($arreglo=$resultado->fetch_array()){
				$row_array['artista']=$arreglo['artista'];
				$row_array['genero']=$arreglo['genero'];
				$row_array['fecha']=$arreglo['fecha'];
				$row_array['ciudad']=$arreglo['ciudad'];
				array_push($aux,$row_array);
			}
			mysqli_close($conexion);
			$nombre="losRecitales";
			$primera=array();
			$segunda=array();
			$tercera=array();
			$ultima=array();
			
			$gustosUsu=explode(",",$gustos);
			$inserto=false;
			foreach($aux as $fila){
				foreach($gustosUsu as $artista){
					if($fila['artista']==$artista){
						if($fila['ciudad'] == $ubicacion){//recitales más importantes artista que le gusta y en su ciudad
							array_push($primera,$fila);
							$inserto=true;
						}
						else{// recitales con artista que le gusta
							array_push($segunda, $fila);
							$inserto=true;
						}
					}
					else{
						if($fila['ciudad'] == $ubicacion){// recitales en su ciudad
							array_push($tercera,$fila);
							$inserto=true;
						}
					}
				}
				if(!$inserto){
					array_push($ultima,$fila);
				}
				
			}
			//inserto los recitales de acuerdo a su importancia
			$respuesta=array();
			array_push($respuesta,$nombre);
			array_push($respuesta,$primera);
			array_push($respuesta,$segunda);
			array_push($respuesta,$tercera);
			array_push($respuesta,$ultima);
			
			$usu=new Usuario();
			$autos=$usu->obtenerAutos($idU);
			//inserto los autos del usuario
			array_push($respuesta,$autos);
			
			//respondo 
			echo json_encode($respuesta);
			
			
		}
	
	}
 }

?>