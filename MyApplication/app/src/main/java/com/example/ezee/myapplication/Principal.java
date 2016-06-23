package com.example.ezee.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }



    public void Perfil(View v){

    }



    public void AgregarRecital(View v){}

    public void Recitales(View v){
        Intent intent3 = new Intent(v.getContext(),MainActivity.class);
        startActivityForResult(intent3, 0);
    }

    public void Inicio(View v){}

}
