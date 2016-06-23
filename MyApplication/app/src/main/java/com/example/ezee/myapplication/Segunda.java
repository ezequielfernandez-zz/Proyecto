package com.example.ezee.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.location.Location;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Segunda extends AppCompatActivity {

    private Location ubicacion;
    private boolean activado;
    String Text;
    String Estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_segunda);

  //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  //  setSupportActionBar(toolbar);

 /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    });*/
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    /* Uso de la clase LocationManager para obtener la localizacion del GPS */
    LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    MyLocationListener Local = new  MyLocationListener();
    Local.setActividad(this);
    try {
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
    }
    catch(SecurityException e){}
}

    public void Obtener(View v){

        if(ubicacion==null){
            Text="null";
        }
        else {
            Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + ubicacion.getLatitude() + "\n Long = " + ubicacion.getLongitude();
        }

        if (ubicacion.getLatitude() != 0.0 && ubicacion.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(ubicacion.getLatitude(), ubicacion.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address direccion = list.get(0);
                    Text=Text+" \n Estoy en: \n" + direccion.getAddressLine(0);
                    Text=Text+" \n "+ direccion.getLocality()+" , "+ direccion.getCountryName();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     //   ((TextView) findViewById(R.id.ubicacion)).setText(Text);
    //    ((TextView) findViewById(R.id.ubicacion)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);



        if(activado){
            Estado="El GPS esta activado";
        }
        else{
            Estado="El GPS esta desactivado";
        }

        //((TextView) findViewById(R.id.estado)).setText(Estado);

    }

    public void setLocation(Location loc){

        ubicacion=loc;


    }

    public void Activado(boolean activado){
        this.activado=activado;
    }

    public void Volver(View v){

        Intent intent2 = new Intent(v.getContext(), MainActivity.class);
        startActivityForResult(intent2, 0);
    }

    public void Login(View v){

        Intent intent3 = new Intent(v.getContext(), actividad_3.class);
        startActivityForResult(intent3, 0);
    }

}
