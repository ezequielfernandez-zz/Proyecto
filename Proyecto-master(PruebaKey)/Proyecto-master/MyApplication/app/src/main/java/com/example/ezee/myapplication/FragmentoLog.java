package com.example.ezee.myapplication;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FragmentoLog extends Fragment implements Subscriptor,Notificador{


    private Location ubicacion;
    protected Usuario usuario;
    Intent intent;
    private CallbackManager callbackManager;
    String Ciudad;
    String Gustos="";
    boolean envioDatos=false;
    int i=0;
    boolean logueado=false;
    AlertDialog alertDialog;
    String intereses;
    boolean puedoEnviar=false;
    boolean enviaUsuario=false;

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


            //Solicito los likes del usuario en facebook
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    usuario.getId()+getResources().getString(R.string.pedidoLikes)
                    ,
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

            //Solicito la musica que al usuario le gusta en facebook
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    usuario.getId()+getResources().getString(R.string.pedidoMusic),
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
                                Gustos=Validar(Gustos);
                                EnviarDatos();

                        }
                    }).executeAsync();

            Profile profile = Profile.getCurrentProfile();



            //Obtengo nombre y apellido del facebook
            usuario.setApellido(profile.getLastName());
            usuario.setNombre(profile.getFirstName());
            puedoEnviar=usuario.getId()!=null && Ciudad!=null;
            EnviarUsuario();

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

    //Recibe la ubicacion del usuario
    public void Actualizar (Location l) {
        ubicacion = l;
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(ubicacion.getLatitude(), ubicacion.getLongitude(), 1);
            if (!list.isEmpty()) {
                Address direccion = list.get(0);
                Ciudad = Validar(direccion.getLocality());
                puedoEnviar=usuario.getId()!=null && Ciudad!=null;
                if(!envioDatos) EnviarDatos();
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
                getResources().getString(R.string.perfilPublico),
                getResources().getString(R.string.mail),
                getResources().getString(R.string.birthday),
                getResources().getString(R.string.friends),
                getResources().getString(R.string.likes),
                getResources().getString(R.string.music)));
        // If using in a fragment0
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, callBack);
    }

    //Obtengo la foto de perfil del facebook
    class ObtenerFoto extends AsyncTask<String, Void, Usuario> {
        private Exception exception;
        Bitmap bitmap = null;

        protected Usuario doInBackground(String... params) {

            try {
                URL imageURL = new URL(getResources().getString(R.string.urlFotoPrimera) + usuario.getId() + getResources().getString(R.string.urlFotoSegunda));

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

    //Envia el usuario al servidor para agregarlo a la base de datos si el mismo no esta
    public void EnviarUsuario() {
        if(puedoEnviar) {
            enviaUsuario=true;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("nombre", usuario.getNombre());
                jsonObject.put("apellido", usuario.getApellido());
                jsonObject.put("id", Math.abs(usuario.getId().hashCode()));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Obtenemos los datos del usuario en foramto JSON
            String strJson = jsonObject.toString();
            Log.d("viendo", strJson);
            //Se define la ultima parte de la URL del servidor a la cual se enviarán los datos
            String baseUrl = "postUsuario.php";

            //Se ejecuta la peticion
            new MyHttpPostRequest().execute(baseUrl, strJson, "Usuario");
            //Se envian ademas los datos de ubicacion y gustos del usuario
        }

    }

    public void EnviarDatos() {
        if(puedoEnviar) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ciudad", Validar(Ciudad));
                jsonObject.put("gustos", Gustos);
                jsonObject.put("idU", Math.abs(usuario.getId().hashCode()));
                Log.d("viendo", jsonObject.toString());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String strJson = jsonObject.toString();

            String baseUrl = "inicio.php";

            MyHttpPostRequest request = new MyHttpPostRequest();
            //Se genera una subscripcion, para ser notificado de la respuesta en, ya que se usara la misma para armar
            //La lista de recitales que se le mostrara al usuario en la siguiente pantalla
            request.Subscribirse(this);
            request.execute(baseUrl, strJson, "Recital");
            envioDatos = true;
            if(!enviaUsuario) EnviarUsuario();
        }

    }

    public void Notificar(String s){
        intereses=s;
        if(logueado) {
            intent = new Intent(getActivity(), TabHostNew.class);
            intent.putExtra("Foto", usuario.getFoto());
            intent.putExtra("Nombre", usuario.getNombre());
            intent.putExtra("Intereses", s);
            intent.putExtra("Gustos", Gustos);
            intent.putExtra("id", Math.abs(usuario.getId().hashCode()));
            intent.putExtra("Ciudad",Ciudad);
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

    public String Validar(String s){

        final String ORIGINAL = getResources().getString(R.string.aReemplazar);
        final String REPLACEMENT =getResources().getString(R.string.reemplazarPor);

            if (s == null) {
                return null;
            }
            char[] array = s.toCharArray();
            for (int index = 0; index < array.length; index++) {
                int pos = ORIGINAL.indexOf(array[index]);
                if (pos > -1) {
                    array[index] = REPLACEMENT.charAt(pos);
                }
            }
            return new String(array);
        }



}



