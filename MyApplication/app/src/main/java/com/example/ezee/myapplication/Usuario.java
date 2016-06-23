package com.example.ezee.myapplication;


        import org.json.JSONException;
        import org.json.JSONObject;

public class Usuario {
    private String nombre;
    private String apellido;
    private String edad;
    private String telefono;
    private String url;

    public Usuario (String pNombre, String pApellido, String pEdad, String pTelefono) {
        this.nombre = pNombre;
        this.apellido = pApellido;
        this.edad = pEdad;
        this.telefono = pTelefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono (String telefono) {
        this.telefono = telefono;
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("nombre", getNombre());
            jsonObject.put("apellido", getApellido());
            jsonObject.put("edad", getEdad());
            jsonObject.put("telefono", getTelefono());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}

