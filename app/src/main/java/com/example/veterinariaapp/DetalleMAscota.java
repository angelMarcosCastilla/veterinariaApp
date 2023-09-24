package com.example.veterinariaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetalleMAscota extends AppCompatActivity {

    TextView tvNombres, tvRaza, tvNombreAnimal, tvCliente, tvGeneroMascota, tvColorMascota;
    ImageView ivFotoMascota;
    int idmascota = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mascota);
        loadUI();
        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null){
            idmascota = parametros.getInt("idmascota");
           cargarDetalleMascotas();
        }
    }

    private  void loadUI(){
        tvNombres = findViewById(R.id.tvNombres);
        tvRaza = findViewById(R.id.tvRaza);
        tvNombreAnimal = findViewById(R.id.tvNombreAnimal);
        tvCliente = findViewById(R.id.tvCliente);
        tvGeneroMascota = findViewById(R.id.tvGeneroMascota);
        ivFotoMascota = findViewById(R.id.ivFotoMascotad);
        tvColorMascota = findViewById(R.id.tvColorMascota);
    }

    private void cargarDetalleMascotas(){
        Uri.Builder URLFull = Uri.parse(Utils.URL_MASCOTA).buildUpon();
        URLFull.appendQueryParameter("operacion", "detalleMascota");
        URLFull.appendQueryParameter("idmascota", String.valueOf(idmascota));

        String urlActualizada = URLFull.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActualizada, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("response", response);
                    tvNombres.setText(jsonObject.getString("nombre"));
                    tvRaza.setText(jsonObject.getString("nombreraza"));
                    tvNombreAnimal.setText(jsonObject.getString("nombreanimal"));
                    tvColorMascota.setText(jsonObject.getString("color"));
                    tvCliente.setText(jsonObject.getString("nombres") + " " + jsonObject.getString("apellidos"));
                    tvGeneroMascota.setText(jsonObject.getString("genero"));
                    cargarImagen(jsonObject.getString("fotografia"));
                }catch (Exception e){
                    Utils.showToast(getApplicationContext(), "Error en obtener Datos");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error en obtener Datos");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void cargarImagen(String name){
        ImageRequest imageRequest = new ImageRequest(Utils.BASE_URL_IMG + name, new Response.Listener<android.graphics.Bitmap>() {
            @Override
            public void onResponse(android.graphics.Bitmap response) {

                ivFotoMascota.setImageBitmap(response);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error alcargar imagen");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(imageRequest);
    }


}