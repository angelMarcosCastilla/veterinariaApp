package com.example.veterinariaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    Button btAbrirActRegistrar, btAbrirActBuscar, btAbrirActRegistrarMascota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        loadUI();

        btAbrirActRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RegistrarCliente.class);
            }
        });

        btAbrirActBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(BuscarCliente.class);
            }
        });

        btAbrirActRegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(RegistrarMascota.class);
            }
        });
    }

    private void openActivity(Class nameActivity){
        Intent intent = new Intent(getApplicationContext(), nameActivity);
        startActivity(intent);
    }
    private void loadUI(){
        btAbrirActBuscar = findViewById(R.id.btAbrirActBuscar);
        btAbrirActRegistrar = findViewById(R.id.btAbrirActRegistrar);
        btAbrirActRegistrarMascota = findViewById(R.id.btAbrirActRegistrarMascota);
    }
}