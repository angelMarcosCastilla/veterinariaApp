package com.example.veterinariaapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscarCliente extends AppCompatActivity {

    ListView lvListaMascotas;
    Button btBuscarCliente, btMascotaFromList;
    EditText etBuscarDni;
    String dni;
    LinearLayout layoutClient;
    private List<String> listMascota = new ArrayList<>();
    private List<Integer> listId = new ArrayList<>();
    private CustonAdapter adapter ;
    TextView tvNombreCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cliente);
        loadUI();

        if(Utils.getRol().equals("D")){
            layoutClient.setVisibility(View.GONE);
            cargarDetallesMascotas(Utils.getDni());
        }

        // evento para buscar cliente
        btBuscarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dni = etBuscarDni.getText().toString().trim();
                cargarDetallesMascotas(dni);
            }
        });

        lvListaMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                abrirActivityMascota(listId.get(position));
            }
        });

        // evento para registrar mascota
        btMascotaFromList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrarMascota.class);
                startActivity(intent);
            }
        });

    }

    private void abrirActivityMascota(int id){
        Intent intent = new Intent(this, DetalleMAscota.class);
        intent.putExtra("idmascota", id);
        startActivity(intent);
    }
    private void cargarDetallesMascotas(String dni){
        if(dni.isEmpty()){
            etBuscarDni.setError("Ingrese DNI");
            return;
        }
        Uri.Builder URLFull = Uri.parse(Utils.URL_CLIENTE).buildUpon();
        URLFull.appendQueryParameter("operacion", "buscarClienteMascotas");
        URLFull.appendQueryParameter("dni", dni);

        String urlActualizada = URLFull.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActualizada, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // limpiamos las listas
                    listMascota.clear();
                    listId.clear();
                    adapter = new CustonAdapter(getApplicationContext(), listMascota);
                    lvListaMascotas.setAdapter(adapter);
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length() == 0){
                        Utils.showToast(BuscarCliente.this, "No se encontraron datos");
                        tvNombreCliente.setText("");
                        return;
                    }

                    JSONObject cliente = jsonArray.getJSONObject(0);
                    tvNombreCliente.setText("Mascotas de " + cliente.getString("nombres") + " " + cliente.getString("apellidos"));

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listMascota.add(jsonObject.getString("nombre") + " - " + jsonObject.getString("nombreanimal"));
                        listId.add(jsonObject.getInt("idmascota"));
                    }

                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Utils.showToast(BuscarCliente.this, "Error al cargar los datos");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(BuscarCliente.this, "Error en la conexión");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void loadUI(){
        lvListaMascotas = findViewById(R.id.lvListaMascotas);
        btBuscarCliente = findViewById(R.id.btBuscarCliente);
        etBuscarDni = findViewById(R.id.etBuscarDNI);
        tvNombreCliente = findViewById(R.id.tvNombreCliente);
        layoutClient = findViewById(R.id.layoutClient);
        btMascotaFromList = findViewById(R.id.btMascotaFromList);
    }
}