package com.example.ezee.myapplication;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TabHostNew extends ActionBarActivity implements Notificador {
    protected String[] primerosRecitales;
    protected String[] segundosRecitales;
    protected String[] tercerosRecitales;
    protected String[] cuartosRecitales;
    protected String[] autosUsuario;
    String[] definitivo={""};


    protected Bitmap foto;
    protected String nombre;
    protected String intereses;
    protected String idU;
    protected String gustos;
    protected String ciudad;
    protected String ListaAutosRecital;
    protected  String autoQueEligio;
    EditText Modelo;
    EditText Capacidad;
    EditText Patente;

    EditText NuevaDescripcion;
    EditText Telefono;

    TextView Descripcion;
    TextView telefono;

    private ListView lv;
    private ListView lvMisReci;

    String recital;
    View vista;
    TabHost tabHost;

    boolean salio = false;

    AlertDialog Dialog,Dialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_host_new);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {//ver si contiene datos
            foto = (Bitmap) extras.get("Foto");//Obtengo el nombre
            nombre = (String) extras.get("Nombre");
            intereses = (String) extras.get("Intereses");
            idU = (String) extras.get("id");
            gustos = (String) extras.get("Gustos");
            ciudad= (String) extras.get("Ciudad");

        }
        PedirRecitales();
        Descripcion = (TextView) findViewById(R.id.descipcion);
        telefono = (TextView) findViewById(R.id.telefono);
        PedirTelefono();

        ArmarLista();

            lvMisReci = (ListView) findViewById(R.id.misreci);


            lv = (ListView) findViewById(R.id.intereses);

            lv.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, definitivo));

            if(!(definitivo.length==1)&&!definitivo[0].equals("")) {
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                        String eligio = list.getItemAtPosition(pos).toString();
                        EnviarRecital(eligio);
                        vista = v;
                    }
                });
            }






        TextView e = (TextView) findViewById(R.id.LabelNombre);
        e.setText(nombre);

        ImageView i = (ImageView) findViewById(R.id.imageView);
        i.setImageBitmap(foto);


        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                int i = tabHost.getCurrentTab();
                if (i == 0) {
                    Actualizar();
                }
            }
        });
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");

        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("tab4");

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

    public void EnviarRecital(String e) {
        //Obtenemos los datos del Articles en foramto JSON
        JSONObject jsonObject = new JSONObject();
        recital=e;
        String[] arreglo = e.split(",");
        try {
            //jsonObject.put("aux", e);
            jsonObject.put("artista", arreglo[0]);
            jsonObject.put("ciudad", arreglo[3]);
            jsonObject.put("fecha", arreglo[2]);
            jsonObject.put("genero", arreglo[1]);

        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        String strJson = jsonObject.toString();
        /*
        * Envio el recital elegido para obtener la lista de autos
        * */
        String baseUrl = "getAutos.php";

        //Se ejecuta la peticion Http POST empleando AsyncTAsk
        MyHttpPostRequest request = new MyHttpPostRequest();
        request.Subscribirse(this);
        request.execute(baseUrl, strJson, "Autos");
    }

    public void editar(View v) {
        abrir(v);
    }

    public void abrir(View v) {
        LayoutInflater inflater = getLayoutInflater();

        View dialoglayout = inflater.inflate(R.layout.dialogo_editar_perfil, null);

        NuevaDescripcion = (EditText) dialoglayout.findViewById(R.id.descr);
        Telefono = (EditText) dialoglayout.findViewById(R.id.telef);

        Button Aceptar = (Button) dialoglayout.findViewById(R.id.aceptaDesc);
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(getApplicationContext(), "Descripcion actualizada ", Toast.LENGTH_SHORT).show();
                    Descripcion.setText(NuevaDescripcion.getText().toString());
                    telefono.setText(Telefono.getText().toString());
                    EnviarTelefono();
                    Dialog.dismiss();
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Ingrese los datos ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button volver = (Button) dialoglayout.findViewById(R.id.VolverDesc);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Dialog.dismiss();
                } catch (NullPointerException e) {
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        Dialog = builder.show();
    }


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

        AgregarRecital(artista, ciudad, fecha, genero, asiste);

    }


    public void clic(View v) {
        salio = true;
        Button deslog = (Button) findViewById(R.id.salirF);
        Button sale = (Button) findViewById(R.id.salir);
        deslog.setVisibility(View.INVISIBLE);
        sale.setVisibility(View.VISIBLE);


    }

    public void Salir(View v) {

        if (salio) finish();


    }


    public Bitmap enviarFoto() {

        return foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void Notificar(String s) {

        if(!s.contains("refused")) {
            if (s.contains("losRecitales")) {
                intereses = s;
                ArmarLista();
                ListView lv = (ListView) findViewById(R.id.intereses);
                lv.setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, definitivo));
            } else {
                if(s.contains("Telefono")){
                    if(s.contains("NULL")) Telefono.setText("No ha agregado su telefono");
                    else telefono.setText(s.replace("Telefono",""));
                }
                else {
                    if(s.contains("misRecitales")){
                        s=s.replace("misRecitales","");
                        ArmarMisRecitales(s);
                    }
                    else {
                        //Recibo la lista de autos para el recital elegido
                        ListaAutosRecital = s;
                        //Abro el dialogo mostrando la informacion para el recital elegido
                        open(vista);
                    }
                }
            }
        }


    }

    public void open(View view) {

        LayoutInflater inflater = getLayoutInflater();
        ListaAutosRecital=ListaAutosRecital.replace('[', ' ');
        ListaAutosRecital=ListaAutosRecital.replace(']',' ');
        ListaAutosRecital=ListaAutosRecital.replace("OK","");
        if(ListaAutosRecital.length()<7) ListaAutosRecital="No hay autos disponibles";
        View dialoglayout = inflater.inflate(R.layout.dialogo, null);
        ListView lista = (ListView) dialoglayout.findViewById(R.id.disponibles);
        lista.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ListaAutosRecital.split(",")));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                String eligio = list.getItemAtPosition(pos).toString();
                Unirse(eligio);
                Dialog.dismiss();

            }
        });



        Modelo = (EditText) dialoglayout.findViewById(R.id.modelo);
        Capacidad = (EditText) dialoglayout.findViewById(R.id.capacidad);
        Patente = (EditText) dialoglayout.findViewById(R.id.patente);
        Button Asistir = (Button) dialoglayout.findViewById(R.id.asistir);
        Asistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Asistira();
                Dialog.dismiss();

            }
        });
        Button Ofrecer = (Button) dialoglayout.findViewById(R.id.ofrecer);
        Ofrecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Ofrecer ", Toast.LENGTH_SHORT).show();
                try {
                    String modelo = Modelo.getText().toString();
                    String capacidad = Capacidad.getText().toString();
                    String patente = Patente.getText().toString();
                    EnviarAuto(capacidad, modelo, patente);
                    Dialog.dismiss();
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), R.string.excep, Toast.LENGTH_SHORT).show();
                }


            }
        });




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        Dialog = builder.show();
    }

    public String getId(){
        return idU;
    }

    public void EnviarAuto(String capacity, String model, String pat) {
        //Obtenemos los datos del Articles en formato JSON
        JSONObject jsonObject = new JSONObject();
        String[] reci = recital.split(",");
        //String [] reci={"Bandana","Bahia Blanca","22/1/17","pop"};

        try {
            jsonObject.put("idU", idU);
            jsonObject.put("modelo", model);
            jsonObject.put("capacidad", capacity);
            jsonObject.put("patente", pat);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson = jsonObject.toString();
        //Se define la URL del servidor a la cual se enviarán lso datos
        //String baseUrl = "http://www.server.com/newarticle.php";
        String baseUrl = "postAuto.php";

        //Se ejecuta la peticion Http POST empleando AsyncTAsk
        MyHttpPostRequest request = new MyHttpPostRequest();
        request.MostrarToast(getApplicationContext());
        request.execute(baseUrl, strJson, "Auto");

    }

    public void Unirse(String pat) {
        //Obtenemos los datos del Articles en formato JSON
        JSONObject jsonObject = new JSONObject();
        String[] reci = recital.split(",");
        //String [] reci={"Bandana","Bahia Blanca","22/1/17","pop"};

        try {
            jsonObject.put("artista", reci[0]);
            jsonObject.put("ciudad", reci[1]);
            jsonObject.put("fecha", reci[2]);
            jsonObject.put("genero", reci[3]);
            jsonObject.put("idU", idU);
            jsonObject.put("patente", pat);
            jsonObject.toString();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson = jsonObject.toString();
        //Se define la URL del servidor a la cual se enviarán lso datos
        //String baseUrl = "http://www.server.com/newarticle.php";
        String baseUrl = "unirse.php";

        //Se ejecuta la peticion Http POST empleando AsyncTAsk
        MyHttpPostRequest request = new MyHttpPostRequest();
        request.MostrarToast(getApplicationContext());
        request.execute(baseUrl, strJson, "Unirse");

    }

    public void Asistira() {
        //Obtenemos los datos del Articles en formato JSON
        JSONObject jsonObject = new JSONObject();
        String[] reci = recital.split(",");


        try {
            jsonObject.put("artista", reci[0]);
            jsonObject.put("ciudad", reci[3]);
            jsonObject.put("fecha", reci[2]);
            jsonObject.put("genero", reci[1]);
            jsonObject.put("idU", idU);
            jsonObject.toString();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson = jsonObject.toString();
        //Se define la URL del servidor a la cual se enviarán lso datos
        //String baseUrl = "http://www.server.com/newarticle.php";
        String baseUrl = "asistira.php";

        //Se ejecuta la peticion Http POST empleando AsyncTAsk
        MyHttpPostRequest request = new MyHttpPostRequest();
        request.MostrarToast(getApplicationContext());
        request.execute(baseUrl, strJson, "Asiste");

    }

    public void AgregarRecital(String artista, String ciudad, String fecha, String genero, boolean asiste) {
        //Obtenemos los datos del recital en formato JSON

        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("artista", artista);
            jsonObject1.put("ciudad", ciudad);
            jsonObject1.put("fecha", fecha);
            jsonObject1.put("genero", genero);
            jsonObject1.toString();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson1 = jsonObject1.toString();
        Log.d("viendo", "envio: " + strJson1);
        //Se define la URL del servidor a la cual se enviarán lso datos
        //String baseUrl = "http://www.server.com/newarticle.php";
        String baseUrl1 = "postRecital.php";

        //Se ejecuta la peticion Http POST empleando AsyncTAsk
        MyHttpPostRequest request1 = new MyHttpPostRequest();
        request1.MostrarToast(getApplicationContext());
        request1.execute(baseUrl1, strJson1, "AgregarRecital");

        if (asiste) {
            JSONObject jsonObject2 = new JSONObject();

            try {
                jsonObject2.put("artista", artista);
                jsonObject2.put("ciudad", ciudad);
                jsonObject2.put("fecha", fecha);
                jsonObject2.put("genero", genero);
                jsonObject2.put("idU", idU);
                jsonObject2.toString();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String strJson2 = jsonObject2.toString();
            //Se define la URL del servidor a la cual se enviarán lso datos
            //String baseUrl = "http://www.server.com/newarticle.php";
            String baseUrl2 = "asistira.php";

            //Se ejecuta la peticion Http POST empleando AsyncTAsk
            MyHttpPostRequest request2 = new MyHttpPostRequest();
            request2.MostrarToast(getApplicationContext());
            request2.execute(baseUrl2, strJson2, "Asiste");
        }
    }

    public void Actualizar() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ciudad", "bahia");//ciudad);
            jsonObject.put("gustos", gustos);
            jsonObject.put("gustos", "asd");
            jsonObject.put("idU", idU);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson = jsonObject.toString();

        String baseUrl = "inicio.php";

        MyHttpPostRequest request = new MyHttpPostRequest();
        request.Subscribirse(this);
        request.execute(baseUrl, strJson, "Recital");

    }

    public void EnviarTelefono(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idU", idU);
            jsonObject.put("telefono", Telefono.getText().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson = jsonObject.toString();

        String baseUrl = "postTelefono.php";


        MyHttpPostRequest request = new MyHttpPostRequest();
        request.execute(baseUrl, strJson, "Telefono");
    }

    public void PedirTelefono(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idU", idU);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson = jsonObject.toString();

        String baseUrl = "getTelefono.php";

        MyHttpPostRequest request = new MyHttpPostRequest();
        request.Subscribirse(this);
        request.execute(baseUrl, strJson, "Telefono");
    }

    public void PedirRecitales(){
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("idU", idU);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson = jsonObject.toString();
        String baseUrl = "misRecitales.php";

        MyHttpPostRequest request = new MyHttpPostRequest();
        request.Subscribirse(this);

        request.execute(baseUrl, strJson, "mRecital");
    }

    public void ArmarMisRecitales(String s){
        s = s.replace(']',' ');
        s = s.replace('}',' ');
        s = s.replace('"',' ');
        s = s.replace('[',' ');
        s = s.replace('{',' ');
        s = s.replace('"',' ');


        String [] recis = s.split("artista");

        for (int i = 1; i < recis.length; i++) {
            recis[i] = recis[i].replace(":", "");
            recis[i] = recis[i].replace("ciudad", "");
            recis[i] = recis[i].replace("genero", "");
            recis[i] = recis[i].replace("fecha", "");
            recis[i] = recis[i].replace('"', ' ');
            recis[i] = recis[i].replace('[', ' ');
            recis[i] = recis[i].replace(']', ' ');
            recis[i] = recis[i].replace('{', ' ');
            recis[i] = recis[i].replace('}', ' ');

        }

        lvMisReci.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, recis));

    }

    public void ArmarLista() {

        intereses=intereses.replace("losRecitales","");
        Log.d("viendo","intereses: "+intereses);
        String[] aux = intereses.split("]");
        boolean conexion=true;
        boolean listaVacia=false;
        if(intereses.contains("refused")) conexion=false;
        if(!intereses.contains(("artista"))) listaVacia=true;



        if(!listaVacia && conexion){
            //aux[0]=artista&ciudad , aux[1]=artista, aux[2]=ciudad, aux[3]=otros, aux[4]=vacio, aux[5]=autos

            primerosRecitales = aux[0].split("artista");
            segundosRecitales = aux[1].split("artista");
            tercerosRecitales = aux[2].split("artista");
            cuartosRecitales = aux[3].split("artista");
            autosUsuario = aux[5].split(",");


            for (int i = 0; i < primerosRecitales.length; i++) {
                primerosRecitales[i] = primerosRecitales[i].replace(":", "");
                primerosRecitales[i] = primerosRecitales[i].replace("ciudad", "");
                primerosRecitales[i] = primerosRecitales[i].replace("genero", "");
                primerosRecitales[i] = primerosRecitales[i].replace("fecha", "");
                primerosRecitales[i] = primerosRecitales[i].replace('"', ' ');
                primerosRecitales[i] = primerosRecitales[i].replace('[', ' ');
                primerosRecitales[i] = primerosRecitales[i].replace(']', ' ');
                primerosRecitales[i] = primerosRecitales[i].replace('{', ' ');
                primerosRecitales[i] = primerosRecitales[i].replace('}', ' ');

            }
            for (int i = 0; i < segundosRecitales.length; i++) {
                segundosRecitales[i] = segundosRecitales[i].replace(":", "");
                segundosRecitales[i] = segundosRecitales[i].replace("ciudad", "");
                segundosRecitales[i] = segundosRecitales[i].replace("genero", "");
                segundosRecitales[i] = segundosRecitales[i].replace("fecha", "");
                segundosRecitales[i] = segundosRecitales[i].replace('"', ' ');
                segundosRecitales[i] = segundosRecitales[i].replace('[', ' ');
                segundosRecitales[i] = segundosRecitales[i].replace(']', ' ');
                segundosRecitales[i] = segundosRecitales[i].replace('{', ' ');
                segundosRecitales[i] = segundosRecitales[i].replace('}', ' ');
            }
            for (int i = 0; i < tercerosRecitales.length; i++) {
                tercerosRecitales[i] = tercerosRecitales[i].replace(":", "");
                tercerosRecitales[i] = tercerosRecitales[i].replace("ciudad", "");
                tercerosRecitales[i] = tercerosRecitales[i].replace("genero", "");
                tercerosRecitales[i] = tercerosRecitales[i].replace("fecha", "");
                tercerosRecitales[i] = tercerosRecitales[i].replace('"', ' ');
                tercerosRecitales[i] = tercerosRecitales[i].replace('[', ' ');
                tercerosRecitales[i] = tercerosRecitales[i].replace(']', ' ');
                tercerosRecitales[i] = tercerosRecitales[i].replace('{', ' ');
                tercerosRecitales[i] = tercerosRecitales[i].replace('}', ' ');

            }
            for (int i = 0; i < cuartosRecitales.length; i++) {
                cuartosRecitales[i] = cuartosRecitales[i].replace(":", "");
                cuartosRecitales[i] = cuartosRecitales[i].replace("ciudad", "");
                cuartosRecitales[i] = cuartosRecitales[i].replace("genero", "");
                cuartosRecitales[i] = cuartosRecitales[i].replace("fecha", "");
                cuartosRecitales[i] = cuartosRecitales[i].replace('"', ' ');
                cuartosRecitales[i] = cuartosRecitales[i].replace('[', ' ');
                cuartosRecitales[i] = cuartosRecitales[i].replace(']', ' ');
                cuartosRecitales[i] = cuartosRecitales[i].replace('{', ' ');
                cuartosRecitales[i] = cuartosRecitales[i].replace('}', ' ');

            }

            int a = primerosRecitales.length + segundosRecitales.length + tercerosRecitales.length + cuartosRecitales.length;
            String[] arreglo = new String[a];

            int puntero = 0;

            int cantidad = 0;
            for (
                    int i = 0;
                    i < primerosRecitales.length; i++)

            {
                if (primerosRecitales[i].lastIndexOf(",") > 3) {
                    //Es un elemento valido
                    if (primerosRecitales[i].length() - primerosRecitales[i].lastIndexOf(",") < 9) {
                        primerosRecitales[i] = primerosRecitales[i].substring(0, primerosRecitales[i].lastIndexOf(","));
                    }
                    arreglo[puntero] = primerosRecitales[i];
                    cantidad++;
                    puntero++;
                }
            }

            for (
                    int i = 0;
                    i < segundosRecitales.length; i++)

            {
                if (segundosRecitales[i].lastIndexOf(",") > 3) {
                    //Es un elemento valido
                    if (segundosRecitales[i].length() - segundosRecitales[i].lastIndexOf(",") < 9) {
                        segundosRecitales[i] = segundosRecitales[i].substring(0, segundosRecitales[i].lastIndexOf(","));
                    }
                    arreglo[puntero] = segundosRecitales[i];
                    cantidad++;
                    puntero++;
                }
            }

            for (
                    int i = 0;
                    i < tercerosRecitales.length; i++)

            {
                if (tercerosRecitales[i].lastIndexOf(",") > 3) {
                    //Es un elemento valido
                    if (tercerosRecitales[i].length() - tercerosRecitales[i].lastIndexOf(",") < 9) {
                        tercerosRecitales[i] = tercerosRecitales[i].substring(0, tercerosRecitales[i].lastIndexOf(","));
                    }
                    arreglo[puntero] = tercerosRecitales[i];
                    cantidad++;
                    puntero++;
                }
            }

            for (
                    int i = 0;
                    i < cuartosRecitales.length; i++)

            {
                if (cuartosRecitales[i].lastIndexOf(",") > 3) {
                    //Es un elemento valido
                    if (cuartosRecitales[i].length() - cuartosRecitales[i].lastIndexOf(",") < 9) {
                        cuartosRecitales[i] = cuartosRecitales[i].substring(0, cuartosRecitales[i].lastIndexOf(","));
                    }
                    arreglo[puntero] = cuartosRecitales[i];
                    cantidad++;
                    puntero++;
                }
            }

            definitivo = new String[cantidad];
            for (int i = 0; i < cantidad; i++) {
                definitivo[i] = arreglo[i];
            }
        }
    }


}
