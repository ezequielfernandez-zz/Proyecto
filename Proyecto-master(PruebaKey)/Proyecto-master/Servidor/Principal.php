<?php

$MY_SERVER = "localhost";
$MY_DB_USER = "root";
$MY_DB_PASS = "1234";
$MY_DB = "appAndroid";

getAllArticles();
echo intval("44");
function postNewArticle() {
	//$jsonStr = file_get_contents('php://input');
	if (isset($_POST['article'])) {
		$jsonStr = urldecode($_POST['article']);
		$jsonObj = json_decode($jsonStr);
		$strTitle = $jsonObj->{'title'};
		$strUrl = $jsonObj->{'url'};
		$strDescrip = $jsonObj->{'desc'};

		if ($strTitle != null && $strUrl != null && $strDescrip != null) {
			insertArticle($strTitle, $strUrl, $strDescrip);
			echo "OK";
		} else {
			echo "KO: Title: " . $strTitle ." URL: " . $strUrl . " or Desc: " . $strDescrip . " is not set ";
		}
	} else {
		echo "KO: Article Json Object not received";
	}
}

function insertArticle($strTitle, $strUrl, $strDescrip) {
	global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
	$conexion=mysql_connect($MY_SERVER,$MY_DB_USER,$MY_DB_PASS);
	$conecta=mysql_select_db($MY_DB);
 
	if(!$conexion) {
		echo "Fallo al conectarse con la base de datos";
    	exit();	
 	}
	if (!$conecta) {
		printf("Error al intentar abrir la base de datos");
		exit();
	}
	
	// INSERTAMOS LO VALORES OBTENIDOS
  	$resultado="INSERT INTO articles (title, url, descrip) VALUES ('$strTitle', '$strUrl', '$strDescrip')";
	mysql_query($resultado);
	if(!$resultado) {
		echo "no se pudo insertar ";
	}
	mysql_close($conexion);
}

function getAllArticles() {
	global $MY_SERVER, $MY_DB_USER, $MY_DB_PASS, $MY_DB;
	//ABRIMOS CONEXION CON BD y SELECCIONAMOS LA BD
	$conexion=new mysqli($MY_SERVER,$MY_DB_USER,$MY_DB_PASS,$MY_DB);
	//$conecta=mysql_select_db($MY_DB);
		include 'Usuario.php';
	if($conexion->connect_errno) {
		die("Fallo al conectarse con la base de datos");
    	exit();	
 	}
	$usu=new Usuario();
	$resultado = $usu->getNombre($conexion,1);
	$arreglo=$resultado->fetch_array();
	//echo "usuario ".$row['nombre'].
	"<br>";
	$id_res=$arreglo["nombre"];
	$apellido=$arreglo["apellido"];
	echo "$id_res <br />" ;
	
	echo "$apellido";
	/*
  	$query="SELECT * FROM article";
  	//EJECUTAMOS EL QUERY A LA BD
	if ($resultado =$conexion->query($query)){
		echo "entre aca <br>";
		while($row = $resultado->fetch_assoc()){
			echo "title  " .$row['title'].
			"<br>";
		}
		$resultado->close();
	}
	else{
		echo "no entré consulta";
	}
	/*
	if(!$resultado) {
		echo "fallo getAllArticles ";
	} else {
		//ARMAMOS LA RESPUESTA EN FORMATO JSON.
		$json_response = array();
		while ($row = mysql_fetch_array($resultado, MYSQL_ASSOC)) {
			$row_array['title'] = $row['title'];
			$row_array['url'] = $row['url'];
			$row_array['descrip'] = $row['descrip'];
        	//push de la fila en el arreglo matricial
        	array_push($json_response,$row_array);
    	}
    	//RETORNAMOS LA RESPUESTA
		echo json_encode($json_response);
		//die("json_response is => " . $json_response);
    	//print json_encode($json_response);
	}
	*/

	//CERRAMOS CONEXION CON BD
	$conexion->close();
}

?>
 