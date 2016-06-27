package com.example.ezee.myapplication;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;


public class TabHostNew extends ActionBarActivity {

    protected Bitmap foto;
    protected String nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_host_new);


        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        if (extras != null) {//ver si contiene datos
            foto=(Bitmap)extras.get("Foto");//Obtengo el nombre
            nombre=(String)extras.get("Nombre");
        }

        TextView e = (TextView)findViewById(R.id.LabelNombre);
        e.setText(nombre);

        ImageView i = (ImageView )findViewById(R.id.imageView);
        i.setImageBitmap(foto);


        TabHost tabHost= (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1=tabHost.newTabSpec("tab1");

        TabHost.TabSpec tab3=tabHost.newTabSpec("tab3");
        TabHost.TabSpec tab4=tabHost.newTabSpec("tab4");

        //que queremos que aparezca en las pestañas
        tab1.setIndicator("Inicio");
        //tab1.setIndicator("Inicio");//, getResources().getDrawable(R.drawable.home));
        tab1.setContent(R.id.Inicio);




        tab3.setIndicator("Agregar Recital");
        tab3.setContent(R.id.AgregarRecital);

        tab4.setIndicator("Perfil");
        tab4.setContent(R.id.Perfil);

        tabHost.addTab(tab1);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);


    }

    public void editar(View v){}
    public void Aceptar(View v) {
        EditText ar = (EditText) findViewById(R.id.artista);
        String artista = ar.getText().toString();

        EditText ciu = (EditText) findViewById(R.id.ciudad);
        String ciudad = ciu.getText().toString();

        EditText fe = (EditText) findViewById(R.id.fecha);
        String fecha = fe.getText().toString();

        Spinner gen = (Spinner) findViewById(R.id.genero);
        String genero = gen.getSelectedItem().toString();

        CheckBox asis = (CheckBox) findViewById(R.id.asiste);
        boolean asiste = asis.isChecked();

    }

    public void clic (View v) {
        finish();//Muere la actividad
    }

    public Bitmap enviarFoto(){

        return foto;
    }

    public String getNombre(){
        return nombre;
    }

}
