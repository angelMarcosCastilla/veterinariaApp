package com.example.veterinariaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // pasando 3 segundo se va al login
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(Login.class);
                finish();
            }
        }, 3000);
    }

    private void openActivity(Class nameActivity){
        Intent intent = new Intent(getApplicationContext(), nameActivity);
        startActivity(intent);
    }
}