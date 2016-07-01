
# CREACIÓN DE LA BASE DE DATOS.-

CREATE DATABASE IF NOT EXISTS appAndroid CHARACTER SET latin1 ;
USE appAndroid;

# DECLARACIÓN DE LAS TABLAS.-






DROP TABLE IF EXISTS `autos`;
CREATE TABLE `autos` (
	`idU` int UNSIGNED  NOT NULL,
	`patente` varchar(40)  NOT NULL,
	`modelo` varchar(40) NOT NULL,
	`capacidad` int NOT NULL,

	
	CONSTRAINT pk_patente
	PRIMARY KEY (`patente`),
	
	CONSTRAINT fk_usuarios_auto
	FOREIGN KEY(`idU`) REFERENCES usuarios(`id`)

	) ENGINE=InnoDB;
	
	
DROP TABLE IF EXISTS `pone`;
CREATE TABLE `pone` (
	`idU` int unsigned NOT NULL,
	`idR` int NOT NULL AUTO_INCREMENT,
	`patente` varchar(40)  NOT NULL,
	
	CONSTRAINT pk_inspectores
	PRIMARY KEY (`idU`,`idR`,`patente`),
	
	CONSTRAINT fk_usuarios_pone
	FOREIGN KEY(`idU`) REFERENCES usuarios(`id`),
	CONSTRAINT fk_recitales_pone
	FOREIGN KEY(`idR`) REFERENCES recitales(`idR`),
	CONSTRAINT fk_autos_pone
	FOREIGN KEY(patente) REFERENCES autos(`patente`)
	
	) ENGINE=InnoDB;
	
		DROP TABLE IF EXISTS `asiste`;
CREATE TABLE `asiste` (
	`idA` int unsigned NOT NULL,
	`idU` int unsigned NOT NULL,
	`idR` int NOT NULL AUTO_INCREMENT,
	
	CONSTRAINT	pk_asiste
	PRIMARY KEY (`idA`),
	CONSTRAINT fk_usuarios_asiste
	FOREIGN KEY(`idU`) REFERENCES usuarios(`id`),
	CONSTRAINT fk_recitales_asiste
	FOREIGN KEY(`idR`) REFERENCES recitales(`idR`)
	) ENGINE=InnoDB;

	

	
	
	
	


	
	


# Fin appAndroid.sql