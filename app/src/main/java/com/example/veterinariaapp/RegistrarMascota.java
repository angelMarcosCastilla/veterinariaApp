package com.example.veterinariaapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrarMascota extends AppCompatActivity {
    EditText  etNombreMascota, etColorMascotas;
    Button botonSubirFoto, btRegistrarMascota;
    ImageView ivMascota;
    RadioGroup rdGroupGeneroMascota;
    Spinner spAnimal, spRaza, spCliente;

    String  nombreMascota, colorMascota, imagenMascota, generoMascota;
    int idCliente = 0, idAnimal = 0, idRaza = 0;
    ArrayList<Item> listAnimal = new ArrayList<>();
    ArrayList<Item> listRaza = new ArrayList<>();
    ArrayList<Item> listCliente = new ArrayList<>();
    Bitmap bitmap;
    LinearLayout LyPropietario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mascota);
        loadUI();

        if(Utils.getRol().equals("D")){
            LyPropietario.setVisibility(View.GONE);
            idCliente = Integer.parseInt(Utils.getIdCliente());
        }else{
            getCLiente();
        }
        // obtenemos loas datos para los select

        getAnimal();
        getRazas();

        btRegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

        // evento cada ves que seleccionamos un item del spinner animal
        spAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Item item = (Item) parent.getSelectedItem();
                    idAnimal = item.getId();
                    getRazas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // evento cada ves que seleccionamos un item del spinner raza
        spRaza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Item item = (Item) parent.getSelectedItem();
                    idRaza = item.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // evento cada ves que seleccionamos un item del spinner cliente
        spCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Item item = (Item) parent.getSelectedItem();
                    idCliente = item.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // evento para subir la foto
        botonSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cargarImagen();
            }
        });
    }

    private String ConvertImageToBase64(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = imageBytes != null ? Base64.encodeToString(imageBytes, Base64.DEFAULT) : null;
        return encodedImage;
    }
    private  void cargarImagen(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona imagen"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivMascota.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void validateFields(){
        nombreMascota = etNombreMascota.getText().toString().trim();
        colorMascota = etColorMascotas.getText().toString().trim();
        generoMascota = rdGroupGeneroMascota.getCheckedRadioButtonId() == R.id.rdGroupGeneroMascota ? "M" : "H";
        imagenMascota = ivMascota.getDrawable() != null ? ConvertImageToBase64(bitmap) : "";;

        if(nombreMascota.isEmpty()){
            etNombreMascota.setError("Completa este campo");
            etNombreMascota.requestFocus();
        }else if(colorMascota.isEmpty()){
            etColorMascotas.setError("Completa este campo");
            etColorMascotas.requestFocus();
        }else if(idCliente == 0){
            Utils.showToast(getApplicationContext(), "Selecciona un cliente");
        }else if(idAnimal == 0){
            Utils.showToast(getApplicationContext(), "Selecciona un animal");

        }else if(idRaza == 0){
            Utils.showToast(getApplicationContext(), "Selecciona una raza");

        } else if (imagenMascota.isEmpty()) {
            Utils.showToast(getApplicationContext(), "Selecciona una imagen");
        } else{
            registrarMascota();
        }

    }

    private  void registrarMascota(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.URL_MASCOTA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Resetear campos
                resetFields();
                Utils.showToast(getApplicationContext(), "Mascota registrada correctamente");
                Intent intent = new Intent(getApplicationContext(), BuscarCliente.class);
                startActivity(intent);
                finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showToast(getApplicationContext(), "Error al registrar mascota ");
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("operacion","registrarmascota");
                parametros.put("nombre",nombreMascota);
                parametros.put("color",colorMascota);
                parametros.put("fotografia",imagenMascota);
                parametros.put("genero",generoMascota);
                parametros.put("idcliente",String.valueOf(idCliente));
                parametros.put("idraza",String.valueOf(idAnimal));

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void resetFields(){
        nombreMascota = "";
        colorMascota = "";
        imagenMascota = "";
        generoMascota = "";
        idAnimal = 0;
        idRaza = 0;
        // limpiamos los componentes
        etColorMascotas.setText("");
        etNombreMascota.setText("");
        spAnimal.setSelection(0);
        spRaza.setSelection(0);
        rdGroupGeneroMascota.check(R.id.rbMacho);
        ivMascota.setImageBitmap(null);
        if(!Utils.getRol().equals("D")){
            spCliente.setSelection(0);
            idCliente = 0;

        }
    }
    private void loadUI(){
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etColorMascotas = findViewById(R.id.etColorMascotas);
        botonSubirFoto = findViewById(R.id.botonSubirFoto);
        btRegistrarMascota = findViewById(R.id.btRegistrarMascota);
        ivMascota = findViewById(R.id.ivMascota);
        rdGroupGeneroMascota = findViewById(R.id.rdGroupGeneroMascota);
        spAnimal = findViewById(R.id.spAnimal);
        spRaza = findViewById(R.id.spRaza);
        spCliente = findViewById(R.id.spCliente);
        LyPropietario = findViewById(R.id.LyPropietario);
    }

    private void getRazas(){
        listRaza.clear();
        Uri.Builder URLFull = Uri.parse(Utils.URL_MASCOTA).buildUpon();
        URLFull.appendQueryParameter("operacion", "listarRazas");
        URLFull.appendQueryParameter("idanimal", String.valueOf(idAnimal));
        String urlActualizada = URLFull.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActualizada, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                       for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int idRaza = jsonObject.getInt("id");
                            String nombreRaza = jsonObject.getString("label");
                            listRaza.add(new Item(idRaza,nombreRaza));
                        }
                       ArrayAdapter<Item> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listRaza);
                        spRaza.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showToast(getApplicationContext(), "Error al cargar razas");
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getAnimal(){
        Uri.Builder URLFull = Uri.parse(Utils.URL_MASCOTA).buildUpon();
        URLFull.appendQueryParameter("operacion", "listarAnimales");
        String urlActualizada = URLFull.build().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActualizada, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listAnimal.add(new Item(jsonObject.getInt("id"), jsonObject.getString("label")));
                    }
                    ArrayAdapter<Item> adapterAnimal = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listAnimal);
                    spAnimal.setAdapter(adapterAnimal);
                }catch (Exception e){
                  e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showToast(getApplicationContext(), "Error al cargar razas");
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getCLiente(){
        Uri.Builder URLFull = Uri.parse(Utils.URL_CLIENTE).buildUpon();
        URLFull.appendQueryParameter("operacion", "listarCliente");
        String urlActualizada = URLFull.build().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActualizada, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.i("RESPONSE", jsonObject.getString("label"));

                        listCliente.add(new Item(jsonObject.getInt("id"), jsonObject.getString("label")));
                    }
                    ArrayAdapter<Item> adapterCliente = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listCliente);
                    spCliente.setAdapter(adapterCliente);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showToast(getApplicationContext(), "Error al cargar razas");
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

