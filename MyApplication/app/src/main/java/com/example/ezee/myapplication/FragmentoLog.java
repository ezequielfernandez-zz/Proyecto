package com.example.ezee.myapplication;

/**
 * Created by ezee on 11/6/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.impl.cookie.DateUtils;
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

public class FragmentoLog extends Fragment implements Subscriptor{


    private Location ubicacion;
    protected Usuario usuario;
    protected Bitmap f;
    private CallbackManager callbackManager;
    String Direccion, Ciudad, Pais;
    boolean envioUsuario=false;
    int i=0;

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

            Profile profile = Profile.getCurrentProfile();

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.d("LoginActivity", response.toString());
                            String  email;
                            String  birthday;
                            try {
                                email = object.getString("email");
                                birthday = object.getString("birthday"); // 01/31/1980 format
                                birthday.replace("\\ ", "");
                                DateFormat format=new SimpleDateFormat("MM/dd/yyyy");
                                java.util.Date nacimiento=format.parse(birthday);
                                java.util.Date hoy=new java.util.Date();
                                long diferenciaEn_ms = hoy.getTime() - nacimiento.getTime();
                                long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
                                usuario.setEdad(String.valueOf(dias/365));
                            } catch (Exception e) {
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
            usuario.setApellido(profile.getLastName());
            usuario.setNombre(profile.getFirstName());
            EnviarUsuario();
            if (profile != null) {

                Intent intent = new Intent(getActivity(), TabHostNew.class);
                intent.putExtra("Foto", usuario.getFoto());
                intent.putExtra("Nombre", profile.getFirstName());
                startActivity(intent);


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
                Direccion = direccion.getAddressLine(0);
                Ciudad = direccion.getLocality();
                Pais = direccion.getCountryName();
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
                "public_profile", "email", "user_birthday", "user_friends"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, callBack);
    }

    public void clic(View v) {
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
            //Obtenemos los datos del Articles en foramto JSON
            String strJson = usuario.toJSON();
            Log.d("usuario", strJson);
            //Se define la URL del servidor a la cual se enviarán lso datos
            //String baseUrl = "http://www.server.com/newarticle.php";
            String baseUrl = "http://192.168.0.166/aplicacion/postUsuario.php";

            //Se ejecuta la peticion Http POST empleando AsyncTAsk
            new MyHttpPostRequest().execute(baseUrl, strJson);

    }


    //OYENTE DEL BOTON "ENVIAR"
    public void processScreen(View v) {
        //Obtenemos los datos insertados por el usuario en pantalla


        //Instanciamos el objeto  con los datos insertados por el usuario


        //Llamamos la método sendNewArticle definido en el MainActivity
        //para Enviar los datos al servidor

        //sendNewUsuario();
    }

}



