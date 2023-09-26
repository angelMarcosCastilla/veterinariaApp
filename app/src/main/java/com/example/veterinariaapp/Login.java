package com.example.veterinariaapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText etPassword, etUsuario;

    TextView etRegistrarse;
    String password, usuario;
    Button btLogin;

  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadUI();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

        etRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrarCliente.class);
                Bundle bundle = new Bundle();
                bundle.putString("tiposuario", "D");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    private  void validateFields(){
        password = etPassword.getText().toString().trim();
        usuario = etUsuario.getText().toString().trim();

        if(password.isEmpty()){
            etPassword.setError("Completa este campo");
            etPassword.requestFocus();
        }else if(usuario.isEmpty()){
            etUsuario.setError("Completa este campo");
            etUsuario.requestFocus();
        }else{
            login();
        }
    }

    private  void login(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Utils.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success")){
                                JSONObject jsonObjectUser = jsonObject.getJSONObject("data");
                                String tipousuario = jsonObjectUser.getString("tipousuario");
                                Utils.setRol(tipousuario);
                                Utils.setDni(jsonObjectUser.getString("dni"));
                                Utils.setIdCliente(jsonObjectUser.getString("idcliente"));
                                if(tipousuario.equals("D")){
                                    Intent intent = new Intent(getApplicationContext(), BuscarCliente.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }else{
                                Utils.showToast(Login.this, jsonObject.getString("message"));
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros =  new HashMap<String, String>();
                parametros.put("operacion", "login");
                parametros.put("nombreusuario", usuario);
                parametros.put("claveacceso", password);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void loadUI(){
        etPassword = findViewById(R.id.etPassword);
        etUsuario = findViewById(R.id.etUsuario);
        btLogin = findViewById(R.id.btLogin);
        etRegistrarse = findViewById(R.id.etRegistrarse);
    }
}