<?php
// Ejemplo 1
	$fecha="20-03-1992";
	$date = new DateTime($fecha);
	$fecha =$date->format('Y-m-d');
	echo "$fecha";
?>