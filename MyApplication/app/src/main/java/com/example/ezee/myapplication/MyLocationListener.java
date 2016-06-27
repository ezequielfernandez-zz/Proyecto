package com.example.ezee.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ezee on 26/6/2016.
 */
public class MyLocationListener implements LocationListener {


    Subscriptor subscripto=null;

    @Override
    public void onLocationChanged(Location loc) {
        if(subscripto!=null) subscripto.Actualizar(loc);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es desactivado
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es activado
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Este metodo se ejecuta cada vez que se detecta un cambio en el
        // status del proveedor de localizacion (GPS)
        // Los diferentes Status son:
        // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
        // TEMPORARILY_UNAVAILABLE -> Temporalmente no disponible pero se
        // espera que este disponible en breve
        // AVAILABLE -> Disponible
    }


    public void Subscribirse(Subscriptor s){
        subscripto=s;
    }





}/* Fin de la clase localizacion */