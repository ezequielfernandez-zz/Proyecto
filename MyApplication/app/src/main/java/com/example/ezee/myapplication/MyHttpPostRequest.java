package com.example.ezee.myapplication;

/**
 * Created by ezee on 27/6/2016.
 */

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * MyHttpPostRequest => Esta clase privada maneja toda la gestión de envío de datos al servidor
 *
 */
public class MyHttpPostRequest extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        BufferedReader in = null;
        String baseUrl = params[0];
        String jsonData = params[1];

        try {
            //Creamos un objeto Cliente HTTP para manejar la peticion al servidor
            HttpClient httpClient = new DefaultHttpClient();
            //Creamos objeto para armar peticion de tipo HTTP POST
            HttpPost post = new HttpPost(baseUrl);

            //Configuramos los parametos que vamos a enviar con la peticion HTTP POST
            List<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
            nvp.add(new BasicNameValuePair("Usuario", jsonData));
            //post.setHeader("Content-type", "application/json");
            post.setEntity(new UrlEncodedFormEntity(nvp));

            //Se ejecuta el envio de la peticion y se espera la respuesta de la misma.
            HttpResponse response = httpClient.execute(post);

            //Obtengo el contenido de la respuesta en formato InputStream Buffer y la paso a formato String
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }

            in.close();

            return sb.toString();


        } catch (Exception e) {
            return "Exception happened: " + e.getMessage();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void onProgressUpdate(Integer... progress) {
        //Se obtiene el progreso de la peticion
    }

    protected void onPostExecute(String result) {
        //Se obtiene el resultado de la peticion Asincrona
    }
}

