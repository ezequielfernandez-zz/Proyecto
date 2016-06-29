package com.example.ezee.myapplication;

/**
 * Created by ezee on 11/6/2016.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.textservice.SpellCheckerService;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class FragmentoLog extends Fragment implements Subscriptor,Notificador{


    private Location ubicacion;
    protected Usuario usuario;
    Intent intent;
    String edad;
    protected Bitmap f;
    private CallbackManager callbackManager;
    String Direccion, Ciudad, Pais;
    String Gustos="";
    boolean envioDatos=false;
    int i=0;
    String [] a={"h"};
    boolean logueado=false;
    AlertDialog alertDialog;
    String intereses;

    private FacebookCallback<LoginResult> callBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();

            usuario.setId(accessToken.getUserId());
            ObtenerFoto asyn = new ObtenerFoto();
            asyn.execute();
            try {
                asyn.get();
            } catch (Exception e) {
            }

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    usuario.getId()+"/likes/",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {

                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            String [] aux=graphResponse.toString().toString().split("name");
                            for(int i=0;i<aux.length;i++){
                                aux[i]=aux[i].replace('"',' ');
                                aux[i]=aux[i].replace(":", "");
                                aux[i]=aux[i].substring(0, aux[i].indexOf(','));
                                aux[i]=aux[i].replace("}", "");
                                aux[i]=aux[i].replace("]", "");
                                aux[i]=aux[i].replace(".","");
                                Gustos=Gustos+","+aux[i];
                            }
                        }
                    }).executeAsync();

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    usuario.getId()+"/music/",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {

                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            String [] aux=graphResponse.toString().toString().split("name");
                            for(int i=0;i<aux.length;i++){
                                aux[i]=aux[i].replace('"',' ');
                                aux[i]=aux[i].replace(":", "");
                                aux[i]=aux[i].substring(0, aux[i].indexOf(','));
                                aux[i]=aux[i].replace("}", "");
                                aux[i]=aux[i].replace("]", "");
                                aux[i]=aux[i].replace(".","");
                                Gustos=Gustos+","+aux[i];
                            }


                                envioDatos=true;
                                EnviarDatos();

                        }
                    }).executeAsync();


            Profile profile = Profile.getCurrentProfile();
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            String  birthday,likes;
                            try {
                                birthday = object.getString("birthday"); // 01/31/1980 format
                                birthday.replace("\\ ", "");
                                DateFormat format=new SimpleDateFormat("MM/dd/yyyy");
                                java.util.Date nacimiento=format.parse(birthday);
                                java.util.Date hoy=new java.util.Date();
                                long diferenciaEn_ms = hoy.getTime() - nacimiento.getTime();
                                long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
                                edad=String.valueOf(dias / 365);
                                EnviarUsuario();
                            } catch (Exception e) {
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "birthday,friends,actions.music");
            request.setParameters(parameters);
            request.executeAsync();





            usuario.setApellido(profile.getLastName());
            usuario.setNombre(profile.getFirstName());
            if(envioDatos) Notificar(intereses);

            if (profile != null) {
                logueado=true;
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario=new Usuario();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    public void Actualizar (Location l) {
        ubicacion = l;
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(ubicacion.getLatitude(), ubicacion.getLongitude(), 1);
            if (!list.isEmpty()) {
                Address direccion = list.get(0);
                Ciudad = direccion.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_log, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends", "user_likes","user_actions.music"));
        // If using in a fragment0
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, callBack);
    }


    class ObtenerFoto extends AsyncTask<String, Void, Usuario> {
        private Exception exception;
        Bitmap bitmap = null;

        protected Usuario doInBackground(String... params) {

            try {

                URL imageURL = new URL("https://graph.facebook.com/" + usuario.getId() + "/picture?type=large");
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                usuario.setFoto(bitmap);

            } catch (Exception e) {
            }
            return usuario;
        }

        protected void onPostExecute(Usuario u) {

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void EnviarUsuario() {
            JSONObject jsonObject= new JSONObject();
            try {
                jsonObject.put("nombre", usuario.getNombre());
                jsonObject.put("apellido", usuario.getApellido());
                jsonObject.put("edad", edad);
                jsonObject.put("telefono", "0");
                jsonObject.put("id", usuario.getId());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Obtenemos los datos del Articles en foramto JSON
            String strJson = jsonObject.toString();
            Log.d("viendo", strJson);
            //Se define la URL del servidor a la cual se enviarán lso datos
            //String baseUrl = "http://www.server.com/newarticle.php";
            String baseUrl = "postUsuario.php";

            //Se ejecuta la peticion Http POST empleando AsyncTAsk
            new MyHttpPostRequest().execute(baseUrl, strJson,"Usuario");
        EnviarDatos();

    }

    public void EnviarDatos() {

        //Obtenemos los datos del Articles en foramto JSON
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("ciudad", Ciudad);
            jsonObject.put("gustos", Gustos);
            //jsonObject.put("gustos", "asd");
            jsonObject.put("idU", usuario.getId());
            Log.d("viendo",jsonObject.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strJson = jsonObject.toString();
        //Se define la URL del servidor a la cual se enviarán lso datos
        //String baseUrl = "http://www.server.com/newarticle.php";
        String baseUrl = "inicio.php";

        //Se ejecuta la peticion Http POST empleando AsyncTAsk
        MyHttpPostRequest request=new MyHttpPostRequest();
        request.Subscribirse(this);
        request.execute(baseUrl, strJson,"Recital");
        envioDatos=true;

    }

    public void Notificar(String s){
        intereses=s;
        if(logueado) {
            intent = new Intent(getActivity(), TabHostNew.class);
            intent.putExtra("Foto", usuario.getFoto());
            intent.putExtra("Nombre", usuario.getNombre());
            intent.putExtra("Intereses", s);
            intent.putExtra("Gustos",Gustos);
            intent.putExtra("id", usuario.getId());
            //alertDialog.dismiss();
            startActivity(intent);
        }
    }

    public void open(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("");
        builder.setIcon(R.drawable.guitar);
        alertDialog = builder.create();
        alertDialog.show();


    }

}



